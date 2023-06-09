package sk.hike_de_slovakia.controllers;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import sk.hike_de_slovakia.SceneController;
import sk.hike_de_slovakia.instances.LoggedUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import sk.hike_de_slovakia.instances.StringHolder;
import sk.hike_de_slovakia.instances.User;

/**
 *  This class is controller for fxml file which display main user's home page.
 *  This class implements Initializable and SceneController,
 *  because from the display that controls this controller the user can be redirected to the next screen
 **/
public class MainPageController implements Initializable, SceneController {


	//	FXML annotations
	@FXML
	private Label label_welcome;
	@FXML
	public ChoiceBox<String> createdJourneys;
	@FXML
	public ChoiceBox<String> completedJourneys;
	@FXML
	public Button displayCreatedJourneys;
	@FXML
	public Button displayCompletedJourney;
	@FXML
	public Button settingsButton;

	/**
	 * This method ensures that when the fxml screen controlled by this controller is displayed,
	 * all completed and created journeys will be added into choice boxes.
	 * And also, this method checks if the journey is completed or not.
	 **/
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		LoggedUser loggedUser = LoggedUser.getInstance();
		
		label_welcome.setAlignment(Pos.CENTER);
		label_welcome.setText("Welcome " + loggedUser.getFirstName());

		if(loggedUser.getJourneys().size() > 0) {

			for(int i = 0; i < loggedUser.getJourneys().size(); i++) {
				int pom = 0;
				for(int j = 0; j < loggedUser.getJourneys().get(i).getPlaces().size(); j++) {
					if(!loggedUser.getJourneys().get(i).getPlaces().get(j).isVisited()) {
						pom++;
						break;
					}
				}

				if(pom == 0) {
					loggedUser.getJourneys().get(i).setCompleted(true);

					setJourneyCompletedInFile(loggedUser, i);
				}

				if(!loggedUser.getJourneys().get(i).isCompleted()) {
					createdJourneys.getItems().add(loggedUser.getJourneys().get(i).getName());
				}
				else {
					completedJourneys.getItems().add(loggedUser.getJourneys().get(i).getName());
				}
			}
		}
	}

	/**
	 * This method ensures that when user clicks on the logout button,
	 * he will be logged out from his account.
	 **/
	public void clickOnLogout(ActionEvent event) throws IOException {
		switchScene(event, "/views/loginPage.fxml");
	}

	/**
	 * This method ensures that when user clicks on the create journey button,
	 * he will be redirected on page where he can create his journey.
	 **/
	public void clickOnCreateJourney(ActionEvent event) throws IOException {
		switchScene(event, "/views/createJourneyPage.fxml");
	}

	/**
	 * This method ensures that when user clicks on the display created journey button,
	 * he will be redirected into other fxml view which display details about created journey.
	 * In this method is set up string holder,
	 * thanks to which in the next fxml view application knows which journey must display
	 **/
	public void clickOnDisplayCreatedJourney(ActionEvent event) throws IOException {
		if(createdJourneys.getValue() != null) {
			StringHolder<String> stringHolder = StringHolder.getInstance();
			stringHolder.setObj(createdJourneys.getValue());
			switchScene(event, "/views/displayJourneyPage.fxml");
		}
	}

	/**
	 * This method ensures that when user clicks on the display completed journey button,
	 * he will be redirected into other fxml view which display details about completed journey.
	 * In this method is set up string holder,
	 * thanks to which in the next fxml view application knows which journey must display
	 **/
	public void clickOnDisplayCompletedJourney(ActionEvent event) throws IOException {
		if(completedJourneys.getValue() != null) {
			StringHolder<String> stringHolder = StringHolder.getInstance();
			stringHolder.setObj(completedJourneys.getValue());
			switchScene(event, "/views/displayCompletedJourney.fxml");
		}
	}

	/**
	 * This method ensures that when user clicks on the settings button,
	 * he will be redirected into other fxml view which display user's settings.
	 **/
	public void clickOnSettings(ActionEvent event) throws IOException {
		switchScene(event,"/views/userSettingsPage.fxml");
	}

	/**
	 * This method ensures that if the journey is completed,
	 * the status of that journey will also be changed in the serialize file,
	 * so that the data will be up-to-date even after it is turned off and on again.
	 */
	public void setJourneyCompletedInFile(LoggedUser loggedUser, int i) {
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
				users.get(j).getJourneys().get(i).setCompleted(true);
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
