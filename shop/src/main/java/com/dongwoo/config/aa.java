package com.dongwoo.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class aa {
    public static String solution(String[] players, String[] callings) {
        int k=1;
        String temp = "";
        List<Integer> list = new ArrayList<Integer>();

//        for (int j = 0; j < callings.length; j++) {
//            k = Arrays.asList(players).indexOf(callings[j]);
//            temp = players[k - 1];
//            players[k - 1] = players[k];
//            players[k] = temp;
//        }

        for (int i = 0; i < callings.length; i++) {
            k = Arrays.asList(players).indexOf(callings[i]);
            list.add(k);
        }

        for (int i = 0; i < list.size(); i++) {
            k=list.get(i);
            temp = players[k - 1];
            players[k - 1] = players[k];
            players[k] = temp;
        }


        return Arrays.toString(players);
    }

    public static void main(String[] args) {
        String[] a = {"mumu", "soe", "poe", "kai", "mine"};
        String[] b = {"kai", "kai", "mine", "mine"};
        String[] c = {"kai","kai"};
        System.out.println(solution(a,c));
        //System.out.println(Arrays.asList(a).indexOf(b[0]));
    }
}
