package edu.uob;

import java.util.*;

public class ConditionDealer {

    static List<String> operator = Arrays.asList("==" , ">" , "<" , ">=" , "<=" , "!=" , "LIKE", "like");

    Stack<String> op;

    public static List<String> convertToSuffix(List<String> tokens) {
        String suffix = "";
        Deque<String> stack = new ArrayDeque<>();

        for (String token : tokens) {
            if (isOperand(token)) {
                suffix = suffix + token + "\t";
            } else if (token.equals("(")) {
                stack.push(token);
            } else if (token.equals(")")) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    suffix = suffix + stack.pop() + "\t";
                }

                if (!stack.isEmpty() && stack.peek().equals("(")) {
                    stack.pop();
                }
            } else {
                if (stack.isEmpty()){
                    stack.push(token);
                }else{
                    String temp = "";
                    while (!stack.isEmpty() && opPrecedence(token) <= opPrecedence(stack.peek())) {
                        temp = stack.pop();
                        suffix = suffix + temp + "\t";
                    }
                    if (temp.equals("(")){
                        stack.push(temp);
                    }
                    stack.push(token);
                }
            }
        }

        while (!stack.isEmpty()) {
            suffix = suffix + stack.pop() + "\t";
        }

        return Arrays.stream(suffix.toString().trim().split("\t")).toList();
    }

    private static int opPrecedence(String operator) {
        if (operator.toLowerCase().equals("and")) {
            return 2;
        } else if (operator.toLowerCase().equals("or")) {
            return 1;
        } else {
            return 0;
        }
    }

    private static boolean isOperand(String token) {
        return !token.toLowerCase().equals("and") && !token.toLowerCase().equals("or") && !token.equals("(") && !token.equals(")");
    }

    public static List<String> conditionExecute(List<String> conditions, Table t){
        List<String> res = ConditionDealer.convertToSuffix(conditions);

        Stack<List<String>> stack = new Stack<>();
        for (int i = 0; i < res.size(); i++){
            if (res.get(i).toLowerCase().equals("and") || res.get(i).toLowerCase().equals("or")){

                List<String> itema = stack.pop();
                List<String> itemb = stack.pop();
                List<String> ansItem;

                if (res.get(i).toLowerCase().equals("and")){
                    ansItem = BoolOperation.and(itema, itemb);
                }else{
                    ansItem = BoolOperation.or(itema, itemb);
                }
                stack.add(ansItem);

            }else{
                List<String> currentItem = t.predicate(res.get(i));
                stack.add(currentItem);
            }
        }
        return stack.get(0);
    }

}
