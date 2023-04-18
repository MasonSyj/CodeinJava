package edu.uob;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Player extends GameEntity{

    private Location currentLocation;

    private Map<String, Artefact> inventory;
    public Player(String name, String description) {
        super(name, description);
        inventory = new HashMap<String, Artefact>();
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

    public Artefact removeArtefact(String artefactName){
        if (this.inventory.containsKey(artefactName)){
            return this.inventory.remove(artefactName);
        } else{
            return null;
        }
    }
}
