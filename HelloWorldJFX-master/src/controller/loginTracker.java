package controller;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class loginTracker {
    private static final String logFile = "login_activity.txt";

    /***
     *  Logs to txt file any login attempt, successful or failed
     * @param username username entered
     * @param loginSuccess if username and pass matches database
     */
    public static void loginTracking(String username, boolean loginSuccess) {
        try {
            if (!loginSuccess) {
                BufferedWriter logger = new BufferedWriter(new FileWriter(logFile, true));
                logger.append(String.valueOf(ZonedDateTime.now(ZoneOffset.UTC))).append(" | Login attempted by: ").append(username).append(" | Login Status: Failed \n");
                logger.flush();
                logger.close();
            } else {
                BufferedWriter logger = new BufferedWriter(new FileWriter(logFile, true));
                logger.append(String.valueOf(ZonedDateTime.now(ZoneOffset.UTC))).append(" | Login attempted by: ").append(username).append(" | Login Status: Successful \n");
                logger.flush();
                logger.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
