public class Runner {
    public static void main (String [] args){
        Attack a1 = new Rock();
        Attack a2 = new Scissors();
        Attack a3 = new Paper();

        a1.handle(a3);
    }
}
