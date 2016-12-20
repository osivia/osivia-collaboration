/**
 * 
 */
package org.osivia.services.workspace.portlet.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.nuxeo.ecm.automation.client.model.DocumentPermissions;
import org.osivia.directory.v2.model.ext.WorkspaceRole;
import org.osivia.services.workspace.portlet.model.AclEntries;
import org.osivia.services.workspace.portlet.model.AclEntry;
import org.osivia.services.workspace.portlet.model.Role;


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
    
    /**
     * Build Permissions from AclEntries.
     * 
     * @param aclEntries
     * @return Permissions
     */
    public static List<Permission> buildPermissionsList(AclEntries aclEntries) {
        List<Permission> permissions = new ArrayList<Permission>();
        
        if(aclEntries != null && CollectionUtils.isNotEmpty(aclEntries.getEntries())){
            for(AclEntry aclEntry : aclEntries.getEntries()){
                Permission permission = buildPermission(aclEntry);
                permissions.add(permission);
            }
        }
        
        return permissions;
    }
    
    /**
     * Build Permission from AclEntry.
     * 
     * @param aclEntry
     * @return Permission
     */
    public static Permission buildPermission(AclEntry aclEntry) {
        Permission permission = new Permission();
        
        permission.setName(aclEntry.getId());
        Role role = aclEntry.getRole();
        WorkspaceRole workspaceRole = WorkspaceRole.fromId(role.getId());
        permission.setValues(Arrays.asList(workspaceRole.getPermissions()));
        
        return permission;
    }
    
    /**
     * Build updated Permission from AclEntry.
     * The returned permissions indicates to backOffice to remove all permissions
     * of user associated to this entry.
     * 
     * @param aclEntry
     * @return Permission
     */
    public static Permission buildOldPermission(AclEntry aclEntry) {
        Permission permission = new Permission();
        
        permission.setName(aclEntry.getId());
        permission.setValues(Arrays.asList(new String[] {"*"}));
        
        return permission;
    }
    
}
