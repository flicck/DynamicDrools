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
    public  DroolsTemplateHandler setPackage(String packageName){
        this.setExpression(expression.replace("@{Package}",packageName));
        return this;
    }
    public  DroolsTemplateHandler setBean(String beanDescription){
        this.setExpression(expression.replace("@{Bean}",beanDescription));
        return this;
    }
    public  DroolsTemplateHandler setRuleName(String ruleName){
        this.setExpression(expression.replace("@{RuleName}",ruleName));
        return this;
    }
    public  DroolsTemplateHandler setLeft(String leftHand){
        this.setExpression(expression.replace("@{Left}",leftHand));
        return this;
    }
    public  DroolsTemplateHandler setRight(String rightHand){
        this.setExpression(expression.replace("@{Right}",rightHand));
        return this;
    }
    public  DroolsTemplateHandler setSalience(Integer salience){
        this.setExpression(expression.replace("@{Salience}","salience "+salience.toString()));
        return this;
    }
    public DroolsTemplateHandler setNoLoop(Boolean noLoop){
        if(noLoop){
            this.setExpression(expression.replace("@{NoLoop}","no-loop true"));
        }
        return this;
    }
    private void wipeAllEL(){
        this.setExpression(expression.replaceAll("@\\{.*}",""));
    }
}
