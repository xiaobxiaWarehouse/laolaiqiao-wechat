package com.codi.laolaiqiao.dao.impl;

import org.springframework.stereotype.Repository;

import com.codi.base.dao.BaseDAOImpl;
import com.codi.laolaiqiao.dao.UserImageDao;
import com.codi.laolaiqiao.domain.UserImage;

@Repository("userImageDao1")
public class UserImageDaoImpl extends BaseDAOImpl<UserImage> implements UserImageDao {

    @Override
    public int add(UserImage userImage) {
        return this.insert(generateStatement("insertSelective"), userImage);
    }

    @Override
    public UserImage queryImageByOpenId(String openId) {
        return this.getObject(generateStatement("selectByPrimaryKey"), openId);
    }
    
}
