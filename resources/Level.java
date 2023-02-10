/* Create a level for the maze game. Instead of reading level data from a file,
it is defined directly as an array. There is thus only one level at present. */
class Level {
    private int w, h;
    private Grid<Entity> g;
    private State<Entity> s;

    // Definition of type of entity in each cell. The natrix uses matrix (r, c)
    // coordinates for intuitive visual effect.
    private static char[][] data = {
        {'#', '#', '#', '#', '#', '#', '#', '#', '#'},
        {'#', '.', '.', '.', '#', '.', '*', '#', '#'},
        {'#', '.', '#', '.', '#', '.', '#', '#', '#'},
        {'#', '.', '#', '.', '.', '.', '.', '.', '#'},
        {'#', '.', '#', '#', '#', '#', '.', '#', '#'},
        {'#', '@', '#', '.', '.', '.', '.', '.', '#'},
        {'#', '#', '#', '#', '.', '#', '#', '.', '#'},
        {'#', '*', '.', '.', '.', '#', '*', '.', '#'},
        {'#', '#', '#', '#', '#', '#', '#', '#', '#'}
    };

    // Initialize the grid of entities.
    Level() {
        h = data[0].length;
        w = data.length;
        g = new Grid<Entity>(w, h);
        s = new State<Entity>();
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                char t = data[y][x];
                build(x, y, t);
            }
        }
        start();
    }

    // Accept a command from the user to go in a given direction.
    void command(Direction d) {
        Entity p = s.entity();
        p.direction(d);
        p.act();
    }

    // Display the maze textually.
    void display() {
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                Entity e = g.get(x, y);
                System.out.print(e.type());
            }
            System.out.println();
        }
    }

    // Check of there are any stars left to find.
    boolean end() { return s.counter() == 0; }

    // Create an entity of a given type at a given grid location.
    private void build(int x, int y, char t) {
        Entity e, ground;
        switch (t) {
            case '#': e = new Wall(); break;
            case '.': e = new Space(); break;
            case '*': e = new Star(); break;
            case '@': e = new Player(); break;
            default: e = null;
        }
        if (t == '*') {
            ground = new Space();
            ground.set(g, s, '.', null, x, y);
        }
        else ground = null;
        e.set(g, s, t, ground, x, y);
    }

    // Ask each entity to act, to give it a chance to initialize the state.
    private void start() {
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                Entity e = g.get(x, y);
                e.act();
            }
        }
    }

    public static void main(String[] args) {
        // TODO: add testing
    }
}
