<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />


<div class="workspace-member-management">
    <!-- Tabs -->
    <%@ include file="../commons/tabs.jspf" %>
    
            <c:if test="${import.batchRunning}">

			   <div class="row">
					<div class="col-sm-12">
						<div class="alert alert-info" role="alert">
			        			<op:translate key="WORKSPACE_MEMBER_MANAGEMENT_INVITATION_BATCH_RUNNING" args="${import.timeEstimated}"/>
						</div>
					</div>
				
				</div>
        	</c:if> 
	                    
    
    <div class="panel panel-default">
        <div class="panel-body">
            <!-- Invitations creation -->
            <%@ include file="import.jspf" %>
                
        </div>
    </div>
</div>
