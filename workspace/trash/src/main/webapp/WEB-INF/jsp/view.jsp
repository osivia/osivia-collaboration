<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<portlet:defineObjects/>

<portlet:actionURL name="restore-all" var="restoreAllUrl"/>
<portlet:actionURL name="delete-all" var="deleteAllUrl"/>

<portlet:resourceURL id="location-breadcrumb" var="locationBreadcrumbUrl"/>

<c:set var="namespace"><portlet:namespace/></c:set>


<div class="trash" data-location-url="${locationBreadcrumbUrl}">
    <%@ include file="table.jspf" %>


    <!-- Restore all modal -->
    <div id="${namespace}-restore-all" class="modal fade" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">
                        <span><op:translate key="TRASH_RESTORE_ALL_MODAL_TITLE"/></span>
                    </h5>

                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>

                <div class="modal-body">
                    <p>
                        <span><op:translate key="TRASH_RESTORE_ALL_MODAL_MESSAGE"/></span>
                    </p>
                </div>

                <div class="modal-footer">
                    <a href="${restoreAllUrl}" class="btn btn-primary" data-dismiss="modal">
                        <span><op:translate key="TRASH_RESTORE_ALL"/></span>
                    </a>

                    <button type="button" class="btn btn-secondary" data-dismiss="modal">
                        <span><op:translate key="CANCEL"/></span>
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
                    <h5 class="modal-title">
                        <span><op:translate key="TRASH_EMPTY_TRASH_MODAL_TITLE"/></span>
                    </h5>

                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>

                <div class="modal-body">
                    <p class="text-danger">
                        <span><op:translate key="TRASH_EMPTY_TRASH_MODAL_MESSAGE"/></span>
                    </p>

                    <div class="alert alert-danger">
                        <i class="glyphicons glyphicons-exclamation-sign"></i>
                        <strong><op:translate key="TRASH_DELETE_MODAL_ALERT_MESSAGE"/></strong>
                    </div>
                </div>

                <div class="modal-footer">
                    <a href="${deleteAllUrl}" class="btn btn-danger" data-dismiss="modal">
                        <span><op:translate key="TRASH_EMPTY_TRASH"/></span>
                    </a>

                    <button type="button" class="btn btn-secondary" data-dismiss="modal">
                        <span><op:translate key="CANCEL"/></span>
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
