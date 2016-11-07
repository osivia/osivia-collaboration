package org.osivia.services.workspace.portlet.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletException;

import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.panels.PanelPlayer;
import org.osivia.portal.api.taskbar.ITaskbarService;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.services.workspace.portlet.model.Configuration;
import org.osivia.services.workspace.portlet.model.Group;
import org.osivia.services.workspace.portlet.model.Participants;
import org.osivia.services.workspace.portlet.model.View;
import org.osivia.services.workspace.portlet.model.comparator.GroupComparator;
import org.osivia.services.workspace.portlet.model.comparator.MemberComparator;
import org.osivia.services.workspace.portlet.repository.ParticipantsRepository;
import org.osivia.services.workspace.util.ApplicationContextProvider;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * Participants portlet service implementation.
 * 
 * @author Cédric Krommenhoek
 * @see ParticipantsService
 * @see ApplicationContextAware
 */
@Service
public class ParticipantsServiceImpl implements ParticipantsService, ApplicationContextAware {

    /** Portlet repository. */
    @Autowired
    private ParticipantsRepository repository;

    /** Group comparator. */
    @Autowired
    private GroupComparator groupComparator;

    /** Member comparator. */
    @Autowired
    private MemberComparator memberComparator;

    /** Portal URL factory. */
    @Autowired
    private IPortalUrlFactory portalUrlFactory;

    /** Internationalization bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;


    /** Application context. */
    private ApplicationContext applicationContext;


    /**
     * Constructor.
     */
    public ParticipantsServiceImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Configuration getConfiguration(PortalControllerContext portalControllerContext) throws PortletException {
        return this.repository.getConfiguration(portalControllerContext);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void saveConfiguration(PortalControllerContext portalControllerContext, Configuration configuration) throws PortletException {
        this.repository.saveConfiguration(portalControllerContext, configuration);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Participants getParticipants(PortalControllerContext portalControllerContext) throws PortletException {
        Participants participants = this.applicationContext.getBean(Participants.class);

        if (!participants.isLoaded()) {
            // Participants groups
            List<Group> groups = this.repository.getGroups(portalControllerContext);

            // Sort groups
            Collections.sort(groups, this.groupComparator);

            // Sort members
            for (Group group : groups) {
                Collections.sort(group.getMembers(), this.memberComparator);
            }

            participants.setGroups(groups);

            participants.setLoaded(true);
        }

        return participants;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public PanelPlayer getPlayer(PortalControllerContext portalControllerContext) throws PortletException {
        // Player
        PanelPlayer player = new PanelPlayer();

        // Instance
        player.setInstance("osivia-services-workspace-participants-instance");

        // Properties
        Map<String, String> properties = new HashMap<>();
        properties.put(ParticipantsRepository.VIEW_WINDOW_PROPERTY, View.FULL.toString());
        player.setProperties(properties);

        return player;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getMoreUrl(PortalControllerContext portalControllerContext) throws PortletException {
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        // Player
        PanelPlayer player = this.getPlayer(portalControllerContext);

        // Title
        String title = bundle.getString("WORKSPACE_PARTICIPANTS_TASK");

        // Window properties
        Map<String, String> properties = new HashMap<String, String>();
        if (player.getProperties() != null) {
            properties.putAll(player.getProperties());
        }
        properties.put("osivia.title", title);
        properties.put(ITaskbarService.TASK_ID_WINDOW_PROPERTY, TASK_ID);
        properties.put("osivia.back.reset", String.valueOf(true));
        properties.put("osivia.navigation.reset", String.valueOf(true));

        // URL
        String url;
        try {
            url = this.portalUrlFactory.getStartPortletUrl(portalControllerContext, player.getInstance(), properties);
        } catch (PortalException e) {
            throw new PortletException(e);
        }

        return url;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        ApplicationContextProvider.setApplicationContext(applicationContext);
    }

}
