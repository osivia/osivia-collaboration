<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op"%>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc"%>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />


<c:if test="${not empty questions}">
    <h3>
        <i class="glyphicons glyphicons-hash"></i>
        <span>${faq.title}</span>
        <span>-</span>
        <span><op:translate key="SUMMARY" /></span>
    </h3>

    <div id="faqAccordion" class="panel-group" role="tablist">
        <c:forEach var="summaryQuestion" items="${questions}">
            <div class="panel panel-default">
                <div class="panel-heading" role="tab">
                    <h4 class="panel-title">
                        <a href="#${summaryQuestion.id}" class="no-ajax-link" data-toggle="collapse" data-parent="#faqAccordion" role="button">
                            <span>${summaryQuestion.title}</span>
                        </a>
                    </h4>
                </div>
                
                <div id="${summaryQuestion.id}" class="panel-collapse collapse" role="tabpanel">
                    <div class="panel-body">
                        <div><ttc:transform document="${summaryQuestion}" property="note:note" /></div>
                        
                        <c:if test="${not empty summaryQuestion.attachments}">
                            <hr>
                            
                            <h5 class="panel-title">
                                <i class="glyphicons glyphicons-paperclip"></i>
                                <span><op:translate key="ATTACHMENTS" /></span>
                            </h5>
                            
                            <div class="list-group">
                                <c:forEach var="attachment" items="${summaryQuestion.attachments}">
                                    <a href="${attachment.url}" class="list-group-item">
                                        <span>${attachment.name}</span>
                                    </a>
                                </c:forEach>
                            </div>
                        </c:if>
                        
                        <hr>
                        
                        <c:if test="${not empty summaryQuestion.properties['url']}">
                            <a href="${summaryQuestion.properties['url']}" class="pull-right no-ajax-link" role="button">
                                <span><op:translate key="DETAIL" /></span>
                            </a>
                        </c:if>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</c:if>

<c:if test="${not empty question}">
    <div><ttc:transform document="${question}" property="note:note" /></div>
    
    <c:if test="${not empty question.attachments}">
        <hr>
        
        <h5 class="panel-title">
            <i class="glyphicons glyphicons-paperclip"></i>
            <span><op:translate key="ATTACHMENTS" /></span>
        </h5>
        
        <div class="list-group">
            <c:forEach var="attachment" items="${question.attachments}">
                <a href="${attachment.url}" class="list-group-item">
                    <span>${attachment.name}</span>
                </a>
            </c:forEach>
        </div>
    </c:if>
</c:if>
