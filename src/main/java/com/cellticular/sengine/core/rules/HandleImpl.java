/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cellticular.sengine.core.rules;

import javax.rules.Handle;
import lombok.EqualsAndHashCode;

/**
 * Marker interface for vendor specific object identity mechanism. When using the StatefulRuleSession objects that are added to rule session state are identified using a vendor supplied Handle implementation. 
 * Handles are used to unambigiously identify objects within the rule session state and should not suffer many of the object identity issues that arise when using muliple class loaders, serializing StatefulRuleSessions, or using Object.equals or object1 == object2 reference equality. 
 * @author Szenthe László
 */
@EqualsAndHashCode
public class HandleImpl implements Handle{
    private final String id;

    /**
     * Creates a handle from a prefix and the current thread id concatenated to generate a unique id. Must be changed for clusters.
     * @param prefix The prefix for the id. 
     */
    public HandleImpl(long prefix){
        id = Long.toString(prefix) + "-" + Long.toString(Thread.currentThread().getId());
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }
    
    @Override
    public String toString(){
        return "ObjectHandle["+id+"]";
    }
    
}
