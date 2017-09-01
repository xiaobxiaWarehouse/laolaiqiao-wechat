package com.codi.laolaiqiao.wechat.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.apache.http.HttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.codi.laolaiqiao.domain.User;
import com.codi.laolaiqiao.resultModel.UserResult;
import com.codi.laolaiqiao.service.UserImageService;
import com.codi.laolaiqiao.service.UserService;
import com.codi.laolaiqiao.utils.Global;
import com.codi.laolaiqiao.wechat.thirdparty.WechatSupportImpl;
import com.github.sd4324530.fastweixin.api.response.GetUserInfoResponse;

@Controller
@RequestMapping(value = "/wechat")
@Slf4j
public class WechatCoreController extends WechatSupportImpl {
	@Resource(name="userService")
    private UserService userService;
	
	@Resource(name="userImageService")
    private UserImageService userImageService;
	
	private static String redirectUrl = Global.getConfig("redirectUrl");
	
    @RequestMapping(value = "/core", method = RequestMethod.GET)
    @ResponseBody
    protected final void bind(HttpServletRequest request, HttpServletResponse response) throws IOException{
        if (isLegal(request)) {
        	response.getWriter().write(request.getParameter("echostr"));
        	response.getWriter().flush();
        } else {
        	response.getWriter().write("");
        	response.getWriter().flush();
        }
    }
    
    @RequestMapping(value = "/core", method = RequestMethod.POST)
    protected final void process(HttpServletRequest request, HttpServletResponse response) {
    	if (isLegal(request)) {
    		try {
				pushMessageByEventType(request, response);
			} catch (Exception e) {
			}
    	} 
    }
    
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    protected final ModelAndView test(HttpServletRequest request, HttpServletResponse response) {
    	return new ModelAndView(redirectUrl);
    }
    
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    protected final ModelAndView index(HttpServletRequest request, HttpServletResponse response) {
    	String code = request.getParameter("code");
    	ModelAndView view = new ModelAndView();
    	
    	//对code进行安全参数限制
    	if (code != null) {
    		GetUserInfoResponse userInfoResponse = getUserInfoByCode(code);
    		
    		if (userInfoResponse != null) {
//    			view.addObject("userInfoResponse", userInfoResponse);
    			String openId = userInfoResponse.getOpenid();
    			request.getSession().setAttribute("userOpenId", openId);
    			request.getSession().setAttribute("userImageUrl", userInfoResponse.getHeadimgurl());
    			UserResult userResult = userService.getUseInfo(openId);
    			userResult.setImageUrl(userInfoResponse.getHeadimgurl());
    			// 如果用户不存在，则跳到注册
    			if (userResult.getUser() == null) {
    			    userImageService.saveUserImage(openId, userInfoResponse.getHeadimgurl());
    			    view.setViewName("redirect:/user/register?openId=" + openId);
    				
    				// 如果用户存在，则跳到详情页面
    			} else {
    			    // 更新用户头像
    			    userImageService.saveUserImage(openId, userInfoResponse.getHeadimgurl());
    			    
    		        view.setViewName("redirect:/user/getUserInfo?openId=" + openId);
				}
    		} 
    	} else {
    		view.setViewName("screen/register");
    	}
    	
    	return view;
    }
}
