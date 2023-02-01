package edu.uob;

public class Add {
   private int sum = 0;

   public void addall(String[] args){
      for (int i = 1; i < args.length; i++){
         sum += Integer.parseInt(args[i]);
      }
      System.out.println("sum: " + sum);
   }
   public static void main(String[] args){

   }
}