package com.blue.core;

import com.blue.utils.DroolsTemplate;
import com.blue.utils.DroolsTemplateHandler;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.utils.KieHelper;

import java.io.IOException;
import java.util.UUID;

/** wanghan
 * 动态插入
 */
public class BlueCore {
    public static void main(String[] args) throws IOException {
        String bean = "declare Person\n" +
                "name:String\n" +
                "age:int\n" +
                "end";
        String right ="Person p = new Person();\n" +
                "        p.setName(\"小白\");\n" +
                "        p.setAge(20);\n" +
                "        insert(p);";

        String expression = DroolsTemplate.common;
        final DroolsTemplateHandler handler = new DroolsTemplateHandler(expression);
        final String result = handler.setBean(bean)
                .setPackage("common")
                .setRuleName(UUID.randomUUID().toString())
                .setLeft("")
                .setRight(right)
                .setSalience(100)
                .build();

        System.out.println(result);
        System.out.println("-------------------------------------------------");

        String left2 = "$p:Person(age>10)";
        String right2 = " System.out.println('哈哈 执行成功');";
        String expression2 = DroolsTemplate.common;
        final DroolsTemplateHandler handler2 = new DroolsTemplateHandler(expression2);
        final String result2 = handler2.setPackage("common")
                .setRuleName(UUID.randomUUID().toString())
                .setLeft(left2)
                .setRight(right2)
                .setSalience(99)
                .build();

        System.out.println(result2);
        KieHelper helper = new KieHelper();
        helper.addContent(result, ResourceType.DRL);
        helper.addContent(result2,ResourceType.DRL);
        final KieSession kieSession = helper.build().newKieSession();
        kieSession.fireAllRules();
        kieSession.dispose();
    }

}
