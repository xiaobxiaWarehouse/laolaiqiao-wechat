package com.codi.laolaiqiao.service;

import com.codi.laolaiqiao.domain.User;
import com.codi.laolaiqiao.model.UserEx;
import com.codi.laolaiqiao.resultModel.UserResult;

/**
 * 用户信息Service
 * @author song-jj
 */
public interface UserService {

    /**
     * 根据微信的openId取得用户信息
     * @param openId
     * @return
     */
    UserResult getUseInfo(String openId);
    
    /**
     * 保存用户信息
     * @param user
     * @return
     */
    UserResult saveUser(User user);
    
    /**
     * 分页查询用户信息
     * @param pageSize
     * @param pageIndex
     * @param keyword
     * @return
     */
    UserEx queryPage(Integer pageSize, Integer pageIndex, String keyword);
}
