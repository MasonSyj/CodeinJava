/* A star adds one to the counter during initialization. */

class Star extends Entity {
    void act() {
        state().counter(state().counter() + 1);
    }
}
