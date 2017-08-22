package database;

/**
 * Created by narayan on 8/19/2017.
 */

public class Item {
    private String name;
    private int priority;
    private String memo;
    private String date; //TODO: Find a better structure to store date and time
    private String time;
    private boolean isComplete;

    public Item () {}




    public Item (Item item)
    {
        this.name=item.getName();
        this.priority=item.getPriority();
        this.date=item.getDate();
        this.time=item.getTime();
        this.memo=item.getMemo();
    }

    /*
       Accessor Methods
    */
    public String getName()     { return name;    }
    public int getPriority()    { return priority;}
    public String getMemo()     { return memo;    }
    public String getDate()     { return date;    }
    public String getTime()     { return time;    }
    public boolean getComplete() { return isComplete;    }

    /*
       Modifier Methods
     */
    public void setName(String name){ this.name = name;}
    public void setPriority(int priority)      { this.priority= priority;}
    public void setMemo(String memo)           { this.memo=memo;   }
    public void setDate(String date)           { this.date=date;   }
    public void setTime(String time)           { this.time=time;   }
    public void setComplete(boolean status)    { this.isComplete=status;   }

    /*
    * Checks if two Item object are equal (compares content)
     */
    public boolean equals (Item item)
    {
        return (item.getName().equals(this.name)   &&
                item.getPriority()==this.priority  &&
                item.getMemo().equals(this.memo)   &&
                item.getDate().equals(this.date)   &&
                item.getTime().equals(this.time)   &&
                item.isComplete==this.isComplete);
    }
}
