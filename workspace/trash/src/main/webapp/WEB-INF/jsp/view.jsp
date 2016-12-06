<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<portlet:defineObjects />


<portlet:renderURL var="sortTitleUrl">
    <portlet:param name="sort" value="title" />
    <portlet:param name="alt" value="${sort eq 'title' and not alt}"/>
</portlet:renderURL>
<portlet:renderURL var="sortDateUrl">
    <portlet:param name="sort" value="date" />
    <portlet:param name="alt" value="${sort ne 'date' or not alt}"/>
</portlet:renderURL>
<portlet:renderURL var="sortLocationUrl">
    <portlet:param name="sort" value="location" />
    <portlet:param name="alt" value="${sort eq 'location' and not alt}"/>
</portlet:renderURL>

<portlet:actionURL name="restore" var="restoreUrl" />
<portlet:actionURL name="empty" var="emptyUrl" />
<portlet:actionURL name="update" var="updateUrl">
    <portlet:param name="sort" value="${sort}" />
    <portlet:param name="alt" value="${alt}" />
</portlet:actionURL>

<portlet:resourceURL id="toolbar-message" var="toolbarMessageUrl" />
<portlet:resourceURL id="location-breadcrumb" var="locationBreadcrumbUrl" />


<c:set var="namespace"><portlet:namespace /></c:set>


<div class="trash">
    <form:form action="${updateUrl}" method="post" modelAttribute="trashForm" role="form">
        <div class="table" data-location-url="${locationBreadcrumbUrl}">
            <!-- Header -->
            <div class="table-row table-header">
                <!-- Header contextual toolbar -->
                <div class="contextual-toolbar">
                    <nav class="navbar navbar-default">
                        <h3 class="sr-only"><op:translate key="TRASH_TOOLBAR_TITLE"/></h3>
                    
                        <div class="container-fluid">
                            <!-- Information text -->
                            <p class="navbar-text hidden-xs hidden-sm" data-url="${toolbarMessageUrl}"></p>
                        
                            <!-- Restore selection -->
                            <button type="button" class="btn btn-default navbar-btn" data-toggle="modal" data-target="#${namespace}-restore-selection">
                                <i class="glyphicons glyphicons-history"></i>
                                <span><op:translate key="TRASH_TOOLBAR_RESTORE_SELECTION" /></span>
                            </button>
                            
                            <!-- Delete selection -->
                            <button type="button" class="btn btn-default navbar-btn" data-toggle="modal" data-target="#${namespace}-delete-selection">
                                <i class="glyphicons glyphicons-bin"></i>
                                <span><op:translate key="TRASH_TOOLBAR_DELETE_SELECTION" /></span>
                            </button>
                            
                            <!-- Unselect -->
                            <button type="button" class="btn btn-link navbar-btn hidden-xs unselect">
                                <span><op:translate key="TRASH_TOOLBAR_UNSELECT" /></span>
                            </button>
                        </div>
                    </nav>
                </div>
            
                <div class="row">
                    <!-- Document -->
                    <div class="col-sm-6 col-md-4">
                        <a href="${sortTitleUrl}"><op:translate key="TRASH_HEADER_DOCUMENT" /></a>
                        
                        <c:if test="${sort eq 'title'}">
                            <small class="text-muted">
                                <c:choose>
                                    <c:when test="${alt}"><i class="halflings halflings-sort-by-attributes-alt"></i></c:when>
                                    <c:otherwise><i class="halflings halflings-sort-by-attributes"></i></c:otherwise>
                                </c:choose>
                            </small>
                        </c:if>
                    </div>
                    
                    <!-- Date -->
                    <div class="col-sm-6 col-md-4">
                        <a href="${sortDateUrl}"><op:translate key="TRASH_HEADER_DATE" /></a>
                        
                        <c:if test="${sort eq 'date'}">
                            <small class="text-muted">
                                <c:choose>
                                    <c:when test="${alt}"><i class="halflings halflings-sort-by-attributes-alt"></i></c:when>
                                    <c:otherwise><i class="halflings halflings-sort-by-attributes"></i></c:otherwise>
                                </c:choose>
                            </small>
                        </c:if>
                    </div>
                    
                    <!-- Location -->
                    <div class="col-md-4 hidden-xs hidden-sm">
                        <a href="${sortLocationUrl}"><op:translate key="TRASH_HEADER_LOCATION" /></a>
                        
                        <c:if test="${sort eq 'location'}">
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
                <div class="popover-container">
                    <ul class="list-unstyled selectable">
                        <c:forEach var="trashedDocument" items="${trashForm.trashedDocuments}" varStatus="status">
                            <li>
                                <div class="table-row" data-location-path="${trashedDocument.location.path}">
                                    <form:hidden path="trashedDocuments[${status.index}].selected" />
                                
                                    <div class="row">
                                        <!-- Document -->
                                        <div class="col-sm-6 col-md-4">
                                            <div class="form-control-static text-overflow">
                                                <c:if test="${not empty trashedDocument.icon}">
                                                    <i class="${trashedDocument.icon}"></i>
                                                </c:if>
                                                
                                                <span>${trashedDocument.title}</span>
                                            </div>
                                        </div>
                                        
                                        <!-- Date -->
                                        <div class="col-sm-6 col-md-4">
                                            <div class="text-overflow">
                                                <span><op:formatRelativeDate value="${trashedDocument.deletionDate}" /></span>
                                                <br>
                                                <small><op:translate key="TRASH_BY_LAST_CONTRIBUTOR" args="${trashedDocument.lastContributor}" /></small>
                                            </div>
                                        </div>
                                        
                                        <!-- Location -->
                                        <div class="col-md-4 hidden-xs hidden-sm">
                                            <button type="button" class="btn btn-default text-overflow location">
                                                <i class="${trashedDocument.location.icon}"></i>
                                                <span>${trashedDocument.location.title}</span>
                                            </button>
                                            <%-- <a href="#" class="btn btn-default text-overflow no-ajax-link location" data-toggle="popover" data-content="..." data-container=".table-row" data-placement="top" data-trigger="focus" tabindex="0" role="button">
                                                <i class="${trashedDocument.location.icon}"></i>
                                                <span>${trashedDocument.location.title}</span>
                                            </a> --%>
                                        </div>
                                    </div>
                                </div>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            
                <!-- No results -->
                <c:if test="${empty trashForm.trashedDocuments}">
                    <div class="table-row">
                        <div class="text-center"><op:translate key="TRASH_EMPTY_MESSAGE" /></div>
                    </div>
                </c:if>
            </div>
        </div>
        
        
        <!-- Restore selection -->
        <div id="${namespace}-restore-selection" class="modal fade" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                        
                        <h4 class="modal-title">
                            <span><op:translate key="TRASH_RESTORE_SELECTION_MODAL_TITLE" /></span>
                        </h4>
                    </div>
                    
                    <div class="modal-body">
                        <p>
                            <span><op:translate key="TRASH_RESTORE_SELECTION_MODAL_MESSAGE" /></span>
                        </p>
                    </div>
                    
                    <div class="modal-footer">
                        <button type="submit" name="restore" class="btn btn-primary" data-dismiss="modal">
                            <span><op:translate key="TRASH_RESTORE_SELECTION" /></span>
                        </button>
                        
                        <button type="button" class="btn btn-default" data-dismiss="modal">
                            <span><op:translate key="CANCEL" /></span>
                        </button>
                    </div>
                </div>
            </div>
        </div>
        
        
        <!-- Delete selection -->
        <div id="${namespace}-empty-trash" class="modal fade" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                        
                        <h4 class="modal-title">
                            <span><op:translate key="TRASH_DELETE_SELECTION_MODAL_TITLE" /></span>
                        </h4>
                    </div>
                    
                    <div class="modal-body">
                        <p class="text-danger">
                            <span><op:translate key="TRASH_DELETE_SELECTION_MODAL_MESSAGE" /></span>
                        </p>
                        
                        <div class="alert alert-danger">
                            <i class="glyphicons glyphicons-exclamation-sign"></i>
                            <strong><op:translate key="TRASH_DELETE_MODAL_ALERT_MESSAGE" /></strong>
                        </div>
                    </div>
                    
                    <div class="modal-footer">
                        <button type="submit" name="delete" class="btn btn-danger" data-dismiss="modal">
                            <span><op:translate key="TRASH_DELETE_SELECTION" /></span>
                        </button>
                        
                        <button type="button" class="btn btn-default" data-dismiss="modal">
                            <span><op:translate key="CANCEL" /></span>
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </form:form>
    
    
    <!-- Restore all modal -->
    <div id="${namespace}-restore-all" class="modal fade" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                    
                    <h4 class="modal-title">
                        <span><op:translate key="TRASH_RESTORE_ALL_MODAL_TITLE" /></span>
                    </h4>
                </div>
                
                <div class="modal-body">
                    <p>
                        <span><op:translate key="TRASH_RESTORE_ALL_MODAL_MESSAGE" /></span>
                    </p>
                </div>
                
                <div class="modal-footer">
                    <a href="${restoreUrl}" class="btn btn-primary" data-dismiss="modal">
                        <span><op:translate key="TRASH_RESTORE_ALL" /></span>
                    </a>
                    
                    <button type="button" class="btn btn-default" data-dismiss="modal">
                        <span><op:translate key="CANCEL" /></span>
                    </button>
                </div>
            </div>
        </div>
    </div>
    
    
    <!-- Empty trash modal -->
    <div id="${namespace}-empty-trash" class="modal fade" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                    
                    <h4 class="modal-title">
                        <span><op:translate key="TRASH_EMPTY_TRASH_MODAL_TITLE" /></span>
                    </h4>
                </div>
                
                <div class="modal-body">
                    <p class="text-danger">
                        <span><op:translate key="TRASH_EMPTY_TRASH_MODAL_MESSAGE" /></span>
                    </p>
                    
                    <div class="alert alert-danger">
                        <i class="glyphicons glyphicons-exclamation-sign"></i>
                        <strong><op:translate key="TRASH_DELETE_MODAL_ALERT_MESSAGE" /></strong>
                    </div>
                </div>
                
                <div class="modal-footer">
                    <a href="${emptyUrl}" class="btn btn-danger" data-dismiss="modal">
                        <span><op:translate key="TRASH_EMPTY_TRASH" /></span>
                    </a>
                    
                    <button type="button" class="btn btn-default" data-dismiss="modal">
                        <span><op:translate key="CANCEL" /></span>
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
