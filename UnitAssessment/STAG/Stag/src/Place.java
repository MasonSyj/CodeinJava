import java.util.ArrayList;
import java.util.List;

class Place {
    String name, description;
    List<Place> exists;

    public Place(){

    }

    public String getName(){
        return this.name;
    }

    public Place(String name, String description){
        this.name = name;
        this.description = description;
        exists = new ArrayList<Place>();
    }

    void arrive(){
        System.out.println(this.description);
    }

    void addExists(Place newexist){
        exists.add(newexist);
    }

    void printExists(){
        for (Place place: exists){
            System.out.println(place.getName());
        }
    }
}