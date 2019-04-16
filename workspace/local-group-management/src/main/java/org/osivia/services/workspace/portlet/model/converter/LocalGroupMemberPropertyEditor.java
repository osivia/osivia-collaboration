package org.osivia.services.workspace.portlet.model.converter;

import java.beans.PropertyEditorSupport;

import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.osivia.services.workspace.portlet.model.LocalGroupMember;
import org.osivia.services.workspace.portlet.service.LocalGroupsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Local group member property editor.
 * 
 * @author Cédric Krommenhoek
 * @see PropertyEditorSupport
 */
@Component
public class LocalGroupMemberPropertyEditor extends PropertyEditorSupport {

    /** Person service. */
    @Autowired
    private PersonService personService;

    /** Portlet service. */
    @Autowired
    @Qualifier("edit")
    private LocalGroupsService service;


    /**
     * Constructor.
     */
    public LocalGroupMemberPropertyEditor() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        // Person
        Person person = this.personService.getPerson(text);

        if (person != null) {
            // Member
            LocalGroupMember member = this.service.convertPersonToMember(person);

            this.setValue(member);
        }
    }

}
