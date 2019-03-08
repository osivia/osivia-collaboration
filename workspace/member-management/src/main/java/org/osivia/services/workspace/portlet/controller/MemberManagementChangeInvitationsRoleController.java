package org.osivia.services.workspace.portlet.controller;

import org.osivia.services.workspace.portlet.model.ChangeInvitationsRoleForm;
import org.osivia.services.workspace.portlet.model.Invitation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Change invitations role portlet controller.
 * 
 * @author Cédric Krommenhoek
 * @see AbstractMemberManagementChangeRoleController
 * @see Invitation
 * @see ChangeInvitationsRoleForm
 */
@Controller
@RequestMapping(path = "VIEW", params = {"tab=invitations", "view=change-role"})
public class MemberManagementChangeInvitationsRoleController extends AbstractMemberManagementChangeRoleController<Invitation, ChangeInvitationsRoleForm> {

    /**
     * Constructor.
     */
    public MemberManagementChangeInvitationsRoleController() {
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
    public Class<ChangeInvitationsRoleForm> getFormType() {
        return ChangeInvitationsRoleForm.class;
    }

}
