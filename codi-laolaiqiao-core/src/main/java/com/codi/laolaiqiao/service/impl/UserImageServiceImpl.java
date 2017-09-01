package com.codi.laolaiqiao.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codi.laolaiqiao.dao.UserImageDao;
import com.codi.laolaiqiao.domain.UserImage;
import com.codi.laolaiqiao.service.UserImageService;

@Service("userImageService")
@Transactional
public class UserImageServiceImpl implements UserImageService {
    
    @Resource(name = "userImageDao1")
    private UserImageDao userImageDao;

    @Override
    public UserImage getUserImageByOpenId(String openId) {
        return userImageDao.queryImageByOpenId(openId);
    }

    @Override
    public int saveUserImage(String openId, String imageUrl) {
        UserImage image = new UserImage();
        image.setImageUrl(imageUrl);
        image.setOpenId(openId);
        int count = userImageDao.update(image);
        if (count == 0) {
            userImageDao.add(image);
        }
        return count;
    }

}
