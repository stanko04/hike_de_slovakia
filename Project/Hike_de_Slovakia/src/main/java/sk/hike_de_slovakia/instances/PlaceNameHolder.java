package sk.hike_de_slovakia.instances;

/**
 * This class represent generic class. Where parameter of this class is object T, which is String type.
 * This class is used for storing the name of place for the next fxml view.
 * When the user clicks through to the next screen, so that the application
 * knows what place to associate the entered information to.
 * This class uses the Singleton model.
 * @param <T>
 */
public class PlaceNameHolder<T> {
    private T obj;

    private static final PlaceNameHolder<String> instance = new PlaceNameHolder<>();

    public static PlaceNameHolder<String> getInstance() {
        return instance;
    }

    public T getObj() {
        return this.obj;
    }

    public void setObj(T t) {
        this.obj = t;
    }
}
