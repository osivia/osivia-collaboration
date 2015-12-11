<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>


<!-- Picture -->
<c:if test="${not empty imageSource}">
    <p>
        <img src="${imageSource}" alt="" class="img-responsive center-block" />
    </p>
</c:if>


<h2><a href="${url}">${title}</a></h2>


<!-- Content -->
<c:if test="${not empty content}">
    <div>${content}</div>
</c:if>
