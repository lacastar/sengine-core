/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cellticular.sengine.core.rules;

import com.cellticular.sengine.core.admin.RuleExecutionSetImpl;
import javax.rules.RuleExecutionSetMetadata;

/**
 * The RuleExecutionSetMetadata exposes some simple properties of the RuleExecutionSet to the runtime user. 
 * This interface can be extended by rule engine providers to expose additional proprietary properties to the runtime user. 
 * It is recommended but not required that any properties that are exposed in such extensions be read only, and that their values be static for the duration of the RuleSession.
 * @author Szenthe László
 */
public class RuleExecutionSetMetadataImpl implements RuleExecutionSetMetadata {

  private final String uri;
  private final String name;
  private final String description;
  
  RuleExecutionSetMetadataImpl(RuleExecutionSetImpl impl)
  {
    this.name = impl.getName();
    
    this.description = impl.getDescription();
    
    this.uri = impl.getUri();
  }
  
    /**
     * Get the URI for this RuleExecutionSet.
     * @return The URI for this RuleExecutionSet.
     */
    @Override
  public String getUri()
  {
    return this.uri;
  }
  
    /**
     * Get the name of this RuleExecutionSet.
     * @return The name of the RuleExecutionSet.
     */
    @Override
  public String getName()
  {
    return this.name;
  }
  
    /**
     * Get a short description about this RuleExecutionSet.
     * @return The description of this RuleExecutionSet or null.
     */
    @Override
  public String getDescription()
  {
    return this.description;
  }
    
}
