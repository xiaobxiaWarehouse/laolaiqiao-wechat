package com.codi.laolaiqiao.wechat.thirdparty.model;

import java.util.List;

import com.codi.base.domain.BaseResult;
import com.github.sd4324530.fastweixin.api.entity.MenuButton;

/**
 * 
 * @author song-jj
 */
public class ClickMenuResult extends BaseResult {

    private static final long serialVersionUID = 1L;
    
    private List<MenuButton> menuButtons;
    
    private List<NewsItem> newsItems;

    public List<MenuButton> getMenuButtons() {
        return menuButtons;
    }

    public void setMenuButtons(List<MenuButton> menuButtons) {
        this.menuButtons = menuButtons;
    }

    public List<NewsItem> getNewsItems() {
        return newsItems;
    }

    public void setNewsItems(List<NewsItem> newsItems) {
        this.newsItems = newsItems;
    }
    
}
