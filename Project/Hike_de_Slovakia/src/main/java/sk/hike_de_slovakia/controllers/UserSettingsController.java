package sk.hike_de_slovakia.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import sk.hike_de_slovakia.GoBack;
import sk.hike_de_slovakia.SceneController;
import sk.hike_de_slovakia.instances.LoggedUser;
import sk.hike_de_slovakia.instances.User;
import sk.hike_de_slovakia.instances.UserArray;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * This class is controller for fxml file which display setting about user in account.
 * This class provided that user can change user's account settings .
 * This class implements from Initializable and also SceneController
 * because from the display that controls this controller the user can be redirected to the next screen
 * This class also extends from class GoBack, because uses its method clickOnBack.
 */
public class UserSettingsController extends GoBack implements SceneController, Initializable {

    public Button backButton;
    public PasswordField newPassword;
    public PasswordField newPasswordAgain;
    public Button changePassword;
    public TextField newUsername;
    public Button changeUsername;
    public ChoiceBox<String> createdJourney;
    public Button deleteJourney;
    public Label popupMessage;

    /**
     * This method ensures that when the screen is loaded,
     * the application show details about user which is logged in.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LoggedUser loggedUser = LoggedUser.getInstance();

        if(loggedUser.getJourneys().size() > 0) {
            for(int i = 0; i < loggedUser.getJourneys().size(); i++) {
                if(!loggedUser.getJourneys().get(i).isCompleted()) {
                    createdJourney.getItems().add(loggedUser.getJourneys().get(i).getName());
                }
            }
        }
    }

    /**
     * This method ensures that the user's password is changed.
     */
    public void clickOnChangePassword() {
        LoggedUser loggedUser = LoggedUser.getInstance();

        if(!newPassword.getText().equals("") && !newPasswordAgain.getText().equals("")) {
            if(!newPassword.getText().equals(newPasswordAgain.getText())) {
                newPassword.setText("");
                newPasswordAgain.setText("");
                popupMessage.setText("Passwords does not match !");
            }
            else {
                if(newPassword.getText().length() < 8) {
                    newPassword.setText("");
                    newPasswordAgain.setText("");
                    popupMessage.setText("Password must contains 8 characters");
                }
                else {
                    UserArray userArray = UserArray.getInstance();
                    for(int i = 0; i < userArray.getSize(); i++) {
                        if(userArray.getUser(i).getUsername().equals(loggedUser.getUsername())) {
                            userArray.getUser(i).setPassword(newPasswordAgain.getText());
                        }
                    }

                    changePasswordInFile(userArray, loggedUser);


                    newPassword.setText("");
                    newPasswordAgain.setText("");
                    popupMessage.setText("Your password was changed.");
                }
            }
        }
    }

    /**
     * This method ensures that the user's username is changed.
     */
    public void clickOnChangeUsername() {
        UserArray userArray = UserArray.getInstance();
        LoggedUser loggedUser = LoggedUser.getInstance();

        if(!newUsername.getText().equals("")) {

            if(newUsername.getText().equals(loggedUser.getUsername())) {
                newUsername.setText("");
                popupMessage.setText("It is your actual username");
                return;
            }

            else {
                for(int i = 0; i < userArray.getSize(); i++) {
                    if(userArray.getUser(i).getUsername().equals(newUsername.getText())) {
                        newUsername.setText("");
                        popupMessage.setText("Username is already used.");
                        return;
                    }
                }

                for(int i = 0; i < userArray.getSize(); i++) {
                    if(userArray.getUser(i).getUsername().equals(loggedUser.getUsername())) {
                        userArray.getUser(i).setUsername(newUsername.getText());
                    }
                }

                changeUsernameInFile(userArray, loggedUser);
            }
            newUsername.setText("");
            popupMessage.setText("Your username was changed");

        }
    }

    /**
     * This method ensures deletion of the selected created journey
     */
    public void clickOnDeleteJourney() {
        String nameOfDeletedJourney = createdJourney.getValue();

        LoggedUser loggedUser = LoggedUser.getInstance();

        int index = 0;

        for(int i = 0; i < loggedUser.getJourneys().size(); i++) {
            if(loggedUser.getJourneys().get(i).getName().equals(nameOfDeletedJourney)) {
                loggedUser.getJourneys().remove(i);
                index = 1;
                break;
            }
        }

        deleteCreatedJourneyInFile(loggedUser, nameOfDeletedJourney);

        if(index == 1) {
            popupMessage.setText("Your created journey was successfully deleted.");
            createdJourney.getItems().clear();
            for(int j = 0; j < loggedUser.getJourneys().size(); j++) {
                if(!loggedUser.getJourneys().get(j).isCompleted()) {
                    createdJourney.getItems().add(loggedUser.getJourneys().get(j).getName());
                }
            }
        }


    }

    /**
     * This method ensures that the user's username change is also changed in the serialize file
     */
    public void changeUsernameInFile(UserArray userArray, LoggedUser loggedUser) {
        ArrayList<User> users = null;
        try (FileInputStream fis = new FileInputStream("userData");
             ObjectInputStream ois = new ObjectInputStream(fis);) {

            users = (ArrayList) ois.readObject();

        }
        catch (IOException ignored) {}
        catch (ClassNotFoundException c) {
            System.out.println("Class not found");
        }

        for(int i = 0; i < Objects.requireNonNull(users).size(); i++) {
            if(users.get(i).getUsername().equals(loggedUser.getUsername())) {
                users.get(i).setUsername(newUsername.getText());
            }
        }

        try (FileOutputStream fos = new FileOutputStream("userData");
             ObjectOutputStream oos = new ObjectOutputStream(fos);) {

            oos.writeObject(users);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * This method ensures that the user's password change is also changed in the serialize file
     */
    public void changePasswordInFile(UserArray userArray, LoggedUser loggedUser) {
        ArrayList<User> users = null;
        try (FileInputStream fis = new FileInputStream("userData");
             ObjectInputStream ois = new ObjectInputStream(fis);) {

            users = (ArrayList) ois.readObject();

        }
        catch (IOException ignored) {}
        catch (ClassNotFoundException c) {
            System.out.println("Class not found");
        }

        for(int i = 0; i < Objects.requireNonNull(users).size(); i++) {
            if(users.get(i).getUsername().equals(loggedUser.getUsername())) {
                users.get(i).setPassword(newPasswordAgain.getText());
            }
        }

        try (FileOutputStream fos = new FileOutputStream("userData");
             ObjectOutputStream oos = new ObjectOutputStream(fos);) {

            oos.writeObject(users);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * This method ensures that the deleted user is also deleted from the serialize file.
     */
    public void deleteCreatedJourneyInFile(LoggedUser loggedUser, String nameOfDeletedJourney) {
        ArrayList<User> users = null;
        try (FileInputStream fis = new FileInputStream("userData");
             ObjectInputStream ois = new ObjectInputStream(fis);) {

            users = (ArrayList) ois.readObject();


        } catch (IOException ignored) {}
        catch (ClassNotFoundException c) {
            System.out.println("Class not found");
        }

        for(int i = 0; i < Objects.requireNonNull(users).size(); i++) {
            if(users.get(i).getUsername().equals(loggedUser.getUsername())) {
                for(int j = 0; j < users.get(i).getJourneys().size(); j++) {
                    if(users.get(i).getJourneys().get(j).getName().equals(nameOfDeletedJourney)) {
                        users.get(i).getJourneys().remove(j);
                        break;
                    }
                }
            }
        }

        try (FileOutputStream fos = new FileOutputStream("userData");
             ObjectOutputStream oos = new ObjectOutputStream(fos);) {

            oos.writeObject(users);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * This method ensures that if the user clicks on the back button,
     * he will be redirected to the previous screen.
     **/
    @Override
    public void clickOnBack(ActionEvent event) throws IOException {
        switchScene(event, "/views/mainPage.fxml");
    }
}
