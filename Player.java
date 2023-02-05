/* The player moves onto spaces, is blocked by walls, and picks up stars. */

class Player extends Entity {
    void act() {
        if (state().entity() == null) {
            state().entity(this);
        }
        else {
            Entity e = next();
            if (e instanceof Star) e.mutate();
            if (! (e instanceof Wall)) move();
        }
    }
}
