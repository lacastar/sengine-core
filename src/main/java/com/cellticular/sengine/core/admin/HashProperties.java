package com.cellticular.sengine.core.admin;

/**
 *
 * @author Szenthe László
 */
import java.util.HashMap;
import java.util.Map;

/**
 *  Abstract base class for rule execution set properties
 * 
 * @author Szenthe László
 */
public abstract class HashProperties {

    private final Map map = new HashMap();

    /**
     * Returns a property for key
     * @param key The key that identifies the property
     * @return the object associated with the key
     */
    public Object getProperty(Object key) {
        return this.map.get(key);
    }

    /**
     * Sets a property for a given key
     * @param key The key that identifies the property
     * @param value The property value
     */
    public void setProperty(Object key, Object value) {
        this.map.put(key, value);
    }
}
