package model;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FirstLevelDatabase {

    /*
    * ----------------------
    * BEGIN OBSERVABLE LISTS
    * ----------------------
     */

    /***
     *
     * @param countryName name of country
     * @return observable list of country division
     * @throws SQLException
     */
    public static ObservableList<String> getDivision(String countryName) throws SQLException {
        int countryID;
        // set the country ID based on the country name
        if (countryName.equals("U.S")) {
            countryID = 1;
        } else if (countryName.equals("UK")) {
            countryID = 2;
        } else {
            countryID = 3;
        }
        ObservableList<String> countryFirstLevelDivisions = FXCollections.observableArrayList();
        // show all items in first_level_divisions table that have the associated country ID
        String sql = "SELECT * FROM first_level_divisions WHERE Country_ID = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setInt(1, countryID);
        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()) {
            // show only the Division column
            String countryDivision = resultSet.getString("Division");
            countryFirstLevelDivisions.add(countryDivision);
        } return countryFirstLevelDivisions;
    }

    /**
     * @return observable list of country names
     * @throws SQLException .
     */
    public static ObservableList<String> getCountries() throws SQLException {
        ObservableList<String> countryList = FXCollections.observableArrayList();
        // show all entries in countries table
        String sql = "SELECT * FROM countries ";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()) {
            // show only the column of country names
            String countryName = resultSet.getString("Country");
            countryList.add(countryName);
        } return countryList;
    }

    /*
    * ---------------------
    * END OBSERVABLE LISTS
    * ---------------------
     */

    /**
     * @param firstLevelDivisionName name of division
     * @return first level division ID
     * @throws SQLException .
     */
    public static int getFirstLevelDivisionID(String firstLevelDivisionName) throws SQLException {
        int firstLevelDivisionID = 0;
        String sql = "SELECT * FROM first_level_divisions WHERE Division = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setString(1, firstLevelDivisionName);
        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()) {
            firstLevelDivisionID = resultSet.getInt("Division_ID");
        } return firstLevelDivisionID;
    }
}



