
package sk.hike_de_slovakia.controllers;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import sk.hike_de_slovakia.EmptyFieldsException;
import sk.hike_de_slovakia.SceneController;
import sk.hike_de_slovakia.instances.LoggedUser;
import sk.hike_de_slovakia.instances.User;
import sk.hike_de_slovakia.instances.UserArray;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 *  This class is controller for fxml file which display login page which is shown after starting application.
 *  This class implements SceneController,
 *  because from the display that controls this controller the user can be redirected to the next screen
 **/
public class LoginController implements SceneController {

	// FXML annotations
	@FXML
	private TextField textfield_username;

	@FXML
	private PasswordField passwordfield_password;

	@FXML
	private Label fields_empty;

	@FXML
	private Label wrong_login;

	@FXML
	private Button button_login;

	@FXML
	private Button button_signup;


	/**
	 * This method check if the both textfield for write username and write password is filled.
	 * If one of them is not filled in, the method uses a custom exception for displaying the error message to user.
	 **/
	public boolean checkEmptyTextFields() {
		boolean isEmpty = false;
		try {
			if (textfield_username.getText().isEmpty() || passwordfield_password.getText().isEmpty()) {
				isEmpty = true;
				throw new EmptyFieldsException();
			}
		}
		catch (EmptyFieldsException e) {
			wrong_login.setText("");
			fields_empty.setText(e.getMessage());
		}
		return isEmpty;
	}


	/**
	 * This method ensures that the user will be redirected on the register page,
	 * when he clicks on the create account button.
	 */
	@SuppressWarnings("exports")
	@FXML
	public void clickOnSignUp(ActionEvent event) throws IOException {
		switchScene(event, "/views/signupPage.fxml");
	}


	/**
	 * This method ensures that when the user write correct login credentiels
	 * will be logged in and redirected into user home page.
	 **/
	@SuppressWarnings("exports")
	@FXML
	public void clickOnLogin(ActionEvent event) throws IOException {
		if(!checkEmptyTextFields()) {

			UserArray userArray = UserArray.getInstance();

//				check if in array of user is at least one user
			if(userArray.getSize() == 0) {
				fields_empty.setText("");
				wrong_login.setText("Wrong username or password");
				textfield_username.setText("");
				passwordfield_password.setText("");
				return;
			}


			int userId = 0;

//				check if username and password are correct or not
			boolean isAdmin = false;

			for (int i = 0;i < userArray.getSize();i++){
				if((textfield_username.getText().equals(userArray.getUser(i).getUsername())) &&
						(passwordfield_password.getText().equals(userArray.getUser(i).getPassword()))){
					if(userArray.getUser(i).getUsername().equals("adminadmin") && userArray.getUser(i).getPassword().equals("adminadmin")) {
						switchScene(event, "/views/adminHomePage.fxml");
						isAdmin = true;
					}
					userId = i;
//		            	log user if the username and password are correct
					LoggedUser loggedUser = LoggedUser.getInstance();

					loggedUser.setUsername(userArray.getUser(userId).getUsername());
					loggedUser.setFirstName(userArray.getUser(userId).getFirstName());
					loggedUser.setSecondName(userArray.getUser(userId).getSecondName());
					loggedUser.setJourneys(userArray.getUser(userId).getJourneys());
					loggedUser.setPassword(userArray.getUser(userId).getPassword());
					userArray.getUser(userId).setLastLogin(LocalDateTime.now());

					if(isAdmin) {
						break;
					}

					switchScene(event, "/views/mainPage.fxml");
					return;
				}
			}

			fields_empty.setText("");
			wrong_login.setText("Wrong username or password");
			textfield_username.setText("");
			passwordfield_password.setText("");
		}
	}

}