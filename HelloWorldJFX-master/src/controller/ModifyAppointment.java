package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

public class ModifyAppointment implements Initializable {
    public Label usernameDisplay;
    public Label locationDisplay;
    public Button logoutButton;
    public Button apptButton;
    public Button custButton;
    public Button reportsButton;
    public TextField apptIDTxt;
    public TextField titleTxt;
    public TextField descriptionTxt;
    public TextField locationTxt;
    public TextField apptTypeTxt;
    public TextField customerIDTxt;
    public TextField userIDTxt;
    public ComboBox contactDrop;
    public DatePicker dateDrop;
    public Button cancelButton;
    public Button modifyButton;
    public TextField startTxt;
    public TextField endTxt;
    int i;

    /*
    * -------------------------
    BEGIN NAVIGATION AND LOGOUT
    * -------------------------
    */

    /***
     *
     * @param actionEvent When logout button clicked, verifies user's intent to log out, if "OK"
     *                    clicked then application closes.
     */
    public void logoutButtonClick(ActionEvent actionEvent) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Exit Action");
        confirm.setHeaderText("Are you sure you want to logout and close application?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }

    /***
     *
     * @param actionEvent When appointment button is clicked, navigates to appointment page.
     * @throws IOException .
     */
    public void apptButtonClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainDashboard.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Appointment Scheduler");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /***
     *
     * @param actionEvent When customer button is clicked, navigtes to customer page.
     * @throws IOException .
     */
    public void custButtonClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Customers.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Customer Log");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /***
     *
     * @param actionEvent When reports button is clicked, navigate to the reports page.
     * @throws IOException .
     */
    public void reportsButtonClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Reports.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Reports");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /*
    * -------------------------
    END NAVIGATION AND LOGOUT
    * -------------------------
    */


    /*
    * -------------------------
    BEGIN PAGE FUNCTIONALITY
    * -------------------------
    */

    /***
     *
     * @param actionEvent When modify button is clicked, sends info from text fields/combo boxes/date pickers
     *                    to the SQL database, which will update the table view on the main appointment page
     * @throws IOException Provides errors if invalid information is chosen.
     * @throws SQLException Adjusts time zone to UTC for entry into SQL database.
     */
    public void modifyButtonClick(ActionEvent actionEvent) throws IOException, SQLException {
        boolean apptModified = false;
        int apptID;
        String title;
        String description;
        String location;
        String type;
        LocalDateTime now = LocalDateTime.now();
        LocalTime start;
        LocalTime end;
        LocalDateTime startTime;
        LocalDateTime endTime;
        LocalDate chosenDate;
        ZonedDateTime zonedStartDT;
        ZonedDateTime zonedEndDT;
        int customerID;
        int userID;
        String username;
        int contactID;
        String contactName;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        apptID = Integer.parseInt(apptIDTxt.getText());
        title = titleTxt.getText();
        description = descriptionTxt.getText();
        location = locationTxt.getText();
        type = apptTypeTxt.getText();
        start = LocalTime.parse(startTxt.getText(), formatter);
        end = LocalTime.parse(endTxt.getText(), formatter);
        chosenDate = dateDrop.getValue();
        startTime = LocalDateTime.of(chosenDate, start);
        endTime = LocalDateTime.of(chosenDate, end);
        zonedStartDT = ZonedDateTime.of(startTime, Login.getUserZoneID());
        zonedEndDT = ZonedDateTime.of(endTime, Login.getUserZoneID());
        customerID = Integer.parseInt(customerIDTxt.getText());
        userID = Integer.parseInt(userIDTxt.getText());
        username = User.getUsername();
        contactName = String.valueOf(contactDrop.getValue());
        contactID = ContactsDatabase.getContactID(contactName);

        System.out.println(convertToLocalTime(startTime));

        if (title.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please enter title.");
            alert.showAndWait();
        } else if (description.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please enter description.");
            alert.showAndWait();
        } else if (location.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please enter location.");
            alert.showAndWait();
        } else if (type.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please enter appointment type.");
            alert.showAndWait();
        } else if (startTime.toString().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please enter start time.");
            alert.showAndWait();
        } else if (endTime.toString().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please enter end time.");
            alert.showAndWait();
        } else if (contactName.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please select contact name.");
            alert.showAndWait();
        } else {

            if (Appointment.withinBusinessHours(startTime) && Appointment.withinBusinessHours(endTime)) {
                //System.out.println("got thru within business hours");
                if (startTime.toLocalTime().isBefore(endTime.toLocalTime())) {
                    System.out.println("Before checkTimeAvail");
                    if (Appointment.checkTimeAvailability(customerID, chosenDate, start, end, apptID)) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Desired appointment time not available.");
                        alert.setContentText("Please choose a different time and try again.");
                        alert.showAndWait();
                        System.out.println("Inside checkTimeAvail");
                    } else {
                        System.out.println("MA Check Time Avail: " + Appointment.checkTimeAvailability(customerID, chosenDate, start, end, apptID));
                        System.out.println("MA" + chosenDate);
                        System.out.println("MA Start: " + start + " | End: " + end );
                        System.out.println("After checkTimeAvail");
                        zonedStartDT = zonedStartDT.withZoneSameInstant(ZoneOffset.UTC);
                        zonedEndDT = zonedEndDT.withZoneSameInstant(ZoneOffset.UTC);

                        Appointment modAppt = new Appointment(apptID, title, description, location, type, startTime, endTime, customerID, userID, contactID, contactName);
                        Appointment.modifyAppt(i, modAppt);
                        ApptDatabase.modifyAppt(apptID, title, description, location, type, startTime, endTime, now, username, customerID, userID, contactID);
                        apptModified = true;
                        System.out.println("Appt added true");
                    }
                    if (apptModified) {
                        Parent root = FXMLLoader.load(getClass().getResource("/view/MainDashboard.fxml"));
                        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                        stage.setScene(new Scene(root));
                        stage.show();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Invalid start or end time.");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Start or end time is not within business hours.");
                alert.setContentText("Please enter times between 08:00 and 22:00 EST.");
                alert.showAndWait();
            }
        }
    }

    /***
     *
     * @param chosenAppointment pulls data about appointment that the user has selected from the table to the
     *                          textfields/date picker/combo boxes
     * @throws SQLException
     */
    public void sendAppointments(Appointment chosenAppointment) throws SQLException {
        apptIDTxt.setText(String.valueOf(chosenAppointment.getApptID()));
        titleTxt.setText(chosenAppointment.getTitle());
        descriptionTxt.setText(chosenAppointment.getDescription());
        locationTxt.setText(chosenAppointment.getLocation());
        contactDrop.setItems(ContactsDatabase.getContactName());
        contactDrop.setValue(chosenAppointment.getContactName());
        apptTypeTxt.setText(chosenAppointment.getType());
        dateDrop.setValue(chosenAppointment.getStartTime().toLocalDateTime().toLocalDate());
        //System.out.println("sendAppointments1" + chosenAppointment.getStartTime().toLocalDateTime().toLocalTime());
        startTxt.setText(String.valueOf(chosenAppointment.getStartTime().toLocalDateTime().toLocalTime()));
        //System.out.println("sendAppointments2" + startTxt.getText());
        //System.out.println("sendAppointments3" + chosenAppointment.getStartTime().toLocalDateTime().toLocalTime());
        endTxt.setText(String.valueOf(chosenAppointment.getEndTime().toLocalDateTime().toLocalTime()));
        customerIDTxt.setText(String.valueOf(chosenAppointment.getCustomerID()));
        userIDTxt.setText(String.valueOf(chosenAppointment.getUserID()));
    }

    /***
     *
     * @param chosenIndex retrieves index of chosen appointment
     */
    public void sendIndex(int chosenIndex) {
        i = chosenIndex;
    }

    /***
     *
     * @param startTime used to convert to local time
     * @return
     */
    public LocalTime convertToLocalTime(LocalDateTime startTime) {
        return startTime.toLocalTime();
    }

    /***
     *
     * @param actionEvent when cancel button is clicked, confirms users intent to cancel,
     *                    if "OK" is clicked, navigates to main dashboard/appts page
     * @throws IOException .
     */
    public void cancelButtonClick(ActionEvent actionEvent) throws IOException {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Cancel");
        confirm.setHeaderText("Are you sure you would like to cancel?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Parent root = FXMLLoader.load(getClass().getResource("/view/MainDashboard.fxml"));
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Appointment Scheduler");
            stage.setScene(new Scene(root));
            stage.show();
        }
    }

    /*
    * -------------------------
    END PAGE FUNCTIONALITY
    * -------------------------
    */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        locationDisplay.setText(String.valueOf(Login.getUserZoneID()));
        usernameDisplay.setText(String.valueOf(User.getUsername()));
        try {
            contactDrop.setItems(ContactsDatabase.getContactName());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}