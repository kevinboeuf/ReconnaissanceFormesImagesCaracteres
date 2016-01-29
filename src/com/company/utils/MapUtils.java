package com.company.utils;

import java.util.Map;

public class MapUtils {

    public static <K, V extends Number & Comparable<V>> K getKeyOfMaximum(Map<K, V> map) {
        K maxKey = null;
        Number  a = 12;
        V maxValue = (V) new Integer(0);
        for(Map.Entry<K,V> entry : map.entrySet()) {
            if(maxKey == null) {
                maxKey = entry.getKey();
            }
            if(entry.getValue().compareTo(maxValue) > 0) {
                maxValue = entry.getValue();
                maxKey = entry.getKey();
            }
        }
        return maxKey;
    }

}
