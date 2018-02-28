<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<portlet:renderURL var="backUrl" />

<portlet:resourceURL id="lazyLoading" var="lazyLoadingUrl" />


<c:set var="filterLabel"><op:translate key="FILTER" /></c:set>
<c:set var="clearFilterLabel"><op:translate key="CLEAR_FILTER" /></c:set>


<div class="workspace-acl-management">
    <!-- Title -->
    <h3>
        <i class="glyphicons glyphicons-shield"></i>
        <span><op:translate key="ACL_MANAGEMENT_TITLE" /></span>
    </h3>
    
    <!-- Fancytree -->
    <div class="panel panel-default">
        <div class="panel-body">
            <div class="fancytree fancytree-default" data-lazyloadingurl="${lazyLoadingUrl}">
                <p class="input-group input-group-sm">
                    <span class="input-group-addon">
                        <i class="halflings halflings-filter"></i>
                    </span>
                    
                    <input type="text" class="form-control" placeholder="${filterLabel}" data-filter data-expand="true">
                    
                    <span class="input-group-btn">
                        <button type="button" class="btn btn-default" title="${clearFilterLabel}" data-clear-filter data-toggle="tooltip" data-placement="bottom">
                            <i class="halflings halflings-erase"></i>
                            <span class="sr-only">${clearFilterLabel}</span>
                        </button>
                    </span>
                </p>
            </div>
        </div>
    </div>
    
    <div>
        <!-- Back -->
        <a href="${backUrl}" class="btn btn-default">
            <span><op:translate key="BACK" /></span>
        </a>
    </div>
</div>
