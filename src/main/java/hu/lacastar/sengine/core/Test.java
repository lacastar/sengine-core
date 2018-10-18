/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.lacastar.sengine.core;

import com.google.gson.Gson;
import hu.lacastar.sengine.core.admin.LocalRuleExecutionSetProviderImpl;
import hu.lacastar.sengine.core.rules.engine.Engine;
import hu.lacastar.sengine.core.rules.engine.Rules;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.rules.admin.RuleExecutionSetCreateException;
import javax.script.ScriptException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author Szenthe László
 */
public class Test {
    
    private static Rules readRules() throws ParserConfigurationException, SAXException, IOException, RuleExecutionSetCreateException {
        InputStream is = Test.class.getResourceAsStream("/jsr94-test1.xml");
        Document doc = null;
        
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        doc = db.parse(new InputSource(is));
        NodeList contents = doc.getElementsByTagName("rules");
        if (contents.getLength() == 0) {
            throw new RuleExecutionSetCreateException("Code not specified");
        }
        String code = contents.item(0).getTextContent().trim();
        Gson gson = new Gson();
        Rules rules = gson.fromJson(code, Rules.class);
        return rules;
    }

    /**
     *
     * Simple hello world test class of the engine. 
     * 
     * @param args Standard Java main function args, not used in this test class
     */
    public static void main(String... args) {
        try {
            Engine engine = new Engine(null);
            
            for (Rules.Rule rule : readRules().getRules()) {
                engine.initRule(rule);
            }
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("value", true);
            parameters.put("numericValue", 0);
            engine.executeAll(parameters);
            Logger.getLogger(Test.class.getName()).info("value: " + parameters.get("value"));
            Logger.getLogger(Test.class.getName()).info("numericValue: " + parameters.get("numericValue"));
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RuleExecutionSetCreateException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ScriptException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
