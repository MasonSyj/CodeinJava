package edu.uob;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Player extends GameEntity{

    private Location currentLocation;

    private Map<String, Artefact> inventory;

    int health;
    public Player(String name, String description) {
        super(name, description);
        inventory = new HashMap<String, Artefact>();
        health = 3;
    }

    @Override
    public void add(Location location) {

    }

    @Override
    public GameEntity remove(Location location) {
        return null;
    }

    public Location getCurrentLocation(){
        return this.currentLocation;
    }

    public Map<String, Artefact> getInventory(){
        return this.inventory;
    }

    public void decreaseHealth(){
        this.health--;
    }

    public void increaseHealth(){
        if (this.health == 3){
            return;
        }
        this.health++;
    }
    public int getHealth(){
        return this.health;
    }

    public void resetHealth(){
        this.health = 3;
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

    public void removeAllArtefact(){
        inventory = new HashMap<String, Artefact>();
    }
}
