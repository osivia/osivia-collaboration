<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<portlet:resourceURL id="export-workspaces-csv" var="exportCsvUrl" />

 <!-- Export tools -->
 <div class="btn-group btn-group-sm">
     <a class="btn btn-default" href="${exportCsvUrl}">
         <i class="glyphicons glyphicons-table"></i>
         <span class="hidden-xs"><op:translate key="WORKSPACES_QUOTAS_EXPORT" /></span>
     </a>
 </div>
 
 