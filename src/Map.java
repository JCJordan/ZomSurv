import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.io.File;
import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;


/**
 * Created by CALLUM on 28/01/2017.
 */


public class Map{

    private int mapWidth;
    private int mapHeight;
    private int[][] populationDensity;
    private int[][] infectionDensity;
    private float[][] accessibility;//all less than 1
    private ArrayList<Location> locations = new ArrayList<Location>();
    float ZOM_SPEED = 0.8f;//less than 1
    float ZOM_TRICKLE = 0.2f;
    float POP_TRICKLE = 0.1f;
    float MAX_SPREAD_SPEED = 0.1f;//less than 1
    float POP_SPEED = 0.2f;//less than 1
    int average_pop = 1;
    int average_inf = 1;


    public int get_pop_av(){

        return average_pop;
    }

    public int get_inf_av(){

        return average_inf;
    }

    public Map(){

        loadAccessibility();

        createLocation((int)511.6589138,(int)187.3574531,"cemetery");
        createLocation((int)559.6982814,(int)20.96686572,"cemetery");
        createLocation((int)65.73297856,(int)209.3834976,"caravan");
        createLocation((int)130.5043043,(int)506.3890994,"caravan");
        createLocation((int)232.7700736,(int)534.7601176,"library");
        createLocation((int)312.1364846,(int)242.0023295,"library");
        createLocation((int)101.4143058,(int)229.0423561,"library");
        createLocation((int)41.68164032,(int)237.5095861,"leisure");
        createLocation((int)296.2992644,(int)244.2155706,"leisure");

    }

    // testing method
    public static void main(String [ ] args){
        Map map = new Map();

        map.addZombies(0,0,10000);
        //map.addZombies(310,200,10000);
        map.addZombies(200,310,10000);

        JFrame editorFrame = new JFrame("Image Demo");
        editorFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        BufferedImage img = new BufferedImage(map.mapWidth, map.mapHeight, BufferedImage.TYPE_INT_RGB);
        JLabel jLabel = new JLabel();

        for(int l = 0; l < 1000;l++){
            map.incrementTime(60);



            int[][][] rgb = map.getRGB();

            //System.out.println("" + map.populationDensity[0][0] + " " + map.infectionDensity[0][0] + " " + rgb[0][0][0] + " " + rgb[0][0][1]);


            for(int i = 0;i < map.mapWidth;i++) {
                for (int j = 0; j < map.mapHeight; j++) {
                    int argb = (0 << 24) | (rgb[i][j][0] << 16 ) | (rgb[i][j][1]<<8) | (rgb[i][j][2]);
                    img.setRGB(i, j, argb );
                }
            }

            ImageIcon imageIcon = new ImageIcon(img);
            jLabel.setIcon(imageIcon);
            editorFrame.getContentPane().add(jLabel, BorderLayout.CENTER);

            editorFrame.pack();
            editorFrame.setLocationRelativeTo(null);
            editorFrame.setVisible(true);
            /*
            try{
                File f = new File("src/Graphics/resources/infection_spread/Output"+l+".png");
                ImageIO.write(img, "png", f);
            }catch(IOException e){
                System.out.println("Error: " + e);
            }
            */

        }



    }

    private void loadAccessibility(){

        BufferedImage imageAccessibility;
        BufferedImage imagePopulation;
        File sourceImageAccessibility = new File("src/Graphics/resources/accessibility.png");
        File sourceImagePopulation = new File("src/Graphics/resources/population.png");
        try {
            imageAccessibility = ImageIO.read(sourceImageAccessibility);
            imagePopulation = ImageIO.read(sourceImagePopulation);
            mapWidth = imageAccessibility.getWidth();
            mapHeight = imageAccessibility.getHeight();
            populationDensity = new int[mapWidth][mapHeight];
            infectionDensity = new int[mapWidth][mapHeight];
            accessibility = new float[mapWidth][mapHeight];


            for(int i = 0;i < mapWidth;i++) {

                for (int j = 0; j < mapHeight; j++) {

                    accessibility[i][j] =  (imageAccessibility.getRGB(i,j)& 0xFF)/255;
                    populationDensity[i][j] = (imagePopulation.getRGB(i,j)& 0xFF)*128;

                }
            }

        } catch(IOException e) {
            e.printStackTrace();
        }


    }

    public void addZombies(int startI, int startJ, int startValue){
        if(startI >= 0 && startJ>=0 && startI < mapHeight && startJ < mapWidth && accessibility[startI][startJ] == 1.0f){
            infectionDensity[startI][startJ] = startValue;

        }
    }

    public int[][] getPopulationDensity(){
        return populationDensity;
    }

    public int[][] getInfectionDensity(){
        return infectionDensity;
    }

    public int[][][] getRGB(){
        int totalPopulation;
        double proportionInfected;
        int[][][] rgb = new int[mapHeight][mapWidth][3];
        for (int i=0;i<mapHeight;i++){
            for(int j=0;j<mapWidth;j++){
                totalPopulation = populationDensity[i][j] + infectionDensity[i][j];

                if(totalPopulation == 0){
                    rgb[i][j][0]= 255;
                    rgb[i][j][1]= 255;
                    rgb[i][j][2]= 255;
                }
                else{
                    proportionInfected = ((double) infectionDensity[i][j]) / totalPopulation;
                    if(proportionInfected < 0.5){
                        rgb[i][j][0]= (int) (proportionInfected*2*255);
                        rgb[i][j][1]= 255;
                        rgb[i][j][2]= 0;
                    }else{
                        rgb[i][j][0]= 255;
                        rgb[i][j][1]= (int) (255-(proportionInfected*2-1)*255);
                        rgb[i][j][2]= 0;
                    }
                }

            }
        }
        return rgb;
    }

    public int getMapWidth(){
        return mapWidth;
    }

    public int getMapHeight(){
        return mapHeight;
    }

    public int get_inf_dens_point(int x,int y){

        return infectionDensity[x][y];
    }

    public int get_pop_dens_point(int x,int y){
        return populationDensity[x][y];
    }


    private float sigmoid(float x) {
        return (float)(1/( 1 + Math.pow(Math.E,(-1*x))));
    }

    //these deal with population spread
    private void incrementTimePop(int[][] tPopulationDensity, int numMin) {

        for(int i = 0;i < mapWidth;i++) {

            for (int j = 0; j < mapHeight; j++) {


                int[] xValues = {1,1,0,-1,-1,-1,0,1};
                int[] yValues = {0,-1,-1,-1,0,1,1,1};

                for(int k = 0;k < xValues.length;k++){

                    int change;

                    // check if in bounds
                    if (i + xValues[k] < mapWidth && i + xValues[k] > -1 && j + yValues[k] < mapHeight && j + yValues[k] > -1){
                        // check if infection density of adjacent is greater than current
                        if (infectionDensity[i + xValues[k]][j + yValues[k]] > infectionDensity[i][j]){
                            float g = (infectionDensity[i + xValues[k]][j + yValues[k]] - infectionDensity[i][j])/infectionDensity[i + xValues[k]][j + yValues[k]];
                            change = (int)((g + POP_TRICKLE)*populationDensity[i + xValues[k]][j + yValues[k]]*POP_SPEED*accessibility[i][j]);
                        }
                        else {
                            change = (int)(POP_TRICKLE*populationDensity[i + xValues[k]][j + yValues[k]]*POP_SPEED*accessibility[i][j]);
                        }

                        if(change*numMin/60 > tPopulationDensity[i + xValues[k]][j + yValues[k]]){

                            change = tPopulationDensity[i + xValues[k]][j + yValues[k]];
                        }
                        tPopulationDensity[i + xValues[k]][j + yValues[k]] -= change*numMin/60;
                        tPopulationDensity[i][j] += change*numMin/60;
                    }
                }
            }
        }


    }

    private void incrementTimeInf(int[][] tPopulationDensity , int[][] tInfectionDensity, int numMin) {

        for(int i = 0;i < mapWidth;i++){

            for(int j = 0;j < mapHeight;j++){

                int[] xValues = {1,1,0,-1,-1,-1,0,1};
                int[] yValues = {0,-1,-1,-1,0,1,1,1};

                // MOVING INFECTED AROUND
                for(int k = 0;k < xValues.length;k++){

                    int change;

                    // check if in bounds
                    if(i + xValues[k] < mapWidth && i + xValues[k] > -1 && j + yValues[k] < mapHeight && j + yValues[k] > -1){

                        // set change
                        if (populationDensity[i + xValues[k]][j + yValues[k]] < populationDensity[i][j]){ // check if adjacent population is smaller than centre
                            // relative difference of healthy population between adjacent and centre  (pop healthy adjacent - pop healthy centre / pop healthy adjacent)
                            float g = (populationDensity[i][j] - populationDensity[i + xValues[k]][j + yValues[k]])/populationDensity[i][j];
                            change = (int)((g + ZOM_TRICKLE)*infectionDensity[i + xValues[k]][j + yValues[k]]*ZOM_SPEED*accessibility[i][j]);
                        }
                        else{
                            change = (int)(ZOM_TRICKLE*infectionDensity[i + xValues[k]][j + yValues[k]]*ZOM_SPEED*accessibility[i][j]);
                        }

                        // make sure non negative
                        if(change > tInfectionDensity[i + xValues[k]][j + yValues[k]]){
                            change = tInfectionDensity[i + xValues[k]][j + yValues[k]];
                        }

                        tInfectionDensity[i + xValues[k]][j + yValues[k]] -= change*numMin/60;
                        tInfectionDensity[i][j] += change*numMin/60;
                    }

                }

                // INFECTING MORE PEOPLE
                float inf;

                if(Math.abs(populationDensity[i][j]-infectionDensity[i][j]) == 0){

                    inf = 1;

                }else{

                    inf = (float)(1/Math.abs(populationDensity[i][j]-infectionDensity[i][j]));
                    inf = sigmoid(inf);
                }

                int h =  (int)(populationDensity[i][j]*inf*MAX_SPREAD_SPEED*numMin/60);

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
    public void incrementTime(int numMin){
        // deep clone populationDensity and infectionDensity
        int[][] tPopulationDensity = new int[mapWidth][mapHeight];
        int[][] tInfectionDensity = new int[mapWidth][mapHeight];

        average_pop = 0;
        average_inf = 0;

        for(int i = 0;i < mapWidth;i++) {
            for (int j = 0; j < mapHeight; j++) {
                tPopulationDensity[i][j] = populationDensity[i][j];
                tInfectionDensity[i][j] = infectionDensity[i][j];
                average_inf += infectionDensity[i][j];
                average_pop += populationDensity[i][j];
            }
        }

        average_pop = (int)(average_pop/(mapWidth*mapHeight));
        average_inf = (int)(average_inf/(mapWidth*mapHeight));

        incrementTimeInf(tPopulationDensity,tInfectionDensity, numMin);
        incrementTimePop(tPopulationDensity, numMin);
        populationDensity = tPopulationDensity;
        infectionDensity = tInfectionDensity;

    }


    //this creates and stores a location object
    public void createLocation(int x, int y, String type){

        Location newLoc = new Location(x,y,type);

        locations.add(newLoc);

    }

    public ArrayList<Location> getLocations(){
        return locations;
    }
}
