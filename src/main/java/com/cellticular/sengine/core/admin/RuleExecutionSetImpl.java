/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.lacastar.sengine.core.admin;

import hu.lacastar.sengine.core.rules.engine.Engine;
import hu.lacastar.sengine.core.rules.engine.Rules;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.rules.InvalidRuleSessionException;
import javax.rules.ObjectFilter;
import javax.rules.admin.RuleExecutionSet;
import javax.rules.admin.RuleExecutionSetCreateException;
import javax.script.ScriptException;

/**
 * A Rule execution set implementation
 *
 * @author Szenthe László
 */
public class RuleExecutionSetImpl extends HashProperties
        implements RuleExecutionSet {

    private final Engine engine;
    private final String name;
    private final String description;
    private String uri;
    private String filter;

    /**
     *  Creates a rule execution set identified by it's name
     *
     * @param name Name of the execution set
     * @param description Description of the execution set
     * @param uri A URI used to register this execution set serving as a key for management purposes
     * @param properties Properties of the execution set
     * @param rules The rules to register in the execution set
     * @throws RuleExecutionSetCreateException Thrown if a scripting exception occurs during registration
     */
    public RuleExecutionSetImpl(String name, String description, String uri, Map<String, Object> properties, Rules rules) throws RuleExecutionSetCreateException {
        this.name = name;
        this.description = description;
        this.uri = uri;
        this.engine = new Engine(properties);
        for(Rules.Rule rule : rules.getRules()){
            try {
                engine.initRule(rule);
            } catch (ScriptException ex) {
                Logger.getLogger(RuleExecutionSetImpl.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuleExecutionSetCreateException(ex.getMessage());
            }
        }
    }

    /**
     * Name accessor
     * @return the name of the execution set
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Description accessor
     * @return The description of the execution set
     */
    @Override
    public String getDescription() {
        return this.description;
    }

    /**
     * URI accessor
     * @return The URI of the execution set
     */
    public String getUri() {
        return this.uri;
    }

    void setUri(String uri) {
        this.uri = uri;
    }

    /**
     * Default object filter
     * @param objectFilterClassname the name of the class used for object filtering
     */
    @Override
    public void setDefaultObjectFilter(String objectFilterClassname) {
        this.filter = objectFilterClassname;
    }

    /**
     * Return the default object filter class name
     * @return the class name
     */
    @Override
    public String getDefaultObjectFilter() {
        return this.filter;
    }

    /**
     * Resolve the object filter from its class name
     * @return new instance of the object filter 
     * @throws InvalidRuleSessionException Thrown if the class is not found, or any other exception is encountered during instantiation.
     */
    public ObjectFilter resolveObjectFilter() throws InvalidRuleSessionException {
        if (this.filter == null) {
            return new ObjectFilterImpl();
        }
        try {
            Class c = Class.forName(this.filter);
            return (ObjectFilter) c.newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            throw new InvalidRuleSessionException("Bad object filter", e);
        }
    }

    /**
     * Retrieves a list of rules
     * @return The rules in this execution set
     */
    @Override
    public List getRules() {
        List al = new ArrayList();
        al.addAll(engine.getRules());
        return al;
    }

    /**
     * Return the engine
     * @return The scripting engine hosting this execution set
     */
    public Engine getEngine() {
        return engine;
    }
    
    
}
