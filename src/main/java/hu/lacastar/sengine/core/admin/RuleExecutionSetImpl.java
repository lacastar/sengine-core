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
 *
 * @author Szenthe László
 */
public class RuleExecutionSetImpl extends HasProperties
        implements RuleExecutionSet {

    private final Engine engine;
    private final String name;
    private final String description;
    private String uri;
    private String filter;

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

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    public String getUri() {
        return this.uri;
    }

    void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public void setDefaultObjectFilter(String objectFilterClassname) {
        this.filter = objectFilterClassname;
    }

    @Override
    public String getDefaultObjectFilter() {
        return this.filter;
    }

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
     *
     * @return
     */
    @Override
    public List getRules() {
        List al = new ArrayList();
        al.addAll(engine.getRules());
        return al;
    }

   public Engine getEngine() {
        return engine;
    }
    
    
}
