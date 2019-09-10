<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>

<%@ page isELIgnored="false" %>


<ul class="list-unstyled">
    <c:forEach var="document" items="${documents}">
        <li>
            <p>
                <!-- Title -->
                <a href="${document.properties['url']}"><ttc:title document="${document}" linkable="false" icon="true" /></a>

                <br>

                <!-- Sharing author -->
                <c:if test="${not empty document.properties['sharing:author']}">
                    <span><op:translate key="SHARING_LIST_AUTHOR" /></span>
                    <span><ttc:user name="${document.properties['sharing:author']}" /></span>
                    
                    <br>
                </c:if>
                
                <!-- Creation date -->
                <span><op:translate key="SHARING_LIST_CREATED_ON"/></span>
                <span><fmt:formatDate value="${document.properties['dc:created']}" type="date" dateStyle="long" /></span>
            </p>
        </li>
    </c:forEach>


    <c:if test="${empty documents}">
        <li>
            <p class="text-center">
                <span class="text-muted"><op:translate key="SHARING_LIST_NO_ITEMS" /></span>
            </p>
        </li>
    </c:if>
</ul>
