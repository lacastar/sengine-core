/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cellticular.sengine.core.rules;

import com.cellticular.sengine.core.admin.RuleExecutionSetImpl;
import com.cellticular.sengine.core.rules.engine.Fact;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.rules.Handle;
import javax.rules.InvalidHandleException;
import javax.rules.InvalidRuleSessionException;
import javax.rules.ObjectFilter;
import javax.rules.RuleExecutionSetMetadata;
import javax.rules.StatefulRuleSession;

/**
 *  A stateful rules engine session exposes a stateful rule execution API to an underlying rules engine. 
 *  The session allows arbitrary objects to be added and removed to and from the rule session state. 
 *  Additionally, objects currently part of the rule session state may be updated. 
 *  There are inherently side-effects to adding objects to the rule session state. 
 *  The execution of a RuleExecutionSet can add, remove and update objects in the rule session state. 
 *  The objects in the rule session state are therefore dependent on the rules within the RuleExecutionSet as well as the rule engine vendor's specific rule engine behaviour. 
 *  Handle instances are used by the rule engine vendor to track Objects added to the rule session state. 
 *  This allows multiple instances of equivalent Objects to be added to the session state and identified, even after serialization. 
 * @author Szenthe László
 */
public class StatefulRuleSessionImpl extends HandleSequenceGenerator implements StatefulRuleSession {

    RuleExecutionSetImpl ruleSet;
    final Map<String, Object> factsByName = new HashMap<>();
    final Map<Handle, String> factsByHandle = new HashMap<>();

    /**
     * Creates a session from a ruleset with the supported properties
     * @param ruleset RuleSet in the session
     * @param properties Properties map
     */
    public StatefulRuleSessionImpl(RuleExecutionSetImpl ruleset, Map properties) {
        this.ruleSet = ruleset;

    }

    /**
     * Returns true if the given object is contained within rule session state of this rule session.
     * 
     * @param objectHandle the handle to the target object.
     * @return true if the given object is contained within the rule session state of this rule session.
     * @throws InvalidRuleSessionException on illegal rule session state.
     * @throws InvalidHandleException on invalid handle
     */
    @Override
    public boolean containsObject(Handle objectHandle)
            throws InvalidRuleSessionException, InvalidHandleException {
        validateRuleSession();
        return factsByHandle.get(objectHandle) != null;

    }

    /**
     * Adds a given object to the rule session state of this rule session. 
     * The argument to this method is Object because in the non-managed env. not all objects should have to implement Serializable. 
     * If the RuleSession is Serializable and it contains non-serializable fields a runtime exception will be thrown.
     * @param object the object to be added.
     * @return the Handle for the newly added Object
     * @throws InvalidRuleSessionException on illegal rule session state.
     */
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

    /**
     * Adds a List of Objects to the rule session state of this rule session.
     * @param objList the objects to be added.
     * @return a List of Handles, one for each added Object. The List must be ordered in the same order as the input objList.
     * @throws InvalidRuleSessionException on illegal rule session state.
     */
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

    /**
     * Notifies the rules engine that a given object in the rule session state has changed. 
     * The semantics of this call are equivalent to calling removeObject followed by addObject.
     * The original Handle is rebound to the new value for the Object however.
     * @param objectHandle the handle to the original object.
     * @param newObject the new object to bind to the handle.
     * @throws InvalidRuleSessionException on illegal rule session state.
     * @throws InvalidHandleException if the input Handle is no longer valid
     */
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

    /**
     * Removes a given object from the rule session state of this rule session.
     * @param handleObject the handle to the object to be removed from the rule session state.
     * @throws InvalidHandleException if the input Handle is no longer valid
     * @throws InvalidRuleSessionException on illegal rule session state.
     */
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

    /**
     * Returns a List of all objects in the rule session state of this rule session.
     * The objects should pass the default filter test of the default RuleExecutionSet filter (if present). 
     * This may not neccessarily include all objects added by calls to addObject, and may include Objects created by side-effects.
     * The execution of a RuleExecutionSet can add, remove and update objects as part of the rule session state.
     * Therefore the rule session state is dependent on the rules that are part of the executed RuleExecutionSet as well as the rule vendor's specific rule engine behaviour. 
     * @return a List of all objects part of the rule session state.
     * @throws InvalidRuleSessionException on illegal rule session state.
     */
    @Override
    public List getObjects()
            throws InvalidRuleSessionException {
        validateRuleSession();
        return getObjects(this.ruleSet.resolveObjectFilter());
    }

    /**
     * Returns a List over the objects in rule session state of this rule session. 
     * The objects should pass the filter test on the specified ObjectFilter. 
     * This may not neccessarily include all objects added by calls to addObject, and may include Objects created by side-effects.
     * The execution of a RuleExecutionSet can add, remove and update objects as part of the rule session state.
     * Therefore the rule session state is dependent on the rules that are part of the executed RuleExecutionSet as well as the rule vendor's specific rule engine behaviour. 
     * @param filter the object filter.
     * @return a List of all the objects in the rule session state of this rule session based upon the given object filter.
     * @throws InvalidRuleSessionException on illegal rule session state.
     */
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

    /**
     * Returns a List of the Handles being used for object identity.
     * @return a List of Handles present in the currect state of the rule session.
     * @throws InvalidRuleSessionException on illegal rule session state.
     */
    @Override
    public List getHandles()
            throws InvalidRuleSessionException {
        validateRuleSession();

        return new ArrayList(factsByHandle.keySet());
    }

    /**
     * Executes the rules in the bound rule execution set using the objects present in the rule session state.
     * This will typically modify the rule session state - and may add, remove or update Objects bound to Handles.
     * @throws InvalidRuleSessionException on illegal rule session state.
     */
    @Override
    public void executeRules()
            throws InvalidRuleSessionException {
        validateRuleSession();
        ruleSet.getEngine().executeAll(factsByName);
    }

    /**
     * Resets this rule session. 
     * Calling this method will bring the rule session state to its initial state for this rule session and will reset any other state associated with this rule session.
     * A reset will not reset the state on the default object filter for a RuleExecutionSet.
     * @throws InvalidRuleSessionException on illegal rule session state.
     */
    @Override
    public void reset()
            throws InvalidRuleSessionException {
        validateRuleSession();
        factsByHandle.clear();
        factsByName.clear();
        resetSequence();
    }

    /**
     * Returns the Object within the StatefulRuleSession associated with a Handle.
     * @param handle the handle that identifies the object
     * @return Returns the Object within the StatefulRuleSession associated with a Handle.
     * @throws InvalidHandleException if the handle is not found in the session
     * @throws InvalidRuleSessionException on illegal rule session state.
     */
    @Override
    public Object getObject(Handle handle)
            throws InvalidHandleException, InvalidRuleSessionException {
        validateRuleSession();
        if (!(handle instanceof HandleImpl)) {
            throw new InvalidHandleException("Wrong driver");
        }
        return factsByHandle.get(handle);
    }

    /**
     * Returns the meta data for the rule execution set bound to this rule session.
     * @return Returns the meta data for the rule execution set bound to this rule session.
     * @throws InvalidRuleSessionException on illegal rule session state.
     */
    @Override
    public RuleExecutionSetMetadata getRuleExecutionSetMetadata()
            throws InvalidRuleSessionException {
        validateRuleSession();

        return new RuleExecutionSetMetadataImpl(this.ruleSet);
    }

    /**
     * Releases all resources used by this rule session. This method renders this rule session unusable until it is reacquired through the RuleRuntime.
     * @throws InvalidRuleSessionException on illegal rule session state.
     */
    @Override
    public void release()
            throws InvalidRuleSessionException {
        validateRuleSession();

        reset();
        this.ruleSet = null;
    }

    /**
     * Returns the type identifier for this RuleSession. The type identifiers are defined in the RuleRuntime interface.
     * @return 0
     * @throws InvalidRuleSessionException on illegal rule session state.
     */
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
