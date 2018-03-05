/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.lacastar.sengine.core.admin;

import com.google.gson.Gson;
import hu.lacastar.sengine.core.rules.engine.Rules;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.rules.admin.LocalRuleExecutionSetProvider;
import javax.rules.admin.RuleExecutionSet;
import javax.rules.admin.RuleExecutionSetCreateException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author Szenthe László
 */
public class LocalRuleExecutionSetProviderImpl implements LocalRuleExecutionSetProvider {

    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";

    @Override
    public RuleExecutionSet createRuleExecutionSet(InputStream inputStream, Map props)
            throws RuleExecutionSetCreateException, IOException {
        Document doc = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(new InputSource(inputStream));
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new RuleExecutionSetCreateException("Parse error", e);
        }
        return createRuleExecutionSet(doc.getDocumentElement(), props);
    }

    @Override
    public RuleExecutionSet createRuleExecutionSet(Reader reader, Map props)
            throws RuleExecutionSetCreateException, IOException {
        Document doc = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(new InputSource(reader));
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new RuleExecutionSetCreateException("Parse error", e);
        }
        return createRuleExecutionSet(doc.getDocumentElement(), props);
    }

    RuleExecutionSet createRuleExecutionSet(Element docElement, Map<String, String> properties)
            throws RuleExecutionSetCreateException {
        NodeList contents = docElement.getElementsByTagName("name");
        if (contents.getLength() == 0) {
            throw new RuleExecutionSetCreateException("Name not specified");
        }
        //String name = contents.item(0).getFirstChild().getNodeValue().trim();
        String name = contents.item(0).getTextContent().trim();

        contents = docElement.getElementsByTagName("description");
        if (contents.getLength() == 0) {
            throw new RuleExecutionSetCreateException("Description not specified");
        }
        //String description = contents.item(0).getFirstChild().getNodeValue().trim();
        String description = contents.item(0).getTextContent().trim();

        contents = docElement.getElementsByTagName("rules");
        if (contents.getLength() == 0) {
            throw new RuleExecutionSetCreateException("Code not specified");
        }
        String code = contents.item(0).getTextContent().trim();
        if (properties == null) {
            properties = new HashMap();
        }
        properties.put("name", name);
        properties.put("description", description);
        
        Gson gson = new Gson();
        Logger.getLogger(LocalRuleExecutionSetProviderImpl.class.getName()).log(Level.INFO, "Ruleset: {0} code: {1}", new Object[]{name, code});
        Rules rules = gson.fromJson(code, Rules.class);
        
        return createRuleExecutionSetFromCLP(rules, properties);
        
    }

    RuleExecutionSet createRuleExecutionSetFromCLP(Rules rules, Map props)
            throws RuleExecutionSetCreateException{
    //    try {
            String name = "Untitled";
            String description = "Generic rule execution set";
            if (props != null) {
                if (props.get("name") != null) {
                    name = (String) props.get("name");
                }
                if (props.get("description") != null) {
                    description = (String) props.get("description");
                }
            }
            RuleExecutionSetImpl rs = new RuleExecutionSetImpl(name, description, null, props, rules);
            return rs;

    }

    @Override
    public RuleExecutionSet createRuleExecutionSet(Object ast, Map properties)
            throws RuleExecutionSetCreateException {
        throw new RuleExecutionSetCreateException("AST not supported");
    }

}
