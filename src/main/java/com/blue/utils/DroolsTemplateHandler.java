package com.blue.utils;
/** wanghan
 * 对drools规则进行操作的类
 */
public class DroolsTemplateHandler {
    private String expression;

    public DroolsTemplateHandler(String expression) {
        this.expression = expression;
    }

    private void setExpression(String expression) {
        this.expression = expression;
    }

    public String getExpression() {
        wipeAllEL();
        return expression;
    }
    public  DroolsTemplateHandler replacePackage(String packageName){
        this.setExpression(expression.replace("@{Package}",packageName));
        return this;
    }
    public  DroolsTemplateHandler replaceBean(String beanDescription){
        this.setExpression(expression.replace("@{Bean}",beanDescription));
        return this;
    }
    public  DroolsTemplateHandler replaceRuleName(String ruleName){
        this.setExpression(expression.replace("@{RuleName}",ruleName));
        return this;
    }
    public  DroolsTemplateHandler replaceLeft(String leftHand){
        this.setExpression(expression.replace("@{Left}",leftHand));
        return this;
    }
    public  DroolsTemplateHandler replaceRight(String rightHand){
        this.setExpression(expression.replace("@{Right}",rightHand));
        return this;
    }
    public  DroolsTemplateHandler replaceSalience(Integer salience){
        this.setExpression(expression.replace("@{Salience}","salience "+salience.toString()));
        return this;
    }
    private   DroolsTemplateHandler wipeAllEL(){
        this.setExpression(expression.replaceAll("@\\{.*}",""));
        return this;
    }
}
