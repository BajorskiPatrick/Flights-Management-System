package lot.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lot.controllers.menu.MenuController;

import java.io.IOException;

public class MainAppController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    public void manageFlights(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/lot/views/menu/MenuView.fxml"));
        root = loader.load();
        MenuController controller = loader.getController();
        controller.setResourceType("flight");
        setStage(event);
    }

    public void manageReservations(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/lot/views/menu/MenuView.fxml"));
        root = loader.load();
        MenuController controller = loader.getController();
        controller.setResourceType("reservation");
        setStage(event);
    }

    public void managePassengers(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/lot/views/menu/MenuView.fxml"));
        root = loader.load();
        MenuController controller = loader.getController();
        controller.setResourceType("passenger");
        setStage(event);
    }

    private void setStage(ActionEvent event) {
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/lot/css/Menu.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}
