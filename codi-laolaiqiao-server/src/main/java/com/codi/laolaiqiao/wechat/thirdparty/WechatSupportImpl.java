package com.codi.laolaiqiao.wechat.thirdparty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.codi.laolaiqiao.domain.WechatNews;
import com.codi.laolaiqiao.service.WechatNewsService;
import com.codi.laolaiqiao.utils.Global;
import com.codi.laolaiqiao.wechat.thirdparty.model.NewsItem;
import com.codi.laolaiqiao.wechat.thirdparty.model.SubMenuButton;
import com.github.sd4324530.fastweixin.api.MaterialAPI;
import com.github.sd4324530.fastweixin.api.MenuAPI;
import com.github.sd4324530.fastweixin.api.MessageAPI;
import com.github.sd4324530.fastweixin.api.OauthAPI;
import com.github.sd4324530.fastweixin.api.config.ApiConfig;
import com.github.sd4324530.fastweixin.api.entity.Menu;
import com.github.sd4324530.fastweixin.api.entity.MenuButton;
import com.github.sd4324530.fastweixin.api.enums.MaterialType;
import com.github.sd4324530.fastweixin.api.enums.MenuType;
import com.github.sd4324530.fastweixin.api.enums.ResultType;
import com.github.sd4324530.fastweixin.api.response.GetMaterialListResponse;
import com.github.sd4324530.fastweixin.api.response.GetUserInfoResponse;
import com.github.sd4324530.fastweixin.api.response.OauthGetTokenResponse;
import com.github.sd4324530.fastweixin.exception.WeixinException;
import com.github.sd4324530.fastweixin.message.Article;
import com.github.sd4324530.fastweixin.message.NewsMsg;
import com.github.sd4324530.fastweixin.message.req.EventType;
import com.github.sd4324530.fastweixin.servlet.WeixinSupport;
import com.github.sd4324530.fastweixin.util.CollectionUtil;
import com.github.sd4324530.fastweixin.util.MessageUtil;
import com.github.sd4324530.fastweixin.util.SignUtil;

import lombok.extern.slf4j.Slf4j;

public class WechatSupportImpl extends WeixinSupport {
    private static final String token = Global.getConfig("token");
    private static final String appId = Global.getConfig("appId");
    private static final String appSecret = Global.getConfig("appSecret");
    private static final String appOpenId = Global.getConfig("appOpenId");
    private static final String appOpenSecret = Global.getConfig("appOpenSecret");
    private static ApiConfig config = null;
    private static ApiConfig openConfig = null;
    private static MessageAPI messageApi = null;
    private static MenuAPI menuApi = null;
    private static MaterialAPI materialAPI = null;
    private static OauthAPI oauthAPI = null;
    
    @Resource(name="wechatNewsService")
    private WechatNewsService wechatNewsService;

    @Override
    protected String getToken() {
        return token;
    }

    protected String getUserImageUrl(String code) {
        GetUserInfoResponse userInfoResponse = getUserInfoByCode(code);
        if (userInfoResponse == null) {
            return null;
        }
        return userInfoResponse.getHeadimgurl();
    }

    protected boolean updateMenuButtonInfo(String oldMenuName, String newName, String url, MenuType menuType) {
        Menu menu = getMenuApi().getMenu().getMenu();
        MenuButton menuButton = getMenuButton(menu, oldMenuName);

        if (menuButton != null) {
            menuButton.setKey(UUID.randomUUID().toString());
            menuButton.setUrl(url); // URL
            menuButton.setName(newName); // 菜单名称
            menuButton.setType(menuType); // 菜单类型
            // 如果是跳转外链，一定要确保下面没有子菜单
            if (MenuType.VIEW.equals(menuType)) {
                menuButton.setSubButton(new ArrayList<MenuButton>());
            }
            rebuildMenu(menu);

            return true;
        } else {
            return false;
        }
    }

    /**
     * 更新子菜单
     * 
     * @param oldMenuName
     * @param newName
     * @param url
     * @param menuType
     * @param oldParentName
     * @param parentName
     * @return
     */
    protected boolean updateSubMenuButton(String oldMenuName, String newName, String url, String oldParentName,
            String parentName) {
        Menu menu = getMenuApi().getMenu().getMenu();

        if (menu == null) {
            return false;
        }
        
        List<MenuButton> menuButtons = menu.getButton();

        if (menuButtons == null) {
            return false;
        }

        MenuButton subButton = new MenuButton();
        subButton.setKey(UUID.randomUUID().toString());
        subButton.setName(newName);
        subButton.setType(MenuType.VIEW);
        subButton.setUrl(url);
        int removeIndex = -1;
        for (MenuButton menuButton : menuButtons) {
            // 从旧的列表中删除
            if (menuButton.getName().equals(oldParentName)) {
                removeIndex = removeSubButton(menuButton.getSubButton(), oldMenuName);
            }

            // 添加到新列表中原来的位置
            if (menuButton.getName().equals(parentName) && removeIndex != -1) {
                menuButton.getSubButton().add(removeIndex, subButton);
            }
        }

        rebuildMenu(menu);

        return true;
    }

    public int removeSubButton(List<MenuButton> subButtons, String oldName) {
        int index = -1;
        if (CollectionUtils.isEmpty(subButtons)) {
            return index;
        }

        
        for (MenuButton menuButton : subButtons) {
            index++;
            if (menuButton.getName().equals(oldName)) {
                subButtons.remove(menuButton);
                break;
            }
        }
        return index;
    }

    /**
     * 删除菜单
     * 
     * @param newName
     * @return
     */
    protected boolean deleteMenuButton(String name) {
        Menu menu = getMenuApi().getMenu().getMenu();
        int count = 0;
        for (MenuButton button : menu.getButton()) {
            if (name.equals(button.getName())) {
                menu.getButton().remove(button);
                count++;
                break;
            }
        }
        rebuildMenu(menu);
        return count == 0;
    }

    /**
     * 删除子菜单
     * 
     * @param newName
     * @return
     */
    protected boolean deleteSubMenuButton(String menuName) {
        Menu menu = getMenuApi().getMenu().getMenu();
        int count = 0;
        for (MenuButton button : menu.getButton()) {
            if (CollectionUtils.isEmpty(button.getSubButton())) {
                continue;
            }

            // fastweixin导致type为空，需要重新设置下
            if (button.getType() == null) {
                button.setType(MenuType.CLICK);
                button.setKey(UUID.randomUUID().toString());
            }
            
            for (MenuButton subButton : button.getSubButton()) {
                if (menuName.equals(subButton.getName())) {
                    button.getSubButton().remove(subButton);
                    count++;
                    break;
                }
            }
        }
        rebuildMenu(menu);
        return count == 0;
    }

    // 创建一级菜单
    protected void createMenuButton(MenuButton button) {
        Menu menu = getMenuApi().getMenu().getMenu();
        if (menu == null) {
            menu = new Menu();
            List<MenuButton> buttons = new ArrayList<MenuButton>();
            menu.setButton(buttons);
        }
        button.setKey(UUID.randomUUID().toString());
        menu.getButton().add(button);
        rebuildMenu(menu);
    }

    private void rebuildMenu(Menu menu) {
        Menu oldMenu = getMenuApi().getMenu().getMenu();
        getMenuApi().deleteMenu();
        
        ResultType resultType = getMenuApi().createMenu(menu);
        if (resultType == null) {
            // 异常的时候，菜单重建
            getMenuApi().createMenu(oldMenu);
            throw new WeixinException("微信异常");
        } else if (!resultType.equals(ResultType.SUCCESS)) {
            // 异常的时候，菜单重建
            getMenuApi().createMenu(oldMenu);
            throw new WeixinException(resultType.getDescription());
        }
    }

    // 创建二级菜单
    protected boolean createSubMenuButton(SubMenuButton button) {
        Menu menu = getMenuApi().getMenu().getMenu();

        List<MenuButton> menuButtons = menu.getButton();

        if (menuButtons != null) {
            for (MenuButton menuButton : menuButtons) {
                if (menuButton.getType() == null) {
                    menuButton.setType(MenuType.CLICK);
                }
                if (menuButton.getKey() == null) {
                    menuButton.setKey(UUID.randomUUID().toString());
                }
                if (menuButton.getName().equals(button.getParentName())) {
                    menuButton.getSubButton().add(button);
                }
            }

            rebuildMenu(menu);

            return true;
        } else {
            return false;
        }
    }

    private MenuButton getMenuButton(Menu menu, String menuName) {
        List<MenuButton> menuButtons = menu.getButton();

        if (menuButtons != null) {
            for (MenuButton menuButton : menuButtons) {
                if (menuButton.getName().equals(menuName)) {
                    return menuButton;
                }

                List<MenuButton> subButtons = menuButton.getSubButton();

                if (subButtons != null) {
                    for (MenuButton button : subButtons) {
                        if (button.getName().equals(menuName)) {
                            return button;
                        }
                    }
                }
            }
        }

        return null;
    }

    /**
     * 取得微信公众号下面的菜单按钮
     * 
     * @return
     */
    protected List<MenuButton> getMenuButtons() {
        Menu menu = getMenuApi().getMenu().getMenu();
        if (menu == null) {
            return null;
        }
        List<MenuButton> buttons = menu.getButton();
        for (MenuButton menuButton : buttons) {
            if (menuButton.getType() == null) {
                menuButton.setType(MenuType.CLICK);
            }
        }
        return buttons;
    }

    /**
     * 取得全部的子菜单按钮
     * 
     * @return
     */
    protected List<SubMenuButton> getSubButtons() {
        Menu menu = getMenuApi().getMenu().getMenu();
        if (menu == null) {
            return null;
        }
        // 取得全部的顶部菜单按钮
        List<MenuButton> topMenuButtons = menu.getButton();
        //
        if (CollectionUtils.isEmpty(topMenuButtons)) {
            return null;
        }

        List<SubMenuButton> subMenuButtons = new ArrayList<SubMenuButton>();
        for (MenuButton menuButton : topMenuButtons) {
            // 没有子菜单
            if (CollectionUtils.isEmpty(menuButton.getSubButton())) {
                continue;
            }
            for (MenuButton subMenuButton : menuButton.getSubButton()) {
                SubMenuButton sub = new SubMenuButton();
                BeanUtils.copyProperties(subMenuButton, sub);
                sub.setParentName(menuButton.getName());
                subMenuButtons.add(sub);
            }

        }
        return subMenuButtons;
    }

    protected void pushMessageByEventType(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/xml;charset=UTF-8");

        // 获取微信返回消息体map
        Map<String, Object> reqMap = MessageUtil.parseXml(request, getToken(), getAppId(), getAESKey());

        String eventType = (String) reqMap.get("Event");
        fromUserName = (String) reqMap.get("FromUserName");
        toUserName = (String) reqMap.get("ToUserName");

        // 关注公众号回复事件
        if (EventType.SUBSCRIBE.equals(eventType)) {
            pushMessageBySubscribeEventType();
        }

        // 投票点击回复事件
        // if (EventType.CLICK.equals(eventType)) {
        // pushMessageByClickEventType();
        // }
    }

    private void pushMessageBySubscribeEventType() {
        WechatNews welcomeNews = wechatNewsService.getWelcomeNews();
        NewsMsg msg = new NewsMsg();
        msg.setToUserName(toUserName);
        msg.setMsgType("news");

        Article article = new Article();
        article.setTitle(welcomeNews == null ? "欢迎关注我们" : welcomeNews.getNewsTitle());
        article.setDescription(welcomeNews == null ? "您好，欢迎关注金色家园" : welcomeNews.getNewsDescription());
        article.setPicUrl(welcomeNews == null ? 
                "https://mmbiz.qlogo.cn/mmbiz_png/Q0mKfhP2eaRc68iccYbZLFD9EvZQVdm5b2nQrs83VlgoHFcHsKib8nZ9iajxaCTAIzPMHAVXvNKnM6AaYnbvX8bibA/0?wx_fmt=png"
                : welcomeNews.getNewsPicurl());
        article.setUrl(welcomeNews == null ? "http://llq.cd121.com/codi-laolaiqiao-wechat/codi-laolaiqiao-server/wechat/test": welcomeNews.getNewsUrl());

        msg.setArticles(CollectionUtil.newArrayList(article));

        getMessageApi().sendCustomMessage(fromUserName, msg);
    }

    /**
     * 取得所有的图文消息
     * 
     * @return
     */
    public List<NewsItem> getAllMaterial() {
        List<NewsItem> newsMapList = new ArrayList<NewsItem>();
        int offset = 0;
        int pageCount = 10;
        List<Map<String, Object>> allNews = new ArrayList<Map<String, Object>>();
        // 取得前十条
        GetMaterialListResponse materialList = getMaterialAPI().batchGetMaterial(MaterialType.NEWS, offset,
                pageCount);
        
        // 添加前十条
        allNews.addAll(materialList.getItems());

        if (allNews.size() == 0) {
            return newsMapList;
        }

        NewsItem news = null;
        for (Map<String, Object> map : allNews) {
            news = new NewsItem();
            Map<String, Object> contentMap = (Map<String, Object>) map.get("content");
            List<Map<String, Object>> newsItem = (List<Map<String, Object>>) contentMap.get("news_item");
            String title = "";
            Map<String, Object> itemMap = newsItem.get(0);
            
            // 图文标题
            if (itemMap != null && itemMap.get("title") != null) {
                title = itemMap.get("title").toString();
            }
            
            // 图文ID
            news.setMediaId(map.get("media_id").toString());
            news.setTitle(title);
            String url = "";
            
            // 图文地址
            if (itemMap != null && itemMap.get("url") != null) {
                url = itemMap.get("url").toString();
            }
            news.setUrl(url);
            
            // 图文页面图片地址
            String thumbUrl = "";
            if (itemMap != null && itemMap.get("thumb_url") != null) {
                thumbUrl = itemMap.get("thumb_url").toString();
            }
            news.setThumbUrl(thumbUrl);
            
            // 摘要
            String digest = "";
            if (itemMap != null && itemMap.get("digest") != null) {
                digest = itemMap.get("digest").toString();
            }
            news.setDigest(digest);
            
            newsMapList.add(news);
        }

        return newsMapList;
    }
    
    public List<NewsItem> getMaterialByName(String keyword) {
        List<NewsItem> materials = new ArrayList<NewsItem>();
        List<NewsItem> allMaterials = getAllMaterial();
        if (CollectionUtils.isEmpty(allMaterials)) {
            return materials;
        }
        for (NewsItem newsItem : allMaterials) {
            if (newsItem.getTitle().startsWith(keyword)) {
                materials.add(newsItem);
            }
        }
        
        return materials;
    }

    private ApiConfig getConfig() {
        return new ApiConfig(appId, appSecret);
    }

    private ApiConfig getOpenConfig() {
        return new ApiConfig(appOpenId, appOpenSecret);
    }

    private MessageAPI getMessageApi() {
        return new MessageAPI(getConfig());
    }

    private MenuAPI getMenuApi() {
        return new MenuAPI(getConfig());
    }

    public MaterialAPI getMaterialAPI() {
        return new MaterialAPI(getConfig());
    }

    private OauthAPI getOauthAPI() {
        return new OauthAPI(getOpenConfig());
    }

    protected GetUserInfoResponse getUserInfoByCode(String code) {
        GetUserInfoResponse userInfoResponse;
		try {
			OauthGetTokenResponse response = getOauthAPI().getToken(code);
			String accessToken = response.getAccessToken();
			String openId = response.getOpenid();
			userInfoResponse = getOauthAPI().getUserInfo(accessToken, openId);
			
			return userInfoResponse;
		} catch (Exception e) {
			return null;
		}
    }
}
