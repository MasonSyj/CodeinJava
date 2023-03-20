package edu.uob;

import java.util.*;

public class DeleteCmd extends Command{

    List<String> conditionTokens;
    public DeleteCmd(String DBName, String tableName, List<String> ConditionTokens) {
        super(DBName, tableName);
        this.conditionTokens = ConditionTokens;
    }

    @Override
    public String execute() {
        try{
            System.out.println("before convert to suffix");
            conditionTokens = convertToSuffix(conditionTokens);
            System.out.println("after convert to suffix");
            deleteConditionExecute();
            return "[OK], items deleted successfully";
        }catch (Exception e){
            return "[ERROR], failed to delete items";
        }
    }


    public void deleteConditionExecute(){
        FileDealer fd = new FileDealer(getDBName(), getTableName());
        Table t = fd.file2Table();

        Stack<List<String>> stack = new Stack<>();
        for (int i = 0; i < conditionTokens.size(); i++){
            if (conditionTokens.get(i).toLowerCase().equals("and") || conditionTokens.get(i).toLowerCase().equals("or")){

                List<String> itema = stack.pop();
                List<String> itemb = stack.pop();
                List<String> ansItem;

                if (conditionTokens.get(i).toLowerCase().equals("and")){
                    ansItem = BoolOperation.and(itema, itemb);
                }else{
                    ansItem = BoolOperation.or(itema, itemb);
                }
                stack.add(ansItem);

            }else{
                List<String> curres = t.predicate(conditionTokens.get(i));
                stack.add(curres);
            }
        }

        List<String> toBedeleted = stack.get(0);

        List<String> all = t.getAllItems();

        for (String str: all){
            for (String indexed: toBedeleted){
                if (str.equals(indexed)){
                    all.remove(str);
                }

            }
        }
        System.out.println("all.size(): " + all.size());
        t.updateClass(all);
        t.write2File();
        System.out.println("after delete there are: " + t.getNumofItems());
    }
    public List<String> convertToSuffix(List<String> tokens) {
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

    private int precedence(String operator) {
        if (operator.equals("and")) {
            return 2;
        } else if (operator.equals("or")) {
            return 1;
        } else {
            return 0;
        }
    }

    private boolean isOperand(String token) {
        return !token.equals("and") && !token.equals("or") && !token.equals("(") && !token.equals(")");
    }
}
/*
    public void conditionExecute(){
        List<String> res = ConditionTest.convertToSuffix(conditionTokens);
        for (String string: res){
            System.out.println(string);
        }
        FileDealer fd = new FileDealer(".", "PhoneBrand");
        Table t = fd.file2Table();

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

        System.out.println("stack size: " + stack.size());
        for (String s: stack.get(0)){
            System.out.println(s);
        }
    }
    */
