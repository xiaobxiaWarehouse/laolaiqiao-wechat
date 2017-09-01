package com.codi.laolaiqiao.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.codi.base.dao.BaseDAOImpl;
import com.codi.laolaiqiao.dao.WechatNewsDao;
import com.codi.laolaiqiao.domain.WechatNews;

/**
 * 
 * @author song-jj
 */
@Repository("wechatNews1")
public class WechatNewsDaoImpl extends BaseDAOImpl<WechatNews> implements WechatNewsDao {

    @Override
    public List<WechatNews> queryAllNews() {
        return this.findList(generateStatement("selectAllNews"));
    }
    
    @Override
    public int add(WechatNews news) {
        return this.insert(generateStatement("insertSelective"), news);
    }

    
}
