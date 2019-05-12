package com.deprez;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The App class is a simple plain java object that manages appIds and appNames
 */
public class App {
    private int appId;
    private String appName;
    private List<Integer> appOwners;

    /**
     * Constructs an App object.
     * Used for created a new App.
     *
     * @param appId the appId of the app
     * @param appName the name of the app
     */
    public App(int appId, String appName) {
        this.appId = appId;
        this.appName = appName;
        appOwners = new ArrayList<>();
    }

    /**
     * Constructs an App object, with an initial list.
     * Mainly for use when reading in an existing list of owners.
     *
     * @param appId the appId of the app
     * @param appName the name of the app
     * @param appOwners the list of app owners.
     */
    public App(int appId, String appName, List<Integer> appOwners) {
        this.appId = appId;
        this.appName = appName;
        this.appOwners = appOwners;
    }

    /**
     * Gets the appId
     *
     * @return returns the object's appId
     */
    public int getAppId() {
        return appId;
    }

    /**
     * Sets the object's appId
     *
     * @param appId the new app id
     */
    public void setAppId(int appId) {
        this.appId = appId;
    }

    /**
     * Gets the appName
     *
     * @return returns the object's app name
     */
    public String getAppName() {
        return appName;
    }

    /**
     * Sets the object's appName
     *
     * @param appName the new app name
     */
    public void setAppName(String appName) {
        this.appName = appName;
    }

    /**
     * Gets the list of app ids of the owners
     *
     * @return returns the list appId's of owners as Integers.
     */
    public List<Integer> getAppOwners() {
        return appOwners;
    }

    /**
     * Sets the object's appOwners.
     *
     * @param appOwners the new list of app owners.
     */
    public void setAppOwners(List<Integer> appOwners) {
        this.appOwners = appOwners;
    }

    /**
     * Compares the appId and appName difference of two apps.
     *
     * @param o the other App object
     * @return returns the sum of the difference in the appId of the implicit object with the appId of the parameter and the appName of the implicit object with the appName of the parameter.
     */
    public int compareTo(Object o) {
        App other = (App) o;
        return appId - other.appId + appName.compareTo(other.appName);
    }

    /**
     * Checks if the appId and appName are the same apps
     *
     * @param o the other App object
     * @return returns if the appId is the same as the parameter and the appName is the same.
     */
    public boolean equals(Object o) {
        App other = (App) o;
        return appName.equals(other.appName);
    }

    /**
     * Returns the attributes of an App object as a String.
     *
     * @return returns the appId, appName, and appOwners of the App object.
     */
    @Override
    public String toString() {
        return "App{" +
                "appId=" + appId +
                ", appName='" + appName + '\'' +
                ", appOwners=" + appOwners +
                '}';
    }

    public void sort() {
        Collections.sort(appOwners);
    }

    public int size() {
        return appOwners.size();
    }

    public void clear() {
        appOwners.clear();
    }

    // TODO: merge sort
    // TODO: search
}
