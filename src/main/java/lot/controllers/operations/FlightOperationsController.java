package lot.controllers.operations;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lot.controllers.menu.MenuController;
import lot.exceptions.services.ServiceException;
import lot.exceptions.services.ValidationException;
import lot.models.Flight;
import lot.services.FlightService;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for handling flight-related operations in the UI.
 * Manages the display, search, addition, update, and deletion of flights.
 */
public class FlightOperationsController {
    @FXML
    private AnchorPane searchPane;
    @FXML
    private DatePicker dateSearchField;
    @FXML
    private TextField destinationSearchField;
    @FXML
    private ComboBox<Integer> idSearchField;
    @FXML
    private TextField departureSearchField;

    @FXML
    private AnchorPane updatePane;
    @FXML
    private TextField updateDepartureField;
    @FXML
    private TextField updateDestinationField;
    @FXML
    private DatePicker updateDepartureDateField;
    @FXML
    private TextField updateTimeField;
    @FXML
    private TextField updateDurationField;
    @FXML
    private TextField updateSeatRowsAmountField;
    @FXML
    private CheckBox updateTwoWayField;
    @FXML
    private Label updateLabel;
    @FXML
    private ComboBox<Integer> idToUpdateSelectorBox;

    @FXML
    private TableView<Flight> flightTable;
    @FXML
    private TableColumn<Flight, Integer> idColumn;
    @FXML
    private TableColumn<Flight, String> departureColumn;
    @FXML
    private TableColumn<Flight, String> destinationColumn;
    @FXML
    private TableColumn<Flight, LocalDateTime> departureDateColumn;
    @FXML
    private TableColumn<Flight, Integer> durationColumn;
    @FXML
    private TableColumn<Flight, Integer> seatRowsAmountColumn;
    @FXML
    private TableColumn<Flight, Boolean> twoWayColumn;

    @FXML
    private ComboBox<Integer> deleteId;
    @FXML
    private Label choiceLabel;
    @FXML
    private AnchorPane deletionPane;

    @FXML
    private TextField departureField;
    @FXML
    private TextField destinationField;
    @FXML
    private DatePicker departureDateField;
    @FXML
    private TextField timeField;
    @FXML
    private TextField durationField;
    @FXML
    private TextField seatRowsAmountField;
    @FXML
    private CheckBox twoWayField;
    @FXML
    private Label addLabel;
    @FXML
    private AnchorPane addPane;

    private ObservableList<Integer> ids = FXCollections.observableArrayList();
    private Stage stage;
    private Scene scene;
    private Parent root;
    private final FlightService flightService;

    /**
     * Constructs a FlightOperationsController with the specified FlightService.
     *
     * @param flightService the service to handle flight operations
     */
    public FlightOperationsController(FlightService flightService) {
        this.flightService = flightService;
    }

    /**
     * Initializes the controller and sets up table column bindings.
     */
    @FXML
    public void initialize() {
        if (flightTable != null) {
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            departureColumn.setCellValueFactory(new PropertyValueFactory<>("departure"));
            destinationColumn.setCellValueFactory(new PropertyValueFactory<>("destination"));
            departureDateColumn.setCellValueFactory(new PropertyValueFactory<>("departureDate"));
            durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
            seatRowsAmountColumn.setCellValueFactory(new PropertyValueFactory<>("seatRowsAmount"));
            twoWayColumn.setCellValueFactory(new PropertyValueFactory<>("twoWay"));
        }
    }

    /**
     * Configures the list of flight IDs for different operations.
     *
     * @param type the type of operation ("delete" or "update")
     */
    public void configureFlightsIds(String type) {
        ids.clear();
        ids.addAll(flightService.getIds().stream().sorted().collect(Collectors.toList()));

        if (type.equals("delete")) {
            deleteId.getItems().addAll(ids);
            deleteId.setOnAction(this::changeDeletionLabel);
            deleteId.setVisibleRowCount(5);
        }
        else if (type.equals("update")) {
            idToUpdateSelectorBox.setItems(ids);
            idToUpdateSelectorBox.setOnAction(this::loadSelectedFlightDetails);
            idToUpdateSelectorBox.setVisibleRowCount(5);
        }
    }

    /**
     * Loads and displays all flights in the table.
     */
    public void seeAllFlights() {
        try {
            List<Flight> flights = flightService.getAllFlights();
            ObservableList<Flight> flightObservableList = FXCollections.observableArrayList(flights);
            flightTable.setItems(flightObservableList);
        } catch (ServiceException e) {
            showApplicationErrorMessage(e.getMessage());
        }
    }

    /**
     * Changes the visibility of search fields based on the selected search criteria.
     *
     * @param event the action event that triggered this method
     */
    @FXML
    private void changeVisibility(ActionEvent event) {
        Button button = (Button) event.getSource();
        String id = button.getId();
        switch (id) {
            case "buttonIdSearch":
                destinationSearchField.clear();
                departureSearchField.clear();

                dateSearchField.setVisible(false);
                destinationSearchField.setVisible(false);
                departureSearchField.setVisible(false);
                idSearchField.setVisible(true);
                ids.clear();
                ids.addAll(flightService.getIds().stream().sorted().collect(Collectors.toList()));
                idSearchField.setItems(ids);
                idSearchField.setVisibleRowCount(5);
                idSearchField.setPromptText("Select ID");
                break;
            case "buttonDepartureSearch":
                dateSearchField.setVisible(false);
                destinationSearchField.setVisible(false);
                departureSearchField.setVisible(true);
                idSearchField.setVisible(false);
                break;
            case "buttonDestinationSearch":
                dateSearchField.setVisible(false);
                destinationSearchField.setVisible(true);
                departureSearchField.setVisible(false);
                idSearchField.setVisible(false);
                break;
            case "buttonDateSearch":
                destinationSearchField.clear();
                departureSearchField.clear();

                dateSearchField.setVisible(true);
                destinationSearchField.setVisible(false);
                departureSearchField.setVisible(false);
                idSearchField.setVisible(false);
                break;
        }
    }

    /**
     * Performs a search based on the selected criteria and updates the table.
     *
     * @param event the action event that triggered this method
     */
    @FXML
    private void performSearchByCriteria(ActionEvent event) {
        try {
            if (dateSearchField.isVisible()) {
                LocalDate date = dateSearchField.getValue();
                if (date == null) {
                    return;
                }
                List<Flight> flights = flightService.getFlightByDate(date);
                flightTable.setItems(FXCollections.observableArrayList(flights));
            }
            else if (destinationSearchField.isVisible()) {
                String destination = destinationSearchField.getText();
                if (destination.isEmpty()) {
                    return;
                }
                List<Flight> flights = flightService.getFlightByDestination(destination);
                flightTable.setItems(FXCollections.observableArrayList(flights));
            }
            else if (departureSearchField.isVisible()) {
                String departure = departureSearchField.getText();
                if (departure.isEmpty()) {
                    return;
                }
                List<Flight> flights = flightService.getFlightByDeparture(departure);
                flightTable.setItems(FXCollections.observableArrayList(flights));
            }
            else if (idSearchField.isVisible()) {
                Integer id = idSearchField.getValue();
                if (id == null) {
                    return;
                }
                Flight flight = flightService.getFlightById(id);
                flightTable.setItems(FXCollections.observableArrayList(flight));
            }
        }
        catch (ServiceException e) {
            showApplicationErrorMessage(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Deletes the selected flight after confirmation.
     *
     * @param event the action event that triggered this method
     */
    @FXML
    private void deleteSelectedId(ActionEvent event) {
        Integer choice = deleteId.getValue();
        if (choice == null) {
            choiceLabel.setText("You choose no id!");
            choiceLabel.setTextFill(Color.RED);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Deletion");
        alert.setHeaderText("You are about to delete reservation with id: " + choice);
        alert.setContentText("Are you sure you want to continue?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            try {
                flightService.deleteFlight(choice);
            }
            catch (ServiceException e) {
                showApplicationErrorMessage(e.getMessage());
            }
            ids.remove(choice);
            deleteId.setItems(ids);
        }
        clearForm(deletionPane, "Select id for deletion");
    }

    /**
     * Adds a new flight with the provided details.
     *
     * @param event the action event that triggered this method
     */
    @FXML
    private void addNewFlight(ActionEvent event) {
        String departure = departureField.getText();
        String destination = destinationField.getText();
        LocalDate departureDate = departureDateField.getValue();
        String time = timeField.getText();
        String duration = durationField.getText();
        String seatRowsAmount = seatRowsAmountField.getText();
        Boolean twoWay = twoWayField.isSelected();

        if (departure.isEmpty() || destination.isEmpty() || departureDate == null || time.isEmpty() || duration.isEmpty() || seatRowsAmount.isEmpty()) {
            addLabel.setText("You need to provide all information!");
            addLabel.setTextFill(Color.RED);
            return;
        }

        int durationNum;
        int seatNum;
        try {
            durationNum = Integer.parseInt(duration);
        }
        catch (NumberFormatException e) {
            addLabel.setText("Duration must be a number!");
            updateLabel.setTextFill(Color.RED);
            durationField.clear();
            return;
        }
        try {
            seatNum = Integer.parseInt(seatRowsAmount);
        }
        catch (NumberFormatException e) {
            addLabel.setText("Seat rows must be a number!");
            updateLabel.setTextFill(Color.RED);
            seatRowsAmountField.clear();
            return;
        }

        int id;
        try {
            id = flightService.addNewFlight(departure, destination, departureDate, time, durationNum, seatNum, twoWay);
        }
        catch (ValidationException e) {
            showDataErrorMessage(e.getMessage());
            addLabel.setText("Type new flight data");
            addLabel.setTextFill(Color.BLACK);
            return;
        }
        catch (ServiceException e) {
            showApplicationErrorMessage(e.getMessage());
            clearForm(addPane, "Type new flight data");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Adding new flight");
        alert.setHeaderText("Flight with id: " + id  + " has been added successfully");
        alert.showAndWait();

        clearForm(addPane, "Type new flight data");
    }

    /**
     * Updates an existing flight with the provided details.
     *
     * @param event the action event that triggered this method
     */
    @FXML
    private void updateFlight(ActionEvent event) {
        Integer id = idToUpdateSelectorBox.getValue();
        String departure = updateDepartureField.getText();
        String destination = updateDestinationField.getText();
        LocalDate departureDate = updateDepartureDateField.getValue();
        String time = updateTimeField.getText();
        String duration = updateDurationField.getText();
        String seatRowsAmount = updateSeatRowsAmountField.getText();
        Boolean twoWay = updateTwoWayField.isSelected();

        if (departure.isEmpty() || destination.isEmpty() || departureDate == null || time.isEmpty() || duration.isEmpty() || seatRowsAmount.isEmpty()) {
            updateLabel.setText("You need to provide all information!");
            updateLabel.setTextFill(Color.RED);
            return;
        }

        int durationNum;
        int seatNum;
        try {
            durationNum = Integer.parseInt(duration);
        }
        catch (NumberFormatException e) {
            updateLabel.setText("Duration must be a number!");
            updateLabel.setTextFill(Color.RED);
            updateDurationField.clear();
            return;
        }
        try {
            seatNum = Integer.parseInt(seatRowsAmount);
        }
        catch (NumberFormatException e) {
            updateLabel.setText("Seat rows must be a number!");
            updateLabel.setTextFill(Color.RED);
            updateSeatRowsAmountField.clear();
            return;
        }

        try {
            flightService.updateExistingFlight(id, departure, destination, departureDate, time, durationNum, seatNum, twoWay);
        }
        catch (ValidationException e) {
            showDataErrorMessage(e.getMessage());
            updateLabel.setText("Provide updated data (first, on the left, select which to update)");
            updateLabel.setTextFill(Color.BLACK);
            return;
        }
        catch (ServiceException e) {
            showApplicationErrorMessage(e.getMessage());
            clearForm(updatePane, "Provide updated data (first, on the left, select which to update)");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Updating flight");
        alert.setHeaderText("Flight with id: " + id  + " has been updated successfully");
        alert.showAndWait();

        clearForm(updatePane, "Provide updated data (first, on the left, select which to update)");
    }

    /**
     * Returns to the main menu view.
     *
     * @param event the action event that triggered this method
     */
    @FXML
    private void goBack(ActionEvent event) {
        try {
            loadView("/lot/views/menu/MenuView.fxml", event);
        }
        catch (IOException e) {
            showApplicationErrorMessage("Failed to load MenuView. " + e.getMessage());
        }
    }

    /**
     * Updates the deletion confirmation label with the selected ID.
     *
     * @param event the action event that triggered this method
     */
    @FXML
    private void changeDeletionLabel(ActionEvent event) {
        Integer choice = deleteId.getValue();
        choiceLabel.setText("You choose id: " + choice);
        choiceLabel.setTextFill(Color.BLACK);
    }

    private void loadView(String viewPath, ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(viewPath));
        root = loader.load();
        MenuController controller = loader.getController();
        controller.setResourceType("flight");
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/lot/css/Menu.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    private void loadSelectedFlightDetails(ActionEvent event) {
        Integer id = idToUpdateSelectorBox.getValue();
        if (id == null) {
            return;
        }
        Flight flight;
        try {
            flight = flightService.getFlightById(id);
        }
        catch (ServiceException e) {
            showApplicationErrorMessage(e.getMessage());
            clearForm(updatePane, "Provide updated data (first, on the left, select which to update)");
            return;
        }

        updateDepartureField.setText(flight.getDeparture());
        updateDestinationField.setText(flight.getDestination());
        updateDepartureDateField.setValue(flight.getDepartureDate().toLocalDate());
        updateTimeField.setText(flight.getDepartureDate().toLocalTime().toString());
        updateDurationField.setText(Integer.toString(flight.getDuration()));
        updateSeatRowsAmountField.setText(Integer.toString(flight.getSeatRowsAmount()));
        updateTwoWayField.setSelected(flight.getTwoWay());
    }

    private void showApplicationErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Application exception");
        alert.setHeaderText("Unexpected exception has occurred");
        alert.setContentText("Details: " + message);
        alert.showAndWait();
    }

    private void showDataErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Data exception");
        alert.setHeaderText("Exception related to provided data has occurred");
        alert.setContentText("Details:\n" + message);
        alert.showAndWait();
    }

    private void clearForm(AnchorPane anchorPane, String label) {
        for (javafx.scene.Node node : anchorPane.getChildren()) {
            if (node instanceof TextField) {
                ((TextField) node).clear();
            } else if (node instanceof DatePicker) {
                ((DatePicker) node).setValue(null);
            } else if (node instanceof CheckBox) {
                ((CheckBox) node).setSelected(false);
            } else if (node instanceof ComboBox) {
                ((ComboBox<?>) node).setValue(null);
            } else if (node instanceof Label) {
                ((Label) node).setText(label);
                ((Label) node).setTextFill(Color.BLACK);
            }
        }
    }
}
