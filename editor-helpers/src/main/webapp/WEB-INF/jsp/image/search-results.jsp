<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>
<%@ taglib prefix="ttc" uri="http://www.toutatice.fr/jsp/taglib/toutatice" %>


<%--@elvariable id="emptyFilter" type="java.lang.Boolean"--%>
<%--@elvariable id="results" type="java.util.List"--%>
<%--@elvariable id="total" type="java.lang.Integer"--%>
<c:choose>
    <c:when test="${emptyFilter}">
        <p class="form-control-plaintext">
            <span class="text-muted"><op:translate key="EDITOR_SEARCH_EMPTY_FILTER"/></span>
        </p>
    </c:when>

    <c:when test="${empty results}">
        <p class="form-control-plaintext">
            <span class="text-muted"><op:translate key="EDITOR_SEARCH_EMPTY"/></span>
        </p>
    </c:when>

    <c:otherwise>
        <p>
            <span class="text-muted">
                <c:choose>
                    <c:when test="${fn:length(results) eq 1}"><op:translate key="EDITOR_SEARCH_RESULT"/></c:when>
                    <c:when test="${total gt fn:length(results)}"><op:translate key="EDITOR_SEARCH_RESULTS_WITH_TOTAL" args="${fn:length(results)},${total}"/></c:when>
                    <c:otherwise><op:translate key="EDITOR_SEARCH_RESULTS" args="${fn:length(results)}"/></c:otherwise>
                </c:choose>
            </span>
        </p>

        <div class="row">
            <c:forEach var="document" items="${results}">
                <ttc:documentLink document="${document}" picture="true" displayContext="Medium"
                                  var="imageLink"/>
                <portlet:actionURL name="select" var="selectUrl" copyCurrentRenderParameters="true">
                    <portlet:param name="path" value="${document.path}"/>
                </portlet:actionURL>

                <div class="col-xs-6 col-sm-4">
                    <a href="${selectUrl}" class="thumbnail" title="${document.title}">
                        <img src="${imageLink.url}" alt="">
                        <span>${document.title}</span>
                    </a>
                </div>
            </c:forEach>
        </div>
    </c:otherwise>
</c:choose>