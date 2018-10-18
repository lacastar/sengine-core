/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.lacastar.sengine.core.admin;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.rmi.RemoteException;
import java.util.Map;
import javax.rules.admin.RuleExecutionSet;
import javax.rules.admin.RuleExecutionSetCreateException;
import javax.rules.admin.RuleExecutionSetProvider;
import org.w3c.dom.Element;

/**
 * Rule execution set provider implementation
 * @author Szenthe László
 */
public class RuleExecutionSetProviderImpl implements RuleExecutionSetProvider {

    /**
     * Creates a new rule execution set instance by invoking the LocalRuleExecutionSetProviderImpl
     * @param docElement The root doc element to parse from the configuration
     * @param properties Properties passed to the engine
     * @return A new rule execution set instance
     * @throws RuleExecutionSetCreateException Thrown if an instantiation or scripting error is encountered
     * @throws RemoteException not used
     */
    @Override
    public RuleExecutionSet createRuleExecutionSet(Element docElement, Map properties)
            throws RuleExecutionSetCreateException, RemoteException {
        return new LocalRuleExecutionSetProviderImpl().createRuleExecutionSet(docElement, properties);
    }

    /**
     * Not supported operation in this implementation
     * @param ast not used
     * @param properties not used
     * @return n/a
     * @throws RuleExecutionSetCreateException This exception is returned with the message "Operation not supported"
     * @throws RemoteException not used
     */
    @Override
    public RuleExecutionSet createRuleExecutionSet(Serializable ast, Map properties)
            throws RuleExecutionSetCreateException, RemoteException {
        throw new RuleExecutionSetCreateException("Operation not supported");
    }

    /**
     * Creates a new rule execution set instance by invoking the LocalRuleExecutionSetProviderImpl and retrieve the configuration from a URI. First it tries to fetch 
     * from a URLConnection, on failure to do so the standard resource loading mechanism is tried (class.getResourceAsStream) 
     * @param uri Points to the configuration file
     * @param properties Properties used for creating the execution set
     * @return The rule execution set created.
     * @throws RuleExecutionSetCreateException Thrown if the execution set can not be created due to a configuration or scripting error
     * @throws IOException Raised if the configuration can not be loaded from the uri either through URL or as a local resource
     * @throws RemoteException not used
     */
    public RuleExecutionSet createRuleExecutionSet(String uri, Map properties)
            throws RuleExecutionSetCreateException, IOException, RemoteException {
        InputStream is = null;
        try{
            URLConnection urlc = new URL(uri).openConnection();
            is = urlc.getInputStream();
            return new LocalRuleExecutionSetProviderImpl().createRuleExecutionSet(is, properties);
        }catch( IOException ioe){
            is = getClass().getResourceAsStream(uri); 
            return new LocalRuleExecutionSetProviderImpl().createRuleExecutionSet(is, properties);
        }
        
    }

}
