package com.deprez;

/**
 * The App class is a simple plain java object that manages appIds and appNames
 *
 * @see com.deprez.Community
 */
public class App {
    private int    appId;
    private String appName;
    
    /**
     * Constructs an {@link com.deprez.App} object.
     *
     * @param appId   the appId of the app
     * @param appName the name of the app
     */
    public App(int appId, String appName) {
        this.appId   = appId;
        this.appName = appName;
    }
    
    /**
     * Gets the {@link com.deprez.App#appId}
     *
     * @return returns the {@link com.deprez.App#appId}
     */
    public int getAppId() {
        return appId;
    }
    
    /**
     * Sets the {@link com.deprez.App#appId}
     *
     * @param appId the new app id
     */
    public void setAppId(int appId) {
        this.appId = appId;
    }
    
    /**
     * Gets the {@link com.deprez.App#appName}
     *
     * @return returns the {@link com.deprez.App#appName}
     */
    public String getAppName() {
        return appName;
    }
    
    /**
     * Sets the {@link com.deprez.App#appName}
     *
     * @param appName the new app name
     */
    public void setAppName(String appName) {
        this.appName = appName;
    }
    
    /**
     * Compares the appId difference of two {@link com.deprez.App} objects.
     *
     * @param o the other {@link com.deprez.App} object
     *
     * @return returns the difference in the appId of the implicit object with the appId of the parameter `o`
     *
     * @see java.lang.Integer#compareTo(Integer)
     */
    public int compareToId(Object o) {
        return appId - ((App) o).appId;
    }
    
    /**
     * Compares the appName of two {@link com.deprez.App} objects.
     *
     * @param o the other {@link com.deprez.App} object
     *
     * @return returns the difference in the appName of the implicit object with the appName of the parameter `o`
     *
     * @see java.lang.String#compareTo(String)
     */
    public int compareToName(Object o) {
        return appName.compareTo(((App) o).appName);
    }
    
    /**
     * Checks if the appId and appName are the same apps
     *
     * @param o the other App object
     *
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
        return appName;
        /*
        return "App{" +
               "appId=" + appId +
               ", appName='" + appName + '\'' +
               '}';
        */
    }
    
    // TODO: merge sort
    // TODO: search
}
