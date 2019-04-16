package org.osivia.services.workspace.portlet.controller;

import org.osivia.services.workspace.portlet.model.ChangeInvitationRequestsRoleForm;
import org.osivia.services.workspace.portlet.model.InvitationRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Change invitation requests role portlet controller.
 * 
 * @author Cédric Krommenhoek
 * @see AbstractMemberManagementChangeRoleController
 * @see InvitationRequest
 * @see ChangeInvitationRequestsRoleForm
 */
@Controller
@RequestMapping(path = "VIEW", params = {"tab=requests", "view=change-role"})
public class MemberManagementChangeInvitationRequestRoleController
        extends AbstractMemberManagementChangeRoleController<InvitationRequest, ChangeInvitationRequestsRoleForm> {

    /**
     * Constructor.
     */
    public MemberManagementChangeInvitationRequestRoleController() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Class<ChangeInvitationRequestsRoleForm> getFormType() {
        return ChangeInvitationRequestsRoleForm.class;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Class<InvitationRequest> getMemberType() {
        return InvitationRequest.class;
    }

}
