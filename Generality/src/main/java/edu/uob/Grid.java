package edu.uob;
/* The 2D grid of entities during a game. The class is generic to avoid a
dependency on the Entity class. */


import java.lang.Object.*;

class Grid<Entity> {

    private Entity[][] cells;

    Grid(int w, int h){
        cells = (Entity[][]) new Object[w][h];
    }

    Entity get(int w, int h){
        return cells[w][h];
    }

    void set(int w, int h, Entity newentity){
        cells[w][h] = newentity;
    }

    public Entity[] newArray(int n){
        return (Entity[]) new Object[n];
    }



    public static void main(String[] args) {

    }
}