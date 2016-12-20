/**
 * 
 */
package org.osivia.services.workspace.portlet.repository;

import java.util.List;

import org.nuxeo.ecm.automation.client.model.DocumentPermissions;


/**
 * @author david
 *
 */
public class PermissionsAdapter {
    
    /** Inherited group permissions identifier. */
    public static final String INHERITED_GROUP_PERMISSIONS = "inherited";
    /** Local group permissions identifier. */
    public static final String LOCAL_GROUP_PERMISSIONS = "local"; 

    /**
     * Utility class.
     */
    private PermissionsAdapter() {
        super();
    }

    /**
     * Converts portlet Permission object list
     * to Nuxeo DocumentPermission object list.
     * 
     * @param permissions portlet
     * @return documentpermissions (Nuxeo document)
     */
    public static DocumentPermissions getAs(List<Permission> permissions){
        DocumentPermissions docPermissions = new DocumentPermissions(0);
        if(permissions != null) {
            for(Permission permission : permissions){
                String usrGrp = permission.getName();
                
                for(String right : permission.getValues()){
                    docPermissions.set(usrGrp, right);
                }
            }
        }
        return docPermissions;
    }
    
}
