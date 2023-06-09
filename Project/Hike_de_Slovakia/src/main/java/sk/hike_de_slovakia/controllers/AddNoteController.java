package sk.hike_de_slovakia.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.text.TextAlignment;
import sk.hike_de_slovakia.GoBack;
import sk.hike_de_slovakia.JourneyIndex;
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
 * This class is controller for fxml file which display the addition notes to the place of journey.
 * This class extends from class GoBack, because uses its method clickOnBack.
 * Also implements from Initializable and JourneyIndex, because need to get the index of journey with which we need to work.
 **/
public class AddNoteController extends GoBack implements Initializable, JourneyIndex {

    public Button addNote;
    public TextArea textArea;
    public Label note1;
    public Label note2;
    public Label note3;

    /**
     * Class need this method because on the starts it needs to be initialized alignments of notes texts.
     **/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        textArea.setWrapText(true);

        note1.setTextAlignment(TextAlignment.JUSTIFY);
        note1.setAlignment(Pos.TOP_LEFT);
        note1.setWrapText(true);

        note2.setTextAlignment(TextAlignment.JUSTIFY);
        note2.setAlignment(Pos.TOP_LEFT);
        note2.setWrapText(true);

        note3.setTextAlignment(TextAlignment.JUSTIFY);
        note3.setAlignment(Pos.TOP_LEFT);
        note3.setWrapText(true);

        getNotes();
    }

    /**
    * This method ensures that the note which write user into text field will be added into user's journey and its place.
     **/
    public void addNote() {
        if (!textArea.getText().equals("")) {
            LoggedUser loggedUser = LoggedUser.getInstance();
            int index = getIndexOfJourney();
            PlaceNameHolder<String> placeString = PlaceNameHolder.getInstance();

            for (int i = 0; i < loggedUser.getJourneys().get(index).getPlaces().size(); i++) {
                if (loggedUser.getJourneys().get(index).getPlaces().get(i).getName().equals(placeString.getObj())) {
                    if (loggedUser.getJourneys().get(index).getPlaces().get(i).getNotes().size() < 3) {
                        loggedUser.getJourneys().get(index).getPlaces().get(i).addNote(textArea.getText());
                        getNotes();
                        addNoteIntoFile(loggedUser, textArea.getText(), index, i);
                        textArea.setText("");
                    }
                }
            }
        }
    }

    /**
    * This method ensures that all already created notes for the clicked location are displayed.
     **/
    public void getNotes() {
        PlaceNameHolder<String> placeString = PlaceNameHolder.getInstance();

        LoggedUser loggedUser = LoggedUser.getInstance();
        int index = getIndexOfJourney();

        for(int i = 0; i < loggedUser.getJourneys().get(index).getPlaces().size(); i++) {
            if(loggedUser.getJourneys().get(index).getPlaces().get(i).getName().equals(placeString.getObj())) {
                for(int j = 0; j < loggedUser.getJourneys().get(index).getPlaces().get(i).getNotes().size(); j++) {
                    if(j == 0) {
                        note1.setText(loggedUser.getJourneys().get(index).getPlaces().get(i).getNotes().get(j));
                    }
                    else if(j == 1) {
                        note2.setText(loggedUser.getJourneys().get(index).getPlaces().get(i).getNotes().get(j));
                    }
                    else if(j == 2) {
                        note3.setText(loggedUser.getJourneys().get(index).getPlaces().get(i).getNotes().get(j));
                    }
                }
            }
        }
    }

    /**
     * This method ensures that if the user clicks on the back button,
     * he will be redirected to the previous screen.
     **/
    @Override
    public void clickOnBack(ActionEvent event) throws IOException {
        switchScene(event, "/views/displayJourneyPage.fxml");
    }

    /**
    * This method ensures that the newly created note is written to the serialize file,
     * so that when the power is turned off and on again, the created note is saved.
     **/
    public void addNoteIntoFile(LoggedUser loggedUser, String note, int index, int i) {
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
                users.get(j).getJourneys().get(index).getPlaces().get(i).addNote(note);
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





}
