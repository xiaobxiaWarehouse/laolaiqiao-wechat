package com.codi.laolaiqiao.wechat.controller;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.codi.laolaiqiao.utils.Global;
import com.codi.laolaiqiao.wechat.thirdparty.WechatSupportImpl;
import com.github.sd4324530.fastweixin.api.entity.MenuButton;

/**
 * 后台管理注册Controller
 * @author 
 */
@Controller
@RequestMapping(value="index")
public class RegisterController extends WechatSupportImpl {

    /**
     * 进入后台注册首页
     * @return
     */
    @RequestMapping(value = "/login")
    public ModelAndView index2() {
        ModelAndView view = new ModelAndView();
        view.setViewName("screen/login");
        view.addObject(Global.VIEW_PROP_TITLE, "注册|金色家园");
        return view;
    }
    
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response) {
    	String userName = request.getParameter("username");
    	String password = request.getParameter("password");
    	
    	if ("laolaiqiao".equals(userName) && "llq961".equals(password)) {
    		ModelAndView view = new ModelAndView();
            view.setViewName("screen/back/top");
            List<MenuButton> topMenus = getMenuButtons();
            view.addObject("topMenus", topMenus);
            view.addObject("menu", "top");
            return view;
    	} else {
			return new ModelAndView("forward:/index/login");
		}
    }
}
