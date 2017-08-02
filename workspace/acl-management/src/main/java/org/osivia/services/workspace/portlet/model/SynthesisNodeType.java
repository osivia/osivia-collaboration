package org.osivia.services.workspace.portlet.model;

/**
 * Synthesis node types enumeration.
 * 
 * @author Cédric Krommenhoek
 */
public enum SynthesisNodeType {

    /** Workspace role node type. */
    ROLE(1, "glyphicons glyphicons-shield"),
    /** Public group node type. */
    PUBLIC_GROUP(2, "glyphicons glyphicons-unlock"),
    /** Group node type. */
    GROUP(3, "glyphicons glyphicons-group"),
    /** User node type. */
    USER(4, "glyphicons glyphicons-user");


    /** Node sorting order. */
    private final int order;
    /** Node icon. */
    private final String icon;


    /**
     * Constructor.
     * 
     * @param order node sorting order
     * @param icon node icon
     */
    private SynthesisNodeType(int order, String icon) {
        this.order = order;
        this.icon = icon;
    }


    /**
     * Getter for order.
     * 
     * @return the order
     */
    public int getOrder() {
        return order;
    }

    /**
     * Getter for icon.
     * 
     * @return the icon
     */
    public String getIcon() {
        return icon;
    }

}
