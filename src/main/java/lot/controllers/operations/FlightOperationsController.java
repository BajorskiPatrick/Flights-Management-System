package lot.controllers.operations;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;

import lot.exceptions.services.ServiceException;
import lot.exceptions.services.ValidationException;
import lot.models.Flight;
import lot.services.FlightService;
import lot.controllers.utils.ControllerUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
    private Label addLabel;
    @FXML
    private AnchorPane addPane;

    private final ObservableList<Integer> ids = FXCollections.observableArrayList();
    private final FlightService flightService;
    private final ControllerUtils utils;

    /**
     * Constructs a FlightOperationsController with the specified FlightService.
     *
     * @param flightService the service to handle flight operations
     * @param utils the utils to handle management operations
     */
    public FlightOperationsController(FlightService flightService, ControllerUtils utils) {
        this.flightService = flightService;
        this.utils = utils;
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
            departureDateColumn.setCellFactory(column -> new TableCell<>() {
                private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

                @Override
                protected void updateItem(LocalDateTime item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(formatter.format(item));
                    }
                }
            });

            durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
            seatRowsAmountColumn.setCellValueFactory(new PropertyValueFactory<>("seatRowsAmount"));
        }
    }

    /**
     * Configures the list of flight IDs for different operations.
     *
     * @param type the type of operation ("delete" or "update")
     */
    public void configureFlightsIds(String type) {
        ids.clear();
        ids.addAll(flightService.getIds().stream().sorted().toList());

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
            utils.showApplicationErrorMessage(e.getMessage());
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
                dateSearchField.setVisible(false);
                destinationSearchField.setVisible(false);
                departureSearchField.setVisible(false);
                idSearchField.setVisible(true);

                destinationSearchField.clear();
                departureSearchField.clear();
                dateSearchField.setValue(null);

                ids.clear();
                ids.addAll(flightService.getIds().stream().sorted().toList());
                idSearchField.setItems(ids);
                idSearchField.setVisibleRowCount(5);
                break;
            case "buttonDepartureSearch":
                dateSearchField.setVisible(false);
                destinationSearchField.setVisible(false);
                departureSearchField.setVisible(true);
                idSearchField.setVisible(false);

                destinationSearchField.clear();
                dateSearchField.setValue(null);
                idSearchField = utils.clearComboBox(searchPane, idSearchField);
                break;
            case "buttonDestinationSearch":
                dateSearchField.setVisible(false);
                destinationSearchField.setVisible(true);
                departureSearchField.setVisible(false);
                idSearchField.setVisible(false);

                departureSearchField.clear();
                dateSearchField.setValue(null);
                idSearchField = utils.clearComboBox(searchPane, idSearchField);
                break;
            case "buttonDateSearch":
                destinationSearchField.clear();
                departureSearchField.clear();

                dateSearchField.setVisible(true);
                destinationSearchField.setVisible(false);
                departureSearchField.setVisible(false);
                idSearchField.setVisible(false);

                destinationSearchField.clear();
                departureSearchField.clear();
                idSearchField = utils.clearComboBox(searchPane, idSearchField);
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
            utils.showApplicationErrorMessage(e.getMessage());
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
            choiceLabel.setText("You chose no number!");
            choiceLabel.setTextFill(Color.RED);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Deletion");
        alert.setHeaderText("You are about to delete reservation with number: " + choice);
        alert.setContentText("Are you sure you want to continue?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            try {
                flightService.deleteFlight(choice);
            }
            catch (ServiceException e) {
                utils.showApplicationErrorMessage(e.getMessage());
            }
            ids.remove(choice);
            deleteId.setItems(ids);
        }
        utils.clearForm(deletionPane, "Select number for deletion");
        deleteId = utils.clearComboBox(deletionPane, deleteId);
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
            utils.showDataValidationErrorMessage("Duration must be a number!");
            addLabel.setText("Type new flight data");
            addLabel.setTextFill(Color.BLACK);
            return;
        }
        try {
            seatNum = Integer.parseInt(seatRowsAmount);
        }
        catch (NumberFormatException e) {
            utils.showDataValidationErrorMessage("Seat rows must be a number!");
            addLabel.setText("Type new flight data");
            addLabel.setTextFill(Color.BLACK);
            return;
        }

        int id;
        try {
            id = flightService.addNewFlight(departure, destination, departureDate, time, durationNum, seatNum);
        }
        catch (ValidationException e) {
            utils.showDataValidationErrorMessage(e.getMessage());
            addLabel.setText("Type new flight data");
            addLabel.setTextFill(Color.BLACK);
            return;
        }
        catch (ServiceException e) {
            utils.showApplicationErrorMessage(e.getMessage());
            utils.clearForm(addPane, "Type new flight data");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Adding new flight");
        alert.setHeaderText("Flight with id: " + id  + " has been added successfully");
        alert.showAndWait();

        utils.clearForm(addPane, "Type new flight data");
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
            utils.showDataValidationErrorMessage("Duration must be a number!");
            updateLabel.setText("Provide updated data (first, on the left, select which to update)");
            updateLabel.setTextFill(Color.BLACK);
            return;
        }
        try {
            seatNum = Integer.parseInt(seatRowsAmount);
        }
        catch (NumberFormatException e) {
            utils.showDataValidationErrorMessage("Seat rows must be a number!");
            updateLabel.setText("Provide updated data (first, on the left, select which to update)");
            updateLabel.setTextFill(Color.BLACK);
            return;
        }

        try {
            flightService.updateExistingFlight(id, departure, destination, departureDate, time, durationNum, seatNum);
        }
        catch (ValidationException e) {
            utils.showDataValidationErrorMessage(e.getMessage());
            updateLabel.setText("Provide updated data (first, on the left, select which to update)");
            updateLabel.setTextFill(Color.BLACK);
            return;
        }
        catch (ServiceException e) {
            utils.showApplicationErrorMessage(e.getMessage());
            utils.clearForm(updatePane, "Provide updated data (first, on the left, select which to update)");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Updating flight");
        alert.setHeaderText("Flight with id: " + id  + " has been updated successfully");
        alert.showAndWait();

        utils.clearForm(updatePane, "Provide updated data (first, on the left, select which to update)");
        idToUpdateSelectorBox = utils.clearComboBox(updatePane, idToUpdateSelectorBox);
    }

    /**
     * Returns to the main menu view.
     *
     * @param event the action event that triggered this method
     */
    @FXML
    private void goBack(ActionEvent event) {
        try {
            utils.loadView("/lot/views/menu/MenuView.fxml", event, "flight");
        }
        catch (IOException e) {
            utils.showApplicationErrorMessage("Failed to load MenuView. " + e.getMessage());
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
        choiceLabel.setText("You chose number: " + choice);
        choiceLabel.setTextFill(Color.BLACK);
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
            utils.showApplicationErrorMessage(e.getMessage());
            utils.clearForm(updatePane, "Provide updated data (first, on the left, select which to update)");
            return;
        }

        updateDepartureField.setText(flight.getDeparture());
        updateDestinationField.setText(flight.getDestination());
        updateDepartureDateField.setValue(flight.getDepartureDate().toLocalDate());
        updateTimeField.setText(flight.getDepartureDate().toLocalTime().toString());
        updateDurationField.setText(Integer.toString(flight.getDuration()));
        updateSeatRowsAmountField.setText(Integer.toString(flight.getSeatRowsAmount()));
    }
}
