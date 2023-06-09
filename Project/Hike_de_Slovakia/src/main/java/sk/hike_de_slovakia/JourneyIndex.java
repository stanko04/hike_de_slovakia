package sk.hike_de_slovakia;

import sk.hike_de_slovakia.instances.LoggedUser;
import sk.hike_de_slovakia.instances.StringHolder;

/**
 * This interface is used for default method implementation, where its method is used in many others classes.
 **/
public interface JourneyIndex {

    /**
     * This method we used for get the index of journey which we need to work and change.
     * @return this method returns the index of the route we currently need to work with
     */
    public default int getIndexOfJourney() {
        StringHolder<String> stringHolder = StringHolder.getInstance();
        LoggedUser loggedUser = LoggedUser.getInstance();
        int index = 0;

        for(int i=0; i < loggedUser.getJourneys().size(); i++) {
            if(loggedUser.getJourneys().get(i).getName().equals(stringHolder.getObj())) {
                index = i;
                break;
            }
        }
        return index;
    }
}
