package org.osivia.services.rss.feedRss.portlet.model;

import java.util.Date;

public class ItemRssModel {

    public String idConteneur;   
    public String title;
    public String link;
    public String description;
    public String author;    
    public Date pubDate;
    public String guid; 
    public String category;
    public String enclosure;
    public String sourceRss;
    public String path;

    public ItemRssModel(String title, String link, String description, String author, Date pubDate, String guid, String idConteneur,
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

    public ItemRssModel() {
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

	public void setTitle(String title) {
		this.title = title;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((guid == null) ? 0 : guid.hashCode());
        return result;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ItemRssModel other = (ItemRssModel) obj;
        if (guid == null) {
            if (other.guid != null)
                return false;
        } else if (!guid.equals(other.guid))
            return false;
        return true;
    }

	public Date getPubDate() {
		return pubDate;
	}

	public void setPubDate(Date pubDate) {
		this.pubDate = pubDate;
	}	
	
}
