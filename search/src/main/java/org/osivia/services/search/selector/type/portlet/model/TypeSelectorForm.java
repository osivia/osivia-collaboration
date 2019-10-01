package org.osivia.services.search.selector.type.portlet.model;

import java.util.List;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Type selector form java-bean.
 * 
 * @author Loïc Billon
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TypeSelectorForm {

    /** Label. */
    private String label;
    /** Scope. */
    private SearchType type;

    /** Scopes. */
    private List<SearchType> types;


    /**
     * Constructor.
     */
    public TypeSelectorForm() {
        super();
    }


    /**
     * Getter for label.
     * 
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Setter for label.
     * 
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }


    /**
     * 
     * @return
     */
	public SearchType getType() {
		return type;
	}

	/**
	 * 
	 * @param type
	 */
	public void setType(SearchType type) {
		this.type = type;
	}

	/**
	 * 
	 * @return
	 */
	public List<SearchType> getTypes() {
		return types;
	}

	/**
	 * 
	 * @param types
	 */
	public void setTypes(List<SearchType> types) {
		this.types = types;
	}

    
}
