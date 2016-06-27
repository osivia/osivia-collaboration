package org.osivia.services.workspace.task.creation.portlet.service.impl;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.portlet.PortletException;

import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.services.workspace.common.portlet.model.TaskCreationForm;
import org.osivia.services.workspace.task.creation.portlet.model.comparator.AlphaOrderComparator;
import org.osivia.services.workspace.task.creation.portlet.repository.WorkspaceTaskCreationRepository;
import org.osivia.services.workspace.task.creation.portlet.service.WorkspaceTaskCreationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * Workspace task creation service implementation.
 * 
 * @author Cédric Krommenhoek
 * @see WorkspaceTaskCreationService
 */
@Service
public class WorkspaceTaskCreationServiceImpl implements WorkspaceTaskCreationService {

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /** Workspace task creation repository. */
    @Autowired
    private WorkspaceTaskCreationRepository repository;

    /** Alpha order comparator. */
    @Autowired
    private AlphaOrderComparator alphaOrderComparator;

    /** Bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;


    /**
     * Constructor.
     */
    public WorkspaceTaskCreationServiceImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public TaskCreationForm getTaskCreationForm(PortalControllerContext portalControllerContext) throws PortletException {
        return this.applicationContext.getBean(TaskCreationForm.class);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public SortedMap<String, DocumentType> getTaskTypes(PortalControllerContext portalControllerContext) throws PortletException {
        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        // Task document types
        List<DocumentType> documentTypes = this.repository.getTaskTypes(portalControllerContext);

        // Types
        SortedMap<String, DocumentType> types = new TreeMap<>(this.alphaOrderComparator);
        for (DocumentType type : documentTypes) {
            String displayName = bundle.getString(StringUtils.upperCase(type.getName()), type.getCustomizedClassLoader());

            types.put(displayName, type);
        }
        
        return types;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void save(PortalControllerContext portalControllerContext, TaskCreationForm form) throws PortletException {
        // Update model
        form.setValid(true);
    }

}
