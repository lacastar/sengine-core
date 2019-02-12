/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cellticular.sengine.core.rules.engine;

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
 * Java-JavaScript bridge class
 * @author Szenthe László
 */
public class Engine {

    /**
     * Property name of Javascript engine
     */
    public static final String PROPERTY_ENGINE = "ENGINE";

    /**
     * Property name of constants
     */
    public static final String PROPERTY_CONSTANTS = "CONSTANTS";
    
    /**
     * Constant parameters name in Javascript
     */
    public static final String COSTANT_PARAMS = "constantParams";

    /**
     * Factparams  name in Javascript
     */
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

    /**
     * Creates a new instance of the Engine with the given properties
     * @param properties properties to use while creating the engine
     */
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

    /**
     * Register a rule in the engine
     * @param rule Rule to register
     * @throws ScriptException on invalid rule
     */
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

    /**
     * Return all rules registered in the engine
     * @return List of rules
     */
    public List<Rule> getRules() {
        return Collections.unmodifiableList(rules);
    }

    /**
     * Execute a rule identified by name with the supported parameters
     * @param name name of the rule to execute
     * @param parameters parameters 
     * @return Object output of the rule
     */
    public Object execute(String name, Map<String, Object> parameters) {
        try {
           return invocable.invokeFunction(name, constantProperties, parameters);
        } catch (ScriptException | NoSuchMethodException ex) {
            Logger.getLogger(Engine.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Execute all rules in the engine with the supported parameters
     * @param parameters parameters
     */
    public void executeAll(Map<String, Object> parameters) {
        try {
            for(Rule rule:rules){
                invocable.invokeFunction(rule.name, constantProperties, parameters);
            }
        } catch (ScriptException | NoSuchMethodException ex) {
            Logger.getLogger(Engine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Internal rule representation
     */
    public static class Rule {

        private String name;
        private String description;

        /**
         * Create from rule
         * @param rule the rule to create the data from
         */
        public Rule(Rules.Rule rule) {
            this.name = rule.getName();
            this.description = rule.getDescription();
        }

        /**
         * Get the name
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * Set the name
         * @param name the name to set
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * Return the description
         * @return the description
         */
        public String getDescription() {
            return description;
        }

        /**
         * Set the description
         * @param description the description to set
         */
        public void setDescription(String description) {
            this.description = description;
        }
    }
}
