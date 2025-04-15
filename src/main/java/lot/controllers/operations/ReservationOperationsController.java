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
import lot.exceptions.dao.DatabaseActionException;
import lot.exceptions.services.EmailException;
import lot.exceptions.services.NotFoundException;
import lot.exceptions.services.ServiceException;
import lot.models.Reservation;
import lot.services.ReservationService;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.time.LocalDateTime;
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
    public TableColumn<Reservation, String> passengerNameColumn;
    @FXML
    public TableColumn<Reservation, String> passengerSurnameColumn;
    @FXML
    private TableColumn<Reservation, String> seatNumberColumn;
    @FXML
    public TableColumn<Reservation, LocalDateTime> departureDateColumn;
    @FXML
    private TableColumn<Reservation, Boolean> tookPlaceColumn;
    @FXML
    public TextField surnameSearchField;
    @FXML
    public ComboBox<Integer> idSearchField;
    @FXML
    public ComboBox<Integer> flightIdSearchField;
    @FXML
    public ComboBox<Integer> passengerIdSearchField;

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


    public void seeAllReservations() {
        try {
            List<Reservation> reservations = reservationService.getAllReservations();
            ObservableList<Reservation> flightObservableList = FXCollections.observableArrayList(reservations);
            reservationTable.setItems(flightObservableList);
        } catch (ServiceException e) {
            showApplicationErrorMessage(e.getMessage());
        }
    }

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
                reservationIds.addAll(reservationService.getIds().stream().sorted().collect(Collectors.toList()));
                idSearchField.setItems(reservationIds);
                idSearchField.setVisibleRowCount(5);
                idSearchField.setPromptText("Select ID");
                break;
            case "buttonFlightIdSearch":
                surnameSearchField.clear();

                surnameSearchField.setVisible(false);
                flightIdSearchField.setVisible(true);
                passengerIdSearchField.setVisible(false);
                idSearchField.setVisible(false);
                flightIds.clear();
                flightIds.addAll(reservationService.getFlightIds().stream().sorted().collect(Collectors.toList()));
                flightIdSearchField.setItems(flightIds);
                flightIdSearchField.setVisibleRowCount(5);
                flightIdSearchField.setPromptText("Select flight ID");
                break;
            case "buttonPassengerIdSearch":
                surnameSearchField.clear();

                surnameSearchField.setVisible(false);
                flightIdSearchField.setVisible(false);
                passengerIdSearchField.setVisible(true);
                idSearchField.setVisible(false);
                passengerIds.clear();
                passengerIds.addAll(reservationService.getPassengerIds().stream().sorted().collect(Collectors.toList()));
                passengerIdSearchField.setItems(passengerIds);
                passengerIdSearchField.setVisibleRowCount(5);
                passengerIdSearchField.setPromptText("Select passenger ID");
                break;
            case "buttonSurnameSearch":
                flightIdSearchField.getSelectionModel().clearSelection();
                passengerIdSearchField.getSelectionModel().clearSelection();
                idSearchField.getSelectionModel().clearSelection();

                surnameSearchField.setVisible(true);
                flightIdSearchField.setVisible(false);
                passengerIdSearchField.setVisible(false);
                idSearchField.setVisible(false);
                break;
        }
    }

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
            showApplicationErrorMessage(e.getMessage());
        }
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
            showApplicationErrorMessage(e.getMessage());
            clearForm(addPane, "Type new reservation data");
            return;
        }

        try {
            reservationService.sendEmail(id);
        }
        catch (EmailException e) {
            showApplicationErrorMessage(e.getMessage());
            e.printStackTrace();
            hasBeenSent = false;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Adding new reservation");
        alert.setHeaderText("Reservation with id: " + id  + " has been added successfully\n"
                + (hasBeenSent ? "\nConfirmation email has been automatically sent" : ""));
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
            showApplicationErrorMessage(e.getMessage());
            clearForm(updatePane, "Provide updated data (first, on the left, select which to update)");
            return;
        }

        try {
            reservationService.sendEmail(reservationIdChoice);
        }
        catch (EmailException e) {
            showApplicationErrorMessage(e.getMessage());
            hasBeenSent = false;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Updating reservation");
        alert.setHeaderText("Reservation with id: " + reservationIdChoice  + " has been updated successfully"
                + (hasBeenSent ? "\nConfirmation email has been automatically sent" : ""));
        alert.showAndWait();

        clearForm(updatePane, "Provide updated data (first, on the left, select which to update)");
        availableSeatsNumbers.clear();
        passengerIds.clear();
        flightIds.clear();
        updateSeatNumberBox.setItems(availableSeatsNumbers);
        updateFlightIdBox.setItems(flightIds);
        updatePassengerIdBox.setItems(passengerIds);
    }

    @FXML
    private void changeDeletionLabel(ActionEvent event) {
        Integer choice = deleteId.getValue();
        choiceLabel.setText("You choose id: " + choice);
    }

    @FXML
    private void goBack(ActionEvent event) throws IOException {
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
