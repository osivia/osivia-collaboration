<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>
<%@ taglib prefix="ttc" uri="http://www.toutatice.fr/jsp/taglib/toutatice" %>


<%--@elvariable id="results" type="java.util.List"--%>
<c:choose>
    <c:when test="${empty results}">
        <p class="form-control-plaintext">
            <span class="text-muted"><op:translate key="EDITOR_IMAGE_DOCUMENT_FORM_DOCUMENTS_EMPTY"/></span>
        </p>
    </c:when>

    <c:otherwise>
        <div class="row">
            <c:forEach var="document" items="${results}">
                <ttc:documentLink document="${document}" picture="true" displayContext="Medium"
                                  var="imageLink"/>
                <portlet:actionURL name="select" var="selectUrl" copyCurrentRenderParameters="true">
                    <portlet:param name="path" value="${document.path}"/>
                </portlet:actionURL>

                <div class="col-xs-6">
                    <a href="${selectUrl}" class="thumbnail" title="${document.title}">
                        <img src="${imageLink.url}" alt="${document.title}">
                    </a>
                </div>
            </c:forEach>
        </div>
    </c:otherwise>
</c:choose>
