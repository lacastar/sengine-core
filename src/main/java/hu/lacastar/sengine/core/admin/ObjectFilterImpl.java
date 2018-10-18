/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.lacastar.sengine.core.admin;

import javax.rules.ObjectFilter;

/**
 * ObjectFilter implementation
 * 
 * @author Szenthe László
 */
public class ObjectFilterImpl implements ObjectFilter {

    /**
     * Filter the parameter object
     * @param obj Object to filter
     * @return filtered object
     */
    @Override
    public Object filter(Object obj) {
        return obj;
    }

    /**
     * Reset the filter
     */
    @Override
    public void reset() {
    }

}
