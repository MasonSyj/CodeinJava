package edu.uob;

// avoid confusion with Java.lang.Character
public class CharacterStag extends GameEntity{

    public CharacterStag(String name, String description) {
        super(name, description);
    }

    @Override
    public void add(Location location) {
        location.addCharacter(this);
    }

    @Override
    public GameEntity remove(Location location) {
        return location.removeCharacter(this.getName());
    }


}
