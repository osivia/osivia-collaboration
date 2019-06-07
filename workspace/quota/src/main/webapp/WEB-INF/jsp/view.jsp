<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<portlet:defineObjects/>

<portlet:actionURL name="update-quota" var="updateQuota"/>

<c:set var="namespace"><portlet:namespace/></c:set>

<div class="quota"> 
	<div class="progress"> 
		<div class="progress-bar" role="progressbar" style="width: 25%" aria-valuenow="25" aria-valuemin="0" aria-valuemax="100"></div> 
	</div> 
	<div> <small class="text-muted"> <span>${quotaForm.infos.treeSize} utilisés sur 10 Go</span> <br> <a href="${updateQuota}" class="stretched-link">Obtenir plus d'espace</a> </small> 
	</div>
</div>  


