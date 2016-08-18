<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />

<div class="person">
    <div class="person-avatar">
        <img src="${member.person.avatar}" alt="">
    </div>
    <div class="person-title">${member.person.displayName}</div>
    <c:if test="${not empty member.person.mail}">
        <div class="person-extra">${member.person.mail}</div>
    </c:if>
</div>