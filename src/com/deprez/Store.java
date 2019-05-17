package com.deprez;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;

/**
 * The Store class manages the list of {@link com.deprez.App} plain java objects. It can sort the list and change
 * anything about the list.
 *
 * @see com.deprez.App
 */
public class Store {
    
    /**
     * The {@link java.util.List} of {@link com.deprez.App}
     */
    private List<App> apps;
    
    /**
     * Creates a {@link com.deprez.Store} object.
     */
    public Store() {
        this.apps = new ArrayList<>();
    }
    
    /**
     * Creates a {@link com.deprez.Store} object.
     *
     * @param apps the {@link java.util.List} of {@link com.deprez.App} objects
     */
    public Store(List<App> apps) {
        this.apps = apps;
    }
    
    /**
     * Gets the {@link java.util.List} of {@link com.deprez.App} objects
     *
     * @return the {@link java.util.List} of {@link com.deprez.App} objects
     */
    public List<App> getApps() {
        return apps;
    }
    
    /**
     * Sets the {@link java.util.List} of the {@link com.deprez.Store#apps}
     *
     * @param apps replacement {@link java.util.List} of {@link com.deprez.App} objects
     */
    public void setApps(List<App> apps) {
        this.apps = apps;
    }
    
    /**
     * Adds a {@link com.deprez.App} object to the {@link com.deprez.Store#apps} {@link java.util.List}
     *
     * @param appName the new name of the potential {@link com.deprez.App}
     */
    public void addApp(String appName) {
        if (hasApp(appName) >= 0) {
            Driver.LOGGER.log(Level.FINE,
                              "App with the same appName '" + "' already exists in " + this.getClass().getName());
        } else {
            apps.add(new App(apps.size() + 1, appName));
        }
    }
    
    /**
     * Used to see if a {@link com.deprez.App} object with the same `appName` was found in {@link
     * com.deprez.Store#apps}
     *
     * @param appName the `appName` to look for
     *
     * @return Returns the {@link com.deprez.App} identifier if found, otherwise returns false
     */
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
    
    /**
     * Sorts {@link com.deprez.Store#apps} by name
     *
     * @see java.util.Collections#sort(List, Comparator)
     */
    public void sort() {
        Collections.sort(apps, App::compareToName);
    }
    
    /**
     * Overrides the {@link java.lang.Object} `toString()` method
     *
     * @return the object as a string, describing the class attributes.
     *
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return "Store{" +
               "apps=" + apps +
               '}';
    }
    
    /**
     * Clears the {@link java.util.List} of {@link com.deprez.App} -- {@link com.deprez.Store#apps}
     */
    public void clear() {
        apps.clear();
    }
    
    /**
     * Returns the size of the {@link java.util.List} of {@link com.deprez.App} -- {@link com.deprez.Store#apps}
     */
    public int size() {
        return apps.size();
    }
    
    /**
     * Returns the {@link com.deprez.App} identifier that matched the {@link com.deprez.App}'s appName
     *
     * @param appName the appName to search for
     *
     * @return {@link com.deprez.App} object that matched the identifier, returns null if not found
     */
    public App getAppId(String appName) {
        int tmp = hasApp(appName);
        return (tmp >= 0) ? apps.get(tmp) : null;
    }
    
    /**
     * Gets the {@link com.deprez.App} appName given {@link com.deprez.App} identifier, returns null if not found.
     *
     * @param appId the {@link com.deprez.App} to look for.
     *
     * @return returns appName of {@link com.deprez.App} object, returns null if not found
     */
    String getAppName(int appId) {
        System.out.println("appId: " + appId + ", apps.size(): " + apps.size());
        return (appId < apps.size()) ? apps.get(appId - 1).getAppName() : null;
    }
    
    public String[][] toTable() {
        String[][] table = new String[apps.size()][2];
        
        int i = 0;
        for (App app : apps) {
            table[i][0] = Integer.toString(app.getAppId());
            table[i][1] = app.getAppName();
            i++;
        }
        
        return table;
    }
    
    
    // TODO: merge sort
    // TODO: search
}
