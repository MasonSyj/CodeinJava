public class Main {
    public static void main(String[] args){
        Field field = new Field(10, 10);
//        View view = new View();
        View.display(field);
        for(int i = 0; i < 20; i++){
            field.move(Direction.toRight);
            field.move(Direction.toDown);
            View.display(field);
        }
    }
}
