package com.codi.laolaiqiao.service;

import java.util.List;

import com.codi.laolaiqiao.domain.Constant;

/**
 * 常量表对应Service
 * @author song-jj
 */
public interface ConstantService {

    /**
     * 取得兴趣爱好列表
     * @return
     */
    List<Constant> getHobbies(); 
}
