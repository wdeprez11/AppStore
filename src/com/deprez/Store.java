package com.deprez;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

    public void addApp(App app) throws AlreadyExistsException { // TODO
        if (hasApp(app)) {
            throw new AlreadyExistsException("App with same appId and appName already exists");
        } else {
            apps.add(app);
        }

    }

    public void sort() {
        Collections.sort(apps, new AppComparator());
    }

    public boolean hasApp(App app) {
        for (App ap : apps) {
            if (ap.equals(app)) {
                return true;
            }
        }
        return false;
    }

    public int size() {
        return apps.size();
    }

    @Override
    public String toString() {
        return "Store{" +
                "apps=" + apps +
                '}';
    }

    // TODO: merge sort
    // TODO: search
}

class AppComparator implements Comparator<App> {
    @Override
    public int compare(App app, App t1) {
        return app.compareTo(t1);
    }
}