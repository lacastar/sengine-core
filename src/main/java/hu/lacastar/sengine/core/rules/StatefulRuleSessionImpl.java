/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.lacastar.sengine.core.rules;

import hu.lacastar.sengine.core.admin.RuleExecutionSetImpl;
import hu.lacastar.sengine.core.rules.engine.Fact;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.naming.OperationNotSupportedException;
import javax.rules.Handle;
import javax.rules.InvalidHandleException;
import javax.rules.InvalidRuleSessionException;
import javax.rules.ObjectFilter;
import javax.rules.RuleExecutionSetMetadata;
import javax.rules.StatefulRuleSession;

/**
 *
 * @author Szenthe László
 */
public class StatefulRuleSessionImpl extends HandleSequenceGenerator implements StatefulRuleSession {

    RuleExecutionSetImpl ruleSet;
    final Map<String, Object> factsByName = new HashMap<>();
    final Map<Handle, String> factsByHandle = new HashMap<>();

    public StatefulRuleSessionImpl(RuleExecutionSetImpl ruleset, Map properties) {
        this.ruleSet = ruleset;

    }

    @Override
    public boolean containsObject(Handle objectHandle)
            throws InvalidRuleSessionException, InvalidHandleException {
        validateRuleSession();
        return factsByHandle.get(objectHandle) != null;

    }

    @Override
    public Handle addObject(Object object)
            throws InvalidRuleSessionException {
        validateRuleSession();
        if (!(object instanceof Fact)) {
            throw new InvalidRuleSessionException("Invalid Object " + object.toString() + " not instance of Fact");
        }
        Fact fact = (Fact) object;
        Handle handle = new HandleImpl(getNextVal());
        factsByName.put(fact.getName(), fact.getValue());
        factsByHandle.put(handle, fact.getName());
        return handle;
    }

    @Override
    public List addObjects(List objList)
            throws InvalidRuleSessionException {
        validateRuleSession();

        ArrayList al = new ArrayList();
        for (Object obj : objList) {
            al.add(addObject(obj));
        }
        return al;
    }

    @Override
    public void updateObject(Handle objectHandle, Object newObject)
            throws InvalidRuleSessionException, InvalidHandleException {
        validateRuleSession();
        if (!(newObject instanceof Fact)) {
            throw new InvalidRuleSessionException("Invalid Object " + newObject.toString() + " not instance of Fact");
        }
        Fact newFact = (Fact) newObject;

        if (!(objectHandle instanceof HandleImpl) || !containsObject(objectHandle)) {
            throw new InvalidHandleException("Internal error, object handle not found: " + objectHandle.toString());
        }

        String name = factsByHandle.put(objectHandle, newFact.getName());
        if (name != null) {
            factsByName.remove(name);
        }
        factsByName.put(newFact.getName(), newFact.getValue());
    }

    @Override
    public void removeObject(Handle handleObject)
            throws InvalidHandleException, InvalidRuleSessionException {
        validateRuleSession();
        if (!(handleObject instanceof HandleImpl)) {
            throw new InvalidHandleException("Internal error, object handle not found: " + handleObject.toString());
        }
        String name = factsByHandle.remove(handleObject);
        if (name != null) {
            factsByName.remove(name);
        }

    }

    @Override
    public List getObjects()
            throws InvalidRuleSessionException {
        validateRuleSession();
        return getObjects(this.ruleSet.resolveObjectFilter());
    }

    @Override
    public List getObjects(ObjectFilter filter)
            throws InvalidRuleSessionException {
        validateRuleSession();

        List al = new ArrayList();
        for (Map.Entry<String, Object> entry : factsByName.entrySet()) {
            
            Map.Entry<String, Object> filtered = (Map.Entry)filter.filter(entry);
            if (filtered != null) {
                al.add(Fact.builder().name(filtered.getKey()).value(filtered.getValue()).build());
            }
        }
        return al;

    }

    @Override
    public List getHandles()
            throws InvalidRuleSessionException {
        validateRuleSession();

        return new ArrayList(factsByHandle.keySet());
    }

    @Override
    public void executeRules()
            throws InvalidRuleSessionException {
        validateRuleSession();
        ruleSet.getEngine().executeAll(factsByName);
    }

    @Override
    public void reset()
            throws InvalidRuleSessionException {
        validateRuleSession();
        factsByHandle.clear();
        factsByName.clear();
        resetSequence();
    }

    @Override
    public Object getObject(Handle handle)
            throws InvalidHandleException, InvalidRuleSessionException {
        validateRuleSession();
        if (!(handle instanceof HandleImpl)) {
            throw new InvalidHandleException("Wrong driver");
        }
        return factsByHandle.get(handle);
    }


    @Override
    public RuleExecutionSetMetadata getRuleExecutionSetMetadata()
            throws InvalidRuleSessionException {
        validateRuleSession();

        return new RuleExecutionSetMetadataImpl(this.ruleSet);
    }

    @Override
    public void release()
            throws InvalidRuleSessionException {
        validateRuleSession();

        reset();
        this.ruleSet = null;
    }

    @Override
    public int getType()
            throws InvalidRuleSessionException {
        validateRuleSession();

        return 0;
    }

    void validateRuleSession()
            throws InvalidRuleSessionException {
        if (this.ruleSet == null) {
            throw new InvalidRuleSessionException("Null RuleExecutionSet");
        }
    }
}
