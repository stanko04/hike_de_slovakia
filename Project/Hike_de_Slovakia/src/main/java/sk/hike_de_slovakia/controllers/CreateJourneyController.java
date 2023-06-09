package sk.hike_de_slovakia.controllers;

import com.gluonhq.charm.glisten.control.TextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import sk.hike_de_slovakia.GoBack;
import sk.hike_de_slovakia.SceneController;

import javafx.event.ActionEvent;
import sk.hike_de_slovakia.instances.Journey;
import sk.hike_de_slovakia.instances.LoggedUser;
import sk.hike_de_slovakia.instances.User;
//import sk.hike_de_slovakia.instances.Place;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 *  This class is controller for fxml file which display page where user can create his journey.
 *  This class implements from Initializable and also from SceneController,
 *  because from the display that controls this controller the user can be redirected to the next screen
 *  This class also extends from class GoBack, because uses its method clickOnBack.
 **/
public class CreateJourneyController extends GoBack implements Initializable, SceneController {

//    FXML annotations
    @FXML
    public ChoiceBox<String> difficultyOfHikes;
    @FXML
    public ChoiceBox<String> partOfSlovakia;
    @FXML
    public ChoiceBox<String> lengthOfJourney;
    @FXML
    public Button createJourneyButton;
    @FXML
    public Button backButton;
    @FXML
    public Label errorLabel;
    @FXML
    public TextField nameOfJourney;


    //    variables
    public String[] difficultiesArray = {"easy", "medium", "hard"};

    public String[] partsArray = {"west", "middle", "east"};

    public String[] lengthArray = {"short (3 hikes)", "long (5 hikes)"};

    /**
     * In this method application adds values into Choice Boxes.
     **/
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        difficultyOfHikes.getItems().addAll(difficultiesArray);
        lengthOfJourney.getItems().addAll(lengthArray);
        partOfSlovakia.getItems().addAll(partsArray);
    }

    /**
     * This method ensures that if the user click on create journey, the journey will be created.
     * Also, user must have filled all the text fields and choice boxes.
     **/
    @FXML
    public void clickCreateJourney(ActionEvent event) throws IOException {
        if((nameOfJourney.getText().isEmpty() || nameOfJourney.getText().length() > 8)
            || (difficultyOfHikes.getValue() == null)
            || (lengthOfJourney.getValue() == null) || (partOfSlovakia.getValue() == null) ) {
            difficultyOfHikes.valueProperty().set(null);
            lengthOfJourney.valueProperty().set(null);
            partOfSlovakia.valueProperty().set(null);
            nameOfJourney.setText("");
            errorLabel.setText("All fields must be filled in, the journeys name can have a maximum of 8 characters ");
        }
        else {
            if (lengthOfJourney.getValue().equals("short (3 hikes)")) {
                createJourney(nameOfJourney.getText(), partOfSlovakia.getValue(), difficultyOfHikes.getValue(), 3);
            } else {
                createJourney(nameOfJourney.getText(), partOfSlovakia.getValue(), difficultyOfHikes.getValue(), 5);
            }
        }

    }


    /**
     * In this method is provided whole logic for creation new journey for user.
     * @param nameOfJourneyString name of created journey
     * @param partOfSlovakiaString part of Slovakia, where the user can absolve the journey
     * @param difficultyOfHikesString difficulty of created hike routes
     * @param lengthOfJourneyString lenght of created journey
     **/
    public void createJourney(String nameOfJourneyString, String partOfSlovakiaString, String difficultyOfHikesString, int lengthOfJourneyString) {
        LoggedUser loggedUser = LoggedUser.getInstance();

        JSONArray a;
        JSONParser parser = new JSONParser();
        try {
              a = (JSONArray) parser.parse(new InputStreamReader(Objects.requireNonNull(getClass().getResource("/places/" + partOfSlovakiaString + difficultyOfHikesString + ".json")).openStream()));
        }
        catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }

        int pom = 0;
        ArrayList<Journey.Place> places = new ArrayList<Journey.Place>();
        Journey journey = new Journey(nameOfJourneyString, places, false);
        for (Object o : a) {
            JSONObject place = (JSONObject) o;
            String name = (String) place.get("name");
            String shortName = (String) place.get("shortName");
            String desc = (String) place.get("desc");
            String hikeRoute = (String) place.get("hikeRoute");
            String duration = (String) place.get("duration");
            String length = (String) place.get("length");
            String elevation = (String) place.get("elevation");
            String maxAltitude = (String) place.get("maxAltitude");
            String image = (String) place.get("image");

            ArrayList<String> notes = new ArrayList<String>();

            Journey.Place newPlace = new Journey.Place(name, shortName, desc, hikeRoute, duration, length, elevation, maxAltitude, image, notes);
            newPlace.setVisited(false);
            journey.getPlaces().add(newPlace);

            pom++;
            if(pom == lengthOfJourneyString) {
                break;
            }
        }

        loggedUser.getJourneys().add(journey);

        addJourneyIntoFile(journey, loggedUser);

        difficultyOfHikes.valueProperty().set(null);
        lengthOfJourney.valueProperty().set(null);
        partOfSlovakia.valueProperty().set(null);
        nameOfJourney.setText("");
        errorLabel.setText("Your journey was successfully created, now you can go back.");
    }

    /**
     * This method ensures that the newly created journey is written to the serialize file,
     * so that when the power is turned off and on again, the created journey is saved.
     * @param journey which journey will be written into serialize file
     * @param loggedUser
     **/
    public void addJourneyIntoFile(Journey journey, LoggedUser loggedUser) {
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
                users.get(i).getJourneys().add(journey);
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
