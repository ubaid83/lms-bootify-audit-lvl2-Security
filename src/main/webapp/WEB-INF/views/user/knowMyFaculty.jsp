<%@page import="com.spts.lms.web.utils.Utils"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@page import="java.util.*"%>



<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<jsp:include page="../common/newDashboardHeader.jsp" />

<div class="d-flex dataTableBottom" id="facultyAssignmentPage">
	<jsp:include page="../common/newLeftNavbar.jsp" />
	<jsp:include page="../common/newLeftSidebar.jsp" />
	<jsp:include page="../common/rightSidebar.jsp" />

	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper">

		<jsp:include page="../common/newTopHeader.jsp" />

		<div class="container mt-5">
			<div class="row">
				<!-- SEMESTER CONTENT -->
				<div
					class="col-lg-9 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto">

					<!-- page content: START -->

					<nav aria-label="breadcrumb">
						<ol class="breadcrumb">
							<li class="breadcrumb-item"><a
								href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>

							<li class="breadcrumb-item active" aria-current="page">
								Teacher Profile</li>
						</ol>
					</nav>

					<jsp:include page="../common/alert.jsp" />


					<!-- Input Form Panel -->
					<div class="card bg-white border">
						<div class="card-body">
							<div class="form-row">
								<div class="col-lg-5 col-md-5 col-sm-12 mt-3">
									<label class="sr-only">Select Course</label> <select
										class="form-control" id="teacherCourseStudent">
										<c:if test="${empty courseId}">
											<option value="" disabled selected>--SELECT COURSE--</option>
										</c:if>
										<c:forEach var='cList' items='${ courseList }'>
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
						</div>
					</div>

					<!-- Results Panel -->


					<div class="card bg-white border">
						<div class="card-body">
							

									<!-- Input Form Panel -->
									<div class="row">
										<div class="col-xs-12 col-sm-12">
											<div class="x_panel">

												<div class="x_title">
													<h5 class="text-center border-bottom pb-2">Know My Faculty for
														${facultyDetails.course.courseName }</h5>
													<!-- <ul class="nav navbar-right panel_toolbox">
														<li><a class="collapse-link"><i
																class="fa fa-chevron-up"></i></a></li>
													</ul> -->
													<div class="clearfix"></div>
												</div>

												<div class="x_content">
													<form:form action="knowMyFaculty" id="" method="post"
														modelAttribute="facultyDetails"
														enctype="multipart/form-data">
												<c:choose>
													<c:when test="${showDetails}">
														<form:input path="courseId" type="hidden" />
														<form:input path="id" type="hidden" />

														<div class="row">
															<div class="col-sm-12">
																<div class="form-group" id="imagePath">
																	<form:label path="imagePath" for="imagePath"></form:label>
																	<%-- <img src="${imagePath}" alt="image"
															style="height: 220px; width: 220px;"> --%>
																	<img
																		src="${pageContext.request.contextPath}/savedImages/${facultyDetails.username}.JPG"
																		alt="No image"
																		onerror="this.src='<c:url value="/resources/images/download.png" />'"
																		style="height: 220px; width: 220px;">
																</div>
															</div>
															<hr>
															<br>

															<div class="col-md-4 col-sm-6 col-xs-12 column">
																<div class="form-group">
																	<form:label path="firstName" for="firstName"><strong>First Name:</strong></form:label>
																	${facultyDetails.firstName }
																</div>
															</div>
															<div class="col-md-4 col-sm-6 col-xs-12 column">
																<div class="form-group">
																	<form:label path="lastName" for="lastName"><strong>Last Name:</strong></form:label>
																	${facultyDetails.lastName }
																</div>
															</div>
															
														</div>

														<div class="row">
															<div class="col-md-4 col-sm-6 col-xs-12 column">
																<div class="form-group">
																	<form:label path="designation" for="designation"><strong>Designation:</strong></form:label>
																	${facultyDetails.designation}
																</div>
															</div>
															
															<div class="col-md-4 col-sm-6 col-xs-12 column">
																<div class="form-group">
																	<form:label path="experience" for="experience"><strong>Experience:</strong></form:label>
																	${facultyDetails.experience }
																</div>
															</div>
															
														</div>

														
														<br>
													</c:when>
												</c:choose>
												<br/>
												<button id="cancel" class="btn btn-danger"
															formaction="homepage" formnovalidate="formnovalidate">Back</button>
													</form:form>
												</div>
											</div>
										</div>
									</div>
								
						</div>
					</div>



				</div>

				<!-- SIDEBAR START -->
				<jsp:include page="../common/newSidebar.jsp" />
				<!-- SIDEBAR END -->
				<jsp:include page="../common/footer.jsp" />