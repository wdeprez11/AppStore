package com.deprez;

import java.util.ArrayList;
import java.util.List;

public class App {
    private int appId;
    private String appName;
    private List<Integer> appOwners;

    public App(int appId, String appName) {
        this.appId = appId;
        this.appName = appName;
        appOwners = new ArrayList<>();
    }

    public App(int appId, String appName, List<Integer> appOwners) {
        this.appId = appId;
        this.appName = appName;
        this.appOwners = appOwners;
    }
}
