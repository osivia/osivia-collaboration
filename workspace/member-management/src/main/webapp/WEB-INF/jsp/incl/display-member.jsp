<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<div class="person">
    <div class="person-avatar">
        <img src="${member.person.avatar.url}" alt="">
    </div>
    <div class="person-title">${member.person.displayName}</div>
    <c:if test="${not empty member.person.mail}">
        <div class="person-extra">${member.person.mail}</div>
    </c:if>
</div>