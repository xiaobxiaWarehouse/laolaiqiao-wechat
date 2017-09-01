package com.codi.laolaiqiao.service;

import com.codi.laolaiqiao.domain.UserImage;

public interface UserImageService {

    UserImage getUserImageByOpenId(String openId);
    
    int saveUserImage(String openId, String imageUrl);
}
