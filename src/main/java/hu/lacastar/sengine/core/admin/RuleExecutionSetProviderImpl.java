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
 *
 * @author Szenthe László
 */
public class RuleExecutionSetProviderImpl implements RuleExecutionSetProvider {

    @Override
    public RuleExecutionSet createRuleExecutionSet(Element docElement, Map properties)
            throws RuleExecutionSetCreateException, RemoteException {
        return new LocalRuleExecutionSetProviderImpl().createRuleExecutionSet(docElement, properties);
    }

    @Override
    public RuleExecutionSet createRuleExecutionSet(Serializable ast, Map properties)
            throws RuleExecutionSetCreateException, RemoteException {
        throw new RuleExecutionSetCreateException("Operation not supported");
    }

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
