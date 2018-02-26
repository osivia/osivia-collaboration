<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<%@ page contentType="text/html" isELIgnored="false"%>

<portlet:defineObjects />

<portlet:actionURL name="putInBin" var="putInBinUrl">
	<portlet:param name="sort" value="${options.sort}"/>
	<portlet:param name="alt" value="${options.alt}"/>
	<portlet:param name="pageNumber" value="${options.pageNumber}"/>
	<portlet:param name="pageSize" value="${options.pageSize}"/>
	<portlet:param name="way" value="other"/>
</portlet:actionURL>
<portlet:actionURL name="purge" var="purgeUrl" >
	<portlet:param name="sort" value="${options.sort}"/>
	<portlet:param name="alt" value="${options.alt}"/>
	<portlet:param name="pageNumber" value="${options.pageNumber}"/>
	<portlet:param name="pageSize" value="${options.pageSize}"/>
	<portlet:param name="way" value="other"/>
</portlet:actionURL>

<portlet:renderURL var="sortTitleUrl">
    <portlet:param name="sort" value="title" />
    <portlet:param name="alt" value="${options.sort eq 'title' and not options.alt}"/>
    <portlet:param name="pageNumber" value="1"/>
	<portlet:param name="pageSize" value="${options.pageSize}"/>
	<portlet:param name="way" value="other"/>
</portlet:renderURL>
<portlet:renderURL var="sortExpirationDateUrl">
    <portlet:param name="sort" value="expirationDate" />
    <portlet:param name="alt" value="${options.sort eq 'expirationDate' and not options.alt}"/>
    <portlet:param name="pageNumber" value="1"/>
	<portlet:param name="pageSize" value="${options.pageSize}"/>
	<portlet:param name="way" value="other"/>
</portlet:renderURL>
<portlet:renderURL var="sortDeletedDateUrl">
    <portlet:param name="sort" value="deletedDate" />
    <portlet:param name="alt" value="${options.sort eq 'deletedDate' and not options.alt}"/>
    <portlet:param name="pageNumber" value="1"/>
	<portlet:param name="pageSize" value="${options.pageSize}"/>
	<portlet:param name="way" value="other"/>
</portlet:renderURL>


<div class="workspace-management">
	<form:form action="${putInBinUrl}" method="post" enctype="multipart/form-data" modelAttribute="form" cssClass="form-horizontal" role="form">
		<div class="table">

			<!-- Header -->
        	<%@ include file="header.jspf" %>

	        <!-- Body -->
	        <%@ include file="body.jspf" %>

		</div>
		<!-- Paging -->
	    <%@ include file="paging.jspf" %>
	    
		<form:hidden path="selectResult" />

		<!-- Buttons -->
		<%@ include file="buttons.jspf" %>
		

	</form:form>
</div>