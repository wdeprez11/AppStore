package com.deprez;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

    public boolean hasUser(String userName) {
        // TODO: Implement binary search after sort is complete
        for (User user : users) {
            if (user.getUserName().equals(userName)) {
                return true;
            }
        }
        return false;
    }

    public void sortByName() {
        Collections.sort(users, new UserComparator());
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

    @Override
    public String toString() {
        return "Community{" +
                "users=" + users +
                '}';
    }

    public void clear() {
        users.clear();
    }

    public int size() {
        return users.size();
    }
}

class UserComparator implements Comparator<User> {

    @Override
    public int compare(User user, User t1) {
        return user.compareToName(t1);
    }
}
