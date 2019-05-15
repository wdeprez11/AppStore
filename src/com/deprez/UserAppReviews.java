package com.deprez;

import java.util.ArrayList;
import java.util.List;

public class UserAppReviews {
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

    public void setAppReviews(List<UserAppReview> userAppReviews) {
        this.userAppReviews = userAppReviews;
    }

    public void addAppReview(UserAppReview userAppReview) {
        userAppReviews.add(userAppReview);
    }

    public List<Integer> getUsersOfApp(int appId) {
        List<Integer> users = new ArrayList<>();
        for (UserAppReview appReview : userAppReviews) {
            if (appReview.getAppId() == appId) {
                users.add(appReview.getUserId());
            }
        }
        return users;
    }

    public List<Integer> getAppsOfUser(int userId) {
        List<Integer> apps = new ArrayList<>();
        for (UserAppReview appReview : userAppReviews) {
            if (appReview.getUserId() == userId) {
                apps.add(appReview.getAppId());
            }
        }
        return apps;
    }

    /**
     * Adds a new `UserAppReview` object to the class list
     * @param userId
     * @param appId
     */
    public void addUserAppReview(int userId, int appId) {
        addUserAppReview(new UserAppReview(userId, appId));
    }

    /**
     * Adds a new `UserAppReview` object to the class list
     *
     * @param userAppReview the userAppReview object
     */
    public void addUserAppReview(UserAppReview userAppReview) {
        userAppReviews.add(userAppReview);
    }

    /*
    public boolean hasAppReview(UserAppReview userAppReview) {
        for(UserAppReview ar : appReviews) {
            if()
        }
    }
     */

    public boolean userHasApp(int userId, int appId) {
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
}
