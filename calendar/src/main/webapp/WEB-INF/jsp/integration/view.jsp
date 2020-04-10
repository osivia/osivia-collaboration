<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>

<%@ page isELIgnored="false" %>


<c:set var="namespace"><portlet:namespace/></c:set>


<h3><op:translate key="CALENDAR_INTEGRATION_TITLE"/></h3>

<%@ include file="instructions.jspf" %>

<%@ include file="integration-url.jspf" %>

<%@ include file="buttons.jspf" %>
