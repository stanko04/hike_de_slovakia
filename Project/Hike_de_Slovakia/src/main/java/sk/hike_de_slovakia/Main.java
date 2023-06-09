package sk.hike_de_slovakia;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import sk.hike_de_slovakia.instances.User;
import sk.hike_de_slovakia.instances.UserArray;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Objects;


/**
 * This is the main class of whole application.
 * This class starts the whole application process.
 **/
public class Main extends Application {

    /**
     * In this method we set all the application details.
     * Also add the all users from serializable file into UserArray instance.
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/loginPage.fxml")));
            primaryStage.setTitle("Hike de Slovakia");
            Scene scene = new Scene(root,600,400);
            primaryStage.setScene(scene);
            primaryStage.show();
            primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/logo.png"))));
        } catch(Exception e) {
            e.printStackTrace();
        }
        UserArray userArray = UserArray.getInstance();
        User adminUser = new User("Admin", "Admin", "adminadmin", "adminadmin", null);
        userArray.addUser(adminUser);

        ArrayList<User> users = null;
        try (FileInputStream fis = new FileInputStream("userData");
             ObjectInputStream ois = new ObjectInputStream(fis);) {

            users = (ArrayList) ois.readObject();

            for(int i = 0; i < Objects.requireNonNull(users).size(); i++) {
                userArray.addUser(users.get(i));
            }

        } catch (IOException ioe) {
//				ioe.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("Class not found");
//				c.printStackTrace();
        }

    }
    public static void main(String[] args) {
        launch(args);
    }

}
