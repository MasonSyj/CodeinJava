import java.util.ArrayList;
import java.util.List;

class Stag {
    List<Place> places;

    public Stag(){
        places = new ArrayList<Place>();
    }

    void setup() {
        places.add(new Place("house", "You are in the House.\nThere are exists to the garden and the road.\n"));
    }

    void addPlace(Place newPlace){
        places.add(newPlace);
    }

    void run() {
        setup();
        places.get(0).arrive();
    }

    public static void main(String[] args) {
        Stag program = new Stag();
        program.run();
        Place garden = new Place("Garden", "This is garden, you can only be here from house.");
        Place road = new Place("Road", "This is road, you can be here from house or forest");
        Place forest = new Place("Forest", "This is forest, you can be here only from road");

        program.addPlace(garden);
        garden.addExists(program.places.get(0));
        program.addPlace(road);
        road.addExists(program.places.get(0));
        road.addExists(forest);
        program.addPlace(forest);
        forest.addExists(road);

        for (Place place: program.places){
            System.out.println(place.getName() + "'s exits");
            place.printExists();
            System.out.println("----------------------------------");
        }
    }
}