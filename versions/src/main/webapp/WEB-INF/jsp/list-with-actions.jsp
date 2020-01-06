<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page isELIgnored="false" %>

<c:set var="namespace"><portlet:namespace /></c:set>

<div class="table">
        <!-- Header -->
        <div class="table-header">
            <div class="row">
                <!-- Local group -->
                <div class="col-xs-12 col-sm-10">
                    <span><op:translate key="VERSIONS" /></span>
                </div>
            </div>
        </div>
        
        <!-- Body -->
        <c:forEach var="version" items="${versions}" varStatus="status">
            <portlet:actionURL name="restore" var="restoreUrl">
                <portlet:param name="versionId" value="${version.id}"/>
            </portlet:actionURL>

			<div class="row">
				<fieldset>
					<!-- Local group -->
					<div class="col-sm-6">
						<span>${version.title}</span>
					</div>
					<div class="col-sm-2">
						<span>${version.label}</span>
					</div>
					
					<div class="col-sm-4 actions">
                        <!-- Restore -->
                        <a href="${restoreUrl}" class="btn btn-secondary">
                            <i class="glyphicons glyphicons-pencil"></i>
                            <span class="sr-only"><op:translate key="RESTORE" /></span>
                        </a>
                    </div>
	
				</fieldset>
			</div>
	</c:forEach>
        
        <!-- No results -->
        <c:if test="${empty versions}">
            <div class="row">
                <div class="col-xs-12 text-center"><op:translate key="NO_VERSION" /></div>
            </div>
        </c:if>
    </div>
