package edu.uob;

import java.io.File;
import java.io.IOException;

public class FuncTest {
    public static void main(String[] args) throws IOException {


//        File foldertest = new File("subfolder");
//        foldertest.mkdirs();
        File filetest = new File("aFile");
        filetest.createNewFile();
    }
    /*
    Condition examples:
    age > 30;
    (age > 30);
    mark > 60;
    (mark > 60);


    age > 30 AND mark > 60
    age > 30 OR mark > 60
     */
}
