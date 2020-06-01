package com.blue.utils.javassist;

/** wanghan https://github.com/flicck
 * 将字符串的第一个字母大写
 * 2020-06-01
 */
public class StringUtils {
    public static String toUpperCaseFirstOne(String s){
        if(Character.isUpperCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
    }
}
