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


<div class="well">
    <form:form action="${createUrl}" method="post" modelAttribute="creation" role="form">
        <fieldset>
            <legend>
                <span><op:translate key="CREATE_INVITATIONS_LEGEND" /></span>
            </legend>
            
            <c:if test="${creation.warning}">
                <div class="alert alert-warning">
                    <span><op:translate key="INVITATIONS_CREATE_PERSON_WARNING" /></span>
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
                    <c:set var="placeholder"><op:translate key="CREATE_INVITATIONS_ADD_PERSONS_PLACEHOLDER" /></c:set>
                    <c:set var="inputTooShort"><op:translate key="SELECT2_INPUT_TOO_SHORT" args="3" /></c:set>
                    <c:set var="noResults"><op:translate key="SELECT2_NO_RESULTS" /></c:set>
                    <c:set var="searching"><op:translate key="SELECT2_SEARCHING" /></c:set>
                    <spring:bind path="pendingInvitations">
                        <div class="form-group ${status.error ? 'has-error' : (creation.warning ? 'has-warning' : '')}">
                            <form:label path="pendingInvitations" cssClass="control-label"><op:translate key="CREATE_INVITATIONS_ADD_PERSONS_LABEL" /></form:label>
                            <form:select path="pendingInvitations" cssClass="form-control select2" data-placeholder="${placeholder}" data-url="${searchUrl}" data-input-too-short="${inputTooShort}" data-no-results="${noResults}" data-searching="${searching}">
                                <c:forEach var="invitation" items="${creation.pendingInvitations}">
                                    <form:option value="${invitation.id}" data-avatar="${invitation.avatar}">${invitation.displayName}</form:option>
                                </c:forEach>
                            </form:select>
                            <form:errors path="pendingInvitations" cssClass="help-block" />
                        </div>
                    </spring:bind>
                </div>
                
                <div class="col-sm-4 col-lg-3">
                    <!-- Role -->
                    <div class="form-group">
                        <form:label path="role" cssClass="control-label"><op:translate key="ROLE" /></form:label>
                        <form:select path="role" cssClass="form-control">
                            <c:forEach var="role" items="${options.roles}">
                                <form:option value="${role}"><op:translate key="${role.key}" classLoader="${role.classLoader}"/></form:option>
                            </c:forEach>
                        </form:select>
                    </div>
                </div>
            </div>
            
            
            <!-- Buttons -->
            <spring:bind path="*">
                <div id="${namespace}-creation-buttons" class="collapse ${(status.error or creation.warning) ? 'in' : ''}">
                    <!-- Save -->
                    <button type="submit" class="btn btn-primary">
                        <span><op:translate key="${creation.warning ? 'CONFIRM' : 'INVITE'}" /></span>
                    </button>
                    
                    <!-- Cancel -->
                    <button type="reset" class="btn btn-default" data-toggle="collapse" data-target="#${namespace}-creation-buttons">
                        <span><op:translate key="CANCEL" /></span>
                    </button>
                </div>
            </spring:bind>
        </fieldset>
    </form:form>
</div>
