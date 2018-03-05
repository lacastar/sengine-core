/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.lacastar.sengine.core.admin;

/**
 *
 * @author Szenthe László
 */
import java.util.HashMap;
import java.util.Map;

public abstract class HasProperties {

    private final Map map = new HashMap();

    public Object getProperty(Object key) {
        return this.map.get(key);
    }

    public void setProperty(Object key, Object value) {
        this.map.put(key, value);
    }
}
