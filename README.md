# DynamicDrools
链式调用动态生成rules并传入drools执行的的demo  
 `String expression = DroolsTemplate.common;`    
        `final DroolsTemplateHandler handler = new DroolsTemplateHandler(expression);`    
        `final String result = handler.setBean(bean)`    
                `.setPackage("common")`    
                `.setRuleName(UUID.randomUUID().toString())`    
                `.setLeft("")`    
                `.setRight(right)`    
                `.setSalience(100)`    
                `.getExpression();`
