/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.lacastar.sengine.core.rules;

import hu.lacastar.sengine.core.admin.RuleExecutionSetImpl;
import java.util.List;
import java.util.Map;
import javax.rules.InvalidRuleSessionException;
import javax.rules.ObjectFilter;
import javax.rules.RuleExecutionSetMetadata;
import javax.rules.RuleSession;
import javax.rules.StatelessRuleSession;

/**
 * A stateless rules engine session exposes a stateless rule execution API to an underlying rules engine. 
 * @author Szenthe László
 */
public class StatelessRuleSessionImpl implements RuleSession, StatelessRuleSession {

   
  private StatefulRuleSessionImpl session;
  
  StatelessRuleSessionImpl(RuleExecutionSetImpl res, Map properties)
  {
    this.session = new StatefulRuleSessionImpl(res, properties);
  }
  
    /**
     * Executes the rules in the bound rule execution set using the supplied list of objects.
     * A List is returned containing the objects created by (or passed into the rule session) the executed rules that pass the filter test of the default RuleExecutionSet ObjectFilter (if present). 
     * The returned list may not neccessarily include all objects passed, and may include Objects created by side-effects.
     * The execution of a RuleExecutionSet can add, remove and update objects. 
     * Therefore the returned object list is dependent on the rules that are part of the executed RuleExecutionSet as well as the rule vendor's specific rule engine behaviour. 
     * @param objects the objects used to execute rules.
     * @return a List containing the objects as a result of executing the rules.
     * @throws InvalidRuleSessionException  on illegal rule session state.
     */
    @Override
  public List executeRules(List objects)
    throws InvalidRuleSessionException
  {
    return executeRules(objects, this.session.ruleSet.resolveObjectFilter());
  }
  
    /**
     * Executes the rules in the bound rule execution set using the supplied list of objects.
     * A List is returned containing the objects created by (or passed into the rule engine) the executed rules and filtered with the supplied object filter.
     * The returned list may not neccessarily include all objects passed, and may include Objects created by side-effects.
     * The execution of a RuleExecutionSet can add, remove and update objects.
     * Therefore the returned object list is dependent on the rules that are part of the executed RuleExecutionSet as well as the rule vendor's specific rule engine behaviour. 
     * @param objects the objects used to execute rules.
     * @param filter the object filter.
     * @return a List containing the objects as a result of executing rules, after passing through the supplied object filter.
     * @throws InvalidRuleSessionException  on illegal rule session state.
     */
    @Override
  public List executeRules(List objects, ObjectFilter filter)
    throws InvalidRuleSessionException
  {
    this.session.reset();
    this.session.addObjects(objects);
    this.session.executeRules();
    return this.session.getObjects(filter);
  }
  
    /**
     * Returns the meta data for the rule execution set bound to this rule session.
     * @return Returns the meta data for the rule execution set bound to this rule session.
     * @throws InvalidRuleSessionException  on illegal rule session state.
     */
    @Override
  public RuleExecutionSetMetadata getRuleExecutionSetMetadata()
    throws InvalidRuleSessionException
  {
    return this.session.getRuleExecutionSetMetadata();
  }
  
    /**
     * Releases all resources used by this rule session. This method renders this rule session unusable until it is reacquired through the RuleRuntime.
     * @throws InvalidRuleSessionException  on illegal rule session state.
     */
    @Override
  public void release()
    throws InvalidRuleSessionException
  {
    this.session.release();
    this.session = null;
  }
  
    /**
     * Returns the type identifier for this RuleSession. The type identifiers are defined in the RuleRuntime interface.
     * @return 1
     * @throws InvalidRuleSessionException  on illegal rule session state.
     */
    @Override
  public int getType()
    throws InvalidRuleSessionException
  {
    this.session.validateRuleSession();
    
    return 1;
  }
    
}
