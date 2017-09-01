package com.codi.laolaiqiao.wechat.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.codi.base.domain.BaseResult;
import com.codi.laolaiqiao.domain.WechatNews;
import com.codi.laolaiqiao.service.WechatNewsService;
import com.codi.laolaiqiao.utils.Global;
import com.codi.laolaiqiao.wechat.thirdparty.WechatSupportImpl;
import com.codi.laolaiqiao.wechat.thirdparty.model.ClickMenuResult;
import com.codi.laolaiqiao.wechat.thirdparty.model.NewsItem;
import com.codi.laolaiqiao.wechat.thirdparty.model.NewsResult;
import com.codi.laolaiqiao.wechat.thirdparty.model.SubMenuButton;
import com.github.sd4324530.fastweixin.api.entity.MenuButton;
import com.github.sd4324530.fastweixin.api.enums.MenuType;
import com.github.sd4324530.fastweixin.exception.WeixinException;

/**
 * 后台菜单管理
 * 
 * @author song-jj
 */
@Controller
@RequestMapping(value = "menu")
public class MenuController extends WechatSupportImpl {
    
    @Resource(name="wechatNewsService")
    private WechatNewsService wechatNewsService;
    
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
    		request.getSession().setAttribute("user_login_session", userName);
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
    
    
    /**
     * 显示顶级菜单
     * 
     * @return
     */
    @RequestMapping(value = "/showTopMenu")
    public ModelAndView showTopMenu() {
        ModelAndView view = new ModelAndView();
        view.setViewName("screen/back/top");
        List<MenuButton> topMenus = getMenuButtons();
        view.addObject("topMenus", topMenus);
        view.addObject("menu", "top");
        return view;
    }

    /**
     * 显示子菜单
     * 
     * @return
     */
    @RequestMapping(value = "/showSubMenu", method = RequestMethod.GET)
    public ModelAndView showSubMenu() {
        ModelAndView view = new ModelAndView();
        view.setViewName("screen/back/sub");
        List<SubMenuButton> subMenus = getSubButtons();
        view.addObject("subMenus", subMenus);
        view.addObject("menu", "sub");
        return view;
    }
    
    /**
     * 显示子菜单
     * 
     * @return
     */
    @RequestMapping(value = "/showUserStatistics", method = RequestMethod.GET)
    public ModelAndView showUserStatistics() {
        ModelAndView view = new ModelAndView();
        view.setViewName("screen/back/userStatistics");
        view.addObject("menu", "userStatistics");
        return view;
    }

    /**
     * 取得可点击的父菜单
     * 
     * @return
     */
    @RequestMapping(value = "/getTopClickMenu", method = RequestMethod.GET)
    @ResponseBody
    public BaseResult getTopClickMenu() {
        ClickMenuResult result = new ClickMenuResult();
        List<MenuButton> clickMenuList = new ArrayList<MenuButton>();
        result.setMenuButtons(clickMenuList);
        
        try {
            List<MenuButton> topMenus = getMenuButtons();
            if (CollectionUtils.isEmpty(topMenus)) {
                return result;
            }

            for (MenuButton menuButton : topMenus) {
                if (MenuType.CLICK.equals(menuButton.getType())) {
                    clickMenuList.add(menuButton);
                }
            }
            
            List<NewsItem> newsItems = getAllMaterial();
            result.setNewsItems(newsItems);
        } catch (WeixinException e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setErrorMessage(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setErrorMessage("应用错误");
        }
        return result;
    }

    /**
     * 保存顶级菜单
     * 
     * @param request
     * @param response
     * @param user
     * @return
     */
    @RequestMapping(value = "/createTopMenu", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult saveTop(HttpServletRequest request, HttpServletResponse response, String name, String url,
            String type) {
        BaseResult result = new BaseResult();
        try {
            MenuButton button = new MenuButton();
            button.setName(name);
            if (MenuType.CLICK.toString().equals(type)) {
                button.setType(MenuType.CLICK);
            } else if (MenuType.VIEW.toString().equals(type)) {
                button.setType(MenuType.VIEW);
            }
            button.setUrl(url);
            createMenuButton(button);
        } catch (WeixinException e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setErrorMessage(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setErrorMessage("应用错误");
        }

        return result;
    }

    /**
     * 更新一级菜单
     * 
     * @param request
     * @param response
     * @param user
     * @return
     */
    @RequestMapping(value = "/updateTopMenu", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult updateTopMenu(HttpServletRequest request, HttpServletResponse response, String oldName,
            String name, String url, String type) {
        BaseResult result = new BaseResult();
        try {
            // MenuType menuType = null;
            MenuType menuType = null;
            if (MenuType.CLICK.toString().equals(type)) {
                menuType = MenuType.CLICK;
            } else if (MenuType.VIEW.toString().equals(type)) {
                menuType = MenuType.VIEW;
            }
            updateMenuButtonInfo(oldName, name, url, menuType);
        } catch (WeixinException e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setErrorMessage(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setErrorMessage("应用错误");
        }
        return result;
    }

    /**
     * 保存顶级菜单
     * 
     * @param request
     * @param response
     * @param user
     * @return
     */
    @RequestMapping(value = "/createSubMenu", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult createSubMenu(HttpServletRequest request, HttpServletResponse response, String parentName,
            String name, String url, String type) {
        BaseResult result = new BaseResult();
        try {
            SubMenuButton button = new SubMenuButton();
            button.setName(name);
            button.setType(MenuType.VIEW);
            button.setUrl(url);
            button.setParentName(parentName);
            createSubMenuButton(button);
        } catch (WeixinException e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setErrorMessage(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setErrorMessage("应用错误");
        }

        return result;
    }

    /**
     * 更新二级菜单
     * 
     * @param request
     * @param response
     * @param user
     * @return
     */
    @RequestMapping(value = "/updateSubMenu", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult updateSubMenu(HttpServletRequest request, HttpServletResponse response, String oldName,
            String name, String url, String oldParentName, String parentName) {
        BaseResult result = new BaseResult();
        try {
            updateSubMenuButton(oldName, name, url, oldParentName, parentName);
        } catch (WeixinException e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setErrorMessage(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setErrorMessage("应用错误");
        }
        return result;
    }

    /**
     * 取得图文消息数据
     * 
     * @param request
     * @param response
     * @param me
     * @return
     */
    @RequestMapping(value = "/getMenuData", method = RequestMethod.GET)
    @ResponseBody
    public NewsResult getMenuData(HttpServletRequest request, HttpServletResponse response) {
        NewsResult result = new NewsResult();
        try {
            List<NewsItem> newsItems = getAllMaterial();
            result.setNewsItems(newsItems);
        } catch (WeixinException e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setErrorMessage(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setErrorMessage("应用错误");
        }
        return result;
    }

    /**
     * 保存二级菜单
     * 
     * @param request
     * @param response
     * @param user
     * @return
     */
    @RequestMapping(value = "/deleteTopMenu", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult deleteTopMenu(HttpServletRequest request, HttpServletResponse response, String menuName) {
        BaseResult result = new BaseResult();
        try {
            deleteMenuButton(menuName);
        } catch (WeixinException e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setErrorMessage(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setErrorMessage("应用错误");
        }
        return result;
    }

    /**
     * 保存二级菜单
     * 
     * @param request
     * @param response
     * @param user
     * @return
     */
    @RequestMapping(value = "/deleteSubMenu", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult deleteSubMenu(HttpServletRequest request, HttpServletResponse response, String menuName) {
        BaseResult result = new BaseResult();
        try {
            deleteSubMenuButton(menuName);
        } catch (WeixinException e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setErrorMessage(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setErrorMessage("应用错误");
        }
        return result;
    }
    
    /**
     * 显示顶级菜单
     * 
     * @return
     */
    @RequestMapping(value = "/showWelcomeNews", method = RequestMethod.GET)
    public ModelAndView showWelcomeNews() {
        ModelAndView view = new ModelAndView();
        view.setViewName("screen/back/welcomeNews");
        WechatNews news = wechatNewsService.getWelcomeNews();
        view.addObject("news", news);
        view.addObject("menu", "wechatRegister");
        return view;
    }
    
    /**
     * 显示顶级菜单
     * 
     * @return
     */
    @RequestMapping(value = "/showNewsSearch", method = RequestMethod.GET)
    public ModelAndView newsSearch() {
        ModelAndView view = new ModelAndView();
        view.setViewName("screen/back/newsSearch");
        view.addObject("menu", "showNewsSearch");
        return view;
    }
}
