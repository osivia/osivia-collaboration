<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:renderURL var="sortNameUrl">
    <portlet:param name="tab" value="invitations" />
    <portlet:param name="sort" value="name" />
    <portlet:param name="alt" value="${sort eq 'name' and not alt}"/>
</portlet:renderURL>
<portlet:renderURL var="sortDateUrl">
    <portlet:param name="tab" value="invitations" />
    <portlet:param name="sort" value="date" />
    <portlet:param name="alt" value="${sort ne 'date' or not alt}"/>
</portlet:renderURL>
<portlet:renderURL var="sortStateUrl">
    <portlet:param name="tab" value="invitations" />
    <portlet:param name="sort" value="state" />
    <portlet:param name="alt" value="${sort eq 'state' and not alt}"/>
</portlet:renderURL>
<portlet:renderURL var="sortRoleUrl">
    <portlet:param name="tab" value="invitations" />
    <portlet:param name="sort" value="role" />
    <portlet:param name="alt" value="${sort ne 'role' or not alt}"/>
</portlet:renderURL>

<portlet:actionURL name="update" var="updateUrl">
    <portlet:param name="tab" value="invitations" />
    <portlet:param name="sort" value="${sort}" />
    <portlet:param name="alt" value="${alt}" />
</portlet:actionURL>


<c:set var="namespace"><portlet:namespace /></c:set>


<form:form action="${updateUrl}" method="post" modelAttribute="invitations" role="form">
    <div class="table table-hover">
        <!-- Header -->
        <div class="table-row table-header">
            <div class="row">
                <!-- Invitation -->
                <div class="col-xs-7 col-sm-3 col-md-4">
                    <a href="${sortNameUrl}"><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_INVITATION"/></a>
                    
                    <c:if test="${sort eq 'name'}">
                        <small class="text-muted">
                            <c:choose>
                                <c:when test="${alt}"><i class="halflings halflings-sort-by-attributes-alt"></i></c:when>
                                <c:otherwise><i class="halflings halflings-sort-by-attributes"></i></c:otherwise>
                            </c:choose>
                        </small>
                    </c:if>
                </div>
                
                <!-- Date -->
                <div class="col-xs-5 col-sm-3 col-md-2">
                    <a href="${sortDateUrl}"><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_INVITATION_DATE"/></a>
                    
                    <c:if test="${sort eq 'date'}">
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
                
                <!-- State -->
                <div class="col-xs-5 col-sm-2">
                    <a href="${sortStateUrl}"><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_INVITATION_STATE"/></a>
                    
                    <c:if test="${sort eq 'state'}">
                        <small class="text-muted">
                            <c:choose>
                                <c:when test="${alt}"><i class="halflings halflings-sort-by-attributes-alt"></i></c:when>
                                <c:otherwise><i class="halflings halflings-sort-by-attributes"></i></c:otherwise>
                            </c:choose>
                        </small>
                    </c:if>
                </div>

                <!-- Role -->
                <div class="col-xs-7 col-sm-2">
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
        <div class="portlet-filler">
            <c:forEach var="invitation" items="${invitations.invitations}" varStatus="status">
                <div class="table-row ${invitation.state.editable ? '' : 'muted'}">
                    <form:hidden path="invitations[${status.index}].edited" />
                    <form:hidden path="invitations[${status.index}].deleted" />
                
                    <fieldset>
                        <div class="row">
                            <!-- Invitation -->
                            <div class="col-xs-7 col-sm-3 col-md-4">
                                <c:set var="person" scope="request" value="${invitation}" />
                                <jsp:include page="../commons/person.jsp" />
                            </div>
                            
                            <!-- Invitation date -->
                            <div class="col-xs-5 col-sm-3 col-md-2">
                                <div class="form-control-static">
                                    <span><fmt:formatDate value="${invitation.date}" type="date" dateStyle="medium" /></span>
                                </div>
                            </div>
                            
                            <!-- Column reset -->
                            <div class="clearfix visible-xs-block"></div>
                            
                            <!-- State -->
                            <div class="col-xs-5 col-sm-2">
                                <div class="form-control-static">
                                    <span class="${invitation.state.htmlClasses}">
                                        <i class="${invitation.state.icon}"></i>
                                        <span><op:translate key="${invitation.state.key}" /></span>
                                    </span>
                                </div>
                            </div>
                            
                            <!-- Role -->
                            <div class="col-xs-7 col-sm-2">
                                <c:choose>
                                    <c:when test="${invitation.state.editable}">
                                        <form:label path="invitations[${status.index}].role" cssClass="sr-only"><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_ROLE" /></form:label>
                                        <form:select path="invitations[${status.index}].role" cssClass="form-control">
                                            <c:forEach var="role" items="${options.roles}">
                                                <form:option value="${role}"><op:translate key="${role.key}" classLoader="${role.classLoader}" /></form:option>
                                            </c:forEach>
                                        </form:select>
                                    </c:when>
                                    
                                    <c:otherwise>
                                        <div class="form-control-static">
                                            <span><op:translate key="${invitation.role.key}" classLoader="${invitation.role.classLoader}" /></span>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            
                            <!-- Deletion -->
                            <c:if test="${invitation.state.editable}">
                                <!-- Column reset -->
                                <div class="clearfix visible-xs-block"></div>
                            
                                <div class="col-xs-12 col-sm-2">
                                    <button type="button" class="btn btn-default delete">
                                        <span><op:translate key="CANCEL" /></span>
                                    </button>
                                </div>
                            </c:if>
                        </div>
                    </fieldset>
                </div>
            </c:forEach>
            
            <!-- No results -->
            <c:if test="${empty invitations.invitations}">
                <div class="table-row">
                    <div class="text-center text-muted"><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_NO_INVITATION" /></div>
                </div>
            </c:if>
        </div>
    </div>
    
    
    <div class="portlet-toolbar">
        <div id="${namespace}-invitations-buttons" class="panel panel-default collapse">
            <div class="panel-body">
                <div class="alert alert-warning">
                    <span><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_SAVE_MEMBERS_MESSAGE" /></span>
                </div>
            
                <div>
                    <!-- Save -->
                    <button type="submit" class="btn btn-primary">
            			<i class="glyphicons glyphicons-floppy-disk"></i>
                        <span><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_SAVE_INVITATIONS" /></span>
                    </button>
                    
                    <!-- Cancel -->
                    <button type="reset" class="btn btn-default" data-toggle="collapse" data-target="#${namespace}-invitations-buttons">
                        <span><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_CANCEL_INVITATIONS" /></span>
                    </button>
                </div>
            </div>
        </div>
    </div>
</form:form>
