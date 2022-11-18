package model;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ContactsDatabase {

    /**
     *
     * @return All contacts in an observalbe list
     * @throws SQLException uses data from SQL DB
     */
    public static ObservableList<String> getContactName() throws SQLException {
        ObservableList<String> contactList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM contacts";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ResultSet resultSet = ps.executeQuery();
        while(resultSet.next()) {
            String contactName = resultSet.getString("Contact_Name");
            contactList.add(contactName);
        } return contactList;
    }

    /**
     * This finds the contact ID associated with the contact name
     * @param contactName contact name
     * @return contact ID
     * @throws SQLException
     */
    public static int getContactID(String contactName) throws SQLException {
        int contactID = 0;
        String sql = "SELECT * FROM contacts WHERE Contact_Name = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setString(1, contactName);
        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()) {
            contactID = resultSet.getInt("Contact_ID");
        } return contactID;
    }

    /***
     * This finds the contact name associated with a contact ID
     * @param contactID contactID
     * @return contactName
     * @throws SQLException
     */
    public static String getContactNameID(int contactID) throws SQLException {
        String contactName ="";
        String sql = "SELECT * FROM contacts WHERE Contact_ID = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setInt(1, contactID);
        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()) {
            contactName = resultSet.getString("Contact_Name");
        } return contactName;
    }


}