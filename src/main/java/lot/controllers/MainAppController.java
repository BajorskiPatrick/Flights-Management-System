package lot.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import lot.controllers.menu.MenuController;

import java.io.IOException;

/**
 * Main application controller that handles navigation to different management modules.
 * Provides methods to switch between flight, passenger, and reservation management views.
 */
public class MainAppController {
    private Stage stage;

    /**
     * Constructs a new instance of the class with default values.
     * Initializes all fields to their default initial values.
     */
    public MainAppController() {}

    /**
     * Navigates to the flight management view.
     *
     * @param event the action event that triggered this navigation
     */
    @FXML
    private void manageFlights(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/lot/views/menu/MenuView.fxml"));
        try {
            setStage(event, loader, "flight");
        }
        catch (IOException e) {
            showErrorAlert(e.getMessage());
            return;
        }
        stage.show();
    }

    /**
     * Navigates to the reservation management view.
     *
     * @param event the action event that triggered this navigation
     */
    @FXML
    private void manageReservations(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/lot/views/menu/MenuView.fxml"));
        try {
            setStage(event, loader, "reservation");
        }
        catch (IOException e) {
            showErrorAlert(e.getMessage());
            return;
        }
        stage.show();
    }

    /**
     * Navigates to the passenger management view.
     *
     * @param event the action event that triggered this navigation
     */
    @FXML
    private void managePassengers(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/lot/views/menu/MenuView.fxml"));
        try {
            setStage(event, loader, "passenger");
        }
        catch (IOException e) {
            showErrorAlert(e.getMessage());
            return;
        }
        stage.show();
    }

    /**
     * Configures and displays the stage with the loaded view.
     *
     * @param event the action event that triggered this navigation
     */
    private void setStage(ActionEvent event, FXMLLoader loader, String type) throws IOException {
        Parent root = loader.load();
        MenuController controller = loader.getController();
        controller.setResourceType(type);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/lot/css/Menu.css").toExternalForm());
        stage.setScene(scene);
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Application exception");
        alert.setHeaderText("Unable to load Menu View");
        alert.setContentText("Details:\n" + message);
        alert.showAndWait();
    }
}
