package org.osivia.services.edition.portlet.repository;

import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.context.PortalControllerContext;
import org.springframework.validation.Errors;

import javax.portlet.PortletException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Document edition common repository interface.
 *
 * @param <T> object type
 * @author Cédric Krommenhoek
 */
public interface DocumentEditionCommonRepository<T> {

    /**
     * Get object.
     *
     * @param portalControllerContext portlet controller context
     * @param document                related Nuxeo document
     * @return object
     */
    T get(PortalControllerContext portalControllerContext, Document document) throws PortletException, IOException;


    /**
     * Validate object.
     *
     * @param object related object
     * @param errors validation errors
     */
    void validate(T object, Errors errors);


    /**
     * Customize object properties.
     *
     * @param portalControllerContext portal controller context
     * @param object                  related object
     * @param properties              document properties
     * @param binaries                document binaries
     */
    void customizeProperties(PortalControllerContext portalControllerContext, T object, PropertyMap properties, Map<String, List<Blob>> binaries) throws PortletException, IOException;

}
