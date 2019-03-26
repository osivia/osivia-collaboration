package org.osivia.services.workspace.portlet.controller;

import org.osivia.services.workspace.portlet.model.AddMembersToGroupForm;
import org.osivia.services.workspace.portlet.model.Member;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Add members to group portlet controller.
 * 
 * @author Cédric Krommenhoek
 */
@Controller
@RequestMapping(path = "VIEW", params = {"tab=members", "view=add-to-group"})
public class MemberManagementAddMembersToGroupController extends AbstractMemberManagementAddToGroupController<Member, AddMembersToGroupForm> {

    /**
     * Constructor.
     */
    public MemberManagementAddMembersToGroupController() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Class<Member> getMemberType() {
        return Member.class;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Class<AddMembersToGroupForm> getFormType() {
        return AddMembersToGroupForm.class;
    }

}
