/*
 * This class just holds all the variables for the big slime, and a method that returns a bool based
 * on whether the slime should be alive or killed.
 */

public class Slime
{
    private int health, damage;
    
    public Slime()
    {
        health = 3;
        damage = 1;
    }
    
    /*
     * Return the slimes damage.
     */
    public int attack(){
        return damage;
    }
    
    public int getHealth(){
        return health;
    }
    
    /*
     * Takes the damage received and subtracts it from the slime health, if it's still alive return false,
     * otherwise return true.
     */
    public boolean attacked(int damage){
        health -= damage;
        if (health <= 0){
            return true;
        }
        return false;
    }

}
