public class Room {
    public String description;
    public Room northExit;
    public Room southExit;
    public Room eastExit;
    public Room westExit;

    public Room(String description){
        this.description = description;
    }

    public void setExits(Room northExit, Room southExit, Room eastExit, Room westExit){
        this.northExit = northExit;
        this.southExit = southExit;
        this.eastExit = eastExit;
        this.westExit = westExit;
    }

    @Override
    public String toString(){
        return this.description;
    }
}
