package model;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDatabase {

    /**
     * Observable list of all users
     * @return all users in list form
     */
    public static ObservableList<User> getAllUsers(){
        ObservableList<User> userList = FXCollections.observableArrayList();
        try{
            // show all users in users table
            String sql = "SELECT * FROM users";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                int userID = resultSet.getInt("User_ID");
                String username = resultSet.getString("User_Name");
                User user = new User(userID,username);
                userList.add(user);
            }
        }  catch (SQLException throwables) {
            throwables.printStackTrace();
        } return userList;
    }

    /***
     *
     * @param userID user ID
     * @return username associated with userID
     */
    public static String getUserName(int userID) {
        String username = "Default";
        // show all users in users table that have a specified userID number
        try{
            String sql = "SELECT * FROM users WHERE User_ID = ?";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.setInt(1,userID);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                // find the username associated with that integer
                username = resultSet.getString("User_Name");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } return username;
    }
}
