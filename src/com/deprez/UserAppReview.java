package com.deprez;

public class UserAppReview {
    private int userId;
    private String userName;
    private String appName;
    private int appId;
    private int appReviewScore;
    private String appReviewDetail;

    /* TODO
    public UserAppReview(String userName, String appName, int appReviewScore, String appReviewDetail) {
        this.userName = userName;
        this.appName = appName;
        this.appReviewScore = appReviewScore;
        this.appReviewDetail = appReviewDetail;
    } */

    public UserAppReview(int userId, int appId) {
        this.userId = userId;
        this.appId = appId;
    }

    public UserAppReview(int userId, int appId, int appReviewScore, String appReviewDetail) {
        this.userId = userId;
        this.appId = appId;
        this.appReviewScore = appReviewScore;
        this.appReviewDetail = appReviewDetail;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public int getAppReviewScore() {
        return appReviewScore;
    }

    public void setAppReviewScore(int appReviewScore) {
        this.appReviewScore = appReviewScore;
    }

    /**
     * Gets the appReviewDetail.
     *
     * @return the class attribute appReviewDetail.
     */
    public String getAppReviewDetail() {
        return appReviewDetail;
    }

    /**
     * Replaces the existing appReviewDetail attribute with the parameter.
     *
     * @param appReviewDetail the new app review detail.
     */
    public void setAppReviewDetail(String appReviewDetail) {
        this.appReviewDetail = appReviewDetail;
    }

    /**
     * Returns the UserAppReview object as a String.
     *
     * @return the class attributes: appId, appReviewScore, and appReviewDetail.
     */
    @Override
    public String toString() {
        return "UserAppReview{" +
                "userId=" + userId +
                ", appId=" + appId +
                ", appReviewScore=" + appReviewScore +
                ", appReviewDetail='" + appReviewDetail + '\'' +
                '}';
    }

    /**
     * Returns the difference of a UserAppReview object's attributes.
     *
     * @param o the other object.
     * @return return the difference of two UserAppReview object's appIds
     */
    public int compareTo(Object o) {
        UserAppReview other = (UserAppReview) o;
        return (appId - other.appId);
    }

    /**
     * Checks if two reviews are equal to each other.
     *
     * @param o the other object.
     * @return return if the implicit appId, appReviewScore, and appReviewDetail and the same as the parameters.
     */
    public boolean equals(Object o) {
        UserAppReview other = (UserAppReview) o;
        return appId == other.appId && appReviewScore == other.appReviewScore && appReviewDetail.equals(other.appReviewDetail);
    }

}
