import Graphics.GraphicsManager;
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
        em = new EventManager();
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
        pm.getPlayer().setPosition(new Location(gm.player.x, gm.player.y, null));
        System.out.println("Graphics Loaded");
        loadMap();
        gm.render();
        gm.player.x = 511 * 4;
        gm.player.y = 187 * 4;
        long lastTime = System.currentTimeMillis();
        long currentTime;
        double delta;
        while(gm.loaded){
            currentTime = System.currentTimeMillis();
            delta = currentTime - lastTime;
            if(delta > 10000){
                update();
                lastTime = System.currentTimeMillis();
            }
        }
        System.out.println("Goodbye! See you next time! :D");
        //gm.player.x = gm.player.x + 100;
        //gm.render();
    }

    public void update(){
        pm.updatePlayerLocation(new Location(gm.player.x, gm.player.y, "player"), map.getLocations() );
        pm.getPlayer().setPosition(new Location(gm.player.x, gm.player.y, "player"));
        System.out.println(pm.getPlayer().getPosition().toString());
        Event event = em.getEvent(pm.getPlayer());
        if(event != null) {
            processEvent(event);
        }
        //map.update()
    }

    public void processEvent(Event event){
        System.out.println(event.getScript());
        System.out.println(event.getActions());
        String currentAction = event.getActions().get(0);
        if(currentAction.equalsIgnoreCase("Search")) {
            pm.updateInventory(event.getItems());
            System.out.println("New Inventory: " + pm.getPlayer().getInventory());
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

    public void loadMap(){
        map = new Map();
        ArrayList<Location> locations = map.getLocations();
        for(Location location : locations){
            gm.addNewArea(location.getXPos() * 4, location.getYPos() * 4, "Building", 4);
        }
    }

    public void getNextEvent(){

    }

    public void loadGraphicClass(){

    }

}
