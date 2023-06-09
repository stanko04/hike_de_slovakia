package sk.hike_de_slovakia.controllers;

import java.io.*;
import java.util.ArrayList;

import sk.hike_de_slovakia.GoBack;
import sk.hike_de_slovakia.SceneController;
import sk.hike_de_slovakia.instances.Journey;
import sk.hike_de_slovakia.instances.User;
import sk.hike_de_slovakia.instances.UserArray;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 *  This class is controller for fxml file which display register page where the user can create his account.
 *  And also is controller for fxml file which display add user in admin account.
 *  This class implements SceneController,
 *  because from the display that controls this controller the user can be redirected to the next screen
 **/
public class SignupController implements SceneController{
	
	// FXML annotations
	
	@FXML 
	private TextField textfield_firstname;
	
	@FXML
	private TextField textfield_lastname;
	
	@FXML
	private TextField textfield_username;
	
	@FXML 
	private PasswordField passwordfield_password;
	
	@FXML
	private Button button_signup;
	
	@FXML
	private Button button_back;
	
	@FXML
	private Label fields_empty;

	/**
	 * This method is only used within the admin account,
	 * and ensures that if the admin user clicks the back button,
	 * he will be redirected to the previous screen.
	 */
	@SuppressWarnings("exports")
	public void goBackAdmin(ActionEvent event) throws IOException {
		switchScene(event, "/views/adminHomePage.fxml");
	}

	/**
	 * This method is only used for the registration screen.
	 * If the user clicks the back button he will be redirected back to the login screen.
	 */
	public void goBackNormal(ActionEvent event) throws IOException {
		switchScene(event, "/views/loginPage.fxml");
	}

	/**
	 * * This method check if the both textfield for write username and write password is filled.
	 * If one of them is not filled the error is shown to the user.
	 */
	public boolean checkEmptyFields(){
		if(textfield_firstname.getText().isEmpty() || textfield_lastname.getText().isEmpty() 
			|| textfield_username.getText().isEmpty() || passwordfield_password.getText().isEmpty()) {
			fields_empty.setText("All fields are required");
			return true;
		}
		return false;
	}

	/**
	 * This method provides the entire logic of user registration.
	 * As well as writing the newly created user to the serialize file.
	 */
	@SuppressWarnings("exports")
	public void clickOnSignUp(ActionEvent event) throws IOException {
		if(!checkEmptyFields()) {
			UserArray userArray = UserArray.getInstance();

//			ArrayList<User> users = new ArrayList<>();
			
			String firstName = textfield_firstname.getText();
			String lastName = textfield_lastname.getText();
			String username = textfield_username.getText();
			String password = passwordfield_password.getText();
			
			for (int i = 0;i < userArray.getSize();i++) {
				if(username.equals(userArray.getUser(i).getUsername())) {
					fields_empty.setText("Username is already used");
					textfield_username.setText("");
					return;
				}
			}

			ArrayList<Journey> journeys = new ArrayList<Journey>();
			User user = new User(firstName, lastName, username, password, journeys);

			userArray.addUser(user);


			ArrayList<User> users = null;

			try (FileInputStream fis = new FileInputStream("userData");
				 ObjectInputStream ois = new ObjectInputStream(fis);) {

				users = (ArrayList) ois.readObject();

			} catch (IOException ioe) {
//				ioe.printStackTrace();
			} catch (ClassNotFoundException c) {
				System.out.println("Class not found");
//				c.printStackTrace();
			}

			if(users == null ) {
				users = new ArrayList<>();
			}

			users.add(user);

			try (FileOutputStream fos = new FileOutputStream("userData");
				 ObjectOutputStream oos = new ObjectOutputStream(fos);) {

				oos.writeObject(users);

			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}

			switchScene(event, "/views/loginPage.fxml");
		}
	}

	/**
	 * This method provides the entire logic of add the user in admin account.
	 * As well as writing the newly created user to the serialize file.
	 */
	public void addUserAsAdmin(ActionEvent event) throws IOException {
		if(!checkEmptyFields()) {
			UserArray userArray = UserArray.getInstance();

//			ArrayList<User> users = new ArrayList<>();

			String firstName = textfield_firstname.getText();
			String lastName = textfield_lastname.getText();
			String username = textfield_username.getText();
			String password = passwordfield_password.getText();

			for (int i = 0;i < userArray.getSize();i++) {
				if(username.equals(userArray.getUser(i).getUsername())) {
					fields_empty.setText("Username is already used");
					textfield_username.setText("");
					return;
				}
			}

			ArrayList<Journey> journeys = new ArrayList<Journey>();
			User user = new User(firstName, lastName, username, password, journeys);

			userArray.addUser(user);


			ArrayList<User> users = null;

			try (FileInputStream fis = new FileInputStream("userData");
				 ObjectInputStream ois = new ObjectInputStream(fis);) {

				users = (ArrayList) ois.readObject();

			} catch (IOException ioe) {
//				ioe.printStackTrace();
			} catch (ClassNotFoundException c) {
				System.out.println("Class not found");
//				c.printStackTrace();
			}

			if(users == null ) {
				users = new ArrayList<>();
			}

			users.add(user);

			try (FileOutputStream fos = new FileOutputStream("userData");
				 ObjectOutputStream oos = new ObjectOutputStream(fos);) {

				oos.writeObject(users);

			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}

			switchScene(event, "/views/adminHomePage.fxml");
		}
	}


}
