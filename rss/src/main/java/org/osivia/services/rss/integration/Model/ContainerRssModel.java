package org.osivia.services.rss.integration.Model;

import java.util.List;
import java.util.UUID;

public class ContainerRssModel {

    public String titleConteneur;
    public String linkConteneur;
    public String descriptionConteneur;
    public String pubDateConteneur;
    public String idConteneur;
    public List<RssModel> sources;

    public ContainerRssModel(String titleConteneur, String linkConteneur, String descriptionConteneur, String pubDateConteneur, 
    		String idConteneur) {
        this.titleConteneur = titleConteneur;
        this.linkConteneur = linkConteneur;
        this.descriptionConteneur = descriptionConteneur;
        this.pubDateConteneur = pubDateConteneur;
        this.idConteneur = idConteneur;
    }

	public String getTitleConteneur() {
		return titleConteneur;
	}

	public void setTitleConteneur(String titleConteneur) {
		this.titleConteneur = titleConteneur;
	}

	public String getLinkConteneur() {
		return linkConteneur;
	}

	public void setLinkConteneur(String linkConteneur) {
		this.linkConteneur = linkConteneur;
	}

	public String getDescriptionConteneur() {
		return descriptionConteneur;
	}

	public void setDescriptionConteneur(String descriptionConteneur) {
		this.descriptionConteneur = descriptionConteneur;
	}

	public String getPubDateConteneur() {
		return pubDateConteneur;
	}

	public void setPubDateConteneur(String pubDateConteneur) {
		this.pubDateConteneur = pubDateConteneur;
	}

	public String getIdConteneur() {
		return idConteneur;
	}

	public void setIdConteneur() {
		this.idConteneur = UUID.randomUUID().toString();;
	}

	public List<RssModel> getSources() {
		return sources;
	}

	public void setSources(List<RssModel> sources) {
		this.sources = sources;
	}

}
