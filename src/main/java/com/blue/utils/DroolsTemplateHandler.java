package com.blue.utils;

import org.apache.commons.lang3.StringUtils;

/** wanghan https://github.com/flicck
 * 对drools规则进行操作的类
 * 2020-06-01
 */
public class DroolsTemplateHandler {
    private String expression;

    public DroolsTemplateHandler(String expression) {
        this.expression = expression;
    }

    private void setExpression(String expression) {
        this.expression = expression;
    }

    public String build() {
        wipeAllEL();
        return expression;
    }
    public DroolsTemplateHandler setPackage(String packageName){
        if(StringUtils.isEmpty(packageName)){
            throw new RuntimeException("please set a package name");
        }
        this.setExpression(expression.replace("@{Package}",packageName));
        return this;
    }
    public DroolsTemplateHandler setBean(String beanDescription){
        this.setExpression(expression.replace("@{Bean}",beanDescription+"\n@{Bean}"));
        return this;
    }
    public DroolsTemplateHandler setRuleName(String ruleName){
        if(StringUtils.isEmpty(ruleName)){
            throw new RuntimeException("please set a rule name");
        }
        this.setExpression(expression.replace("@{RuleName}",ruleName));
        return this;
    }
    public DroolsTemplateHandler setLeft(String leftHand){
        this.setExpression(expression.replace("@{Left}",leftHand+"\n@{Left}"));
        return this;
    }
    public DroolsTemplateHandler setRight(String rightHand){
        this.setExpression(expression.replace("@{Right}",rightHand+"\n@{Right}"));
        return this;
    }
    public DroolsTemplateHandler setSalience(int salience){
        this.setExpression(expression.replace("@{Salience}","salience "+salience));
        return this;
    }
    public DroolsTemplateHandler setNoLoop(boolean noLoop){
        if(noLoop){
            this.setExpression(expression.replace("@{NoLoop}","no-loop true"));
        }
        return this;
    }
    public DroolsTemplateHandler setLockOnActive(boolean lockOnActive){
        if(lockOnActive){
            this.setExpression(expression.replace("@{LockOnActive}","lock-on-active true"));
        }
        return this;
    }
    public DroolsTemplateHandler setEnabled(boolean enabled){
        if(!enabled){
            this.setExpression(expression.replace("@{Enabled}","enabled false"));
        }
        return this;
    }
    //Java或Mvel
    public DroolsTemplateHandler setDialect(String dialect){
        if(StringUtils.isEmpty(dialect)){
            throw new RuntimeException("param is illegal");
        }else if(dialect.equals("Java")){
            this.setExpression(expression.replace("@{Dialect}","dialect \"Java\""));
        }else if(dialect.equals("Mvel")){
            this.setExpression(expression.replace("@{Dialect}","dialect \"Mvel\""));
        }else{
            throw new RuntimeException("param is illegal");
        }
        return this;
    }
    public DroolsTemplateHandler setActivationGroup(String group){
        if(StringUtils.isEmpty(group)){
            throw new RuntimeException("please set a activation group");
        }
        this.setExpression(expression.replace("@{ActivationGroup}",String
                .format("activation-group \"%s\"",group)));
        return this;
    }
    public DroolsTemplateHandler setAgendaGroup(String group){
        if(StringUtils.isEmpty(group)){
            throw new RuntimeException("please set a agenda group");
        }
        this.setExpression(expression.replace("@{AgendaGroup}",String
                .format("agenda-group \"%s\"",group)));
        return this;
    }
    public DroolsTemplateHandler setAutoFocus(boolean autoFocus){
        if(autoFocus){
            this.setExpression(expression.replace("@{AutoFocus}","auto-focus true"));
        }
        return this;
    }
    public DroolsTemplateHandler setTimer(String timer){
        this.setExpression(expression.replace("@{Timer}","timer "+timer));
        return this;
    }
    public DroolsTemplateHandler setImport(String importClass){
        this.setExpression(expression.replace("@{Import}","import "+importClass+";\n@{Import}"));
        return this;
    }
    private void wipeAllEL(){
        this.setExpression(expression.replaceAll("@\\{.*}",""));
    }
}
