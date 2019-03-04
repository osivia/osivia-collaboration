<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>


<c:set var="namespace"><portlet:namespace /></c:set>

<portlet:actionURL name="purge" var="purgeUrl">
    <portlet:param name="tab" value="invitations" />
    <portlet:param name="sort" value="${sort}" />
    <portlet:param name="alt" value="${alt}" />
</portlet:actionURL>


<c:if test="${invitations.purgeAvailable}">
    <p>
        <button type="button" class="btn btn-default" data-toggle="modal" data-target="#${namespace}-purge-confirmation">
            <i class="glyphicons glyphicons-cleaning"></i>
            <span><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_INVITATIONS_PURGE" /></span>
        </button>
    </p>
    
    <!-- Modal -->
    <div id="${namespace}-purge-confirmation" class="modal fade" tabindex="-1" role="dialog">
        <div class="modal-dialog modal-sm" role="document">
            <div class="modal-content">
                <div class="modal-body">
                    <p class="text-center"><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_INVITATIONS_PURGE_CONFIRMATION_MESSAGE" /></p>
                
                    <p class="text-center">
                        <a href="${purgeUrl}" class="btn btn-default" data-dismiss="modal">
                            <span><op:translate key="YES" /></span>
                        </a>
                    
                        <button type="button" class="btn btn-default" data-dismiss="modal">
                            <span><op:translate key="NO" /></span>
                        </button>
                    </p>
                </div>
            </div>
        </div>
    </div>
</c:if>
