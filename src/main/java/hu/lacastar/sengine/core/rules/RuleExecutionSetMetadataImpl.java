/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.lacastar.sengine.core.rules;

import hu.lacastar.sengine.core.admin.RuleExecutionSetImpl;
import javax.rules.RuleExecutionSetMetadata;

/**
 *
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
  
   @Override
  public String getUri()
  {
    return this.uri;
  }
  
   @Override
  public String getName()
  {
    return this.name;
  }
  
   @Override
  public String getDescription()
  {
    return this.description;
  }
    
}
