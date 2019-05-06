package com.deprez;

import jdk.nashorn.internal.ir.FunctionCall;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.IntToDoubleFunction;

public class Community {
    private List<User> users;
    private List<Integer> sorted;
    // TODO: Javadoc
    // TODO: Fast Search
    // TODO: Sort (Merge)

    public Community() {
        users = new ArrayList<>();
    }

    public Community(List<User> users) {
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void addUser(User user) throws AlreadyExistsException {
        if (hasUser(user.getUserName())) {
            throw new AlreadyExistsException("User with same userName '" + user.getUserName() + "' was found in " + this.getClass().getName());
        } else {
            users.add(user);
        }
    }

    public void addUser(String userName) throws AlreadyExistsException {
        if (hasUser(userName)) {
            throw new AlreadyExistsException("User with same userName '" + userName + "' was found in " + this.getClass().getName());
        } else {
            users.add(new User(users.size(), userName));
        }
    }

    public boolean hasUser(User user) {
        // TODO: Implement binary search after sort is complete
        String userName = user.getUserName();
        for (User usr : users) {
            if (usr.getUserName().equals(userName)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param <I> functional interface type.
     */
    public class Recursive<I> {
        public I func;
    }

    public interface MyFunctionalInterface {
        public int myfunc(int l, int r);
    }

    /**
     * Assumes the list is sorted, and uses a binary search to search for the userName.
     *
     * @param userName the userName to search for.
     * @return returns the index if found, otherwise returns -1.
     */
    public int hasUser(String userName) {
        // TODO: Implement binary search after sort is complete

        Recursive<MyFunctionalInterface> binarySearch = new Recursive<>();
        binarySearch.func = (int l, int r) -> {
            if (r >= 1) {
                int mid = l + (r - l) / 2;

                if (users.get(mid).getUserName().equals(userName))
                    return mid;

                if (users.get(mid).getUserName().compareTo(userName) > 0)
                    return binarySearch.func.myfunc(l, r);

                return binarySearch.func.myfunc(mid + 1, r);

            }

            return -1;
        };
        return -1;
    }

    public void sortByName() {
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
     *
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
}
