<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>

<portlet:defineObjects />


<portlet:actionURL name="submit" var="submitUrl" />
<portlet:actionURL name="unlink" var="unlinkUrl" />

<portlet:resourceURL id="search" var="searchUrl" />
<portlet:resourceURL id="filters" var="filtersUrl" />

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<c:set var="popoverTitle"><op:translate key="EDITOR_FILTERS_POPOVER_TITLE"/></c:set>


<link rel="stylesheet" href="${contextPath}/css/editor.min.css">
<script type="text/javascript" src="${contextPath}/js/editor.js"></script>


<div class="editor-link" data-close-modal="${form.done}">
    <form:form action="${submitUrl}" method="post" modelAttribute="form" cssClass="form-horizontal" role="form">
        <fieldset>
            <!-- Done indicator -->
            <form:hidden path="done" />
            <!-- URL -->
            <form:hidden path="url" />
    
            <!-- URL type -->
            <div class="form-group required">
                <form:label path="urlType" cssClass="col-sm-3 col-lg-2 control-label"><op:translate key="EDITOR_URL" /></form:label>
                <div class="col-sm-9 col-lg-10">
                    <c:forEach var="type" items="${urlTypes}">
                        <div class="radio">
                            <p>
                                <label>
                                    <form:radiobutton path="urlType" value="${type.id}" />
                                    <op:translate key="${type.key}" />
                                </label>
                            </p>
    
                            <div class="collapse ${type eq form.urlType ? 'in' : ''}">
                                <c:choose>
                                    <c:when test="${type.id eq 'MANUAL'}">
                                        <spring:bind path="manualUrl">
                                            <div class="${status.error ? 'has-error has-feedback' : ''}">
                                                <div>
                                                    <form:input path="manualUrl" cssClass="form-control" />
                                                    <c:if test="${status.error}">
                                                        <span class="form-control-feedback">
                                                            <i class="glyphicons glyphicons-remove"></i>
                                                        </span>
                                                    </c:if>
                                                </div>
                                                
                                                <form:errors path="manualUrl" cssClass="help-block" />
                                            </div>
                                        </spring:bind>
                                    </c:when>
    
                                    <c:when test="${type.id eq 'DOCUMENT'}">
                                        <c:set var="noResults"><op:translate key="SELECT2_NO_RESULTS" /></c:set>
                                        <c:set var="searching"><op:translate key="SELECT2_SEARCHING" /></c:set>
                                        <c:set var="loadingMore"><op:translate key="SELECT2_LOADING_MORE"/></c:set>
    
                                        <spring:bind path="documentWebId">
                                            <div class="${status.error ? 'has-error' : ''}">
                                                <div class="input-group">
                                                    <form:select path="documentWebId" cssClass="form-control select2" data-url="${searchUrl}" data-no-results="${noResults}" data-searching="${searching}" data-loading-more="${loadingMore}">
                                                        <c:if test="${not empty form.document}">
                                                            <form:option value="${form.document.properties['ttc:webid']}" data-icon="${form.document.icon}">${form.document.title}</form:option>
                                                        </c:if>
                                                    </form:select>
                                                    
                                                    <span class="input-group-btn">
                                                        <button type="button" title="${popoverTitle}" class="btn btn-default" data-filter data-url="${filtersUrl}">
                                                            <i class="glyphicons glyphicons-filter"></i>
                                                            <span><op:translate key="EDITOR_DOCUMENT_FILTER"/></span>
                                                        </button>
                                                    </span>
                                                </div>
                                                
                                                <form:errors path="documentWebId" cssClass="help-block" />
                                            </div>
                                        </spring:bind>
                                    </c:when>
                                </c:choose>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
    
            <!-- Text -->
            <c:if test="${form.displayText}">
                <div class="form-group">
                    <form:label path="text" cssClass="col-sm-3 col-lg-2 control-label"><op:translate key="EDITOR_TEXT" /></form:label>
                    <div class="col-sm-9 col-lg-10">
                        <form:input path="text" cssClass="form-control" />
                    </div>
                </div>
            </c:if>
    
            <!-- Title -->
            <div class="form-group">
                <form:label path="title" cssClass="col-sm-3 col-lg-2 control-label"><op:translate key="EDITOR_TITLE" /></form:label>
                <div class="col-sm-9 col-lg-10">
                    <form:input path="title" cssClass="form-control" />
                </div>
            </div>
    
            
            <!-- Buttons -->
            <div class="form-group">
                <div class="col-sm-offset-3 col-sm-9 col-lg-offset-2 col-lg-10">
                    <!-- Submit -->
                    <button type="submit" class="btn btn-primary">
                        <span><op:translate key="VALIDATE" /></span>
                    </button>
    
                    <!-- Remove -->
                    <a href="${unlinkUrl}" class="btn btn-default" ${empty form.url ? 'disabled' : ''}>
                        <span><op:translate key="EDITOR_UNLINK" /></span>
                    </a>
                    
                    <!-- Cancel -->
                    <button type="button" class="btn btn-default" data-dismiss="modal">
                        <span><op:translate key="CANCEL" /></span>
                    </button>
                </div>
            </div>
        </fieldset>
    </form:form>
</div>
