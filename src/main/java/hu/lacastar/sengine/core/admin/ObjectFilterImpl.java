/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.lacastar.sengine.core.admin;

import javax.rules.ObjectFilter;

/**
 *
 * @author Szenthe László
 */
public class ObjectFilterImpl implements ObjectFilter {

    @Override
    public Object filter(Object obj) {
        return obj;
    }

    @Override
    public void reset() {
    }

}
