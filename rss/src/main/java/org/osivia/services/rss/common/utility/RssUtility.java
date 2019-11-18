package org.osivia.services.rss.common.utility;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osivia.services.rss.common.model.FeedRssModel;
import org.osivia.services.rss.feedRss.portlet.model.ItemRssModel;

/**
 * Read RSS feed
 * 
 * @author Frédéric Boudan
 *
 */
public class RssUtility {

	/** logger */
	protected static final Log logger = LogFactory.getLog(RssUtility.class);	
	
    static final String TITLE = "title";
    static final String DESCRIPTION = "description";
    static final String CHANNEL = "channel";
    static final String LINK = "link";
    static final String AUTHOR = "author";
    static final String ITEM = "item";
    static final String PUB_DATE = "pubDate";
    static final String GUID = "guid";
    static final String CATEGORY = "category";
    static final String SOURCE = "source";
    static final String ENCLOSURE = "enclosure";     
    
    /**
     * Read RSS feed.
     * 
     */
	public static List<ItemRssModel> readRss(FeedRssModel feed) {
    	// Restitution d'une map
    	logger.info("Lecture du flux RSS");
        String description = "";
        String title = "";
        String link = "";
        String author = "";
        String pubDate = "";
        String guid = "";
        String category = "";
        String enclosure = "";
        String sourceRss = "";
        String idConteneur = feed.getSyncId();
    	ItemRssModel rss = null;
        List<ItemRssModel> list = new ArrayList<ItemRssModel>();
    	
		try {
			
            // Création d'un XMLInputFactory
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();

            URL url = null;
			try {
				url = new URL(feed.getUrl());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
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
                        event = eventReader.nextEvent();
                        break;
                    case TITLE:
                        title = getCharacterData(event, eventReader);
                        break;
                    case LINK:
                        link = getCharacterData(event, eventReader);
                        break;
                    case DESCRIPTION:
                        description = getCharacterData(event, eventReader);
                        break;
                    case AUTHOR:
                        author = getCharacterData(event, eventReader);
                        break;
                    case CATEGORY:
                        category = getCharacterData(event, eventReader);
                        break;
                    case ENCLOSURE:
                        enclosure = getEnclosure(event, eventReader);
                        break;                        
                    case GUID:
                        guid = getCharacterData(event, eventReader);
                        break;
                    case PUB_DATE:
                        pubDate = getCharacterData(event, eventReader);
                        break;
                    case SOURCE:
                    	sourceRss = getCharacterData(event, eventReader);
                        break;
                    }
                } else if (event.isEndElement()) {
                    if (event.asEndElement().getName().getLocalPart() == (ITEM)) {
                    	// Création du document nuxeo contenant le flux RSS
                        rss = new ItemRssModel(title, link, description, author, pubDate, guid, idConteneur, category, enclosure, sourceRss);   
                        list.add(rss);
                        event = eventReader.nextEvent();
                        continue;
                    }
                }
            }
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
		return list;
    }

	public static String getCharacterData(XMLEvent event, XMLEventReader eventReader)
            throws XMLStreamException {
        String result = "";
        event = eventReader.nextEvent();
        if (event instanceof Characters) {
            result = event.asCharacters().getData();
        }
        return result;
    }
	
	public static String getEnclosure(XMLEvent event, XMLEventReader eventReader)
            throws XMLStreamException {
        String result = "";
//        Iterator<Attribute> attributes = event.ATTRIBUTE.getName;      
//        while(attributes.hasNext()) {
//        	if(attributes.equals("URL")){
//        		System.out.println("cpipci");
//        		break;
//        	}
//        }        

        return result;
    }    
	
	public static InputStream read(URL url) {
		try {
			return url.openStream();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
    	
	// Algo à mettre en place
	// --> 1 : Lecture d'une url ex: le monde
	// --> 2 : rapatriement du flux
	// --> 3 : parsing du flux
	// --> 4 : enregistrement des données dans une map
	// --> 5 : Que fait on des anciens Items du flux ? Lecture des anciens Items et si les nouveaux Items sont supérieurs à 5 (nb de flux qui peuvent apparaître) on peut suprimer les anciens flux
}
