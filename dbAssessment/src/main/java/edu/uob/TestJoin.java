package edu.uob;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestJoin {
    public static void main(String[] args) {
        String ATableStr1 = "Xiaomi 10\tXiaomi\t6000";
        String ATableStr2 = "Mate40\tHuawei\t5000";
        String ATableStr3 = "Iphone 13\tApple\t8000";


        String BTableStr1 = "Xiaomi 10\tChina\t2020";
        String BTableStr2 = "Mate40\tChina\t2021";
        String BTableStr3 = "Iphone 13\tUS\t2022";

        List<String> TableA = Arrays.asList(ATableStr1, ATableStr2, ATableStr3);
        List<String> TableB = Arrays.asList(BTableStr1, BTableStr2, BTableStr3);

        int valueIndexA = 0;
        int valueIndexB = 0;

        List<String> res = new ArrayList<>();

        for (String stra : TableA){
            for (String strb: TableB){
                if (Arrays.stream(stra.split("\t")).toList().get(valueIndexA)
                        .equals(Arrays.stream(strb.split("\t")).toList().get(valueIndexB))){
                    res.add(stra + "\t" + strb);
                    break;
                }
            }
        }

        List<String> newRes = new ArrayList<>();
        for (String resStr: res){
            String[] curItem = resStr.split("\t");
            for (int i = 0; i < curItem.length; i++){
                if (i == valueIndexB){
                    curItem[i] = "";
                    break;
                }
            }
            newRes.add(String.join("\t", curItem));
        }

        for (String resStr: newRes){
            resStr.trim();
            System.out.println(resStr);
        }

    }
}
