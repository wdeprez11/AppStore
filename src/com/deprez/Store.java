package com.deprez;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

/**
 * The Store class manages the list of {@link com.deprez.User}
 * plain java objects. It can sort the list and change anything about the list.
 *
 * @see com.deprez.App
 */
public class Store {
    private List<App> apps;

    public Store() {
        this.apps = new ArrayList<>();
    }

    public Store(List<App> apps) {
        this.apps = apps;
    }

    public List<App> getApps() {
        return apps;
    }

    public void setApps(List<App> apps) {
        this.apps = apps;
    }

    public void addApp(String appName) {
        if (hasApp(appName) >= 0) {
            Driver.LOGGER.log(Level.FINE, "App with the same appName '" + "' already exists in " + this.getClass().getName());
        } else {
            apps.add(new App(apps.size() + 1, appName));
        }
    }

    public void sort() {
        Collections.sort(apps, App::compareTo);
    }

    public int hasApp(String appName) {
        int i = 0;
        for (App app : apps) {
            if (app.getAppName().equals(appName)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    @Override
    public String toString() {
        return "Store{" +
                "apps=" + apps +
                '}';
    }

    public void clear() {
        apps.clear();
    }

    public int size() {
        return apps.size();
    }

    public App getApp(String appName) {
        int tmp = hasApp(appName);
        return (tmp >= 0) ? apps.get(tmp) : null;
    }

    public String getApp(int i) {
        return apps.get(i).getAppName();
    }

    // TODO: merge sort
    // TODO: search
}
