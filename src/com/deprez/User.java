package com.deprez;

/**
 * The plain java object of 'User' Contains {@link com.deprez.User#userId} and {@link com.deprez.User#userName}.
 *
 * @see com.deprez.Community
 */
public class User {
    private int    userId;
    private String userName;
    
    /**
     * Creates a User object.
     *
     * @param userId   the userId of the user in the table. User identifier is chosen by the order of creation in
     *                 Community class.
     * @param userName the userName of the user. Unique to each user.
     */
    public User( int userId, String userName ) {
        this.userId   = userId;
        this.userName = userName;
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
    public void setUserId( int userId ) {
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
    public void setUserName( String userName ) {
        this.userName = userName;
    }
    
    /**
     * Compares two user objects for their difference in names.
     *
     * @param o The other object
     *
     * @return Returns the difference in userNames of two User objects
     */
    public int compareToName( Object o ) {
        return userName.compareTo(( (User) o ).userName);
    }
    
    /**
     * Compares two user objects for their difference in userId.
     *
     * @param o The other object
     *
     * @return Returns the difference in ids of two User objects
     */
    public int compareToId( Object o ) {
        return userId - ( (User) o ).userId;
    }
    
    /**
     * Checks to see if two User objects have the same userName
     *
     * @param o The other object
     *
     * @return Returns true if the userNames are the same
     */
    public boolean equals( Object o ) {
        return userName.equals(( (User) o ).userName);
    }
    
    /**
     * Returns a String of the class attributes.
     *
     * @return Returns String of class attributes
     */
    @Override
    public String toString() {
        return userName;
        /*
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                '}';

         */
    }
    
    // TODO: search
}

