<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:actionURL name="create" var="createUrl">
    <portlet:param name="tab" value="invitations" />
    <portlet:param name="sort" value="${sort}" />
    <portlet:param name="alt" value="${alt}" />
</portlet:actionURL>

<portlet:resourceURL id="search" var="searchUrl" />


<c:set var="namespace"><portlet:namespace /></c:set>


<div class="well clearfix relative">
    <div id="shadowbox-${namespace}" class="ajax-shadowbox">
        <div class="progress">
            <div class="progress-bar progress-bar-striped active" role="progressbar">
                <strong><op:translate key="AJAX_REFRESH" /></strong>
            </div>
        </div>
    </div>

    <form:form action="${createUrl}" method="post" modelAttribute="creation" role="form">
        <fieldset>
            <c:if test="${creation.warning}">
                <div class="alert alert-warning">
                    <span><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_INVITATIONS_CREATE_PERSON_WARNING" /></span>
                    <ul>
                        <c:forEach var="invitation" items="${creation.pendingInvitations}">
                            <c:if test="${invitation.unknownUser}">
                                <li>${invitation.id}</li>
                            </c:if>
                        </c:forEach>
                    </ul>
                </div>
            </c:if>
            
            <div class="row">
                <div class="col-sm-8 col-lg-9">
                    <!-- Invitations -->
                    <c:set var="placeholder"><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_CREATE_INVITATIONS_ADD_PERSONS_PLACEHOLDER" /></c:set>
                    <c:set var="inputTooShort"><op:translate key="SELECT2_INPUT_TOO_SHORT" args="3" /></c:set>
                    <c:set var="noResults"><op:translate key="SELECT2_NO_RESULTS" /></c:set>
                    <c:set var="searching"><op:translate key="SELECT2_SEARCHING" /></c:set>
                    <c:set var="loadingMore"><op:translate key="SELECT2_LOADING_MORE"/></c:set>
                    <spring:bind path="pendingInvitations">
                        <div class="form-group ${status.error ? 'has-error' : (creation.warning ? 'has-warning' : '')}">
                            <form:label path="pendingInvitations" cssClass="control-label"><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_CREATE_INVITATIONS_ADD_PERSONS_LABEL" /></form:label>
                            <form:select path="pendingInvitations" cssClass="form-control select2 select2-invitation" data-placeholder="${placeholder}" data-url="${searchUrl}" data-input-too-short="${inputTooShort}" data-no-results="${noResults}" data-searching="${searching}" data-loading-more="${loadingMore}">
                                <c:forEach var="invitation" items="${creation.pendingInvitations}">
                                    <form:option value="${invitation.id}" data-avatar="${invitation.person.avatar.url}">${invitation.displayName}</form:option>
                                </c:forEach>
                            </form:select>
                            <form:errors path="pendingInvitations" cssClass="help-block" />
                        </div>
                    </spring:bind>
                </div>
                
                <div class="col-sm-4 col-lg-3">
                    <!-- Role -->
                    <div class="form-group">
                        <form:label path="role" cssClass="control-label"><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_ROLE" /></form:label>
                        <form:select path="role" cssClass="form-control">
                            <c:forEach var="role" items="${options.roles}">
                                <form:option value="${role}"><op:translate key="${role.key}" classLoader="${role.classLoader}"/></form:option>
                            </c:forEach>
                        </form:select>
                    </div>
                </div>
            </div>
            
            
            <!-- Other options -->
            <p>                
                <a href="#${namespace}-other-options" class="no-ajax-link" data-toggle="collapse">
                    <span><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_INVITATION_OTHER_OPTIONS" /></span>
                </a>
            </p>
            <div id="${namespace}-other-options" class="collapse invitations-other-options">
                <!-- Local groups -->
                <div class="form-group">
                    <form:label path="localGroups" cssClass="control-label"><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_INVITATION_LOCAL_GROUPS" /></form:label>
                    <form:select path="localGroups" cssClass="form-control select2 select2-default">
                        <c:forEach var="group" items="${options.workspaceLocalGroups}">
                            <form:option value="${group.cn}">${group.displayName}</form:option>
                        </c:forEach>
                    </form:select>
                    <p class="help-block"><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_INVITATION_LOCAL_GROUPS_HELP" /></p>
                </div>
            
                <!-- Message -->
                <div class="form-group">
                    <c:set var="placeholder"><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_INVITATION_MESSAGE_PLACEHOLDER" /></c:set>
                    <form:label path="message" cssClass="control-label"><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_INVITATION_MESSAGE" /></form:label>
                    <form:textarea path="message" rows="3" cssClass="form-control" placeholder="${placeholder}" />
                </div>
            </div>
            
            
            <!-- Buttons -->
            <spring:bind path="*">
                <div id="${namespace}-creation-buttons" class="collapse ${(status.error or creation.warning) ? 'in' : ''}">
                    <!-- Save -->
                    <button type="submit" class="btn btn-primary" data-ajax-shadowbox="#shadowbox-${namespace}">
                        <span><op:translate key="${creation.warning ? 'WORKSPACE_MEMBER_MANAGEMENT_INVITATIONS_CREATION_CONFIRM' : 'WORKSPACE_MEMBER_MANAGEMENT_INVITATIONS_CREATION_SAVE'}" /></span>
                    </button>
                    
                    <!-- Cancel -->
                    <button type="reset" class="btn btn-default" data-toggle="collapse" data-target="#${namespace}-creation-buttons">
                        <span><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_INVITATIONS_CREATION_CANCEL" /></span>
                    </button>
                </div>
            </spring:bind>
        </fieldset>
    </form:form>
</div>
