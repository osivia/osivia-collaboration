<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<portlet:renderURL var="sortNameUrl">
    <portlet:param name="sort" value="name" />
    <portlet:param name="alt" value="${sort eq 'name' and not alt}"/>
</portlet:renderURL>
<portlet:renderURL var="sortDateUrl">
    <portlet:param name="sort" value="date" />
    <portlet:param name="alt" value="${sort ne 'date' or not alt}"/>
</portlet:renderURL>
<portlet:renderURL var="sortRoleUrl">
    <portlet:param name="sort" value="role" />
    <portlet:param name="alt" value="${sort ne 'role' or not alt}"/>
</portlet:renderURL>

<portlet:actionURL name="update" var="updateUrl">
    <portlet:param name="sort" value="${sort}" />
    <portlet:param name="alt" value="${alt}" />
</portlet:actionURL>


<c:set var="namespace"><portlet:namespace /></c:set>


<form:form action="${updateUrl}" method="post" modelAttribute="members" role="form">
    <div class="table table-hover">
        <!-- Header -->
        <div class="table-row table-header">
            <div class="row">
                <!-- Member -->
                <div class="col-xs-12 col-sm-4 col-md-5 col-lg-6">
                    <a href="${sortNameUrl}"><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_MEMBER"/></a>
                    
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
                
                <!-- Acknowledgment date -->
                <div class="col-xs-6 col-sm-3 col-md-2">
                    <a href="${sortDateUrl}"><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_MEMBER_ACKNOWLEDGMENT_DATE" /></a>
                    
                    <c:if test="${sort eq 'date'}">
                        <small class="text-muted">
                            <c:choose>
                                <c:when test="${alt}"><i class="halflings halflings-sort-by-attributes-alt"></i></c:when>
                                <c:otherwise><i class="halflings halflings-sort-by-attributes"></i></c:otherwise>
                            </c:choose>
                        </small>
                    </c:if>
                    
                    <!-- Help -->
                    <c:set var="help"><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_MEMBER_ACKNOWLEDGMENT_DATE_HELP" /></c:set>
                    <button type="button" title="${help}" data-toggle="tooltip" data-placement="top" class="btn btn-link btn-xs">
                        <i class="glyphicons glyphicons-question-sign"></i>
                        <span class="sr-only"><op:translate key="CONTEXTUAL_HELP" /></span>
                    </button>
                </div>
                
                <!-- Role -->
                <div class="col-xs-6 col-sm-3 col-lg-2">
                    <a href="${sortRoleUrl}"><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_ROLE" /></a>
                    
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
            <c:forEach var="member" items="${members.members}" varStatus="status">                  
                <div class="table-row">
                    <form:hidden path="members[${status.index}].edited" />
                    <form:hidden path="members[${status.index}].deleted" />
                
                    <fieldset>
                        <div class="row">
                            <!-- Member -->
                            <div class="col-xs-12 col-sm-4 col-md-5 col-lg-6">
                                <c:set var="person" scope="request" value="${member}" />
                                <jsp:include page="../commons/person.jsp" />
                            </div>
                            
                            <!-- Column reset -->
                            <div class="clearfix visible-xs-block"></div>
                            
                            <!-- Acknowledgment date -->
                            <div class="col-xs-6 col-sm-3 col-md-2">
                                <div class="form-control-static">
                                    <span><fmt:formatDate value="${member.date}" type="date" dateStyle="medium" /></span>
                                </div>
                            </div>
                            
                            <!-- Role -->
                            <div class="col-xs-6 col-sm-3 col-lg-2">
                                <c:choose>
                                    <c:when test="${member.editable}">
                                        <form:label path="members[${status.index}].role" cssClass="sr-only"><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_ROLE" /></form:label>
                                        <form:select path="members[${status.index}].role" cssClass="form-control">
                                            <c:forEach var="role" items="${options.roles}">
                                                <form:option value="${role}"><op:translate key="${role.key}" classLoader="${role.classLoader}" /></form:option>
                                            </c:forEach>
                                        </form:select>
                                    </c:when>
                                    
                                    <c:otherwise>
                                        <div class="form-control-static">
                                            <span><op:translate key="${member.role.key}" classLoader="${member.role.classLoader}" /></span>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            
                            <c:if test="${member.editable}">
                                <!-- Column reset -->
                                <div class="clearfix visible-xs-block"></div>
                            
                                <!-- Deletion -->
                                <div class="col-xs-12 col-sm-2">
                                    <button type="button" class="btn btn-default delete">
                                        <span><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_REMOVE_MEMBER" /></span>
                                    </button>
                                </div>
                            </c:if>
                        </div>
                    </fieldset>
                </div>
            </c:forEach>
        </div>
        
        <!-- No results -->
        <c:if test="${empty members.members}">
            <div class="table-row">
                <div class="text-center"><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_NO_MEMBER" /></div>
            </div>
        </c:if>
    </div>
    
    
    <div id="${namespace}-buttons" class="form-group collapse">
        <div class="alert alert-warning">
            <span><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_SAVE_MEMBERS_MESSAGE" /></span>
        </div>
    
        <div>
            <!-- Save -->
            <button type="submit" class="btn btn-primary">
                <i class="glyphicons glyphicons-floppy-disk"></i>
                <span><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_SAVE_MEMBERS" /></span>
            </button>
            
            <!-- Cancel -->
            <button type="reset" class="btn btn-default" data-toggle="collapse" data-target="#${namespace}-buttons">
                <span><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_CANCEL_MEMBERS" /></span>
            </button>
        </div>
    </div>
</form:form>