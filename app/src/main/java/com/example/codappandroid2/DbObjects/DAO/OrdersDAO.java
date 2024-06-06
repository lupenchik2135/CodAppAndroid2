package com.example.codappandroid2.DbObjects.DAO;

import android.icu.text.SimpleDateFormat;

import com.example.codappandroid2.DbObjects.LegalEntities;
import com.example.codappandroid2.DbObjects.Orders;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class OrdersDAO {
    private final Connection con;

    public OrdersDAO(Connection con) {
        this.con = con;
    }

    //получение Orders из строки ResultSet
    private Orders getOrdersFromRS(ResultSet rs) throws SQLException {
        Orders result = new Orders();

        result.setId(rs.getInt("id"));
        result.setClientID(rs.getInt("clientID"));
        result.setOrderDate(rs.getDate("orderDate"));
        result.setDataCenterID(rs.getInt("dataCenterID"));
        result.setTotalAmount(rs.getFloat("totalAmount"));

        return result;
    }

    //получение Orders по id
    public Orders getOrderId(int clientid) {
        Orders result = null;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM orders WHERE id=?");
            ps.setInt(1, clientid);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = getOrdersFromRS(rs);
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    //получение списка всеx Orders
    public List<Orders> getOrdersListByClientId(int clientid) {
        List<Orders> result = new ArrayList<Orders>();
        try {

            PreparedStatement ps = con.prepareStatement("SELECT * FROM orders where clientid =?");
            ps.setInt(1, clientid);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                result.add(getOrdersFromRS(rs));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    // создание/обновление договора в базе
    public boolean registerOrder(int clientId, Date orderDate, int dataCenterId) {


        try {
            CallableStatement cs = con.prepareCall("call createorderprocedure(?, ?, ?)");

            cs.setInt(1, clientId);
            cs.setDate(2, orderDate);
            cs.setInt(3, dataCenterId);
            cs.execute();

            //             Получить уведомления
            SQLWarning warning = cs.getWarnings();
            if (warning != null) {
                System.out.println("Получено уведомление: " + warning.getMessage());
                return false;
            }

            cs.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
