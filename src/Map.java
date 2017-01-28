import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.io.File;
import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;


/**
 * Created by CALLUM on 28/01/2017.
 */


public class Map{

    private int mapWidth;
    private int mapHeight;
    public int[][] populationDensity;
    public int[][] infectionDensity;
    public float[][] accessibility;//all less than 1
    public ArrayList<ArrayList<Location>> locations = new ArrayList<ArrayList<Location>>(4);;//0:hospital,1:graveyard,2:church,3:Supermarket
    float zomSpeed = 0.5f;//less than 1
    float zomTrickle = 0.01f;
    float popTrickle = 0.02f;
    float maxSpreadSpeed = 0.2f;//less than 1
    float popSpeed = 0.6f;//less than 1




    public Map(){

        loadAccessibility();

        BufferedImage img = new BufferedImage(mapWidth, mapHeight, BufferedImage.TYPE_INT_ARGB);
        File f = null;
        int a;
        int r;
        int g;
        int b;

        for(int i = 0;i < mapWidth;i++) {

            for (int j = 0; j < mapHeight; j++) {

                if(infectionDensity[i][j] > 0){

                    a = 256;
                    r = 256;
                    g = 256;
                    b = 256;
                    int p = (a<<24) | (r<<16) | (g<<8) | b;
                    img.setRGB(i, j, p);
                }else{

                    a = 0;
                    r = 0;
                    g = 0;
                    b = 0;
                    int p = (a<<24) | (r<<16) | (g<<8) | b;
                    img.setRGB(i, j, p);

                }
            }
        }

        try{
            f = new File("src/Graphics/resources/Output.png");
            ImageIO.write(img, "png", f);
        }catch(IOException e){
            System.out.println("Error: " + e);
        }

        for(int l = 0; l < 10;l++){

            incrementTimeHr();
        }





        img = new BufferedImage(mapWidth, mapHeight, BufferedImage.TYPE_INT_ARGB);
        f = null;

        for(int i = 0;i < mapWidth;i++) {

            for (int j = 0; j < mapHeight; j++) {

                if(infectionDensity[i][j] > 0){

                    a = 50;
                    r = 256;
                    g = 256;
                    b = 256;
                    int p = (a<<24) | (r<<16) | (g<<8) | b;
                    img.setRGB(i, j, p);
                }else{

                    a = 0;
                    r = 0;
                    g = 0;
                    b = 0;
                    int p = (a<<24) | (r<<16) | (g<<8) | b;
                    img.setRGB(i, j, p);

                }
            }
        }

        try{
            f = new File("src/Graphics/resources/Output2.png");
            ImageIO.write(img, "png", f);
        }catch(IOException e){
            System.out.println("Error: " + e);
        }


    }

    private void loadAccessibility(){

        BufferedImage image;
        File sourceimage = new File("src/Graphics/resources/accessibility.png");
        try {
            image = ImageIO.read(sourceimage);
            mapWidth = image.getWidth();
            mapHeight = image.getHeight();
            populationDensity = new int[mapWidth][mapHeight];
            infectionDensity = new int[mapWidth][mapHeight];
            accessibility = new float[mapWidth][mapHeight];

            for(int i = 0;i < mapWidth;i++) {

                for (int j = 0; j < mapHeight; j++) {

                    accessibility[i][j] =  (image.getRGB(i,j)& 0xFF)/255;
                    populationDensity[i][j] = (int)(200000*accessibility[i][j]);

                }

            }
            boolean test = false;
            for(int i = 0;i < mapWidth;i++) {

                for (int j = 0; j < mapHeight; j++) {

                    if(accessibility[i][j] == 1.0f && test == false){

                        infectionDensity[i][j] = 10000;
                        test = true;


                    }


                }

            }

        } catch(IOException e) {
            e.printStackTrace();
        }


    }


    private float sigmoid(float x) {
        return (float)(1/( 1 + Math.pow(Math.E,(-1*x))));
    }

    //these deal with population spread
    private void incrementTimePopMin(int[][] tPopulationDensity , int[][] tInfectionDensity) {

        for(int i = 0;i < mapWidth;i++) {

            for (int j = 0; j < mapHeight; j++) {


                int[] xValues = {1,1,0,-1,-1,-1,0,1};
                int[] yValues = {0,-1,-1,-1,0,1,1,1};

                for(int k = 0;k < xValues.length;k++){

                    int change;

                    if(i + xValues[k] < mapWidth && i + xValues[k] > -1 && j + yValues[k] < mapHeight && j + yValues[k] > -1 && infectionDensity[i + xValues[k]][j + yValues[k]] > infectionDensity[i][j]){

                        float g = (infectionDensity[i + xValues[k]][j + yValues[k]] - infectionDensity[i][j])/infectionDensity[i + xValues[k]][j + yValues[k]];
                        change = (int)((g + popTrickle)*populationDensity[i + xValues[k]][j + yValues[k]]*popSpeed*accessibility[i][j]);
                        if(change/60 > tPopulationDensity[i + xValues[k]][j + yValues[k]]){

                            change = tPopulationDensity[i + xValues[k]][j + yValues[k]];
                        }

                        tPopulationDensity[i + xValues[k]][j + yValues[k]] -= change/60;
                        tPopulationDensity[i][j] += change/60;


                    }else if(i + xValues[k] < mapWidth && i + xValues[k] > -1 && j + yValues[k] < mapHeight && j + yValues[k] > -1){

                        change = (int)(popTrickle*populationDensity[i + xValues[k]][j + yValues[k]]*popSpeed*accessibility[i][j]);

                        if(change/60 > tPopulationDensity[i + xValues[k]][j + yValues[k]]){

                            change = tPopulationDensity[i + xValues[k]][j + yValues[k]];
                        }

                        tPopulationDensity[i + xValues[k]][j + yValues[k]] -= change/60;
                        tPopulationDensity[i][j] += change/60;
                    }


                }

            }
        }


    }
    private void incrementTimePopHr(int[][] tPopulationDensity , int[][] tInfectionDensity) {

        for(int i = 0;i < mapWidth;i++) {

            for (int j = 0; j < mapHeight; j++) {


                int[] xValues = {1,1,0,-1,-1,-1,0,1};
                int[] yValues = {0,-1,-1,-1,0,1,1,1};

                for(int k = 0;k < xValues.length;k++){

                    int change;

                    if(i + xValues[k] < mapWidth && i + xValues[k] > -1 && j + yValues[k] < mapHeight && j + yValues[k] > -1 && infectionDensity[i + xValues[k]][j + yValues[k]] > infectionDensity[i][j]){

                        float g = (infectionDensity[i + xValues[k]][j + yValues[k]] - infectionDensity[i][j])/infectionDensity[i + xValues[k]][j + yValues[k]];
                        change = (int)((g + popTrickle)*populationDensity[i + xValues[k]][j + yValues[k]]*popSpeed*accessibility[i][j]);
                        if(change/60 > tPopulationDensity[i + xValues[k]][j + yValues[k]]){

                            change = tPopulationDensity[i + xValues[k]][j + yValues[k]];
                        }

                        tPopulationDensity[i + xValues[k]][j + yValues[k]] -= change;
                        tPopulationDensity[i][j] += change;


                    }else if(i + xValues[k] < mapWidth && i + xValues[k] > -1 && j + yValues[k] < mapHeight && j + yValues[k] > -1){

                        change = (int)(popTrickle*populationDensity[i + xValues[k]][j + yValues[k]]*popSpeed*accessibility[i][j]);
                        if(change/60 > tPopulationDensity[i + xValues[k]][j + yValues[k]]){

                            change = tPopulationDensity[i + xValues[k]][j + yValues[k]];
                        }

                        tPopulationDensity[i + xValues[k]][j + yValues[k]] -= change;
                        tPopulationDensity[i][j] += change;
                    }


                }

            }
        }

    }

    //these deal with zombie spread
    private void incrementTimeInfMin(int[][] tPopulationDensity , int[][] tInfectionDensity) {

        for(int i = 0;i < mapWidth;i++){

            for(int j = 0;j < mapHeight;j++){

                int[] xValues = {1,1,0,-1,-1,-1,0,1};
                int[] yValues = {0,-1,-1,-1,0,1,1,1};

                for(int k = 0;k < xValues.length;k++){

                    int change = 0;

                    if(i + xValues[k] < mapWidth && i + xValues[k] > -1 && j + yValues[k] < mapHeight && j + yValues[k] > -1 && populationDensity[i + xValues[k]][j + yValues[k]] > populationDensity[i][j]){

                        float g = (populationDensity[i + xValues[k]][j + yValues[k]] - populationDensity[i][j])/populationDensity[i + xValues[k]][j + yValues[k]];
                        change = (int)((g + zomTrickle)*infectionDensity[i + xValues[k]][j + yValues[k]]*zomSpeed*accessibility[i][j]);

                        if(change/60 > tInfectionDensity[i + xValues[k]][j + yValues[k]]){

                            change = tInfectionDensity[i + xValues[k]][j + yValues[k]];
                        }

                        tInfectionDensity[i + xValues[k]][j + yValues[k]] -= change/60;
                        tInfectionDensity[i][j] += change/60;


                    }else if(i + xValues[k] < mapWidth && i + xValues[k] > -1 && j + yValues[k] < mapHeight && j + yValues[k] > -1){

                        change = (int)(zomTrickle*infectionDensity[i + xValues[k]][j + yValues[k]]*zomSpeed*accessibility[i][j]);
                        if(change/60 > tInfectionDensity[i + xValues[k]][j + yValues[k]]){

                            change = tInfectionDensity[i + xValues[k]][j + yValues[k]];
                        }

                        tInfectionDensity[i + xValues[k]][j + yValues[k]] -= change/60;
                        tInfectionDensity[i][j] += change/60;
                    }


                }

                float inf;

                if(Math.abs(populationDensity[i][j]-infectionDensity[i][j]) == 0){

                    inf = 1;

                }else{

                    inf = (float)(1/Math.abs(populationDensity[i][j]-infectionDensity[i][j]));
                    inf = sigmoid(inf);
                }

                int h =  (int)(populationDensity[i][j]*inf*maxSpreadSpeed/60);

                if(h > tPopulationDensity[i][j]){

                    h = tPopulationDensity[i][j];
                }

                if(infectionDensity[i][j] != 0){

                    tPopulationDensity[i][j] -= h;
                    tInfectionDensity[i][j] += h;
                }



            }

        }

    }
    private void incrementTimeInfHr(int[][] tPopulationDensity , int[][] tInfectionDensity) {

        for(int i = 0;i < mapWidth;i++){

            for(int j = 0;j < mapHeight;j++){

                int[] xValues = {1,1,0,-1,-1,-1,0,1};
                int[] yValues = {0,-1,-1,-1,0,1,1,1};

                for(int k = 0;k < xValues.length;k++){

                    int change;

                    if(i + xValues[k] < mapWidth && i + xValues[k] > -1 && j + yValues[k] < mapHeight && j + yValues[k] > -1 && populationDensity[i + xValues[k]][j + yValues[k]] > populationDensity[i][j]){

                        float g = (populationDensity[i + xValues[k]][j + yValues[k]] - populationDensity[i][j])/populationDensity[i + xValues[k]][j + yValues[k]];
                        change = (int)((g + zomTrickle)*infectionDensity[i + xValues[k]][j + yValues[k]]*zomSpeed*accessibility[i][j]);

                        if(change > infectionDensity[i + xValues[k]][j + yValues[k]]){

                            change = infectionDensity[i + xValues[k]][j + yValues[k]];
                        }

                        infectionDensity[i + xValues[k]][j + yValues[k]] -= change;
                        infectionDensity[i][j] += change;


                    }else if(i + xValues[k] < mapWidth && i + xValues[k] > -1 && j + yValues[k] < mapHeight && j + yValues[k] > -1){

                        change = (int)(zomTrickle*infectionDensity[i + xValues[k]][j + yValues[k]]*zomSpeed*accessibility[i][j]);

                        if(change > tInfectionDensity[i + xValues[k]][j + yValues[k]]){

                            change = tInfectionDensity[i + xValues[k]][j + yValues[k]];
                        }

                        tInfectionDensity[i + xValues[k]][j + yValues[k]] -= change;
                        tInfectionDensity[i][j] += change;
                    }

                }

                float inf;

                if(Math.abs(populationDensity[i][j]-infectionDensity[i][j]) == 0){

                    inf = 1;

                }else{

                    inf = (float)(1/Math.abs(populationDensity[i][j]-infectionDensity[i][j]));
                    inf = sigmoid(inf);
                }

                int h =  (int)(populationDensity[i][j]*inf*maxSpreadSpeed);

                if(h > tPopulationDensity[i][j]){

                    h = tPopulationDensity[i][j];
                }

                if(infectionDensity[i][j] != 0){

                    tPopulationDensity[i][j] -= h;
                    tInfectionDensity[i][j] += h;

                }





            }

        }

    }

    //these apply the above functions to move time forward
    public void incrementTimeMin(){

        int[][] tPopulationDensity = populationDensity;
        int[][] tInfectionDensity = infectionDensity;
        incrementTimeInfMin(tPopulationDensity,tInfectionDensity);
        incrementTimePopMin(tPopulationDensity,tInfectionDensity);
        populationDensity = tPopulationDensity;
        infectionDensity = tInfectionDensity;

    }
    public void incrementTimeHr(){

        int[][] tPopulationDensity = populationDensity;
        int[][] tInfectionDensity = infectionDensity;
        incrementTimeInfHr(tPopulationDensity,tInfectionDensity);
        incrementTimePopHr(tPopulationDensity,tInfectionDensity);
        populationDensity = tPopulationDensity;
        infectionDensity = tInfectionDensity;

    }

    //this creates and stores a location object
    public void createLocation(int x, int y, int type){

        Location newLoc = new Location(x,y,type);

        locations.get(type).add(newLoc);



    }
}
