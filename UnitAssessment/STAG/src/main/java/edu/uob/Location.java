package edu.uob;

import java.util.HashMap;
import java.util.Map;

public class Location extends GameEntity{

    private Map<String, Location> exits;
    private Map<String, CharacterStag> characters;
    private Map<String, Artefact> artefacts;
    private Map<String, Furniture> furnitures;


    public void addArtefact(Artefact artefact){
        artefacts.put(artefact.getName(), artefact);
    }

    public Artefact removeArtefact(String artefactName) {
        return this.artefacts.remove(artefactName);
    }

    public void addFurniture(Furniture furniture) {
        furnitures.put(furniture.getName(), furniture);
    }

    public Furniture removeFurniture(String furnitureName) {
        return this.furnitures.remove(furnitureName);
    }
    public void addCharacter(CharacterStag characterStag) {
        characters.put(characterStag.getName(), characterStag);
    }

    public CharacterStag removeCharacter(String characterName) {
        return this.characters.remove(characterName);
    }

    public void addExit(Location exit){
        exits.put(exit.getName(), exit);
    }

    public Location removeExit(String exitName) {
        return this.exits.remove(exitName);
    }

    public Location(String name, String description) {
        super(name, description);
        this.exits = new HashMap<String, Location>();
        this.artefacts = new HashMap<String, Artefact>();
        this.furnitures = new HashMap<String, Furniture>();
        this.characters = new HashMap<String, CharacterStag>();
    }

    @Override
    public void add(Location location) {
        location.addExit(this);
    }

    @Override
    public GameEntity remove(Location location) {
        return location.removeExit(this.getName());
    }

    public String showInformation() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("You current are: " + this.getName());
        stringBuilder.append(" " + this.getDescription()).append("\n");
        if (exits.size() != 0){
            stringBuilder.append(" | exits: ");
            for (Location exist: exits.values()){
                stringBuilder.append(exist.getName()).append("  ");
            }
            stringBuilder.append(" | \n");
        }
        if (furnitures.size() != 0){
            stringBuilder.append("| furnitures: ");
            for (Furniture furniture: furnitures.values()){
                stringBuilder.append(furniture.toString());
            }
            stringBuilder.append(" | \n");
        }
        if (artefacts.size() != 0){
            stringBuilder.append("| artefacts: ");
            for (Artefact artefact: artefacts.values()){
                stringBuilder.append(artefact.toString());
            }
            stringBuilder.append(" | \n");
        }

        if (characters.size() != 0){
            stringBuilder.append("| characters: ");
            for (CharacterStag characterStag : characters.values()){
                stringBuilder.append(characterStag.toString());
            }
            stringBuilder.append(" | \n");
        }
        return stringBuilder.toString();
    }

    public Map<String, CharacterStag> getCharacters() {
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

    public GameEntity getGameEntity (String name){
        if (getCharacters().get(name) != null) {
            return getCharacters().get(name);
        } else if (getExits().get(name) != null) {
            return getExits().get(name);
        } else if (getFurnitures().get(name) != null) {
            return getFurnitures().get(name);
        } else if (getArtefacts().get(name) != null) {
            return getArtefacts().get(name);
        } else {
            return null;
        }
    }
}
