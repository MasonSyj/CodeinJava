package edu.uob;

public class Entity {
    int x;
    int y;

    State<Entity> state;
    Grid<Entity> grid;

    void setGrid(Grid<Entity> g){
        grid = g;
    }

    void setState(State<Entity> s){
        state = s;
    }

    public static void main(String[] args){
        Entity entity1 = new Entity<String>();
        State<String> s = new State<String>();
        Grid<String> g = new Grid<String>(4, 5);
        entity1.setGrid(g);
        entity1.setState(s);
    }
}
