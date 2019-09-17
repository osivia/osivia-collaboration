<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<portlet:defineObjects/>

<portlet:resourceURL id="refresh" var="refreshUrl" />


<c:set var="namespace"><portlet:namespace/></c:set>



<div class="quota"> 
	<c:if test="${quotaForm.asynchronous}">
		<div class="asynchronous" data-url="${refreshUrl}">
		</div>
	</c:if>

	<%@ include file="quota.jspf" %>
</div> 	