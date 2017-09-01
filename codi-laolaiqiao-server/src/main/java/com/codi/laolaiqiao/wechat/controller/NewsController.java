package com.codi.laolaiqiao.wechat.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codi.base.domain.BaseResult;
import com.codi.laolaiqiao.domain.WechatNews;
import com.codi.laolaiqiao.resultModel.UserResult;
import com.codi.laolaiqiao.service.WechatNewsService;
import com.codi.laolaiqiao.wechat.thirdparty.WechatSupportImpl;
import com.codi.laolaiqiao.wechat.thirdparty.model.NewsItem;
import com.codi.laolaiqiao.wechat.thirdparty.model.NewsResult;

/**
 * 
 * @author song-jj
 */
@Controller
@RequestMapping(value = "news")
public class NewsController extends WechatSupportImpl {

    @Resource(name="wechatNewsService")
    private WechatNewsService wechatNewsService;
    
    /**
     * 保存顶级菜单
     * @param request
     * @param response
     * @param news
     * @return
     */
    @RequestMapping(value = "/saveWelcome", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult saveUser(HttpServletRequest request, HttpServletResponse response, @ModelAttribute WechatNews news) {
        UserResult result = new UserResult();
        try {
            wechatNewsService.saveNews(news);
        } catch (Exception e) {
            result.setErrorMessage("应用错误");
            result.setSuccess(false);
            e.printStackTrace();
        }
        
        return result;
    }
    
    /**
     * 根据名称搜索图文消息
     * @param request
     * @param response
     * @param keyword
     */
    @RequestMapping(value = "/getNewsByName", method = RequestMethod.GET)
    @ResponseBody
    public NewsResult getNewsByName(HttpServletRequest request, HttpServletResponse response, String keyword) {
        NewsResult result = new NewsResult();
        try {
            // 根据名称搜索图文消息
            List<NewsItem> news = getMaterialByName(keyword);
            result.setNewsItems(news);
        } catch (Exception e) {
            result.setErrorMessage("应用错误");
            result.setSuccess(false);
            e.printStackTrace();
        }
        return result;
    }
    
    
}
