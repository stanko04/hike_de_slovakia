package sk.hike_de_slovakia.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import sk.hike_de_slovakia.GoBack;
import sk.hike_de_slovakia.SceneController;
import sk.hike_de_slovakia.instances.StringHolder;
import sk.hike_de_slovakia.instances.User;
import sk.hike_de_slovakia.instances.UserArray;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * This class is controller for fxml file which display details about user in admin account.
 * This class provided that admin user can change user's account settings and also delete user.
 * This class implements from Initializable and also SceneController
 * because from the display that controls this controller the user can be redirected to the next screen
 * This class also extends from class GoBack, because uses its method clickOnBack.
 */
public class UserDetailsController extends GoBack implements Initializable, SceneController {

    public Label name;
    public Label surname;
    public Label username;
    public Button deleteUser;
    public Label lastLogin;
    public ChoiceBox<String> createdJourneys;
    public ChoiceBox<String> completedJourneys;
    public Button deleteCreatedJourney;
    public Button deleteCompletedJourney;
    public TextField newUsername;
    public Button changeUsername;
    public Label popupMessage;

    /**
     * This method ensures that when the screen is loaded,
     * the user details of the admin user clicked are always displayed.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        UserArray userArray = UserArray.getInstance();
        StringHolder<String> stringHolder = StringHolder.getInstance();

        for(int i = 0; i < userArray.getSize(); i++) {
            if(userArray.getUser(i).getUsername().equals(stringHolder.getObj())) {
                name.setText(userArray.getUser(i).getFirstName());
                surname.setText(userArray.getUser(i).getSecondName());
                username.setText(userArray.getUser(i).getUsername());
                if (userArray.getUser(i).getLastLogin() != null) {
                    lastLogin.setText(lastLogin.getText() + " " + userArray.getUser(i).getLastLogin().toString());
                }
                for(int j = 0; j < userArray.getUser(i).getJourneys().size(); j++) {
                    if(!userArray.getUser(i).getJourneys().get(j).isCompleted()) {
                        createdJourneys.getItems().add(userArray.getUser(i).getJourneys().get(j).getName());
                    }
                    else {
                        completedJourneys.getItems().add(userArray.getUser(i).getJourneys().get(j).getName());
                    }
                }
                break;
            }
        }
    }

    /**
     * This method ensures deletion of the selected completed journey
     */
    public void clickOnDeleteCompletedJourney(ActionEvent event) {
        UserArray userArray = UserArray.getInstance();

        String nameOfDeletedJourney = completedJourneys.getValue();

        int index = 0;

        for(int i = 0; i < userArray.getSize(); i++) {
            if(userArray.getUser(i).getUsername().equals(username.getText())) {
                index = i;
                break;
            }
        }

        int pom = 0;

        for(int i = 0; i < userArray.getUser(index).getJourneys().size(); i++) {
            if(userArray.getUser(index).getJourneys().get(i).getName().equals(nameOfDeletedJourney)) {
                userArray.getUser(index).getJourneys().remove(i);

                deleteJourneyInFile(index-1, i);

                popupMessage.setText("Journey was deleted");
                pom = 1;
                break;
            }
        }

        if(pom == 1) {
            completedJourneys.getItems().clear();
            for(int j = 0; j < userArray.getUser(index).getJourneys().size(); j++) {
                if(userArray.getUser(index).getJourneys().get(j).isCompleted()) {
                    completedJourneys.getItems().add(userArray.getUser(index).getJourneys().get(j).getName());
                }
            }
        }

    }

    /**
     * This method ensures that the user's username is changed.
     */
    public void clickOnChangeUsername(ActionEvent event) {
        UserArray userArray = UserArray.getInstance();

        int index = getIndexOfUser(userArray);

        if(!newUsername.getText().equals("")) {

            if(newUsername.getText().equals(userArray.getUser(index).getUsername())) {
                newUsername.setText("");
            }

            else {
                for(int i = 0; i < userArray.getSize(); i++) {
                    if(userArray.getUser(i).getUsername().equals(newUsername.getText())) {
                        newUsername.setText("");
                        popupMessage.setText("Username is already used.");
                        return;
                    }
                }

                userArray.getUser(index).setUsername(newUsername.getText());

                changeUsernameInFile(index);


                newUsername.setText("");
                username.setText(userArray.getUser(index).getUsername());
                popupMessage.setText("Username was changed");

            }

        }
    }

    /**
     * This method ensures deletion of the selected created journey
     */
    public void clickOnDeleteCreatedJourney(ActionEvent event) {

        String nameOfDeletedJourney = createdJourneys.getValue();

        UserArray userArray = UserArray.getInstance();

        int index = getIndexOfUser(userArray);

        int pom = 0;

        for(int i = 0; i < userArray.getUser(index).getJourneys().size(); i++) {
            if(userArray.getUser(index).getJourneys().get(i).getName().equals(nameOfDeletedJourney)) {
                userArray.getUser(index).getJourneys().remove(i);

                deleteJourneyInFile(index-1, i);

                popupMessage.setText("Journey was deleted");
                pom = 1;
                break;
            }
        }

        if(pom == 1) {
            createdJourneys.getItems().clear();
            for(int j = 0; j < userArray.getUser(index).getJourneys().size(); j++) {
                if(!userArray.getUser(index).getJourneys().get(j).isCompleted()) {
                    createdJourneys.getItems().add(userArray.getUser(index).getJourneys().get(j).getName());
                }
            }
        }
    }

    /**
     * This method ensures deletion whole selected user's account.
     */
    public void clickOnDeleteUser(ActionEvent event) throws IOException {
        UserArray userArray = UserArray.getInstance();

        for(int i = 1; i < userArray.getSize(); i++) {
            if(userArray.getUser(i).getUsername().equals(username.getText())) {
                userArray.getArray().remove(i);
                userArray.setSize(userArray.getSize() - 1);
                break;
            }
        }

        removeUserInFile();

        switchScene(event, "/views/adminHomePage.fxml");
    }

    /**
     * This method ensures retrieved the index of the user to be displayed.
     */
    public int getIndexOfUser(UserArray userArray) {
        int index = 0;

        for(int i = 0; i < userArray.getSize(); i++) {
            if(userArray.getUser(i).getUsername().equals(username.getText())) {
                index = i;
                break;
            }
        }
        return index;
    }

    /**
     * This method ensures that the deleted user is also deleted from the serialize file.
     */
    public void removeUserInFile() {
        ArrayList<User> users = null;
        try (FileInputStream fis = new FileInputStream("userData");
             ObjectInputStream ois = new ObjectInputStream(fis);) {

            users = (ArrayList) ois.readObject();

        } catch (IOException ignored) {}
        catch (ClassNotFoundException c) {
            System.out.println("Class not found");
        }

        for(int i = 0; i < Objects.requireNonNull(users).size(); i++) {
            if (users.get(i).getUsername().equals(username.getText())) {
                users.remove(i);
                break;
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
     * This method ensures that the user's username change is also changed in the serialize file
     */
    public void changeUsernameInFile(int index) {
        ArrayList<User> users = null;
        try (FileInputStream fis = new FileInputStream("userData");
             ObjectInputStream ois = new ObjectInputStream(fis);) {

            users = (ArrayList) ois.readObject();

        } catch (IOException ignored) {}
        catch (ClassNotFoundException c) {
            System.out.println("Class not found");
        }

        assert users != null;
        users.get(index-1).setUsername(newUsername.getText());

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
     * This method ensures that the deleted journey is also deleted from the serialize file.
     */
    public void deleteJourneyInFile(int index, int i) {
        ArrayList<User> users = null;
        try (FileInputStream fis = new FileInputStream("userData");
             ObjectInputStream ois = new ObjectInputStream(fis);) {

            users = (ArrayList) ois.readObject();


        } catch (IOException ignored) {}
        catch (ClassNotFoundException c) {
            System.out.println("Class not found");
        }

        assert users != null;
        users.get(index).getJourneys().remove(i);


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
        switchScene(event, "/views/adminHomePage.fxml");
    }
}
