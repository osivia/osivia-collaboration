package org.osivia.services.workspace.quota.portlet.model;

import java.util.List;

import org.osivia.portal.api.portlet.Refreshable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;


/**
 * Update form java-bean.
 * 
 * @author Jean-Sébastien Steux
 */

@Component
public class UpdateForm {

	/** The size. */
	private String size;

	
    public String getSize() {
        return size;
    }


    
    public void setSize(String size) {
        this.size = size;
    }




    /**
	 * Constructor.
	 */
	public UpdateForm() {
		super();
	}



}
