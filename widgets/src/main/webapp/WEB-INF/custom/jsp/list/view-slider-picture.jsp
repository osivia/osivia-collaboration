<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ page isELIgnored="false"%>


<div class="bxslider-container">
    <ul class="list-unstyled bxslider bxslider-default clearfix" data-pause="${timer}">
        <c:forEach var="document" items="${documents}" varStatus="status">
            <c:set var="pictureUrl"><ttc:documentLink document="${document}" picture="true" displayContext="Medium"/></c:set>
        
            <li class="bxslider-slide">
				<img src="${pictureUrl}" alt="" class="img-responsive center-block" />
            </li>
        </c:forEach>
    </ul>
</div>
