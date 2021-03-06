/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2011.08.08
 */

import java.util.HashMap;

public class Game 
{
    private Parser parser;
    private Room currentRoom;
    private Player player;
    private Stats stats;
    private boolean hasEnded;
        
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        parser = new Parser();
        player = new Player(this);
        stats = player.getStats();
        hasEnded = false;
        createRooms();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room outside, theater, pub, lab, office, ass, assnse, change, wall;
      
        // create the rooms
        outside = new Room("outside the main entrance of the university\n", true, stats);
        theater = new Room("in a lecture theater\n", true, stats);
        pub     = new Room("in the campus pub\n", true, stats);
        lab     = new Room("in a computing lab\n", true, stats);
        office  = new Room("in the computing admin office\n", true, stats);
        ass     = new Room("in the Aston Slime Slayer lounge.\n" +
                           "Type 'station' to fully heal here.\n", false, stats);
        wall    = new Room("to the Aston University slime barricade.\n", true, stats);
        assnse  = new Room("in the Nuclear Slime Exterminator launch room.\n" +
                           "If you have two set of launch codes, type 'launch' to end the game.\n", false, stats);
        change  = new Room("in the theater dressing rooms.\n", true, stats);
        
        // initialise room exits
        outside.setExit("east", theater);
        outside.setExit("south", lab);
        outside.setExit("west", pub);
        outside.setExit("north", wall);
        wall.setExit("south", outside);
        theater.setExit("west", outside);
        theater.setExit("north", change);
        change.setExit("south", theater);
        pub.setExit("east", outside);
        lab.setExit("down", ass);
        lab.setExit("north", outside);
        lab.setExit("east", office);   
        ass.setExit("up", lab);
        ass.setExit("east", assnse);
        ass.startRoom();
        assnse.setExit("west", ass);
        assnse.endRoom();
        office.setExit("west", lab);
        
        // add weapons and items to rooms
        pub.addItem("key", 1);
        wall.addItem("key", 1);
        theater.addItem("medipack", 1);
        lab.addItem("macbook", 1);
        change.giveChest();
        office.giveChest();
        
        assnse.addWeapon("pistol", 2, "evasion");
        pub.addWeapon("rifle", 5, "evasion");
        office.addWeapon("mallet", 3, "none");
        change.addWeapon("shotgun", 3, "spread");
        

        currentRoom = ass;  // start game outside
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("SLIMES HAVE INVADED!");
        System.out.println("It’s your job as a member of the newly formed Aston Slime Slayers ");
        System.out.println("union to eliminate slimes at all costs. ");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;     
        
        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        
        if(!hasEnded){
            if (commandWord.equals("help")) {
                printHelp();
            }
            else if (commandWord.equals("go")) {
                stats.actionMade();
                getRoom().takeAction(player.evade());
                goRoom(command);
            }
            else if (commandWord.equals("take")){
                getRoom().takeAction(player.evade());
                player.take();
            }
            else if (commandWord.equals("attack")){
                player.attack( command.getSecondWord() );
                getRoom().takeAction(player.evade());
            }
            else if (commandWord.equals("use")){
                getRoom().takeAction(player.evade());
                player.use( command.getSecondWord() );
            }
            else if (commandWord.equals("items")){
                player.items();
            }
            else if (commandWord.equals("weapons")){
                player.weapons();
            }
            else if (commandWord.equals("station")){
                player.heal();
            }
            else if (commandWord.equals("launch")){
                player.launch();
            }
        }
        
        if (commandWord.equals("quit")) {
                wantToQuit = quit(command);
        }
        // else command not recognised.
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("Cut through the horde of slimes to find the Nuclear Slime");
        System.out.println("Exterminator launch codes.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /** 
     * Try to in to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
            stats.actionMade();
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
    
    public HashMap givePlayer(String list){
        if(list.equals("items")){
            return currentRoom.pickUp("items");
        }
        if(list.equals("weapons")){
            return currentRoom.pickUp("weapons");
        }
        return null;
    }
    
    /*
     * Print out stats and change ended to true
     */
    public void victory(){
        System.out.println();
        stats.printStats();
        hasEnded = true;
    }
    
    /*
     * Print out defeat message and set ended to true
     */
    public void defeat(){
        System.out.println("You can't handle all of these slimes and collapse from exhaustion.\nGAME OVER");
        hasEnded = true;
    }
    
    /*
     * Return the player object
     */
    public Player getPlayer(){
        return player;
    }
    
    /*
     * Get the current room
     */
    public Room getRoom(){
        return currentRoom;
    }
}
