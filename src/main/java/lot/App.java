package lot;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lot.database.DatabaseInitializer;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Main application class that serves as the entry point for the Flight Management System.
 * Extends JavaFX Application class to provide the GUI interface.
 */
public class App extends Application {
    /**
     * Constructs a new instance of the class with default values.
     * Initializes all fields to their default initial values.
     */
    public App() {}

    /**
     * The main entry point for JavaFX applications.
     * Initializes and configures the primary application window.
     *
     * @param primaryStage the primary stage for this application
     * @throws IOException if the FXML file cannot be loaded
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/lot/views/MainApp.fxml"));

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/lot/css/MainApp.css").toExternalForm());

            primaryStage.setTitle("Flight Management System");
            primaryStage.setScene(scene);
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/plane.png")));
            primaryStage.setResizable(false);
            primaryStage.show();
        }
        catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Critical Error");
            alert.setHeaderText("Failed to load application resources");
            alert.setContentText("Could not load required files to start the application.\n"
                    + "Please reinstall the application.\n\n"
                    + "Technical details: " + e.getMessage());
            alert.showAndWait();

            Platform.exit();
        }
        catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Resource Error");
            alert.setHeaderText("Missing application resources");
            alert.setContentText("Required application files are missing or corrupted.\n"
                    + "Please reinstall the application.");
            alert.showAndWait();

            Platform.exit();
        }
    }

    /**
     * Main method that launches the application.
     * Initializes the database before starting the JavaFX application.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            DatabaseInitializer.initialize();
        }
        catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }

        launch();
    }
}
