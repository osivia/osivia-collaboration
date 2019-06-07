package org.osivia.services.workspace.quota.portlet.model;

import java.util.List;

import org.osivia.portal.api.portlet.Refreshable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;


/**
 * Quota form java-bean.
 * 
 * @author Jean-Sébastien Steux
 */
@Component
@Scope(WebApplicationContext.SCOPE_SESSION)
@Refreshable
public class QuotaForm {

   
    /**  global quota. */
    private long globalQuota;
    
    /**  Service informations. */
    private QuotaInformations infos;
    

    /**
     * Gets the infos.
     *
     * @return the infos
     */
    public QuotaInformations getInfos() {
		return infos;
	}



	/**
	 * Sets the infos.
	 *
	 * @param infos the new infos
	 */
	public void setInfos(QuotaInformations infos) {
		this.infos = infos;
	}



	/**
     * Gets the global quota.
     *
     * @return the global quota
     */
    public long getGlobalQuota() {
		return globalQuota;
	}



	/**
	 * Sets the global quota.
	 *
	 * @param globalQuota the new global quota
	 */
    
	public void setGlobalQuota(long globalQuota) {
		this.globalQuota = globalQuota;
	}

	/** Loaded indicator. */
    private boolean loaded;


    /**
     * Constructor.
     */
    public QuotaForm() {
        super();
    }




    /**
     * Getter for loaded.
     * 
     * @return the loaded
     */
    public boolean isLoaded() {
        return loaded;
    }

    /**
     * Setter for loaded.
     * 
     * @param loaded the loaded to set
     */
    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

}
