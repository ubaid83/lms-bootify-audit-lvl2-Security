<jsp:include page="../common/header.jsp" />
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<section class="container">
<jsp:include page="../common/alert.jsp" />

<%
	boolean isEdit = "true".equals((String) request.getAttribute("edit"));
%>


	<div class="row page-body">
		<div class="col-sm-12 column">
			<form:form action="addInstituteCycle" id="addInstituteCycle" method="post" modelAttribute="instituteCycle">
				<fieldset>
					<legend>Add/Update Academic Cycle</legend>
					<form:input type="hidden" path="cycleType"/>
					<% if(isEdit){ %>
							<form:input type="hidden" path="id"/>
					<% } %>
							
					<div class="row">
						<div class="col-sm-4 column">
							<div class="form-group">
								<form:label path="month" for="month">${instituteCycle.cycleType } Month <span style="color: red">*</span></form:label>
								<form:select id="month" path="month"
									class="form-control" required="required">
									<form:option value="">Select Academic Month</form:option>
									<form:options items="${acadMonths}" />
								</form:select>
							</div>
						</div>
						<div class="col-sm-4 column">
							<div class="form-group">
								<form:label path="year" for="year">${instituteCycle.cycleType } Year <span style="color: red">*</span></form:label>
								<form:select id="year" path="year"
									class="form-control" required="required">
									<form:option value="">Select Academic Year</form:option>
									<form:options items="${acadYears}" />
								</form:select>
							</div>
						</div>
						<div class="col-sm-4 column">
							<div class="form-group">
								<form:label path="live" for="live" required="required">Make Live <span style="color: red">*</span></form:label>
								<form:select id="live" path="live"
									class="form-control" required="required">
									<form:option value="">Select Choice</form:option>
									<form:option value="Y">Make Live</form:option>
									<form:option value="N">Not Yet</form:option>
								</form:select>
							</div>
						</div>
					</div>
					
					<hr>
					<div class="row">
						
						<div class="col-sm-8 column">
							<div class="form-group">

								<%
									if (isEdit) {
								%>
								<button id="submit"	class="btn btn-large btn-primary" formaction="updateInstituteCycle">Update Academic Cycle</button>
								<%
									}else{
								%>
								<button id="submit"	class="btn btn-large btn-primary" formaction="addInstituteCycle">Create Academic Cycle</button>
								<%
									}
								%>
								<button id="cancel" class="btn btn-danger" formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
							</div>
						</div>
					</div>
					
				</fieldset>
			</form:form>
		</div>
	</div>
	
	<c:if test="${noOfCycles > 0}">
		<div class="row page-body">
	<fieldset>
	<legend>&nbsp;All Institute Cycles <font size="2px"> (${page.rowCount} Records Found) &nbsp; </font></legend>
	<div class="table-responsive">
	<table class="table table-striped table-hover" style="font-size:12px">
						<thead>
						<tr>
							<th>Sr. No.</th>
							<th>Cycle Type</th>
							<th>Month</th>
							<th>Year</th>
							<th>Is Currently Live?</th>
							<th>Actions</th>
						</tr>
					</thead>
						<tbody>
						
						<c:forEach var="cycle" items="${allCycles}" varStatus="status">
					        <tr>
					            <td><c:out value="${status.count}" /></td>
					            <td><c:out value="${cycle.cycleType}" /></td>
					            <td><c:out value="${cycle.month}" /></td>
								<td><c:out value="${cycle.year}" /></td>
								<td><c:out value="${cycle.live}" /></td>
					            <td> 
						            <c:url value="/addInstituteCycleForm" var="editurl">
									  <c:param name="id" value="${cycle.id}" />
									</c:url>
									<c:url value="deleteInstituteCycleForm" var="deleteurl">
									  <c:param name="id" value="${cycle.id}" />
									</c:url>
									
									<a href="${editurl}" title="Edit"><i class="fa fa-pencil-square-o fa-lg"></i></a>&nbsp;
									<a href="${deleteurl}" title="Delete" onclick="return confirm('Are you sure you want to delete this record?')"><i class="fa fa-trash-o fa-lg"></i></a> 
							
									
					            </td>
					        </tr>   
					    </c:forEach>
							
							
						</tbody>
					</table>
	</div>
	<br>
	</fieldset>
	</div>
	</c:if>
</section>
<jsp:include page="../common/footer.jsp" />
