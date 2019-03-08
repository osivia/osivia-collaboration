package org.osivia.services.workspace.portlet.controller;

import org.osivia.services.workspace.portlet.model.ChangeMembersRoleForm;
import org.osivia.services.workspace.portlet.model.Member;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Change members role portlet controller.
 * 
 * @author Cédric Krommenhoek
 * @see AbstractMemberManagementChangeRoleController
 * @see Member
 * @see ChangeMembersRoleForm
 */
@Controller
@RequestMapping(path = "VIEW", params = {"tab=members", "view=change-role"})
public class MemberManagementChangeMemberRoleController extends AbstractMemberManagementChangeRoleController<Member, ChangeMembersRoleForm> {

    /**
     * Constructor.
     */
    public MemberManagementChangeMemberRoleController() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Class<ChangeMembersRoleForm> getFormType() {
        return ChangeMembersRoleForm.class;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Class<Member> getMemberType() {
        return Member.class;
    }

}
