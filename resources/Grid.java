/* The 2D grid of entities during a game. The class is generic to avoid a
dependency on the Entity class. */

class Grid<Entity> {
    private Entity[][] cells;

    @SuppressWarnings("unchecked")
    Grid(int w, int h) {
        cells = (Entity[][]) new Object[w][h];
    }

    // Get the entity at a particular position.
    Entity get(int x, int y) {
        return cells[x][y];
    }

    void set(int x, int y, Entity e) {
        cells[x][y] = e;
    }

    Entity next(int x, int y, Direction d) {
        return get(x + d.dx(), y + d.dy());
    }

    public static void main(String[] args) {
        Grid<String> g = new Grid<>(3, 3);
        String e = "e";
        g.set(2, 2, e);
        assert(g.get(2, 2) == e);
        System.out.println("Grid class OK");
    }
}
