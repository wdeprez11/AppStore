package com.deprez;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int userId;
    private String userName;
    private List<Integer> userApps;

    public User(int userId, String userName) {
        this.userId = userId;
        this.userName = userName;
        userApps = new ArrayList<>();
    }

    public User(int userId, String userName, List<Integer> userApps) {
        this.userId = userId;
        this.userName = userName;
        this.userApps = userApps;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<Integer> getUserApps() {
        return userApps;
    }

    public void setUserApps(List<Integer> userApps) {
        this.userApps = userApps;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userApps=" + userApps +
                '}';
    }

    // TODO: equals method
    // TODO: compareTo
    // TODO: merge sort
    // TODO: search
}
