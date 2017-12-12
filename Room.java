import java.util.Set;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  For each existing exit, the room 
 * stores a reference to the neighboring room.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2011.08.08
 */

public class Room 
{
    private String description;
    private HashMap<String, Room> exits;        // stores exits of this room.
    private boolean hasEnemies, hasItems, hasWeapons, hasChest, startRoom, newRoom, endRoom;
    private Enemies enemies;                    // stores the enemies contained in the room
    private Stats stats;
    private HashMap<String, Item> items;
    private HashMap<String, Weapon> weapons;

    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * @param description The room's description.
     */
    public Room(String description, boolean enemies, Stats stat) 
    {
        this.description = description;
        stats = stat;
        newRoom = true;
        hasEnemies = enemies;
        hasWeapons = false;
        hasItems = false;
        this.enemies = new Enemies(stats);
        exits = new HashMap<String, Room>();
        items = new HashMap<String, Item>();
        weapons = new HashMap<String, Weapon>();
    }

    /**
     * Define an exit from this room.
     * @param direction The direction of the exit.
     * @param neighbor  The room to which the exit leads.
     */
    public void setExit(String direction, Room neighbor) 
    {
        exits.put(direction, neighbor);
    }

    /**
     * @return The short description of the room
     * (the one that was defined in the constructor).
     */
    public String getShortDescription()
    {
        return description;
    }

    /**
     * Return a description of the room in the form:
     *     You are in the kitchen.
     *     Exits: north west
     * @return A long description of this room
     */
    public String getLongDescription()
    {
        String extras = "";
        
        if(hasChest){extras += "You spot a chest in the corner.\n";}
        if(hasWeapons){extras += "There's a " + weapons.keySet() + ".\n";}
        if(hasItems){extras += "There's some items lying around.\n";}
        
        
        
        return  newRoom() + 
                "You are " + description + extras
                + spawnEnemies() + ".\n" 
                + getExitString();
    }
    
    /*
     * Returns an empty string because we want this to take place before spawning enemies, 
     * but after the description of the area.
     * Checks if there are any present enemies in the room. Takes a boolean called evaded which
     * determines if the attack should happen or not, based on the players condition, or hard set
     * when the function is called.
     */
    public String takeAction(boolean evaded){
        if(enemies.enemiesPresent()){
            if(!evaded){
                System.out.println("The slimes in the room attack.");
                int damage = enemies.attack();
                stats.modifyHealth(-damage);
            }else{
                System.out.println("The slimes attacked, but you narrowly dodge the assault.");
            }
        }
        
        return "";
    }
    
    /*
     * Check if this is a new room. Return if the state as a readable string.
     */
    private String newRoom(){
        if (newRoom == true){
            stats.roomDiscovered();
            newRoom = false;
            return "You haven't been here before.\n";
        }
        
        return "";
    }

    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     * @return Details of the room's exits.
     */
    private String getExitString()
    {
        String returnString = "Exits:";
        Set<String> keys = exits.keySet();
        
        for(String exit : keys) {
            returnString += " " + exit;
        }
        
        return returnString;
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     * @param direction The exit's direction.
     * @return The room in the given direction.
     */
    public Room getExit(String direction) 
    {
        return exits.get(direction);
    }
    
    /*
     * Adds an items to the rooms items
     */
    public void addItem(String name, int number){
        hasItems = true;
        items.put(name, new Item(name, number));
    }
    
    /*
     * Adds a weapon to the rooms weapons
     */
    public void addWeapon(String name, int damage, String property){
        hasWeapons = true;
        weapons.put(name, new Weapon(damage, property));
    }
    
    /*
     * Return the relevant hashmap for the room items.
     */
    public HashMap pickUp(String name){
        if (name.equals("items")){
            hasItems = false;
            return items;
        }else if(name.equals("weapons")){
            hasWeapons = false;
            return weapons;
        }
        return null;
    }
    
    public boolean hasItems(){
        return hasItems;
    }
    
    public boolean hasWeapons(){
        return hasWeapons;
    }
    
    /*
     * Spawn enemies in the room every time you enter. Checks if the room has enemies enabled,
     * if it does, add enemies. addEnemies returns true if enemies already exist, and false if they
     * do not. This is used to determine the output.
     */
    private String spawnEnemies(){
        if(hasEnemies == true){
            if(enemies.addEnemies()){
                return "\nThe slimes multiplied, they're getting out of control!";
            }else if(enemies.enemiesPresent()){
                return "\nThere are slimes around, you're on guard.";
            }
        }
        return "\nLooks like it's safe from the slimes";
        
    }
    
    /*
     * Return the enemies object
     */
    public Enemies getEnemies(){
        return enemies;
    }
    
    /*
     * Returns if the room has enemies as a boolean
     */
    public boolean hasEnemies(){
        if (enemies.enemiesPresent()){
            return false;
        }
        return true;
    }
    
    /*
     * Sets room as a start room
     */
    public void startRoom(){
        startRoom = true;
    }
    
    /*
     * Sets room as an end room
     */
    public void endRoom(){
        endRoom = true;
    }
    
    /*
     * Returns if the room is a start room
     */
    public boolean isStartRoom(){
        return startRoom;
    }
    
    /*
     * Returns if the room is an end room
     */
    public boolean isEndRoom(){
        return endRoom;
    }
    
    /*
     * Give the room a chest
     */
    public void giveChest(){
        hasChest = true;
    }
    
    /*
     * Return if the room has a chest
     */
    public boolean hasChest(){
        return hasChest;
    }
    
    /*
     * When this method is called add the launch_codes item to the item list, set the chest state
     * to false and print a string for the user.
     */
    public void openChest(){
        addItem("launch_codes", 1);
        System.out.println("You open the chest and see some launch codes.");
        hasChest = false;
    }
}
