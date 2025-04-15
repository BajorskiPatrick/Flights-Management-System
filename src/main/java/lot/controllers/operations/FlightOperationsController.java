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
import lot.exceptions.services.NotFoundException;
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

public class FlightOperationsController {
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

    @FXML
    private AnchorPane deletionPane;

    private ObservableList<Integer> ids = FXCollections.observableArrayList();

    private Stage stage;
    private Scene scene;
    private Parent root;

    private final FlightService flightService;

    public FlightOperationsController(FlightService flightService) {
        this.flightService = flightService;
    }

    public void configureFlightsIds(String type) {
        ids.addAll(flightService.getIds().stream().sorted().collect(Collectors.toList()));

        if (type.equals("delete")) {
            deleteId.getItems().addAll(ids);
            deleteId.setOnAction(this::changeDeletionLabel);
            deleteId.setVisibleRowCount(5);
        }
        else if (type.equals("update")) {

        }
    }


    public void configureTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        departureColumn.setCellValueFactory(new PropertyValueFactory<>("departure"));
        destinationColumn.setCellValueFactory(new PropertyValueFactory<>("destination"));
        departureDateColumn.setCellValueFactory(new PropertyValueFactory<>("departureDate"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        seatRowsAmountColumn.setCellValueFactory(new PropertyValueFactory<>("seatRowsAmount"));
        twoWayColumn.setCellValueFactory(new PropertyValueFactory<>("twoWay"));
    }

    public void loadFlights() {
        try {
            List<Flight> flights = flightService.getAllFlights();
            ObservableList<Flight> flightObservableList = FXCollections.observableArrayList(flights);
            flightTable.setItems(flightObservableList);
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
        controller.setResourceType("flight");
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
                flightService.deleteFlight(choice);
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
            durationField.clear();
            return;
        }
        try {
            seatNum = Integer.parseInt(seatRowsAmount);
        }
        catch (NumberFormatException e) {
            addLabel.setText("Seat rows must be a number!");
            seatRowsAmountField.clear();
            return;
        }

        int id;
        try {
            id = flightService.addNewFlight(departure, destination, departureDate, time, durationNum, seatNum, twoWay);
        }
        catch (ValidationException e) {
            showDataErrorMessage(e.getMessage());
            clearForm(addPane, "Type new flight data");
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

    @FXML
    private void updateFlight(ActionEvent event) {

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
                ((ComboBox<?>) node).setValue(null);
            } else if (node instanceof Label) {
                ((Label) node).setText(label);
                ((Label) node).setTextFill(Color.BLACK);
            }
        }
    }
}
