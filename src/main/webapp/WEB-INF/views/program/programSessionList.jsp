<jsp:include page="../common/header.jsp" />

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%-- <%@ taglib prefix="lmsutils" uri="http://samplefn"%> --%>
	
    <section class="container-fluid">
        <jsp:include page="../common/alert.jsp" />
	
	<c:choose>
	<c:when test="${page.rowCount > 0}">
	<div class="row page-body">
	<fieldset>
	<legend>&nbsp;Map Programs to Sessions <font size="2px"> (${page.rowCount} Programs Found) &nbsp; </font></legend>
	<div class="table-responsive">
		<table class="table table-striped table-hover" style="font-size:12px">
						<thead>
						<tr>
							<th>Sr. No.</th>
							<th>Full Name</th>
							<th>Program Type</th>
							<th>Session</th>
							<th>Action</th>
							
						</tr>
					</thead>
						<tbody>
						
						<c:forEach var="programVar" items="${page.pageItems}" varStatus="status">
						<form:form method="post" action="saveProgramSession" modelAttribute="program">
					        <tr>
					        	 
					        	<%-- <form:input type="hidden" path="programs[${status.index}].id" value="${programVar.id}" /> --%>
					             
					            <td><c:out value="${status.count}" /></td>
					           
								<td><c:out value="${programVar.programName}" /></td>
								<td><c:out value="${programVar.sessionType}" /></td>
								<td><form:input type="hidden" path="id" value="${programVar.id}"/>
								<form:checkboxes items="${sessionList}" path="programSessionArray" data-value="${programVar.programSession}" delimiter="&nbsp;&nbsp;"/>
									  
					            </td>
					            <td><button name="submit" class="btn btn-xs btn-primary">Save</button></td>
					        </tr>   
					     </form:form>  
					    </c:forEach>
							
							
						</tbody>
					</table>
					
	</div>
	<br>
</fieldset>
</div>
</c:when>
</c:choose>
<jsp:include page="../common/paginate.jsp">
        <jsp:param name="baseUrl" value="searchProgramSession"/>
</jsp:include>
</section>

<jsp:include page="../common/footer.jsp" />