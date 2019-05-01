package com.deprez;

import java.util.ArrayList;
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

    // TODO: merge sort
    // TODO: search
}
