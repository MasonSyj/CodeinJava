public class Purchaser {
    private static int nextId = 1;

    private int id;
    private String name;
    private int height;
    private int purchaseId;


    private static void increment(){
        nextId++;
    }

    public Purchaser(String name, int height, int purchaseId){
        id = nextId;
        this.name = name;
        this.height = height;
        this.purchaseId = purchaseId;
        increment();
    }

}
