/**
 * 
 */
package org.osivia.services.workspace.quota.reporting.portlet.controller;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.IOUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PaginableDocuments;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cache.services.CacheInfo;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.services.workspace.quota.reporting.portlet.service.QuotaReportingService;
import org.osivia.services.workspace.quota.reporting.portlet.service.ReportQuotasCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;

/**
 * 
 * @author Loïc Billon
 *
 */
@Controller
@RequestMapping("VIEW")
public class QuotaReportingController extends CMSPortlet {

    /**
     * Portlet config.
     */
    @Autowired
    private PortletConfig portletConfig;
    
    /**
     * Portlet context.
     */
    @Autowired
    private PortletContext portletContext;
    
    /** Bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;    
    
    /**
     * Portlet service.
     */
    @Autowired
    private QuotaReportingService service;    
	
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response) throws PortletException {
    	return "view";
    }
    

    /**
     * Export workspaces quotas table in CSV format resource mapping.
     * 
     * @param request resource request
     * @param response resource response
     * @param members members form model attribute
     * @throws PortletException
     * @throws IOException
     */
    @ResourceMapping("export-workspaces-csv")
    public void exportCsv(ResourceRequest request, ResourceResponse response) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
        
        // Content type
        response.setContentType("text/csv");
        // Content disposition
        response.setProperty("Content-disposition", "attachment; filename=\"" + "quotas.csv" + "\"");

		NuxeoController nuxeoController = new NuxeoController(portletContext);

		nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
		nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);

		int currentPage = 0;
		
		List<Document> allWorkspaces = new ArrayList<>();
		PaginableDocuments workspaces = (PaginableDocuments) nuxeoController.executeNuxeoCommand(new ReportQuotasCommand(currentPage));
		allWorkspaces.addAll(workspaces.list());
		
		while(workspaces.size() == ReportQuotasCommand.PAGE_SIZE) {
			currentPage = currentPage+1;
			workspaces = (PaginableDocuments) nuxeoController.executeNuxeoCommand(new ReportQuotasCommand(currentPage));
			allWorkspaces.addAll(workspaces.list());
		}


        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        // Writer
        OutputStreamWriter writer = new OutputStreamWriter(response.getPortletOutputStream());

        // Date format
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");

        try {
            // CSV format
            CSVFormat format = CSVFormat.EXCEL;

            // Headers
            List<String> headers = new ArrayList<String>();
            headers.add("Titre");
            headers.add("Taille (en Go)");
            format.withHeader(headers.toArray(new String[headers.size()]));

            // CSV printer
            CSVPrinter printer = format.print(writer);

            try {
            	DecimalFormat df = new DecimalFormat("#.##");
            	
                for (Document workspace : allWorkspaces) {
                	//Double currentSize = new Double(0);
                    Object object = workspace.getProperties().get("qtc:currentSize");
                    
                    String currentSizeStr = "0";
                    if(object != null) {
                    	Double currentSize = Double.parseDouble(object.toString());
                        currentSize = currentSize / 1000000000;
                        
                        df.setRoundingMode(RoundingMode.DOWN);
                        currentSizeStr = df.format(currentSize);
                        
                    }
					
					printer.printRecord(workspace.getTitle(), currentSizeStr);
                }
            } finally {
                IOUtils.closeQuietly(printer);
            }
        } finally {
            IOUtils.closeQuietly(writer);
        }

    }
    
    /**
     * Post-construct.
     */
    @PostConstruct
    public void postConstruct() throws PortletException {
        super.init(this.portletConfig);
        
        try {
			service.installBatch(portletContext);
		} catch (PortalException e) {
			throw new PortletException(e);
		}
    }
    
    /* (non-Javadoc)
     * @see fr.toutatice.portail.cms.nuxeo.api.CMSPortlet#destroy()
     */
    @Override
    @PreDestroy
    public void destroy() {

    	service.uninstallBatch();
    	super.destroy();
    	
    }    
}
