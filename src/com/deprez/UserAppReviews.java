package com.deprez;

import java.util.ArrayList;
import java.util.List;

public class UserAppReviews {
    private List<UserAppReview> appReviews;

    public UserAppReviews() {
        appReviews = new ArrayList<>();
    }

    public UserAppReviews(List<UserAppReview> appReviews) {
        this.appReviews = appReviews;
    }

    public List<UserAppReview> getAppReviews() {
        return appReviews;
    }

    public void setAppReviews(List<UserAppReview> appReviews) {
        this.appReviews = appReviews;
    }

    public void addAppReview(UserAppReview appReview) {
        appReviews.add(appReview);
    }

    public List<Integer> getUsersOfApp(int appId) {
        List<Integer> users = new ArrayList<>();
        for (UserAppReview appReview : appReviews) {
            if (appReview.getAppId() == appId) {
                users.add(appReview.getUserId());
            }
        }
        return users;
    }

    public List<Integer> getAppsOfUser(int userId) {
        List<Integer> apps = new ArrayList<>();
        for (UserAppReview appReview : appReviews) {
            if (appReview.getUserId() == userId) {
                apps.add(appReview.getAppId());
            }
        }
        return apps;
    }

    public void addUserAppReview(int userId, int appId) {
        System.out.println("addUserAppReview()...");
        addUserAppReview(new UserAppReview(userId, appId));
        System.out.println("addUserAppReview()...completed");
    }

    public void addUserAppReview(UserAppReview userAppReview) {
        System.out.println("  addUserAppReview()...");
        appReviews.add(userAppReview);
        System.out.println("  addUserAppReview()...completed");
    }

    /*
    public boolean hasAppReview(UserAppReview appReview) {
        for(UserAppReview ar : appReviews) {
            if()
        }
    }
     */

    @Override
    public String toString() {
        return "UserAppReviews{" +
                "appReviews=" + appReviews +
                '}';
    }
}
