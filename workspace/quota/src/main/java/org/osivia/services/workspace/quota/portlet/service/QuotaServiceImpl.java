package org.osivia.services.workspace.quota.portlet.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.EcmDocument;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.osivia.portal.api.html.HtmlFormatter;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.core.cms.CMSException;
import org.osivia.portal.core.cms.CMSServiceCtx;
import org.osivia.portal.core.cms.ICMSService;
import org.osivia.portal.core.cms.ICMSServiceLocator;
import org.osivia.services.workspace.quota.portlet.model.AskQuotaForm;
import org.osivia.services.workspace.quota.portlet.model.BigFile;
import org.osivia.services.workspace.quota.portlet.model.QuotaForm;
import org.osivia.services.workspace.quota.portlet.model.QuotaInformations;
import org.osivia.services.workspace.quota.portlet.model.UpdateForm;
import org.osivia.services.workspace.quota.portlet.model.QuotaForm.QuotaProcedureStep;
import org.osivia.services.workspace.quota.portlet.repository.QuotaRepository;
import org.osivia.services.workspace.quota.util.ApplicationContextProvider;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterException;
import fr.toutatice.portail.cms.nuxeo.api.forms.IFormsService;
import fr.toutatice.portail.cms.nuxeo.api.services.dao.DocumentDAO;

/**
 * Quota portlet service implementation.
 *
 * @author Jean-Sébastien Steux
 * @see QuotaService
 * @see ApplicationContextAware
 */
@Service
public class QuotaServiceImpl implements QuotaService, ApplicationContextAware {

    /**
     * Portlet repository.
     */
    @Autowired
    private QuotaRepository repository;

    /**
     * Internationalization bundle factory.
     */
    @Autowired
    private IBundleFactory bundleFactory;


    /** Person service. */
    @Autowired
    private PersonService personService;

    /**
     * Application context.
     */
    private ApplicationContext applicationContext;
    
    @Autowired
    private IFormsService formService;
    
    @Autowired
    private ICMSServiceLocator cmsServiceLocator;
    
    /**
     * Constructor.
     */
    public QuotaServiceImpl() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QuotaForm getQuotaForm(PortalControllerContext portalControllerContext) throws PortletException {

        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();


        QuotaForm form = getQuotaStatsForm(portalControllerContext);

        Long updateNav = (Long) request.getAttribute(Constants.PORTLET_ATTR_UPDATE_SPACE_DATA_TS);

        form.setAsynchronous(false);
        form.setTs(System.currentTimeMillis());

        if (request instanceof RenderRequest) {
            if (updateNav != null) {

                // On considère que pendant 10s. après l'évènement d'update la valeur de quota peut changer
                // (La mise à jour depuis la corbeille est par exemple asynchrone)
                // On joue donc un chargement asynchrone
                
                if (System.currentTimeMillis() - updateNav < 10000) {

                    form.setAsynchronous(true);

                    // Ne pas stocker dans le cache partagé
                    request.setAttribute("osivia.invalidateSharedCache", "1");
                }
            }
        }

        // Procedure infos
        Document procedure = this.repository.getProcedureInfos(portalControllerContext);
        
        if(procedure != null) {
	        String procedureInfos = procedure.getString("pi:currentStep");
	        if(StringUtils.equals(procedureInfos, "warning")) {
	        	form.setCurrentStep(QuotaProcedureStep.WARNING);
	        }
	        else if(StringUtils.equals(procedureInfos, "quota_request")) {
	        	form.setCurrentStep(QuotaProcedureStep.QUOTA_REQUEST);
	
	        }
        }
        
        // Big files
        getBigFiles(portalControllerContext, form);
        
        // Administrator tools
        Boolean administrator = Boolean.TRUE.equals(request.getAttribute("osivia.isAdministrator"));
        form.setAdministrator(administrator);
        
        return form;
    }

	private void getBigFiles(PortalControllerContext portalControllerContext, QuotaForm form) {
		// Big files
        List<BigFile> dtos = new ArrayList<BigFile>();
        Documents bigFiles = this.repository.getBigFiles(portalControllerContext);
        for(Document bigFile : bigFiles) {
        	DocumentDTO dto = DocumentDAO.getInstance().toDTO(bigFile);
        	BigFile bf = new BigFile();
        	bf.setDocument(dto);
        	bf.setSize(bigFile.getLong("common:size"));
        	
            String lastContributorId = bigFile.getString("dc:lastContributor");
            Person lastContributorPerson;
            if (lastContributorId == null) {
                lastContributorPerson = null;
            } else {
                lastContributorPerson = this.personService.getPerson(lastContributorId);
            }
            String lastContributorDisplayName;
            if (lastContributorPerson == null) {
                lastContributorDisplayName = lastContributorId;
            } else {
                lastContributorDisplayName = StringUtils.defaultIfBlank(lastContributorPerson.getDisplayName(), lastContributorId);
            }
        	
        	bf.setLastContributor(lastContributorDisplayName);
        	bf.setModificationDate(bigFile.getDate("dc:modified"));
        	
        	if(StringUtils.equals(bigFile.getState(),"deleted")) {
        		bf.setInTrash(true);
        	}
        	
        	dtos.add(bf);
        }
        form.setBigFiles(dtos);
	}

	private QuotaForm getQuotaStatsForm(PortalControllerContext portalControllerContext) throws PortletException {
		// Internationalization bundle
        Locale locale = portalControllerContext.getRequest().getLocale();
        Bundle bundle = this.bundleFactory.getBundle(locale);

        // quota form
        QuotaForm form = this.applicationContext.getBean(QuotaForm.class);

        QuotaInformations infos = this.repository.getQuotaItems(portalControllerContext);
        String occupiedSize = HtmlFormatter.formatSize(locale, bundle, infos.getTreeSize());
        String trashSize = HtmlFormatter.formatSize(locale, bundle, infos.getTrashedTreeSize());
        String quota = HtmlFormatter.formatSize(locale, bundle, infos.getQuota());
        
        int ratio = 0, trashRatio = 0;
        String sizeMessage = "";
        if (infos.getQuota() > 0) {
            trashRatio = (int) ((infos.getTrashedTreeSize() * 100) / infos.getQuota());            
            ratio = (int) ((infos.getTreeSize() * 100) / infos.getQuota()) - trashRatio;
            
            sizeMessage = bundle.getString("QUOTA_INFO", occupiedSize, quota, trashSize); 
        }
        else {
            sizeMessage = bundle.getString("SPACE_OCCUPIED_INFO", occupiedSize, trashSize); 

        }
        
        form.setSizeMessage(sizeMessage);
        form.setRatio(ratio);
        form.setTrashRatio(trashRatio);

        form.setInfos(infos);
		return form;
	}
    

    /**
     * {@inheritDoc}
     */
    @Override
    public AskQuotaForm getAskForm(PortalControllerContext portalControllerContext) {
    	AskQuotaForm form = this.applicationContext.getBean(AskQuotaForm.class);
    	
    	Document workspace = this.repository.getWorkspace(portalControllerContext);
        String uuid = workspace.getProperties().getString("qtc:uuid");
        form.setProcedureUuid(uuid);
        
    	return form;
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateQuota(PortalControllerContext portalControllerContext, UpdateForm form) throws PortletException {
		
		
		Long size = null;
		if(StringUtils.isNotBlank(form.getSize())) {
			size = Long.parseLong(form.getSize());
		}
				
		this.repository.updateQuota(portalControllerContext, size);

		if (form.getUuid() != null) {
			Map<String, String> variables = new HashMap<String, String>();

			ICMSService cmsService = cmsServiceLocator.getCMSService();
			CMSServiceCtx cmsContext = new CMSServiceCtx();
			cmsContext.setPortletCtx(portalControllerContext.getPortletCtx());
			cmsContext.setScope("superuser_no_cache");

			try {
				Document task = (Document) cmsService.getTask(cmsContext, null, null, UUID.fromString(form.getUuid()));

				if(form.isStepRequest()) {
					formService.proceed(portalControllerContext, task, "accept", variables);
				}
				else {
					formService.proceed(portalControllerContext, task, "stopWarning", variables);

				}
			} catch (PortalException | FormFilterException e) {
				throw new PortletException(e);
			} catch (CMSException e) {
				throw new PortletException(e);
			}
		}
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        ApplicationContextProvider.setApplicationContext(applicationContext);
    }

    @Override
    public UpdateForm getUpdateForm(PortalControllerContext portalControllerContext) throws PortletException {

        UpdateForm form = this.applicationContext.getBean(UpdateForm.class);
        
        QuotaInformations infos = this.repository.getQuotaItems(portalControllerContext);
        // Internationalization bundle
        Locale locale = portalControllerContext.getRequest().getLocale();
        Bundle bundle = this.bundleFactory.getBundle(locale);
        
        long quota = infos.getQuota();
        String quotaStr = bundle.getString("NO_LIMIT");
        
        Document workspace = repository.getWorkspace(portalControllerContext);
        
        
        if(infos.getQuota() > 0) {
        	quotaStr = HtmlFormatter.formatSize(locale, bundle, quota);
			// Quota defined in space
			if(workspace.getProperties().getLong("qt:maxSize") != null) {
				quotaStr = quotaStr.concat(bundle.getString("WORKSPACE_QUOTA"));
			}
			// Quota defined in super space
			else if(infos.getQuota() < 0) {
				quotaStr = quotaStr.concat(bundle.getString("GLOBAL_QUOTA"));
			}
        }


        form.setCurrentSize(quotaStr);
        
        form.setSize( new Long(quota/1048576L).toString());
        
        Document procedure = this.repository.getProcedureInfos(portalControllerContext);
	    if(procedure != null) {
	        String procedureInfos = procedure.getString("pi:currentStep");
	        PropertyMap map = procedure.getProperties().getMap("pi:globalVariablesValues");

	        if(StringUtils.equals(procedureInfos, "quota_request")) {
	        	form.setStepRequest(true);
	        	form.setMessage(map.get("msg").toString());

	        }
	        else {
	        	form.setMessage(bundle.getString("QUOTA_NO_REQUEST"));

	        }

	        
	        String uuid = workspace.getProperties().getString("qtc:uuid");
	        
	        form.setProcedureUuid(uuid);
        }
	    
        return form;
        
    }


	@Override
	public void ask(PortalControllerContext portalControllerContext, AskQuotaForm form) throws PortalException, FormFilterException, CMSException {

		Map<String, String> variables = new HashMap<String, String>();
		variables.put("msg",form.getRequestMsg());
		
		ICMSService cmsService = cmsServiceLocator.getCMSService();
		CMSServiceCtx cmsContext = new CMSServiceCtx();
		cmsContext.setPortletCtx(portalControllerContext.getPortletCtx());
        cmsContext.setScope("superuser_no_cache");
		
		Document task = (Document) cmsService.getTask(cmsContext, null, null, UUID.fromString(form.getProcedureUuid()));
		
		formService.proceed(portalControllerContext, task, "quotaRequest", variables);
	}

	@Override
	public void refuseQuota(PortalControllerContext portalControllerContext, UpdateForm form) throws PortletException {
		if (form.getUuid() != null) {
			Map<String, String> variables = new HashMap<String, String>();

			ICMSService cmsService = cmsServiceLocator.getCMSService();
			CMSServiceCtx cmsContext = new CMSServiceCtx();
			cmsContext.setPortletCtx(portalControllerContext.getPortletCtx());
			cmsContext.setScope("superuser_no_cache");

			try {
				Document task = (Document) cmsService.getTask(cmsContext, null, null, UUID.fromString(form.getUuid()));

				formService.proceed(portalControllerContext, task, "refuse", variables);
			} catch (PortalException | FormFilterException e) {
				throw new PortletException(e);
			} catch (CMSException e) {
				throw new PortletException(e);
			}
		}
	}

}
