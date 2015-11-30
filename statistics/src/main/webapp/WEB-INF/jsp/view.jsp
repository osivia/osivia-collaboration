<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" %>


<portlet:defineObjects />

<portlet:resourceURL id="loadStatistics" var="url" />


<c:if test="${empty configuration.request}">
    <p class="text-danger">
        <i class="halflings halflings-exclamation-sign"></i>
        <span>Erreur&nbsp;: la requ&ecirc;te n'a pas &eacute;t&eacute; d&eacute;finie.</span>
    </p>
</c:if>

<div class="chart-container">
    <div class="clearfix">
        <canvas class="chart bar-chart" height="400" data-url="${url}"></canvas>
    </div>
</div>
