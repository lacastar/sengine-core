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
 *
 * @author Szenthe László
 */
public class StatelessRuleSessionImpl implements RuleSession, StatelessRuleSession {

   
  private StatefulRuleSessionImpl session;
  
  StatelessRuleSessionImpl(RuleExecutionSetImpl res, Map properties)
  {
    this.session = new StatefulRuleSessionImpl(res, properties);
  }
  
  @Override
  public List executeRules(List objects)
    throws InvalidRuleSessionException
  {
    return executeRules(objects, this.session.ruleSet.resolveObjectFilter());
  }
  
  @Override
  public List executeRules(List objects, ObjectFilter filter)
    throws InvalidRuleSessionException
  {
    this.session.reset();
    this.session.addObjects(objects);
    this.session.executeRules();
    return this.session.getObjects(filter);
  }
  
  @Override
  public RuleExecutionSetMetadata getRuleExecutionSetMetadata()
    throws InvalidRuleSessionException
  {
    return this.session.getRuleExecutionSetMetadata();
  }
  
  @Override
  public void release()
    throws InvalidRuleSessionException
  {
    this.session.release();
    this.session = null;
  }
  
  @Override
  public int getType()
    throws InvalidRuleSessionException
  {
    this.session.validateRuleSession();
    
    return 1;
  }
    
}
