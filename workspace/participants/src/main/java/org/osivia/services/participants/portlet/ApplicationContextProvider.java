/*
 * (C) Copyright 2016 OSIVIA (http://www.osivia.com) 
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
package org.osivia.services.participants.portlet;

import org.springframework.context.ApplicationContext;

/**
 * Application context provider.
 * 
 * @author Cédric Krommenhoek
 */
public class ApplicationContextProvider {

    /** Application context. */
    private static ApplicationContext applicationContext;


    /**
     * Private constructor, prevent instanciation.
     */
    private ApplicationContextProvider() {
        // Do nothing
    }


    /**
     * Getter for applicationContext.
     * 
     * @return the applicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * Setter for applicationContext.
     * 
     * @param applicationContext the applicationContext to set
     */
    public static void setApplicationContext(ApplicationContext applicationContext) {
        ApplicationContextProvider.applicationContext = applicationContext;
    }

}
