package edu.uob;

public class Test {
    public static void main(String[] args) {
        double x = 43.5;
        double y = 33.9;

        String strx = "43.5";
        String stry = "33.9";
        if (Double.valueOf(strx) > Double.valueOf(stry)){
            System.out.println("strx > stry");
        }

        strx = "6";
        stry = "9.5";
        stry = "9.5";

        if (Double.valueOf(strx) > Double.valueOf(stry)){
            System.out.println("strx > stry");
        }else{
            System.out.println("3: stry > strx");
        }

    }
}
