package edu.uob;

import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

public class weekone {
   public static void main(String[] args){
      var hs = new HashSet<Integer>();
      hs.add(5);
      hs.add(7);
      hs.add(9);
      hs.add(15);
      hs.add(26);

      boolean b1 = hs.contains(6);
      boolean b2 = hs.contains(7);
      boolean b3 = hs.contains(15);
      System.out.printf("b1: %b, b2: %b, b3: %b\n", b1, b2, b3);

      System.out.printf("Size before add 5: %d\n", hs.size());
      hs.add(5);
      System.out.printf("Size after add 5: %d\n", hs.size());
      hs.add(6);
      System.out.printf("Size after add 6: %d\n", hs.size());



      Object[] arr = hs.toArray();

   }
}

