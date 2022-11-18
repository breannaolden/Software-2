package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class Reports implements Initializable {
    public Label usernameDisplay;
    public Label locationDisplay;
    public Button logoutButton;
    public Button apptButton;
    public Button custButton;
    public Button reportsButton;
    public TableView apptTable;
    public TableColumn apptIDColumn;
    public TableColumn titleColumn;
    public TableColumn descColumn;
    public TableColumn locationColumn;
    public TableColumn contactColumn;
    public TableColumn typeColumn;
    public TableColumn startColumn;
    public TableColumn endColumn;
    public TableColumn custIDColumn;
    public TableColumn userIDColumn;
    public ComboBox<String> avgMonthDrop;
    public TextField avgDailyApptsTxt;
    public ComboBox contactDrop;
    public TextField apptTypeByMonthTxt;
    public ComboBox<String> monthDrop;
    public ComboBox<String> apptTypeDrop;
    public Button apptTypeMonthButton;
    public Button contactScheduleButton;
    public Button avgDailyButton;
    public TextField totalApptsTxt;

    /*
    * -------------------------
    BEGIN NAVIGATION AND LOGOUT
    * -------------------------
    */

    /***
     *
     * @param actionEvent When clicked, logs user out and closes application
     */
    public void logoutButtonClick(ActionEvent actionEvent) {
        navAndErrors(1);
    }

    /***
     *
     * @param actionEvent When clicked, navigates to appointments page
     * @throws IOException .
     */
    public void apptButtonClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainDashboard.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Main Reports Page");
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
        stage.setTitle("Main Reports Page");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /***
     *
     * @param actionEvent user is on reports page, therefore no code here, user does not need to navigate anywhere.
     */
    public void reportsButtonClick(ActionEvent actionEvent) {
    }

    /*
    * -------------------------
    END NAVIGATION AND LOGOUT
    * -------------------------
    */





    /*
    * -------------------------
    BEGIN APP FUNCTIONALITY
    * -------------------------
    */

    /***
     *
     * @param actionEvent When clicked, triggers calculation of number of appointments of a certain type
     *                    scheduled within the selected month.
     * @throws SQLException .
     */
    public void apptTypeMonthButtonClick(ActionEvent actionEvent) throws SQLException {
        String TYPE = apptTypeDrop.getValue();
        String month = monthDrop.getValue();
        int numAppts = ApptDatabase.getApptByType(TYPE, month).size();
        apptTypeByMonthTxt.setText(String.valueOf(numAppts));
    }

    /***
     *
     * @param actionEvent when clicked, populate tableview with appointments of each contact
     * @throws SQLException .
     */
    public void contactScheduleButtonClick(ActionEvent actionEvent) throws SQLException {
        String contactName = String.valueOf(contactDrop.getValue());
        int contactID = ContactsDatabase.getContactID(contactName);
        if (ApptDatabase.getApptByContact(contactID).isEmpty()) {
            apptTable.setItems(ApptDatabase.getApptByContact(contactID));
            navAndErrors(2);
        } else {
            apptTable.setItems(ApptDatabase.getApptByContact(contactID));
        }
    }


    /***
     *
     * @param actionEvent calculates average daily visits by taking the total num
     *                    appts per month divided by the total num days in the month.
     *                    Use an array to identify which months have 30 days and
     *                    which have 31 days.
     *
     * @throws SQLException If more appointments were included in the database (as
     * there would be in real life), this information would be more meaningful.
     */
    public void avgDailyButtonClick(ActionEvent actionEvent) throws SQLException {
        String month = avgMonthDrop.getValue();
        int numApptsMonth = ApptDatabase.getApptByMonth(month).size();
        int avgDaily;

        String[] thirtyDays = new String[]{"September", "April", "June", "November"};
        boolean thirtyDaysBool = false;
        String[] thirtyOneDays = new String[]{"January", "March", "May", "July", "August", "October", "December"};
        boolean thirtyOneDaysBool = false;
        String[] twentyEightDays = new String[]{"February"};
        boolean twentyEightDaysBool = false;

        for (String s : thirtyOneDays) {
            if (month.equals(s)) {
                thirtyOneDaysBool = true;
            }
        }

        for (String s : thirtyDays) {
            if (month.equals(s)) {
                thirtyDaysBool = true;
            }
        }

        for (String s : twentyEightDays) {
            if (month.equals(s)) {
                twentyEightDaysBool = true;
            }
        }

        if (thirtyOneDaysBool) {
            avgDaily = numApptsMonth/31;
        } else if (thirtyDaysBool) {
            avgDaily = numApptsMonth/30;
        } else if (twentyEightDaysBool){
            avgDaily = numApptsMonth/28;
        } else {
            avgDaily = 0;
            navAndErrors(3);
        }

        avgDailyApptsTxt.setText(String.valueOf(avgDaily));
        totalApptsTxt.setText(String.valueOf(numApptsMonth));

    }

    /***
     *
     * @param nav Lambda expression to simplify navigation and errors where possible.
     *            PROS: Condenses code for improved readability.
     */
    public static void navAndErrors(int nav) {
        switch (nav) {
            case 1 -> {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Confirm Exit Action");
                confirm.setHeaderText("Are you sure you want to logout and close application?");
                Optional<ButtonType> result = confirm.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    System.exit(0);
                }
            }
            case 2 -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error");
                alert.setTitle("This contact has no appointments.");
                alert.setContentText("Please select another contact and try again.");
                alert.show();

            }
            case 3 -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error");
                alert.setTitle("Invalid month selection");
                alert.setContentText("Please select valid month and try again.");
                alert.show();
            }
        }
    }

    /*
    * -------------------------
    END APP FUNCTIONALITY
    * -------------------------
    */

    /***
     *
     * @param url populates username and location to top left of application
     * @param resourceBundle populates drop boxes with required data
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        apptTable.setItems(Appointment.getAllAppointments());
        apptIDColumn.setCellValueFactory(new PropertyValueFactory<>("apptID"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        custIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        userIDColumn.setCellValueFactory(new PropertyValueFactory<>("userID"));
        locationDisplay.setText(String.valueOf(Login.getUserZoneID()));
        usernameDisplay.setText(String.valueOf(User.getUsername()));

        try {
            contactDrop.setItems(ContactsDatabase.getContactName());
            monthDrop.setItems(ApptDatabase.getApptMonths());
            apptTypeDrop.setItems(ApptDatabase.getApptTypes());
            avgMonthDrop.setItems(ApptDatabase.getApptMonths());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}