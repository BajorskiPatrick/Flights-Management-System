package lot.controllers.menu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

import java.io.IOException;

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
    private Scene scene;
    private Parent root;
    private String resourceType;

    private final FlightService flightService = new FlightService(new FlightDao());
    private final ReservationService reservationService = new ReservationService(new ReservationDao(), new FlightDao(), new PassengerDao(), new EmailService());
    private final PassengerService passengerService = new PassengerService(new PassengerDao());

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
        titleLabel.setText("You are now in the " + resourceType.substring(0, 1).toUpperCase() + resourceType.substring(1) + " Manager!");
        allButton.setText("See all "+ resourceType + "s");
        searchButton.setText("Search for "+ resourceType);
        addButton.setText("Add new "+ resourceType);
        updateButton.setText("Update "+ resourceType);
        deleteButton.setText("Delete "+ resourceType);
    }

    @FXML
    private void seeAllOperation(ActionEvent event) throws IOException {
        FXMLLoader loader = this.getLoader("SeeAllView");
        root = loader.load();

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

        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/lot/css/SearchView.css").toExternalForm());
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    private void searchOperation(ActionEvent event) throws IOException {
        FXMLLoader loader = this.getLoader("SearchView");
        root = loader.load();
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/lot/css/SearchView.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void addNewOperation(ActionEvent event) throws IOException {
        FXMLLoader loader = this.getLoader("AddView");
        root = loader.load();

        if (resourceType.equals("reservation")) {
            ReservationOperationsController reservationController = loader.getController();
            reservationController.configureFlightsAndPassengersIds("add");
        }

        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/lot/css/AddUpdateView.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void updateOperation(ActionEvent event) throws IOException {
        FXMLLoader loader = this.getLoader("UpdateView");
        root = loader.load();

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

        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/lot/css/AddUpdateView.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void deleteOperation(ActionEvent event) throws IOException {
        FXMLLoader loader = this.getLoader("DeleteView");
        root = loader.load();

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

        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/lot/css/DeleteView.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void goBack(ActionEvent event) throws IOException {
        loadView("/lot/views/MainApp.fxml", event);
    }

    private void loadView(String viewPath, ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource(viewPath));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/lot/css/MainApp.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    private FXMLLoader getLoader(String view) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/lot/views/operations/" + resourceType + "/" + resourceType.substring(0, 1).toUpperCase() + resourceType.substring(1) + view + ".fxml"));
        loader.setControllerFactory(type -> {
            if (type == FlightOperationsController.class) {
                return new FlightOperationsController(flightService); // Wstrzyknięcie
            }
            else if (type == ReservationOperationsController.class) {
                return new ReservationOperationsController(reservationService);
            }
            else if (type == PassengerOperationsController.class) {
                return new PassengerOperationsController(passengerService);
            }
            try {
                return type.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return loader;
    }
}
