package org.osivia.services.rss.common.model;

import java.util.Map;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FeedRssModel {

	public String displayName;
	public String url;
	public String syncId;
	public Map<String, String> map;
	public Picture visual;
	public int indexNuxeo;

	public FeedRssModel() {
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getSyncId() {
		return syncId;
	}

	public void setSyncId(String syncId) {
		this.syncId = syncId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}

	public Picture getVisual() {
		return visual;
	}

	public void setVisual(Picture visual) {
		this.visual = visual;
	}

	public int getIndexNuxeo() {
		return indexNuxeo;
	}

	public void setIndexNuxeo(int indexNuxeo) {
		this.indexNuxeo = indexNuxeo;
	}
}
