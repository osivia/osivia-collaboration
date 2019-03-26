package org.osivia.services.workspace.portlet.controller;

import org.osivia.services.workspace.portlet.model.AddInvitationsToGroupForm;
import org.osivia.services.workspace.portlet.model.Invitation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Add invitations to group portlet controller.
 * 
 * @author Cédric Krommenhoek
 */
@Controller
@RequestMapping(path = "VIEW", params = {"tab=invitations", "view=add-to-group"})
public class MemberManagementAddInvitationsToGroupController extends AbstractMemberManagementAddToGroupController<Invitation, AddInvitationsToGroupForm> {

    /**
     * Constructor.
     */
    public MemberManagementAddInvitationsToGroupController() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Class<Invitation> getMemberType() {
        return Invitation.class;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Class<AddInvitationsToGroupForm> getFormType() {
        return AddInvitationsToGroupForm.class;
    }

}
