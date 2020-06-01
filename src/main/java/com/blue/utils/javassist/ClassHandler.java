package com.blue.utils.javassist;

import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import javassist.*;
import javassist.bytecode.AccessFlag;
import javassist.util.HotSwapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/** wanghan https://github.com/flicck
 * 操作class的类
 * 2020-06-01
 */
public class ClassHandler {
    private static HotSwapper hs;

    static {
        try {
            hs = new HotSwapper(8000);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalConnectorArgumentsException e) {
            e.printStackTrace();
        }
    }

    private CtClass ctClass =null;
    private ClassPool classPool = null;
    private String absoluteClass = null;
    public CtClass getCtClass() {
        return ctClass;
    }

    public ClassHandler initClass(String absoluteClass){
        defrost();
        ClassPool pool = new ClassPool(true);;
        final CtClass ct = pool.makeClass(absoluteClass);
        ctClass = ct;
        classPool = pool;
//        this.loader = loader;
        this.absoluteClass = absoluteClass;
        return this;
    }
    public ClassHandler addField(String type, String fname) throws CannotCompileException {
        defrost();
        final CtClass type1 = getType(type);
        final CtField f = new CtField(type1, fname, getCtClass());
        f.setModifiers(AccessFlag.PUBLIC);
        getCtClass().addField(f);
        return this;
    }
    public ClassHandler removeField(String fname) throws NotFoundException {
        defrost();
        final CtField[] fields = ctClass.getFields();
        for(CtField f:fields){
            if(f.getName().equals(fname)){
                ctClass.removeField(f);
            }
        }
      return this;
    }
    public ClassHandler removeFieldAndGetSet(String fname) throws NotFoundException {
        defrost();
        String setName = "set"+StringUtils.toUpperCaseFirstOne(fname);
        String getName = "get"+StringUtils.toUpperCaseFirstOne(fname);
        removeField(fname);
        removeMethod(setName);
        removeMethod(getName);
        return this;
    }
    public ClassHandler addMethod(String methodStr) throws CannotCompileException {
        defrost();
        final CtMethod make = CtNewMethod.make(methodStr, getCtClass());
        getCtClass().addMethod(make);
        return this;
    }

    /**
     *目前支持移除所有同名方法，后续加入只删除某一个重载方法
     */
    public ClassHandler removeMethod(String mName) throws NotFoundException {
        defrost();
        final CtMethod[] methods = ctClass.getMethods();
        for(CtMethod m:methods){
            if(m.getName().equals(mName)){
                ctClass.removeMethod(m);
            }
        }
        return this;
    }
    //防止类被一个classLoader加载两次，javaassist提供了一个Loader使用，
    //也可以使用包下的HotClassLoader
    //但是drools的import默认使用的是AppClassLoader,故这里还是用默认的加载器防止无法导入到drools中
    //然后使用javaassist的HotSwapper技术
    public Class build() throws CannotCompileException {
        defrost();
//        final HotClassLoader hotClassLoader = new HotClassLoader();
//        final String s = absoluteClass.replaceAll("\\.", "/");
//        final File file = new File(String.format(System.getProperty("user.dir")+"/target/classes/%s.class", s));
//        loader.setObjFile(file);
//        final Class<?> aClass = loader.findClass(absoluteClass);
//        Loader cl = new Loader(classPool);
//        final Class<?> aClass = cl.loadClass(absoluteClass);

        final Class<?> aClass = this.ctClass.toClass();
        return aClass;
    }

    public void rebuild(Class aClass) throws IOException, CannotCompileException{
        hs.reload(aClass.getName(), ctClass.toBytecode());
    }

    public void rebuild(String className) throws IOException,  CannotCompileException {
        hs.reload(className, ctClass.toBytecode());
    }
    public void rebuild() throws IOException,  CannotCompileException {
        if(absoluteClass==null){
            throw new RuntimeException("请调用initClass方法");
        }
        hs.reload(absoluteClass, ctClass.toBytecode());
    }
    public ClassHandler genNoParamConstruct() throws CannotCompileException {
        defrost();
        //添加无参构造体
        final CtConstructor[] constructors = ctClass.getConstructors();
        CtConstructor cons = new CtConstructor(new CtClass[] {}, ctClass);
        cons.setBody("{}");
        ctClass.addConstructor(cons);
        return this;
    }
    public ClassHandler genGetAndSet() throws CannotCompileException {
        defrost();
        final CtField[] fields = ctClass.getFields();
        final CtMethod[] methods = ctClass.getMethods();
        final List<CtMethod> ctMethods = Arrays.asList(methods);
        final List<String> ctMethodNames = ctMethods.stream().map(CtMethod::getName).collect(Collectors.toList());
        for(CtField field:fields){
            final String name = StringUtils.toUpperCaseFirstOne(field.getName());
            String setName = "set"+name;
            String getName = "get"+name;
            if(!ctMethodNames.contains(setName)){
                ctClass.addMethod(CtNewMethod.setter(setName,field));
            }
            if(!ctMethodNames.contains(getName)){
                ctClass.addMethod(CtNewMethod.getter(getName,field));
            }
        }
        return this;
    }
    //写入到本地，根据需要写入到本地
    public ClassHandler dumpToLocal() throws IOException, CannotCompileException {
        defrost();
        byte[] byteArr = ctClass.toBytecode();
        final String s = absoluteClass.replaceAll("\\.", "/");
        final File file = new File(String.format(System.getProperty("user.dir")+"/target/classes/%s.class", s));
        //如果file文件存在就干掉
        boolean delete = false;
        if(file.exists()){
            file.setExecutable(true);
           delete = file.delete();
        }else{
            delete = true;
        }
        if(delete){
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(byteArr);
            fos.close();
        }else{
            throw new RuntimeException("无法删除原有class文件");
        }
        return this;
    }


    private void defrost(){
        if(ctClass!=null && ctClass.isFrozen()){
            ctClass.defrost();
        }
    }
    private CtClass getType(String type) {
        if(type.toLowerCase().equals("string")){
            try {
                return classPool.get("java.lang.String");
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
