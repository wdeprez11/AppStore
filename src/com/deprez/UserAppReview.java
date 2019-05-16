package com.deprez;

/**
 * Manages userId and appId pairs for better optimization compared to storing the each pair within {@link
 * com.deprez.Community} and {@link com.deprez.Store}
 *
 * @see com.deprez.UserAppReviews
 * @see com.deprez.Community
 * @see com.deprez.Store
 */
public class UserAppReview {
    
    /**
     * The userId of the pair
     *
     * @see com.deprez.User
     */
    private int userId;
    
    /**
     * The appId of the pair
     *
     * @see com.deprez.App
     */
    private int appId;
    
    /**
     * The score of the potential review
     *
     * @see com.deprez.User
     * @see com.deprez.App
     * @see com.deprez.UserAppReviews
     */
    private int appReviewScore;
    
    /**
     * The detail of the potential review
     *
     * @see com.deprez.User
     * @see com.deprez.App
     * @see com.deprez.UserAppReviews
     */
    private String appReviewDetail;
    
    /**
     * Creates a UserAppReview object without a review score or detail
     *
     * @param userId the userId of the {@link com.deprez.User} object
     * @param appId  the appId of the {@link com.deprez.App} object
     *
     * @see com.deprez.User
     * @see com.deprez.App
     */
    public UserAppReview( int userId, int appId ) {
        this.userId = userId;
        this.appId  = appId;
    }
    
    /**
     * Creates a UserAppReview object with an initial review score and detail
     *
     * @param userId          the userId of the {@link com.deprez.User} object
     * @param appId           the appId of the {@link com.deprez.App} object
     * @param appReviewScore  the score of the review
     * @param appReviewDetail the detail of the review
     */
    public UserAppReview( int userId, int appId, int appReviewScore, String appReviewDetail ) {
        this.userId          = userId;
        this.appId           = appId;
        this.appReviewScore  = appReviewScore;
        this.appReviewDetail = appReviewDetail;
    }
    
    /**
     * Gets the {@link UserAppReview#userId} attribute
     *
     * @return {@link UserAppReview#userId}
     */
    public int getUserId() {
        return userId;
    }
    
    /**
     * Sets the {@link UserAppReview#userId} attribute
     *
     * @param userId the new {@link UserAppReview#userId}
     */
    public void setUserId( int userId ) {
        this.userId = userId;
    }
    
    /**
     * Gets the {@link UserAppReview#appId} attribute
     *
     * @return {@link UserAppReview#appId}
     */
    public int getAppId() {
        return appId;
    }
    
    /**
     * Sets the {@link UserAppReview#appId} attribute
     *
     * @param appId the new {@link UserAppReview#appId}
     */
    public void setAppId( int appId ) {
        this.appId = appId;
    }
    
    /**
     * Gets the {@link UserAppReview#appReviewScore} attribute
     *
     * @return {@link UserAppReview#appReviewScore}
     */
    public int getAppReviewScore() {
        return appReviewScore;
    }
    
    /**
     * Sets the {@link UserAppReview#appReviewScore} attribute
     *
     * @param appReviewScore the new {@link UserAppReview#appReviewScore}
     */
    public void setAppReviewScore( int appReviewScore ) {
        this.appReviewScore = appReviewScore;
    }
    
    /**
     * Gets the {@link UserAppReview#appReviewScore} attribute
     *
     * @return {@link UserAppReview#appReviewScore}
     */
    public String getAppReviewDetail() {
        return appReviewDetail;
    }
    
    /**
     * Sets the {@link UserAppReview#appReviewDetail} attribute
     *
     * @param appReviewDetail the new {@link UserAppReview#appReviewDetail}
     */
    public void setAppReviewDetail( String appReviewDetail ) {
        this.appReviewDetail = appReviewDetail;
    }
    
    /**
     * Returns the difference of a UserAppReview object's attributes.
     *
     * @param o the other object.
     *
     * @return return the difference of two UserAppReview object's appIds
     */
    public int compareTo( Object o ) {
        UserAppReview other = (UserAppReview) o;
        return ( appId - other.appId );
    }
    
    /**
     * Checks if two reviews are equal to each other.
     *
     * @param o the other object.
     *
     * @return return if the implicit appId, appReviewScore, and appReviewDetail and the same as the parameters.
     */
    public boolean equals( Object o ) {
        UserAppReview other = (UserAppReview) o;
        return appId == other.appId && appReviewScore == other.appReviewScore && appReviewDetail.equals(other.appReviewDetail);
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
    
}
