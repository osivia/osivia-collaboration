package org.osivia.services.workspace.sharing.common.repository;

/**
 * Sharing common repository interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface SharingCommonRepository {

    /** Sharing facet. */
    String SHARING_FACET = "Sharing";

    /** Sharing author Nuxeo document property. */
    String SHARING_AUTHOR_PROPERTY = "sharing:author";
    /** Sharing link identifier Nuxeo document property. */
    String SHARING_LINK_ID_PROPERTY = "sharing:linkId";
    /** Sharing link permission Nuxeo document property. */
    String SHARING_LINK_PERMISSION_PROPERTY = "sharing:linkPermission";
    /** Sharing banned users Nuxeo document property. */
    String SHARING_BANNED_USERS_PROPERTY = "sharing:bannedUsers";

}
