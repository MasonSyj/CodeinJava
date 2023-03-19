package edu.uob;

import java.util.*;

public class ConditionTest {

    static String[] operator = {"==" , ">" , "<" , ">=" , "<=" , "!=" , "LIKE", "like"};

    Stack<String> op;

    public static List<String> convertToSuffix(List<String> tokens) {
//        StringBuilder suffix = new StringBuilder();
        String suffix = "";

        Deque<String> stack = new ArrayDeque<>();

        for (String token : tokens) {
            if (isOperand(token)) {
//                suffix.append(token).append("\t");
                suffix = suffix + token + "\t";
            } else if (token.equals("(")) {
                stack.push(token);
            } else if (token.equals(")")) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
//                    suffix.append(operatorStack.pop()).append("\t");
                    suffix = suffix + stack.pop() + "\t";
                }

                if (!stack.isEmpty() && stack.peek().equals("(")) {
                    stack.pop();
                }
            } else {
//                while (!operatorStack.isEmpty() && precedence(token) <= precedence(operatorStack.peek())) {
//                suffix.append(operatorStack.pop()).append("\t");
                while (!stack.isEmpty() && opcmp(token, stack.peek())) {
                    suffix = suffix + stack.pop() + "\t";
                }

                stack.push(token);
            }
        }

        while (!stack.isEmpty()) {
            suffix = suffix + stack.pop() + " ";
        }

        return Arrays.stream(suffix.toString().trim().split("\t")).toList();
    }

    private static int precedence(String operator) {
        if (operator.equals("and")) {
            return 2;
        } else if (operator.equals("or")) {
            return 1;
        } else {
            return 0;
        }
    }

    private static boolean opcmp(String op1, String op2){
        if (op2.equals("and")){
            return true;
        }else {
            if (!op1.equals("and")){
                return true;
            }else{
                return false;
            }
        }
    }

    private static boolean isOperand(String token) {
        try{
            int value = Integer.parseInt(token);
        }catch (Exception e){
            return false;
        }

        return true;
//        return !token.equals("and") && !token.equals("or") && !token.equals("(") && !token.equals(")");
    }


    public static void main(String[] args) {
        String str = "((Brand like Apple or Price >= 7000) or Name like Mate40)";
        List<String> tokens = Token.setup(str);
        for (int i = 0; i < tokens.size(); i++){
            for (String specialChar: ConditionTest.operator){
                if (tokens.get(i).equals(specialChar)){
                    String singleCondition = tokens.get(i - 1) + " " + tokens.get(i) + " " + tokens.get(i + 1);
                    tokens.remove(i);
                    tokens.remove(i);
                    tokens.set(i - 1, singleCondition);
                }
            }
        }

        for (String string: tokens){
            System.out.println(string);
        }

        System.out.println("--------Separate Line-----------");

    }

    public static List<String> conditionExecute(List<String> conditions, Table t){
        List<String> res = ConditionTest.convertToSuffix(conditions);


        Stack<List<String>> stack = new Stack<>();
        for (int i = 0; i < res.size(); i++){
            if (res.get(i).toLowerCase().equals("and") || res.get(i).toLowerCase().equals("or")){

                List<String> itema = stack.pop();
                List<String> itemb = stack.pop();
                List<String> ansItem;

                if (res.get(i).toLowerCase().equals("and")){
                    ansItem = t.and(itema, itemb);
                }else{
                    ansItem = t.or(itema, itemb);
                }
                stack.add(ansItem);

            }else{
                List<String> curres = t.predicate(res.get(i));
                stack.add(curres);
            }
        }

        return stack.get(0);
    }

}
