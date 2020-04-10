<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
 
<%@ page contentType="text/html" isELIgnored="false" %>


<portlet:actionURL name="select" var="url" />


<form:form action="${url}" method="post" modelAttribute="form">
    <!-- Scope -->
    <div class="form-group">
        <c:if test="${not empty form.label}">
            <form:label path="scope" cssClass="control-label">${form.label}</form:label>
        </c:if>
        <form:select path="scope" cssClass="form-control" data-onchange="submit">
            <c:forEach items="${form.scopes}" var="scope">
                <form:option value="${scope}"><op:translate key="${scope.key}" /></form:option>
            </c:forEach>
        </form:select>
    </div>
    
    <!-- Buttons -->
    <div class="hidden-script">
        <button type="submit" class="btn btn-primary">
            <i class="glyphicons glyphicons-floppy-disk"></i>
            <span><op:translate key="SAVE" /></span>
        </button>
    </div>
</form:form>
