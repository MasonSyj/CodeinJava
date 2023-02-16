package edu.uob;

import java.util.*;

public class Pair<K>{
    private K king;

    public Pair(K king){
        this.king = king;
    }

    public K getKing() {
        return king;
    }

    private void setKing(K k) {
        this.king = k;
    }

    public static void main(String[] args){
        Pair<Integer> pair1 = new Pair<Integer>(20);
        pair1.setKing(5);
        System.out.println(pair1.getKing());
    }

}
