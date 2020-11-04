<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>


<c:set var="namespace"><portlet:namespace/></c:set>


<span class="text-danger"><op:translate key="DOCUMENT_EDITION_FORM_FILE_SIZE_LIMIT_EXCEEDED" args="${uploadMaxSize}"/></span>
