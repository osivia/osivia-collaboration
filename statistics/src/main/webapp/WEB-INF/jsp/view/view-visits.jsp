<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<portlet:actionURL name="changeView" var="changeViewUrl">
    <portlet:param name="tab" value="visits" />
</portlet:actionURL>

<portlet:resourceURL id="visits" var="visitsUrl" />


<div class="statistics">
    <!-- Tabs -->
    <c:set var="tab" value="visits" scope="request" />
    <%@ include file="tabs.jspf" %>
    
    <!-- Title -->
    <h3><op:translate key="VISITS_TITLE" /></h3>
    
    <!-- View selector -->
    <form:form action="${changeViewUrl}" method="post" modelAttribute="form" onchange="$JQry(this).find('input[type=submit]').click()" class="form-inline" role="form">
        <div class="form-group">
            <c:forEach var="view" items="${form.visitsViews}">
                <div class="radio">
                    <label>
                        <form:radiobutton path="visitsView" value="${view}" />
                        <span><op:translate key="${view.key}"/></span>
                    </label>
                </div>
            </c:forEach>
        </div>
        
        <input type="submit" class="hidden-script">
    </form:form>
    
    <!-- Chart -->
    <div class="clearfix">
        <canvas class="chart" height="400" data-url="${visitsUrl}"></canvas>
    </div>
    
    <!-- Table -->
    <div class="table-responsive">
        <table class="table table-condensed table-hover">
            <thead>
                <tr>
                    <th><op:translate key="VISITS_DATE" /></th>
                    <th><op:translate key="VISITS_HITS" /></th>
                    <th><op:translate key="VISITS_UNIQUE_VISITORS" /></th>
                </tr>
            </thead>
        </table>
    </div>
</div>
