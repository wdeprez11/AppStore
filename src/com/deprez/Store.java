package com.deprez;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Test Description
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

    public void addApp(String appName) throws AlreadyExistsException { // TODO
        if (hasApp(appName) >= 0) {
            throw new AlreadyExistsException("App with the same appName '" + appName + "'already exists in " + this.getClass().getName());
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
            if (app.equals(appName)) {
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

    // TODO: merge sort
    // TODO: search
}
