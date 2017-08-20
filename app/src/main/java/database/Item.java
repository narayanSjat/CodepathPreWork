package database;

/**
 * Created by narayan on 8/19/2017.
 */

public class Item {
    private String name;

    public Item () { name=null;}
    public Item(String userName)
    {
        this.name=userName;
    }
    public Item (Item item)
    {
        this.name=item.getName();
    }

    /*
       Returns item name
    */
    public String getName()
    {
        return name;
    }

    /*
       Sets Item name
     */
    public void setName(String userName)
    {
        this.name = userName;
    }

    /*
    * Checks if two Item object are equal
     */
    public boolean equals (Item item)
    {
        return (item.getName()==this.name);
    }
}
