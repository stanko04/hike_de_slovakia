package sk.hike_de_slovakia.instances;

import java.util.ArrayList;

/**
 * This class represent the user which is actually logged in application.
 * Is a child of User class, because class LoggedUser has the same properties as user.
 * Different between User and LoggedUser is that LoggedUser is a Singleton model.
 **/
public class LoggedUser extends User{
	
	private static final LoggedUser instance = new LoggedUser(null, null, null, null, null);


	private LoggedUser(String firstName, String secondName, String username, String password, ArrayList<Journey> journeys) {
		super(firstName, secondName, username, password, journeys);
	}

	public static LoggedUser getInstance() {
		return instance;
	}
}
