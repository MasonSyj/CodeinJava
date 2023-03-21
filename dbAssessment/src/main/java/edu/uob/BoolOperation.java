package edu.uob;

import java.util.ArrayList;
import java.util.Arrays;
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

    //remove items in a which also appear in b
    public static List<String> AMinusB(List<String> a, List<String> b){
        List<String> res = new ArrayList<String>(a);
        for (String str: a){
            if (b.contains(str)){
                res.remove(str);
            }
        }
        return res;
    }

    public static List<String> join(List<String> a, List<String> b, int indexA, int indexB){
        List<String> res = new ArrayList<String>();
        for (String stra : a){
            for (String strb: b){
                if (Arrays.stream(stra.split("\t")).toList().get(indexA)
                        .equals(Arrays.stream(strb.split("\t")).toList().get(indexB))){
                    res.add(stra + "\t" + strb);
                    break;
                }
            }
        }
        return res;
    }
}
