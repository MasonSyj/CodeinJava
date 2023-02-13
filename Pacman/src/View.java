public class View {

    public static void display(Field field){
        for (int j = 0; j < field.row; j++){
            for (int i = 0; i < field.col; i++){
                field.board[j][i].draw();
            }
            System.out.println();
        }
        System.out.println("numOfFoods:" + field.numFoods);
        System.out.println("Points:" + field.points);

        System.out.println("-----------------Separate Line------------------");
    }

}
