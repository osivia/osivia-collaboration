<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" %>


<portlet:defineObjects />

<portlet:actionURL name="changeView" var="changeViewUrl" />
<portlet:resourceURL id="loadStatistics" var="loadStatisticsUrl">
    <c:if test="${not empty configuration.view}">
        <portlet:param name="view" value="${configuration.view}" />
    </c:if>
</portlet:resourceURL>


<c:if test="${empty configuration.request}">
    <p class="text-danger">
        <i class="halflings halflings-exclamation-sign"></i>
        <span>Erreur&nbsp;: la requ&ecirc;te n'a pas &eacute;t&eacute; d&eacute;finie.</span>
    </p>
</c:if>


<div class="statistics">
    <form:form action="${changeViewUrl}" method="post" modelAttribute="configuration" onchange="$JQry(this).find('button[type=submit]').click()" cssClass="form-inline" role="form">
        <div class="form-group">
            <c:forEach var="view" items="${views}">
                <label class="radio-inline">
                    <form:radiobutton path="view" value="${view.value}" />
                    <op:translate key="${view.key}" />
                </label>
            </c:forEach>
        </div>
        
        <div class="form-group hidden">
            <button type="submit" class="btn btn-default"><op:translate key="SAVE"/></button>
        </div>
    </form:form>
    
    <div class="chart-container">
        <div class="clearfix">
            <canvas class="chart bar-chart" height="400" data-url="${loadStatisticsUrl}"></canvas>
        </div>
        
    </div>
    
    <div class="table-responsive">
        <table class="table table-condensed table-hover">
            <thead>
                <tr>
                    <th></th>
                    <th><op:translate key="DIFFERENTIAL" /></th>
                    <th><op:translate key="AGGREGATE" /></th>
                </tr>
            </thead>
        </table>
    </div>
</div>
