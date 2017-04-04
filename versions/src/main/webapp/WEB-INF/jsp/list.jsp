<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal"
	prefix="op"%>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice"
	prefix="ttc"%>

<%@ page isELIgnored="false"%>

<p class="lead">
	<i class="glyphicons glyphicons-history"></i> <span><op:translate
			key="VERSIONS" /></span>
</p>

<div class="list-group">
	<c:forEach var="version" items="${versions}">
		<c:set var="link">
			<ttc:documentLink document="${version}" displayContext="downloadVersion" />
		</c:set>
		<!-- FIXME: do not use dc:modified -->
		<c:set var="created" value="${version.properties['dc:modified']}" />
		<p>
			<a href="${link}" target="_blank" class="list-group-item">
			     <op:formatRelativeDate value="${created}" />
			</a>
		</p>
	</c:forEach>

	<!-- No results -->
	<c:if test="${empty versions}">
		<div class="row">
			<div class="col-xs-12 text-center">
				<op:translate key="NO_VERSION" />
			</div>
		</div>
	</c:if>
</div>
