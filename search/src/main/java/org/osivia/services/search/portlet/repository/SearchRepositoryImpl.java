package org.osivia.services.search.portlet.repository;

import java.util.Iterator;
import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cache.services.CacheInfo;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.taskbar.ITaskbarService;
import org.osivia.portal.api.taskbar.TaskbarItem;
import org.osivia.portal.api.taskbar.TaskbarTask;
import org.osivia.services.search.common.repository.CommonRepositoryImpl;
import org.osivia.services.search.portlet.model.TaskPath;
import org.osivia.services.search.portlet.repository.command.CreateSearchTaskCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;

/**
 * Search portlet repository implementation.
 * 
 * @author Cédric Krommenhoek
 * @see CommonRepositoryImpl
 * @see SearchRepository
 */
@Repository
public class SearchRepositoryImpl extends CommonRepositoryImpl implements SearchRepository {

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /** Taskbar service. */
    @Autowired
    private ITaskbarService taskbarService;

    /** Internationalization bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;


    /**
     * Constructor.
     */
    public SearchRepositoryImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public TaskPath getSearchTaskPath(PortalControllerContext portalControllerContext, String basePath) throws PortletException {
        // Search task path
        TaskPath path = this.applicationContext.getBean(TaskPath.class);

        // Search task
        TaskbarTask task = null;
        try {
            // Navigation tasks
            List<TaskbarTask> navigationTasks = this.taskbarService.getTasks(portalControllerContext, basePath, true);

            Iterator<TaskbarTask> iterator = navigationTasks.iterator();
            while (iterator.hasNext() && (task == null)) {
                TaskbarTask navigationTask = iterator.next();
                if (StringUtils.equals(ITaskbarService.SEARCH_TASK_ID, navigationTask.getId())) {
                    task = navigationTask;
                }
            }
        } catch (PortalException e) {
            throw new PortletException(e);
        }

        // Search task CMS path
        String cmsPath;
        if ((task == null) || StringUtils.isEmpty(task.getPath())) {
            cmsPath = this.createSearchTask(portalControllerContext, basePath, task);
            path.setUpdated(true);
        } else {
            cmsPath = task.getPath();
        }
        path.setCmsPath(cmsPath);

        return path;
    }


    /**
     * Create search task.
     * 
     * @param portalControllerContext portal controller context
     * @param basePath base path
     * @param task search task
     * @return path
     * @throws PortletException
     */
    private String createSearchTask(PortalControllerContext portalControllerContext, String basePath, TaskbarTask task) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(request.getLocale());
        
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);


        if (task == null) {
            // Search taskbar item
            TaskbarItem item;
            try {
                item = this.taskbarService.getItem(portalControllerContext, ITaskbarService.SEARCH_TASK_ID);
            } catch (PortalException e) {
                throw new PortletException(e);
            }

            task = this.taskbarService.getFactory().createTaskbarTask(item, null, null, false);
        }


        // Search task display name
        String displayName = bundle.getString(task.getKey(), task.getCustomizedClassLoader());
        
        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(CreateSearchTaskCommand.class, basePath, displayName);
        Document document = (Document) nuxeoController.executeNuxeoCommand(command);

        // Update navigation
        request.setAttribute(Constants.PORTLET_ATTR_UPDATE_CONTENTS, String.valueOf(true));

        return document.getPath();
    }

}
