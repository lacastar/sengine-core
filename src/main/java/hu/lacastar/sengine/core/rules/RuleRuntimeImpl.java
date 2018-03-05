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
 *
 * @author Szenthe László
 */
public class RuleRuntimeImpl implements javax.rules.RuleRuntime {

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

    @Override
    public List getRegistrations() throws RemoteException {
        return RuleAdministratorImpl.getRegistrations();
    }

}
