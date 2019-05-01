package com.deprez;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private String url;

    public Database() {
        url = "jdbc:sqlite:default.db";
    }

    public Database(String url) {
        this.url = url;
    }
}
