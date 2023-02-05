/* Create a maze game as an example of game design, with care taken to avoid
cyclic dependencies. Give it a textual user interface rather than graphics. */

import java.util.*;

class Maze {
    void run() {
        Scanner in = new Scanner(System.in);
        Level level = new Level();
        level.display();
        while (! level.end()) {
            char ch = in.nextLine().charAt(0);
            Direction d = null;
            switch (ch) {
                case 'n': d = Direction.North; break;
                case 's': d = Direction.South; break;
                case 'e': d = Direction.East; break;
                case 'w': d = Direction.West; break;
            }
            level.command(d);
            level.display();
        }
    }

    public static void main(String[] args) {
        Maze program = new Maze();
        program.run();
    }
}
