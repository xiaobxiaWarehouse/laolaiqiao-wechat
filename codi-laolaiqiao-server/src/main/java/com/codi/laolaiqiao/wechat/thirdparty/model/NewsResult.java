package com.codi.laolaiqiao.wechat.thirdparty.model;

import java.util.List;

import com.codi.base.domain.BaseResult;
import com.github.sd4324530.fastweixin.api.entity.MenuButton;

/**
 * 图文结果
 * @author song-jj
 */
public class NewsResult extends BaseResult {

    private static final long serialVersionUID = 1L;
    
    private List<NewsItem> newsItems;
    
    private MenuButton menuButton;

    public List<NewsItem> getNewsItems() {
        return newsItems;
    }

    public void setNewsItems(List<NewsItem> newsItems) {
        this.newsItems = newsItems;
    }

    public MenuButton getMenuButton() {
        return menuButton;
    }

    public void setMenuButton(MenuButton menuButton) {
        this.menuButton = menuButton;
    }
    
}
