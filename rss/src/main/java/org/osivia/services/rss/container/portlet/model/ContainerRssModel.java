package org.osivia.services.rss.container.portlet.model;

import java.util.List;

public class ContainerRssModel {

	public String name;
	public String path;
	public String displayName;
	public String url;
	public String partId;
	public String syncId;
	public List<ItemRssModel> sources;

	public ContainerRssModel() {
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

	public List<ItemRssModel> getSources() {
		return sources;
	}

	public void setSources(List<ItemRssModel> sources) {
		this.sources = sources;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
