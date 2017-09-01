package com.codi.laolaiqiao.resultModel;

import java.util.List;

import com.codi.base.domain.BaseResult;
import com.codi.laolaiqiao.domain.User;

/**
 * 用户信息
 * @author song-jj
 */
public class UserResult extends BaseResult {

    private static final long serialVersionUID = 1L;
    
    private User user;
    
    private String imageUrl;
    
    private List<User> users;
    
    private long pageCount;
    
    private long rowCount;
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public long getPageCount() {
        return pageCount;
    }

    public void setPageCount(long pageCount) {
        this.pageCount = pageCount;
    }

    public long getRowCount() {
        return rowCount;
    }

    public void setRowCount(long rowCount) {
        this.rowCount = rowCount;
    }
	
}
