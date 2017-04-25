<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<c:set var="person" value="${requestScope['person']}" />


<div class="person">
    <!-- Avatar -->
    <div class="person-avatar">
        <c:choose>
            <c:when test="${empty person.person.avatar.url}">
                <i class="glyphicons glyphicons-user"></i>
            </c:when>
            
            <c:otherwise>
                <img src="${person.person.avatar.url}" alt="">
            </c:otherwise>
        </c:choose>
    </div>
    
    <!-- Display name -->
    <div class="person-title">
        <c:choose>
            <c:when test="${empty person.displayName}">
                <span>${person.id}</span>
            </c:when>
            
            <c:otherwise>
                <span>${person.displayName}</span>
            </c:otherwise>
        </c:choose>
    </div>
    
    <!-- Extra -->
    <c:if test="${not empty person.extra}">
        <div class="person-extra">${person.extra}</div>
    </c:if>
</div>
