import java.util.ArrayList;
import java.util.Random;

public class Enemies
{
    private ArrayList<Slime> slimes;
    private BigSlime bSlime;
    private Stats stats;
    private Random rnd;
    
    public Enemies(Stats stats)
    {
        this.stats = stats;
        rnd = new Random();
        slimes = new ArrayList<Slime>();
        bSlime = null;
    }
    
    /*
     * Spawn enemies based on the amount of time spent in the game moving around,
     * the more time spent, the more difficult it becomes.
     */
    private void spawnEnemies(){
        int actions = stats.getActions();
        
        if (actions > 40){
            if (bSlime == null){
                BigSlime bSlime = new BigSlime();
            }
            for(int i = 0; i < rnd.nextInt(4) + 1; i++){  
                slimes.add(new Slime());
            }
        }
        else if(actions > 20){
            for (int i = 0; i < rnd.nextInt(3) + 1; i++){
                slimes.add(new Slime());
            }
        }
        else if(actions > 10){
            for (int i = 0; i < rnd.nextInt(3); i++){
                slimes.add(new Slime());
            }
        }
        else if(rnd.nextInt(2) == 1){
            slimes.add(new Slime());
        }
    }
    
    /*
     * Called when the player attacks enemies in the room.
     * 
     * Method will start with the biggest slime first, because it body blocks (aka didn't have enough
     * time to write targeting methods) and work it's way down the enemy list.
     * 
     * Will cycle through the functions depending on if their is spread on the weapon.
     * 
     * Sets an int at the start using the list size of slimes that determines which slime to attack,
     * attacks on normal slimes start at the end of the list and work up, to avoid any errors.
     * 
     * If the slimes attacked method returns true, it is dead, and should be removed from the
     * enemy object.
     */
    public void attacked(int damage, int number){
        int horde = slimes.size();
        
        for (int i = 0; i < number; i++){
            if (bSlime != null){
                if(bSlime.attacked(damage) == true){
                    bSlime = null;
                    stats.slimeKilled();
                    System.out.println("You attack the big slime, it exploded into pieces.\n");
                }
                else{
                    System.out.println("You hit the big slime, a chunk of goo falls off it.\n");
                }
            }
            
            if(!slimes.isEmpty()){
                if(slimes.get(horde-1-i).attacked(damage)){
                    slimes.remove(horde-1-i);
                    if(number > 1){
                        System.out.print("Your spray hits a slime, goo flies everywhere.\n");
                    }else{
                        System.out.print("You attack the nearest slime, goo splats everywhere.\n");
                    }
                    stats.slimeKilled();
                }else{
                    System.out.print("You hit the nearest slime, it wobbles a little.\n");
                }
            }
        }
    }
    
    /*
     * Attacks the player and returns an int depending on how much damage the player should take, prints
     * out what the slimes do. If a big slime is in the room, set an offset and increase the amount of
     * total enemeis by 1, this way we can cycle through every slime in the slimes list.
     */
    public int attack(){
        int damage = 0;
        int horde = slimes.size();
        int offest = 0;
        
        if (bSlime != null){
            horde++;
            offest = -1;
        }
        
        for (int i = 0; i < horde; i++){
            
            if (bSlime != null){
                damage += bSlime.attack();
                System.out.println("The big slime strikes first, crushing you with it's gelatonous mass!\n");
            }
            
            
            damage += slimes.get(i+offest).attack();
        }
        
        if(horde > 1){
            System.out.println("The slimes all attack at once, slamming into your body.\n");
        }else{
            System.out.println("A slime slaps against you.\n");
        }
        
        return damage;
    }
    
    /*
     * Add enemies and return a condition the tells us if enemies already existed
     */
    public boolean addEnemies(){
        if(slimes.isEmpty() && bSlime == null){
            spawnEnemies();
            return false;
        }
        spawnEnemies();
        return true;
    }
    
    /*
     * Return if enemies are present in the room.
     */
    public boolean enemiesPresent(){
        if(slimes.isEmpty() && bSlime == null){
            return false;
        }
        return true;
    }
}