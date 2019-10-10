package org.osivia.services.rss.integration.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osivia.services.rss.integration.Model.RssModel;

/**
 * RSS service interface
 * Lecture des flux RSS
 * 
 * @author Frédéric Boudan
 *
 */
public class RssServiceImpl implements RssService {

	/** logger */
	protected static final Log logger = LogFactory.getLog(RssServiceImpl.class);	
	
    static final String TITLE = "title";
    static final String DESCRIPTION = "description";
    static final String CHANNEL = "channel";
    static final String LANGUAGE = "language";
    static final String COPYRIGHT = "copyright";
    static final String LINK = "link";
    static final String AUTHOR = "author";
    static final String ITEM = "item";
    static final String PUB_DATE = "pubDate";
    static final String GUID = "guid";
    
    
    /**
     * Lecture d'un flux RSS et création du document Nuxeo.
     * 
     * param URL
     */
    @Override
	public void readRss(URL url) {
    	
    	logger.info("Lecure du flux RSS");
    	RssModel model = null;
        // Set header values intial to the empty string
        String description = "";
        String title = "";
        String link = "";
        String author = "";
        String pubdate = "";
        String guid = "";
        String enclosure = "";        
        boolean isItem = true;        
    	
		try {
			
            // Création d'un XMLInputFactory
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();

            InputStream in = read(url);
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
            
            // lecture du document XML 
            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
                if (event.isStartElement()) {
                    String localPart = event.asStartElement().getName()
                            .getLocalPart();
                    switch (localPart) {
                    case ITEM:
                        if (isItem) {
                        	isItem = false;
                            model = new RssModel(title, link, description, enclosure,
                                    pubdate, guid, description, pubdate, localPart, localPart);
                        }                    	
                        event = eventReader.nextEvent();
                        break;
                    case TITLE:
                        title = getCharacterData(event, eventReader);
                        break;
                    case DESCRIPTION:
                        description = getCharacterData(event, eventReader);
                        break;
                    case LINK:
                        link = getCharacterData(event, eventReader);
                        break;
                    case GUID:
                        guid = getCharacterData(event, eventReader);
                        break;
                    case AUTHOR:
                        author = getCharacterData(event, eventReader);
                        break;
                    case PUB_DATE:
                        pubdate = getCharacterData(event, eventReader);
                        break;
                    }
                } else if (event.isEndElement()) {
                    if (event.asEndElement().getName().getLocalPart() == (ITEM)) {
                    	// Création du document nuxeo contenant le flux RSS
                        createDocument();
                        event = eventReader.nextEvent();
                        continue;
                    }
                }
            }
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }			
			
    }

    private String getCharacterData(XMLEvent event, XMLEventReader eventReader)
            throws XMLStreamException {
        String result = "";
        event = eventReader.nextEvent();
        if (event instanceof Characters) {
            result = event.asCharacters().getData();
        }
        return result;
    }    
    
	private InputStream read(URL url) {
		try {
			return url.openStream();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
    
	private void createDocument() {
		// Ajout du document dans Nuxeo 
		// > Doit-on le faire de manière unitaire ou pas ? A priori non.
		
	}
}
