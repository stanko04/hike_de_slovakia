package sk.hike_de_slovakia.instances;

import java.util.LinkedList;

/**
 * This class represent array of all the users in applications.
 * All the users accounts are stored in LinedList Array.
 * This class is also model Singleton, because the same instance array of the users are used in many classes.
 **/
public class UserArray {
	private User user;
	
    private LinkedList<User> Array = new LinkedList<User>();

    private int size = 0;
    
    private static UserArray instance = new UserArray();

    
//    constructors
    private UserArray() {}

//    getters and setters 
    
    public User getUser(int i) { return Array.get(i);}

    public int getSize() { return size; }

    public void setSize(int size) {
        this.size = size;
    }
    
    public static UserArray getInstance() { return instance; }
    
//    methods 
    public void addUser(User user) {
        Array.add(user);
        size++;
    }

    public LinkedList<User> getArray() {
        return Array;
    }

    public void setArray(LinkedList<User> array) {
        Array = array;
    }
}
