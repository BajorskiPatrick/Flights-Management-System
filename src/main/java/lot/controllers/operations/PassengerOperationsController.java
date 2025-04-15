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
import lot.exceptions.services.NotFoundException;
import lot.exceptions.services.ServiceException;
import lot.exceptions.services.ValidationException;
import lot.models.Passenger;
import lot.services.PassengerService;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class PassengerOperationsController {
    @FXML
    public ComboBox<Integer> idSearchField;
    @FXML
    public TextField surnameSearchField;

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

    private ObservableList<Integer> ids = FXCollections.observableArrayList();
    private Stage stage;
    private Scene scene;
    private Parent root;
    private final PassengerService passengerService;

    public PassengerOperationsController(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

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

    public void configurePassengersIds(String type) {
        ids.addAll(passengerService.getIds().stream().sorted().collect(Collectors.toList()));
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

    public void seeAllPassengers() {
        try {
            List<Passenger> passengers = passengerService.getAllPassengers();
            ObservableList<Passenger> flightObservableList = FXCollections.observableArrayList(passengers);
            passengerTable.setItems(flightObservableList);
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
                idSearchField.setVisible(true);
                ids.clear();
                ids.addAll(passengerService.getIds().stream().sorted().collect(Collectors.toList()));
                idSearchField.setItems(ids);
                idSearchField.setVisibleRowCount(5);
                idSearchField.setPromptText("Select ID");
                break;
            case "buttonSurnameSearch":
                surnameSearchField.setVisible(true);
                idSearchField.setVisible(false);
                break;
        }
    }

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
                passengerService.deletePassenger(choice);
            }
            catch (NotFoundException e) {
                showDataErrorMessage(e.getMessage());
            }
            catch (ServiceException e) {
                showApplicationErrorMessage(e.getMessage());
            }
            ids.remove(choice);
            deleteId.setItems(ids);
        }
        clearForm(deletionPane, "Select id for deletion");
    }

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
            showDataErrorMessage(e.getMessage());
            addLabel.setText("Type new passenger data");
            addLabel.setTextFill(Color.BLACK);
            return;
        }
        catch (ServiceException e) {
            showApplicationErrorMessage(e.getMessage());
            clearForm(addPane, "Type new passenger data");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Adding new passenger");
        alert.setHeaderText("Passenger with id: " + id  + " has been added successfully");
        alert.showAndWait();

        clearForm(addPane, "Type new passenger data");
    }

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
            showDataErrorMessage(e.getMessage());
            updateLabel.setText("Provide updated data (first, on the left, select which to update)");
            updateLabel.setTextFill(Color.BLACK);
            return;
        }
        catch (ServiceException e) {
            showApplicationErrorMessage(e.getMessage());
            clearForm(updatePane, "Provide updated data (first, on the left, select which to update)");
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Updating passenger");
        alert.setHeaderText("Passenger with id: " + id  + " has been updated successfully");
        alert.showAndWait();

        clearForm(updatePane, "Provide updated data (first, on the left, select which to update)");
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
        controller.setResourceType("passenger");
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/lot/css/Menu.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
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
            showApplicationErrorMessage(e.getMessage());
            clearForm(updatePane, "Provide updated data (first, on the left, select which to update)");
            return;
        }

        updateNameField.setText(passenger.getName());
        updateSurnameField.setText(passenger.getSurname());
        updateEmailField.setText(passenger.getEmail());
        updatePhoneNumberField.setText(passenger.getPhoneNumber());
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
