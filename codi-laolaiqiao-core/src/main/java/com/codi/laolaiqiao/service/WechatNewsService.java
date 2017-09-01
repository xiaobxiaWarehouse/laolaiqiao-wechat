package com.codi.laolaiqiao.service;

import com.codi.laolaiqiao.domain.WechatNews;

/**
 * 
 * @author song-jj
 */
public interface WechatNewsService {

    WechatNews getWelcomeNews();
    
    void saveNews(WechatNews news);
}
