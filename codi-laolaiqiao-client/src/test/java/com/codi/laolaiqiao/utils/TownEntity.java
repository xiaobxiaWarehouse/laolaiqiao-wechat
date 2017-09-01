package com.codi.laolaiqiao.utils;

import java.util.List;

public class TownEntity {

    private String value;
    
    private String text;
    
    private List<TownEntity> children;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<TownEntity> getChildren() {
        return children;
    }

    public void setChildren(List<TownEntity> children) {
        this.children = children;
    }
    
}
