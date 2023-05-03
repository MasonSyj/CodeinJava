package edu.uob;

public class Artefact extends GameEntity{
    public Artefact(String name, String description) {
        super(name, description);
    }

    @Override
    public void add(Location location) {
        location.addArtefact(this);
    }

    @Override
    public GameEntity remove(Location location) {
        return location.getArtefacts().remove(this.getName());
    }
}
