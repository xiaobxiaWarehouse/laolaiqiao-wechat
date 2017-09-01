package com.codi.laolaiqiao.wechat.thirdparty.model;

import com.github.sd4324530.fastweixin.api.entity.MenuButton;

/**
 * 子菜单
 * @author song-jj
 */
public class SubMenuButton extends MenuButton {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 父级菜单名称
     */
    private String parentName;

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
    
}
