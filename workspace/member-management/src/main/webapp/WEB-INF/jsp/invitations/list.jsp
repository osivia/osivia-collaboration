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
    <div>
        <div class="table table-hover">
            <!-- Header -->
            <div class="table-row table-header">
                <div class="row">
                    <!-- Invitation -->
                    <div class="col-xs-7 col-sm-4 col-md-5 col-lg-6">
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
                    
                    <!-- Dates -->
                    <div class="col-xs-5 col-sm-3 col-lg-2">
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
                    <div class="col-sm-5 col-md-4">
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
                </div>
            </div>
            
            <!-- Body -->
            <div class="portlet-filler">
                <c:forEach var="invitation" items="${invitations.invitations}" varStatus="status">
                    <div class="table-row">
                        <div class="row">
                            <!-- Invitation -->
                            <div class="col-xs-7 col-sm-4 col-md-5 col-lg-6">
                                <c:set var="person" scope="request" value="${invitation}" />
                                <jsp:include page="../commons/person.jsp" />
                            </div>
                            
                            <!-- Dates -->
                            <div class="col-xs-5 col-sm-3 col-lg-2">
                                <div class="${empty invitation.resendingDate ? 'form-control-static' : ''}">
                                    <span><fmt:formatDate value="${invitation.date}" type="date" dateStyle="medium" /></span>
                                    <c:if test="${not empty invitation.resendingDate}">
                                        <br>
                                        <small>
                                            <span><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_INVITATION_RESENDING_DATE" /></span>
                                            <span><fmt:formatDate value="${invitation.resendingDate}" type="date" dateStyle="medium" /></span>
                                        </small>
                                    </c:if>
                                </div>
                            </div>
                            
                            <!-- Column reset -->
                            <div class="clearfix visible-xs-block"></div>
                            
                            <div class="col-sm-5 col-md-4">
                                <div class="media-body">
                                    <!-- State -->
                                    <div class="form-control-static">
                                        <span class="${invitation.state.htmlClasses}">
                                            <i class="${invitation.state.icon}"></i>
                                            <span><op:translate key="${invitation.state.key}" /></span>
                                        </span>
                                    </div>
                                </div>
                                
                                <c:if test="${invitation.state.editable}">
                                    <portlet:renderURL var="url">
                                        <portlet:param name="view" value="invitation-edition"/>
                                        <portlet:param name="invitationPath" value="${invitation.document.path}"/>
                                    </portlet:renderURL>
                                
                                    <div class="media-right media-middle">
                                        <a href="${url}" class="btn btn-link btn-sm">
                                            <span><op:translate key="EDIT" /></span>
                                        </a>
                                    </div>
                                </c:if>
                            </div>
                        </div>
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
    </div>
</form:form>
