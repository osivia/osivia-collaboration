<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:actionURL name="submit" var="submitUrl">
    <portlet:param name="view" value="invitation-edition" />
    <portlet:param name="invitationPath" value="${invitationEditionForm.path}"/>
</portlet:actionURL>


<div class="workspace-member-management">
    <!-- Tabs -->
    <jsp:include page="../commons/tabs.jsp" />
    
    <hr>
    
    <form:form action="${submitUrl}" method="post" modelAttribute="invitationEditionForm" cssClass="form-horizontal" role="form">
        <!-- Recipient -->
        <div class="form-group">
            <label class="control-label col-sm-3 col-lg-2"><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_INVITATION_RECIPIENT" /></label>
            <div class="col-sm-9 col-lg-10">
                <c:set var="person" scope="request" value="${invitationEditionForm.invitation}" />
                <jsp:include page="../commons/person.jsp" />
            </div>
        </div>
        
        <!-- Creation date -->
        <div class="form-group">
            <label class="control-label col-sm-3 col-lg-2"><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_INVITATION_CREATION_DATE" /></label>
            <div class="col-sm-9 col-lg-10">
                <p class="form-control-static">
                    <span><fmt:formatDate value="${invitationEditionForm.invitation.date}" type="both" dateStyle="full" timeStyle="short" /></span>
                </p>
            </div>
        </div>
        
        <!-- Last resending date -->
        <div class="form-group">
            <label class="control-label col-sm-3 col-lg-2"><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_INVITATION_LAST_RESENDING_DATE" /></label>
            <div class="col-sm-9 col-lg-10">
                <p class="form-control-static">
                    <c:choose>
                        <c:when test="${empty invitationEditionForm.invitation.resendingDate}">
                            <span class="text-muted"><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_INVITATION_NO_RESENDING"/></span>                            
                        </c:when>
                        <c:otherwise>
                            <span><fmt:formatDate value="${invitationEditionForm.invitation.resendingDate}" type="both" dateStyle="full" timeStyle="short" /></span>
                        </c:otherwise>
                    </c:choose>
                </p>
            </div>
        </div>
        
        <!-- Last message -->
        <div class="form-group">
            <label class="control-label col-sm-3 col-lg-2"><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_INVITATION_LAST_MESSAGE" /></label>
            <div class="col-sm-9 col-lg-10">
                <p class="form-control-static">
                    <c:choose>
                        <c:when test="${empty invitationEditionForm.invitation.message}"><span class="text-muted">-</span></c:when>
                        <c:otherwise><span class="text-pre-wrap">${invitationEditionForm.invitation.message}</span></c:otherwise>
                    </c:choose>
                </p>
            </div>
        </div>
        
        <fieldset>
            <legend><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_INVITATION_RESENDING_LEGEND" /></legend>
            
            <!-- New message -->
            <div class="form-group">
                <c:set var="placeholder"><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_INVITATION_EDITION_MESSAGE_PLACEHOLDER" /></c:set>
                <form:label path="message" cssClass="control-label col-sm-3 col-lg-2"><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_INVITATION_EDITION_MESSAGE" /></form:label>
                <div class="col-sm-9 col-lg-10">
                    <form:textarea path="message" cssClass="form-control" placeholder="${placeholder}" />
                </div>
            </div>
            
            <div class="form-group">
                <div class="col-sm-offset-3 col-sm-9 col-lg-offset-2 col-lg-10">
                    <button type="submit" name="resend" class="btn btn-primary">
                        <i class="glyphicons glyphicons-message-out"></i>
                        <span><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_INVITATION_EDITION_RESEND" /></span>
                    </button>
                </div>
            </div>
        </fieldset>
        
        
        <fieldset>
            <legend><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_INVITATION_EDITION_LEGEND"/></legend>
            
            <div class="alert alert-info"><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_INVITATION_EDITION_SAVE_HELP" /></div>
            
            <!-- Role -->
            <div class="form-group">
                <form:label path="invitation.role" cssClass="control-label col-sm-3 col-lg-2"><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_ROLE" /></form:label>
                <div class="col-sm-9 col-lg-10">
                    <form:select path="invitation.role" cssClass="form-control">
                        <c:forEach var="role" items="${options.roles}">
                            <form:option value="${role}"><op:translate key="${role.key}" classLoader="${role.classLoader}" /></form:option>
                        </c:forEach>
                    </form:select>
                </div>
            </div>
            
            <!-- Local groups -->
            <div class="form-group">
                <form:label path="invitation.localGroups" cssClass="control-label col-sm-3 col-lg-2"><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_INVITATION_LOCAL_GROUPS" /></form:label>
                <div class="col-sm-9 col-lg-10">
                    <form:select path="invitation.localGroups" cssClass="form-control select2 select2-default">
                        <c:forEach var="group" items="${options.workspaceLocalGroups}">
                            <form:option value="${group.cn}">${group.displayName}</form:option>
                        </c:forEach>
                    </form:select>
                </div>
            </div>
        
            <!-- Buttons -->
            <div class="form-group">
                <div class="col-sm-offset-3 col-sm-9 col-lg-offset-2 col-lg-10">
                    <div class="clearfix">
                        <!-- Save -->
                        <button type="submit" name="save" class="btn btn-primary">
                            <i class="glyphicons glyphicons-floppy-disk"></i>
                            <span><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_INVITATION_EDITION_SAVE"/></span>
                        </button>
                        
                        <!-- Cancel -->
                        <button type="submit" name="cancel" class="btn btn-default">
                            <span><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_INVITATION_EDITION_CANCEL"/></span>
                        </button>
    
                        <!-- Delete -->                    
                        <button type="submit" name="delete" class="btn btn-default pull-right">
                            <i class="glyphicons glyphicons-bin"></i>
                            <span><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_INVITATION_EDITION_DELETE"/></span>
                        </button>
                    </div>
                </div>
            </div>
        </fieldset>
    </form:form>
</div>
