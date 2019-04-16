<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<portlet:defineObjects />


<div class="workspace-member-management">
    <!-- Tabs -->
    <%@ include file="../commons/tabs.jspf" %>
    
    <div class="panel panel-default">
    	<div class="row">
    		<div class="col-lg-4 col-md-6 col-sm-12">
		   		<div class="panel-body">
		            <!-- Selection -->
		            <%@ include file="selection.jspf" %>
		            
		            <hr>
		            
		            <!-- Form -->
		            <%@ include file="form.jspf" %>
		        </div>
    		</div>
    	</div>

    </div>
</div>
