package com.codi.laolaiqiao.wechat.controller;

import java.util.Date;

import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.codi.base.util.DateEditor;
import com.codi.laolaiqiao.wechat.thirdparty.WechatSupportImpl;

/**
 * Controller基类
 * 
 * @date 2016年10月31日 下午4:59:44
 */
public class BaseController extends WechatSupportImpl  {

    @InitBinder
    public void initBinder(ServletRequestDataBinder binder) {

        binder.registerCustomEditor(Date.class, new DateEditor());
    }
    
    
}
