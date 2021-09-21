<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<jsp:include page="../common/newDashboardHeader.jsp" />

<div class="d-flex dataTableBottom paddingFixAssign" id="assignmentPage">
	<jsp:include page="../common/newLeftNavbar.jsp" />
	<jsp:include page="../common/newLeftSidebar.jsp" />
	<jsp:include page="../common/rightSidebar.jsp" />

	<div class="container-fluid m-0 p-0 dashboardWraper assignmentW">
		<jsp:include page="../common/newTopHeader.jsp" />

		<div class="container mt-5">
			<div class="row">
				<div
					class="col-lg-9 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto">

					<jsp:include page="../common/alert.jsp" />



					<!-- PAGE CONTENT -->

					<nav aria-label="breadcrumb">
						<ol class="breadcrumb">
							<li class="breadcrumb-item"><a
								href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
							<li class="breadcrumb-item" aria-current="page"><c:out
									value="${Program_Name}" /> <sec:authorize
									access="hasRole('ROLE_STUDENT')">
									<c:out value="${AcadSession}" />
								</sec:authorize></li>
							<li class="breadcrumb-item active" aria-current="page">Electives/Specialized
								Subjects Registration</li>
						</ol>
					</nav>


					<div class="card bg-white border">
						<div class="card-body">

							<h5 class="text-center pb-2 border-bottom">Electives/Specialized
								Subjects Registration</h5>

							<form:form action="submitStudentRegistration"
								id="saveStudentRegistration"
								modelAttribute="selectiveUserCourse" method="post"
								enctype="multipart/form-data">

								<form:input type="hidden" path="eventId" />

								<div class="row">
									<div class="col-sm-12 column">
										<h4>${event.title}</h4>

									</div>
								</div>
								<hr>

								<hr>
								<div class="p-3 well" style="border: solid;">
								<c:if test="${course_name_sel eq null}">
									<form:label for="" path="" style="font-size: 17;">Please select Any One of the Course!${course_id_sel}${selectionList.size()}
											</form:label>
								</c:if>
								<c:if test="${course_name_sel ne null}">
								<form:label for="" path="" style="font-size: 17;">Your Previous Selection: ${course_name_sel}
											</form:label>
								</c:if>
									<c:forEach var="course" items="${coursesList}"
										varStatus="status">


										<div class="radio">
											<form:label path="course_id" for="course_id"
												style="font-weight: 800;">

												<c:if test="${course_id_sel eq null}">
													<form:radiobutton name="course_id"
														id="courseNameFall${status.index}" value="${course.id}"
														path="course_id" class="courseSelect mr-2" />${course.course_name}
																
												</c:if>
												<c:if test="${course_id_sel ne null}">
													<c:choose>
														<c:when test="${course_id_sel eq course.id}">
															<form:radiobutton name="course_id"
																id="courseNameFall${status.index}" value="${course.id}"
																path="course_id" class="courseSelect mr-2" checked="checked" />${course.course_name}
													</c:when>
														<c:otherwise>
															<form:radiobutton name="course_id"
																id="courseNameFall${status.index}" value="${course.id}"
																path="course_id" class="courseSelect mr-2" />${course.course_name}
													</c:otherwise>
													</c:choose>
												</c:if>
											</form:label>
										</div>
									</c:forEach>

								</div>





								<div class="row">
									<div class="col-sm-8 mt-3">
										<div class="form-group">
											<jsp:useBean id="now" class="java.util.Date" />
											<c:if test="${showSubmit}">
												<c:if test="${selectionList.size() eq 0 }">
													<button id="submit" class="btn btn-large btn-primary"
														formaction="submitStudentRegistration">Submit</button>
												</c:if>

												<c:if test="${selectionList.size() ne 0 }">
													<button id="submit" class="btn btn-large btn-primary"
														onclick="return confirm ('Your previous selctions may get deleted!')"
														formaction="submitStudentRegistration">Resubmit</button>
												</c:if>
											</c:if>
											<c:if test="${showSubmit ne true}">
												<button id="submit" class="btn btn-large btn-primary"
													disabled="disabled">Deadline Over!!</button>

											</c:if>

											<button id="cancel" class="btn btn-large btn-danger"
												formaction="homepage" formnovalidate="formnovalidate">Back</button>
										</div>
									</div>
								</div>





							</form:form>
						</div>
					</div>



					<!-- /page content -->







				</div>




				<!-- SIDEBAR START -->
				<jsp:include page="../common/newSidebar.jsp" />
				<!-- SIDEBAR END -->
				<jsp:include page="../common/footer.jsp" />