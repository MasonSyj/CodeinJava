package edu.uob;

public class SingleGen<K>{
    private K king;

    public SingleGen(K king){
        this.king = king;
    }

    public K getKing() {
        return king;
    }

    private void setKing(K k) {
        this.king = k;
    }

    public static void main(String[] args){
        SingleGen<Integer> pair1 = new SingleGen<Integer>(20);
        System.out.println(pair1.getKing());

        pair1.setKing(5);
        System.out.println(pair1.getKing());
    }

}
