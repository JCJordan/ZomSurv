/**
 * Created by JCJordan on 28/01/2017.
 */
public class Location {

    public int xPos;
    public int yPos;
    public int type;
    public float defence;
    public float strength;

    public Location(int x, int y , int t){

        xPos = x;
        yPos = y;
        type = t;
        defence = (float)Math.random();
        strength = (float)Math.random();

    }

    public int getXPos(){ return xPos; }
    public int getyPos(){ return yPos; }
    public int getType(){ return type; }
    public float getDefence(){ return defence; }
    public float getStrength(){ return strength; }

}
