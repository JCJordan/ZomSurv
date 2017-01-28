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
        gm.start();
    }

    public static void main(String args[]){
        new MainManager();
    }

    public void updateMap (){
        //HERRO!!
        //Test
        //ergregregregrgr

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
