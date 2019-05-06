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
                "appId=" + appId +
                ", appReviewScore=" + appReviewScore +
                ", appReviewDetail='" + appReviewDetail + '\'' +
                '}';
    }

    /**
     * Returns the difference of a UserAppReview object's attributes.
     *
     * @param o the other object.
     * @return return the sum of the difference of the implicit appId and the parameters appId, the difference of the implicit appReviewScore and the parameters appReviewScore, and the implicit appReviewDetail compared to the parameters appReviewDetail.
     */
    public int compareTo(Object o) {
        UserAppReview other = (UserAppReview) o;
        return (appId - other.appId) + (appReviewScore - other.appReviewScore) + (appReviewDetail.compareTo(other.appReviewDetail));
    }

    /**
     * Checks if two reviews are equal to each other.
     * @param o
     * @return
     */
    public boolean equals(Object o) {
        UserAppReview other = (UserAppReview) o;
        return appId == other.appId && appReviewScore == other.appReviewScore && appReviewDetail.equals(other.appReviewDetail);
    }

}
