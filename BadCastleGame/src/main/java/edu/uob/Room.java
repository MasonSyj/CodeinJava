package edu.uob;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Room {
    public String description;
    public Room northExit;
    public Room southExit;
    public Room eastExit;
    public Room westExit;
    public Map<String, Room> exits;

    public Room(String description){
        this.description = description;
    }
   

    public void setExits(List<Room> rooms){
        this.exits = new HashMap<String, Room>();
        for (Room currentRoom: rooms){
            exits.put(currentRoom.toString(), currentRoom);
        }       
    }

    @Override
    public String toString(){
        return this.description;
    }

    public Room goRoom(String direction){
        if (direction.equals("north")){
            return northExit;
        }
        if (direction.equals("south")){
            return southExit;
        }
        if (direction.equals("west")){
            return westExit;
        }
        if (direction.equals("east")){
            return eastExit;
        }
        return null;
    }

    public String displayExits(){
        StringBuilder ans = new StringBuilder();
        if (this.northExit != null){
            ans.append("north ");
        }
        if (this.southExit != null){
            ans.append("south ");
        }
        if (this.westExit != null){
            ans.append("west ");
        }
        if (this.eastExit != null){
            ans.append("east ");
        }

        return ans.toString();
    }
}
