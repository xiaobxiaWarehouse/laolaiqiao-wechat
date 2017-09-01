package com.codi.laolaiqiao.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codi.laolaiqiao.dao.UserDao;
import com.codi.laolaiqiao.domain.User;
import com.codi.laolaiqiao.exception.BaseException;
import com.codi.laolaiqiao.model.UserEx;
import com.codi.laolaiqiao.resultModel.UserResult;
import com.codi.laolaiqiao.service.UserService;

import lombok.extern.slf4j.Slf4j;

/**
 * 用户信息UserService
 * @author song-jj
 */
@Service("userService")
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {
    
    @Resource(name="userDao1")
    private UserDao userDao;

    /**
     * 取得用户信息
     */
    @Override
    public UserResult getUseInfo(String openId) {
        UserResult result = new UserResult();
        User user = userDao.queryUserByUserId(openId);
        result.setUser(user);
        return result;
    }

    /**
     * 保存用户信息
     */
    @Override
    public UserResult saveUser(User user) {
        UserResult result = new UserResult();
        boolean isCreate = user.getUserId() == null;
        try {
            user.setLastUpdatedDate(new Date());
            // 创建
            if (isCreate) {
                userDao.add(user);
                // 更新
            } else {
                UserResult userResult = getUseInfo(user.getOpenId());
                if (userResult.getUser() == null || !userResult.getUser().getUserId().equals(user.getUserId())) {
                    throw new BaseException("用户不存在");
                }
                userDao.update(user);
            }
            result.setUser(user);
        } catch (BaseException baseException) {
            throw baseException;
        }
        
        return result;
    }
    
    /**
     * 分页查询用户信息
     * 
     * @param pageSize 一页的记录数
     * @param pageIndex 当前页
     * @return 基金列表
     */
    @Override
    public UserEx queryPage(Integer pageSize, Integer pageIndex, String keyword) {
        UserEx userEx = userDao.queryByKeywordWithPage(keyword, pageIndex, pageSize);
        return userEx;
    }

}
