/**
 * Created by JCJordan on 28/01/2017.
 */
import java.util.*;
public class Event {
    private ArrayList<String> items;
    private String script = "";
    private ArrayList<String> actions;

    public Event(ArrayList<String> params){
        /*if(params.contains() == "Hospital"){
            script += "You are in a hospital.";
            items.add("medicine");
        }*/

        /*if(params.contains() == "zombies"){
            script += "You can see zombies"
        }*/

        /*if(params.contains() == "zombies"){
            script += "You see zombies near you!"
        }*/

        /*if(params.contains() == "zombies"){
            script
        }*/
        /*if(params.contains() == "zombies"){

        }*/
        /*if(params.contains() == "zombies"){

        }*/
        private ArrayList<String> allActions = new ArrayList<String>();
        allActions.add("Search");
        allActions.add("Fight");
        allActions.add("Run");
        allActions.add("Sneak");
        allActions.add("Pray");
        allActions.add("kys");
        
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
}
