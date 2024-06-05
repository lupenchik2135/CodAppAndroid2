package com.example.codappandroid2;

import android.widget.Toast;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection implements Serializable {

    private String urlClient = "jdbc:postgresql://192.168.25.1:1488/postgres";
    private String userClient = "client";
    private String passwordClient = "client";

    public String getUrlClient() {
        return urlClient;
    }

    public String getUserClient() {
        return userClient;
    }

    public String getPasswordClient() {
        return passwordClient;
    }

    public Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(urlClient, userClient, passwordClient);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
