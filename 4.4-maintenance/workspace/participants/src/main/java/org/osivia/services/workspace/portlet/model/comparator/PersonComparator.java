package org.osivia.services.workspace.portlet.model.comparator;

import java.util.Comparator;

import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.directory.v2.model.Person;
import org.springframework.stereotype.Component;

/**
 * Person comparator.
 * 
 * @author ckrommenhoek
 * @see Comparator
 * @see Person
 */
@Component
public class PersonComparator implements Comparator<Person> {

    /**
     * Constructor.
     */
    public PersonComparator() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(Person person1, Person person2) {
        int result;

        String displayName1;
        if (person1 == null) {
            displayName1 = null;
        } else {
            displayName1 = StringUtils.defaultIfBlank(person1.getDisplayName(), person1.getUid());
        }

        String displayName2;
        if (person2 == null) {
            displayName2 = null;
        } else {
            displayName2 = StringUtils.defaultIfBlank(person2.getDisplayName(), person2.getUid());
        }

        if (displayName1 == null) {
            result = -1;
        } else if (displayName2 == null) {
            result = 1;
        } else {
            return displayName1.compareToIgnoreCase(displayName2);
        }

        return result;
    }

}
