import java.util.ArrayList;

/**
 * Created by JCJordan on 28/01/2017.
 */
public class Map {

    private int map_width;
    private int map_height;
    public int[][] population_density;
    public int[][] infection_density;
    public int[][] accessibility;//all less than 1
    public ArrayList<ArrayList<Location>> locations = new ArrayList<ArrayList<Location>>(4);;//0:hospital,1:graveyard,2:church,3:Supermarket
    float zom_speed = 0.5f;//less than 1
    float zom_trickle = 0.01f;
    float pop_trickle = 0.02f;
    float max_spread_speed = 0.2f;//less than 1
    float pop_speed = 0.6f;//less than 1




    public Map(int x, int y,int start_time){

        map_width = x;
        map_height = y;
        population_density = new int[x][y];
        infection_density = new int[x][y];
        accessibility = new int[x][y];


    }


    private float sigmoid(float x) {
        return (float)(1/( 1 + Math.pow(Math.E,(-1*x))));
    }

    //these deal with population spread
    private void increment_time_pop_min() {

        for(int i = 0;i < map_width;i++) {

            for (int j = 0; j < map_height; j++) {


                int[] x_values = {1,1,0,-1,-1,-1,0,1};
                int[] y_values = {0,-1,-1,-1,0,1,1,1};

                for(int k = 0;k < x_values.length;k++){

                    int change;

                    if(i + x_values[k] < map_width && i + x_values[k] > -1 && j + y_values[k] < map_height && j + y_values[k] > -1 && infection_density[i + x_values[k]][j + y_values[k]] > infection_density[i][j]){

                        float g = (infection_density[i + x_values[k]][j + y_values[k]] - infection_density[i][j])/infection_density[i + x_values[k]][j + y_values[k]];
                        change = (int)((g + pop_trickle)*population_density[i + x_values[k]][j + y_values[k]]*pop_speed*accessibility[i][j]);


                    }else{

                        change = (int)(pop_trickle*population_density[i + x_values[k]][j + y_values[k]]*pop_speed*accessibility[i][j]);
                    }

                    if(change/60 > population_density[i + x_values[k]][j + y_values[k]]){

                        change = population_density[i + x_values[k]][j + y_values[k]];
                    }

                    population_density[i + x_values[k]][j + y_values[k]] -= change/60;
                    population_density[i][j] += change/60;

                }

            }
        }

    }
    private void increment_time_pop_hr() {

        for(int i = 0;i < map_width;i++) {

            for (int j = 0; j < map_height; j++) {


                int[] x_values = {1,1,0,-1,-1,-1,0,1};
                int[] y_values = {0,-1,-1,-1,0,1,1,1};

                for(int k = 0;k < x_values.length;k++){

                    int change;

                    if(i + x_values[k] < map_width && i + x_values[k] > -1 && j + y_values[k] < map_height && j + y_values[k] > -1 && infection_density[i + x_values[k]][j + y_values[k]] > infection_density[i][j]){

                        float g = (infection_density[i + x_values[k]][j + y_values[k]] - infection_density[i][j])/infection_density[i + x_values[k]][j + y_values[k]];
                        change = (int)((g + pop_trickle)*population_density[i + x_values[k]][j + y_values[k]]*pop_speed*accessibility[i][j]);


                    }else{

                        change = (int)(pop_trickle*population_density[i + x_values[k]][j + y_values[k]]*pop_speed*accessibility[i][j]);
                    }

                    if(change/60 > population_density[i + x_values[k]][j + y_values[k]]){

                        change = population_density[i + x_values[k]][j + y_values[k]];
                    }

                    population_density[i + x_values[k]][j + y_values[k]] -= change;
                    population_density[i][j] += change;

                }

            }
        }

    }

    //these deal with zombie spread
    private void increment_time_inf_min() {

        for(int i = 0;i < map_width;i++){

            for(int j = 0;j < map_height;j++){

                int[] x_values = {1,1,0,-1,-1,-1,0,1};
                int[] y_values = {0,-1,-1,-1,0,1,1,1};

                for(int k = 0;k < x_values.length;k++){

                    int change;

                    if(i + x_values[k] < map_width && i + x_values[k] > -1 && j + y_values[k] < map_height && j + y_values[k] > -1 && population_density[i + x_values[k]][j + y_values[k]] > population_density[i][j]){

                        float g = (population_density[i + x_values[k]][j + y_values[k]] - population_density[i][j])/population_density[i + x_values[k]][j + y_values[k]];
                        change = (int)((g + zom_trickle)*infection_density[i + x_values[k]][j + y_values[k]]*zom_speed*accessibility[i][j]);


                    }else{

                        change = (int)(zom_trickle*infection_density[i + x_values[k]][j + y_values[k]]*zom_speed*accessibility[i][j]);
                    }

                    if(change/60 > infection_density[i + x_values[k]][j + y_values[k]]){

                        change = infection_density[i + x_values[k]][j + y_values[k]];
                    }

                    infection_density[i + x_values[k]][j + y_values[k]] -= change/60;
                    infection_density[i][j] += change/60;

                }

                float inf;

                if(Math.abs(population_density[i][j]-infection_density[i][j]) == 0){

                    inf = 1;

                }else{

                    inf = (float)(1/Math.abs(population_density[i][j]-infection_density[i][j]));
                    inf = sigmoid(inf);
                }

                int h =  (int)(population_density[i][j]*inf*max_spread_speed/60);
                population_density[i][j] -= h;
                infection_density[i][j] += h;




            }

        }

    }
    private void increment_time_inf_hr() {

        for(int i = 0;i < map_width;i++){

            for(int j = 0;j < map_height;j++){

                int[] x_values = {1,1,0,-1,-1,-1,0,1};
                int[] y_values = {0,-1,-1,-1,0,1,1,1};

                for(int k = 0;k < x_values.length;k++){

                    int change;

                    if(i + x_values[k] < map_width && i + x_values[k] > -1 && j + y_values[k] < map_height && j + y_values[k] > -1 && population_density[i + x_values[k]][j + y_values[k]] > population_density[i][j]){

                        float g = (population_density[i + x_values[k]][j + y_values[k]] - population_density[i][j])/population_density[i + x_values[k]][j + y_values[k]];
                        change = (int)((g + zom_trickle)*infection_density[i + x_values[k]][j + y_values[k]]*zom_speed*accessibility[i][j]);


                    }else{

                        change = (int)(zom_trickle*infection_density[i + x_values[k]][j + y_values[k]]*zom_speed*accessibility[i][j]);
                    }

                    if(change > infection_density[i + x_values[k]][j + y_values[k]]){

                        change = infection_density[i + x_values[k]][j + y_values[k]];
                    }

                    infection_density[i + x_values[k]][j + y_values[k]] -= change;
                    infection_density[i][j] += change;

                }

                float inf;

                if(Math.abs(population_density[i][j]-infection_density[i][j]) == 0){

                    inf = 1;

                }else{

                    inf = (float)(1/Math.abs(population_density[i][j]-infection_density[i][j]));
                    inf = sigmoid(inf);
                }

                int h =  (int)(population_density[i][j]*inf*max_spread_speed);
                population_density[i][j] -= h;
                infection_density[i][j] += h;




            }

        }

    }

    //these apply the above functions to move time forward
    public void increment_time_min(){


        increment_time_inf_min();
        increment_time_pop_min();

    }
    public void increment_time_hr(){


        increment_time_inf_hr();
        increment_time_pop_hr();

    }

    //this creates and stores a location object
    public void create_location(int x, int y, int type){

        Location new_loc = new Location(x,y,type);

        locations.get(type).add(new_loc);



    }
}
