package edu.uob;

import java.util.HashSet;
import java.util.Set;

public class GameAction{
    private Set<String> triggers;
    private Set<String> subjects;
    private Set<String> consumables;
    private Set<String> productions;
    private String narration;

    public GameAction() {
        this.triggers = new HashSet<String>();
        this.subjects = new HashSet<String>();
        this.consumables = new HashSet<String>();
        this.productions = new HashSet<String>();
    }

    public void addTrigger(String trigger){
        this.triggers.add(trigger);
    }

    public void addSubject(String subject){
        this.subjects.add(subject);
    }

    public void addConsumable(String consumable){
        this.consumables.add(consumable);
    }

    public void addProduction(String production){
        this.productions.add(production);
    }

    public Set<String> getTriggers() {
        return triggers;
    }

    public Set<String> getSubjects() {
        return subjects;
    }

    public String printSubject(){
        return subjects.stream().sorted((a, b) -> a.compareTo(b)).toList().toString();
    }

    public Set<String> getConsumables() {
        return consumables;
    }

    public Set<String> getProductions() {
        return productions;
    }

    public String getNarration() {
        return narration;
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("trigger: \n");
        for (String str: this.triggers){
            stringBuilder.append(str + " ");
        }

        stringBuilder.append("\nsubjects: \n");
        for (String str: this.subjects){
            stringBuilder.append(str + " ");
        }

        stringBuilder.append("\nconsumables: \n");
        for (String str: this.consumables){
            stringBuilder.append(str + " ");
        }

        stringBuilder.append("\nproductions: \n");
        for (String str: this.productions){
            stringBuilder.append(str + " ");
        }
        return stringBuilder.toString();
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }
}
