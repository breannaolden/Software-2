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
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * The type Add appointment.
 */
public class AddAppointment implements Initializable {
    /**
     * The Username display.
     */
    public Label usernameDisplay;
    /**
     * The Location display.
     */
    public Label locationDisplay;
    /**
     * The Logout button.
     */
    public Button logoutButton;
    /**
     * The Appt button.
     */
    public Button apptButton;
    /**
     * The Cust button.
     */
    public Button custButton;
    /**
     * The Reports button.
     */
    public Button reportsButton;
    /**
     * The Appt id txt.
     */
    public TextField apptIDTxt;
    /**
     * The Title txt.
     */
    public TextField titleTxt;
    /**
     * The Description txt.
     */
    public TextField descriptionTxt;
    /**
     * The Location txt.
     */
    public TextField locationTxt;
    /**
     * The Appt type txt.
     */
    public TextField apptTypeTxt;
    /**
     * The Customer id txt.
     */
    public TextField customerIDTxt;
    /**
     * The User id txt.
     */
    public TextField userIDTxt;
    /**
     * The Contact drop.
     */
    public ComboBox contactDrop;
    /**
     * The Date drop.
     */
    public DatePicker dateDrop;
    /**
     * The Cancel button.
     */
    public Button cancelButton;
    /**
     * The Add button.
     */
    public Button addButton;
    /**
     * The Start txt.
     */
    public TextField startTxt;
    /**
     * The End txt.
     */
    public TextField endTxt;

    /*
     * ---------------------------
     * BEGIN NAVIGATION AND LOGOUT
     * ---------------------------
     */

    /***
     *
     * @param actionEvent When clicked, user is prompted to confirm they would like to log out,                    then the application is closed if the user clicks "OK"
     */
    public void logoutButtonClick(ActionEvent actionEvent) {
        // alert popup
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Exit Action");
        confirm.setHeaderText("Are you sure you want to logout and close application?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // close app withut error
            System.exit(0);
        }
    }

    /***
     *
     * @param actionEvent When clicked, navigates to the appointment page
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
     * @param actionEvent When clicked, navigates to the customer page
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
     * @param actionEvent When clicked, navigates to the reports page
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
     * ---------------------------
     * END NAVIGATION AND LOGOUT
     * ---------------------------
     */




    /*
     * ---------------------------
     * BEGIN APP FUNCTIONALITY
     * ---------------------------
     */

    /***
     *
     * @param actionEvent When clicked, confirms user intent to cancel addition of appointment
     * @throws IOException If "OK" is clicked, navigates to main appointment page
     */
    public void cancelButtonClick(ActionEvent actionEvent) throws IOException {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Cancel");
        confirm.setHeaderText("Are you sure you would like to cancel?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Parent root = FXMLLoader.load(getClass().getResource("/view/MainDashboard.fxml"));
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Home Screen");
            stage.setScene(new Scene(root));
            stage.show();
        }
    }

    /***
     *
     * @param actionEvent When add button is clicked, gets data from text boxes/combo boxes/date pickers                    and adds a new appointment with that data to the SQL database.
     * @throws IOException Will provide a variety of error messages if there are any text boxes, etc. that are left blank.
     * @throws SQLException .
     */
    public void addButtonClick(ActionEvent actionEvent) throws IOException, SQLException {
        boolean apptAdded = false;
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
        int customerID;
        int userID;
        String userCreatedBy;
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
        customerID = Integer.parseInt(customerIDTxt.getText());
        userID = Integer.parseInt(userIDTxt.getText());
        userCreatedBy = User.getUsername();
        contactName = String.valueOf(contactDrop.getValue());
        contactID = ContactsDatabase.getContactID(contactName);
        // System.out.println("about to check errors");
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
        } else if (start.toString().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please enter start time in the format HH:MM, 24 hr time.");
            alert.showAndWait();
        } else if (end.toString().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please enter end time in the format HH:MM, 24 hr time.");
            alert.showAndWait();
        } else if (contactName.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please enter contact name.");
            alert.showAndWait();
        } else if (customerID > CustomerDatabase.getMaxID()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("CustomerID Invalid");
            alert.showAndWait();

        } else {
            //System.out.println("Got thru all errors ADD APPT");
            if (Appointment.withinBusinessHours(startTime) && Appointment.withinBusinessHours(endTime)) {
                //System.out.println("got thru within business hours ADD APPT");
                if (startTime.toLocalTime().isBefore(endTime.toLocalTime())) {
                    if (Appointment.checkTimeAvailability(customerID, chosenDate, start, end, apptID)) {
                        //System.out.println("got thru check time avail ADD APPT");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Desired appointment time is not available.");
                        alert.setContentText("Please select a different time.");
                        alert.showAndWait();
                    } else {
                        //System.out.println("about to add appt ADDAPPT");
                        Appointment appointment = new Appointment(apptID, title, description, location, type, startTime, endTime, customerID, userID, contactID, contactName);
                        Appointment.addAppt(appointment);
                        ApptDatabase.addNewApptDB(title, description, location, type, startTime, endTime, now, userCreatedBy, now, userCreatedBy, customerID, userID, contactID);
                        apptAdded = true;
                        System.out.println("Appt added true");
                    }
                    if (apptAdded) {
                        Parent root = FXMLLoader.load(getClass().getResource("/view/MainDashboard.fxml"));
                        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                        stage.setTitle("Main Dashboard");
                        stage.setScene(new Scene(root));
                        stage.show();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Invalid start or end time dynamic.");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Please enter a time within business hours: 08:00 - 22:00 EST");
                alert.showAndWait();
            }
        }
    }

    /*
     * ---------------------------
     * END APP FUNCTIONALITY
     * ---------------------------
     */

    /***
     *
     * @param url Lambda used to ensure no new appointments are scheduled for dates in the past.
     * @param resourceBundle Only utilized in add appointments because the company may need to adjust
     *                       previously scheduled appointment if mistakes were made or inaccurate
     *                       scheduling was completed.
     * Pulls username and time zones to automatically populate labels at top left of app.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Lambda
        dateDrop.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate dateBox, boolean empty) {
                super.updateItem(dateBox, empty);
                setDisable(
                        empty || dateBox.getDayOfWeek() == DayOfWeek.SATURDAY || dateBox.getDayOfWeek() == DayOfWeek.SUNDAY || dateBox.isBefore(LocalDate.now()));
            }
        });
        locationDisplay.setText(String.valueOf(Login.getUserZoneID()));
        usernameDisplay.setText(String.valueOf(User.getUsername()));
        try {
            apptIDTxt.setText(String.valueOf(ApptDatabase.getMaxID() + 1));
            contactDrop.setItems(ContactsDatabase.getContactName());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}