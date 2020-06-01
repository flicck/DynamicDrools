package com.blue.core;

import com.blue.utils.DroolsTemplate;
import com.blue.utils.DroolsTemplateHandler;
import com.blue.utils.javassist.ClassHandler;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.utils.KieHelper;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.UUID;

/** wanghan https://github.com/flicck
 * 动态插入,动态变化字节码
 * 2020-06-01
 */
public class BlueCore {
    /**需要给jdk打对应版本的dcevm补丁
     * 然后在edit configuration中设置 vm option为：
     * -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8000 -XXaltjvm=dcevm
     * 再启动
     */
    public static void main(String[] args) throws IllegalAccessException, IOException, InstantiationException, NoSuchFieldException, CannotCompileException, ClassNotFoundException, NotFoundException, IllegalConnectorArgumentsException {
        getResult();
    }
    public static void getResult() throws IOException, IllegalAccessException, InstantiationException, NoSuchFieldException, CannotCompileException, ClassNotFoundException, NotFoundException, IllegalConnectorArgumentsException {

        String expression = DroolsTemplate.common;
        final DroolsTemplateHandler handler = new DroolsTemplateHandler(expression);

        final String result = handler
                .setPackage("common")
                .setImport("com.blue.gen.TemplateEntity")
                .setRuleName(UUID.randomUUID().toString())
                .setLeft("$p:TemplateEntity(address==\"test\")")
                .setRight("System.out.println(\"哈哈 执行成功\");")
                .setSalience(100)
                .build();

        System.out.println(result);
        KieHelper helper = new KieHelper();
        helper.addContent(result, ResourceType.DRL);
        final KieSession kieSession = helper.build().newKieSession();

        final ClassHandler classHandler = new ClassHandler();
        final Thread thread1 = Thread.currentThread();
        final ClassLoader contextClassLoader1 = thread1.getContextClassLoader();
        final Class<?> build1 = contextClassLoader1.loadClass("com.blue.gen.TemplateEntity");
        /**对com.blue.gen.TemplateEntity的字节码进行操作，增加name字段*/
         classHandler.initClass("com.blue.gen.TemplateEntity")
                .addField("String", "name")
                .genGetAndSet()
                .genNoParamConstruct()
//                .dumpToLocal()
                .rebuild();
        /**对com.blue.gen.TemplateEntity的字节码再次操作，删除name字段，增加address字段*/
       classHandler.initClass("com.blue.gen.TemplateEntity")
                .addField("String", "address")
                .removeFieldAndGetSet("name")
                .genGetAndSet()
//                .dumpToLocal()
                .rebuild();
        /**实例化com.blue.gen.TemplateEntity，将address字段的值设置为test*/
        final Object o = build1.newInstance();

        final Field f = build1.getDeclaredField("address");
        f.setAccessible(true);
        f.set(o,"test");

        /**触发address字段的值为test的条件，打印 哈哈，执行成功*/
        kieSession.insert(o);
        kieSession.fireAllRules();
        kieSession.dispose();
    }


}
