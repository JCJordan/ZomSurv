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

    public void updatePlayerLocation(Location playerPosition, ArrayList<Location> locations){
        //TODO: Add tolerance
        for(Location location : locations){
            if(playerPosition.getXPos() == location.getXPos()){
                if(playerPosition.getYPos() == playerPosition.getYPos()){
                    player.setLocation(location);
                    return;
                }
            }
        }
        player.setLocation(new Location(playerPosition.getXPos(), playerPosition.getYPos(), "null"));

    }

}


