package sk.hike_de_slovakia;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * This class represent switch controller between fxml scenes and views.
 * In this class is implemented default method implementation
 **/
public interface SceneController {

    /**
     * Thanks to this method application can switch between two fxml scenes and views.
     * Is used in many other classes.
     * @param newScene route to the new fxml scene file
     */
    default void switchScene(ActionEvent event, String newScene) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(newScene)));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}

