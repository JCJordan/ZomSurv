import Graphics.GraphicsManager;
import Graphics.InputHandler;
import Graphics.level.tiles.Tile;

import java.util.ArrayList;

/**
 * Created by JCJordan on 28/01/2017.
 */
public class MainManager {

    private GraphicsManager gm;
    private EventManager em;
    private PlayerManager pm;
    private Map map;

    public MainManager(){
        pm = new PlayerManager(new Player());
        gm = new GraphicsManager();
        runGame();
    }

    public static void main(String args[]){
        new MainManager();
    }

    public void runGame(){
        gm.start();
        System.out.println("GM Started");
        while(!gm.loaded){ System.out.print(""); }
        pm.getPlayer().set_pos(gm.player.x, gm.player.y);
        System.out.println("Graphics Loaded");
        loadMap();
        map.addZombies(0,0,10000);
        em = new EventManager(map);
        gm.render();
        gm.player.x = 500*8;
        gm.player.y = 500*8;
        long lastTime = System.currentTimeMillis();
        long currentTime;
        double delta;
        while(gm.loaded){
            currentTime = System.currentTimeMillis();
            delta = currentTime - lastTime;
            if(delta > 1000){
                update();
                lastTime = System.currentTimeMillis();
            }
        }
        System.out.println("Goodbye! See you next time! :D");
        //gm.player.x = gm.player.x + 100;
        //gm.render();
    }

    public void update() {
        pm.update_location(gm.player.x / 8, gm.player.y / 8, map.getLocations(), em);
        map.incrementTime(60);

    }


    public void loadMap(){
        map = new Map();
        ArrayList<Location> locations = map.getLocations();
        //for(Location location : locations){
         //   gm.addNewArea(location.getXPos() * 4, location.getYPos() * 4, "Building", 4);
        //}
    }


}
