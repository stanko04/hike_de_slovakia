package sk.hike_de_slovakia.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import sk.hike_de_slovakia.GoBack;
import sk.hike_de_slovakia.JourneyIndex;
import sk.hike_de_slovakia.SceneController;
import sk.hike_de_slovakia.instances.LoggedUser;
import sk.hike_de_slovakia.instances.User;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 *  This class is controller for fxml file which display page where is displayed details about completed journey.
 *  This class implements from Initializable SceneController and also from JourneyIndex
 *  because from the display that controls this controller the user can be redirected to the next screen
 *  This class also extends from class GoBack, because uses its method clickOnBack.
 **/
public class DisplayCompletedJourneyController extends GoBack implements SceneController, Initializable, JourneyIndex {
    @FXML
    public Label nameOfJourney;
    @FXML
    public Label numberOfPlaces;
    @FXML
    public Label totalKilometers;
    @FXML
    public Label totalElevation;
    @FXML
    public Label totalDuration;
    @FXML
    public Button deleteJourney;

    /**
     * In this method application initialize and display all the details about completed journey which is displayed.
     **/
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LoggedUser loggedUser = LoggedUser.getInstance();
        int index = getIndexOfJourney();

        nameOfJourney.setText(loggedUser.getJourneys().get(index).getName());
        numberOfPlaces.setText(numberOfPlaces.getText() + " " + loggedUser.getJourneys().get(index).getPlaces().size());
        float numberOfTotalKilometers = 0;
        int numberOfTotalElevation = 0;
        int totalHours = 0;
        int totalMinutes = 0;
        for(int i = 0; i < loggedUser.getJourneys().get(index).getPlaces().size(); i++) {
            numberOfTotalKilometers = numberOfTotalKilometers + Float.parseFloat(loggedUser.getJourneys().get(index).getPlaces().get(i).getLength());
            numberOfTotalElevation = numberOfTotalElevation + Integer.parseInt(loggedUser.getJourneys().get(index).getPlaces().get(i).getElevation());
            String hod = loggedUser.getJourneys().get(index).getPlaces().get(i).getDuration().split(":")[0];
            String min = loggedUser.getJourneys().get(index).getPlaces().get(i).getDuration().split(":")[1];
            totalHours = totalHours + Integer.parseInt(hod);
            totalMinutes = totalMinutes + Integer.parseInt(min);
        }

        totalKilometers.setText(totalKilometers.getText() + String.valueOf(numberOfTotalKilometers) + "km");
        totalElevation.setText(totalElevation.getText() + String.valueOf(numberOfTotalElevation) + "m");
        totalHours = totalHours + (totalMinutes / 60);
        totalMinutes = totalMinutes % 60;
        totalDuration.setText(totalDuration.getText() + totalHours + ":" + totalMinutes + "h");
    }

    /**
     * This method ensure deletion of journey, when the user clicks on the delete journey button.
     **/
    public void deleteJourney(ActionEvent event) throws IOException {
        int index = getIndexOfJourney();
        LoggedUser loggedUser = LoggedUser.getInstance();
        loggedUser.getJourneys().remove(index);
        deleteJourneyInFile(loggedUser, index);

        switchScene(event, "/views/mainPage.fxml");

    }

    /**
     * This method ensures, that if the journey is deleted,
     * will be also deleted in the serialze file.
     **/
    public void deleteJourneyInFile(LoggedUser loggedUser, int index) {
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
                users.get(i).getJourneys().remove(index);
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
