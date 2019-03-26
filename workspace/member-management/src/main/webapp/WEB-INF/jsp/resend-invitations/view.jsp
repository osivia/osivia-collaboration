<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<portlet:defineObjects />


<portlet:actionURL name="submit" var="submitUrl" copyCurrentRenderParameters="true" />

<portlet:actionURL name="redirectTab" var="cancelUrl" copyCurrentRenderParameters="true">
    <portlet:param name="view" value="add-to-group"/>
    <portlet:param name="redirection" value="invitations"/>
</portlet:actionURL>


<div class="workspace-member-management">
    <!-- Tabs -->
    <%@ include file="../commons/tabs.jspf" %>
    
    <div class="panel panel-default">
        <div class="panel-body">
            <!-- Selection -->
            <p><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_RESEND_INVITATIONS_SELECTED_MEMBERS" /></p>
            <ul>
                <c:forEach var="member" items="${resendInvitationsForm.members}">
                    <li>
                        <p>
                            <span><ttc:user name="${member.id}" linkable="false" /></span>
                            <small class="text-muted"><op:translate key="${member.role.key}" classLoader="${member.role.classLoader}" /></small>
                        </p>
                    </li>
                </c:forEach>
            </ul>
            
            <hr>
            
            <!-- Form -->
            <form:form action="${submitUrl}" method="post" modelAttribute="resendInvitationsForm">
                <!-- Message -->
                <div class="form-group">
                    <c:set var="placeholder"><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_INVITATION_EDITION_MESSAGE_PLACEHOLDER" /></c:set>
                    <form:label path="message" cssClass="control-label"><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_INVITATION_MESSAGE"/></form:label>
                    <form:textarea path="message" rows="3" cssClass="form-control" placeholder="${placeholder}" />
                </div>
                
                <div class="form-group">
                    <button type="submit" class="btn btn-primary">
                        <span><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_INVITATION_EDITION_RESEND" /></span>
                    </button>
                    
                    <a href="${cancelUrl}" class="btn btn-default">
                        <span><op:translate key="CANCEL" /></span>
                    </a>
                </div>
            </form:form>
        </div>
    </div>
</div>




