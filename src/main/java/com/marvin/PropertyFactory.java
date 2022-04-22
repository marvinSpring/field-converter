package com.marvin;

import java.util.List;
import java.util.stream.Collectors;

public class PropertyFactory {

    private static String doReplaceJavaObjectPropertiesToDataBaseField(String property, int length) {
        if (length > 0) {
            String upper = null;
            Character c = null;
            if (Character.isUpperCase(property.charAt(length - 1))) {
                c = property.charAt(length - 1);
                upper = "_" + String.valueOf(c).toLowerCase();
            }
            property = doReplaceJavaObjectPropertiesToDataBaseField(upper == null ?property:property.replace(String.valueOf(c), upper), length - 1);
        }
        return property;
    }

    public static String replaceJavaObjectPropertiesToDataBaseField(String property) {
        if (null==property||property.length()==0){
            return property;
        }
        return doReplaceJavaObjectPropertiesToDataBaseField(property, property.length());
    }

    public static String replaceDataBaseFieldToJavaObjectProperties(String filed) {
        StringBuilder filedSb = new StringBuilder(filed);
        StringBuilder filedSbTemp = new StringBuilder();
        for (int i = 0; i < filedSb.length(); i++) {
            if (filedSb.charAt(i)=='_'){
                char c = filedSb.charAt(i);
                filedSb = new StringBuilder(filedSb.toString().replace(String.valueOf(c), "\n"));

            }
        }
        List<String> filedList = filedSb.toString().lines().skip(1L).collect(Collectors.toList());
        String first = filedSb.toString().lines().findFirst().get();
        for (String one : filedList) {
            char toUpper = one.charAt(0);
            filedSbTemp.append(one.replaceFirst(String.valueOf(toUpper), String.valueOf(Character.toUpperCase(toUpper))));
        }
        return first+filedSbTemp;
    }

}
