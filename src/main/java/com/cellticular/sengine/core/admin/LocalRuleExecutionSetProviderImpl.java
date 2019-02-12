package com.cellticular.sengine.core.admin;

import com.google.gson.Gson;
import com.cellticular.sengine.core.rules.engine.Rules;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
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
 * A rule config parser to process and create a rule execution set. 
 * Input stream is supposed to contain a document like 'jsr94-test.xml': a root node called rule-execution-set that contains
 * a  name,  description and rules element. Name is the name and description is the description of the rule
 * execution set respectively. Rules contains the rules as JavaScript in the following form:  {"rules": [
                    { 
                        "name": "testRule1",
                        "description": "test rule 1 returns true", 
                        "code": "factParams.value = factParams.value;" 
                    },
             ...
 * 
 * @author Szenthe László
 */
public class LocalRuleExecutionSetProviderImpl implements LocalRuleExecutionSetProvider {

    /**
     *  Name element constant.
     */
    public static final String NAME = "name";

    /**
     * Description element constant.
     */
    public static final String DESCRIPTION = "description";

    /**
     * Rules element constant.
     */
    public static final String RULES = "rules";
    
    /**
     * Create a RuleExecutionSet from the provided inputStream.
     * @param inputStream The stream that contains the configuration document.
     * @param props Properties provided for the rule engine.
     * @return The processed rule execution set.
     * @throws RuleExecutionSetCreateException Business or configuration error encountered during creation.
     * @throws IOException Error during reading the inputstream.
     */
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

    /**
     * Create a RuleExecutionSet from the provided Reader.
     * @param reader The reader that contains the configuration document.
     * @param props Properties provided for the rule engine.
     * @return The procecessed rule execution set.
     * @throws RuleExecutionSetCreateException Business or configuration error encountered during creation.
     * @throws IOException Error during reading.
     */
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
        NodeList contents = docElement.getElementsByTagName(NAME);
        if (contents.getLength() == 0) {
            throw new RuleExecutionSetCreateException("Name not specified");
        }
        //String name = contents.item(0).getFirstChild().getNodeValue().trim();
        String name = contents.item(0).getTextContent().trim();

        contents = docElement.getElementsByTagName(DESCRIPTION);
        if (contents.getLength() == 0) {
            throw new RuleExecutionSetCreateException("Description not specified");
        }
        //String description = contents.item(0).getFirstChild().getNodeValue().trim();
        String description = contents.item(0).getTextContent().trim();

        contents = docElement.getElementsByTagName(RULES);
        if (contents.getLength() == 0) {
            throw new RuleExecutionSetCreateException("Code not specified");
        }
        String code = contents.item(0).getTextContent().trim();
        if (properties == null) {
            properties = new HashMap();
        }
        properties.put(NAME, name);
        properties.put(DESCRIPTION, description);
        
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

    /**
     * Not implemented 
     * @param ast ast 
     * @param properties  properties
     * @return RuleExecutionSet
     * @throws RuleExecutionSetCreateException on fail
     */
    @Override
    public RuleExecutionSet createRuleExecutionSet(Object ast, Map properties)
            throws RuleExecutionSetCreateException {
        throw new RuleExecutionSetCreateException("AST not supported");
    }

}
