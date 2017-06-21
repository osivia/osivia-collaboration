<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page isELIgnored="false" %>


<!-- Picture -->
<c:if test="${not empty imageSource}">
    <p>
        <a href="${url}" target="${external ? '_blank' : ''}" class="no-ajax-link">
            <img src="${imageSource}" alt="" class="img-responsive center-block" />
        </a>
    </p>
</c:if>


<!-- Title -->
<c:if test="${not empty title}">
    <h2>
        <a href="${url}" target="${external ? '_blank' : ''}" class="no-ajax-link">${title}</a>
    </h2>
</c:if>


<!-- Content -->
<c:if test="${not empty content}">
    <div>${content}</div>
</c:if>
