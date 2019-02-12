/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cellticular.sengine.core.rules;

import java.util.logging.Logger;
import javax.rules.ConfigurationException;
import javax.rules.RuleRuntime;
import javax.rules.RuleServiceProvider;
import javax.rules.RuleServiceProviderManager;
import javax.rules.admin.RuleAdministrator;

/**
 * This class provides access to the RuleRuntime and RuleAdministrator implementation.
 * @author Szenthe László
 */
public class RuleServiceProviderImpl extends RuleServiceProvider {

    private static final Logger LOGGER = Logger.getLogger(RuleServiceProviderImpl.class.getName());
    
    static {
        try {
            LOGGER.info("Register provider hu.lacastar.sengine");
            RuleServiceProviderManager.registerRuleServiceProvider("hu.lacastar.sengine", RuleServiceProviderImpl.class);

        } catch (ConfigurationException ce) {

            ce.printStackTrace();
        }
    }

    /**
     * Returns a class instance of RuleRuntime
     * @return RuleRuntime instance
     * @throws ConfigurationException Thrown if a configuration exception is encountered
     */
    @Override
    public RuleRuntime getRuleRuntime()
            throws ConfigurationException {
        try {
            return (RuleRuntime) RuleServiceProvider.createInstance("hu.lacastar.sengine.core.rules.RuleRuntimeImpl");

        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {

            throw new ConfigurationException("Can't create RuleRuntime", e);
        }
    }

    /**
     * Returns a class instance of RuleAdministrator
     * @return RuleAdministrator instance
     * @throws ConfigurationException Thrown if a configuration exception is encountered
     */
    @Override
    public RuleAdministrator getRuleAdministrator()
            throws ConfigurationException {
        try {
            return (RuleAdministrator) RuleServiceProvider.createInstance("hu.lacastar.sengine.core.admin.RuleAdministratorImpl");

        } catch (Exception e) {

            throw new ConfigurationException("Can't create RuleAdministrator", e);
        }
    }

}
