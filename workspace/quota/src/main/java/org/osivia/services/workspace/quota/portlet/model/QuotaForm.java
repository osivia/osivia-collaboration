package org.osivia.services.workspace.quota.portlet.model;

import java.util.List;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;



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
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class QuotaForm {

	public enum QuotaProcedureStep {NONE, WARNING, QUOTA_REQUEST;}
	
	/** The asynchronous. */
	private boolean asynchronous = false;
	
	
	   /** The asynchronous. */
    private long ts = 0;
    
	/** The size message. */
	private String sizeMessage = "";

	
	private QuotaProcedureStep currentStep = QuotaProcedureStep.NONE;


	/** The ratio. */
	private int ratio=0;
	
	/** The ratio in trash. */
	private int trashRatio=0;



	/** Service informations. */
	private QuotaInformations infos;

	/** Big files */
	private List<BigFile> bigFiles;


	private Boolean administrator;
    


	/**
	 * Constructor.
	 */
	public QuotaForm() {
		super();
	}
	
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

	/**
	 * Get the trash ratio
	 * @return
	 */
	public int getTrashRatio() {
		return trashRatio;
	}

	/**
	 * Set the trash ratio
	 * @param trashRatio
	 */
	public void setTrashRatio(int trashRatio) {
		this.trashRatio = trashRatio;
	}

	

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
	 * Get procedure current step
	 * @return
	 */
	public QuotaProcedureStep getCurrentStep() {
		return currentStep;
	}

	/***
	 * Set procedure current step
	 * @param currentStep
	 */
	public void setCurrentStep(QuotaProcedureStep currentStep) {
		this.currentStep = currentStep;
	}

	public List<BigFile> getBigFiles() {
		return bigFiles;
	}

	public void setBigFiles(List<BigFile> bigFiles) {
		this.bigFiles = bigFiles;
		
	}


	
	public Boolean getAdministrator() {
		return administrator;
	}


	public void setAdministrator(Boolean administrator) {
		this.administrator = administrator;
		
	}



}
