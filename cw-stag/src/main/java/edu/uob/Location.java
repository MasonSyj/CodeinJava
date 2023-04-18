package edu.uob;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Location extends GameEntity{

    private HashMap<String, Location> exits;
    private HashMap<String, Character> characters;
    private HashMap<String, Artefact> artefacts;
    private HashMap<String, Furniture> furnitures;
//    private Set<Player> players;


    public void addArtefact(Artefact artefact){
        artefacts.put(artefact.getName(), artefact);
    }
    public void addFurniture(Furniture furniture) {
        furnitures.put(furniture.getName(), furniture);
    }

    public void addCharacter(Character character) {
        characters.put(character.getName(), character);
    }
/*
    public void addPlayer(Player player) {
        players.add(player);
    }

 */
    public Location(String name, String description) {
        super(name, description);
    }

    public String showInformation() {
        return exits.toString() + artefacts.toString() + furnitures.toString() + characters.toString();
    }

    public HashMap<String, Artefact> getArtefacts() {
        return artefacts;
    }
/*
    public void setExits(Set<Location> exits){
       this.exits = exits;
    }

    public void setCharacters(Set<Character> characters){
        this.characters = characters;
    }

    public void setArtefacts(Set<Artefact> artefacts){
        this.artefacts = artefacts;
    }

    public void setFurnitures(Set<Furniture> furnitures){
        this.furnitures = furnitures;
    }
**/
}
