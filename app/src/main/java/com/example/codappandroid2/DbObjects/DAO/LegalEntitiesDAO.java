package com.example.codappandroid2.DbObjects.DAO;

import com.example.codappandroid2.DbObjects.LegalEntities;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.ArrayList;
import java.util.List;

public class LegalEntitiesDAO {
    private final Connection con;

    public LegalEntitiesDAO(Connection con) {
        this.con = con;
    }

    //получение LegalEntities из строки ResultSet
    private LegalEntities getIndividualFromRS(ResultSet rs) throws SQLException {
        LegalEntities result = new LegalEntities();

        result.setId(rs.getInt("id"));
        result.setCompanyName(rs.getString("companyName"));
        result.setContactName(rs.getString("contactName"));
        result.setInn(rs.getString("iNN"));
//        result.setoGRN(rs.getString("oGRN"));
//        result.setLicense(rs.getString("license"));
//        result.setLegalAddress(rs.getString("legalAddress"));
//        result.setRealAddress(rs.getString("realAddress"));
        result.setContactEmail(rs.getString("contactEmail"));
        result.setContactName(rs.getString("contactNumber"));
        result.setClientID(rs.getInt("clientID"));

        return result;
    }

    //получение LegalEntities по id
    public LegalEntities getLegalEntity(int id) {
        LegalEntities result = null;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM individuals WHERE id=?");
            ps.setInt(1, id);

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

    //получение списка всеx LegalEntities
    public List<LegalEntities> getLegalEntitiesList() {
        List<LegalEntities> result = new ArrayList<LegalEntities>();
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
    public boolean registerLegalEntity(String FirstName, String SecondName, String ThirdName, String Email, int ClientID) {
        try {
//            CallableStatement cs = con.prepareCall("call createindividualprocedure(?, ?, ?, ?, ?, ?, ?)");
            CallableStatement cs = con.prepareCall("call createindividualprocedure(?, ?, ?, ?, ?, ? )");
            cs.setString(1, FirstName);
            cs.setString(2, SecondName);
            cs.setString(3, ThirdName);
            cs.setString(4, Email);
            cs.setString(5, Email);
            cs.setInt(6, ClientID);
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

    public LegalEntities loginLegalEntity(String login, String password) {
        LegalEntities legalEntities = new LegalEntities();;

        try {
//            String query = "SELECT id, firstName, secondName, thirdName, email, phoneNumber, birthDay, clientID FROM loginindividualfunction(?, ?)";
            String query = "SELECT id, firstName, secondName, thirdName, email, clientID FROM loginindividualfunction(?, ?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, login);
            ps.setString(2, password);

            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                legalEntities.setId(resultSet.getInt("id"));
                legalEntities.setCompanyName(resultSet.getString("companyName"));
                legalEntities.setContactName(resultSet.getString("contactName"));
                legalEntities.setInn(resultSet.getString("iNN"));
//        legalEntities.setoGRN(resultSet.getString("oGRN"));
//        legalEntities.setLicense(resultSet.getString("license"));
//        legalEntities.setLegalAddress(resultSet.getString("legalAddress"));
//        legalEntities.setRealAddress(resultSet.getString("realAddress"));
                legalEntities.setContactEmail(resultSet.getString("contactEmail"));
                legalEntities.setContactName(resultSet.getString("contactNumber"));
                legalEntities.setClientID(resultSet.getInt("clientID"));
            }else legalEntities = null;

            resultSet.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return legalEntities;
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