package org.osivia.services.forum.thread.portlet.model;

import org.apache.commons.lang.StringUtils;

/**
 * Forum thread parser actions enumeration.
 *
 * @author Cédric Krommenhoek
 */
public enum ForumThreadParserAction {

    /** Load. */
    LOAD,
    /** Save. */
    SAVE;


    /** Identifier. */
    private final String id;


    private ForumThreadParserAction() {
        this.id = StringUtils.lowerCase(this.name());
    }


    /**
     * Getter for id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

}
