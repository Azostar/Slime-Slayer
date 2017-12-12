import java.util.Random;

public class Weapon
{
    private int damage;
    private boolean spread, evasion;
    private Random rnd;
    
    public Weapon(int damage, String properties)
    {
        this.damage = damage;
        rnd = new Random();
        addProperties(properties);
    }
    
    /*
     * Toggles propeties for the weapon based on the input.
     */
    public void addProperties(String properties){
        if (properties.equals("spread")){
            spread = true;
        }
        if (properties.equals("evasion")){
            evasion = true;
        }
    }
    
    /*
     * This method checks if we have evasion on our weapon, if we do, the player has
     * 33% chance to dodge the attack incoming.
     */
    public boolean hasEvasion(){
        if (evasion == true && rnd.nextInt(3) == 2){
            return true;
        }
        return false;
    }
    
    /*
     * Returns the damage that the weapon has.
     */
    public int getDamage(){
        return damage;
    }
    
    /*
     * This method checks if our weapon has spread, if it does, generate a random number
     * between 1 and 3 and return that number.
     */
    public int getSpread(){
        if (spread == true){
            return rnd.nextInt(2)+2;
        }
        return 1;
    }

}
