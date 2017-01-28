import java.util.ArrayList;

/**
 * Created by JCJordan on 28/01/2017.
 */
public class EventManager {

    private ArrayList<Event> events;

    public EventManager() {

    }

    public boolean eventAvailable(){
        return true;
    }

    public Event getEvent(){
        return null;
    }


    public void readEventList(){
        //events.add(new Event("data from file"))
        //TODO: Read events from file into events array
    }

}
