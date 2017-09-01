package com.codi.laolaiqiao.dao;

import com.codi.base.dao.BaseDAO;
import com.codi.laolaiqiao.domain.UserImage;

public interface UserImageDao extends BaseDAO<UserImage>  {
    int add(UserImage userImage);
    
    UserImage queryImageByOpenId(String openId);
}