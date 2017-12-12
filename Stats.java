public class Stats
{
    private int actions, weapons, rooms, slimes, healed, health;
    private Game game;
    
    public Stats(Game game)
    {
        actions = 0;
        weapons = 0;
        rooms = 0;
        slimes = 0;
        healed = 0;
        health = 20;
        this.game = game;
    }
    
    /*
     * Modify the players health based on the input.
     */
    public void modifyHealth(int num){
        health += num;
        
        // If we have less than or equal 0 health, end the game.
        if (health <= 0){
            game.defeat();
        }
        
        if (health > 20){
            health = 20;
        }
        
        checkHealth();
    }
    
    /*
     * Prints a string based on the players health stat
     */
    public void checkHealth(){
        if (health == 20){
            System.out.println("You're able to take on the world! [20HP]");
        }else if(health > 12){
            System.out.println("You feel a little rough. ["+health+"HP]");
        }else if(health > 5){
            System.out.println("You feel weak. ["+health+"HP]");
        }else{
            System.out.println("You can't take much more... ["+health+"HP]");
        }
    }
    
    public void actionMade(){
        actions++;
    }
    
    public void weaponPicked(){
        weapons++;
    }
    
    public void roomDiscovered(){
        rooms++;
    }
    
    public void slimeKilled(){
        slimes++;
    }
    
    public void playerHealed(){
        healed++;
    }
    
    public void bigSlimeKilled(){
        slimes += 3;
    }
    
    public int getActions(){
        return actions;
    }
    
    /*
     * Print the the total stats that we have, and generate a score based on the stats.
     */
    public void printStats(){
        String output = "";
        double multiplier = (100.0/actions);
        double score = (((-weapons*100.0) + (-healed*10.0) + (rooms*100.0) + (slimes * 10.0)) * (multiplier));
        String scoreOut = Double.toString(score);
        
        output += "Weapons Taken: " + weapons + "\t\t[" + (-weapons * 100) + "]\n";
        output += "Times Healed: " + healed + "\t\t\t[" + (-healed * 10) + "]\n";
        output += "Rooms Seen: " + rooms + "\t\t\t[" + (rooms * 100) + "]\n";
        output += "Slimes Killed: " + slimes + "\t\t[" + (slimes * 10) + "]\n";
        output += "Actions Made: " + actions + "\t\t[X" + Double.toString(multiplier).substring(0,3) + "]\n";
        output += "\nFINAL SCORE: "+ scoreOut.substring(0,scoreOut.indexOf("."));
        System.out.println(output);
        
    }

}
