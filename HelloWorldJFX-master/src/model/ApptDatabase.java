package model;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class ApptDatabase {

    /* ---------------------
     * BEGIN OBSERVABLE LISTS
     *
     * ---------------------*/

    /***
     *
     * @return all scheduled appointments in an observable list
     */
    public static ObservableList<Appointment> getAllAppointments() {
        ObservableList<Appointment> apptList = FXCollections.observableArrayList();
        try {
            String sql = "SELECT apt.Appointment_ID, apt.Title, apt.Description, apt.Location, apt.Type, apt.Start, " +
                    "apt.End, apt.Customer_ID, u.User_ID, c.Contact_ID, c.Contact_Name " +
                    "FROM appointments as apt " +
                    "JOIN customers as cust on apt.Customer_ID = cust.Customer_ID " +
                    "JOIN users as u on apt.User_ID = u.User_ID " +
                    "JOIN contacts as c on apt.Contact_ID = c.Contact_ID ";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                int apptID = resultSet.getInt("Appointment_ID");
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                String location = resultSet.getString("Location");
                String type = resultSet.getString("Type");
                LocalDateTime startTime = resultSet.getTimestamp("Start").toLocalDateTime();
                LocalDateTime endTime = resultSet.getTimestamp("End").toLocalDateTime();
                int customerID = resultSet.getInt("Customer_ID");
                int userID = resultSet.getInt("User_ID");
                int contactID = resultSet.getInt("Contact_ID");
                String contactName = resultSet.getString("Contact_Name");

                Appointment appointment = new Appointment(apptID, title, description, location, type, startTime, endTime, customerID, userID, contactID, contactName);
                apptList.add(appointment);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return apptList;
    }

    public static ObservableList<Appointment> getApptByID(int custID) throws SQLException {
        ObservableList<Appointment> customerApptList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM appointments WHERE Customer_ID = ? ";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setInt(1, custID);
        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()) {
            int apptID = resultSet.getInt("Appointment_ID");
            String title = resultSet.getString("Title");
            String description = resultSet.getString("Description");
            String location = resultSet.getString("Location");
            String type = resultSet.getString("Type");
            LocalDateTime startDate = resultSet.getTimestamp("Start").toLocalDateTime();
            LocalDateTime endDate = resultSet.getTimestamp("End").toLocalDateTime();
            int customerID = resultSet.getInt("Customer_ID");
            int userID = resultSet.getInt("User_ID");
            int contactID = resultSet.getInt("Contact_ID");
            String contactName = ContactsDatabase.getContactNameID(contactID);


            Appointment appointment = new Appointment(apptID, title, description, location, type, startDate, endDate, customerID, userID, contactID, contactName);
            customerApptList.add(appointment);
        }
        return customerApptList;
    }

    /**
     * Gets list of appointments assigned to contact from DB
     *
     * @param contact selected contact
     * @return list of contact appointments.
     * @throws SQLException
     */
    public static ObservableList<Appointment> getApptByContact(int contact) throws SQLException {
        ObservableList<Appointment> contactApptList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM appointments WHERE Contact_ID = ? ";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setInt(1, contact);
        ResultSet resultSet = ps.executeQuery();

        while (resultSet.next()) {
            int apptID = resultSet.getInt("Appointment_ID");
            String title = resultSet.getString("Title");
            String description = resultSet.getString("Description");
            String location = resultSet.getString("Location");
            String type = resultSet.getString("Type");
            LocalDateTime startDate = resultSet.getTimestamp("Start").toLocalDateTime();
            LocalDateTime endDate = resultSet.getTimestamp("End").toLocalDateTime();
            int customerID = resultSet.getInt("Customer_ID");
            int userID = resultSet.getInt("User_ID");
            int contactID = resultSet.getInt("Contact_ID");
            String contactName = ContactsDatabase.getContactNameID(contactID);


            Appointment appointment = new Appointment(apptID, title, description, location, type, startDate, endDate, customerID, userID, contactID, contactName);
            contactApptList.add(appointment);
        }
        return contactApptList;
    }

    /**
     * Gets all appointment types
     *
     * @return appointment type list
     * @throws SQLException
     */
    public static ObservableList<String> getApptTypes() throws SQLException {
        ObservableList<String> typeAppointmentList = FXCollections.observableArrayList();
        String sql = "SELECT DISTINCT * FROM appointments";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()) {
            String type = resultSet.getString("Type");
            typeAppointmentList.add(type);
        }
        return typeAppointmentList;
    }

    /**
     * Gets month name from appointments
     *
     * @return Months that have an appointment.
     * @throws SQLException
     */
    public static ObservableList<String> getApptMonths() throws SQLException {
        ObservableList<String> apptMonth = FXCollections.observableArrayList();
        String sql = "SELECT DISTINCT MONTHNAME(START) AS NAME FROM appointments";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()) {
            String month = resultSet.getString("NAME");
            apptMonth.add(month);
        }
        return apptMonth;
    }

    /**
     * Gets appointments matching the selected type.
     *
     * @param apptType selected type
     * @return number of appointments with that type
     * @throws SQLException
     */
    public static ObservableList<Appointment> getApptByType(String apptType, String apptMonth) throws SQLException {
        ObservableList<Appointment> typeAppointmentList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM appointments WHERE Type = ? AND MONTHNAME(START) = ? ";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setString(1, apptType);
        ps.setString(2, apptMonth);
        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()) {
            int apptID = resultSet.getInt("Appointment_ID");
            String title = resultSet.getString("Title");
            String description = resultSet.getString("Description");
            String location = resultSet.getString("Location");
            String type = resultSet.getString("Type");
            LocalDateTime startDate = resultSet.getTimestamp("Start").toLocalDateTime();
            LocalDateTime endDate = resultSet.getTimestamp("End").toLocalDateTime();
            int customerID = resultSet.getInt("Customer_ID");
            int userID = resultSet.getInt("User_ID");
            int contactID = resultSet.getInt("Contact_ID");
            String contactName = ContactsDatabase.getContactNameID(contactID);


            Appointment appointment = new Appointment(apptID, title, description, location, type, startDate, endDate, customerID, userID, contactID, contactName);
            typeAppointmentList.add(appointment);
        }
        return typeAppointmentList;
    }

    /**
     * Gets the number of appointments by month
     *
     * @param inputMonth selected month
     * @return number of appointments that month
     * @throws SQLException
     */
    public static ObservableList<Appointment> getApptByMonth(String inputMonth) throws SQLException {
        ObservableList<Appointment> monthAppointmentList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM appointments WHERE MONTHNAME(START) = ? ";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setString(1, inputMonth);
        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()) {
            int appointmentID = resultSet.getInt("Appointment_ID");
            String title = resultSet.getString("Title");
            String description = resultSet.getString("Description");
            String location = resultSet.getString("Location");
            String type = resultSet.getString("Type");
            LocalDateTime startDate = resultSet.getTimestamp("Start").toLocalDateTime();
            LocalDateTime endDate = resultSet.getTimestamp("End").toLocalDateTime();
            int customerID = resultSet.getInt("Customer_ID");
            int userID = resultSet.getInt("User_ID");
            int contactID = resultSet.getInt("Contact_ID");
            String contactName = ContactsDatabase.getContactNameID(contactID);

            Appointment appointment = new Appointment(appointmentID, title, description, location, type, startDate, endDate, customerID, userID, contactID, contactName);
            monthAppointmentList.add(appointment);
        }
        return monthAppointmentList;
    }

    /* ----------------------
     *  END OBSERVABLE LISTS
     * ----------------------- */

    /* ----------------------
     * BEGIN FUNCTIONALITY
     * -----------------------*/


    /**
     * Shows if customer has any appointments scheduled
     *
     * @param customerID ID number of customer in question
     * @return true if customer has appointment scheduled. False if no appts scheduled
     * @throws SQLException takes from SQL DB
     */
    public static boolean custHasAppt(int customerID) throws SQLException {
        boolean hasAppointment = false;
        String sql = "SELECT * FROM appointments WHERE Customer_ID = ? ";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setInt(1, customerID);
        ResultSet resultSet = ps.executeQuery();

        if (resultSet.next()) {
            hasAppointment = true;
        }
        return hasAppointment;
    }

    /***
     *
     * @return the current highest ID + 1 in order to auto increment IDs
     * @throws SQLException .
     */
    public static int getMaxID() throws SQLException {
        int nextID = 0;
        String sql = "SELECT max(Appointment_ID) AS Max_Appt_ID FROM appointments";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()) {
            nextID = resultSet.getInt("Max_Appt_ID");
        }
        return nextID;
    }

    /**
     * This resets the auto increment after an appointment has been deleted
     *
     * @throws SQLException
     */
    public static void resetAutoIncrement() throws SQLException {
        String sql = "ALTER TABLE appointments AUTO_INCREMENT = 1";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.executeUpdate();
    }

    /***
     *
     * @param title appt title
     * @param description appt description
     * @param location appt location
     * @param type appt type
     * @param startDT start time in type LocalDateTime
     * @param endDT end time in type LocalDateTime
     * @param dateCreated date appt was created
     * @param userCreatedBy user who created it
     * @param lastUpdate last time it was updated
     * @param lastUpdatedBy the person who did the last update
     * @param customerID id of the customer
     * @param userID id of the user
     * @param contactID contact id of the contact for the customer for whom this appt is being added
     * @throws SQLException .
     */
    public static void addNewApptDB(String title, String description, String location, String type, LocalDateTime startDT, LocalDateTime endDT, LocalDateTime dateCreated, String userCreatedBy, LocalDateTime lastUpdate, String lastUpdatedBy, int customerID, int userID, int contactID) throws SQLException {
        String sql = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES (?, ?, ?, ?, ?, ?, ? ,? ,? , ?, ?, ?, ? )";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setTimestamp(5, Timestamp.valueOf(startDT));
        ps.setTimestamp(6, Timestamp.valueOf(endDT));
        ps.setString(7, String.valueOf(dateCreated));
        ps.setString(8, userCreatedBy);
        ps.setString(9, String.valueOf(lastUpdate));
        ps.setString(10, lastUpdatedBy);
        ps.setInt(11, customerID);
        ps.setInt(12, userID);
        ps.setInt(13, contactID);
        ps.executeUpdate();
    }

    /***
     *
     * @param title appt title
     * @param description appt description
     * @param location appt location
     * @param type appt type
     * @param lastUpdate last time it was updated
     * @param lastUpdatedBy the person who did the last update
     * @param customerID id of the customer
     * @param userID id of the user
     * @param contactID contact id of the contact for the customer for whom this appt is being added
     * @throws SQLException .
     */
    public static void modifyAppt(int apptID, String title, String description, String location, String type, LocalDateTime startTime, LocalDateTime endTime, LocalDateTime lastUpdate, String lastUpdatedBy, int customerID, int userID, int contactID) throws SQLException {
        String sql = "UPDATE appointments SET Title = ?, Description = ?, Location = ? , Type = ?, Start = ?, End = ?, Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ?  WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setTimestamp(5, Timestamp.valueOf(startTime));
        ps.setTimestamp(6, Timestamp.valueOf(endTime));
        ps.setString(7, String.valueOf(lastUpdate));
        ps.setString(8, lastUpdatedBy);
        ps.setInt(9, customerID);
        ps.setInt(10, userID);
        ps.setInt(11, contactID);
        ps.setInt(12, apptID);
        ps.executeUpdate();
    }

    /**
     * @param appointmentID Delete chosen item
     * @throws SQLException
     */
    public static void deleteChosenAppointment(int appointmentID) throws SQLException {
        String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, appointmentID);
        ps.executeUpdate();
    }
}

    /* ---------------------
    *
    * END FUNCTIONALITY
    *
     -----------------------*/