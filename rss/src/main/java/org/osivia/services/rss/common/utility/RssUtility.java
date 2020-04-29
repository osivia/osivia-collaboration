package org.osivia.services.rss.common.utility;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osivia.services.rss.common.model.FeedRssModel;
import org.osivia.services.rss.common.model.Picture;
import org.osivia.services.rss.feedRss.portlet.model.ItemRssModel;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

/**
 * Read RSS feed
 *
 * @author Frédéric Boudan
 */
public class RssUtility {

    /**
     * logger
     */
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
     */
    public static List<ItemRssModel> readRss(FeedRssModel feed) {
        // Restitution d'une map
        String description = "";
        String title = "";
        String link = "";
        String author = "";
        Date pubDate = null;
        String guid = "";
        String category = "";
        String enclosure = "";
        String sourceRss = "";
        String docid = "";
        Picture visual = null;
        String idConteneur = feed.getSyncId();
        ItemRssModel rss = null;
        List<ItemRssModel> list = new ArrayList<ItemRssModel>();
        SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy", Locale.ENGLISH);

        try {

            // Création d'un XMLInputFactory
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();

            URL url = null;
            try {
                url = new URL(feed.getUrl());
            } catch (MalformedURLException e) {
                logger.error("BATCH RSS - E04 - methode readRss - MalformedURLException" );
            }
            logger.info("BATCH RSS - I03 - Lecture du flux RSS :" + url + ";");
            InputStream inputStream = null;
            try {
                Proxy proxy = getProxy();

                URLConnection urlConnection = url.openConnection(proxy);
                inputStream = urlConnection.getInputStream();

                if (inputStream != null) {
                    XMLEventReader eventReader = inputFactory.createXMLEventReader(inputStream);

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
                                    description = getCharacterDataSanitazier(event, eventReader);
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
                                    try {
                                        pubDate = format.parse(getCharacterData(event, eventReader).substring(0, 16));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case SOURCE:
                                    sourceRss = getCharacterData(event, eventReader);
                                    break;
                            }
                        } else if (event.isEndElement()) {
                            if (event.asEndElement().getName().getLocalPart() == (ITEM)) {
                                // Création du document nuxeo contenant le flux RSS
                                rss = new ItemRssModel(title, link, description, author, pubDate, guid, idConteneur, category, enclosure, sourceRss, visual, docid);
                                list.add(rss);
                                event = eventReader.nextEvent();
                                continue;
                            }
                        }
                    }
                }
            } finally {
                IOUtils.closeQuietly(inputStream);
            }
        } catch (XMLStreamException | IOException e) {
            logger.error("Erreur lors de la lecture du flux:" + e.getMessage());
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

    public static String getCharacterDataSanitazier(XMLEvent event, XMLEventReader eventReader)
            throws XMLStreamException {
        String result = "";
        event = eventReader.nextEvent();
        if (event instanceof Characters) {
            // Add sanitizer
            PolicyFactory policy = new HtmlPolicyBuilder().allowElements("b", "i", "u", "em", "strong").toFactory();
            result = policy.sanitize(event.asCharacters().getData());
        }
        return result;
    }

    public static String getEnclosure(XMLEvent event, XMLEventReader eventReader)
            throws XMLStreamException {
        String result = "";
        QName hrefQName = new QName("", "url");
        result = event.asStartElement().getAttributeByName(hrefQName).getValue();

        return result;
    }

    /**
     * Get proxy.
     *
     * @return proxy
     */
    public static Proxy getProxy() {
        String proxyHost = System.getProperty("http.proxyHost");
        int proxyPort = NumberUtils.toInt(System.getProperty("http.proxyPort"));

        Proxy proxy;
        if (StringUtils.isEmpty(proxyHost)) {
            proxy = Proxy.NO_PROXY;
        } else {
            SocketAddress socketAddress = new InetSocketAddress(proxyHost, proxyPort);
            proxy = new Proxy(Proxy.Type.HTTP, socketAddress);
        }
        
        return proxy;
    }
}
