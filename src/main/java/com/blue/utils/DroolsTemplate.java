package com.blue.utils;
/** wanghan
 * drools规则模板
 */
public class DroolsTemplate {
    public static String common ="package @{Package};\n" +
            "global org.slf4j.Logger logger\n"+
            "@{Bean}\n" +
            "rule \"@{RuleName}\"\n" +
            "@{Salience}\n" +
            "\n" +
            "    when\n" +
            "        @{Left}\n" +
            "    then\n" +
            "        @{Right}\n" +
            "end";
}
