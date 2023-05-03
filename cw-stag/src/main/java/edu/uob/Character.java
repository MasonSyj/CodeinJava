package edu.uob;

public class Character extends GameEntity{

    public Character(String name, String description) {
        super(name, description);
    }

    @Override
    public void add(Location location) {
        location.addCharacter(this);
    }

    @Override
    public GameEntity remove(Location location) {
        return location.getCharacters().remove(this.getName());
    }


}
