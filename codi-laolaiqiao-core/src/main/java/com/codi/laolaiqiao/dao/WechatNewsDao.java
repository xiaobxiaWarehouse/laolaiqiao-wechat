package com.codi.laolaiqiao.dao;

import java.util.List;

import com.codi.base.dao.BaseDAO;
import com.codi.laolaiqiao.domain.WechatNews;

public interface WechatNewsDao extends BaseDAO<WechatNews>  {
    List<WechatNews> queryAllNews();
    
    int add(WechatNews news);
}