package lot.controllers.operations;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;

import lot.exceptions.services.ServiceException;
import lot.exceptions.services.ValidationException;
import lot.models.Passenger;
import lot.services.PassengerService;
import lot.controllers.utils.ControllerUtils;

import java.io.IOException;
import java.util.List;

/**
 * Controller for handling passenger-related operations in the UI.
 * Manages the display, search, addition, update, and deletion of passengers.
 */
public class PassengerOperationsController {
    @FXML
    private AnchorPane searchPane;
    @FXML
    private ComboBox<Integer> idSearchField;
    @FXML
    private TextField surnameSearchField;

    @FXML
    private TableView<Passenger> passengerTable;
    @FXML
    private TableColumn<Passenger, Integer> idColumn;
    @FXML
    private TableColumn<Passenger, String> nameColumn;
    @FXML
    private TableColumn<Passenger, String> surnameColumn;
    @FXML
    private TableColumn<Passenger, String> emailColumn;
    @FXML
    private TableColumn<Passenger, String> phoneNumberColumn;

    @FXML
    private ComboBox<Integer> deleteId;
    @FXML
    private Label choiceLabel;
    @FXML
    private AnchorPane deletionPane;

    @FXML
    private TextField nameField;
    @FXML
    private TextField surnameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private Label addLabel;
    @FXML
    private AnchorPane addPane;

    @FXML
    private AnchorPane updatePane;
    @FXML
    private TextField updateNameField;
    @FXML
    private TextField updateSurnameField;
    @FXML
    private TextField updateEmailField;
    @FXML
    private TextField updatePhoneNumberField;
    @FXML
    private Label updateLabel;
    @FXML
    private ComboBox<Integer> idToUpdateSelectorBox;

    private final ObservableList<Integer> ids = FXCollections.observableArrayList();
    private final PassengerService passengerService;
    private final ControllerUtils utils;


    /**
     * Constructs a PassengerOperationsController with the specified PassengerService.
     *
     * @param passengerService the service to handle passenger operations
     * @param utils the utils to handle management operations
     */
    public PassengerOperationsController(PassengerService passengerService, ControllerUtils utils) {
        this.passengerService = passengerService;
        this.utils = utils;
    }

    /**
     * Initializes the controller and sets up table column bindings.
     */
    @FXML
    public void initialize() {
        if (passengerTable != null) {
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
            emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
            phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        }
    }

    /**
     * Configures the list of passenger IDs for different operations.
     *
     * @param type the type of operation ("delete" or "update")
     */
    public void configurePassengersIds(String type) {
        ids.addAll(passengerService.getIds().stream().sorted().toList());
        if (type.equals("delete")) {
            deleteId.getItems().addAll(ids);
            deleteId.setOnAction(this::changeDeletionLabel);
            deleteId.setVisibleRowCount(5);
        }
        else if (type.equals("update")) {
            idToUpdateSelectorBox.setItems(ids);
            idToUpdateSelectorBox.setOnAction(this::loadSelectedPassengerDetails);
            idToUpdateSelectorBox.setVisibleRowCount(5);
        }
    }

    /**
     * Loads and displays all passengers in the table.
     */
    public void seeAllPassengers() {
        try {
            List<Passenger> passengers = passengerService.getAllPassengers();
            ObservableList<Passenger> flightObservableList = FXCollections.observableArrayList(passengers);
            passengerTable.setItems(flightObservableList);
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
                idSearchField.setVisible(true);
                ids.clear();
                ids.addAll(passengerService.getIds().stream().sorted().toList());
                idSearchField.setItems(ids);
                idSearchField.setVisibleRowCount(5);
                break;
            case "buttonSurnameSearch":
                surnameSearchField.setVisible(true);
                idSearchField.setVisible(false);
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
            if (idSearchField.isVisible()) {
                Integer id = idSearchField.getValue();
                if (id == null) {
                    return;
                }
                Passenger passenger = passengerService.getPassengerById(id);
                passengerTable.setItems(FXCollections.observableArrayList(passenger));
            }
            else if (surnameSearchField.isVisible()) {
                String surname = surnameSearchField.getText();
                if (surname.isEmpty()) {
                    return;
                }
                List<Passenger> passengers = passengerService.getPassengerBySurname(surname);
                passengerTable.setItems(FXCollections.observableArrayList(passengers));
            }
        }
        catch (ServiceException e) {
            utils.showApplicationErrorMessage(e.getMessage());
        }
    }

    /**
     * Deletes the selected passenger after confirmation.
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
                passengerService.deletePassenger(choice);
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
     * Adds a new passenger with the provided details.
     *
     * @param event the action event that triggered this method
     */
    @FXML
    private void addNewPassenger(ActionEvent event) {
        String name = nameField.getText();
        String surname = surnameField.getText();
        String email = emailField.getText();
        String phoneNumber = phoneNumberField.getText();

        if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || phoneNumber.isEmpty()) {
            addLabel.setText("You need to provide all information!");
            addLabel.setTextFill(Color.RED);
            return;
        }

        int id;
        try {
            id = passengerService.addNewPassenger(name, surname, email, phoneNumber);
        }
        catch (ValidationException e) {
            utils.showDataValidationErrorMessage(e.getMessage());
            addLabel.setText("Type new passenger data");
            addLabel.setTextFill(Color.BLACK);
            return;
        }
        catch (ServiceException e) {
            utils.showApplicationErrorMessage(e.getMessage());
            utils.clearForm(addPane, "Type new passenger data");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Adding new passenger");
        alert.setHeaderText("Passenger with id: " + id  + " has been added successfully");
        alert.showAndWait();

        utils.clearForm(addPane, "Type new passenger data");
    }

    /**
     * Updates an existing passenger with the provided details.
     *
     * @param event the action event that triggered this method
     */
    @FXML
    private void updatePassenger(ActionEvent event) {
        Integer id = idToUpdateSelectorBox.getValue();
        String name = updateNameField.getText();
        String surname = updateSurnameField.getText();
        String email = updateEmailField.getText();
        String phoneNumber = updatePhoneNumberField.getText();

        if (id == null || name.isEmpty() || surname.isEmpty() || email.isEmpty() || phoneNumber.isEmpty()) {
            updateLabel.setText("You need to provide all information!");
            updateLabel.setTextFill(Color.RED);
            return;
        }

        try {
            passengerService.updateExistingPassenger(id, name, surname, email, phoneNumber);
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
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Updating passenger");
        alert.setHeaderText("Passenger with id: " + id  + " has been updated successfully");
        alert.showAndWait();

        utils.clearForm(updatePane, "Provide updated data (first, on the left, select which to update)");
        idToUpdateSelectorBox = utils.clearComboBox(updatePane, idToUpdateSelectorBox);
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
            utils.loadView("/lot/views/menu/MenuView.fxml", event, "passenger");
        }
        catch (IOException e) {
            utils.showApplicationErrorMessage("Failed to load MenuView. " + e.getMessage());
        }
    }

    private void loadSelectedPassengerDetails(ActionEvent event) {
        Integer id = idToUpdateSelectorBox.getValue();
        if (id == null) {
            return;
        }
        Passenger passenger;
        try {
            passenger = passengerService.getPassengerById(id);
        }
        catch (ServiceException e) {
            utils.showApplicationErrorMessage(e.getMessage());
            utils.clearForm(updatePane, "Provide updated data (first, on the left, select which to update)");
            return;
        }

        updateNameField.setText(passenger.getName());
        updateSurnameField.setText(passenger.getSurname());
        updateEmailField.setText(passenger.getEmail());
        updatePhoneNumberField.setText(passenger.getPhoneNumber());
    }
}
