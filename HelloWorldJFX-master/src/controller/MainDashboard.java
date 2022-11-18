package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.ApptDatabase;
import model.Login;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainDashboard implements Initializable {
    public Label usernameDisplay;
    public Label locationDisplay;
    public Button logoutButton;
    public Button apptButton;
    public Button custButton;
    public Button reportsButton;
    public RadioButton monthRadio;
    public ToggleGroup ViewBy;
    public RadioButton weekRadio;
    public TableView<Appointment> apptTable;
    public TableColumn<Appointment, Integer> apptIDColumn;
    public TableColumn<Appointment, String> titleColumn;
    public TableColumn<Appointment, String> descColumn;
    public TableColumn<Appointment, String> locationColumn;
    public TableColumn<Appointment, String> contactColumn;
    public TableColumn<Appointment, String> typeColumn;
    public TableColumn<Appointment, ZonedDateTime> startColumn;
    public TableColumn<Appointment, ZonedDateTime> endColumn;
    public TableColumn<Appointment, Integer> custIDColumn;
    public TableColumn<Appointment, Integer> userIDColumn;
    public Button addButton;
    public Button modifyButton;
    public Button deleteButton;
    public RadioButton allTimeRadio;


    /*
     * ---------------------------
     * BEGIN NAVIGATION AND LOGOUT
     * ---------------------------
     */
    public void logoutButtonClick(ActionEvent actionEvent) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Exit Action");
        confirm.setHeaderText("Are you sure you want to log out and close application?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }

    /***
     *
     * @param actionEvent unused button click because this is the current page.
     */
    public void apptButtonClick(ActionEvent actionEvent) {
    }

    /***
     *
     * @param actionEvent When customer button is clicked, navigates to main customer page
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
     * @param actionEvent When reports button clicked, navigates to reports page.
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
     * BEGIN PAGE FUNCTIONALITY
     * ---------------------------
     */

    /***
     *
     * @param actionEvent When toggle button selected, shows all appointments within the current month
     */
    public void monthRadioClick(ActionEvent actionEvent) {
        apptTable.setItems(Appointment.getMonthlyAppt());
    }

    /***
     *
     * @param actionEvent When toggle button selected, shows all appointments within current week
     */
    public void weekRadioClick(ActionEvent actionEvent) {
        apptTable.setItems(Appointment.getWeeklyAppt());
    }

    /***
     *
     * @param actionEvent When toggle button selected, shows all appointments. Created this to "reset" after
     *                    monthly or weekly toggle button selected.
     */
    public void allTimeRadioClick(ActionEvent actionEvent) { apptTable.setItems(Appointment.getAllAppointments());
    }

    /***
     *
     * @param actionEvent When add button is clicked, navigates to the add appointment page
     * @throws IOException .
     */
    public void addButtonClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddAppointment.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Add New Appointment");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /***
     *
     * @param actionEvent When modify button is clicked, collects data from SQL database to allow for it to
     *                    be populated into text boxes/combo boxes/date pickers on modify appt page.
     * @throws SQLException Provides error if no appointment is selected
     */
    public void modifyButtonClick(ActionEvent actionEvent) throws SQLException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ModifyAppointment.fxml"));
            loader.load();
            ModifyAppointment modifyAppointment = loader.getController();
            modifyAppointment.sendAppointments(apptTable.getSelectionModel().getSelectedItem());
            modifyAppointment.sendIndex(apptTable.getSelectionModel().getSelectedIndex());
            Parent root = loader.getRoot();
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Modify Existing Appointment");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setTitle("No appointment selected.");
            alert.setContentText("Please selected appointment to modify.");
            alert.show();
        } catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    /***
     *
     * @param actionEvent When delete buton clicked, if appt is selected, verifies user intent to delete
     *                    chosen appointment. If "OK" clicked, appointment is deleted from SQL database.
     * @throws SQLException Provides error if no appointment is selected.
     */
    public void deleteButtonClick(ActionEvent actionEvent) throws SQLException {
        Appointment deleteAPPT = apptTable.getSelectionModel().getSelectedItem();
        if (deleteAPPT == null) {
            // if no appt is selected
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No appointment selected.");
            alert.setContentText("Please select appointment for deletion.");
            alert.show();
        } else {
            // PER EVALUATOR REPORT: added appt ID and appt type to delete message
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Delete Action");
            confirm.setHeaderText("Are you sure you want to delete this appointment?");
            confirm.setContentText("Appointment ID: " + deleteAPPT.getApptID() + "   |   Appointment Type: " + deleteAPPT.getType());
            Optional<ButtonType> result = confirm.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // if user confirms, delete appt ID first, then appt, then reset the auto increment
                int deleteApptID = deleteAPPT.getApptID();
                ApptDatabase.deleteChosenAppointment(deleteApptID);
                Appointment.deleteAppt(deleteAPPT);
                ApptDatabase.resetAutoIncrement();
            }
        }
    }

    /*
     * ---------------------------
     * END PAGE FUNCTIONALITY
     * ---------------------------
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
    }
}