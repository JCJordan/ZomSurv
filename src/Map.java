import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.io.File;
import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;


/**
 * Created by JCJordan on 28/01/2017.
 */


public class Map{

    private int map_width;
    private int map_height;
    public int[][] population_density;
    public int[][] infection_density;
    public float[][] accessibility;//all less than 1
    public ArrayList<ArrayList<Location>> locations = new ArrayList<ArrayList<Location>>(4);;//0:hospital,1:graveyard,2:church,3:Supermarket
    float zom_speed = 0.5f;//less than 1
    float zom_trickle = 0.01f;
    float pop_trickle = 0.02f;
    float max_spread_speed = 0.2f;//less than 1
    float pop_speed = 0.6f;//less than 1




    public Map(){

        load_accessibility();

        BufferedImage img = new BufferedImage(map_width, map_height, BufferedImage.TYPE_INT_ARGB);
        File f = null;
        int a;
        int r;
        int g;
        int b;

        for(int i = 0;i < map_width;i++) {

            for (int j = 0; j < map_height; j++) {

                if(infection_density[i][j] > 0){

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
            f = new File("res/Output.png");
            ImageIO.write(img, "png", f);
        }catch(IOException e){
            System.out.println("Error: " + e);
        }

        for(int l = 0; l < 100;l++){

            increment_time_min();
        }





        img = new BufferedImage(map_width, map_height, BufferedImage.TYPE_INT_ARGB);
        f = null;

        for(int i = 0;i < map_width;i++) {

            for (int j = 0; j < map_height; j++) {

                if(infection_density[i][j] > 0){

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
            f = new File("res/Output2.png");
            ImageIO.write(img, "png", f);
        }catch(IOException e){
            System.out.println("Error: " + e);
        }


    }

    private void load_accessibility(){

        BufferedImage image;
        File sourceimage = new File("res/accessiblity.png");
        try {
            image = ImageIO.read(sourceimage);
            map_width = image.getWidth();
            map_height = image.getHeight();
            population_density = new int[map_width][map_height];
            infection_density = new int[map_width][map_height];
            accessibility = new float[map_width][map_height];

            for(int i = 0;i < map_width;i++) {

                for (int j = 0; j < map_height; j++) {

                    accessibility[i][j] =  (image.getRGB(i,j)& 0xFF)/255;
                    population_density[i][j] = (int)(200000*accessibility[i][j]);

                }

            }
            boolean test = false;
            for(int i = 0;i < map_width;i++) {

                for (int j = 0; j < map_height; j++) {

                    if(accessibility[i][j] == 1.0f && test == false){

                        infection_density[i][j] = 10000;
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
    private void increment_time_pop_min(int[][] t_population_density , int[][] t_infection_density) {

        for(int i = 0;i < map_width;i++) {

            for (int j = 0; j < map_height; j++) {


                int[] x_values = {1,1,0,-1,-1,-1,0,1};
                int[] y_values = {0,-1,-1,-1,0,1,1,1};

                for(int k = 0;k < x_values.length;k++){

                    int change;

                    if(i + x_values[k] < map_width && i + x_values[k] > -1 && j + y_values[k] < map_height && j + y_values[k] > -1 && infection_density[i + x_values[k]][j + y_values[k]] > infection_density[i][j]){

                        float g = (infection_density[i + x_values[k]][j + y_values[k]] - infection_density[i][j])/infection_density[i + x_values[k]][j + y_values[k]];
                        change = (int)((g + pop_trickle)*population_density[i + x_values[k]][j + y_values[k]]*pop_speed*accessibility[i][j]);
                        if(change/60 > t_population_density[i + x_values[k]][j + y_values[k]]){

                            change = t_population_density[i + x_values[k]][j + y_values[k]];
                        }

                        t_population_density[i + x_values[k]][j + y_values[k]] -= change/60;
                        t_population_density[i][j] += change/60;


                    }else if(i + x_values[k] < map_width && i + x_values[k] > -1 && j + y_values[k] < map_height && j + y_values[k] > -1){

                        change = (int)(pop_trickle*population_density[i + x_values[k]][j + y_values[k]]*pop_speed*accessibility[i][j]);

                        if(change/60 > t_population_density[i + x_values[k]][j + y_values[k]]){

                            change = t_population_density[i + x_values[k]][j + y_values[k]];
                        }

                        t_population_density[i + x_values[k]][j + y_values[k]] -= change/60;
                        t_population_density[i][j] += change/60;
                    }


                }

            }
        }


    }
    private void increment_time_pop_hr(int[][] t_population_density , int[][] t_infection_density) {

        for(int i = 0;i < map_width;i++) {

            for (int j = 0; j < map_height; j++) {


                int[] x_values = {1,1,0,-1,-1,-1,0,1};
                int[] y_values = {0,-1,-1,-1,0,1,1,1};

                for(int k = 0;k < x_values.length;k++){

                    int change;

                    if(i + x_values[k] < map_width && i + x_values[k] > -1 && j + y_values[k] < map_height && j + y_values[k] > -1 && infection_density[i + x_values[k]][j + y_values[k]] > infection_density[i][j]){

                        float g = (infection_density[i + x_values[k]][j + y_values[k]] - infection_density[i][j])/infection_density[i + x_values[k]][j + y_values[k]];
                        change = (int)((g + pop_trickle)*population_density[i + x_values[k]][j + y_values[k]]*pop_speed*accessibility[i][j]);
                        if(change/60 > t_population_density[i + x_values[k]][j + y_values[k]]){

                            change = t_population_density[i + x_values[k]][j + y_values[k]];
                        }

                        t_population_density[i + x_values[k]][j + y_values[k]] -= change;
                        t_population_density[i][j] += change;


                    }else if(i + x_values[k] < map_width && i + x_values[k] > -1 && j + y_values[k] < map_height && j + y_values[k] > -1){

                        change = (int)(pop_trickle*population_density[i + x_values[k]][j + y_values[k]]*pop_speed*accessibility[i][j]);
                        if(change/60 > t_population_density[i + x_values[k]][j + y_values[k]]){

                            change = t_population_density[i + x_values[k]][j + y_values[k]];
                        }

                        t_population_density[i + x_values[k]][j + y_values[k]] -= change;
                        t_population_density[i][j] += change;
                    }


                }

            }
        }

    }

    //these deal with zombie spread
    private void increment_time_inf_min(int[][] t_population_density , int[][] t_infection_density) {

        for(int i = 0;i < map_width;i++){

            for(int j = 0;j < map_height;j++){

                int[] x_values = {1,1,0,-1,-1,-1,0,1};
                int[] y_values = {0,-1,-1,-1,0,1,1,1};

                for(int k = 0;k < x_values.length;k++){

                    int change = 0;

                    if(i + x_values[k] < map_width && i + x_values[k] > -1 && j + y_values[k] < map_height && j + y_values[k] > -1 && population_density[i + x_values[k]][j + y_values[k]] > population_density[i][j]){

                        float g = (population_density[i + x_values[k]][j + y_values[k]] - population_density[i][j])/population_density[i + x_values[k]][j + y_values[k]];
                        change = (int)((g + zom_trickle)*infection_density[i + x_values[k]][j + y_values[k]]*zom_speed*accessibility[i][j]);

                        if(change/60 > t_infection_density[i + x_values[k]][j + y_values[k]]){

                            change = t_infection_density[i + x_values[k]][j + y_values[k]];
                        }

                        t_infection_density[i + x_values[k]][j + y_values[k]] -= change/60;
                        t_infection_density[i][j] += change/60;


                    }else if(i + x_values[k] < map_width && i + x_values[k] > -1 && j + y_values[k] < map_height && j + y_values[k] > -1){

                        change = (int)(zom_trickle*infection_density[i + x_values[k]][j + y_values[k]]*zom_speed*accessibility[i][j]);
                        if(change/60 > t_infection_density[i + x_values[k]][j + y_values[k]]){

                            change = t_infection_density[i + x_values[k]][j + y_values[k]];
                        }

                        t_infection_density[i + x_values[k]][j + y_values[k]] -= change/60;
                        t_infection_density[i][j] += change/60;
                    }


                }

                float inf;

                if(Math.abs(population_density[i][j]-infection_density[i][j]) == 0){

                    inf = 1;

                }else{

                    inf = (float)(1/Math.abs(population_density[i][j]-infection_density[i][j]));
                    inf = sigmoid(inf);
                }

                int h =  (int)(population_density[i][j]*inf*max_spread_speed/60);

                if(h > t_population_density[i][j]){

                    h = t_population_density[i][j];
                }

                if(infection_density[i][j] != 0){

                    t_population_density[i][j] -= h;
                    t_infection_density[i][j] += h;
                }



            }

        }

    }
    private void increment_time_inf_hr(int[][] t_population_density , int[][] t_infection_density) {

        for(int i = 0;i < map_width;i++){

            for(int j = 0;j < map_height;j++){

                int[] x_values = {1,1,0,-1,-1,-1,0,1};
                int[] y_values = {0,-1,-1,-1,0,1,1,1};

                for(int k = 0;k < x_values.length;k++){

                    int change;

                    if(i + x_values[k] < map_width && i + x_values[k] > -1 && j + y_values[k] < map_height && j + y_values[k] > -1 && population_density[i + x_values[k]][j + y_values[k]] > population_density[i][j]){

                        float g = (population_density[i + x_values[k]][j + y_values[k]] - population_density[i][j])/population_density[i + x_values[k]][j + y_values[k]];
                        change = (int)((g + zom_trickle)*infection_density[i + x_values[k]][j + y_values[k]]*zom_speed*accessibility[i][j]);

                        if(change > infection_density[i + x_values[k]][j + y_values[k]]){

                            change = infection_density[i + x_values[k]][j + y_values[k]];
                        }

                        infection_density[i + x_values[k]][j + y_values[k]] -= change;
                        infection_density[i][j] += change;


                    }else if(i + x_values[k] < map_width && i + x_values[k] > -1 && j + y_values[k] < map_height && j + y_values[k] > -1){

                        change = (int)(zom_trickle*infection_density[i + x_values[k]][j + y_values[k]]*zom_speed*accessibility[i][j]);

                        if(change > t_infection_density[i + x_values[k]][j + y_values[k]]){

                            change = t_infection_density[i + x_values[k]][j + y_values[k]];
                        }

                        t_infection_density[i + x_values[k]][j + y_values[k]] -= change;
                        t_infection_density[i][j] += change;
                    }

                }

                float inf;

                if(Math.abs(population_density[i][j]-infection_density[i][j]) == 0){

                    inf = 1;

                }else{

                    inf = (float)(1/Math.abs(population_density[i][j]-infection_density[i][j]));
                    inf = sigmoid(inf);
                }

                int h =  (int)(population_density[i][j]*inf*max_spread_speed);

                if(h > t_population_density[i][j]){

                    h = t_population_density[i][j];
                }

                if(infection_density[i][j] != 0){

                    t_population_density[i][j] -= h;
                    t_infection_density[i][j] += h;

                }





            }

        }

    }

    //these apply the above functions to move time forward
    public void increment_time_min(){

        int[][] t_population_density = population_density;
        int[][] t_infection_density = infection_density;
        increment_time_inf_min(t_population_density,t_infection_density);
        increment_time_pop_min(t_population_density,t_infection_density);
        population_density = t_population_density;
        infection_density = t_infection_density;

    }
    public void increment_time_hr(){

        int[][] t_population_density = population_density;
        int[][] t_infection_density = infection_density;
        increment_time_inf_hr(t_population_density,t_infection_density);
        increment_time_pop_hr(t_population_density,t_infection_density);
        population_density = t_population_density;
        infection_density = t_infection_density;

    }

    //this creates and stores a location object
    public void create_location(int x, int y, int type){

        Location new_loc = new Location(x,y,type);

        locations.get(type).add(new_loc);



    }
}
