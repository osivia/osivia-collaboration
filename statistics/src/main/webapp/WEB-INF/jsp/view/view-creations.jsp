<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<portlet:actionURL name="changeView" var="changeViewUrl">
    <portlet:param name="tab" value="creations" />
</portlet:actionURL>

<portlet:resourceURL id="creations" var="creationsUrl" />


<div class="statistics">
    <!-- Tabs -->
    <c:set var="tab" value="creations" scope="request" />
    <%@ include file="tabs.jspf" %>

    <!-- Title -->
    <h3><op:translate key="CREATIONS_TITLE" /></h3>
    
    <!-- View selector -->
    <form:form action="${changeViewUrl}" method="post" modelAttribute="form" onchange="$JQry(this).find('input[type=submit]').click()" class="form-inline" role="form">
        <div class="form-group">
            <c:forEach var="view" items="${form.creationsViews}">
                <div class="radio">
                    <label>
                        <form:radiobutton path="creationsView" value="${view}" />
                        <span><op:translate key="${view.key}"/></span>
                    </label>
                </div>
            </c:forEach>
        </div>
        
        <input type="submit" class="hidden-script">
    </form:form>
    
    <!-- Chart -->
    <div class="clearfix">
        <canvas class="chart" height="400" data-url="${creationsUrl}"></canvas>
    </div>
    
    <!-- Table -->
    <div class="table-responsive">
        <table class="table table-condensed table-hover">
            <thead>
                <tr>
                    <th></th>
                    <th><op:translate key="CREATIONS_DIFFERENTIAL" /></th>
                    <th><op:translate key="CREATIONS_AGGREGATE" /></th>
                </tr>
            </thead>
        </table>
    </div>
    
    
    
    <%-- <c:if test="${empty windowSettings.request}">
        <p class="text-danger">
            <i class="halflings halflings-exclamation-sign"></i>
            <span><op:translate key="ERROR_REQUEST_UNDEFINED" /></span>
        </p>
    </c:if>
    
    <form:form action="${changeViewUrl}" method="post" modelAttribute="windowSettings" onchange="$JQry(this).find('button[type=submit]').click()" cssClass="form-inline" role="form">
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
            <canvas class="chart" height="400" data-type="bar" data-url="${loadStatisticsUrl}"></canvas>
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
    </div> --%>
</div>
