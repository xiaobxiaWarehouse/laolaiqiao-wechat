package com.codi.laolaiqiao.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.codi.base.dao.BaseDAOImpl;
import com.codi.base.dao.plugin.page.PageView;
import com.codi.laolaiqiao.dao.UserDao;
import com.codi.laolaiqiao.domain.User;
import com.codi.laolaiqiao.model.UserEx;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author song-jj
 */
@Repository("userDao1")
@Slf4j
public class UserDaoImpl extends BaseDAOImpl<User> implements UserDao {

    @Override
    public int add(User user) {
        return this.insert(generateStatement("insertSelective"), user);
    }
    
    @Override
    public User queryUserByUserId(String openId) {
        return this.getObject(generateStatement("selectByPrimaryKey"), openId);
    }

    @Override
    public UserEx queryByKeywordWithPage(String keyword, Integer pageIndex, Integer pageSize) {
        UserEx userEx = new UserEx();
        PageView pageView = getPageView(pageIndex, pageSize);

        Map<String, Object> param = new HashMap<>();
        param.put("keyword", keyword);
        param.put("pageView", pageView);

        List<User> users = this.findList(generateStatement("queryByKeywordWithPage"), param);
        userEx.setUser(users);
        userEx.setPageCount(pageView.getPageCount());
        userEx.setRowCount(pageView.getRowCount());
        return userEx;
    }

}
