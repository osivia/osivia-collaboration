package org.osivia.services.rss.fluxRss.portlet.model;

public class ItemRssModel {

    public String idConteneur;   
    public String title;
    public String link;
    public String description;
    public String author;    
    public String pubDate;
    public String guid; 
    public String category;
    public String enclosure;
    public String sourceRss;

    public ItemRssModel(String title, String link, String description, String author, String pubDate, String guid, String idConteneur,
    		String category, String enclosure, String sourceRss) {
        this.idConteneur = idConteneur;
    	this.title = title;
        this.link = link;
        this.description = description;
        this.author = author;
        this.pubDate = pubDate;
        this.guid = guid;
        this.category = category;
        this.enclosure = enclosure;
        this.sourceRss = sourceRss;
    }

	public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getPubDate() {
        return pubDate;
    }

	public void setTitle(String title) {
		this.title = title;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

	public String getIdConteneur() {
		return idConteneur;
	}

	public void setIdConteneur(String idConteneur) {
		this.idConteneur = idConteneur;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getEnclosure() {
		return enclosure;
	}

	public void setEnclosure(String enclosure) {
		this.enclosure = enclosure;
	}

	public String getSourceRss() {
		return sourceRss;
	}

	public void setSourceRss(String sourceRss) {
		this.sourceRss = sourceRss;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
	
}
