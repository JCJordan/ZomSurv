/**
 * Created by JCJordan on 28/01/2017.
 */
import java.util.*;
public class Event {
    private String location;
    private ArrayList<String> items;
    private String script;
    private ArrayList<String> actions;
    private float rarity;
    private int type;

    public Event(String location, String script, ArrayList<String> items, ArrayList<String> actions){
        this.location = location;
        this.script = script;
        this.items = new ArrayList<String>(items);
        this.actions = new ArrayList<String>(actions);
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
