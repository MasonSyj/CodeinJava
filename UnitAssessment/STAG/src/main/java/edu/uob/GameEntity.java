package edu.uob;

public abstract class GameEntity
{
    private String name;
    private String description;

    public GameEntity(String name, String description)
    {
        this.name = name;
        this.description = description;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    @Override
    public String toString(){
        return "{name:" + getName() +  ", description: " + getDescription() + "}";
    }

    public abstract void add(Location location);

    public abstract GameEntity remove(Location location);
}
