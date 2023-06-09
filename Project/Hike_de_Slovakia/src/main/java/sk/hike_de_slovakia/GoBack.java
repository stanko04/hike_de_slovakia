package sk.hike_de_slovakia;

import javafx.event.ActionEvent;

import java.io.IOException;

/**
 * This abstract class is used for polymorphism, because its method clickOnBack is used in many other classes with different results.
 **/
public abstract class GoBack implements SceneController {

    /**
     * This method is used in many others classes, but with the other body and result, and therefore ensures polymorphism.
     * @param event
     * @throws IOException
     */
    public abstract void clickOnBack(ActionEvent event) throws IOException;
}
