package lot.controllers;

import javafx.event.ActionEvent;
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
    private Scene scene;
    private Parent root;

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
    public void manageFlights(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/lot/views/menu/MenuView.fxml"));
        try {
            setStage(event, loader);
        }
        catch (IOException e) {
            showErrorAlert(e.getMessage());
            return;
        }
        MenuController controller = loader.getController();
        controller.setResourceType("flight");
        stage.show();
    }

    /**
     * Navigates to the reservation management view.
     *
     * @param event the action event that triggered this navigation
     */
    public void manageReservations(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/lot/views/menu/MenuView.fxml"));
        try {
            setStage(event, loader);
        }
        catch (IOException e) {
            showErrorAlert(e.getMessage());
            return;
        }
        MenuController controller = loader.getController();
        controller.setResourceType("reservation");
        stage.show();
    }

    /**
     * Navigates to the passenger management view.
     *
     * @param event the action event that triggered this navigation
     */
    public void managePassengers(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/lot/views/menu/MenuView.fxml"));
        try {
            setStage(event, loader);
        }
        catch (IOException e) {
            showErrorAlert(e.getMessage());
            return;
        }
        MenuController controller = loader.getController();
        controller.setResourceType("passenger");
        stage.show();
    }

    /**
     * Configures and displays the stage with the loaded view.
     *
     * @param event the action event that triggered this navigation
     */
    private void setStage(ActionEvent event, FXMLLoader loader) throws IOException {
        root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
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
