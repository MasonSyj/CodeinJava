package edu.uob;

import java.util.Scanner;

public class weekone {

   public static int numofdigits(int num){
       if (num < 0){
           return numofdigits(num * -1);
       }
       int res = 0;
       while (num > 0){
           num /= 10;
           res++;
       }
       return res;
   }

   public static void main(String[] args){
       Scanner in = new Scanner(System.in);
       System.out.println("Input a number and its nums of digits is: ");
       int num = in.nextInt();
       int res = numofdigits(num);
       System.out.println(res);

       System.out.println("-----------------");
       System.out.println("You typed " + args.length + "variables");
       for (int i = 0; i < args.length; i++){
           System.out.println(i + ": " + args[i]);
       }

       System.out.println("-----------------");
       System.out.println("Input a number.");
       int choice = in.nextInt();

       switch (choice){
           case 1:
               System.out.println("1");
               break;
           case 2:
               System.out.println("2"); break;
           default:
               System.out.println("Others"); break;
       }


       System.out.println("-----------------");

       boolean floatcmp = 10 / 3 >= 3;

       if (floatcmp == true){
           System.out.println("10 / 3 >= 3");
       }else{
           System.out.println("10 / 3 <= 3");
       }

       int x = 10 / 3;
       double y = (10 / 3);
       System.out.println("x: " + x + ", y: " + y);

       System.out.println("-----------------");

       System.out.println("Pls input foot and inch Separately.");
       int foot = in.nextInt();
       int inch = in.nextInt();
       System.out.println("Your height is" + (foot + inch / 12) * 0.3048 + "m");

       System.out.println("-----------------");

       System.out.println("Input your Grade");
       int grade = in.nextInt();
       System.out.print("Here's your evaluation: ");
       if (grade < 50){
           System.out.println("Failed");
       }else if (grade < 60){
           System.out.println("Pass");
       }else if (grade < 70){
           System.out.println("Credit");
       }else{
           System.out.println("Distinct");
       }

       System.out.println("-----------------");

       System.out.println("Input a String");
       System.out.println(in.nextLine());
   }
}

