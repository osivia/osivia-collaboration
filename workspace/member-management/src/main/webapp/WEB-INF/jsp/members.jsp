<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:renderURL var="sortNameUrl">
    <portlet:param name="sort" value="name" />
    <portlet:param name="alt" value="${sort eq 'name' and not alt}"/>
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
                <div class="col-sm-8">
                    <a href="${sortNameUrl}"><op:translate key="MEMBER"/></a>
                    
                    <c:if test="${sort eq 'name'}">
                        <small class="text-muted">
                            <c:choose>
                                <c:when test="${alt}"><i class="halflings halflings-sort-by-attributes-alt"></i></c:when>
                                <c:otherwise><i class="halflings halflings-sort-by-attributes"></i></c:otherwise>
                            </c:choose>
                        </small>
                    </c:if>
                </div>
                
                <!-- Role -->
                <div class="col-sm-3">
                    <a href="${sortRoleUrl}"><op:translate key="ROLE"/></a>
                    
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
        <c:forEach var="member" items="${members.members}" varStatus="status">                  
            <div class="table-row">
                <form:hidden path="members[${status.index}].deleted" />
            
                <fieldset>
                    <div class="row">
                        <!-- Member -->
                        <div class="col-xs-12 col-sm-8">
                            <div class="person">
                                <div class="person-avatar">
                                    <img src="${member.avatar}" alt="">
                                </div>
                                <div class="person-title">${member.displayName}</div>
                                <c:if test="${not empty member.mail}">
                                    <div class="person-extra">${member.mail}</div>
                                </c:if>
                            </div>
                        </div>
                        
                        <!-- Role -->
                        <div class="col-xs-10 col-sm-3">
                            <c:choose>
                                <c:when test="${member.editable}">
                                    <form:label path="members[${status.index}].role" cssClass="sr-only"><op:translate key="ROLE" /></form:label>
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
                            <!-- Deletion -->
                            <div class="col-xs-2 col-sm-1">
                                <button type="button" class="btn btn-default delete">
                                    <i class="glyphicons glyphicons-remove"></i>
                                    <span class="sr-only"><op:translate key="DELETE" /></span>
                                </button>
                            </div>
                        </c:if>
                    </div>
                </fieldset>
            </div>
        </c:forEach>
        
        <!-- No results -->
        <c:if test="${empty members.members}">
            <div class="table-row">
                <div class="text-center"><op:translate key="NO_MEMBER" /></div>
            </div>
        </c:if>
    </div>
    
    
    <div id="${namespace}-buttons" class="form-group collapse">
        <!-- Save -->
        <button type="submit" class="btn btn-primary">
            <i class="glyphicons glyphicons-floppy-disk"></i>
            <span><op:translate key="SAVE" /></span>
        </button>
        
        <!-- Cancel -->
        <button type="reset" class="btn btn-default" data-toggle="collapse" data-target="#${namespace}-buttons">
            <span><op:translate key="CANCEL" /></span>
        </button>
    </div>
</form:form>
