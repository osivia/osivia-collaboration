package org.osivia.services.workspace.portlet.service;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.osivia.directory.v2.model.CollabProfile;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.services.workspace.portlet.model.LocalGroupForm;
import org.osivia.services.workspace.portlet.model.LocalGroupMember;
import org.osivia.services.workspace.portlet.model.LocalGroupsSummary;
import org.osivia.services.workspace.portlet.repository.LocalGroupsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * Edit local group portlet service implementation.
 * 
 * @author Cédric Krommenhoek
 * @see LocalGroupsServiceImpl
 * @see EditLocalGroupService
 */
@Service("edit")
public class EditLocalGroupServiceImpl extends LocalGroupsServiceImpl implements EditLocalGroupService {

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /** Portlet repository. */
    @Autowired
    private LocalGroupsRepository repository;

    /** Internationalization bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;

    /** Notifications service. */
    @Autowired
    private INotificationsService notificationsService;


    /**
     * Constructor.
     */
    public EditLocalGroupServiceImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public LocalGroupForm getForm(PortalControllerContext portalControllerContext, String id) throws PortletException {
        // Local group
        CollabProfile group = this.repository.getLocalGroup(portalControllerContext, id);

        // Form
        LocalGroupForm form = this.applicationContext.getBean(LocalGroupForm.class);
        form.setId(id);
        if (group != null) {
            form.setDisplayName(group.getDisplayName());
            form.setDescription(group.getDescription());
        }

        // Members
        List<Person> persons = this.repository.getLocalGroupMembers(portalControllerContext, id);
        if (CollectionUtils.isNotEmpty(persons)) {
            List<LocalGroupMember> members = new ArrayList<>(persons.size());
            for (Person person : persons) {
                LocalGroupMember member = this.convertPersonToMember(person);
                members.add(member);
            }
            form.setMembers(members);
        }

        return form;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<LocalGroupMember> getAllMembers(PortalControllerContext portalControllerContext) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        
        // Local group identifier
        String id = request.getParameter("id");
        
        // Local group members
        List<LocalGroupMember> localGroupMembers;
        if (StringUtils.isEmpty(id)) {
            localGroupMembers = null;
        } else {
            List<Person> persons = this.repository.getLocalGroupMembers(portalControllerContext, id);
            if (CollectionUtils.isEmpty(persons)) {
                localGroupMembers = null;
            } else {
                localGroupMembers = new ArrayList<>(persons.size());
                for (Person person : persons) {
                    LocalGroupMember member = this.applicationContext.getBean(LocalGroupMember.class);
                    member.setUid(person.getUid());
                    localGroupMembers.add(member);
                }
            }
        }
        
        // All workspace members
        List<LocalGroupMember> allMembers = super.getAllMembers(portalControllerContext);

        if (CollectionUtils.isNotEmpty(localGroupMembers) && CollectionUtils.isNotEmpty(allMembers)) {
            for (LocalGroupMember member : allMembers) {
                if (localGroupMembers.contains(member)) {
                    member.setSelected(true);
                }
            }
        }

        return allMembers;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void edit(PortalControllerContext portalControllerContext, LocalGroupForm form) throws PortletException {
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        this.repository.editLocalGroup(portalControllerContext, form);

        // Force summary update
        LocalGroupsSummary summary = this.getSummary(portalControllerContext);
        summary.setLoaded(false);

        // Notification
        String message = bundle.getString("MESSAGE_SUCCESS_EDIT_LOCAL_GROUP", form.getDisplayName());
        this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
    }

}
