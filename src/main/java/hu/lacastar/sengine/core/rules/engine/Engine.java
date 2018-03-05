/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.lacastar.sengine.core.rules.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
//import jdk.nashorn.api.scripting.NashornScriptEngine;

/**
 *
 * @author Szenthe László
 */
public class Engine {

    public static final String PROPERTY_ENGINE = "ENGINE";
    public static final String PROPERTY_CONSTANTS = "CONSTANTS";
    
    public static final String COSTANT_PARAMS = "constantParams";
    public static final String FACT_PARAMS = "factParams";
    
    
    static final String FUNCTION_PREFIX = " var ";
    //static final String FUNCTION_PREFIX_VERSION = "_v";
    static final String FUNCTION_PREFIX_2 = " = function(" + COSTANT_PARAMS + ", " + FACT_PARAMS +  ") { ";
    static final String FUNCTION_POSTFIX = " }; ";
    
    ScriptEngine engine;
    Invocable invocable;
    final Map<String, Rule> ruleMap = new HashMap<>();
    final List<Rule> rules = new ArrayList<>();
    final Map<String, Object> constantProperties;

    public Engine(Map<String, Object> properties) {
        String engineName = "nashorn";
        if (properties != null) {
            String engineNameProp = (String)properties.get(PROPERTY_ENGINE);
            if (engineNameProp != null && !engineNameProp.isEmpty()) {
                engineName = engineNameProp;
            }
            constantProperties = (Map<String, Object>)properties.get(PROPERTY_CONSTANTS);
        }else{
            constantProperties = null;
        }

        engine = new ScriptEngineManager().getEngineByName(engineName);
        invocable = (Invocable) engine;
    }

    public void initRule(Rules.Rule rule) throws ScriptException {
        String code = FUNCTION_PREFIX + rule.getName() + FUNCTION_PREFIX_2 + rule.getCode() + FUNCTION_POSTFIX;
        engine.eval(code);
        Rule newRule = new Rule(rule);
        Rule oldRule = ruleMap.put(rule.getName(), newRule);
        if (oldRule != null) {
            rules.remove(oldRule);
        }
        rules.add(newRule);
    }

    public List<Rule> getRules() {
        return Collections.unmodifiableList(rules);
    }

    public Object execute(String name, Map<String, Object> parameters) {
        try {
           return invocable.invokeFunction(name, constantProperties, parameters);
        } catch (ScriptException | NoSuchMethodException ex) {
            Logger.getLogger(Engine.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void executeAll(Map<String, Object> parameters) {
        try {
            for(Rule rule:rules){
                invocable.invokeFunction(rule.name, constantProperties, parameters);
            }
        } catch (ScriptException | NoSuchMethodException ex) {
            Logger.getLogger(Engine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public static class Rule {

        private String name;
        private String description;

        public Rule(Rules.Rule rule) {
            this.name = rule.getName();
            this.description = rule.getDescription();
        }

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name the name to set
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * @return the description
         */
        public String getDescription() {
            return description;
        }

        /**
         * @param description the description to set
         */
        public void setDescription(String description) {
            this.description = description;
        }
    }
}
