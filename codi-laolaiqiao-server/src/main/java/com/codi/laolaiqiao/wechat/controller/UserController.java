package com.codi.laolaiqiao.wechat.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.codi.laolaiqiao.domain.User;
import com.codi.laolaiqiao.domain.UserImage;
import com.codi.laolaiqiao.model.UserEx;
import com.codi.laolaiqiao.resultModel.UserResult;
import com.codi.laolaiqiao.service.ConstantService;
import com.codi.laolaiqiao.service.UserImageService;
import com.codi.laolaiqiao.service.UserService;
import com.codi.laolaiqiao.utils.Global;
import com.github.sd4324530.fastweixin.api.response.GetUserInfoResponse;

/**
 * 用户信息Controller
 * 
 * @author song-jj
 */
@Controller
@RequestMapping(value = "user")
public class UserController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "constantService")
    private ConstantService constantService;

    @Resource(name = "userImageService")
    private UserImageService userImageService;

    /**
     * 进入注册首页
     * 
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView index(@RequestParam String openId) {
        ModelAndView view = new ModelAndView();
        view.setViewName("screen/register");
        view.addObject(Global.VIEW_PROP_TITLE, "注册|金色家园");
        view.addObject("openId", openId);

        // 兴趣爱好
        view.addObject("hobbies", constantService.getHobbies());

        return view;
    }

    /**
     * 取得用户信息
     * 
     * @param request
     * @param response
     * @param userId
     * @return
     */
    @RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
    public ModelAndView getUserInfo(HttpServletRequest request, HttpServletResponse response,
            @RequestParam String openId) {
        UserResult result = userService.getUseInfo(openId);
        ModelAndView view = new ModelAndView();
        view.setViewName("screen/user");
        view.addObject(Global.VIEW_PROP_TITLE, "我的资料|金色家园");

        User user = result.getUser();
        view.addObject("user", user);

        UserImage image = userImageService.getUserImageByOpenId(openId);
        if (image != null) {
            user.setImageUrl(image.getImageUrl());
        }

        return view;
    }

    /**
     * 修改用户信息
     * 
     * @param request
     * @param response
     * @param userId
     * @return
     */
    @RequestMapping(value = "/turnToModify", method = RequestMethod.GET)
    public ModelAndView modifyUser(HttpServletRequest request, HttpServletResponse response,
            @RequestParam String openId) {
        UserResult result = userService.getUseInfo(openId);
        ModelAndView view = new ModelAndView();
        view.setViewName("screen/modifyUser");
        view.addObject(Global.VIEW_PROP_TITLE, "修改个人资料|金色家园");
        view.addObject("user", result.getUser());
        return view;
    }

    /**
     * 保存用户信息
     * 
     * @param request
     * @param response
     * @param user
     * @return
     */
    @RequestMapping(value = "/saveUser", method = RequestMethod.POST)
    @ResponseBody
    public UserResult saveUser(HttpServletRequest request, HttpServletResponse response, @ModelAttribute User user) {
        UserResult result = new UserResult();
        try {
            result = userService.saveUser(user);
        } catch (Exception e) {
            logger.error("保存出错");
            result.setSuccess(false);
            result.setErrorMessage(e.getMessage());
        }

        return result;
    }

    /**
     * 修改用户兴趣爱好
     * 
     * @param request
     * @param response
     * @param userId
     * @return
     */
    @RequestMapping(value = "/changeUserHobby", method = RequestMethod.GET)
    public ModelAndView changeUserHobby(HttpServletRequest request, HttpServletResponse response,
            @RequestParam String openId) {
        UserResult result = userService.getUseInfo(openId);
        ModelAndView view = new ModelAndView();
        view.setViewName("screen/changeHobby");
        view.addObject(Global.VIEW_PROP_TITLE, "修改爱好|金色家园");
        view.addObject("user", result.getUser());

        // 兴趣爱好
        view.addObject("hobbies", constantService.getHobbies());

        return view;
    }

    /**
     * 保存用户信息
     * 
     * @param request
     * @param response
     * @param keyword 搜索关键词
     * @param pageIndex 第几页
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    public UserResult search(HttpServletRequest request, HttpServletResponse response, String keyword,
            Integer pageIndex) {
        UserResult result = new UserResult();
        try {
            UserEx userEx = userService.queryPage(10, pageIndex, keyword);
            result.setUsers(userEx.getUser());
            result.setPageCount(userEx.getPageCount());
            result.setRowCount(userEx.getRowCount());
        } catch (Exception e) {
            logger.error("应用错误");
            result.setSuccess(false);
            result.setErrorMessage(e.getMessage());
        }

        return result;
    }
}
