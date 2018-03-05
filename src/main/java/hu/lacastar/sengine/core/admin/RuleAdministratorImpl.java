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

    public RuleAdministratorImpl() {
    }

    @Override
    public RuleExecutionSetProvider getRuleExecutionSetProvider(Map properties)
            throws RemoteException {
        return new RuleExecutionSetProviderImpl();
    }

    @Override
    public LocalRuleExecutionSetProvider getLocalRuleExecutionSetProvider(Map properties)
            throws RemoteException, UnsupportedOperationException {
        return new LocalRuleExecutionSetProviderImpl();
    }

    @Override
    public void registerRuleExecutionSet(String bindUri, RuleExecutionSet set, Map properties)
            throws RuleExecutionSetRegisterException, RemoteException {
        if (!(set instanceof RuleExecutionSetImpl)) {
            throw new RuleExecutionSetRegisterException("Wrong driver");
        }
        ((RuleExecutionSetImpl) set).setUri(bindUri);
        RULE_EXECUTIONS_SETS.put(bindUri, set);
    }

    @Override
    public void deregisterRuleExecutionSet(String bindUri, Map properties)
            throws RuleExecutionSetDeregistrationException, RemoteException {
        RuleExecutionSetImpl set = (RuleExecutionSetImpl) RULE_EXECUTIONS_SETS.remove(bindUri);

        if (set != null) {
            set.setUri(null);
        }
    }

    public static List getRegistrations() {
        return new ArrayList(RULE_EXECUTIONS_SETS.keySet());
    }

    public static RuleExecutionSetImpl lookup(String uri) {
        return (RuleExecutionSetImpl) RULE_EXECUTIONS_SETS.get(uri);
    }

}
