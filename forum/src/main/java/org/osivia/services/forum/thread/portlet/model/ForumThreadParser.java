package org.osivia.services.forum.thread.portlet.model;

import org.apache.commons.io.IOUtils;
import org.cyberneko.html.parsers.SAXParser;
import org.dom4j.Element;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.html.DOM4JUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.portlet.PortletException;
import javax.xml.transform.*;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.StringReader;

/**
 * Forum thread parser java-bean.
 *
 * @author Cédric Krommenhoek
 */
@Component
public class ForumThreadParser {

    /** XSL path. */
    private static final String XSL_PATH = "/WEB-INF/xsl/forum-thread.xsl";


    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;


    /** Transformer templates. */
    private final Templates templates;


    /**
     * Constructor.
     *
     * @throws PortletException
     */
    public ForumThreadParser() throws PortletException {
        super();

        // Transformer templates
        this.templates = this.getTemplates();
    }


    /**
     * Get transformer templates.
     *
     * @return transformer templates
     * @throws PortletException
     */
    private Templates getTemplates() throws PortletException {
        // Transformer factory
        TransformerFactory factory = TransformerFactory.newInstance();
        // Transformer source
        Source source = new StreamSource(this.getClass().getResourceAsStream(XSL_PATH));

        try {
            return factory.newTemplates(source);
        } catch (TransformerConfigurationException e) {
            throw new PortletException(e);
        }
    }


    /**
     * Parse HTML content.
     *
     * @param portalControllerContext portal controller context
     * @param content                 HTML content
     * @param action                  parsing action
     * @return transformed content
     * @throws PortletException
     */
    public String parse(PortalControllerContext portalControllerContext, String content, ForumThreadParserAction action) throws PortletException {
        OutputStream output = new ByteArrayOutputStream();

        // Protected content
        Element element = DOM4JUtils.generateElement("div", null, content);
        String protectedElement = DOM4JUtils.writeCompact(element);

        try {
            // Parser context
            ForumThreadParserContext parserContext = this.applicationContext.getBean(ForumThreadParserContext.class, portalControllerContext);

            // Transformer
            Transformer transformer = this.templates.newTransformer();
            transformer.setParameter("parserContext", parserContext);
            transformer.setParameter("action", action.getId());

            XMLReader parser = XMLReaderFactory.createXMLReader(SAXParser.class.getName());
            Source source = new SAXSource(parser, new InputSource(new StringReader(protectedElement)));
            Result result = new StreamResult(output);
            transformer.transform(source, result);

            return output.toString();
        } catch (SAXException | TransformerException e) {
            throw new PortletException(e);
        } finally {
            IOUtils.closeQuietly(output);
        }
    }


}
