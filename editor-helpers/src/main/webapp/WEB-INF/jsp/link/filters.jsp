<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>


<portlet:defineObjects />

<c:set var="namespace"><portlet:namespace /></c:set>


<form>
    <!-- Document type -->
    <div class="form-group">
        <label for="${namespace}-filter-type" class="control-label small"><op:translate key="EDITOR_FILTERS_DOCUMENT_TYPE" /></label>
        <select id="${namespace}-filter-type" name="filterType" class="form-control input-sm select2">
            <c:forEach var="type" items="${filterTypes}">
                <option value="${type.name}" data-icon="${type.icon}"><op:translate key="${type.key}" /></option>
            </c:forEach>
        </select>
    </div>
    
    
    
    <div>
        <button type="button" class="btn btn-sm btn-primary">
            <span><op:translate key="EDITOR_FILTERS_SEARCH" /></span>
        </button>
        
        <button type="button" class="btn btn-sm btn-link">
            <span><op:translate key="EDITOR_FILTERS_RESET" /></span>
        </button>
    </div>
</form>
