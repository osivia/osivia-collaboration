<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<portlet:renderURL var="sortNameUrl">
    <portlet:param name="tab" value="requests" />
    <portlet:param name="sort" value="name" />
    <portlet:param name="alt" value="${sort eq 'name' and not alt}"/>
</portlet:renderURL>
<portlet:renderURL var="sortDateUrl">
    <portlet:param name="tab" value="requests" />
    <portlet:param name="sort" value="date" />
    <portlet:param name="alt" value="${sort ne 'date' or not alt}"/>
</portlet:renderURL>
<portlet:renderURL var="sortRoleUrl">
    <portlet:param name="tab" value="requests" />
    <portlet:param name="sort" value="role" />
    <portlet:param name="alt" value="${sort ne 'role' or not alt}"/>
</portlet:renderURL>

<portlet:actionURL name="update" var="updateUrl">
    <portlet:param name="tab" value="requests" />
    <portlet:param name="sort" value="${sort}" />
    <portlet:param name="alt" value="${alt}" />
</portlet:actionURL>


<c:set var="namespace"><portlet:namespace /></c:set>


<form:form action="${updateUrl}" method="post" modelAttribute="invitationRequests" role="form">
    <div class="table table-hover">
        <!-- Header -->
        <div class="table-row table-header">
            <div class="row">
                <!-- Invitation request -->
                <div class="col-sm-6 col-md-4 col-lg-5">
                    <a href="${sortNameUrl}"><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_INVITATION_REQUEST"/></a>
                    
                    <c:if test="${sort eq 'name'}">
                        <small class="text-muted">
                            <c:choose>
                                <c:when test="${alt}"><i class="halflings halflings-sort-by-attributes-alt"></i></c:when>
                                <c:otherwise><i class="halflings halflings-sort-by-attributes"></i></c:otherwise>
                            </c:choose>
                        </small>
                    </c:if>
                </div>
                
                <!-- Column reset -->
                <div class="clearfix visible-xs-block"></div>
                
                <!-- Date -->
                <div class="col-xs-6 col-sm-3 col-md-2">
                    <a href="${sortDateUrl}"><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_INVITATION_REQUEST_DATE"/></a>
                    
                    <c:if test="${sort eq 'date'}">
                        <small class="text-muted">
                            <c:choose>
                                <c:when test="${alt}"><i class="halflings halflings-sort-by-attributes-alt"></i></c:when>
                                <c:otherwise><i class="halflings halflings-sort-by-attributes"></i></c:otherwise>
                            </c:choose>
                        </small>
                    </c:if>
                </div>
                
                <!-- Role -->
                <div class="col-xs-6 col-sm-3 col-md-2">
                    <a href="${sortRoleUrl}"><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_ROLE"/></a>
                    
                    <c:if test="${sort eq 'role'}">
                        <small class="text-muted">
                            <c:choose>
                                <c:when test="${alt}"><i class="halflings halflings-sort-by-attributes-alt"></i></c:when>
                                <c:otherwise><i class="halflings halflings-sort-by-attributes"></i></c:otherwise>
                            </c:choose>
                        </small>
                    </c:if>
                </div>
            </div>
        </div>
        
        <!-- Body -->
        <div class="table-body-wrapper">
            <c:forEach var="invitationRequest" items="${invitationRequests.requests}" varStatus="status">                  
                <div class="table-row">
                    <form:hidden path="requests[${status.index}].edited" />
                    <form:hidden path="requests[${status.index}].deleted" />
                    <form:hidden path="requests[${status.index}].accepted" />
                
                    <fieldset>
                        <div class="row">
                            <!-- Invitation request -->
                            <div class="col-sm-6 col-md-4 col-lg-5">
                                <c:set var="person" scope="request" value="${invitationRequest}" />
                                <jsp:include page="../commons/person.jsp" />
                            </div>
                            
                            <!-- Column reset -->
                            <div class="clearfix visible-xs-block"></div>
                            
                            <!-- Date -->
                            <div class="col-xs-6 col-sm-3 col-md-2">
                                <div class="form-control-static">
                                    <span><fmt:formatDate value="${invitationRequest.date}" type="date" dateStyle="medium" /></span>
                                </div>
                            </div>
                            
                            <!-- Role -->
                            <div class="col-xs-6 col-sm-3 col-md-2">
                                <form:label path="requests[${status.index}].role" cssClass="sr-only"><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_ROLE" /></form:label>
                                <form:select path="requests[${status.index}].role" cssClass="form-control">
                                    <c:forEach var="role" items="${options.roles}">
                                        <form:option value="${role}"><op:translate key="${role.key}" classLoader="${role.classLoader}" /></form:option>
                                    </c:forEach>
                                </form:select>
                            </div>
                            
                            <!-- Column reset -->
                            <div class="clearfix visible-xs-block visible-sm-block"></div>
                            
                            <!-- Action -->
                            <div class="col-sm-offset-7 col-sm-5 col-md-offset-0 col-md-4 col-lg-3">
                                <button type="button" class="btn btn-success accept">
                                    <i class="glyphicons glyphicons-ok"></i>
                                    <span><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_ACCEPT_INVITATION_REQUEST" /></span>
                                </button>
                                
                                <button type="button" class="btn btn-danger delete">
                                    <i class="glyphicons glyphicons-remove"></i>
                                    <span><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_DECLINE_INVITATION_REQUEST" /></span>
                                </button>
                                
                                <div class="accepted-message hidden">
                                    <div class="form-control-static text-success">
                                        <i class="glyphicons glyphicons-ok"></i>
                                        <span><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_ACCEPTED_INVITATION_REQUEST_MESSAGE" /></span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </fieldset>
                </div>
            </c:forEach>
        </div>
        
        <!-- No results -->
        <c:if test="${empty invitationRequests.requests}">
            <div class="table-row">
                <div class="text-center"><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_NO_INVITATION_REQUEST" /></div>
            </div>
        </c:if>
    </div>
    
    
    <div id="${namespace}-buttons" class="form-group collapse">
        <div class="alert alert-warning">
            <span><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_SAVE_INVITATION_REQUESTS_MESSAGE" /></span>
        </div>
    
        <div>
            <!-- Save -->
            <button type="submit" class="btn btn-primary">
                <i class="glyphicons glyphicons-floppy-disk"></i>
                <span><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_SAVE_INVITATION_REQUESTS" /></span>
            </button>
            
            <!-- Cancel -->
            <button type="reset" class="btn btn-default" data-toggle="collapse" data-target="#${namespace}-buttons">
                <span><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_CANCEL_INVITATION_REQUESTS" /></span>
            </button>
        </div>
    </div>
</form:form>
