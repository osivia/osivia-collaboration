<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal"
	prefix="op"%>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice"
	prefix="ttc"%>

<%@ page isELIgnored="false"%>

<!-- Title -->
<p class="lead">
	<i class="glyphicons glyphicons-history"></i> <span><op:translate
			key="VERSIONS" /></span><br />
	<!-- SubTitle -->
	<small class="text-muted"><op:translate key="VERSIONS_SUBTITLE" /></small>
</p>

<c:choose>

	<c:when test="${empty versions}">
		<div class="row">
			<div class="col-xs-12 text-center">
				<op:translate key="NO_VERSION" />
			</div>
		</div>

	</c:when>

	<c:otherwise>
		<!-- Date format -->
		<c:set var="dateFormat">
			<op:translate key='VERSION_DATE_FORMAT' />
		</c:set>


		<table class="table table-hover">
			<thead>
				<tr>
					<th><op:translate key='VERSION_CREATION' /></th>
					<th><op:translate key='VERSION_CREATOR' /></th>
					<th><op:translate key='VERSION_SIZE' /></th>
					<th><op:translate key='VERSION_DOWNLOADING' /></th>
				</tr>
			</thead>
			<tbody>

				<c:forEach var="version" items="${versions}">

					<c:set var="link">
						<ttc:documentLink document="${version}"
							displayContext="downloadVersion" />
					</c:set>

					<tr>
					   <!-- Date -->
					   <td><fmt:formatDate pattern="${dateFormat}" value="${version.properties['dc:modified']}" /></td>
					   <!-- Creator -->
					   <td><ttc:user name="${version.properties['dc:lastContributor']}" linkable="false" /></td>
					   <!-- Size -->
					   <td><ttc:fileSize size="${version.properties['common:size']}" /></td>
					   <!-- Download -->
					   <td><a href="${link}" target="_blank"><op:translate key='VERSION_DOWNLOAD' /></a></td>
					</tr>

				</c:forEach>

			</tbody>
		</table>

	</c:otherwise>
</c:choose>
