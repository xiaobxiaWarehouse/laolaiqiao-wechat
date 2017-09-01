package com.codi.laolaiqiao.dao;

import com.codi.base.dao.BaseDAO;
import com.codi.laolaiqiao.domain.User;
import com.codi.laolaiqiao.model.UserEx;

public interface UserDao extends BaseDAO<User> {
    int add(User user);
    
    User queryUserByUserId(String openId);
    
    /**
     * 分页查询
     * @param keyword
     * @return
     */
    UserEx queryByKeywordWithPage(String keyword, Integer pageIndex, Integer pageSize);
}