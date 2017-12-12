import java.util.HashMap;

public class Bag
{
    private HashMap<String, Weapon> weapons;
    private HashMap<String, Item> items;
    
    public Bag()
    {
        weapons = new HashMap<String, Weapon>();
        items = new HashMap<String, Item>();
        
        // put some weapons and items in the players bag
        weapons.put("fists", new Weapon(2, ""));
        items.put("medipack", new Item("medipack", 1));
    }
    
    /*
     * Return the weapons the bag contains in a string
     */
    public String getWeaponsAsString(){
        String output = "";
        
        for(String weapon : weapons.keySet()){
            output += weapon + ", ";
        }
        
        output = output.substring(0, output.length() -2);
        return output;
    }
    
    /*
     * Returns a weapons
     */
    public Weapon getWeapon(String name){
        return weapons.get(name);
    }
    
    /*
     * Adds a weapon to the bag
     */
    public void addWeapon(int damage, String name, String property){
        weapons.put(name, new Weapon(damage, property));
    }
    
    /*
     * Puts a whole list of weapons into the players bag
     */
    public void addToWeaponList(HashMap<String, Weapon> list){
        if(!list.isEmpty()){
            String output = "";
            
            for(String weapon : list.keySet() ){
                System.out.println(weapon);
            }
            
            weapons.putAll(list);
            list.clear();
        }
    }
    
    /*
     * Puts a whole list of items into the players bag
     */
    public void addToItemList(HashMap<String, Item> list){
        if(!list.isEmpty()){
            // create a string for the output
            String output = "";
            
            for(String item : list.keySet() ){
                output += item + " x" + list.get(item).getNumber() + ", ";
                // check if the item already exists, if it does add the number
                // of items to the value
                if(items.get(item) == null){
                    items.put(item, new Item(   list.get(item).getName(), 
                                                list.get(item).getNumber()
                                            ));
                }else{
                    items.get(item).addNumber(list.get(item).getNumber());
                }
            }
            
            // trim the output string
            System.out.println( output.substring(0, output.length() -2) );
            
            // clear the room items
            list.clear();
        }
    }
    
    /*
     * Returns an items name
     */
    public String getItemName(String name){
        return items.get(name).getName();
    }
    
    /*
     * Returns the item object
     */
    public Item getItem(String name){
        return items.get(name);
    }
    
    /*
     * Removes an item from our bag
     */
    public void removeItem(String name){
        if(items.get(name).getNumber() == 1){
            items.remove(name);
        }else{
            items.get(name).addNumber(-1);
        }
    }
    
    /*
     * Gets the items as a string
     */
    public String getItemsAsString(){
        String output = "";
        
        for(String item : items.keySet()){
            output += item + " x" + items.get(item).getNumber() + ", ";
        }
        
        output = output.substring(0, output.length() -2);
        return output;
    }
    
    /*
     * Return if the item exists in our bag
     */
    public boolean exists(String item){
        if (items.get(item) != null){
            return true;
        }
        
        return false;
    }
    
    /*
     * Return if the bag has items
     */
    public boolean hasItems(){
        if (items.isEmpty()){
            return false;
        }
        
        return true;
    }
}
