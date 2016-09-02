package org.osivia.services.workspace.portlet.model.converter;

import java.beans.PropertyEditorSupport;

import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.osivia.services.workspace.portlet.model.Invitation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Invitation property editor.
 * 
 * @author Cédric Krommenhoek
 * @see PropertyEditorSupport
 */
@Component
public class InvitationPropertyEditor extends PropertyEditorSupport {

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /** Person service. */
    @Autowired
    private PersonService personService;


    /**
     * Constructor.
     */
    public InvitationPropertyEditor() {
        super();
    }


    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        // Person
        Person person = this.personService.getPerson(text);

        // Invitation
        Invitation invitation;
        if (person == null) {
            invitation = this.applicationContext.getBean(Invitation.class, text);
        } else {
            invitation = this.applicationContext.getBean(Invitation.class, person);
        }

        this.setValue(invitation);
    }

}
