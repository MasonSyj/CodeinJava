package edu.uob;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BoolOperation {
    public static List<String> or(List<String> a, List<String> b){
        List<String> res = new ArrayList<>();
        for (String str: a){
            res.add(str);
        }
        for (String str: b){
            res.add(str);
        }

        return res.stream().distinct().collect(Collectors.toList());
    }

    public static List<String> and(List<String> a, List<String> b){
        List<String> res = new ArrayList<>();
        for (String str: a){
            if (b.contains(str)){
                res.add(str);
            }
        }

        return res;
    }
}
