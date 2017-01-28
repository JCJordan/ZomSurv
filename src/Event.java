/**
 * Created by JCJordan on 28/01/2017.
 */
import java.util.*;
public class Event {
    private String location;
    private ArrayList<String> items;
    private String script;
    private ArrayList<String> actions;

    public Event(String script, ArrayList<String> items, ArrayList<String> actions){
        this.script = script;
        this.items = new ArrayList<String>(items);
        this.actions = new ArrayList<String>(actions);
        //infection probability
        //accessability
        //population density
        //parameters e.g. how close to zombies or type of tile.
    }
    public ArrayList<String> getActions(){
        return actions;
    }
    public ArrayList<String> getItems(){
        return items;
    }
    public String getScript(){
        return script;
    }
    public String getLocation(){
        return location;
    }
}
