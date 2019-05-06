package com.deprez;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class User {
    private int userId;
    private String userName;
    private List<UserAppReview> userApps;

    /**
     * Creates a User object.
     *
     * @param userId   the userId of the user in the table. User identifier is chosen by the order of creation in Community class.
     * @param userName the userName of the user. Unique to each user.
     */
    public User(int userId, String userName) {
        this.userId = userId;
        this.userName = userName;
        userApps = new ArrayList<>();
    }

    /**
     * Creates a User object.
     *
     * @param userId   the userId of the user in the table. User identifier is chosen by the order of creation in Community class.
     * @param userName the userName of the user. Unique to each user.
     * @param userApps the apps the user owns.
     */
    public User(int userId, String userName, List<UserAppReview> userApps) {
        this.userId = userId;
        this.userName = userName;
        this.userApps = userApps;
    }

    /**
     * Returns the user id of the user.
     *
     * @return Returns the userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Replaces the existing user id.
     *
     * @param userId the new userId
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Returns the user name of the user.
     *
     * @return Returns the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Replaces the existing userName
     *
     * @param userName the new userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Returns the indexes of appIds within the Store class' apps list.
     *
     * @return returns the indexes of appId
     */
    public List<UserAppReview> getUserApps() {
        return userApps;
    }

    /**
     * Replaces the existing userApps list
     *
     * @param userApps the new userApps list
     */
    public void setUserApps(List<UserAppReview> userApps) {
        this.userApps = userApps;
    }

    public void addAppReview(UserAppReview appReview) {
        userApps.add(appReview);
    }

    /**
     * Returns a String of the class attributes.
     *
     * @return Returns String of class attributes
     */
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userApps=" + userApps +
                '}';
    }

    /**
     * Compares two user objects for their difference in names.
     *
     * @param o The other object
     * @return Returns the difference in userNames of two User objects
     */
    public int compareToName(Object o) {
        return userName.compareTo(((User) o).userName);
    }

    /**
     * Compares two user objects for their difference in userId.
     *
     * @param o The other object
     * @return Returns the difference in ids of two User objects
     */
    public int compareToId(Object o) {
        return userId - ((User) o).userId;
    }

    /**
     * Checks to see if two User objects have the same userName
     *
     * @param o The other object
     * @return Returns true if the userNames are the same
     */
    public boolean equals(Object o) {
        return userName.equals(((User) o).userName);
    }

    /**
     * Sorts the list.
     *
     * TODO: Implement as merge sort, temporarily using Collections because focusing on more important things.
     */
    public void sort() {
        Collections.sort(userApps, UserAppReview::compareTo);
    }

    // TODO: search
}

