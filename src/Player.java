import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by JCJordan on 28/01/2017.
 */
public class Player {
    private Location location;
    private Location position;
    private ArrayList<String> inventory;

    public Player(){
        inventory = new ArrayList<>();
        location = null;
    }

    public Location getLocation(){
        return location;
    }
    public void setLocation(Location location){
        this.location = location;
    }

    public Location getPosition(){
        return position;
    }

    public void setPosition(Location position){
        this.position = position;
    }

    public ArrayList<String> getInventory(){
        return inventory;
    }

    public void setInventory(ArrayList<String> newinv){
        this.inventory = newinv;
    }
}
