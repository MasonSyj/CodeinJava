package edu.uob;

import java.util.Set;

public class Location extends GameEntity{

    private Set<Location> exits;
    private Set<Character> characters;
    private Set<Artefact> artefacts;
    private Set<Furniture> furnitures;
    private Set<Player> players;


    public void addArtefact(Artefact artefact){
        artefacts.add(artefact);
    }
    public void addFurniture(Furniture furniture) {
        furnitures.add(furniture);
    }

    public void addCharacter(Character character) {
        characters.add(character);
    }

    public void addPlayer(Player player) {
        players.add(player);
    }
    public Location(String name, String description) {
        super(name, description);
    }

    public String showInformation() {
        return exits.toString() + artefacts.toString() + furnitures.toString() + characters.toString() + players.toString();
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
