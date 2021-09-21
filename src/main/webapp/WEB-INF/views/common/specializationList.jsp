<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> 

<jsp:include page="header.jsp" />
<html class="no-js"> <!--<![endif]-->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<body class="inside">

<%-- <%@ include file="header.jsp"%> --%>
	
    <section class="content-container login">
        <div class="container-fluid customTheme">
       <div class="row"> <legend>Specialization List</legend></div>
       <%--  <%@ include file="messages.jsp"%> --%>
		
		<form:form  action="searchSpecialization" method="post" modelAttribute="specialization">
			<fieldset>
			<div class="row clearfix">
			
			
			
			<div class="col-md-6 column">
					
					<div class="form-group">
							<form:input id="abbr" path="abbr" type="text"  placeholder="Prgoram Abbriviation" class="form-control" value="${specialization.abbr}"/>
					</div>

					<div class="form-group">
							<form:input id="name" path="name" type="text" placeholder="Specialization Full Name" class="form-control" value="${specialization.name}"/>
					</div>
					
					<div class="form-group">

						<button id="submit" name="submit" class="btn btn-large btn-primary" formaction="searchSpecialization">Search</button>
						<input  id="reset" type="reset" class="btn btn-danger">
						<button id="cancel" name="cancel" class="btn btn-danger" formaction="viewAllSpecializations" formnovalidate="formnovalidate">Cancel</button>
					</div>
			</div>	
			</div>
			</fieldset>
		</form:form>
		
	</div>
	
	<c:choose>
	<c:when test="${rowCount > 0}">

	<legend>&nbsp;Specializations<font size="2px"> (${rowCount} Records Found) &nbsp; </font></legend>
	<div class="table-responsive">
	<table class="table table-striped table-hover" style="font-size:12px">
						<thead>
						<tr>
							<th>Sr. No.</th>
							<th>id</th>
							<th>Abbriviation</th>
							<th>Full Name</th>
							<th>Actions</th>
						</tr>
					</thead>
						<tbody>
						
						<c:forEach var="specialization" items="${specializationList}" varStatus="status">
					        <tr>
					            <td><c:out value="${status.count}" /></td>
					            <td><c:out value="${specialization.id}" /></td>
					            <td><c:out value="${specialization.abbr}" /></td>
								<td><c:out value="${specialization.name}" /></td>
					            <td> 
						            <c:url value="addSpecializationForm" var="editurl">
									  <c:param name="specializationId" value="${specialization.id}" />
									</c:url>
									<c:url value="deleteSpecialization" var="deleteurl">
									  <c:param name="specializationId" value="${specialization.id}" />
									</c:url>
									<c:url value="commonpecializationDetails" var="detailsUrl">
									  <c:param name="specializationId" value="${specialization.id}" />
									</c:url>
									<c:url value="viewExamCenterSlots" var="SlotsdetailsUrl">
									  <c:param name="centerId" value="${specialization.id}" />
									</c:url>
									
									<a href="${detailsUrl}" title="Details"><i class="fa fa-info-circle fa-lg">Details</i></a>&nbsp;
								
									<a href="${editurl}" title="Edit"><i class="fa fa-pencil-square-o fa-lg">Edit</i></a>&nbsp;
									<a href="${deleteurl}" title="Delete" onclick="return confirm('Are you sure you want to delete this record?')"><i class="fa fa-trash-o fa-lg">Delete</i></a> 
							
									
					            </td>
					        </tr>   
					    </c:forEach>
							
							
						</tbody>
					</table>
	</div>
	<br>

</c:when>
</c:choose>


<c:url var="firstUrl" value="searchSpecialization?pageNo=1&${q}" />
<c:url var="lastUrl" value="searchSpecialization?pageNo=${page.totalPages}&${q}" />
<c:url var="prevUrl" value="searchSpecialization?pageNo=${page.currentIndex - 1}&${q}" />
<c:url var="nextUrl" value="searchSpecialization?pageNo=${page.currentIndex + 1}&${q}" />
 

<c:choose>
<c:when test="${page.totalPages > 1}">
<div align="center">
    <ul class="pagination">
        <c:choose>
            <c:when test="${page.currentIndex == 1}">
                <li class="disabled"><a href="#">&lt;&lt;</a></li>
                <li class="disabled"><a href="#">&lt;</a></li>
            </c:when>
            <c:otherwise>
                <li><a href="${firstUrl}">&lt;&lt;</a></li>
                <li><a href="${prevUrl}">&lt;</a></li>
            </c:otherwise>
        </c:choose>
        <c:forEach var="i" begin="${page.beginIndex}" end="${page.endIndex}">
            <c:url var="pageUrl" value="searchSpecialization?pageNo=${i}&${q}" />
            <c:choose>
                <c:when test="${i == page.currentIndex}">
                    <li class="active"><a href="${pageUrl}"><c:out value="${i}" /></a></li>
                </c:when>
                <c:otherwise>
                    <li><a href="${pageUrl}"><c:out value="${i}" /></a></li>
                </c:otherwise>
            </c:choose>
        </c:forEach>
        <c:choose>
            <c:when test="${page.currentIndex == page.totalPages}">
                <li class="disabled"><a href="#">&gt;</a></li>
                <li class="disabled"><a href="#">&gt;&gt;</a></li>
            </c:when>
            <c:otherwise>
                <li><a href="${nextUrl}">&gt;</a></li>
                <li><a href="${lastUrl}">&gt;&gt;</a></li>
            </c:otherwise>
        </c:choose>
    </ul>
</div>
</c:when>
</c:choose>

 <c:url var="addSpecialization" value="addSpecializationForm" />
<a href="${addSpecialization}">Add Specialization&lt;&lt;</a>
	</section>

	<jsp:include page="footer.jsp" /> 


</body>
</html>