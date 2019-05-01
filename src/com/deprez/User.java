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
}
