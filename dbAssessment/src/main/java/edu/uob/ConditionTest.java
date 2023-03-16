package edu.uob;

import java.util.*;

public class ConditionTest {

    static String[] operator = {"==" , ">" , "<" , ">=" , "<=" , "!=" , "LIKE", "like"};

    Stack<String> op;

    public static List<String> convertToSuffix(List<String> tokens) {
        StringBuilder suffix = new StringBuilder();
        Deque<String> operatorStack = new ArrayDeque<>();

        for (String token : tokens) {
            if (isOperand(token)) {
                suffix.append(token).append("\t");
            } else if (token.equals("(")) {
                operatorStack.push(token);
            } else if (token.equals(")")) {
                while (!operatorStack.isEmpty() && !operatorStack.peek().equals("(")) {
                    suffix.append(operatorStack.pop()).append("\t");
                }

                if (!operatorStack.isEmpty() && operatorStack.peek().equals("(")) {
                    operatorStack.pop();
                }
            } else {
                while (!operatorStack.isEmpty() && precedence(token) <= precedence(operatorStack.peek())) {
                    suffix.append(operatorStack.pop()).append("\t");
                }

                operatorStack.push(token);
            }
        }

        while (!operatorStack.isEmpty()) {
            suffix.append(operatorStack.pop()).append(" ");
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

    private static boolean isOperand(String token) {
        return !token.equals("and") && !token.equals("or") && !token.equals("(") && !token.equals(")");
    }


    public static void main(String[] args) {
        String str = "((Brand like Apple and Price >= 7000) or Name like Mate40)";
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

        List<String> res = ConditionTest.convertToSuffix(tokens);
        for (String string: res){
            System.out.println(string);
        }
        FileDealer fd = new FileDealer(".", "PhoneBrand");
        Table t = fd.file2Table();

        Stack<String> stack = new Stack<String>();
        for (int i = 0; i < res.size(); i++){
            if (res.get(i).toLowerCase().equals("and") || res.get(i).toLowerCase().equals("or")){
                AndOr operator;

                String a = stack.pop();
                String b = stack.pop();
                List<String> itema = t.predicate(a);
                List<String> itemb = t.predicate(b);
                List<String> ansItem;

                if (res.get(i).toLowerCase().equals("and")){
                    ansItem = t.and(itema, itemb);
                }else{
                    ansItem = t.or(itema, itemb);
                }
                stack.add

            }else{
                stack.add(res.get(i));
            }
        }
    }

}
