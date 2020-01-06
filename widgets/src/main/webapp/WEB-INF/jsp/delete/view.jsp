<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>
<%@ taglib prefix="ttc" uri="http://www.toutatice.fr/jsp/taglib/toutatice" %>

<%@ page isELIgnored="false" %>


<portlet:defineObjects/>

<portlet:actionURL name="delete" var="deleteUrl"/>


<div>
    <c:choose>
        <c:when test="${empty form.items}">
            <p class="mb-1 text-error"><op:translate key="DELETE_NO_ITEM_MESSAGE"/></p>
        </c:when>

        <c:when test="${fn:length(form.items) eq 1}">
            <p class="mb-1"><op:translate key="DELETE_ONE_ITEM_MESSAGE"/></p>
        </c:when>

        <c:otherwise>
            <p class="mb-1"><op:translate key="DELETE_N_ITEMS_MESSAGE" args="${fn:length(form.items)}"/></p>
        </c:otherwise>
    </c:choose>

    <ul>
        <c:forEach var="item" items="${form.items}">
            <li>
                <p class="mb-1">
                    <span><ttc:title document="${item.document}" linkable="false"/></span>

                    <c:if test="${item.childrenCount gt 0}">
                        <span class="badge badge-warning">
                            <c:choose>
                                <c:when test="${item.childrenCount eq 1}"><op:translate key="DELETE_CHILD_WARNING" /></c:when>
                                <c:otherwise><op:translate key="DELETE_CHILDREN_WARNING" args="${item.childrenCount}" /></c:otherwise>
                            </c:choose>
                        </span>
                    </c:if>
                    
                    <c:if test="${item.remoteProxies}">
                        <span class="badge badge-danger"><op:translate key="DELETE_REMOTE_PROXIES_ERROR"/></span>
                    </c:if>
                </p>
            </li>
        </c:forEach>
    </ul>


    <%--Remote proxies error message--%>
    <c:if test="${form.remoteProxiesCount gt 0}">
        <div class="alert alert-danger">
            <c:choose>
                <c:when test="${form.remoteProxiesCount gt 1}">
                    <span><op:translate key="DELETE_REMOTE_PROXIES_MULTIPLE_ERRORS_MESSAGE" args="${form.remoteProxiesCount}"/></span>
                </c:when>
                <c:when test="${fn:length(form.items) eq 1}">
                    <span><op:translate key="DELETE_REMOTE_PROXIES_ONE_DOCUMENT_ERROR_MESSAGE"/></span>
                </c:when>
                <c:otherwise>
                    <span><op:translate key="DELETE_REMOTE_PROXIES_ONE_ERROR_MESSAGE"/></span>
                </c:otherwise>
            </c:choose>
        </div>
    </c:if>


    <%--Buttons--%>
    <div class="text-right">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">
            <span><op:translate key="CANCEL"/></span>
        </button>

        <c:if test="${not empty form.items}">
            <c:choose>
                <c:when test="${form.remoteProxiesCount eq 0}">
                    <a href="${deleteUrl}" class="btn btn-primary ml-2">
                        <span><op:translate key="DELETE_ACTION"/></span>
                    </a>
                </c:when>

                <c:otherwise>
                    <a href="#" class="btn btn-primary disabled ml-2">
                        <span><op:translate key="DELETE_ACTION"/></span>
                    </a>
                </c:otherwise>
            </c:choose>
        </c:if>
    </div>
</div>
