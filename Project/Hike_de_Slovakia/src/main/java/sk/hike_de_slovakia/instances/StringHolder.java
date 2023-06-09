package sk.hike_de_slovakia.instances;
/**
 * This class represent generic class. Where parameter of this class is object T, which is String type.
 * This class is used for storing the value of String for the next fxml view.
 * When the user clicks through to the next screen, so that the application
 * knows which journey should be displayed thanks to this class.
 * This class uses the Singleton model.
 * @param <T>
 */
public class StringHolder<T> {
    private T obj;

    private static final StringHolder<String> instance = new StringHolder<>();

    public static StringHolder<String> getInstance() {
        return instance;
    }

    public T getObj() {
        return this.obj;
    }

    public void setObj(T t) {
        this.obj = t;
    }
}
