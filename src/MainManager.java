import Graphics.GraphicsManager;

/**
 * Created by JCJordan on 28/01/2017.
 */
public class MainManager {

    private GraphicsManager gm = new GraphicsManager();
    private EventManager em = new EventManager();
    private PlayerManager pm;

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

    }

    public void updateMap (){
        //HERRO!!
        //Test
        //ergregregregrgr

    }

    public void getAction(){

    }

    public void updateInventory() {

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
