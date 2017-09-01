package com.codi.laolaiqiao.wechat.thirdparty.model;

/**
 * 图文消息类
 * @author song-jj
 */
public class NewsItem {

    private String title;
    
    private String mediaId;
    
    private String url;
    
    private String thumbUrl;
    
    /**
     * 摘要
     */
    private String digest;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

  
}
