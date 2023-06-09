package sk.hike_de_slovakia.instances;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represent journey which own the user class.
 * Journey has nested class Place, because class Place is a direct contemporaneity of Journey.
 * Classes Journey and Place contains only its variables, constructors, getters and setters.
 * In the application Journey is created by the user.
 **/
public class Journey implements Serializable {
//    variables
    private String name;
    private ArrayList<Place> places;
    private boolean isCompleted;

//    constructor
    public Journey(String name, ArrayList<Place> places, boolean isCompleted) {
        this.name = name;
        this.places = places;
        this.isCompleted = isCompleted;
    }


//    getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Place> getPlaces() {
        return places;
    }

    public void setPlaces(ArrayList<Place> places) {
        this.places = places;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    /**
     * This class represent place that is a direct part of the Journey class.
     */
    public static class Place implements Serializable {
        //    variables
        private String name;
        private String shortName;
        private String desc;
        private String hikeRoute;
        private String duration;
        private String length;
        private String elevation;
        private String maxAltitude;
        private String highProfile;
        private String image;
        private boolean isVisited;
        private ArrayList<String> notes;

        //    constructor
        public Place() {}

        public Place(String name, String shortName, String desc, String hikeRoute, String duration, String length, String elevation, String maxAltitude, String image, ArrayList<String> notes) {
            this.name = name;
            this.shortName = shortName;
            this.desc = desc;
            this.hikeRoute = hikeRoute;
            this.duration = duration;
            this.length = length;
            this.elevation = elevation;
            this.maxAltitude = maxAltitude;
            this.image = image;
            this.notes = notes;
        }

        //    getters and setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getHikeRoute() {
            return hikeRoute;
        }

        public void setHikeRoute(String hikeRoute) {
            this.hikeRoute = hikeRoute;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getLength() {
            return length;
        }

        public void setLength(String length) {
            this.length = length;
        }

        public String getElevation() {
            return elevation;
        }

        public void setElevation(String elevation) {
            this.elevation = elevation;
        }

        public String getMaxAltitude() {
            return maxAltitude;
        }

        public void setMaxAltitude(String maxAltitude) {
            this.maxAltitude = maxAltitude;
        }

        public String getHighProfile() {
            return highProfile;
        }

        public void setHighProfile(String highProfile) {
            this.highProfile = highProfile;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getShortName() {
            return shortName;
        }

        public void setShortName(String shortName) {
            this.shortName = shortName;
        }

        public boolean isVisited() {
            return isVisited;
        }

        public void setVisited(boolean visited) {
            isVisited = visited;
        }


        public ArrayList<String> getNotes() {
            return notes;
        }

        public void setNotes(ArrayList<String> notes) {
            this.notes = notes;
        }

        public void addNote(String string) {
            this.notes.add(string);
        }
    }
}
