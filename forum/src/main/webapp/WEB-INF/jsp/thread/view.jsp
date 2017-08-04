<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<portlet:actionURL name="save" var="saveUrl"/>

<portlet:resourceURL id="quote" var="quoteUrl"/>

<c:set var="namespace" scope="request"><portlet:namespace/></c:set>
<c:set var="editorUrl" scope="request"><portlet:resourceURL id="editor"/></c:set>


<div class="forum forum-thread">
    <form:form action="${saveUrl}" method="post" enctype="multipart/form-data" modelAttribute="form" role="form" data-quote-url="${quoteUrl}">
        <!-- Thread -->
        <%@ include file="thread.jspf" %>

        <!-- Thread posts -->
        <%@ include file="posts.jspf" %>

        <!-- Reply -->
        <%@ include file="reply.jspf" %>

        <!-- Modals -->
        <%@ include file="modals.jspf" %>
    </form:form>
</div>
