package com.example.codappandroid2.DbObjects.DAO;

import com.example.codappandroid2.DbObjects.Client;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.ArrayList;
import java.util.List;

public class ClientsDAO {
    private final Connection con;

    public ClientsDAO(Connection con) {
        this.con = con;
    }

    //получение Client из строки ResultSet
    private Client getClientFromRS(ResultSet rs) throws SQLException {
        Client result = new Client();

        result.setId(rs.getInt("id"));
        result.setLogin(rs.getString("login"));
        result.setPassword(rs.getString("password"));
        result.setTotalSpent(rs.getFloat("totalSpent"));
        result.setBonusStatus(rs.getString("bonusStatus"));
        result.setBonuses(rs.getFloat("bonuses"));
        result.setManagerId(rs.getInt("managerId"));
        return result;
    }

    //получение Client по id
    public Client getClient(int id) {
        Client result = null;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM clients WHERE id=?");
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = getClientFromRS(rs);
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    //получение списка всего Client
    public List<Client> getClientList() {
        List<Client> result = new ArrayList<Client>();
        try {
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM clients");
            while (rs.next()) {
                result.add(getClientFromRS(rs));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    // создание/обновление договора в базе
    public boolean registerClient(String login, String password) {
        try {
            CallableStatement cs = con.prepareCall("call createclientprocedure(?, ?)");
            cs.setString(1, login);
            cs.setString(2, password);
            cs.execute();

            // Получить уведомления
//            SQLWarning warning = cs.getWarnings();
//            if (warning != null) {
//                System.out.println("Получено уведомление: " + warning.getMessage());
//                return warning;
//            }
            cs.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
//    public boolean loginClient(String login, String password) {
//        boolean result = false;
//
//        try {
//            String query = "SELECT loginClient(?, ?)";
//            PreparedStatement ps = con.prepareStatement(query);
//            ps.setString(1, login);
//            ps.setString(2, password);
//
//            ResultSet resultSet = ps.executeQuery();
//            if (resultSet.next()) {
//                result = resultSet.getBoolean(1);
//            }
//
//            resultSet.close();
//            ps.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return result;
//    }

    public Client loginClient(String login, String password) {
        Client client = new Client();;

        try {
//            String query = "SELECT id, login, password, totalSpent, bonusStatus, bonuses, managerId FROM loginclientfunction(?, ?)";
            String query = "SELECT id, login, password, totalSpent, bonuses, managerId FROM loginclientfunction(?, ?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, login);
            ps.setString(2, password);

            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                client.setId(resultSet.getInt("id"));
                client.setLogin(login);
                client.setPassword(password);
                client.setTotalSpent(resultSet.getFloat("totalSpent"));
//                client.setBonusStatus(resultSet.getString("bonusStatus"));
                client.setBonuses(resultSet.getFloat("bonuses"));
                client.setManagerId(resultSet.getInt("managerId"));
            }else client = null;

            resultSet.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return client;
    }
    //удаление
    public void deleteClient(int id) {
        try {
            PreparedStatement ps = con.prepareStatement("DELETE FROM clients WHERE id=?");
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

