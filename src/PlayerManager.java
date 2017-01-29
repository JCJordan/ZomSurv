import Graphics.InputHandler;
import Graphics.gfx.Colours;
import Graphics.gfx.Font;
import Graphics.gfx.Screen;
import jdk.internal.util.xml.impl.Input;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by JCJordan on 28/01/2017.
 */
public class PlayerManager {

    private Player player;

    public PlayerManager(Player player){
        this.player = player;
    }

    public Player getPlayer(){
        return this.player;
    }

    public void updateInventory(ArrayList<String> items) {
        ArrayList<String> newInv = player.getInventory();
        newInv.addAll(items);
        player.setInventory(newInv);
    }
    public void processEvent(Event event, Screen screen, InputHandler input){
        System.out.println(event.getScript());
        Font.render("Correct Answer!", screen, (("Correct Answer!".length() -1) / 2*8), (4*8), Colours.get(-1, -1, -1, 555), 1);
        System.out.println(event.getActions());
        //String currentAction = event.getActions().get(0);
        String currentAction = "";
        if(input.a.isPressed()){
            currentAction = event.getActions().get(0);
        }
        if(currentAction.equalsIgnoreCase("Search")) {
            updateInventory(event.getItems());
            System.out.println("New Inventory: " + player.getInventory());
        }
        if(currentAction.equalsIgnoreCase("Fight")){
            //DoShit
            System.out.println("You fought");
        }
        if(currentAction.equalsIgnoreCase("Run")){
            if(runProbability() < Math.random()){
                System.out.println("You got absolutely Raped");
            }
        }
        if(currentAction.equalsIgnoreCase("Pray")){
            if(Math.random() > 1/100){
                System.out.println("Get fucked son");
            }
        }
        if(currentAction.equalsIgnoreCase("Explore")){
            //GetEventByName?
            System.out.println("You Explored");
        }
    }

    public double runProbability(){
        return 0.5;
    }

    public void update_location(int x,int y, ArrayList<Location> locations,EventManager em){
        if(player.get_x_pos() == x && player.get_y_pos() == y){


        }else{

            Event event = em.getEvent(player);
            if(event != null) {
                //processEvent(event);
            }

            for(Location location : locations){
                if(x == location.getXPos()){
                    if(y == location.getYPos()){
                        player.set_pos(x,y);
                        player.set_loc_type(location.getType());
                        return;
                    }
                }
            }
            player.set_pos(x,y);
            player.set_loc_type("null");

        }


    }

}


