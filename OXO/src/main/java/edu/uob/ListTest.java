package edu.uob;

import java.util.ArrayList;
import java.util.List;

public class ListTest {
    public static void main(String[] main) {
        List<List<String>> board;
        board = new ArrayList<List<String>>(5);
        for (int j = 0; j < 5; j++) {
            board.add(new ArrayList<>(5));
            for (int i = 0; i < 5; i++){
                String temp = "";
                board.get(j).add(temp);
            }

        }
    }
}
