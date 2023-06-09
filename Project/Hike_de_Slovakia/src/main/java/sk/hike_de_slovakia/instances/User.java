package sk.hike_de_slovakia.instances;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
* This class represent the user account of application.
* The account identifier are: firstName, secondName, username, password, lastLogin and journeys
* Class is also serializable because user information are stored in serialize file.
* Class contains only its variables, constructors getters and setters.
**/
public class User implements Serializable {
	
//	set variable 
	private String firstName;
	private String secondName;
	private String username;
	private String password;
	private LocalDateTime lastLogin;
	ArrayList<Journey> journeys;
	
//	constructors 
	public User(String firstName, String secondName, String username, String password, ArrayList<Journey> journeys) {
		this.firstName = firstName;
		this.secondName = secondName;
		this.username = username;
		this.password = password;
		this.journeys = journeys;
	}

	public String getFirstName() { return firstName; }

	public void setFirstName(String firstName) { this.firstName = firstName; }

	public String getSecondName() { return secondName; }

	public void setSecondName(String secondName) { this.secondName = secondName; }

	public String getUsername() { return username; }

	public void setUsername(String username) { this.username = username;}

	public String getPassword() { return password; }

	public void setPassword(String password) { this.password = password;}

	public ArrayList<Journey> getJourneys() {
		return journeys;
	}

	public void setJourneys(ArrayList<Journey> journeys) {
		this.journeys = journeys;
	}

	public LocalDateTime getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(LocalDateTime lastLogin) {
		this.lastLogin = lastLogin;
	}
}
