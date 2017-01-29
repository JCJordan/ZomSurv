/**
 * Created by JCJordan on 28/01/2017.
 */
public class Location {

    private int xPos;
    private int yPos;
    private String type;
    private float defence;
    private float strength;

    public Location(int x, int y , String t){

        xPos = x;
        yPos = y;
        type = t;
        defence = (float)Math.random();
        strength = (float)Math.random();

    }

    public int getXPos(){ return xPos; }
    public int getYPos(){ return yPos; }
    public String getType(){ return type; }
    public float getDefence(){ return defence; }
    public float getStrength(){ return strength; }

}
