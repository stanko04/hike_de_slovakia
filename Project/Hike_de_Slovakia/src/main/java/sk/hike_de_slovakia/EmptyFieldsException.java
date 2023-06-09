package sk.hike_de_slovakia;

/**
 * This class represents my own custom exception.
 * Is used in class LoginController where when the user does not fill
 * all fields this custom exception will be caught.
 **/
public class EmptyFieldsException extends Exception {
    public EmptyFieldsException() {
        super("Field username or password cannot be empty");
    }
}
