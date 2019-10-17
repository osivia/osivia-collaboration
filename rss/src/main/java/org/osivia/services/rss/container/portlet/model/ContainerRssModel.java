package org.osivia.services.rss.container.portlet.model;

import java.util.List;

public class ContainerRssModel {

    public String displayName;
    public String url;
    public String partId;
    public String syncId;
    public List<RssModel> sources;

    public ContainerRssModel(String displayName, String url, String partId, String syncId) {
        this.displayName = displayName;
        this.url = url;
        this.partId = partId;
        this.syncId = syncId;
    }

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPartId() {
		return partId;
	}

	public void setPartId(String partId) {
		this.partId = partId;
	}

	public String getSyncId() {
		return syncId;
	}

	public void setSyncId(String syncId) {
		this.syncId = syncId;
	}

	public List<RssModel> getSources() {
		return sources;
	}

	public void setSources(List<RssModel> sources) {
		this.sources = sources;
	}

}
