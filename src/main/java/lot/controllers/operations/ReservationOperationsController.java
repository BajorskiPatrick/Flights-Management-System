package lot.controllers.operations;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;

import lot.exceptions.services.EmailException;
import lot.exceptions.services.ServiceException;
import lot.models.Reservation;
import lot.services.ReservationService;
import lot.controllers.utils.ControllerUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller for handling reservation-related operations in the UI.
 * Manages the display, search, addition, update, and deletion of reservations.
 */
public class ReservationOperationsController {
    @FXML
    private AnchorPane searchPane;
    @FXML
    private TableView<Reservation> reservationTable;
    @FXML
    private TableColumn<Reservation, Integer> idColumn;
    @FXML
    private TableColumn<Reservation, Integer> flightIdColumn;
    @FXML
    private TableColumn<Reservation, Integer> passengerIdColumn;
    @FXML
    private TableColumn<Reservation, String> passengerNameColumn;
    @FXML
    private TableColumn<Reservation, String> passengerSurnameColumn;
    @FXML
    private TableColumn<Reservation, String> seatNumberColumn;
    @FXML
    private TableColumn<Reservation, LocalDateTime> departureDateColumn;
    @FXML
    private TableColumn<Reservation, Boolean> tookPlaceColumn;
    @FXML
    private TextField surnameSearchField;
    @FXML
    private ComboBox<Integer> idSearchField;
    @FXML
    private ComboBox<Integer> flightIdSearchField;
    @FXML
    private ComboBox<Integer> passengerIdSearchField;

    @FXML
    private ComboBox<Integer> deleteId;
    @FXML
    private Label choiceLabel;
    @FXML
    private AnchorPane deletionPane;

    @FXML
    private ComboBox<Integer> flightIdBox;
    @FXML
    private ComboBox<Integer> passengerIdBox;
    @FXML
    private ComboBox<String> seatNumberBox;
    @FXML
    private Label addLabel;
    @FXML
    private AnchorPane addPane;

    @FXML
    private ComboBox<Integer> idToUpdateSelectorBox;
    @FXML
    private ComboBox<Integer> updateFlightIdBox;
    @FXML
    private ComboBox<Integer> updatePassengerIdBox;
    @FXML
    private ComboBox<String> updateSeatNumberBox;
    @FXML
    private AnchorPane updatePane;
    @FXML
    private Label updateLabel;

    private final ObservableList<Integer> reservationIds = FXCollections.observableArrayList();
    private final ObservableList<Integer> flightIds = FXCollections.observableArrayList();
    private final ObservableList<Integer> passengerIds = FXCollections.observableArrayList();
    private final ObservableList<String> availableSeatsNumbers = FXCollections.observableArrayList();
    private final ReservationService reservationService;
    private final ControllerUtils utils;


    /**
     * Constructs a ReservationOperationsController with the specified ReservationService.
     *
     * @param reservationService the service to handle reservation operations
     * @param utils the utils to handle management operations
     */
    public ReservationOperationsController(ReservationService reservationService, ControllerUtils utils) {
        this.reservationService = reservationService;
        this.utils = utils;
    }

    /**
     * Initializes the controller and sets up table column bindings.
     */
    @FXML
    public void initialize() {
        if (reservationTable != null) {
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            flightIdColumn.setCellValueFactory(new PropertyValueFactory<>("flightId"));
            passengerIdColumn.setCellValueFactory(new PropertyValueFactory<>("passengerId"));
            passengerNameColumn.setCellValueFactory(new PropertyValueFactory<>("passengerName"));
            passengerSurnameColumn.setCellValueFactory(new PropertyValueFactory<>("passengerSurname"));
            seatNumberColumn.setCellValueFactory(new PropertyValueFactory<>("seatNumber"));
            departureDateColumn.setCellValueFactory(new PropertyValueFactory<>("departureDate"));
            tookPlaceColumn.setCellValueFactory(new PropertyValueFactory<>("tookPlace"));
        }
    }

    /**
     * Configures the list of reservation IDs for different operations.
     *
     * @param type the type of operation ("delete" or "update")
     */
    public void configureReservationIds(String type) {
        reservationIds.addAll(reservationService.getIds().stream().sorted().toList());
        if (type.equals("delete")) {
            deleteId.setItems(reservationIds);
            deleteId.setOnAction(this::changeDeletionLabel);
            deleteId.setVisibleRowCount(5);
        }
        else if (type.equals("update")) {
            idToUpdateSelectorBox.setItems(reservationIds);
            idToUpdateSelectorBox.setOnAction(this::loadSelectedReservationDetails);
            idToUpdateSelectorBox.setVisibleRowCount(5);
        }
    }

    /**
     * Configures the lists of flight and passenger IDs for different operations.
     *
     * @param type the type of operation ("add" or "update")
     */
    public void configureFlightsAndPassengersIds(String type) {
        flightIds.addAll(reservationService.getFlightIds().stream().sorted().toList());
        passengerIds.addAll(reservationService.getPassengerIds().stream().sorted().toList());

        if (type.equals("add")) {
            flightIdBox.setItems(flightIds);
            flightIdBox.setOnAction(this::getAvailableSeats);
            flightIdBox.setVisibleRowCount(5);

            passengerIdBox.setItems(passengerIds);
            passengerIdBox.setVisibleRowCount(5);
        }
        else if (type.equals("update")) {
            updateFlightIdBox.setItems(flightIds);
            updateFlightIdBox.setOnAction(this::getAvailableSeats);
            updateFlightIdBox.setVisibleRowCount(5);

            updatePassengerIdBox.setItems(passengerIds);
            updatePassengerIdBox.setVisibleRowCount(5);
        }
    }

    /**
     * Loads and displays all reservations in the table.
     */
    public void seeAllReservations() {
        try {
            List<Reservation> reservations = reservationService.getAllReservations();
            ObservableList<Reservation> flightObservableList = FXCollections.observableArrayList(reservations);
            reservationTable.setItems(flightObservableList);
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
                surnameSearchField.clear();

                surnameSearchField.setVisible(false);
                flightIdSearchField.setVisible(false);
                passengerIdSearchField.setVisible(false);
                idSearchField.setVisible(true);
                reservationIds.clear();
                reservationIds.addAll(reservationService.getIds().stream().sorted().toList());
                idSearchField.setItems(reservationIds);
                idSearchField.setVisibleRowCount(5);
                flightIdSearchField = utils.clearComboBox(searchPane, flightIdSearchField);
                passengerIdSearchField = utils.clearComboBox(searchPane, passengerIdSearchField);
                break;
            case "buttonFlightIdSearch":
                surnameSearchField.clear();

                surnameSearchField.setVisible(false);
                flightIdSearchField.setVisible(true);
                passengerIdSearchField.setVisible(false);
                idSearchField.setVisible(false);
                flightIds.clear();
                flightIds.addAll(reservationService.getFlightIds().stream().sorted().toList());
                flightIdSearchField.setItems(flightIds);
                flightIdSearchField.setVisibleRowCount(5);
                idSearchField = utils.clearComboBox(searchPane, idSearchField);
                passengerIdSearchField = utils.clearComboBox(searchPane, passengerIdSearchField);
                break;
            case "buttonPassengerIdSearch":
                surnameSearchField.clear();

                surnameSearchField.setVisible(false);
                flightIdSearchField.setVisible(false);
                passengerIdSearchField.setVisible(true);
                idSearchField.setVisible(false);
                passengerIds.clear();
                passengerIds.addAll(reservationService.getPassengerIds().stream().sorted().toList());
                passengerIdSearchField.setItems(passengerIds);
                passengerIdSearchField.setVisibleRowCount(5);
                idSearchField = utils.clearComboBox(searchPane, idSearchField);
                flightIdSearchField = utils.clearComboBox(searchPane, flightIdSearchField);
                break;
            case "buttonSurnameSearch":
                flightIdSearchField.getSelectionModel().clearSelection();
                passengerIdSearchField.getSelectionModel().clearSelection();
                idSearchField.getSelectionModel().clearSelection();

                surnameSearchField.setVisible(true);
                flightIdSearchField.setVisible(false);
                passengerIdSearchField.setVisible(false);
                idSearchField.setVisible(false);
                idSearchField = utils.clearComboBox(searchPane, idSearchField);
                flightIdSearchField = utils.clearComboBox(searchPane, flightIdSearchField);
                passengerIdSearchField = utils.clearComboBox(searchPane, passengerIdSearchField);
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
            if (surnameSearchField.isVisible()) {
                String surname = surnameSearchField.getText();
                if (surname.isEmpty()) {
                    return;
                }
                List<Reservation> reservations = reservationService.getReservationBySurname(surname);
                reservationTable.setItems(FXCollections.observableArrayList(reservations));
            }
            else if (flightIdSearchField.isVisible()) {
                Integer flightId = flightIdSearchField.getValue();
                if (flightId == null) {
                    return;
                }
                List<Reservation> reservations = reservationService.getReservationsByFlightId(flightId);
                reservationTable.setItems(FXCollections.observableArrayList(reservations));
            }
            else if (passengerIdSearchField.isVisible()) {
                Integer passengerId = passengerIdSearchField.getValue();
                if (passengerId == null) {
                    return;
                }
                List<Reservation> reservations = reservationService.getReservationsByPassengerId(passengerId);
                reservationTable.setItems(FXCollections.observableArrayList(reservations));
            }
            else if (idSearchField.isVisible()) {
                Integer id = idSearchField.getValue();
                if (id == null) {
                    return;
                }
                Reservation reservation = reservationService.getReservationById(id);
                reservationTable.setItems(FXCollections.observableArrayList(reservation));
            }
        }
        catch (ServiceException e) {
            utils.showApplicationErrorMessage(e.getMessage());
        }
    }

    /**
     * Deletes the selected reservation after confirmation.
     *
     * @param event the action event that triggered this method
     */
    @FXML
    private void deleteSelectedId(ActionEvent event) {
        Integer choice = deleteId.getValue();
        if (choice == null) {
            choiceLabel.setText("You chose no number!");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Deletion");
        alert.setHeaderText("You are about to delete reservation with number: " + choice);
        alert.setContentText("Are you sure you want to continue?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            try {
                reservationService.deleteReservation(choice);
            }
            catch (ServiceException e) {
                utils.showApplicationErrorMessage(e.getMessage());
            }
            reservationIds.remove(choice);
            deleteId.setItems(reservationIds);
        }
        utils.clearForm(deletionPane, "Select number for deletion");
        deleteId = utils.clearComboBox(deletionPane, deleteId);
    }

    /**
     * Adds a new reservation with the provided details and sends confirmation email.
     *
     * @param event the action event that triggered this method
     */
    @FXML
    private void addNewReservation(ActionEvent event) {
        Integer flightIdChoice = flightIdBox.getValue();
        Integer passengerIdChoice = passengerIdBox.getValue();
        String seatNumber = seatNumberBox.getValue();

        boolean hasBeenSent = true;

        if (flightIdChoice == null || passengerIdChoice == null || seatNumber.isEmpty()) {
            addLabel.setText("You need to provide all information!");
            addLabel.setTextFill(Color.RED);
            return;
        }

        int id;
        try {
            id = reservationService.makeNewReservation(flightIdChoice, passengerIdChoice, seatNumber);
        }
        catch (ServiceException e) {
            utils.showApplicationErrorMessage(e.getMessage());
            utils.clearForm(addPane, "Type new reservation data");
            return;
        }

        try {
            reservationService.sendEmail(id);
        }
        catch (EmailException e) {
            utils.showApplicationErrorMessage(e.getMessage());
            hasBeenSent = false;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Adding new reservation");
        alert.setHeaderText("Reservation with id: " + id  + " has been added successfully\n"
                + (hasBeenSent ? "\nConfirmation email has been automatically sent" : ""));
        alert.showAndWait();

        utils.clearForm(addPane, "Type new reservation data");
        flightIdBox = utils.clearComboBox(addPane, flightIdBox);
        passengerIdBox = utils.clearComboBox(addPane, passengerIdBox);

        availableSeatsNumbers.clear();
        seatNumberBox.setItems(availableSeatsNumbers);
        seatNumberBox = utils.clearComboBox(addPane, seatNumberBox);
    }

    /**
     * Updates an existing reservation with the provided details and sends confirmation email.
     *
     * @param event the action event that triggered this method
     */
    @FXML
    private void updateReservation(ActionEvent event) {
        Integer reservationIdChoice = idToUpdateSelectorBox.getValue();
        Integer flightIdChoice = updateFlightIdBox.getValue();
        Integer passengerIdChoice = updatePassengerIdBox.getValue();
        String seatNumber = updateSeatNumberBox.getValue();

        boolean hasBeenSent = true;

        if (reservationIdChoice == null || flightIdChoice == null || passengerIdChoice == null || seatNumber.isEmpty()) {
            updateLabel.setText("You need to provide all information!");
            updateLabel.setTextFill(Color.RED);
            return;
        }

        try {
            reservationService.updateExistingReservation(reservationIdChoice, flightIdChoice, passengerIdChoice, seatNumber);
        }
        catch (ServiceException e) {
            utils.showApplicationErrorMessage(e.getMessage());
            utils.clearForm(updatePane, "Provide updated data (first, on the left, select which to update)");
            return;
        }

        try {
            reservationService.sendEmail(reservationIdChoice);
        }
        catch (EmailException e) {
            utils.showApplicationErrorMessage(e.getMessage());
            hasBeenSent = false;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Updating reservation");
        alert.setHeaderText("Reservation with id: " + reservationIdChoice  + " has been updated successfully"
                + (hasBeenSent ? "\nConfirmation email has been automatically sent" : ""));
        alert.showAndWait();

        utils.clearForm(updatePane, "Provide updated data (first, on the left, select which to update)");
        idToUpdateSelectorBox = utils.clearComboBox(updatePane, idToUpdateSelectorBox);
        updateFlightIdBox = utils.clearComboBox(updatePane, updateFlightIdBox);
        updatePassengerIdBox = utils.clearComboBox(updatePane, updatePassengerIdBox);

        availableSeatsNumbers.clear();
        updateSeatNumberBox.setItems(availableSeatsNumbers);
        updateSeatNumberBox = utils.clearComboBox(updatePane, updateSeatNumberBox);
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
    }

    /**
     * Returns to the main menu view.
     *
     * @param event the action event that triggered this method
     */
    @FXML
    private void goBack(ActionEvent event) {
        try {
            utils.loadView("/lot/views/menu/MenuView.fxml", event, "reservation");
        }
        catch (IOException e) {
            utils.showApplicationErrorMessage("Failed to load MenuView. " + e.getMessage());
        }
    }

    private void getAvailableSeats(ActionEvent event) {
        ComboBox<Integer> choice = (ComboBox<Integer>) event.getSource();

        if (choice.getValue() == null) {
            return;
        }

        availableSeatsNumbers.clear();
        availableSeatsNumbers.addAll(reservationService.getAvailableSeats(choice.getValue()));

        if (seatNumberBox != null) {
            seatNumberBox.setItems(availableSeatsNumbers);
            seatNumberBox.setVisibleRowCount(5);
        }
        else if (updateSeatNumberBox != null) {
            updateSeatNumberBox.setItems(availableSeatsNumbers);
            updateSeatNumberBox.setVisibleRowCount(5);
        }
    }

    private void loadSelectedReservationDetails(ActionEvent event) {
        Integer id = idToUpdateSelectorBox.getValue();
        if (id == null) {
            return;
        }
        Reservation reservation;
        try {
            reservation = reservationService.getReservationById(id);
        }
        catch (ServiceException e) {
            utils.showApplicationErrorMessage(e.getMessage());
            utils.clearForm(updatePane, "Provide updated data (first, on the left, select which to update)");
            return;
        }
        this.configureFlightsAndPassengersIds("update");

        updateFlightIdBox.setValue(reservation.getFlightId());
        updatePassengerIdBox.setValue(reservation.getPassengerId());
        updateSeatNumberBox.setValue(reservation.getSeatNumber());
    }
}
