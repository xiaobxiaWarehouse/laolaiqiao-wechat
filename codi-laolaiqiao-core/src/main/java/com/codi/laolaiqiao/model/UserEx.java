package com.codi.laolaiqiao.model;

import java.util.List;

import com.codi.laolaiqiao.domain.User;

public class UserEx extends User {
    
    private List<User> user;

    private long pageCount;
    
    private long rowCount;

    public long getPageCount() {
        return pageCount;
    }

    public void setPageCount(long pageCount) {
        this.pageCount = pageCount;
    }

    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }

    public long getRowCount() {
        return rowCount;
    }

    public void setRowCount(long rowCount) {
        this.rowCount = rowCount;
    }
    
}
