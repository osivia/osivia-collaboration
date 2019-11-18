<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="op"  uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>
<%@ taglib prefix="ttc" uri="http://www.toutatice.fr/jsp/taglib/toutatice" %>

<%@ page isELIgnored="false" %>

<portlet:renderURL var="addUrl">
	<portlet:param name="view" value="add" />
</portlet:renderURL>

<div>
    <c:if test="${empty containers}">
        <p>
            <span><op:translate key="LIST_NO_RESULT" /></span>
        </p>
    </c:if>

	<table class="table table-condensed table-hover">
		<thead>
			<tr>
				<th><op:translate key="NAME_CONTAINER" /></th>
				<th><op:translate key="EDITION" /></th>
			</tr>
		</thead>
		<c:forEach var="container" items="${containers}" varStatus="status">
			<portlet:renderURL var="editContainer">
				<portlet:param name="view" value="edit" />
				<portlet:param name="name" value="${container.name}" />
				<portlet:param name="path" value="${container.path}" />
				<portlet:param name="id"   value="${container.document.id}" />
			</portlet:renderURL>
			<tr>
				<td><ttc:title document="${container.document}" /></td>
				<td><a href="${editContainer}" class="btn btn-primary"><op:translate key="EDIT_CONTAINER"/></a></td>
			</tr>
		</c:forEach>
	</table>

</div>

<div>
	<a href="${addUrl}" class="btn btn-primary"><op:translate key="ADD_CONTAINER"/></a>
</div>