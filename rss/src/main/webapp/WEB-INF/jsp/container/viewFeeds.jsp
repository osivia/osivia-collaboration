<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="c"       uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"    uri="http://www.springframework.org/tags/form"  %>
<%@ taglib prefix="op"      uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>

<%@ page isELIgnored="false" %>

<portlet:renderURL var="addUrl">
	<portlet:param name="add" value="feed" />
</portlet:renderURL>
<portlet:renderURL var="editContainer">
	<portlet:param name="edit" value="container" />
</portlet:renderURL>
<portlet:actionURL name="synchro" var="synchro" />

<form:form action="${synchro}" method="post" modelAttribute="form">
	<div>
		<c:if test="${empty feeds}">
			<p>
				<span><op:translate key="LIST_NO_RESULT" /></span>
			</p>
		</c:if>

		<ul class="list-inline">
			<c:if test="${!empty feeds}">
				<table class="table table-condensed table-hover">
					<thead>
						<tr>
							<th><op:translate key="NAME_TITLE" /></th>
							<th><op:translate key="URL_TITLE" /></th>
						</tr>
					</thead>
					<c:forEach var="feed" items="${feeds}" varStatus="status">
						<portlet:renderURL var="editFeed">
							<portlet:param name="edit" value="feed" />
							<portlet:param name="id" value="${feed.syncId}" />
							<portlet:param name="index" value="${feed.indexNuxeo}" />
						</portlet:renderURL>
						<tr>
							<td><a href="${editFeed}">${feed.displayName}</a></td>
							<td>${feed.url}</td>
						</tr>
					</c:forEach>
				</table>
			</c:if>
		</ul>
	</div>

	<div  class="float-right">
		<a href="${addUrl}" class="btn btn-primary"><op:translate
				key="ADD_FEED" /></a>
		<button type="submit" name="synchro" class="btn btn-primary">
			<op:translate key="SYNCHRO" />
		</button>
		<a href="${editContainer}" class="btn btn-primary"><op:translate
				key="EDIT_CONTAINER" /></a>
	</div>
</form:form>