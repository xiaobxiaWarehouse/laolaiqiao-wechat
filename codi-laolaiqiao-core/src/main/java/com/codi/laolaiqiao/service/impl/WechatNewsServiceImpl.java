package com.codi.laolaiqiao.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.codi.laolaiqiao.dao.WechatNewsDao;
import com.codi.laolaiqiao.domain.WechatNews;
import com.codi.laolaiqiao.service.WechatNewsService;

/**
 * 
 * @author song-jj
 */
@Service("wechatNewsService")
@Transactional
public class WechatNewsServiceImpl implements WechatNewsService {
    
    @Resource(name="wechatNews1")
    private WechatNewsDao wechatNews;

    @Override
    public WechatNews getWelcomeNews() {
        List<WechatNews> news = wechatNews.queryAllNews();
        if (CollectionUtils.isEmpty(news)) {
            return null;
        }
        return news.get(0);
    }

    @Override
    public void saveNews(WechatNews news) {
        boolean isCreate = news.getId() == null;
        if (isCreate) {
            wechatNews.add(news);
        } else {
            news.setLastUpdatedDate(new Date());
            wechatNews.update(news);
        }
        return;
    }
}
