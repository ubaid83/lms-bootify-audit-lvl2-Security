<jsp:include page="../common/header.jsp" />
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<section class="container">
	<jsp:include page="../common/alert.jsp" />
	<div class="row page-body">
		<div class="col-sm-12 column">
			<form:form action="addCalenderEvent" method="post" modelAttribute="calender">
				<fieldset>
					<legend>Event Details</legend>
					<div class="row">
						<div class="col-sm-4 column">
							<div class="form-group">
								<form:label path="courseName" for="courseName">Course :</form:label>
								${courseName}
							</div>
						</div>
						
						
						<div class="col-sm-4 column">
							<form:input type="hidden" path="courseId" />
							<div class="form-group">
								<form:label path="event" for="event">Event Name :</form:label>
								${calender.event}
							</div>
						</div>
						<div class="col-sm-4 column">
							<div class="form-group">
								<form:label path="startDate" for="startDate">Start Date :</form:label>
								<c:out
									value="${fn:replace(calender.startDate, 
                                'T', ' ')}"></c:out>
							</div>
						</div>
						<%-- <div class="col-sm-4 column">
							<div class="form-group">
								<form:label path="dueDate" for="dueDate">End Date :</form:label>
								<c:out value="${fn:replace(calender.endDate,'T', ' ')}"></c:out>
							</div>
						</div> --%>
						<sec:authorize access="hasRole('ROLE_FACULTY')">
							<div class="col-sm-4 column">
								<div class="form-group">
									<form:label path="endDate" for="endDate">End Date :</form:label>
									<c:out value="${fn:replace(calender.endDate,'T', ' ')}"></c:out>
								</div>
							</div>
							</sec:authorize>
					</div>
					<div class="row">

						<div class="col-sm-12 column">
							<div class="form-group">
								<sec:authorize access="hasRole('ROLE_FACULTY')">
									<c:url value="addCalenderEventForm" var="editEventUrl">
										<c:param name="id">${calender.id}</c:param>
										
									</c:url>
									

									<button id="submit" class="btn btn-large btn-primary"
										formaction="${editEventUrl}">Edit</button>
									<button id="cancel" class="btn btn-danger"
										formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
									
								</sec:authorize>
								<%-- <sec:authorize access="hasRole('ROLE_STUDENT')">
									<c:url value="startStudentTest" var="takeTestUrl">
										<c:param name="id" value="${test.id}" />
									</c:url>
								
									<a href="${takeTestUrl}" title="Start Test"
										onclick="return confirm('Are you ready to take the test?')"><i
										class="fa fa-pencil-square-o fa-lg"></i></a>
									
								
								</sec:authorize> --%>
							</div>
						</div>

					</div>


				</fieldset>
			</form:form>
		</div>
	</div>

	<!-- Section to allocate this test to Students -->
	<sec:authorize access="hasRole('ROLE_FACULTY')">
		<div class="row page-body">
			<div class="col-sm-12 column">
				<form:form action="saveStudentEvents" id="saveStudentEvents"
					method="post" modelAttribute="calender">
					<fieldset>
						<legend>Calender Event Allocation Details</legend>
						<form:input path="id" type="hidden" />
						
						<form:input path="courseId" type="hidden" value="${calender.courseId}" />
						<div class="table-responsive">
							<table class="table table-striped table-hover"
								style="font-size: 12px">
								<thead>
									<tr>
										<th>Sr. No.</th>
										<th>Select To Allocate</th>
										<th>Email</th>
										<th>Student Name</th>
									</tr>
								</thead>
								<tfoot>
									<tr>
										<th></th>
										<th></th>
										<th>Email</th>
										<th>Student Name</th>
									</tr>
								</tfoot>
								<tbody>

									<c:forEach var="student" items="${studentsForEvents}" varStatus="status">
										<tr>
											<td><c:out value="${status.count}" /></td>
											<td><c:if test="${empty student.id }">
													<form:checkbox path="students" value="${student.username}" />
												</c:if> <c:if test="${not empty student.id }">
						            	Test Allocated
						            </c:if></td>
											<td><c:out value="${student.email}" /></td>
											<td><c:out
													value="${student.firstname} ${student.lastname}" /></td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>

						<div class="col-sm-4 column">
							<div class="form-group">

								<button id="submit" class="btn btn-large btn-primary"
									formaction="saveStudentEvents">Allocate Event</button>
								<button id="cancel" class="btn btn-danger" formaction="homepage"
									formnovalidate="formnovalidate">Cancel</button>
							</div>
						</div>

					</fieldset>
				</form:form>
			</div>
		</div>
	</sec:authorize>
</section>
<jsp:include page="../common/footer.jsp" />