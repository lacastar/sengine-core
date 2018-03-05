/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.lacastar.sengine.core.rules;

import javax.rules.Handle;
import lombok.EqualsAndHashCode;

/**
 *
 * @author Szenthe László
 */
@EqualsAndHashCode
public class HandleImpl implements Handle{
    private final String id;
    public HandleImpl(long prefix){
        id = Long.toString(prefix) + "-" + Long.toString(Thread.currentThread().getId());
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }
    
    public String toString(){
        return "ObjectHandle["+id+"]";
    }
    
}
