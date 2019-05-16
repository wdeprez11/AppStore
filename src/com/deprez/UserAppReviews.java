package com.deprez;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@link com.deprez.UserAppReviews} class manages the list of {@link com.deprez.UserAppReview} plain java objects.
 * It can sort the list and change anything about the list.
 *
 * @see com.deprez.UserAppReviews
 */
public class UserAppReviews {
    
    /**
     * The {@link java.util.List} of {@link com.deprez.UserAppReview}s
     */
    private List<UserAppReview> userAppReviews;
    
    public UserAppReviews() {
        userAppReviews = new ArrayList<>();
    }
    
    public UserAppReviews(List<UserAppReview> userAppReviews) {
        this.userAppReviews = userAppReviews;
    }
    
    public List<UserAppReview> getUserAppReviews() {
        return userAppReviews;
    }
    
    /**
     * Sets the {@link java.util.List} of {@link com.deprez.UserAppReviews#userAppReviews} to the parameter
     *
     * @param userAppReviews the new {@link com.deprez.UserAppReviews#userAppReviews}
     */
    public void setAppReviews(List<UserAppReview> userAppReviews) {
        this.userAppReviews = userAppReviews;
    }
    
    /**
     * Adds a {@link com.deprez.UserAppReview} to the {@link java.util.List} {@link
     * com.deprez.UserAppReviews#userAppReviews}
     *
     * @param userAppReview the new {@link com.deprez.UserAppReview}
     */
    public void addAppReview(UserAppReview userAppReview) {
        userAppReviews.add(userAppReview);
    }
    
    /**
     * Gets the {@link com.deprez.User} identifiers that have {@link com.deprez.UserAppReview} instances with the
     * parameter and returns them as a {@link java.util.List}
     *
     * @param appId the {@link com.deprez.App} identifier to search for pairs of
     *
     * @return matching {@link com.deprez.User} identifiers as a {@link java.util.List} of {@link java.lang.Integer}
     */
    List<Integer> getUsersOfApp(int appId) {
        List<Integer> users = new ArrayList<>();
        for (UserAppReview appReview : userAppReviews) {
            if (appReview.getAppId() == appId) {
                users.add(appReview.getUserId());
            }
        }
        return users;
    }
    
    /**
     * Returns the {@link com.deprez.App} identifiers that matches a pair with the userId of the parameter.
     *
     * @param userId the {@link com.deprez.User} identifier
     *
     * @return the appIds that were found within {@link com.deprez.UserAppReviews#userAppReviews}
     */
    List<Integer> getAppsOfUser(int userId) {
        List<Integer> apps = new ArrayList<>();
        for (UserAppReview appReview : userAppReviews) {
            if (appReview.getUserId() == userId) {
                apps.add(appReview.getAppId());
            }
        }
        return apps;
    }
    
    /**
     * Adds a new {@link com.deprez.UserAppReview} object to {@link com.deprez.UserAppReviews#userAppReviews}.
     *
     * @param userId the {@link com.deprez.User} identifier.
     * @param appId  the {@link com.deprez.App} identifier.
     */
    void addUserAppReview(int userId, int appId) {
        addUserAppReview(new UserAppReview(userId, appId));
    }
    
    /**
     * Adds a new `UserAppReview` object to the class list
     *
     * @param userAppReview the userAppReview object
     */
    void addUserAppReview(UserAppReview userAppReview) {
        userAppReviews.add(userAppReview);
    }
    
    /**
     * Checks if a user app pair is found in {@link com.deprez.UserAppReviews#userAppReviews}.
     *
     * @param userId the {@link com.deprez.User} identifier.
     * @param appId  the {@link com.deprez.App} identifier.
     *
     * @return true or false depending on whether the pair was found or not.
     */
    boolean userHasApp(int userId, int appId) {
        for (UserAppReview review : userAppReviews) {
            if (review.getUserId() == userId && review.getAppId() == appId) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "UserAppReviews{" +
               "appReviews=" + userAppReviews +
               '}';
    }
    
    /**
     * Converts all data for show all into 2D array. Each row is a separate row for the table.
     *
     * @param community a reference to the community, used to get {@link com.deprez.User} attribute `userName`.
     * @param store     a reference to the store, used to get {@link com.deprez.App} attribute `appName`.
     *
     * @return returns a 2D array with all attributes for use in JTable
     */
    String[][] toTable(Community community, Store store) {
        String[][] table = new String[userAppReviews.size()][6];
        int i = 0;
        for (UserAppReview userAppReview : userAppReviews) {
            int userId = userAppReview.getUserId();
            int appId = userAppReview.getAppId();
            table[i][0] = Integer.toString(userId);
            table[i][1] = community.getUserName(userId);
            table[i][2] = Integer.toString(appId);
            table[i][3] = store.getAppName(appId);
            table[i][4] = Integer.toString(userAppReview.getAppReviewScore());
            table[i][5] = userAppReview.getAppReviewDetail();
            i++;
        }
        return table;
    }
}
