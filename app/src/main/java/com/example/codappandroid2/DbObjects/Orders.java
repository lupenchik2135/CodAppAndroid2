package com.example.codappandroid2.DbObjects;

import java.sql.Date;

public class Orders {
    private int id;
    private int clientID;
    private Date orderDate;
    private int dataCenterID;
    private float totalAmount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public int getDataCenterID() {
        return dataCenterID;
    }

    public void setDataCenterID(int dataCenterID) {
        this.dataCenterID = dataCenterID;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }
}
