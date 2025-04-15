package lot.controllers.operations;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lot.controllers.menu.MenuController;
import lot.exceptions.services.EmailException;
import lot.exceptions.services.NotFoundException;
import lot.exceptions.services.ServiceException;
import lot.models.Reservation;
import lot.services.ReservationService;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationOperationsController {
    @FXML
    private TableView<Reservation> reservationTable;

    @FXML
    private TableColumn<Reservation, Integer> idColumn;

    @FXML
    private TableColumn<Reservation, Integer> flightIdColumn;

    @FXML
    private TableColumn<Reservation, Integer> passengerIdColumn;

    @FXML
    private TableColumn<Reservation, String> seatNumberColumn;

    @FXML
    private TableColumn<Reservation, Boolean> tookPlaceColumn;

    @FXML
    private ComboBox<Integer> deleteId;

    @FXML
    private Label choiceLabel;

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
    private AnchorPane deletionPane;

    @FXML
    private Label updateLabel;

    private ObservableList<Integer> reservationIds = FXCollections.observableArrayList();
    private ObservableList<Integer> flightIds = FXCollections.observableArrayList();
    private ObservableList<Integer> passengerIds = FXCollections.observableArrayList();
    private ObservableList<String> availableSeatsNumbers = FXCollections.observableArrayList();

    private Stage stage;
    private Scene scene;
    private Parent root;

    private final ReservationService reservationService;

    public ReservationOperationsController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }


    public void configureReservationIds(String type) {
        reservationIds.addAll(reservationService.getIds().stream().sorted().collect(Collectors.toList()));
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

    public void configureFlightsAndPassengersIds(String type) {
        flightIds.addAll(reservationService.getFlightIds().stream().sorted().collect(Collectors.toList()));
        passengerIds.addAll(reservationService.getPassengerIds().stream().sorted().collect(Collectors.toList()));

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


    public void configureTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        flightIdColumn.setCellValueFactory(new PropertyValueFactory<>("flightId"));
        passengerIdColumn.setCellValueFactory(new PropertyValueFactory<>("passengerId"));
        seatNumberColumn.setCellValueFactory(new PropertyValueFactory<>("seatNumber"));
        tookPlaceColumn.setCellValueFactory(new PropertyValueFactory<>("tookPlace"));
    }


    public void loadReservations() {
        try {
            List<Reservation> reservations = reservationService.getAllReservations();
            ObservableList<Reservation> flightObservableList = FXCollections.observableArrayList(reservations);
            reservationTable.setItems(flightObservableList);
        } catch (ServiceException e) {
            showApplicationErrorMessage(e.getMessage());
        }
    }


    public void goBack(ActionEvent event) throws IOException {
        loadView("/lot/views/menu/MenuView.fxml", event);
    }

    private void loadView(String viewPath, ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(viewPath));
        root = loader.load();
        MenuController controller = loader.getController();
        controller.setResourceType("reservation");
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/lot/css/Menu.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void changeDeletionLabel(ActionEvent event) {
        Integer choice = deleteId.getValue();
        choiceLabel.setText("You choose id: " + choice);
    }

    @FXML
    private void deleteSelectedId(ActionEvent event) {
        Integer choice = deleteId.getValue();
        if (choice == null) {
            choiceLabel.setText("You choose no id!");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Deletion");
        alert.setHeaderText("You are about to delete reservation with id: " + choice);
        alert.setContentText("Are you sure you want to continue?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            try {
                reservationService.deleteReservation(choice);
            }
            catch (NotFoundException e) {
                showDataErrorMessage(e.getMessage());
            }
            catch (ServiceException e) {
                showApplicationErrorMessage(e.getMessage());
            }
            reservationIds.remove(choice);
            deleteId.setItems(reservationIds);
        }
        clearForm(deletionPane, "Select id for deletion");
    }

    @FXML
    private void addNewReservation(ActionEvent event) {
        Integer flightIdChoice = flightIdBox.getValue();
        Integer passengerIdChoice = passengerIdBox.getValue();
        String seatNumber = seatNumberBox.getValue();

        if (flightIdChoice == null || passengerIdChoice == null || seatNumber.isEmpty()) {
            addLabel.setText("You need to provide all information!");
            addLabel.setTextFill(Color.RED);
            return;
        }

        int id;
        try {
            id = reservationService.makeNewReservation(flightIdChoice, passengerIdChoice, seatNumber);
        }
//        catch (ValidationException e) {
//            showDataErrorMessage(e.getMessage());
//            clearForm(addPane);
//            return;
//        }
        catch (ServiceException e) {
            showApplicationErrorMessage(e.getMessage());
            clearForm(addPane, "Type new reservation data");
            return;
        }

        try {
            reservationService.sendEmail(id);
        }
        catch (EmailException e) {
            showApplicationErrorMessage(e.getMessage());
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Adding new reservation");
        alert.setHeaderText("Reservation with id: " + id  + " has been added successfully\n" +
                "Confirmation email has been automatically sent");
        alert.showAndWait();

        clearForm(addPane, "Type new reservation data");
        availableSeatsNumbers.clear();
        seatNumberBox.setItems(availableSeatsNumbers);
    }

    @FXML
    private void updateReservation(ActionEvent event) {
        Integer reservationIdChoice = idToUpdateSelectorBox.getValue();
        Integer flightIdChoice = updateFlightIdBox.getValue();
        Integer passengerIdChoice = updatePassengerIdBox.getValue();
        String seatNumber = updateSeatNumberBox.getValue();

        if (reservationIdChoice == null || flightIdChoice == null || passengerIdChoice == null || seatNumber.isEmpty()) {
            updateLabel.setText("You need to provide all information!");
            updateLabel.setTextFill(Color.RED);
            return;
        }

        try {
            reservationService.updateExistingReservation(reservationIdChoice, flightIdChoice, passengerIdChoice, seatNumber);
        }
        catch (ServiceException e) {
            showApplicationErrorMessage(e.getMessage());
            clearForm(updatePane, "Provide updated data (first, on the left, select which to update)");
            return;
        }

        try {
            reservationService.sendEmail(reservationIdChoice);
        }
        catch (EmailException e) {
            showApplicationErrorMessage(e.getMessage());
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Updating reservation");
        alert.setHeaderText("Reservation with id: " + reservationIdChoice  + " has been updated successfully\n" +
                "Confirmation email has been automatically sent");
        alert.showAndWait();

        clearForm(updatePane, "Provide updated data (first, on the left, select which to update)");
        availableSeatsNumbers.clear();
        passengerIds.clear();
        flightIds.clear();
        updateSeatNumberBox.setItems(availableSeatsNumbers);
        updateFlightIdBox.setItems(flightIds);
        updatePassengerIdBox.setItems(passengerIds);
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
            showApplicationErrorMessage(e.getMessage());
            clearForm(updatePane, "Provide updated data (first, on the left, select which to update)");
            return;
        }
        this.configureFlightsAndPassengersIds("update");

        updateFlightIdBox.setValue(reservation.getFlightId());
        updatePassengerIdBox.setValue(reservation.getPassengerId());
        updateSeatNumberBox.setValue(reservation.getSeatNumber());
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
        alert.setContentText("Details: " + message);
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
                ((ComboBox<?>) node).getSelectionModel().clearSelection();
            } else if (node instanceof Label) {
                ((Label) node).setText(label);
                ((Label) node).setTextFill(Color.BLACK);
            }
        }
    }
}
