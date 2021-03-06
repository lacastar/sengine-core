/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cellticular.sengine.core;

import com.cellticular.sengine.core.rules.engine.Engine;
import com.cellticular.sengine.core.rules.engine.Fact;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.rules.ConfigurationException;
import javax.rules.InvalidRuleSessionException;
import javax.rules.RuleExecutionSetNotFoundException;
import javax.rules.RuleRuntime;
import javax.rules.RuleServiceProvider;
import javax.rules.RuleServiceProviderManager;
import javax.rules.RuleSessionCreateException;
import javax.rules.RuleSessionTypeUnsupportedException;
import javax.rules.StatelessRuleSession;
import javax.rules.admin.RuleAdministrator;
import javax.rules.admin.RuleExecutionSet;
import javax.rules.admin.RuleExecutionSetCreateException;
import javax.rules.admin.RuleExecutionSetDeregistrationException;
import javax.rules.admin.RuleExecutionSetProvider;
import javax.rules.admin.RuleExecutionSetRegisterException;

/**
 *
 * @author Szenthe László
 */
public class TestJSR94 {

    static final String RULE_SERVICE_PROVIDER = "hu.lacastar.sengine";

// Load the rule service provider of the vendor implementation.
// For more information on loading the service provider.
    /**
     *
     * @param args Java standard args for a main class, not used
     */
    public static void main(String... args) {
        try {
            try {
                Class.forName(RULE_SERVICE_PROVIDER + ".core.rules.RuleServiceProviderImpl");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(TestJSR94.class.getName()).log(Level.SEVERE, null, ex);
            }

// Get the rule service provider from the provider manager.
            RuleServiceProvider svcProvider = RuleServiceProviderManager.getRuleServiceProvider(RULE_SERVICE_PROVIDER);

// For the next series of steps, see Rule Administration: Acquiring and Registering the RuleExecutionSet.
// Get the Rule Administrator
            RuleAdministrator ruleAdmin = svcProvider.getRuleAdministrator();

// Get the RuleExecutionSet provider
            RuleExecutionSetProvider ruleSetProvider = ruleAdmin.getRuleExecutionSetProvider(null);

// Get a sample rulebase file.
//RuleExecutionSet ruleSet = ruleSetProvider.createRuleExecutionSet("file:/C:/tmp/temp/jsr94-test1.xml", null);
            RuleExecutionSet ruleSet = ruleSetProvider.createRuleExecutionSet("/jsr94-test1.xml", null);

            String ruleSetUri = "rulebases://" + ruleSet.getName();
            ruleAdmin.registerRuleExecutionSet(ruleSetUri, ruleSet, null);

// For the next series of steps, see Establish a Rule Session.
// Create a RuleRuntime
            RuleRuntime runtime = svcProvider.getRuleRuntime();

// Create a Stateless Rule Session using the RuleRuntime
            StatelessRuleSession session = (StatelessRuleSession) runtime.createRuleSession(ruleSetUri, null, RuleRuntime.STATELESS_SESSION_TYPE);

// Enter local code to create client instances on the Java side.
//  (Assume an instance, inst1, is created for input and an
//  inst2 is created for output.)
// Enter local code to set properties of created instances
// Perform inferencing over the added objects with the rules
// passed to this session at create time. See Stateless versus
// Stateful Rule Sessions.
            List inputObjects = (List) new LinkedList();
            Map<String, Object> param = new HashMap<>();
            Fact fact = Fact.builder().name("value").value(true).build();
            inputObjects.add(fact);
            fact = Fact.builder().name("numericValue").value(0).build();
            inputObjects.add(fact);

//inputObjects.add(inst1);
//inputObjects.add(inst2);  //NOTE: Also pass the output instance to the rulebase
            List objectsReadBack = session.executeRules(inputObjects);

// Enter local code to process results, for example, iterate over
// objectsReadBack.
// Close the session with the inference engine.
            for (Object obj : objectsReadBack) {
                fact = (Fact) obj;
                Logger.getLogger(TestJSR94.class.getName()).info("returned: " + fact.toString());
            }

            session.release();

            ruleAdmin.deregisterRuleExecutionSet("rulebases://" + ruleSet.getName(), null);
        } catch (ConfigurationException ex) {
            Logger.getLogger(TestJSR94.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(TestJSR94.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RuleExecutionSetCreateException ex) {
            Logger.getLogger(TestJSR94.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TestJSR94.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RuleExecutionSetRegisterException ex) {
            Logger.getLogger(TestJSR94.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RuleSessionTypeUnsupportedException ex) {
            Logger.getLogger(TestJSR94.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RuleSessionCreateException ex) {
            Logger.getLogger(TestJSR94.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RuleExecutionSetNotFoundException ex) {
            Logger.getLogger(TestJSR94.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidRuleSessionException ex) {
            Logger.getLogger(TestJSR94.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RuleExecutionSetDeregistrationException ex) {
            Logger.getLogger(TestJSR94.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
