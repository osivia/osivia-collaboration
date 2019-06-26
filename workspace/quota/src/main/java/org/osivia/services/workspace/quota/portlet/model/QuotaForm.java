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
/**
 * @author Jean-Sébastien
 *
 */
@Component

public class QuotaForm {

	/** The asynchronous. */
	private boolean asynchronous = false;
	
	
	   /** The asynchronous. */
    private long ts = 0;
    
    
    
	
    public long getTs() {
        return ts;
    }

    
    public void setTs(long ts) {
        this.ts = ts;
    }

    /**
	 * Checks if is asynchronous.
	 *
	 * @return true, if is asynchronous
	 */
	public boolean isAsynchronous() {
		return asynchronous;
	}

	/**
	 * Sets the asynchronous.
	 *
	 * @param asynchronous the new asynchronous
	 */
	public void setAsynchronous(boolean asynchronous) {
		this.asynchronous = asynchronous;
	}


	/** The size message. */
	private String sizeMessage = "";

	/**
	 * Gets the size message.
	 *
	 * @return the size message
	 */
	public String getSizeMessage() {
		return sizeMessage;
	}

	/**
	 * Sets the size message.
	 *
	 * @param sizeMessage the new size message
	 */
	public void setSizeMessage(String sizeMessage) {
		this.sizeMessage = sizeMessage;
	}
	
	
	/** The ratio. */
	private int ratio=0;
	
	

	/**
	 * Gets the ratio.
	 *
	 * @return the ratio
	 */
	public int getRatio() {
		return ratio;
	}

	/**
	 * Sets the ratio.
	 *
	 * @param ratio the new ratio
	 */
	public void setRatio(int ratio) {
		this.ratio = ratio;
	}


	/** Service informations. */
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
	 * @param infos
	 *            the new infos
	 */
	public void setInfos(QuotaInformations infos) {
		this.infos = infos;
	}


	/**
	 * Constructor.
	 */
	public QuotaForm() {
		super();
	}



}
