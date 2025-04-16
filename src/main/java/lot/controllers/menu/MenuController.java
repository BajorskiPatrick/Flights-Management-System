package lot.controllers.menu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lot.controllers.operations.FlightOperationsController;
import lot.controllers.operations.PassengerOperationsController;
import lot.controllers.operations.ReservationOperationsController;
import lot.dao.FlightDao;
import lot.dao.PassengerDao;
import lot.dao.ReservationDao;
import lot.services.EmailService;
import lot.services.FlightService;
import lot.services.PassengerService;
import lot.services.ReservationService;
import lot.controllers.utils.ControllerUtils;

import java.io.IOException;

/**
 * Menu controller that manages navigation between different operation views
 * (view all, search, add, update, delete) for flights, passengers, and reservations.
 * Also initializes and injects the appropriate services into operation controllers.
 */
public class MenuController {
    @FXML
    private Label titleLabel;
    @FXML
    private Button allButton;
    @FXML
    private Button searchButton;
    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;

    private Stage stage;
    private String resourceType;
    private final FlightService flightService = new FlightService(new FlightDao());
    private final ReservationService reservationService = new ReservationService(new ReservationDao(), new FlightDao(), new PassengerDao(), new EmailService());
    private final PassengerService passengerService = new PassengerService(new PassengerDao());

    /**
     * Constructs a new instance of the class with default values.
     * Initializes all fields to their default initial values.
     */
    public MenuController() {}

    /**
     * Sets the resource type (flight, passenger, or reservation) and updates UI labels accordingly.
     *
     * @param resourceType the type of resource to manage ("flight", "passenger", or "reservation")
     */
    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
        titleLabel.setText("You are now in the " + resourceType.substring(0, 1).toUpperCase() + resourceType.substring(1) + " Manager!");
        allButton.setText("See all "+ resourceType + "s");
        searchButton.setText("Search for "+ resourceType);
        addButton.setText("Add new "+ resourceType);
        updateButton.setText("Update "+ resourceType);
        deleteButton.setText("Delete "+ resourceType);
    }

    /**
     * Navigates to the view all operation view and loads all records.
     *
     * @param event the action event that triggered this navigation
     */
    @FXML
    private void seeAllOperation(ActionEvent event) {
        FXMLLoader loader = this.getLoader("SeeAllView");
        try {
            setStage(event, loader, "SearchView.css");
        }
        catch (IOException e) {
            this.showErrorAlert(e.getMessage());
            return;
        }

        switch(resourceType) {
            case "flight":
                FlightOperationsController flightController = loader.getController();
                flightController.seeAllFlights();
                break;
            case "passenger":
                PassengerOperationsController passengerController = loader.getController();
                passengerController.seeAllPassengers();
                break;
            case "reservation":
                ReservationOperationsController reservationController = loader.getController();
                reservationController.seeAllReservations();
                break;
        }
        stage.show();
    }

    /**
     * Navigates to the search operation view.
     *
     * @param event the action event that triggered this navigation
     */
    @FXML
    private void searchOperation(ActionEvent event) {
        FXMLLoader loader = this.getLoader("SearchView");
        try {
            setStage(event, loader, "SearchView.css");
        }
        catch (IOException e) {
            this.showErrorAlert(e.getMessage());
            return;
        }

        stage.show();
    }

    /**
     * Navigates to the add new operation view.
     * For reservations, also configures available flights and passengers.
     *
     * @param event the action event that triggered this navigation
     */
    @FXML
    private void addNewOperation(ActionEvent event) {
        FXMLLoader loader = this.getLoader("AddView");
        try {
            setStage(event, loader, "AddUpdateView.css");
        }
        catch (IOException e) {
            this.showErrorAlert(e.getMessage());
            return;
        }

        if (resourceType.equals("reservation")) {
            ReservationOperationsController reservationController = loader.getController();
            reservationController.configureFlightsAndPassengersIds("add");
        }
        stage.show();
    }

    /**
     * Navigates to the update operation view and configures available IDs.
     *
     * @param event the action event that triggered this navigation
     */
    @FXML
    private void updateOperation(ActionEvent event) {
        FXMLLoader loader = this.getLoader("UpdateView");
        try {
            setStage(event, loader, "AddUpdateView.css");
        }
        catch (IOException e) {
            this.showErrorAlert(e.getMessage());
            return;
        }

        switch(resourceType) {
            case "flight":
                FlightOperationsController flightController = loader.getController();
                flightController.configureFlightsIds("update");
                break;
            case "passenger":
                PassengerOperationsController passengerController = loader.getController();
                passengerController.configurePassengersIds("update");
                break;
            case "reservation":
                ReservationOperationsController reservationController = loader.getController();
                reservationController.configureReservationIds("update");
                break;
        }
        stage.show();
    }

    /**
     * Navigates to the delete operation view and configures available IDs.
     *
     * @param event the action event that triggered this navigation
     */
    @FXML
    private void deleteOperation(ActionEvent event) {
        FXMLLoader loader = this.getLoader("DeleteView");
        try {
            setStage(event, loader, "DeleteView.css");
        }
        catch (IOException e) {
            this.showErrorAlert(e.getMessage());
            return;
        }

        switch(resourceType) {
            case "flight":
                FlightOperationsController flightController = loader.getController();
                flightController.configureFlightsIds("delete");
                break;
            case "passenger":
                PassengerOperationsController passengerController = loader.getController();
                passengerController.configurePassengersIds("delete");
                break;
            case "reservation":
                ReservationOperationsController reservationController = loader.getController();
                reservationController.configureReservationIds("delete");
                break;
        }
        stage.show();
    }

    /**
     * Returns to the main application view.
     *
     * @param event the action event that triggered this navigation
     */
    @FXML
    private void goBack(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/lot/views/MainApp.fxml"));
        try {
            setStage(event, loader, "MainApp.css");
        }
        catch (IOException e) {
            this.showErrorAlert(e.getMessage());
            return;
        }
        stage.show();
    }

    /**
     * Creates and configures an FXMLLoader for the specified view.
     * Sets up the appropriate controller factory based on resource type.
     *
     * @param view the name of the view to load (without extension)
     * @return configured FXMLLoader instance
     */
    private FXMLLoader getLoader(String view) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/lot/views/operations/" + resourceType + "/" + resourceType.substring(0, 1).toUpperCase() + resourceType.substring(1) + view + ".fxml"));
        loader.setControllerFactory(type -> {
            if (type == FlightOperationsController.class) {
                return new FlightOperationsController(flightService, new ControllerUtils());
            }
            else if (type == ReservationOperationsController.class) {
                return new ReservationOperationsController(reservationService, new ControllerUtils());
            }
            else if (type == PassengerOperationsController.class) {
                return new PassengerOperationsController(passengerService, new ControllerUtils());
            }

            try {
                return type.getDeclaredConstructor().newInstance();
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return loader;
    }

    /**
     * Configures and displays the stage with the loaded view and specified CSS.
     *
     * @param event the action event that triggered this navigation
     * @param cssName the name of the CSS file to apply
     */
    private void setStage(ActionEvent event, FXMLLoader loader, String cssName) throws IOException {
        Parent root = loader.load();
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/lot/css/" + cssName).toExternalForm());
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
