<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<jsp:include page="../common/newDashboardHeader.jsp" />


<div class="d-flex dataTableBottom" id="myCoursesPage">
	<jsp:include page="../common/newLeftNavbar.jsp" />
	<jsp:include page="../common/newLeftSidebar.jsp" />
	<jsp:include page="../common/rightSidebar.jsp" />

	<div class="container-fluid m-0 p-0 dashboardWraper assignmentW">
		<jsp:include page="../common/newTopHeader.jsp" />

		<div class="container mt-5">
			<div class="row">
				<div
					class="col-lg-9 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto">


					<div class="bg-white pb-5 mb-5">
						<nav class="" aria-label="breadcrumb">
							<ol class="breadcrumb d-flex">
								<li class="breadcrumb-item"><a
									href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
								<li class="breadcrumb-item active" aria-current="page">Grade
									Dashboard</li>
							</ol>

						</nav>


						<div class="form-row">
							<div class="col-lg-5 col-md-5 col-sm-12 mt-3 mr-auto ml-auto">
								<label class="sr-only">Select Semester</label> <select
									type="text" class="form-control" id="sGradeSem">
									
									<option value="" disabled selected>--SELECT SEMESTER--</option>
									
									<c:forEach items="${ sessionWiseCourseList }" var="sList"
										varStatus="count">
										<c:if test="${sList.key eq acadSessionStudent}">
											<option value="${sList.key}" selected>${sList.key}</option>
										</c:if>
										<c:if test="${sList.key ne acadSessionStudent }">
											<option value="${sList.key}">${sList.key}</option>
										</c:if>
									</c:forEach>
								</select>
							</div>

							<div class="col-lg-5 col-md-5 col-sm-12 mt-3 mr-auto ml-auto">
								<label class="sr-only">Select Course</label> <select
									class="form-control" id="sGradeCourse">
									<c:if test="${empty courseId}">
										<option value="" disabled selected>--SELECT COURSE--</option>
									</c:if>
									<c:forEach var='cList'
										items='${ sessionWiseCourseList[acadSessionStudent] }'>
										<c:if test="${courseId eq cList.id }">
											<option value="${cList.id}" selected>${cList.courseName}</option>
										</c:if>
										<c:if test="${courseId ne cList.id }">
											<option value="${cList.id}">${cList.courseName}</option>
										</c:if>
									</c:forEach>
								</select>
							</div>
						</div>

						<section class="searchAssign  mt-5">
							<div class="col-12 bg-dark text-white subHead1">
								<h6 class="p-2 mb-3">ASSIGNMENTS</h6>
							</div>
							<small class="p-3 mb-3">NA: Not Allocated, NC: Not Completed, NE: Not
								Evaluated, ND : Not Declared, F: FAIL, P: PASS</small>
							<div class="col-12">


								<div class="table-responsive mb-3 testAssignTable">
									<table class="table table-striped table-hover"
										id="viewAssignmentTableStudent">
										<thead>
											<tr>
												<th scope="col">No.</th>
												<th scope="col">Assignment Name</th>
												<th scope="col">Start Date</th>
												<th scope="col">End Date</th>
												<th scope="col">Marks</th>


											</tr>
										</thead>
										<tbody>
											<c:forEach items="${ assignments }" var="assignment"
												varStatus="status">

												<tr>
													<th scope="row"><c:out value="${ status.count }"></c:out></th>
													<td><c:out value="${assignment.assignmentName}" /></td>
													<td><c:out value="${assignment.startDate}" /></td>
													<td><c:out value="${assignment.endDate}" /></td>
													<td><c:out value="${assignment.score}" /></td>





												</tr>
											</c:forEach>
										</tbody>
									</table>
								</div>


							</div>


						</section>

						<section class="searchAssign">
							<div class="col-12 bg-dark text-white subHead1">
								<h6 class="p-2 mb-3">TESTS</h6>
							</div>
							<small class="p-3 mb-3">NA: Not Allocated, NC: Not Completed, NE: Not
								Evaluated, ND : Not Declared, F: FAIL, P: PASS</small>
							<div class="col-12">

								<div class="table-responsive mb-3 testAssignTable">
									<table class="table table-striped table-hover"
										id="viewTestTableStudent">
										<thead>
											<tr>
												<th scope="col">No.</th>
												<th scope="col">Test Name</th>
												<th scope="col">Marks</th>
												<th scope="col">Status</th>


											</tr>
										</thead>
										
										<tbody>
											<c:forEach items="${ tests }" var="test" varStatus="status">


												<tr>
													<th scope="row"><c:out value="${ status.count }"></c:out></th>
													<td><c:out value="${test.testName}" /></td>
													<td><c:out value="${test.stringscore}" /></td>
													<td><c:out value="${test.status}" /></td>

												</tr>
											</c:forEach>
										</tbody>
									</table>
								</div>


							</div>


						</section>
						
						<section class="searchAssign  mt-5">
							<div class="col-12 bg-dark text-white subHead1">
								<h6 class="p-2 mb-3">Class Participation</h6>
							</div>
							<small class="p-3 mb-3">NA: Not Allocated, NC: Not Completed, NE: Not
								Evaluated, ND : Not Declared, F: FAIL, P: PASS</small>
							<div class="col-12">

								<div class="table-responsive mb-3 testAssignTable">
									<table class="table table-striped table-hover"
										id="viewCPTableStudent">
										<thead>
											<tr>
												<th scope="col">No.</th>
												<th scope="col">Name</th>
												<th scope="col">Marks</th>
												<th scope="col">Remarks</th>


											</tr>
										</thead>
										
										<tbody>
											<c:forEach items="${ cpList }" var="cp" varStatus="status">


												<tr>
													<th scope="row"><c:out value="${ status.count }"></c:out></th>
													<td><c:out value="${cp.nameToShow}" /></td>
													<td><c:out value="${cp.score}" /></td>
													<td><c:out value="${cp.remarks}" /></td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</div>


							</div>


						</section>
					</div>


				</div>


				<!-- SIDEBAR START -->
				<jsp:include page="../common/newSidebar.jsp" />
				<!-- SIDEBAR END -->
				<jsp:include page="../common/footer.jsp" />