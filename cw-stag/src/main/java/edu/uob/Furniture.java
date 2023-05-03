package edu.uob;

public class Furniture extends GameEntity{
    public Furniture(String name, String description) {
        super(name, description);
    }

    @Override
    public void add(Location location) {
//        location.getFurnitures().put(this.getName(), this);
        location.addFurniture(this);
    }

    @Override
    public GameEntity remove(Location location) {
        return location.getFurnitures().remove(this.getName());
    }
}
