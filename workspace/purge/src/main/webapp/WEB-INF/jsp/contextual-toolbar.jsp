<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>


<c:set var="namespace"><portlet:namespace /></c:set>

<c:set var="messageSingleSelection"><op:translate key="WORKSPACE_MANAGEMENT_ONE_ELEMENT_SELECTED" /></c:set>
<c:set var="messageMultipleSelection"><op:translate key="WORKSPACE_MANAGEMENT_N_ELEMENTS_SELECTED" /></c:set>

<div class="contextual-toolbar hidden-print" data-ajax-shadowbox="#shadowbox-${namespace}-toolbar">
    <nav class="navbar navbar-default" role="toolbar">
        <h3 class="sr-only"><op:translate key="FILE_BROWSER_TOOLBAR_TITLE"/></h3>
    
        <div class="relative">
            <div id="shadowbox-${namespace}-toolbar" class="ajax-shadowbox">
                <div class="progress">
                    <div class="progress-bar progress-bar-striped active" role="progressbar">
                        <strong><op:translate key="AJAX_REFRESH" /></strong>
                    </div>
                </div>
            </div>
            
            <div class="container-fluid">
                <!-- Information text -->
                <p class="navbar-text hidden-xs hidden-sm">
                    <span class="message-selection label label-primary" data-message-single-selection="${messageSingleSelection}" data-message-multiple-selection="${messageMultipleSelection}">
                        <span></span>
                    </span>
                </p>
                
                <div class="btn-group btn-group-sm" role="group">
               		<!-- Delete -->
					<button type="submit" id="putInTrash" class="btn btn-secondary navbar-btn">
						<i class="glyphicons glyphicons-bin"></i> <span><op:translate key="PUT_IN_TRASH" /></span>
					</button>
                </div>
                
                <!-- Unselect -->
                <button type="button" onclick="deselect(this)" class="btn btn-link btn-sm navbar-btn hidden-xs">
                    <span><op:translate key="DESELECT" /></span>
                </button>
                
            </div>
        </div>
    </nav>
</div>
