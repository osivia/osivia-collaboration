<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />

<c:set var="namespace"><portlet:namespace /></c:set>


<div class="workspace-member-management">
    <!-- Tabs -->
    <%@ include file="../commons/tabs.jspf" %>

    <!-- Requests -->
    <div class="panel panel-default">
        <div class="panel-body">
            <%@ include file="table.jspf" %>
        </div>
    </div>
    
    <!-- User message modal -->
    <div id="${namespace}-user-message" class="modal fade" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <i class="glyphicons glyphicons-remove"></i>
                        <span class="sr-only"><op:translate key="CLOSE" /></span>
                    </button>
                    
                    <h4 class="modal-title"></h4>
                </div>
                
                <div class="modal-body">
                    <p class="text-pre-wrap"></p>
                </div>
                
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">
                        <span><op:translate key="CLOSE" /></span>
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
