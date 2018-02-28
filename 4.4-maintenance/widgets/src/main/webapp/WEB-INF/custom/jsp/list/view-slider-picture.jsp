

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ page isELIgnored="false"%>


<script src="/osivia-services-widgets/js/bxslider-fragment-integration.js"></script>


<div class="bxslider-container">
    <ul class="list-unstyled bxfgtSlider clearfix" data-timer="${timer}">
        <c:forEach var="document" items="${documents}" varStatus="status">
            <li class="bxslider-slide">
                <article class="clearfix">
                	<!-- To use in included jsp -->
                	<c:set var="doc" value="${document}" scope="request" />

					<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc"%>
					
					<%@ page isELIgnored="false" %>
					
					<c:set var="pictureURL"><ttc:documentLink document="${doc}" picture="true" displayContext="Medium"/></c:set>
					
					<div class="col-sm-12">
						<img src="${pictureURL}" alt="" class="center-block" />
					</div>
										             	
                	
                	
                </article>
            </li>
        </c:forEach>
    </ul>
</div>
