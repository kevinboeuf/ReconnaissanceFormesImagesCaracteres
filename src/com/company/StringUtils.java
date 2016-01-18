package com.company;

import java.util.List;

public class StringUtils {

    public static String joinEnum(Enum[] enumSource, String seperator) {
        String result = "";
        for (Enum anEnumSource : enumSource) {
            result += anEnumSource.toString() + seperator;
        }
        if(enumSource.length > 0) {
            result = result.substring(0, result.length() - seperator.length());
        }
        return result;
    }

    public static String joinStringArray(List<String> strings, String seperator) {
        String result = "";
        for (String s : strings) {
            result += s + seperator;
        }
        if(strings.size() > 0) {
            result = result.substring(0, result.length() - seperator.length());
        }
        return result;
    }
}
