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

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public List<Integer> getAppOwners() {
        return appOwners;
    }

    public void setAppOwners(List<Integer> appOwners) {
        this.appOwners = appOwners;
    }

    // TODO: equals method
    // TODO: compareTo
    // TODO: merge sort
    // TODO: search
}
