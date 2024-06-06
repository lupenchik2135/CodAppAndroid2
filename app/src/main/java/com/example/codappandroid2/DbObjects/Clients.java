package com.example.codappandroid2.DbObjects;

import java.io.Serializable;

public class Clients implements Serializable {
    private int id;
    private String login;
    private String password;
    private float totalSpent;
    private String bonusStatus;
    private float bonuses;
    private int managerId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public float getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(float totalSpent) {
        this.totalSpent = totalSpent;
    }

    public String getBonusStatus() {
        return bonusStatus;
    }

    public void setBonusStatus(String bonusStatus) {
        this.bonusStatus = bonusStatus;
    }

    public float getBonuses() {
        return bonuses;
    }

    public void setBonuses(float bonuses) {
        this.bonuses = bonuses;
    }

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }
    @Override
    public String toString() {
        return "Order{" +
                "\nid=" + id +
                "\nlogin=" + login +
                "\npassword=" + password +
                "\ntotalSpent=" + totalSpent +
                "\nbonusStatus=" + bonusStatus +
                "\nbonuses=" + bonuses +
                "\nmanagerId=" + managerId +
                '}';
    }
}

