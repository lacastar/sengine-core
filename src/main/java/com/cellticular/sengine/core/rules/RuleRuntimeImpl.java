/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.lacastar.sengine.core.rules;

import hu.lacastar.sengine.core.admin.RuleAdministratorImpl;
import hu.lacastar.sengine.core.admin.RuleExecutionSetImpl;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import javax.rules.RuleExecutionSetNotFoundException;
import javax.rules.RuleSession;
import javax.rules.RuleSessionCreateException;
import javax.rules.RuleSessionTypeUnsupportedException;

/**
 * It provides methods to create RuleSession implementation as well as methods
 * to retrieve RuleExecutionSets that have been previously registered using the
 * RuleAdministrator. The RuleRuntime should be accessed through the
 * RuleServiceProvider. An instance of the RuleRuntime can be retrieved by
 * calling RuleServiceProvider ruleServiceProvider = RuleServiceProvider.newInstance(); 
 * RuleRuntime ruleRuntime = ruleServiceProvider.getRuleRuntime(); 
 * Note: the release method must be called on the RuleSession to clean up all resources used by the RuleSession.
 *
 * @author Szenthe László
 */
public class RuleRuntimeImpl implements javax.rules.RuleRuntime {

    /**
     * Creates a RuleSession implementation using the supplied vendor-specific rule execution set registration URI.
     * @param uri  the URI for the RuleExecutionSet
     * @param properties additional properties used to create the RuleSession implementation.
     * @param ruleSessionType the type of rule session to create.
     * @return The new RuleSession instance
     * @throws RuleSessionTypeUnsupportedException  if the ruleSessionType is not supported by the vendor or the RuleExecutionSet
     * @throws RuleSessionCreateException if an internal error prevents a RuleSession from being created
     * @throws RuleExecutionSetNotFoundException if the URI could not be resolved into a RuleExecutionSet
     * @throws RemoteException not used
     */
    @Override
    public RuleSession createRuleSession(String uri, Map properties, int ruleSessionType) throws RuleSessionTypeUnsupportedException, RuleSessionCreateException, RuleExecutionSetNotFoundException, RemoteException {

        RuleExecutionSetImpl res = RuleAdministratorImpl.lookup(uri);
        if (res == null) {
            throw new RuleExecutionSetNotFoundException(uri);
        }
        switch (ruleSessionType) {
            case 1:
                return new StatelessRuleSessionImpl(res, properties);
            case 0:
                return new StatefulRuleSessionImpl(res, properties);
        }
        String message = String.valueOf(ruleSessionType);
        throw new RuleSessionTypeUnsupportedException(message);
    }

    /**
     * Retrieves a List of the URIs that currently have RuleExecutionSets associated with them. An empty list is returned is there are no associations.
     * @return List of URIs
     * @throws RemoteException a List of Strings (URIs)
     */
    @Override
    public List getRegistrations() throws RemoteException {
        return RuleAdministratorImpl.getRegistrations();
    }

}
