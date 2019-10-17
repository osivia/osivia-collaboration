package org.osivia.services.rss.container.portlet.service;

import java.net.URL;

import javax.portlet.PortletException;

/**
 * RSS service interface
 * Lecture des flux RSS
 * 
 * @author Frédéric Boudan
 *
 */
public interface RssService {

    /**
     * Lecture du flux RSS et enregistrement du document Nuxeo.
     * 
     * @param portalControllerContext portal controller context
     * @return URL
     * @throws PortletException
     */
    void readRss(URL url);
    
}
