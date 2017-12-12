import java.util.HashMap;
import java.util.Random;

public class Player
{
    private Stats currentStats;
    private Game game;
    private Enemies nearbyEnemies;
    private Bag bag;
    private Weapon currentWeapon;
    private Random rnd;
    
    public Player(Game game)
    {
        bag = new Bag();
        this.game = game;
        rnd = new Random();
        currentStats = new Stats(game);
        currentWeapon = bag.getWeapon("fists");
    }
    
    public Stats getStats(){
        return currentStats;
    }
    
    /*
     * Take all the items in the room.
     * Gets the item list from the game room, and places it inside the players bag.
     * If there are no weapons or items, say so.
     */
    public void take(){
        if(game.getRoom().hasItems() || game.getRoom().hasWeapons()){
            System.out.println("You got:");
            bag.addToItemList(game.givePlayer("items"));
            if(game.getRoom().hasWeapons()){
                bag.addToWeaponList(game.givePlayer("weapons"));
                currentStats.weaponPicked();
            }
        }else{
            System.out.println("There are no weapons or items here.");
        }
    }
    
    /*
     * When the player attacks, get the enemies in the room and the weapon object
     * of the weapon that the player wants to use.
     * If the weapon does not exist, print that it doesn't exist.
     * Otherwise, state that there are no enemies nearby.
     * 
     */
    public void attack(String weapon){
        nearbyEnemies = game.getRoom().getEnemies();
        Weapon usedWeapon = bag.getWeapon(weapon);
        
        if(usedWeapon == null){
            System.out.println("You don't have that weapon");
        }else if(!nearbyEnemies.enemiesPresent()){
            System.out.println("There are no enemies nearby");
        }else{
            currentWeapon = usedWeapon;
            nearbyEnemies.attacked(currentWeapon.getDamage(), currentWeapon.getSpread());
        }
    }
    
    /*
     * Use the item described in the input.
     */
    public void use(String item){
        boolean existed = false;
        
        // If the item eists in the bag, say the player used the item. Otherwise say the player does not have it.
        if (!bag.exists(item)){
            System.out.println("You don't have that item.");
        }else{
            System.out.println("You used your "+item+"...");
        }
        
        /*
         * Check the name of the item used, if it exists in the bag, run the relevant methods and remove the item.
         */
        if (item.equals("medipack")){
            if (bag.exists("medipack")){
                modifyHealth(8);
                currentStats.playerHealed();
                bag.removeItem(item);
            }
        }
        
        if (item.equals("key")){
            if (bag.exists("key")){
                if(game.getRoom().hasChest()){
                    game.getRoom().openChest();
                    bag.removeItem(item);
                }else{
                    System.out.println("There's nothing to use that on.");
                }
            }
        }
        
        if (item.equals("macbook")){
            if (bag.exists("macbook")){
                System.out.println("There's nothing to use on that.");
                bag.removeItem(item);
            }
        }
    }
   
    
    /* 
     * Print out available weapons as a string
     */
    public void weapons(){
        System.out.println(bag.getWeaponsAsString());
    }
    
    /*
     * Print out available items as a string
     */
    public void items(){
        if (bag.hasItems()){
            System.out.println(bag.getItemsAsString());
        }
        else{
            System.out.println("You have no items.");
        }
    }
    
    /*
     * This command is only used with the health station, check if we're in the start room and heal, otherwise say there is no station here.
     */
    public void heal(){
        if(game.getRoom().isStartRoom()){
            currentStats.actionMade();
            modifyHealth(20);
            currentStats.playerHealed();
        }else{
            System.out.println("There is no station here.");
        }
    }
    
    /*
     * Shorthand for modyfying health using the currentStats class
     */
    public void modifyHealth(int num){
        currentStats.modifyHealth(num);
    }
    
    /*
     * If the player has launch codes and are in the end room, they can end the game.
     * Print a string based on how many launch codes they own, or if they're not in the end room.
     */
    public void launch(){
        if(game.getRoom().isEndRoom()){
            currentStats.actionMade();

            if(bag.exists("launch_codes")){
                if(bag.getItem("launch_codes").getNumber() == 2){
                    System.out.println("YOU LAUNCHED THE NSE! With a loud explosion, all slimes within the vacinity were eliminated.\n...along with the university.");
                    game.victory();
                }
                if(bag.getItem("launch_codes").getNumber() == 1){
                    System.out.println("You try to use the launch codes but it looks like you're missing half of them...");
                }
            }else{
                System.out.println("You can't launch the NSE without the codes!");
            }
        }else{
            System.out.println("You can't do that here.");
        }
    }
    
    /*
     * Return if the player has evaded
     */
    public boolean evade(){
        if (currentWeapon.hasEvasion()){
            return true;
        }
        return false;
    }
}
