package edu.uob;

import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;

public class Location extends GameEntity{

    private Map<String, Location> exits;
    private Map<String, Character> characters;
    private Map<String, Artefact> artefacts;
    private Map<String, Furniture> furnitures;


    public void addArtefact(Artefact artefact){
        artefacts.put(artefact.getName(), artefact);
    }
    public void addFurniture(Furniture furniture) {
        furnitures.put(furniture.getName(), furniture);
    }

    public void addCharacter(Character character) {
        characters.put(character.getName(), character);
    }

    public void addExit(Location exit){
        exits.put(exit.getName(), exit);
    }
    public Location(String name, String description) {
        super(name, description);
        this.exits = new HashMap<String, Location>();
        this.artefacts = new HashMap<String, Artefact>();
        this.furnitures = new HashMap<String, Furniture>();
        this.characters = new HashMap<String, Character>();
    }


    public String showInformation() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("You current are: " + this.getName());
        stringBuilder.append(" " + this.getDescription());
        if (exits.size() != 0){
            stringBuilder.append(" | exits: ");
            for (Location exist: exits.values()){
                stringBuilder.append(exist.toString());
            }
            stringBuilder.append(" | ");
        }
        // DRY here.
        if (furnitures.size() != 0){
            stringBuilder.append("| furnitures: ");
            for (Furniture furniture: furnitures.values()){
                stringBuilder.append(furniture.toString());
            }
            stringBuilder.append(" | ");
        }
        if (artefacts.size() != 0){
            stringBuilder.append("| artefacts: ");
            for (Artefact artefact: artefacts.values()){
                stringBuilder.append(artefact.toString());
            }
            stringBuilder.append(" | ");
        }

        if (characters.size() != 0){
            stringBuilder.append("| characters: ");
            for (Character character: characters.values()){
                stringBuilder.append(character.toString());
            }
            stringBuilder.append(" | ");
        }
        return stringBuilder.toString();
    }

    public Map<String, Character> getCharacters() {
        return characters;
    }

    public Map<String, Furniture> getFurnitures() {
        return furnitures;
    }

    public Map<String, Artefact> getArtefacts() {
        return artefacts;
    }

    public Map<String, Location> getExits() {
        return this.exits;
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
