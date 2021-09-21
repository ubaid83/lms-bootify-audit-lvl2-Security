<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<jsp:include page="../common/newDashboardHeader.jsp" />

<div class="d-flex adminPage dataTableBottom" id="adminPage">
	<jsp:include page="../common/newAdminLeftNavBar.jsp" />
	<jsp:include page="../common/rightSidebarAdmin.jsp" />


	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper">

		<jsp:include page="../common/newAdminTopHeader.jsp" />

		<!-- SEMESTER CONTENT -->
		<div class="col-lg-12 col-md-12 col-sm-12 dashboardBody p-5">

			<!-- page content: START -->

			<nav aria-label="breadcrumb">
				<ol class="breadcrumb">
					<li class="breadcrumb-item"><a
						href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
					<li class="breadcrumb-item" aria-current="page"><c:out
							value="${Program_Name}" /></li>
					<sec:authorize access="hasRole('ROLE_STUDENT')">
						<c:out value="${AcadSession}" />
					</sec:authorize>
					<li class="breadcrumb-item active" aria-current="page">My
						Students</li>
				</ol>
			</nav>

			<!-- Input Form Panel -->
			<div class="card bg-white border">
				<div class="card-body">

					<h5 class="text-center border-bottom pb-2">My Program Students
						for ${programName }</h5>


					<form:form action="" method="post" modelAttribute="userCourse"
						enctype="multipart/form-data">


						<form:input path="courseId" type="hidden" />
						<div class="row">
							<div class="col-md-4 col-sm-6 col-xs-12 column">
								<div class="form-group">
									<form:label path="courseId" for="courseId">Course</form:label>
									<form:select id="courseIdForAdmin" path="courseId"
										class="form-control">
										<form:option value="">Select Course</form:option>
										<c:forEach var="course" items="${allCourses}"
											varStatus="status">
											<form:option value="${course.id}" title="${course.courseName}">${course.courseName}(${course.acadYear}-${course.acadSession})</form:option>
										</c:forEach>
									
									</form:select>
								</div>
							</div>
						</div>
					</form:form>

				</div>
			</div>

			<!-- Results Panel -->
			<div class="card bg-white border">
				<div class="card-body">

					<h5 class="text-center border-bottom pb-2">Students |
						${fn:length(students)} Records found</h5>

					<form:form action="" method="post" modelAttribute="userCourse"
						enctype="multipart/form-data">
						<form:input path="courseId" type="hidden" />
						<div class="table-responsive testAssignTable">
							<table class="table table-striped  table-hover" id="example">
								<thead>
									<tr>
										<th>Sr. No.</th>
										<th>Picture</th>


										<th>SAP IDs <i class="fa fa-sort" aria-hidden="true"
											style="cursor: pointer"></i></th>
										<th>Roll No. <i class="fa fa-sort" aria-hidden="true"
											style="cursor: pointer"></i></th>
										<th>Student Name <i class="fa fa-sort" aria-hidden="true"
											style="cursor: pointer"></i></th>
										<th>Email Id <i class="fa fa-sort" aria-hidden="true"
											style="cursor: pointer"></i></th>
										<th>Contact No. <i class="fa fa-sort" aria-hidden="true"
											style="cursor: pointer"></i></th>
										<th>Session <i class="fa fa-sort" aria-hidden="true"
											style="cursor: pointer"></i></th>
									</tr>
								</thead>
								<tfoot>
									<tr>
										<th></th>
										<th>Picture</th>

										<th>SAP IDs</th>
										<th>Roll No.</th>
										<th>Student Name</th>
										<th>Email Id</th>
										<th>Contact No.</th>
										<th>Session</th>
									</tr>
								</tfoot>
								<tbody>

									<c:forEach var="student" items="${students}" varStatus="status">
										<tr>
											<td><c:out value="${status.count}" /></td>
											<td><img
												src="${pageContext.request.contextPath}/savedImages/${student.username}.JPG"
												alt="No image"
												onerror="this.src='<c:url value="/resources/images/download.png" />'"
												style="height: 125px;"></td>

											<td><c:out value="${student.username}" /></td>
											<td><c:out value="${student.rollNo}" /></td>
											<td><c:out
													value="${student.firstname} ${student.lastname}" /></td>
											<td><c:out value="${student.email}" /></td>
											<td><c:out value="${student.mobile}" /></td>
											<td><c:out value="${student.acadSession}" /></td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</form:form>

				</div>
			</div>

			<!-- /page content: END -->



		</div>

		<!-- SIDEBAR START -->

		<!-- SIDEBAR END -->
		<jsp:include page="../common/newAdminFooter.jsp" />
		
		<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/js/select2.min.js"></script>


	<script type="text/javascript">
		$(document).ready(function() {

			// Initialize select2
			$("#courseIdForAdmin").select2();
		});
	</script>






		<script>
			$("#courseIdForAdmin")
					.on(
							'change',
							function() {

								//alert("Onchange Function called!");
								var selectedValue = $(this).val();
								window.location = '${pageContext.request.contextPath}/showMyCourseStudents?courseId='
										+ encodeURIComponent(selectedValue);
								//$("#courseIdForAdmin").select2();
								return false;

							});
		</script>