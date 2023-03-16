package edu.uob;

public class Condition {
    private String attribute;
    private String operator;
    private String value;

    public Condition(String t){
        this.attribute = t.split(" ")[0];
        this.operator = t.split(" ")[1];
        this.value = t.split(" ")[2];
    }
}
