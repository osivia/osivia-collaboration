<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:renderURL var="sortNameUrl">
    <portlet:param name="tab" value="invitations" />
    <portlet:param name="sort" value="${sort}" />
    <portlet:param name="alt" value="${alt}" />
    <portlet:param name="sort2" value="name" />
    <portlet:param name="alt2" value="${sort2 eq 'name' and not alt2}"/>
</portlet:renderURL>
<portlet:renderURL var="sortDateUrl">
    <portlet:param name="tab" value="invitations" />
    <portlet:param name="sort" value="${sort}" />
    <portlet:param name="alt" value="${alt}" />
    <portlet:param name="sort2" value="date" />
    <portlet:param name="alt2" value="${sort2 ne 'date' or not alt2}"/>
</portlet:renderURL>
<portlet:renderURL var="sortStateUrl">
    <portlet:param name="tab" value="invitations" />
    <portlet:param name="sort" value="${sort}" />
    <portlet:param name="alt" value="${alt}" />
    <portlet:param name="sort2" value="state" />
    <portlet:param name="alt2" value="${sort2 eq 'state' and not alt2}"/>
</portlet:renderURL>
<portlet:renderURL var="sortRoleUrl">
    <portlet:param name="tab" value="invitations" />
    <portlet:param name="sort" value="${sort}" />
    <portlet:param name="alt" value="${alt}" />
    <portlet:param name="sort2" value="role" />
    <portlet:param name="alt2" value="${sort2 ne 'role' or not alt2}"/>
</portlet:renderURL>

<portlet:actionURL name="updateHistory" var="updateUrl">
    <portlet:param name="tab" value="invitations" />
    <portlet:param name="sort" value="${sort}" />
    <portlet:param name="alt" value="${alt}" />
    <portlet:param name="sort2" value="${sort2}" />
    <portlet:param name="alt2" value="${alt2}" />
</portlet:actionURL>


<c:set var="namespace"><portlet:namespace /></c:set>

<c:if test="${not empty invitations.history}">
    <div class="panel panel-default">
        <div class="panel-body">
            <form:form action="${updateUrl}" method="post" modelAttribute="invitations" role="form">
                <fieldset>
                    <legend>
                        <i class="glyphicons glyphicons-history"></i>
                        <span><op:translate key="INVITATIONS_HISTORY" /></span>
                    </legend>
                </fieldset>
            
                <div class="table table-hover">
                    <!-- Header -->
                    <div class="table-row table-header">
                        <div class="row">
                            <!-- Invitation -->
                            <div class="col-xs-8 col-sm-4 col-lg-5">
                                <a href="${sortNameUrl}"><op:translate key="INVITATION"/></a>
                                
                                <c:if test="${sort2 eq 'name'}">
                                    <small class="text-muted">
                                        <c:choose>
                                            <c:when test="${alt2}"><i class="halflings halflings-sort-by-attributes-alt"></i></c:when>
                                            <c:otherwise><i class="halflings halflings-sort-by-attributes"></i></c:otherwise>
                                        </c:choose>
                                    </small>
                                </c:if>
                            </div>
                            
                            <!-- Date -->
                            <div class="col-xs-4 col-sm-2">
                                <a href="${sortDateUrl}"><op:translate key="INVITATION_DATE"/></a>
                                
                                <c:if test="${sort2 eq 'date'}">
                                    <small class="text-muted">
                                        <c:choose>
                                            <c:when test="${alt2}"><i class="halflings halflings-sort-by-attributes-alt"></i></c:when>
                                            <c:otherwise><i class="halflings halflings-sort-by-attributes"></i></c:otherwise>
                                        </c:choose>
                                    </small>
                                </c:if>
                            </div>
                            
                            <!-- Column reset -->
                            <div class="clearfix visible-xs-block"></div>
                            
                            <!-- State -->
                            <div class="col-xs-10 col-sm-2">
                                <a href="${sortStateUrl}"><op:translate key="INVITATION_STATE"/></a>
                                
                                <c:if test="${sort2 eq 'state'}">
                                    <small class="text-muted">
                                        <c:choose>
                                            <c:when test="${alt2}"><i class="halflings halflings-sort-by-attributes-alt"></i></c:when>
                                            <c:otherwise><i class="halflings halflings-sort-by-attributes"></i></c:otherwise>
                                        </c:choose>
                                    </small>
                                </c:if>
                            </div>
                            
                            <!-- Role -->
                            <div class="hidden-xs col-sm-3 col-lg-2">
                                <a href="${sortRoleUrl}"><op:translate key="ROLE"/></a>
                                
                                <c:if test="${sort2 eq 'role'}">
                                    <small class="text-muted">
                                        <c:choose>
                                            <c:when test="${alt2}"><i class="halflings halflings-sort-by-attributes-alt"></i></c:when>
                                            <c:otherwise><i class="halflings halflings-sort-by-attributes"></i></c:otherwise>
                                        </c:choose>
                                    </small>
                                </c:if>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Body -->
                    <c:forEach var="invitation" items="${invitations.history}" varStatus="status">
                        <div class="table-row">
                            <form:hidden path="history[${status.index}].deleted" />
                        
                            <fieldset>
                                <div class="row">
                                    <!-- Invitation -->
                                    <div class="col-xs-8 col-sm-4 col-lg-5">
			                        	<c:set var="member" value="${invitation}" scope="request" />
			    						<jsp:include page="incl/display-member.jsp" />
                                    </div>
                                    
                                    <!-- Date -->
                                    <div class="col-xs-4 col-sm-2">
                                        <div class="form-control-static">
                                            <span class="text-muted"><fmt:formatDate value="${invitation.date}" type="date" dateStyle="medium" /></span>
                                        </div>
                                    </div>
                                    
                                    <!-- Column reset -->
                                    <div class="clearfix visible-xs-block"></div>
                                    
                                    <!-- State -->
                                    <div class="col-xs-10 col-sm-2">
                                        <div class="form-control-static">
                                            <span class="${invitation.state.htmlClasses}"><op:translate key="${invitation.state.key}" /></span>
                                        </div>
                                    </div>
                                    
                                    <!-- Role -->
                                    <div class="hidden-xs col-sm-3 col-lg-2">
                                        <div class="form-control-static">
                                            <span><op:translate key="${invitation.role.key}" classLoader="${invitation.role.classLoader}" /></span>
                                        </div>
                                    </div>
                                    
                                    <!-- Deletion -->
                                    <div class="col-xs-2 col-sm-1">
                                        <button type="button" class="btn btn-default delete">
                                            <i class="glyphicons glyphicons-remove"></i>
                                            <span class="sr-only"><op:translate key="DELETE" /></span>
                                        </button>
                                    </div>
                                </div>
                            </fieldset>
                        </div>
                    </c:forEach>
                </div>
                
                
                <div id="${namespace}-history-buttons" class="form-group collapse">
                    <!-- Save -->
                    <button type="submit" class="btn btn-primary">
                        <i class="glyphicons glyphicons-floppy-disk"></i>
                        <span><op:translate key="SAVE" /></span>
                    </button>
                    
                    <!-- Cancel -->
                    <button type="reset" class="btn btn-default" data-toggle="collapse" data-target="#${namespace}-history-buttons">
                        <span><op:translate key="CANCEL" /></span>
                    </button>
                </div>
            </form:form>
        </div>
    </div>
</c:if>
