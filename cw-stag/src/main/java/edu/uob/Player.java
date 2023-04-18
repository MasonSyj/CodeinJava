package edu.uob;

import java.util.HashMap;
import java.util.Set;

public class Player extends GameEntity{

    private Location currentLocation;

    private HashMap<String, Artefact> inventory;
    public Player(String name, String description) {
        super(name, description);
    }

    public void setCurrentLocation(Location location){
        this.currentLocation = location;
    }

    public String displayInventory(){
        return inventory.toString();
    }

    public void addArtefact(Artefact artefact){
        inventory.put(artefact.getName(), artefact);
    }
}
