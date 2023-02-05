/* Directions and their x,y deltas. */

enum Direction {
    North, South, East, West;

    int dx() {
        switch (this) {
            case East: return 1;
            case West: return -1;
        }
        return 0;
    }

    int dy() {
        switch (this) {
            case South: return 1;
            case North: return -1;
        }
        return 0;
    }

    public static void main(String[] args) {
        assert(North.dx() == 0);
        assert(North.dy() == -1);
    }
}
