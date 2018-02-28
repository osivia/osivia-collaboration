<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>

<%@ page isELIgnored="false" %>


<ul class="list-unstyled">
    <c:forEach var="document" items="${documents}">
        <c:set var="vignetteUrl"><ttc:pictureLink document="${document}" property="ttc:vignette" /></c:set>
        <c:set var="workspaceType" value="${document.properties['workspaceType']}" />
        <c:set var="description" value="${document.properties['dc:description']}" />
    
        <li class="media">
            <!-- Vignette -->
            <c:if test="${not empty vignetteUrl}">
                <div class="media-left media-middle">
                    <img src="${vignetteUrl}" alt="" class="media-object">
                </div>
            </c:if>
            
            <div class="media-body media-middle">
                <!-- Title -->
                <h3 class="h4 media-heading">
                    <span><ttc:title document="${document}" /></span>
                </h3>
                
                <!-- Type -->
                <c:if test="${not empty workspaceType}">
                    <p>
                        <span class="label label-${workspaceType.color}">
                            <i class="${workspaceType.icon}"></i>
                            <span><op:translate key="LIST_TEMPLATE_${workspaceType.key}" /></span>
                        </span>
                    </p>
                </c:if>
                
                <!-- Description -->
                <c:if test="${not empty description}">
                    <p class="text-pre-wrap">${description}</p>
                </c:if>
            </div>
        </li>
    </c:forEach>
    
    <c:if test="${empty documents}">
        <li>
            <p class="text-center">
                <span class="text-muted"><op:translate key="LIST_NO_ITEMS" /></span>
            </p>
        </li>
    </c:if>
</ul>
