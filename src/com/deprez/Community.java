package com.deprez;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

/**
 * The Community class manages the list of {@link com.deprez.User}
 * plain java objects. It can sort the list and change anything about the list.
 *
 * @see com.deprez.User
 */
public class Community {

    /**
     * The {@link java.util.List} attribute used to manage the list.
     */
    private List<User> users;
    // TODO: Javadoc
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
            Driver.LOGGER.log(Level.FINE, "User with same userName '" + userName + "' was found in " + this.getClass().getName());
        } else {
            users.add(new User(users.size() + 1, userName));
        }
    }

    /**
     * Assumes the list is sorted, and uses a binary search to search for the userName.
     *
     * @param userName the userName to search for.
     * @return returns the index if found, otherwise returns -1.
     */
    public int hasUser(String userName) {
        // TODO: Implement binary search after sort is completed
        sortByName();
        int i = 0;
        for (User user : users) {
            if(user.getUserName().equals(userName)) {
                return i;
            }
            i++;
        }
        return -1;
        // return (binarySearch(userName) >= 0);
    }

    /**
     * Searches the list with a binary search.
     * Assumes the list is sorted by name.
     * TODO: FIX DIS!
     * @param userName the username that is searched for
     * @return The index of the username, which should correspond to the userId as well. Returns <code>-1</code> if not found.
     */
    private int binarySearch(String userName) {
        int left = 0, right = users.size() - 1, mid;

        while (1 <= right) {
            mid = left + (right - left) / 2;

            if (users.get(mid).getUserName().equals(userName)) {
                return mid;
            } else if (users.get(mid).getUserName().compareTo(userName) > 0) {
                right = mid - 1;
            } else if (mid != users.size() - 1) {
                left = mid + 1;
            } else if (left == right) {
                break;
            }
        }


        /*
        if (right >= 1) {
            int mid = left + (right - left) / 2;

            if (users.get(mid).getUserName().equals(userName)) {
                return mid;
            }

            if (users.get(mid).getUserName().compareTo(userName) >= 0) { // TODO: Check this expression.
                return binarySearch(left, mid - 1, userName);
            }

            return binarySearch(mid + 1, right, userName);
        }
         */

        return -1;
    }

    private void sortByName() {
        Collections.sort(users, User::compareToName);

        /*
        if (!(users.size() <= 1)) {

            int midpoint = users.size() / 2;
            List<User> left = new ArrayList<>(users.subList(0, midpoint));
            List<User> right = new ArrayList<>(users.subList((users.size() % 2 == 0) ? midpoint : midpoint + 1, users.size()));

            // List<User> result = Stream.concat(left.stream(), right.stream()).collect(Collectors.toList());
            List<User> result = new ArrayList<>(left.size() + right.size());

            int leftPtr = 0, rightPtr = 0, resultPtr = 0;

            while (leftPtr < left.size() || rightPtr < right.size()) {
                if (leftPtr < left.size() && rightPtr < right.size()) {
                    if (left.get(leftPtr).compareToName(right.get(rightPtr)) < 0) {
                        result.add(resultPtr++, left.get(leftPtr++));
                    } else {
                        result.add(resultPtr++, right.get(rightPtr++));
                    }
                } else if (leftPtr < left.size()) {
                    result.add(resultPtr++, left.get(leftPtr++));
                } else if (rightPtr < right.size()) {
                    result.add(resultPtr++, right.get(rightPtr++));
                }
            }

            users = result;
        }
         */
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

    /**
     * Returns a User object based on userName, otherwise returns null
     *
     * @param userName the userName to search for
     * @return the `User` object with a matching user
     */
    public User getUser(String userName) {
        int temp = hasUser(userName);
        return (temp >= 0) ? users.get(temp) : null;
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
        return users.get(userId).getUserName();
    }

    public int getUserId(String userName) {
        return getUser(userName).getUserId();
    }
}
