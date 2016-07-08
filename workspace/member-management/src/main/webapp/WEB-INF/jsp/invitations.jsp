<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:renderURL var="sortNameUrl">
    <portlet:param name="tab" value="invitations" />
    <portlet:param name="sort" value="name" />
    <portlet:param name="alt" value="${sort eq 'name' and not alt}"/>
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
                <div class="col-xs-8 col-sm-6 col-lg-7">
                    <a href="${sortNameUrl}"><op:translate key="INVITATION"/></a>
                    
                    <c:if test="${sort eq 'name'}">
                        <small class="text-muted">
                            <c:choose>
                                <c:when test="${alt}"><i class="halflings halflings-sort-by-attributes-alt"></i></c:when>
                                <c:otherwise><i class="halflings halflings-sort-by-attributes"></i></c:otherwise>
                            </c:choose>
                        </small>
                    </c:if>
                </div>
                
                <!-- Invitation state -->
                <div class="col-xs-4 col-sm-2 col-lg-1">
                    <a href="${sortStateUrl}"><op:translate key="INVITATION_STATE"/></a>
                    
                    <c:if test="${sort eq 'state'}">
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
                
                <!-- Role -->
                <div class="col-xs-10 col-sm-3">
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
        <c:forEach var="invitation" items="${invitations.invitations}" varStatus="status">
            <div class="table-row">
                <form:hidden path="invitations[${status.index}].deleted" />
            
                <fieldset>
                    <div class="row">
                        <!-- Invitation -->
                        <div class="col-xs-8 col-sm-6 col-lg-7">
                            <div class="person">
                                <div class="person-avatar">
                                    <img src="${invitation.avatar}" alt="">
                                </div>
                                <div class="person-title">
                                    <span>${invitation.displayName}</span>
                                </div>
                                <c:if test="${not empty invitation.mail}">
                                    <div class="person-extra">${invitation.mail}</div>
                                </c:if>
                            </div>
                        </div>
                        
                        <!-- Invitation state -->
                        <div class="col-xs-4 col-sm-2 col-lg-1">
                            <div class="form-control-static">
                                <c:if test="${not empty invitation.state}">
                                    <span class="${invitation.state.htmlClasses}"><op:translate key="${invitation.state.key}" /></span>
                                </c:if>
                            </div>
                        </div>
                        
                        <!-- Column reset -->
                        <div class="clearfix visible-xs-block"></div>
                        
                        <!-- Role -->
                        <div class="col-xs-10 col-sm-3">
                            <form:label path="invitations[${status.index}].role" cssClass="sr-only"><op:translate key="ROLE" /></form:label>
                            <form:select path="invitations[${status.index}].role" cssClass="form-control">
                                <c:forEach var="role" items="${options.roles}">
                                    <form:option value="${role}"><op:translate key="${role.key}" classLoader="${role.classLoader}" /></form:option>
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
        <c:if test="${empty invitations.invitations}">
            <div class="table-row">
                <div class="text-center"><op:translate key="NO_INVITATION" /></div>
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
