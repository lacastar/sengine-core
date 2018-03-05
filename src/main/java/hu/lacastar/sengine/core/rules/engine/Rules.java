/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.lacastar.sengine.core.rules.engine;

/**
 *
 * @author Szenthe László
 */
public class Rules {

    private Rule[] rules;

    /**
     * @return the rules
     */
    public Rule[] getRules() {
        return rules;
    }

    /**
     * @param rules the rules to set
     */
    public void setRules(Rule[] rules) {
        this.rules = rules;
    }

    public static class Rule {

        private String name;
        private String description;
        private String code;

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name the name to set
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * @return the description
         */
        public String getDescription() {
            return description;
        }

        /**
         * @param description the description to set
         */
        public void setDescription(String description) {
            this.description = description;
        }

        /**
         * @return the code
         */
        public String getCode() {
            return code;
        }

        /**
         * @param code the code to set
         */
        public void setCode(String code) {
            this.code = code;
        }

    }
}
