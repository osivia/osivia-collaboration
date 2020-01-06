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

<portlet:renderURL var="synthesisUrl">
    <portlet:param name="synthesis" value="true" />
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


<!-- Modified ACL information -->
<c:if test="${entries.modified}">
    <div class="alert alert-info" role="alert">
        <div class="media">
            <div class="media-left media-middle">
                <i class="glyphicons glyphicons-info-sign"></i>
            </div>
            
            <div class="media-body">
                <strong><op:translate key="INFORMATION" /></strong>
                <span>&ndash;</span>
                <span><op:translate key="MESSAGE_INFORMATION_MODIFIED_ACL" /></span>
                <br>
                <a href="javascript:;" class="alert-link no-ajax-link" data-toggle="collapse" data-target="#${namespace}-confirm-reset"><op:translate key="ACL_RESET" /></a>
            </div>
        </div>
        
        <div id="${namespace}-confirm-reset" class="collapse">
            <div class="row">
                <div class="col-sm-offset-2 col-sm-8">
                    <p>&nbsp;</p>
                    <div class="panel panel-info">
                        <div class="panel-body">
                            <div class="text-center">
                                <p>
                                    <span><op:translate key="RESET_WARNING_MESSAGE" /></span>
                                </p>
                                
                                <div>
                                    <a href="${resetUrl}" class="btn btn-primary">
                                        <span><op:translate key="CONFIRM" /></span>
                                    </a>
                                    
                                    <button type="button" class="btn btn-secondary" data-toggle="collapse" data-target="#${namespace}-confirm-reset">
                                        <span><op:translate key="CANCEL" /></span>
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</c:if>


<div class="panel panel-default">
    <div class="panel-body">
        <form:form action="${updateUrl}" method="post" modelAttribute="entries" role="form">
            <fieldset>
                <legend><op:translate key="LIST_ACL_ENTRIES_LEGEND" /></legend>
        
                <!-- Public -->
                <div class="form-group">
                    <input type="submit" name="change-public" class="hidden">
                    
                    <div class="row">
                        <div class="col-xs-9">
                            <div class="checkbox">
                                <label>
                                    <form:checkbox path="publicEntry" />
                                    <span><op:translate key="PUBLIC_ACL_ENTRY" /></span>
                                </label>
                                <p class="help-block">
                                    <span><op:translate key="PUBLIC_ACL_ENTRY_HELP" /></span>
                                </p>
                            </div>
                        </div>
                        
                        <c:if test="${entries.publicEntryOriginal or (entries.publicInheritance and entries.inheritedOriginal)}">
                            <div class="col-xs-3">
                                <div class="form-control-static text-right">
                                    <span class="label label-success">
                                        <i class="glyphicons glyphicons-unlock"></i>
                                        <span class="hidden-xs">
                                            <c:choose>
                                                <c:when test="${entries.publicEntryOriginal}"><op:translate key="PUBLIC_LABEL" /></c:when>
                                                <c:otherwise><op:translate key="INHERITED_PUBLIC_LABEL" /></c:otherwise>
                                            </c:choose>
                                        </span>
                                    </span>
                                </div>
                            </div>
                        </c:if>
                    </div>
                </div>
        
                <!-- Inheritance -->
                <div class="form-group">
                    <input type="submit" name="change-inheritance" class="hidden">
                
                    <div class="radio">
                        <label>
                            <form:radiobutton path="inherited" value="true" />
                            <span><op:translate key="INHERITED_ACL_ENTRIES" /></span>
                        </label>
                        <p class="help-block">
                            <span><op:translate key="INHERITED_ACL_ENTRIES_HELP" /></span>
                        </p>
                    </div>
                
                    <div class="radio">
                        <label>
                            <form:radiobutton path="inherited" value="false" />
                            <span><op:translate key="LOCAL_ACL_ENTRIES" /></span>
                        </label>
                        <p class="help-block">
                            <span><op:translate key="LOCAL_ACL_ENTRIES_HELP" /></span>
                        </p>
                    </div>
                </div>
        
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
                        <div class="table-row">
                            <form:hidden path="entries[${status.index}].updated" />
                            <form:hidden path="entries[${status.index}].deleted" />

                            <fieldset ${entry.deleted ? 'disabled' : ''}>
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
                                        <c:choose>
                                            <c:when test="${entry.editable}">
                                                <form:label path="entries[${status.index}].role" cssClass="sr-only"><op:translate key="ROLE"/></form:label>
                                                <form:select path="entries[${status.index}].role" cssClass="form-control">
                                                    <c:forEach var="role" items="${roles}">
                                                        <form:option value="${role.id}">${role.displayName}</form:option>
                                                    </c:forEach>
                                                </form:select>
                                            </c:when>
                                            
                                            <c:otherwise>
                                                <p class="form-control-static">${entry.role.displayName}</p>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                    
                                    <!-- Deletion -->
                                    <c:if test="${entry.editable}">
                                        <div class="col-xs-2 col-sm-1">
                                            <button type="button" class="btn btn-secondary delete">
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
                    <c:if test="${empty entries.entries}">
                        <div class="table-row">
                            <div class="row">
                                <div class="col-xs-12 text-center"><op:translate key="NO_ACL_ENTRIES" /></div>
                            </div>
                        </div>
                    </c:if>
                </div>
                
                <div id="${namespace}-buttons" class="form-group collapse ${entries.displayControls ? 'in' : ''}">
                    <!-- Save -->
                    <button type="submit" name="save" class="btn btn-primary">
                        <i class="glyphicons glyphicons-floppy-disk"></i>
                        <span><op:translate key="SAVE" /></span>
                    </button>
                    
                    <!-- Cancel -->
                    <button type="submit" name="cancel" class="btn btn-secondary" data-toggle="collapse" data-target="#${namespace}-buttons">
                        <span><op:translate key="CANCEL" /></span>
                    </button>
                </div>
            </fieldset>
        </form:form>
        
        
        <!-- Synthesis -->
        <div>
            <a href="${synthesisUrl}" class="btn btn-link">
                <span><op:translate key="VIEW_SYNTHESIS" /></span>
            </a>
        </div>
    </div>
</div>
