package com.codi.laolaiqiao.domain;

public class UserImage {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_image.open_id
     *
     * @mbggenerated Thu Dec 29 20:49:16 CST 2016
     */
    private String openId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_image.image_url
     *
     * @mbggenerated Thu Dec 29 20:49:16 CST 2016
     */
    private String imageUrl;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_image.open_id
     *
     * @return the value of user_image.open_id
     *
     * @mbggenerated Thu Dec 29 20:49:16 CST 2016
     */
    public String getOpenId() {
        return openId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_image.open_id
     *
     * @param openId the value for user_image.open_id
     *
     * @mbggenerated Thu Dec 29 20:49:16 CST 2016
     */
    public void setOpenId(String openId) {
        this.openId = openId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_image.image_url
     *
     * @return the value of user_image.image_url
     *
     * @mbggenerated Thu Dec 29 20:49:16 CST 2016
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_image.image_url
     *
     * @param imageUrl the value for user_image.image_url
     *
     * @mbggenerated Thu Dec 29 20:49:16 CST 2016
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl == null ? null : imageUrl.trim();
    }
}