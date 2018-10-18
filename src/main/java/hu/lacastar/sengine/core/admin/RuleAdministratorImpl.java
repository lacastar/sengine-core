/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.lacastar.sengine.core.admin;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.rules.admin.LocalRuleExecutionSetProvider;
import javax.rules.admin.RuleAdministrator;
import javax.rules.admin.RuleExecutionSet;
import javax.rules.admin.RuleExecutionSetDeregistrationException;
import javax.rules.admin.RuleExecutionSetProvider;
import javax.rules.admin.RuleExecutionSetRegisterException;

/**
 *
 * @author Szenthe László
 */
public class RuleAdministratorImpl implements RuleAdministrator {

    private static final Map<String, RuleExecutionSet> RULE_EXECUTIONS_SETS = new ConcurrentHashMap<>();

    /**
     *  Simple Rule administrator implementation
     */
    public RuleAdministratorImpl() {
    }

    /**
     * Return a rule execution set provider
     * @param properties Properties
     * @return the rule execution set provider
     * @throws RemoteException Not thrown in this implementation
     */
    @Override
    public RuleExecutionSetProvider getRuleExecutionSetProvider(Map properties)
            throws RemoteException {
        return new RuleExecutionSetProviderImpl();
    }

    /**
     * Return a rule execution set provider
     * @param properties Properties
     * @return the rule execution set provider
     * @throws RemoteException Not thrown in this implementation
     * @throws UnsupportedOperationException Not thrown in this implementation
     */
    @Override
    public LocalRuleExecutionSetProvider getLocalRuleExecutionSetProvider(Map properties)
            throws RemoteException, UnsupportedOperationException {
        return new LocalRuleExecutionSetProviderImpl();
    }

    /**
     * Register a rule execution set with this provider
     * @param bindUri A URI used to indentify the execution set
     * @param set The rule execution set
     * @param properties Properties - not used
     * @throws RuleExecutionSetRegisterException Exception is thrown if the set is not of type RuleExecutionSetImpl
     * @throws RemoteException no thrown by this implementation
     */
    @Override
    public void registerRuleExecutionSet(String bindUri, RuleExecutionSet set, Map properties)
            throws RuleExecutionSetRegisterException, RemoteException {
        if (!(set instanceof RuleExecutionSetImpl)) {
            throw new RuleExecutionSetRegisterException("Wrong driver");
        }
        ((RuleExecutionSetImpl) set).setUri(bindUri);
        RULE_EXECUTIONS_SETS.put(bindUri, set);
    }

    /**
     * Deregister a rule execution set from this provider
     * @param bindUri The URI used to register the execution set
     * @param properties Properties - not used
     * @throws RuleExecutionSetDeregistrationException Not thrown by this implementation
     * @throws RemoteException Not thrown by this implementation
     */
    @Override
    public void deregisterRuleExecutionSet(String bindUri, Map properties)
            throws RuleExecutionSetDeregistrationException, RemoteException {
        RuleExecutionSetImpl set = (RuleExecutionSetImpl) RULE_EXECUTIONS_SETS.remove(bindUri);

        if (set != null) {
            set.setUri(null);
        }
    }

    /**
     * Return the URIs used to register the rule execution sets
     * @return a List of URIs used to register the rule execution sets
     */
    public static List<String> getRegistrations() {
        return new ArrayList(RULE_EXECUTIONS_SETS.keySet());
    }

    /**
     * Retrieve a rule execution set implementation for the given registration URI
     * @param uri The URI used to register the rule execution set
     * @return The Rule execution set implementation
     */
    public static RuleExecutionSetImpl lookup(String uri) {
        return (RuleExecutionSetImpl) RULE_EXECUTIONS_SETS.get(uri);
    }

}
