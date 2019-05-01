package com.deprez;

import java.util.ArrayList;
import java.util.List;

public class Community {
    private List<User> users;
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

    public void addUser(User user) {
        if(hasUser(user)) {
            // do nothing
        } else {
            users.add(user);
        }
    }

    public void addUser(String userName) {
        if(hasUser(userName)) {

        } else {
            users.add(new User(users.size(), userName));
        }
    }

    public boolean hasUser(User user) {
        String userName = user.getUserName();
        for (User usr : users) {
            if(usr.getUserName().equals(userName)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasUser(String userName) {
        for (User usr : users) {
            if(usr.getUserName().equals(userName)) {
                return true;
            }
        }
        return false;
    }

    public void removeUser(String userName) {
        int i = 0;
        for (User usr : users) {
            if(usr.getUserName().equals(userName)) {
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
}
