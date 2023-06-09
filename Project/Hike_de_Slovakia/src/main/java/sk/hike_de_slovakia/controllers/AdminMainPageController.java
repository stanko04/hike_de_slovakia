package sk.hike_de_slovakia.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import sk.hike_de_slovakia.SceneController;
import sk.hike_de_slovakia.instances.StringHolder;
import sk.hike_de_slovakia.instances.UserArray;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 *  This class is controller for fxml file which display the main page for admin user.
 *  This class implements from Initializable and also from SceneController,
 *  because from the display that controls this controller the user can be redirected to the next screen
 **/
public class AdminMainPageController implements Initializable, SceneController {

    public Button logoutButton;
    public ListView<String> userList;
    public Button showUserButton;
    public Button addUserButton;


    /**
     * We need this method because, on the start of view which is controlled by this controller,
     * we need initialize and display the all the user into list view.
     **/
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        UserArray userArray = UserArray.getInstance();

        if(userArray.getSize() > 1) {
            for(int i = 1; i < userArray.getSize(); i++) {
                userList.getItems().add(userArray.getUser(i).getUsername());
            }
        }


    }

    /**
     * This method ensures that when user clicks on the logout button,
     * he will be logged out from his admin account.
     **/
    public void clickOnLogout(ActionEvent event) throws IOException {
        switchScene(event, "/views/loginPage.fxml");
    }

    /**
     * This method ensures that when user clicks on the add user button,
     * he will be redirected into other fxml view.
     **/
    public void clickOnAddUser(ActionEvent event) throws IOException {
        switchScene(event, "/views/addUserAsAdmin.fxml");
    }

    /**
     * This method ensures that when user clicks on the show user button,
     * he will be redirected into other fxml view.
     * In this method is set up string holder,
     * thanks to which in the next fxml view application knows which user must display
     **/
    public void clickOnShowUser(ActionEvent event) throws IOException {
        if(userList.getSelectionModel().getSelectedItem() != null) {
            StringHolder<String> stringHolder = StringHolder.getInstance();
            stringHolder.setObj(userList.getSelectionModel().getSelectedItem());
            switchScene(event, "/views/userDetailsPage.fxml");
        }
    }
}
