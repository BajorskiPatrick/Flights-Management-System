package lot.controllers.utils;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lot.controllers.menu.MenuController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class providing common controller-related operations for JavaFX applications.
 * Contains methods for view loading, error handling, form clearing, and ComboBox management.
 */
public class ControllerUtils {
    /**
     * Constructs a new instance of the class with default values.
     */
    public ControllerUtils() {}

    /**
     * Loads a new view from the specified FXML file and replaces the current scene.
     *
     * @param viewPath      the path to the FXML file to load
     * @param event         the ActionEvent that triggered the view change
     * @param resourceType  the type of resource to be set in the MenuController
     * @throws IOException  if the FXML file cannot be loaded
     */
    public void loadView(String viewPath, ActionEvent event, String resourceType) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(viewPath));
        Parent root = loader.load();
        MenuController controller = loader.getController();
        controller.setResourceType(resourceType);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/lot/css/Menu.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Displays an error alert for unexpected application exceptions.
     *
     * @param message  the detailed error message to display
     */
    public void showApplicationErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Application exception");
        alert.setHeaderText("Unexpected exception has occurred");
        alert.setContentText("Details: " + message);
        alert.showAndWait();
    }

    /**
     * Displays a warning alert for data-related exceptions.
     *
     * @param message  the detailed warning message to display
     */
    public void showDataValidationErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Data validation exception");
        alert.setHeaderText("Problem related to provided data has occurred");
        alert.setContentText("Details: " + message);
        alert.showAndWait();
    }

    /**
     * Clears all input fields in the specified AnchorPane.
     *
     * @param anchorPane  the container holding the form elements to clear
     * @param label       the default text to set for Label controls
     */
    public void clearForm(AnchorPane anchorPane, String label) {
        List<Node> nodesToClear = new ArrayList<>(anchorPane.getChildren());

        for (Node node : nodesToClear) {
            if (node instanceof TextField) {
                ((TextField) node).clear();
            } else if (node instanceof DatePicker) {
                ((DatePicker) node).setValue(null);
            } else if (node instanceof CheckBox) {
                ((CheckBox) node).setSelected(false);
            } else if (node instanceof Label) {
                ((Label) node).setText(label);
                ((Label) node).setTextFill(Color.BLACK);
            }
        }
    }

    /**
     * Recreates a ComboBox with the same properties but cleared state.
     * Useful for completely resetting a ComboBox while maintaining its visual properties.
     *
     * @param <T>        the type of elements in the ComboBox
     * @param anchorPane the parent container of the ComboBox
     * @param comboBox   the ComboBox to reset
     * @return the newly created ComboBox instance
     */
    public <T> ComboBox<T> clearComboBox(AnchorPane anchorPane, ComboBox<T> comboBox) {
        String prompt = comboBox.getPromptText();
        double layoutX = comboBox.getLayoutX();
        double layoutY = comboBox.getLayoutY();
        double prefWidth = comboBox.getPrefWidth();
        double prefHeight = comboBox.getPrefHeight();
        Object userData = comboBox.getUserData();
        int count = comboBox.getVisibleRowCount();
        boolean visibility = comboBox.isVisible();
        EventHandler<ActionEvent> event = comboBox.getOnAction();
        ObservableList<T> items = comboBox.getItems();

        anchorPane.getChildren().remove(comboBox);

        ComboBox<T> newComboBox = new ComboBox<>();
        newComboBox.setPromptText(prompt);
        newComboBox.setUserData(userData);
        newComboBox.setLayoutX(layoutX);
        newComboBox.setLayoutY(layoutY);
        newComboBox.setPrefWidth(prefWidth);
        newComboBox.setPrefHeight(prefHeight);
        newComboBox.setItems(items);
        newComboBox.setVisibleRowCount(count);
        newComboBox.setVisible(visibility);
        newComboBox.setOnAction(event);

        newComboBox.getStyleClass().setAll(comboBox.getStyleClass());
        newComboBox.setStyle(comboBox.getStyle());

        anchorPane.getChildren().add(newComboBox);

        return newComboBox;
    }
}
