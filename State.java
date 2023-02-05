/* The state of a game holds the active entity (the player) and a counter to
hold (say) the score. The class is generic to avoid a direct dependency on the
Entity class. TODO: Make the class more general by having a list of active
entities, and a collection of counters. */

class State<Entity> {
    private Entity entity;
    private int counter;

    Entity entity() { return entity; }

    void entity(Entity e) { entity = e; }

    int counter() { return counter; }

    void counter(int c) { counter = c; }

    public static void main(String[] args) {
        State<String> s = new State<>();
        String e = "e";
        s.entity(e);
        assert(s.entity() == e);
    }
}
