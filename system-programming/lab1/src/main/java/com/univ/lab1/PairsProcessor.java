package com.univ.lab1;

import java.util.ArrayList;
import java.util.List;

public class PairsProcessor implements Processor {

    @Override
    public List<List<String>> process(List<String> words) {
        int dist = 0;
        List<List<String>> result = new ArrayList<>();
        for (int i = 0; i < words.size(); i++) {
            if (words.get(i).length() < dist) {
                break;
            }
            for (int j = i; j < words.size(); j++) {
                if (words.get(j).length() < dist) {
                    break;
                }
                int currentDist = calculateDistance(words.get(i), words.get(j));
                List<String> pair = new ArrayList<>();
                pair.add(words.get(i));
                pair.add(words.get(j));

                if (currentDist > dist) {
                    result.clear();
                    result.add(pair);
                    dist = currentDist;
                } else if (currentDist == dist) {
                    result.add(pair);
                }
            }

        }
        return result;
    }

    private int calculateDistance(String first, String second) {
        int result = 0;
        int len = Math.min(first.length(), second.length());
        for (int i = 0; i < len; i++) {
            if (first.charAt(i) != second.charAt(i)) {
                result++;
            }
        }
        return result;
    }

}
