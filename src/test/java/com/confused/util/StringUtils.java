package com.confused.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    public static boolean patternFound(String stringToMatch, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(stringToMatch);
        return m.find();
    }
}