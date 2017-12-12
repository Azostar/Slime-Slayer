/*
 * The item class just holds the item name, and the number of those items that we have.
 */

public class Item
{
    private String name;
    private int number;
    
    public Item(String name, int number)
    {
        this.name = name;
        this.number = number;
    }
    
    public String getName(){
        return name;
    }
    
    public int getNumber(){
        return number;
    }
    
    public void addNumber(int number){
        this.number += number;
    }
}
