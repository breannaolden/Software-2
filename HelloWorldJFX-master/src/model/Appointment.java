package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;


public class Appointment {
    int apptID;
    String title;
    String description;
    String location;
    String type;
    LocalDateTime startTime;
    LocalDateTime endTime;
    int customerID;
    int userID;
    int contactID;
    String contactName;
    private static ObservableList<Appointment> appointments = ApptDatabase.getAllAppointments();

    /***
     * Constructor for appointment
     * @param apptID appointment ID
     * @param title appointment title
     * @param description appointment description
     * @param location appointment location
     * @param type appointment type
     * @param startTime starting time of appointment
     * @param endTime ending time of appointment
     * @param customerID ID of customer
     * @param userID ID of user
     * @param contactID contact ID of contact
     * @param contactName name of contact
     */
    public Appointment(int apptID, String title, String description, String location, String type, LocalDateTime startTime, LocalDateTime endTime, int customerID, int userID, int contactID, String contactName) {
        this.apptID = apptID;
        this.title = title;
        this.location = location;
        this.description = description;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.customerID = customerID;
        this.userID = userID;
        this.contactID = contactID;
        this.contactName = contactName;
    }

    /*
     *----------------------------------
     * ESTABLISHING SETTERS AND GETTERS
     * ---------------------------------
     */

    /***
     *
     * @return Getter for apptID
     */
    public int getApptID() {
        return apptID;
    }

    /***
     *
     * @param apptID setter for apptID
     */
    public void setApptID(int apptID) {
        this.apptID = apptID;
    }


    /***
     *
     * @return Getter for title
     */
    public String getTitle() {
        return title;
    }

    /***
     *
     * @param title Setter for title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /***
     *
     * @return Getter for location
     */
    public String getLocation() {
        return location;
    }

    /***
     *
     * @param location Setter for location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /***
     *
     * @return Getter for description
     */
    public String getDescription() {
        return description;
    }

    /***
     *
     * @param description Setter for description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /***
     *
     * @return Getter for type
     */
    public String getType() {
        return type;
    }

    /***
     *
     * @param type Setter for type
     */
    public void setType(String type) {
        this.type = type;
    }

    /***
     *
     * @return Getter for start time
     */
    public Timestamp getStartTime() {
        return Timestamp.valueOf(startTime);
    }

    /***
     *
     * @param startTime Setter for start time
     */
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    /***
     *
     * @return Getter for end time
     */
    public Timestamp getEndTime() {
        return Timestamp.valueOf(endTime);
    }

    /***
     *
     * @param endTime Setter for end time
     */
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    /***
     *
     * @return Getter for customer ID
     */
    public int getCustomerID() {
        return customerID;
    }

    /***
     *
     * @param customerID Setter for customer ID
     */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /***
     *
     * @return Getter for user ID
     */
    public int getUserID() {
        return userID;
    }

    /***
     *
     * @param userID Setter for userID
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /***
     *
     * @return Getter for contact ID
     */
    public int getContactID() {
        return contactID;
    }

    /***
     *
     * @param contactID Setter for contact ID
     */
    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    /***
     *
     * @return Getter for contact name
     */
    public String getContactName() {
        return contactName;
    }

    /***
     *
     * @param contactName setter for contact name
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /*
     * ---------------------------
     * COMPLETED SETTERS AND GETTERS
     * ---------------------------
     */


    /***
     *
     * @param appointment adds appointment
     */
    public static void addAppt(Appointment appointment) {
        appointments.add(appointment);
    }

    /***
     *
     * @param chosenAppointment deletes the selected appointment
     */
    public static void deleteAppt(Appointment chosenAppointment) {
        appointments.remove(chosenAppointment);
    }

    /***
     *
     * @param i the index of the selected appointment
     * @param appointment pulls data of the appointment at index i in order to modify the data
     */
    public static void modifyAppt(int i, Appointment appointment) {
        appointments.set(i, appointment);
    }

    /***
     *
     * @return returns list of all appointments
     */
    public static ObservableList<Appointment> getAllAppointments() {
        return appointments;
    }

    /***
     *
     * @param startTime the time of the appointment
     * @return converts to users local time from utc
     */
    public LocalTime convertToLocalTime(LocalDateTime startTime) {
        return startTime.toLocalTime();
    }

    /***
     * sends alert at login if there is an appointment within 15 minutes.
     * If no appointment, congratulates users for being prepared and loggin in early.
     */
    public static void nextAppointment() {
        if (Appointment.comingAppt() != null) {
            for (int i = 0; i< comingAppt().size(); i++) {
                Appointment nextAppointment = comingAppt().get(i);
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Upcoming Appointment");
                alert.setHeaderText("You have an appointment scheduled in the next 15 minutes.");
                alert.setContentText("Appointment ID: " + nextAppointment.getApptID() + " begins at " + nextAppointment.getStartTime());
                alert.show();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No upcoming appointments.");
            alert.setHeaderText("No appointment scheduled within the next 15 minutes.");
            alert.setContentText("Great job logging in early!");
            alert.show();
        }
    }

    /***
     * Utilized by nextAppointment in order to tell if there is appt within 15 min
     * @return null
     */
    public static ObservableList<Appointment> comingAppt() {
        LocalDateTime apptStartTime = LocalDateTime.now(Login.getUserZoneID());
        LocalDateTime apptEndTime = apptStartTime.plusMinutes(15);
        ObservableList<Appointment> comingAppointment = FXCollections.observableArrayList();
        for (Appointment searchedAppt : appointments) {
            ZonedDateTime zonedSearch = ZonedDateTime.from(searchedAppt.getStartTime().toLocalDateTime().atZone(Login.getUserZoneID()));
            zonedSearch = zonedSearch.withZoneSameInstant(Login.getUserZoneID());
            LocalDateTime searchedTime = zonedSearch.toLocalDateTime();
            if (searchedTime.isAfter(apptStartTime) && searchedTime.isBefore(apptEndTime)) {
                comingAppointment.add(searchedAppt);
                return comingAppointment;
            }
        }
        return null;
    }

    /***
     *
     * @param apptTime the appointment start time
     * @return if the appointment is within business hours
     */
    public static boolean withinBusinessHours(LocalDateTime apptTime) {
        ZonedDateTime apptTimeZone = apptTime.atZone(ZoneId.systemDefault());
        apptTimeZone = apptTimeZone.withZoneSameInstant(ZoneId.of("US/Eastern"));
        apptTime = apptTimeZone.toLocalDateTime();

        LocalTime open = LocalTime.of(8,0);
        LocalTime closed = LocalTime.of(22,0);
        boolean isItOpen = false;

        System.out.println("first check");

        if (apptTime.toLocalTime().isAfter(open) || apptTime.toLocalTime().equals(open)) {
            if (apptTime.toLocalTime().isBefore(closed)) {
                isItOpen = true;
            } else {
                isItOpen = false;
            }
        } else {
            isItOpen = false;
        }
        System.out.println("2nd check");
        return (isItOpen);
    }

    /***
     *  checks if there is already an appointment at the time requested by this new appointment
     *
     * @param customerID the customer id
     * @param newDate the proposed date of this appt
     * @param newStart the proposed start time of this appt
     * @param newEnd the proposed end time of this appt
     * @param apptID the appt id
     * @return if there is availability at the time requested
     * @throws SQLException .
     */
    public static boolean checkTimeAvailability(int customerID, LocalDate newDate, LocalTime newStart, LocalTime newEnd, int apptID) throws SQLException {
        ObservableList<Appointment> customerApptsList = ApptDatabase.getApptByID(customerID);
        boolean concurrentAppt = false;
        LocalDate startDate;
        LocalTime apptStartTime;
        LocalTime apptEndTime;
        for (int i = 0; i < customerApptsList.size(); i++) {
            Appointment appt = customerApptsList.get(i);
            startDate = appt.getStartTime().toLocalDateTime().toLocalDate();
            if (apptID == appt.getApptID()){
                System.out.println("Appt check time 1");
                return false;
            } else if (startDate.equals(newDate)) {
                apptStartTime = appt.getStartTime().toLocalDateTime().toLocalTime();
                apptEndTime = appt.getEndTime().toLocalDateTime().toLocalTime();
                System.out.println("Appt check time 2");
                System.out.println("Appt" + startDate);
                System.out.println("Appt Start: " + apptStartTime + " | End: " + apptEndTime );
                if (newStart.equals(apptStartTime) && (newEnd.equals(apptEndTime))) {
                    concurrentAppt = true;
                    System.out.println("check time 3 " + concurrentAppt);
                    return concurrentAppt;
                } else if (newEnd.isAfter(apptStartTime) && newEnd.isBefore(apptEndTime)) {
                    concurrentAppt = true;
                    System.out.println("check time 4");
                    return concurrentAppt;
                } else if (newStart.isBefore(apptEndTime) && newStart.isAfter(apptStartTime)) {
                    concurrentAppt = true;
                    System.out.println("check time 5");
                    return concurrentAppt;
                } else if ((newStart.isBefore(apptStartTime) || newStart.equals(apptStartTime)) && newEnd.isAfter(apptEndTime)) {
                    concurrentAppt = true;
                    System.out.println("check time 6");
                    return concurrentAppt;
                } else if ((newStart.isBefore(apptStartTime)) && (newEnd.equals(apptEndTime))) {
                    concurrentAppt = true;
                    return concurrentAppt;
                } else if((newStart.isBefore(apptStartTime)) && (newEnd.isBefore(apptEndTime) || newEnd.isAfter(apptEndTime))) {
                    concurrentAppt = true;
                    return concurrentAppt;
                }
            }
        }
        System.out.println("Concurrent appointment: " + concurrentAppt);
        return concurrentAppt;
    }

    /***
     *
     * @return appointments that are happening this month
     */
    public static ObservableList<Appointment> getMonthlyAppt() {
        ObservableList<Appointment> monthlyAppt = FXCollections.observableArrayList();
        LocalDateTime startTime = LocalDateTime.now(Login.getUserZoneID());
        LocalDateTime endTime = startTime.plusMonths(1);

        appointments.forEach(appointment -> {
            LocalDateTime searchedTime = appointment.getStartTime().toLocalDateTime();
            if (searchedTime.isAfter(startTime) && searchedTime.isBefore(endTime)) {
                monthlyAppt.add(appointment);
            }
        });
        return monthlyAppt;
    }

    /***
     *
     * @return appointments that are happening this week
     */
    public static ObservableList<Appointment> getWeeklyAppt() {
        LocalDateTime apptStartTime = LocalDateTime.now(Login.getUserZoneID());
        LocalDateTime apptEndTime = apptStartTime.plusWeeks(1);
        ObservableList<Appointment> weeklyAppt = FXCollections.observableArrayList();

        for (Appointment searchedAppt : appointments) {
            LocalDateTime searchedTime = searchedAppt.getStartTime().toLocalDateTime();
            if (searchedTime.isAfter(apptStartTime) && searchedTime.isBefore(apptEndTime)) {
                weeklyAppt.add(searchedAppt);
            }
        }
        return weeklyAppt;
    }
}

