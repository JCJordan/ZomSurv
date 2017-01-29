import Graphics.GraphicsManager;

import java.util.ArrayList;

/**
 * Created by JCJordan on 28/01/2017.
 */
public class MainManager {

    private GraphicsManager gm = new GraphicsManager();
    private EventManager em = new EventManager();
    private PlayerManager pm;
    private Map map = new Map();

    public MainManager(){
        //em.readEventList();
        runGame();
    }

    public static void main(String args[]){
        new MainManager();
    }

    public void runGame(){
        gm.start();
        while(!gm.loaded){}
        System.out.println("Graphics Loaded");
        loadMap();
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

    public void update(){
        pm.updatePlayerLocation(new Location(gm.player.x, gm.player.y, -1), null ); //map.getLocations();
        if(em.eventAvailable(pm.getPlayer())){
            Event event = em.getEvent(pm.getPlayer());
            if(event != null) {
                processEvent(event);
            }
        }
        //map.update();
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
            if(runProbablity() < Math.random()){
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

    public double runProbablity(){
        return 0.5;
    }

    public void updateMap (){
        //HERRO!!
        //Test
        //ergregregregrgr

    }

    public void getAction(){

    }

    public void loadMap(){

    }

    public void getNextScreen(){

    }

    public void getNextEvent(){

    }

    public void loadGraphicClass(){

    }

}
