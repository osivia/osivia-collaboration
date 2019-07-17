<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>


<div>
    <%--Title--%>
    <h3 class="h4">${form.title}</h3>


    <%--Actions--%>
    <ul class="list-inline">
        <li>
            <a href="${form.viewUrl}" class="no-ajax-link">
                <span><op:translate key="CALENDAR_EVENT_PREVIEW_DETAIL"/></span>
            </a>
        </li>

        <c:if test="${not empty form.editUrl}">
            <li>
                <a href="${form.editUrl}" class="no-ajax-link">
                    <span><op:translate key="CALENDAR_EVENT_PREVIEW_EDIT"/></span>
                </a>
            </li>
        </c:if>
    </ul>


    <%--Close--%>
    <div class="text-right">
        <button type="button" class="btn btn-default" data-dismiss="modal">
            <span><op:translate key="CLOSE"/></span>
        </button>
    </div>
</div>