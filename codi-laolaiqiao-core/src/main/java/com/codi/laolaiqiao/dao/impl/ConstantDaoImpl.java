package com.codi.laolaiqiao.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.codi.base.dao.BaseDAOImpl;
import com.codi.laolaiqiao.dao.ConstantDao;
import com.codi.laolaiqiao.domain.Constant;

/**
 * 常量表dao
 * @author song-jj
 */
@Repository("constantDao1")
public class ConstantDaoImpl extends BaseDAOImpl<Constant> implements ConstantDao {

    @Override
    public List<Constant> queryConstantByCategory(String category) {
        return this.findList(generateStatement("selectByCategory"), category);
    }

}
