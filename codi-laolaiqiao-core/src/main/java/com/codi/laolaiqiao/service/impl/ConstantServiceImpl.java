package com.codi.laolaiqiao.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codi.laolaiqiao.constant.GlobalConst;
import com.codi.laolaiqiao.dao.ConstantDao;
import com.codi.laolaiqiao.domain.Constant;
import com.codi.laolaiqiao.service.ConstantService;

/**
 * 常量表ServiceImpl
 * @author song-jj
 */
@Service("constantService")
@Transactional
public class ConstantServiceImpl implements ConstantService {
    
    @Resource(name = "constantDao1")
    private ConstantDao constantDao;

    /**
     * 取得兴趣爱好列表
     */
    @Override
    public List<Constant> getHobbies() {
        return constantDao.queryConstantByCategory(GlobalConst.DB_CONST_CATEGORY);
    }

}
