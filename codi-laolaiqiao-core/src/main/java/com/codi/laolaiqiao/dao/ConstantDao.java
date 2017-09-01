package com.codi.laolaiqiao.dao;

import java.util.List;

import com.codi.base.dao.BaseDAO;
import com.codi.laolaiqiao.domain.Constant;

/**
 * 常量表对应的DAO
 * 
 * @author song-jj
 */
public interface ConstantDao extends BaseDAO<Constant> {
    
    /**
     * 根据类别查找相应的常量
     * @param category
     * @return
     */
    List<Constant> queryConstantByCategory(String category);
}