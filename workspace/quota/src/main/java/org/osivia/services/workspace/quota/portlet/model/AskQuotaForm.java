package org.osivia.services.workspace.quota.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 
 * @author Loïc Billon
 *
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AskQuotaForm {

	/** Message for administrators */
	private String requestMsg;
	
	/** Quota procedure uuid */
	private String procedureUuid;

	public String getRequestMsg() {
		return requestMsg;
	}

	public void setRequestMsg(String requestMsg) {
		this.requestMsg = requestMsg;
	}

	public String getProcedureUuid() {
		return procedureUuid;
	}

	public void setProcedureUuid(String procedureUuid) {
		this.procedureUuid = procedureUuid;
	}
	
	
	
}
