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
                <div class="col-xs-7 col-sm-4 col-lg-5">
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
                
                <!-- Date -->
                <div class="col-xs-5 col-sm-2">
                    <a href="${sortDateUrl}"><op:translate key="INVITATION_DATE"/></a>
                    
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

                <!-- Role -->
                <div class="col-xs-7 col-sm-2">
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
        <div class="table-body-wrapper">
            <c:forEach var="invitation" items="${invitations.invitations}" varStatus="status">
                <div class="table-row ${invitation.state.editable ? '' : 'muted'}">
                    <form:hidden path="invitations[${status.index}].deleted" />
                
                    <fieldset>
                        <div class="row">
                            <!-- Invitation -->
                            <div class="col-xs-7 col-sm-4 col-lg-5">
                                <div class="person">
                                    <div class="person-avatar">
                                        <c:choose>
                                            <c:when test="${not empty invitation.avatar}">
                                                <img src="${invitation.avatar}" alt="">
                                            </c:when>
                                            
                                            <c:when test="${invitation.unknownUser}">
                                                <i class="glyphicons glyphicons-user-add"></i>
                                            </c:when>
                                        </c:choose>
                                    </div>
                                    
                                    <div class="person-title">
                                        <span>${invitation.displayName}</span>
                                    </div>
                                    
                                    <c:choose>
                                        <c:when test="${not empty invitation.mail}">
                                            <div class="person-extra">${invitation.mail}</div>
                                        </c:when>
                                        
                                        <c:when test="${invitation.unknownUser}">
                                            <div class="person-extra"><op:translate key="INVITATIONS_PERSON_BEING_CREATED" /></div>
                                        </c:when>
                                    </c:choose>
                                </div>
                            </div>
                            
                            <!-- Invitation date -->
                            <div class="col-xs-5 col-sm-2">
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
                                        <form:label path="invitations[${status.index}].role" cssClass="sr-only"><op:translate key="ROLE" /></form:label>
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
                            
                                <div class="col-xs-12 col-sm-2 col-lg-1">
                                    <button type="button" class="btn btn-default delete">
                                        <span><op:translate key="CANCEL" /></span>
                                    </button>
                                </div>
                            </c:if>
                        </div>
                    </fieldset>
                </div>
            </c:forEach>
        </div>
        
        <!-- No results -->
        <c:if test="${empty invitations.invitations}">
            <div class="table-row">
                <div class="text-center"><op:translate key="NO_INVITATION" /></div>
            </div>
        </c:if>
    </div>
    
    
    <div id="${namespace}-invitations-buttons" class="form-group collapse">
        <!-- Save -->
        <button type="submit" class="btn btn-primary">
			<i class="glyphicons glyphicons-floppy-disk"></i>
            <span><op:translate key="SAVE" /></span>
        </button>
        
        <!-- Cancel -->
        <button type="reset" class="btn btn-default" data-toggle="collapse" data-target="#${namespace}-invitations-buttons">
            <span><op:translate key="CANCEL" /></span>
        </button>
    </div>
</form:form>
