package com.deprez;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import static java.util.Collections.swap;

/**
 * The Community class manages the list of {@link com.deprez.User} plain java objects. It can sort the list and change
 * anything about the list.
 *
 * @see com.deprez.User
 */
public class Community {
    
    /**
     * The {@link java.util.List} attribute used to manage the list.
     */
    private List<User> users;
    // TODO: Fast Search
    // TODO: Sort (Merge)
    
    /**
     * Creates a {@link com.deprez.Community} object.
     */
    public Community() {
        users = new ArrayList<>();
    }
    
    /**
     * Creates a {@link com.deprez.Community} object.
     *
     * @param users an existing list of users
     */
    public Community(List<User> users) {
        this.users = users;
    }
    
    /**
     * Returns the list of users
     *
     * @return the {@link java.util.List} of users, sorted by userId
     */
    public List<User> getUsers() {
        Collections.sort(users, User::compareToId);
        return users;
    }
    
    /**
     * Sets the {@link java.util.List} of the {@link com.deprez.Community#users}
     *
     * @param users replacement {@link java.util.List} of {@link com.deprez.User} objects
     */
    public void setUsers(List<User> users) {
        this.users = users;
    }
    
    /**
     * Adds a new user to the list of users.
     *
     * @param userName the userName to create a new user with.
     */
    public void addUser(String userName) {
        if (hasUser(userName) >= 0) {
            Driver.LOGGER.log(Level.FINE,
                              "User with same userName '" + userName + "' was found in " + this.getClass().getName());
        } else {
            users.add(new User(users.size() + 1, userName));
        }
    }
    
    /**
     * Assumes the list is sorted, and uses a binary search to search for the userName.
     *
     * @param userName the userName to search for.
     *
     * @return returns the index if found, otherwise returns -1.
     */
    public int hasUser(String userName) {
        // TODO: Implement binary search after sort is completed
        sortByName();
        int i = 0;
        for (User user : users) {
            if (user.getUserName().equals(userName)) {
                return i;
            }
            i++;
        }
        return -1;
        // return (binarySearch(userName) >= 0);
    }
    
    private void sortByName() {
        Collections.sort(users, User::compareToName);
    }
    
    /**
     * Sorts the list alphabetically Calls {@link com.deprez.Community#quickSort(List, int, int)}
     *
     * @see Community#quickSort(List, int, int)
     * @see java.util.Collection
     * @see java.util.Collections
     */
    public void quickSort() {
        quickSort(users, 0, users.size());
    }
    
    /**
     * Sorts the list alphabetically
     *
     * @param userList the userList reference
     * @param leftPtr  the pointer to the left side of the list
     * @param rightPtr the point to the right side of the list
     *
     * @see Community#quickSort()
     * @see Community#partition(List, int, int, User)
     * @see Community#nameBinarySearch(String)
     * @see java.util.Collection
     * @see java.util.Collections
     */
    private void quickSort(List<User> userList, int leftPtr, int rightPtr) {
        if (leftPtr >= rightPtr) {
            return;
        }
        
        User pivot = userList.get((leftPtr + rightPtr) / 2);
        int index = partition(userList, leftPtr, rightPtr, pivot);
        quickSort(userList, leftPtr, index - 1);
        quickSort(userList, index, rightPtr);
    }
    
    /**
     * Partitions the {@link java.util.List} of {@link com.deprez.User} objects to divide and conquer the list
     *
     * @param userList the {@link com.deprez.Community#users} reference
     * @param leftPtr  the pointer to the left side of the partition
     * @param rightPtr the pointer to the right side of the partition
     * @param pivot    the pivot point, in which the left and right list items positions are swapped
     *
     * @return returns the leftPtr position as the new pivot point
     *
     * @see Community#quickSort()
     * @see Community#quickSort(List, int, int)
     * @see Community#nameBinarySearch(String)
     * @see java.util.Collection
     * @see java.util.Collections
     */
    private int partition(List<User> userList, int leftPtr, int rightPtr, User pivot) {
        while (leftPtr <= rightPtr) {
            while (userList.get(leftPtr).compareToName(pivot) > 0) {
                leftPtr++;
            }
            
            while (userList.get(rightPtr).compareToName(pivot) < 0) {
                rightPtr--;
            }
            
            if (leftPtr <= rightPtr) {
                swap(userList, leftPtr, rightPtr);
                leftPtr++;
                rightPtr++;
            }
        }
        
        return leftPtr;
    }
    
    /**
     * Returns the index of the userName, assumes the list is sorted by {@link com.deprez.User} userName
     *
     * @param userName the userName to search for
     *
     * @return the index of the userName, returns -1 if not found.
     *
     * @see Community#quickSort()
     * @see Community#quickSort(List, int, int)
     * @see Community#partition(List, int, int, User)
     * @see java.util.Collection
     * @see java.util.Collections
     */
    private int nameBinarySearch(String userName) {
        int leftPtr = 0, rightPtr = users.size() - 1, mid;
        
        while (leftPtr <= rightPtr) {
            mid = (leftPtr + rightPtr) / 2;
            
            String midUserName = users.get(mid).getUserName();
            if (userName.compareTo(midUserName) < 0) {
                rightPtr = mid - 1;
            } else if (userName.compareTo(midUserName) > 0) {
                leftPtr = mid + 1;
            } else {
                return mid;
            }
        }
        
        return -1;
    }
    
    /**
     * Returns the index of the userId, assumes the list is sorted by {@link com.deprez.User} userId
     *
     * @param userId the userId to search for
     *
     * @return the index of the userId, returns -1 if not found
     */
    private int idBinarySearch(int userId) {
        int leftPtr = 0, rightPtr = users.size() - 1, mid;
        
        while (leftPtr <= rightPtr) {
            mid = (leftPtr + rightPtr) / 2;
            
            int midUserId = users.get(mid).getUserId();
            if (userId < midUserId) {
                rightPtr = mid - 1;
            } else if (userId > midUserId) {
                leftPtr = mid + 1;
            } else {
                return mid;
            }
        }
        
        return -1;
    }
    
    /**
     * Removes a user with a matching userName from the list.
     * <p>
     * TODO: Implement a binary search.
     *
     * @param userName the userName of the User object to remove.
     */
    public void removeUser(String userName) {
        int i = 0;
        for (User usr : users) {
            if (usr.getUserName().equals(userName)) {
                users.remove(i);
                break;
            }
            i++;
        }
    }
    
    public void removeUser(User user) {
        users.remove(user);
    }
    
    /**
     * Returns the list of users as a String.
     *
     * @return return the users list as a String.
     */
    @Override
    public String toString() {
        return "Community{" +
               "users=" + users +
               '}';
    }
    
    /**
     * Clears the users implicit parameter.
     */
    public void clear() {
        users.clear();
    }
    
    /**
     * Returns the size of the implicit users list.
     *
     * @return returns the users list length.
     */
    public int size() {
        return users.size();
    }
    
    public void addUserApp(String userName, App app) {
        int temp = hasUser(userName);
        if (temp >= 0) {
            // TODO users.get(temp).addApp(app);
        }
    }
    
    public void addUserAppReviewToUser(String userName, String appName, int reviewScore, String reviewDetail) {
        /*
        TODO
        int tmp = hasUser(userName);
        if (tmp  >= 0) {
            // TODO
            users.get(tmp).addUserAppReview(userName, appName, reviewScore, reviewDetail);
        }
        */
    }
    
    public List<App> getUserAppReviews(String userName) {
        // TODO
        return null;
    }
    
    public String getUserName(int userId) {
        for (User user : users) {
            if (user.getUserId() == userId) {
                return user.getUserName();
            }
        }
        return "USER NOT FOUND";
        //return users.get(userId).getUserName();
    }
    
    public int getUserId(String userName) {
        return getUser(userName).getUserId();
    }
    
    /**
     * Returns a User object based on userName, otherwise returns null
     *
     * @param userName the userName to search for
     *
     * @return the `User` object with a matching user
     *         <p>
     *         TODO: fix this doc
     */
    public User getUser(String userName) {
        /*int temp = hasUser(userName);
        return (temp >= 0) ? users.get(temp) : null;*/
    
        for (User user : users) {
            if (user.getUserName().equals(userName)) {
                return user;
            }
        }
        return null;
    }
    
    public String[][] toTable() {
        Collections.sort(users, User::compareToId);
        String[][] table = new String[users.size()][2];
        
        int i = 0;
        for (User user : users) {
            table[i][0] = Integer.toString(user.getUserId());
            table[i][1] = user.getUserName();
            i++;
        }
        
        return table;
    }
}
