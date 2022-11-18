package model;

import helper.JDBC;

import java.time.ZoneId;
import java.util.Locale;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Login {

    private static User userLoggedIn;
    private static Locale userLocale;
    private static ZoneId userZoneID;

    /*
    * --------------------------
    * BEGIN GETTERS AND SETTERS
    * --------------------------
     */

    /***
     *
     * @return getter for userLoggedIn.
     */
    public static User getUserLoggedIn() {
        return userLoggedIn;
    }

    /***
     *
     * @return Getter for userLocale
     */
    public static Locale getUserLocale() {
        return userLocale;
    }

    /***
     *
     * @return Getter for user zoneId
     */
    public static ZoneId getUserZoneID() {
        return userZoneID;
    }


    /*
     * --------------------------
     * END GETTERS AND SETTERS
     * --------------------------
     */


    /***
     * If username and password match credentials for user, marks that user as logged it.
     * @param username username
     * @param password password
     * @return true if login successful
     * @throws SQLException
     */
    public static boolean loggedIn(String username, String password) throws SQLException {
        // Find user that has matching username and password
        String sql = ("SELECT * FROM users WHERE User_Name = ? AND Password = ?") ;
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setString(1, username);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        if (rs.next()){
            // if login success then get users locale and zone ID, username
            String userNameLog = rs.getString("User_Name");
            int userIdLog = rs.getInt("User_ID");
            userLoggedIn = new User(userIdLog, userNameLog);
            userLocale = Locale.getDefault();
            userZoneID = ZoneId.systemDefault();
            return true;
        } else {
            return false;
        }
    }
}