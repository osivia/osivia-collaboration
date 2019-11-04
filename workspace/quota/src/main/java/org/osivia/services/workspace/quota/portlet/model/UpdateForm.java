package org.osivia.services.workspace.quota.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


/**
 * Update form java-bean.
 * 
 * @author Jean-Sébastien Steux
 */

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UpdateForm {


	/** The current size setted. */
	private String currentSize;

	
	/** The size. */
	private String size;
	
	/** User request message */
	private String message;


	private String uuid;


	private boolean stepRequest = false;

	
    public String getSize() {
        return size;
    }
    
    public void setSize(String size) {
        this.size = size;
    }

	public String getCurrentSize() {
		return currentSize;
	}

	public void setCurrentSize(String currentSize) {
		this.currentSize = currentSize;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public void setProcedureUuid(String uuid) {
		this.uuid = uuid;
		
	}

	public void setStepRequest(boolean stepRequest) {
		this.stepRequest = stepRequest;
		
	}

	public boolean isStepRequest() {
		return stepRequest;
	}

	
    


}
