package sk.hike_de_slovakia.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.TextAlignment;
import sk.hike_de_slovakia.GoBack;
import sk.hike_de_slovakia.JourneyIndex;
import sk.hike_de_slovakia.SceneController;
import sk.hike_de_slovakia.instances.LoggedUser;

import sk.hike_de_slovakia.instances.PlaceNameHolder;
import sk.hike_de_slovakia.instances.StringHolder;
import sk.hike_de_slovakia.instances.User;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 *  This class is controller for fxml file which display page where is displayed details about created journey's place.
 *  The file for which this class controller displays the details of the specific places of the selected and created route.
 *  This class implements from Initializable SceneController and also from JourneyIndex
 *  because from the display that controls this controller the user can be redirected to the next screen
 *  This class also extends from class GoBack, because uses its method clickOnBack.
 **/
public class DisplayJourneyController extends GoBack implements Initializable, SceneController, JourneyIndex {

    @FXML
    public Button place1;
    @FXML
    public Button place2;
    @FXML
    public Button place3;
    @FXML
    public Button place4;
    @FXML
    public Button place5;
    @FXML
    public Button backButton;
    @FXML
    public ArrayList<Button> placeList;
    @FXML
    public Label description;
    @FXML
    public ImageView place_image;
    @FXML
    public Label name;
    @FXML
    public Label hikeRoute;
    @FXML
    public Label duration;
    @FXML
    public Label length;
    @FXML
    public Label elevation;
    @FXML
    public Label maxAltitude;
    @FXML
    public ToggleButton visitedButton;
    @FXML
    public ToggleButton unvisitedButton;
    public Button addNoteButton;

    ToggleGroup toggleGroup = new ToggleGroup();

    /**
     * In this method application initialize and display all the details about specific place the user clicked on.
     * Also controls two buttons that show whether the place is visited or not.
     **/
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LoggedUser loggedUser = LoggedUser.getInstance();
        int index = getIndexOfJourney();

        for(int i=0; i < loggedUser.getJourneys().get(index).getPlaces().size(); i++) {
            placeList.get(i).setText(loggedUser.getJourneys().get(index).getPlaces().get(i).getShortName());
        }

        int placeIndex = 0;
        for(int i=0; i < loggedUser.getJourneys().get(index).getPlaces().size(); i++) {
            if(loggedUser.getJourneys().get(index).getPlaces().get(i).getName().equals(name.getText())) {
                placeIndex = i;
                break;
            }
        }

        name.setText(loggedUser.getJourneys().get(index).getPlaces().get(placeIndex).getName());

        description.setTextAlignment(TextAlignment.JUSTIFY);
        description.setAlignment(Pos.TOP_LEFT);
        description.setText(loggedUser.getJourneys().get(index).getPlaces().get(placeIndex).getDesc());

        hikeRoute.setTextAlignment(TextAlignment.JUSTIFY);
        hikeRoute.setAlignment(Pos.TOP_LEFT);
        hikeRoute.setText(loggedUser.getJourneys().get(index).getPlaces().get(placeIndex).getHikeRoute());

        duration.setText("Duration: " + loggedUser.getJourneys().get(index).getPlaces().get(placeIndex).getDuration() + "h");
        length.setText("Length: " + loggedUser.getJourneys().get(index).getPlaces().get(placeIndex).getLength() + "km");
        elevation.setText("Elevation: " + loggedUser.getJourneys().get(index).getPlaces().get(placeIndex).getElevation() + "m");
        maxAltitude.setText("Highest point: " + loggedUser.getJourneys().get(index).getPlaces().get(placeIndex).getMaxAltitude());

        if(!loggedUser.getJourneys().get(index).getPlaces().get(placeIndex).isVisited()) {
            unvisitedButton.setStyle("-fx-background-color: red;");
            unvisitedButton.setSelected(true);
            visitedButton.setStyle("-fx-background-color: gray;");
        }
        else  {
            visitedButton.setStyle("-fx-background-color: green;");
            visitedButton.setSelected(true);
            unvisitedButton.setStyle("-fx-background-color: gray;");
        }


        InputStream inStream = getClass().getResourceAsStream(loggedUser.getJourneys().get(index).getPlaces().get(placeIndex).getImage());
        assert inStream != null;
        Image imageObject = new Image(inStream);
        place_image.setImage(imageObject);


        toggleGroup.getToggles().addAll(unvisitedButton, visitedButton);

        toggleGroup.selectedToggleProperty().addListener((ov, toggle, newToggle) -> {
            int placeIndex1 = 0;
            for(int i=0; i < loggedUser.getJourneys().get(index).getPlaces().size(); i++) {
                if(loggedUser.getJourneys().get(index).getPlaces().get(i).getName().equals(name.getText())) {
                    placeIndex1 = i;
                    break;
                }
            }
            if (((ToggleButton)newToggle).getText().equals("Visited") && ((ToggleButton)toggle).getText().equals("Unvisited")) {
                visitedButton.setStyle("-fx-background-color: green;");
                unvisitedButton.setStyle("-fx-background-color: gray;");
                loggedUser.getJourneys().get(index).getPlaces().get(placeIndex1).setVisited(true);

                setPlaceVisitedInFile(loggedUser, index, placeIndex1, true);
            }
            if (((ToggleButton)newToggle).getText().equals("Unvisited") && ((ToggleButton)toggle).getText().equals("Visited")) {
                unvisitedButton.setStyle("-fx-background-color: red;");
                visitedButton.setStyle("-fx-background-color: gray;");
                loggedUser.getJourneys().get(index).getPlaces().get(placeIndex1).setVisited(false);

                setPlaceVisitedInFile(loggedUser, index, placeIndex1, false);
            }
        });
    }

    /**
     * This method ensures that if the user clicks on the visited or unvisited button,
     * the status of that place will also be changed in the serialize file,
     * so that the data will be up-to-date even after it is turned off and on again.
     */
    private void setPlaceVisitedInFile(LoggedUser loggedUser, int index, int placeIndex1, boolean isVisited) {
        ArrayList<User> users = null;
        try (FileInputStream fis = new FileInputStream("userData");
             ObjectInputStream ois = new ObjectInputStream(fis);) {

            users = (ArrayList) ois.readObject();


        } catch (IOException ignored) {}
        catch (ClassNotFoundException c) {
            System.out.println("Class not found");
        }

        for(int j = 0; j < Objects.requireNonNull(users).size(); j++) {
            if(users.get(j).getUsername().equals(loggedUser.getUsername())) {
                users.get(j).getJourneys().get(index).getPlaces().get(placeIndex1).setVisited(isVisited);
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
     * This method ensures that the data and details of the place where the user clicked are always displayed,
     * because the menu is dynamic
     */
    public void clickOnPlace(ActionEvent event) {
        LoggedUser loggedUser = LoggedUser.getInstance();

        String placeString = ((Button)event.getSource()).getText();
        int index = getIndexOfJourney();

        for(int i=0; i < loggedUser.getJourneys().get(index).getPlaces().size(); i++) {
            if(loggedUser.getJourneys().get(index).getPlaces().get(i).getShortName().equals(placeString)) {

                name.setText(loggedUser.getJourneys().get(index).getPlaces().get(i).getName());

                description.setTextAlignment(TextAlignment.JUSTIFY);
                description.setAlignment(Pos.TOP_LEFT);
                description.setText(loggedUser.getJourneys().get(index).getPlaces().get(i).getDesc());

                hikeRoute.setTextAlignment(TextAlignment.JUSTIFY);
                hikeRoute.setAlignment(Pos.TOP_LEFT);
                hikeRoute.setText(loggedUser.getJourneys().get(index).getPlaces().get(i).getHikeRoute());

                duration.setText("Duration: " + loggedUser.getJourneys().get(index).getPlaces().get(i).getDuration() + "h");
                length.setText("Length: " + loggedUser.getJourneys().get(index).getPlaces().get(i).getLength() + "km");
                elevation.setText("Elevation: " + loggedUser.getJourneys().get(index).getPlaces().get(i).getElevation() + "m");
                maxAltitude.setText("Highest point: " + loggedUser.getJourneys().get(index).getPlaces().get(i).getMaxAltitude());

                if(!loggedUser.getJourneys().get(index).getPlaces().get(i).isVisited()) {
                    unvisitedButton.setStyle("-fx-background-color: red;");
                    unvisitedButton.setSelected(true);
                    visitedButton.setStyle("-fx-background-color: gray;");
                }
                else {
                    visitedButton.setStyle("-fx-background-color: green;");
                    visitedButton.setSelected(true);
                    unvisitedButton.setStyle("-fx-background-color: gray;");
                }

                InputStream inStream = getClass().getResourceAsStream(loggedUser.getJourneys().get(index).getPlaces().get(i).getImage());
                assert inStream != null;
                Image imageObject = new Image(inStream);
                place_image.setImage(imageObject);
            }
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

    /**
     * This method ensures that if the user clicks on the add/display notes button,
     * the application redirects him to the add notes screen.
     **/
    public void clickOnAddNote(ActionEvent event) throws IOException {
        PlaceNameHolder<String> placeString = PlaceNameHolder.getInstance();
        placeString.setObj(name.getText());
        switchScene(event, "/views/addNoteForPlace.fxml");
    }
}
