package com.deprez;

import java.util.ArrayList;
import java.util.List;

public class Community {
    private List<User> users;

    public Community() {
        users = new ArrayList<>();
    }

    public Community(List<User> users) {
        this.users = users;
    }
}
