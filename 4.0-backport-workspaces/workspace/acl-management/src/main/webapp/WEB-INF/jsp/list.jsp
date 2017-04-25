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

<portlet:actionURL name="reset" var="resetUrl">
    <portlet:param name="sort" value="${sort}" />
    <portlet:param name="alt" value="${alt}" />
</portlet:actionURL>


<c:set var="namespace"><portlet:namespace /></c:set>


<!-- Inherited ACL information -->
<c:if test="${entries.inherited and not empty entries.entries}">
    <div class="alert alert-info" role="alert">
        <div class="media">
            <div class="media-left media-middle">
                <i class="glyphicons glyphicons-info-sign"></i>
            </div>
            
            <div class="media-body">
                <strong><op:translate key="INFORMATION" /></strong>
                <span>&ndash;</span>
                <span><op:translate key="MESSAGE_INFORMATION_INHERITED_ACL" /></span>
            </div>
        </div>
    </div>
</c:if>


<!-- Local ACL information -->
<c:if test="${not entries.inherited}">
    <div class="alert alert-info" role="alert">
        <div class="media">
            <div class="media-left media-middle">
                <i class="glyphicons glyphicons-info-sign"></i>
            </div>
            
            <div class="media-body">
                <strong><op:translate key="INFORMATION" /></strong>
                <span>&ndash;</span>
                <span><op:translate key="MESSAGE_INFORMATION_LOCAL_ACL" /></span>
                <br>
                <a href="${resetUrl}" class="alert-link"><op:translate key="ACL_RESET" /></a>
            </div>
        </div>
    </div>
</c:if>


<c:if test="${not empty entries.entries or not entries.inherited}">
    <div class="panel panel-default">
        <div class="panel-body">
            <form:form action="${updateUrl}" method="post" modelAttribute="entries" role="form">
                <fieldset>
                    <legend><op:translate key="LIST_ACL_ENTRIES_LEGEND" /></legend>
            
                    <!-- Table -->
                    <div class="table">
                        <!-- Header -->
                        <div class="table-row table-header">
                            <div class="row">
                                <!-- Entry -->
                                <div class="col-sm-8">
                                    <a href="${sortNameUrl}"><op:translate key="ACL_ENTRY" /></a>
                                    
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
                        <c:forEach var="entry" items="${entries.entries}" varStatus="status">
                            <c:if test="${entry.deleted}">
                                <c:set var="collapseStatus" value="in" />
                            </c:if>
                        
                            <div class="table-row">
                                <form:hidden path="entries[${status.index}].updated" />
                                <form:hidden path="entries[${status.index}].deleted" />
    
                                <fieldset>
                                    <div class="row">
                                        <!-- Entry -->
                                        <div class="col-xs-12 col-sm-8">
                                            <div class="person">
                                                <div class="person-avatar">
                                                    <c:choose>
                                                        <c:when test="${'GROUP' eq entry.type}"><i class="glyphicons glyphicons-group"></i></c:when>
                                                        <c:when test="${not empty entry.avatar}"><img src="${entry.avatar}" alt=""></c:when>
                                                        <c:otherwise><i class="glyphicons glyphicons-user"></i></c:otherwise>
                                                    </c:choose>
                                                </div>
                                                <div class="person-title">${entry.displayName}</div>
                                                <c:if test="${not empty entry.extra}">
                                                    <div class="person-extra">${entry.extra}</div>
                                                </c:if>
                                            </div>
                                        </div>
                                        
                                        <!-- Role -->
                                        <div class="col-xs-10 col-sm-3">
                                            <form:label path="entries[${status.index}].role" cssClass="sr-only"><op:translate key="ROLE"/></form:label>
                                            <form:select path="entries[${status.index}].role" cssClass="form-control">
                                                <c:forEach var="role" items="${roles}">
                                                    <form:option value="${role.id}">${role.displayName}</form:option>
                                                </c:forEach>
                                            </form:select>
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
                        
                        <!-- No results -->
                        <c:if test="${empty entries.entries}">
                            <div class="table-row">
                                <div class="row">
                                    <div class="col-xs-12 text-center"><op:translate key="NO_ACL_ENTRIES" /></div>
                                </div>
                            </div>
                        </c:if>
                    </div>
                    
                    <div id="${namespace}-buttons" class="form-group collapse ${collapseStatus}">
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
                </fieldset>
            </form:form>
        </div>
    </div>
</c:if>
