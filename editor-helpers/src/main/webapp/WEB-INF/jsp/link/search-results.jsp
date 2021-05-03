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

        <c:forEach var="document" items="${results}">
            <portlet:actionURL name="select" var="selectUrl" copyCurrentRenderParameters="true">
                <portlet:param name="path" value="${document.path}"/>
            </portlet:actionURL>

            <c:set var="vignetteUrl"><ttc:pictureLink document="${document}" property="ttc:vignette" /></c:set>
            <c:choose>
                <c:when test="${not empty vignetteUrl}">
                    <c:set var="previewUrl" value="${vignetteUrl}"/>
                </c:when>
                <c:when test="${document.type.name eq 'Picture'}">
                    <c:set var="previewUrl"><ttc:documentLink document="${document}" picture="true" displayContext="Thumbnail" /></c:set>
                </c:when>
                <c:otherwise>
                    <c:remove var="previewUrl"/>
                </c:otherwise>
            </c:choose>

            <a href="${selectUrl}" class="thumbnail" title="${document.title}">
                <span class="media">
                    <span class="media-left media-middle">
                        <ttc:icon document="${document}" style="inline"/>
                    </span>
                    <span class="media-body media-middle">${document.title}</span>
                    <c:if test="${not empty previewUrl}">
                        <span class="media-right media-middle">
                            <img src="${previewUrl}" alt="" class="media-object">
                        </span>
                    </c:if>
                </span>
            </a>
        </c:forEach>
    </c:otherwise>
</c:choose>
