package com.example.codappandroid2.DbObjects.DAO;

import com.example.codappandroid2.DbObjects.Clients;
import com.example.codappandroid2.DbObjects.Individuals;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.ArrayList;
import java.util.List;

public class IndividualsDAO {
    private final Connection con;

    public IndividualsDAO(Connection con) {
        this.con = con;
    }

    //получение Individual из строки ResultSet
    private Individuals getIndividualFromRS(ResultSet rs) throws SQLException {
        Individuals result = new Individuals();

        result.setId(rs.getInt("id"));
        result.setFirstName(rs.getString("firstName"));
        result.setSecondName(rs.getString("secondName"));
        result.setThirdName(rs.getString("thirdName"));
        result.setEmail(rs.getString("email"));
//        result.setPhoneNumber(rs.getString("phoneNumber"));
//        result.setBirthDay(rs.getDate("birthDay"));
        result.setClientID(rs.getInt("clientID"));
        return result;
    }

    //получение Individual по clientid
    public Individuals getIndividualByClient(int clientid) {
        Individuals result = null;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM individuals WHERE clientid=?");
            ps.setInt(1, clientid);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = getIndividualFromRS(rs);
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    //получение списка всеx Individual
    public List<Individuals> getIndividualsList() {
        List<Individuals> result = new ArrayList<Individuals>();
        try {
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM individuals");
            while (rs.next()) {
                result.add(getIndividualFromRS(rs));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    // создание/обновление договора в базе
    public boolean registerIndividual(String FirstName, String SecondName, String ThirdName, String Email, int ClientID) {
        try {
//            CallableStatement cs = con.prepareCall("call createindividualprocedure(?, ?, ?, ?, ?, ?, ?)");
            CallableStatement cs = con.prepareCall("call createindividualprocedure(?, ?, ?, ?, ?)");
            cs.setString(1, FirstName);
            cs.setString(2, SecondName);
            cs.setString(3, ThirdName);
            cs.setString(4, Email);
//            cs.setString(5, phoneNumber);
//            cs.setDate(6, birthDay);
            cs.setInt(5, ClientID);
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

    public Individuals loginIndividual(String login, String password) {
        Individuals individuals = new Individuals();;

        try {
//            String query = "SELECT id, firstName, secondName, thirdName, email, phoneNumber, birthDay, clientID FROM loginindividualfunction(?, ?)";
            String query = "SELECT id, firstName, secondName, thirdName, email, clientID FROM loginindividualfunction(?, ?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, login);
            ps.setString(2, password);

            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                individuals.setId(resultSet.getInt("id"));
                individuals.setFirstName(resultSet.getString("firstName"));
                individuals.setSecondName(resultSet.getString("secondName"));
                individuals.setThirdName(resultSet.getString("thirdName"));
                individuals.setEmail(resultSet.getString("email"));
//                individuals.setPhoneNumber(resultSet.getString("phoneNumber"));
//                individuals.setBirthDay(resultSet.getDate("birthDay"));
                individuals.setClientID(resultSet.getInt("clientID"));;
            }else individuals = null;

            resultSet.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return individuals;
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
