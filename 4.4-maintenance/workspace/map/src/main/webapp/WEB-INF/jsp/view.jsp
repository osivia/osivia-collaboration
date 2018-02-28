<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<portlet:defineObjects />


<portlet:resourceURL id="lazyLoading" var="lazyLoadingUrl">
    <portlet:param name="cmsBasePath" value="${options.workspacePath}" />
    <portlet:param name="cmsNavigationPath" value="${options.navigationPath}" />
    <portlet:param name="excludedTypes" value="Staple" />
    <portlet:param name="navigation" value="true" />
    <portlet:param name="live" value="true" />
    <portlet:param name="link" value="true" />
    <portlet:param name="fullLoad" value="true" />
</portlet:resourceURL>


<c:set var="filterLabel"><op:translate key="FILTER" /></c:set>
<c:set var="clearFilterLabel"><op:translate key="CLEAR_FILTER" /></c:set>


<div class="panel panel-default">
    <div class="panel-body">
        <div class="fancytree fancytree-links" data-lazyloadingurl="${lazyLoadingUrl}">
            <p class="input-group input-group-sm">
                <span class="input-group-addon">
                    <i class="halflings halflings-filter"></i>
                </span>
                
                <input type="text" class="form-control" placeholder="${filterLabel}" data-expand="true">
                
                <span class="input-group-btn">
                    <button type="button" class="btn btn-default" title="${clearFilterLabel}" data-toggle="tooltip" data-placement="bottom">
                        <i class="halflings halflings-erase"></i>
                        <span class="sr-only">${clearFilterLabel}</span>
                    </button>
                </span>
            </p>
        </div>
    </div>
</div>
