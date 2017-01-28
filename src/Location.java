/**
 * Created by JCJordan on 28/01/2017.
 */
public class Location {

    public int x_pos;
    public int y_pos;
    public int type;
    public float defence;
    public float strength;

    public Location(int x, int y , int t){

        x_pos = x;
        y_pos = y;
        type = t;
        defence = (float)Math.random();
        strength = (float)Math.random();

    }
}
