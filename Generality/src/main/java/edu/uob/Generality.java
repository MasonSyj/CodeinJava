package edu.uob;

import java.util.*;

public class Generality {

    public static void main(String[] main){
        List<String> arr = new ArrayList<String>(5);
        System.out.println(arr.size());
        for (int i = 0; i < 10; i++){
            arr.add("hello world" + i);
        }
        System.out.println(arr.size());

        List<String> stack = new Stack<String>();
        for (int i = 0; i < 10; i++){
            stack.add("abc");
        }

        //Doesn't work although stack really is a stack.
        /*stack.pop();
        stack.push();*/


        Stack<String> stack2 = new Stack<String>();
        for (int i = 0; i < 10; i++){
            stack2.add("happy world");
        }
        stack2.pop();
        stack2.push("apple");

        for (String e: stack){
            System.out.println(e);
        }

        Queue<String> queue = new PriorityQueue<String>();
        for (int i = 0; i < 10; i++){
            queue.add("cba");
        }

        for (String e: queue){
            System.out.println(e);
        }


        Queue<String> queue2 = new LinkedList<String>();
        for (int i = 0; i < 10; i++){
            queue2.add("sad");
        }


        for (String e: queue2){
            System.out.println(e);
        }



    }

}