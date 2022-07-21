package com.marvin;

import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

public class PropertyFactory {

    private static String doReplaceJavaObjectPropertiesToDataBaseField(String property, int length) {
        if (length > 0) {
            String upper = null;
            Character c = null;
            if (Character.isUpperCase(property.charAt(length - 1))) {
                c = property.charAt(length - 1);
                if (length==1){
                    upper = String.valueOf(c).toLowerCase();
                }else {
                    upper = "_" + String.valueOf(c).toLowerCase();
                }
            }
            property = doReplaceJavaObjectPropertiesToDataBaseField(upper == null ? property : property.replace(String.valueOf(c), upper), length - 1);
        }
        return property;
    }

    public static String replaceJavaObjectPropertiesToDataBaseField(String property) {
        if (null == property || property.length() == 0) {
            return property;
        }
        property = doReplaceJavaObjectPropertiesToDataBaseField(property, property.length());
        return afterCompleted(property);
    }

    /**
     * replace property first element -> lower ,example _user_action -> user_action
     * @param property property
     * @return after {@link #replaceJavaObjectPropertiesToDataBaseField(String property)} replace first element
     */
    private static String afterCompleted(String property) {
        if (StringUtils.isEmpty(property)){
            return property;
        }
        char c = property.charAt(0);
        char underLine = '_';
        if (c==underLine){
            String beforeProperty = property.replaceFirst("_", "");
            char[] chars = beforeProperty.toCharArray();
            char c1 = beforeProperty.charAt(0);
            char c2 = Character.toLowerCase(c1);
            chars[0] = c2;
            return String.valueOf(chars);
        }
        return property;
    }

    public static String replaceDataBaseFieldToJavaObjectProperties(String filed) {
        StringBuilder filedSb = new StringBuilder(filed);
        StringBuilder filedSbTemp = new StringBuilder();
        for (int i = 0; i < filedSb.length(); i++) {
            if (filedSb.charAt(i) == '_') {
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
        return first + filedSbTemp;
    }

}
