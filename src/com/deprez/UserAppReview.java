package com.deprez;

public class UserAppReview {
    private int userId;
    private int appId;
    private int appReviewScore;
    private String appReviewDetail;

    public UserAppReview() {

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

    public String getAppReviewDetail() {
        return appReviewDetail;
    }

    public void setAppReviewDetail(String appReviewDetail) {
        this.appReviewDetail = appReviewDetail;
    }

    @Override
    public String toString() {
        return "UserAppReview{" +
                "appId=" + appId +
                ", appReviewScore=" + appReviewScore +
                ", appReviewDetail='" + appReviewDetail + '\'' +
                '}';
    }
}
