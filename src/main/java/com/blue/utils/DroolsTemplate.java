package com.blue.utils;
/** wanghan https://github.com/flicck
 * drools规则模板
 * 2020-06-01
 */
public class DroolsTemplate {
    public static String common ="package @{Package};\n" +
            "@{Import}\n"+
            "global org.slf4j.Logger logger\n"+
            "@{Bean}\n" +
            "rule \"@{RuleName}\"\n" +
            "@{NoLoop}\n" +
            "@{Salience}\n" +
            "@{LockOnActive}\n" +
            "@{Enabled}\n" +
            "@{Dialect}\n" +
            "@{ActivationGroup}\n" +
            "@{AgendaGroup}\n" +
            "@{AutoFocus}\n" +
            "@{Timer}\n" +
            "\n" +
            "    when\n" +
            "        @{Left}\n" +
            "    then\n" +
            "        @{Right}\n" +
            "end";
}
