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
        return exits.getOrDefault(direction, null);
    }

    public String displayExits(){
        StringBuilder ans = new StringBuilder();
        for (String exit: exits.keySet()) {
            ans.append(exit);
            ans.append(" ");
        }

        return ans.toString();
    }
}
