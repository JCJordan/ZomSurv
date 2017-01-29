import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by JCJordan on 28/01/2017.
 */
public class Player {

    int x_pos;
    int y_pos;
    String loc_type;
    private ArrayList<String> inventory;

    public Player(){
        inventory = new ArrayList<>();
    }

    public void set_pos(int x,int y){

        x_pos = x;
        y_pos = y;

    }

    public int get_x_pos(){

        return x_pos;

    }

    public int get_y_pos(){

        return y_pos;

    }

    public void set_loc_type(String type){

        loc_type = type;

    }

    public String get_loc_type(){

        return loc_type;

    }

    public ArrayList<String> getInventory(){
        return inventory;
    }

    public void setInventory(ArrayList<String> newinv){
        this.inventory = newinv;
    }
}
