/*
 * (C) Copyright 2016 Académie de Rennes (http://www.ac-rennes.fr/), OSIVIA (http://www.osivia.com) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 */
package org.osivia.services.pad.portlet.controller;

/**
 * @author Loïc Billon
 *
 */
public class PadModel {

	private String url;
	
	private Boolean editionMode = false;

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the editionMode
	 */
	public Boolean getEditionMode() {
		return editionMode;
	}

	/**
	 * @param editionMode the editionMode to set
	 */
	public void setEditionMode(Boolean editionMode) {
		this.editionMode = editionMode;
	}
	
	

	
}
