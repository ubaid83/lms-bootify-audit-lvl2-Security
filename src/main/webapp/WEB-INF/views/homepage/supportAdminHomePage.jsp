<jsp:include page="../common/topHeaderLibrian.jsp" />
<jsp:include page="../common/css.jsp" />
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<body class="nav-md footer_fixed dashboard_left">

	<!-- Example row of columns -->
	<div class="loader"></div>
	<div class="container body">
		<div class="main_container">


			<!--  -->
			<div class="right_col" role="main">

				<div class="dashboard_container">

					<!-- <div class="dash-main"> -->
					<div class="dashboard_height" id="main">


						<div class="dashboard_contain_specing dash-main">

							<div class="alert alert-success alert-dismissible" role="alert">
								<button type="button" class="close" data-dismiss="alert"
									aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>
								<h4>
									<strong>Welcome to Support Admin Login </strong>

								</h4>
							</div>

							<div class="container-fluid dashboardcon">

								<sec:authorize access="hasAnyRole('ROLE_SUPPORT_ADMIN')">
									
									<c:if test="${userBean.username ne 'minal_SUPPORT_ADMIN' && userBean.username ne 'tulsi_SUPPORT_ADMIN'}">
									<div class="col-md-4 course_widget_height">
										<div class="dashboardcon_title bgcolor2">
											<div>
												<c:url value="viewSchoolsList" var="searchArticlesUrl">
													<c:param name="" value="" />
												</c:url>
											</div>
											<span><a href="${searchArticlesUrl}" />View Portal
												Feedbacks</a></span>
										</div>
									</div>

									<div class="col-md-4 course_widget_height">
										<div class="dashboardcon_title bgcolor3">
											<div>
												<c:url value="addNewAdminSchoolWiseForm" var="addAdminUrl">
													<c:param name="" value="" />
												</c:url>
											</div>
											<span><a href="${addAdminUrl}" />Add new Admin </a></span>
										</div>
									</div>

									<div class="col-md-4 course_widget_height">
										<div class="dashboardcon_title bgcolor4">
											<div>
												<c:url value="addAdminProgramForm" var="addAdminProgramUrl">
													<c:param name="" value="" />
												</c:url>
											</div>
											<span><a href="${addAdminProgramUrl}" />Admin Program
												Mapping</a></span>
										</div>
									</div>
									<div class="col-md-4 course_widget_height">
										<div class="dashboardcon_title bgcolor5">
											<div>
												<c:url value="addAdminMenuRightsForm"
													var="addAdminMenuRightsUrl">
													<c:param name="" value="" />
												</c:url>
											</div>
											<span><a href="${addAdminMenuRightsUrl}" />Admin Menu
												Rights</a></span>
										</div>
									</div>



									<div class="col-md-4 course_widget_height">
										<div class="dashboardcon_title bgcolor6">
											<div>
												<c:url value="enterCourseList" var="enterCourseList">
													<c:param name="" value="" />
												</c:url>
											</div>
											<span><a href="${enterCourseList}">Course List </a></span>
										</div>
									</div>

									<div class="col-md-4 course_widget_height">
										<div class="dashboardcon_title bgcolor7">
											<div>
												<c:url value="viewUserLoginDetails"
													var="viewUserLoginDetails">
													<c:param name="" value="" />
												</c:url>
											</div>
											<span><a href="${viewUserLoginDetails}">View User
													Login Details</a></span>
										</div>
									</div>
									<%-- <div class="col-md-4 course_widget_height">
									<div class="dashboardcon_title bgcolor8">
										<div>
											<c:url value="addOtherUserForm" var="addLibrarianUserUrl">
												<c:param name="" value="" />
											</c:url>
										</div>
										<span><a href="${addLibrarianUserUrl}">View User
												Login Details</a></span>
									</div>
								</div>
 --%>
									<div class="col-md-4 course_widget_height">
										<div class="dashboardcon_title bgcolor8">
											<div>
												<c:url value="addOtherUserForm" var="addLibrarianUserUrl">
													<c:param name="" value="" />
												</c:url>
											</div>
											<span><a href="${addLibrarianUserUrl}">Other User
													Login Creation </a></span>
										</div>
									</div>

									<div class="col-md-4 course_widget_height">
										<div class="dashboardcon_title bgcolor9">
											<div>
												<c:url value="createBFServiceSupportForm"
													var="createBFServiceSupportUrl">
													<c:param name="" value="" />
												</c:url>
											</div>
											<span><a href="${createBFServiceSupportUrl}">Create
													Student Service</a></span>
										</div>
									</div>

									<div class="col-md-4 course_widget_height">
										<div class="dashboardcon_title bgcolor11">
											<div>
												<c:url value="addNewFeatures" var="addNewFeaturesUrl">
													<c:param name="" value="" />
												</c:url>
											</div>
											<span><a href="${addNewFeaturesUrl}">PUBLISH
													FEATURE NOTIFICATION </a></span>
										</div>
									</div>


									<div class="col-md-4 course_widget_height">
										<div class="dashboardcon_title bgcolor11">
											<div>
												<c:url value="createTraingProgramForm"
													var="createTrainingProgram">
													<c:param name="" value="" />
												</c:url>
											</div>
											<span><a href="${createTrainingProgram}">CREATE
													TRAINING </a></span>
										</div>
									</div>

									<div class="col-md-4 course_widget_height">
										<div class="dashboardcon_title bgcolor2">
											<div>
												<c:url value="AddIcaComponentNew" var="AddIcaComponent">
													<c:param name="" value="" />
												</c:url>
											</div>
											<span><a href="${AddIcaComponent}">Add Ica
													Component</a></span>
										</div>
									</div>

									<%-- <div class="col-md-4 course_widget_height">
										<div class="dashboardcon_title bgcolor12">
											<div>
												<c:url value="changePassword" var="changePassword">
													<c:param name="" value="" />
												</c:url>
											</div>
											<span><a href="changePasswordBySupportAdminForm">Change
													Password </a></span>
										</div>
									</div> --%>

									<div class="col-md-4 course_widget_height">
										<div class="dashboardcon_title bgcolor1">
											<div>
												<c:url value="announcement" var="announcement">
													<c:param name="" value="" />
												</c:url>
											</div>
											<span><a href="createAnnouncementBySupportAdminForm">Create
													Announcement for Server Down </a></span>
										</div>
									</div>

									<div class="col-md-4 course_widget_height">
										<div class="dashboardcon_title bgcolor10">
											<div>
												<c:url value="announcement" var="announcement">
													<c:param name="" value="" />
												</c:url>
											</div>
											<span><a href="icaListBySupportAdmin">Change ICA
													Date </a></span>
										</div>
									</div>
									<div class="col-md-4 course_widget_height">
										<div class="dashboardcon_title bgcolor4">
											<div>
												<c:url value="test" var="test">
													<c:param name="" value="" />
												</c:url>
											</div>
											<span><a href="searchTestListBySupportAdminForm">Check
													Test </a></span>
										</div>
									</div>

									<div class="col-md-4 course_widget_height">
										<div class="dashboardcon_title bgcolor11">
											<div>
												<c:url value="copyLeaksStudentForm" var="copyLeaks">
													<c:param name="" value="" />
												</c:url>
											</div>
											<span><a href="${copyLeaks}">Copy Leaks Assignment
											</a></span>
										</div>
									</div>
									
									
										<!-- Student Master  -->
										<div class="col-md-4 course_widget_height">
										<div class="dashboardcon_title bgcolor3">
											<div>
												<c:url value="StudentMasterSupport" var="StudentMasterSupportAdmin">
													<c:param name="" value="" />
												</c:url>
											</div>
											<span><a href="StudentMasterSupportAdmin">Student Master</a></span>
										</div>
									</div> 
									
								<div class="col-md-4 course_widget_height">
										<div class="dashboardcon_title bgcolor10">
											<div>
												<c:url value="announcement" var="announcement">
													<c:param name="" value="" />
												</c:url>
											</div>
											<span><a href="teeListBySupportAdmin">Change TEE
													Date </a></span>
										</div>
									</div>


									<div class="col-md-4 course_widget_height">
										<div class="dashboardcon_title bgcolor10">
											<div>
												<c:url value="gotoupdateFeedbackQuestion" var="gotoupdateFeedbackQuestion">
													<c:param name="" value="" />
												</c:url>
											</div>
											<span><a href="gotoupdateFeedbackQuestion">Update Feedback Questions</a></span>
										</div>
									</div>
									
									<div class="col-md-4 course_widget_height">
										<div class="dashboardcon_title bgcolor1">
											<div>
												<c:url value="SerchdeleteUserCourseEnrollmentSupport" var="searchuserCourseEnrollmentDelete">
													<c:param name="" value="" />
												</c:url>
											</div>
											<span><a href="${searchuserCourseEnrollmentDelete}">Delete  User Course Enrolment</a></span>
										</div>
								</div>
								
								<div class="col-md-4 course_widget_height">
										<div class="dashboardcon_title bgcolor1">
											<div>
												<c:url value="hostleStudentDetails" var="hostleStudentDetails">
													<c:param name="" value="" />
												</c:url>
											</div>
											<span><a href="hostleStudentDetailsBySupportAdminForm">Hostle Student Details</a></span>
										</div>
								</div>
								</c:if>
								
								<div class="col-md-4 course_widget_height">
									<div class="dashboardcon_title bgcolor12">
										<div>
											<c:url value="changePassword" var="changePassword">
												<c:param name="" value="" />
											</c:url>
										</div>
										<span><a href="changePasswordBySupportAdminForm">Change
												Password </a></span>
									</div>
								</div>
								<div class="col-md-4 course_widget_height">
									<div class="dashboardcon_title bgcolor17">
										<div>
											<c:url value="AddLorUser" var="AddLorUser">
												<c:param name="" value="" />
											</c:url>
										</div>
										<span><a href="AddLorUser">
												 Add Lor Users</a></span>
									</div>
								</div>
								<!-- Peter 07/07/2021 START -->
								<div class="col-md-4 course_widget_height">
									<div class="dashboardcon_title bgcolor7">
										<div>
											<c:url value="lmsVariablesListBySupportAdmin" var="lmsVariablesListBySupportAdmin">
												<c:param name="" value="" />
											</c:url>
										</div>
										<span><a href="lmsVariablesListBySupportAdmin">
												 Change LMS Variables</a></span>
									</div>
								</div>
								<!-- Peter END -->
								<c:if test="${userBean.username eq 'minal_SUPPORT_ADMIN' || userBean.username eq 'tulsi_SUPPORT_ADMIN'}">
								
									<div class="col-md-4 course_widget_height">
									<div class="dashboardcon_title bgcolor11">
										<div>
											<c:url value="getAssignmentStatus" var="assignmentStatus">
												<c:param name="" value="" />
											</c:url>
										</div>
										<span><a href="${assignmentStatus}">Get Assignment Submission Count
										</a></span>
									</div>
								</div>
								
								</c:if>

								</sec:authorize>
								<sec:authorize access="hasAnyRole('ROLE_SUPPORT_ADMIN_REPORT')">
									<div class="col-md-4 course_widget_height">
										<div class="dashboardcon_title bgcolor8">
											<div>
												<c:url value="downloadAttendence_Report"
													var="downloadAttendenceReport">
													<c:param name="" value="" />
												</c:url>
											</div>
											<span><a href="${downloadAttendenceReport}">Download
													Attendance Report</a></span>
										</div>
									</div>
								</sec:authorize>


								<!--end dashboard-->




							</div>
						</div>
					</div>
				</div>


			</div>
		</div>
	</div>
</body>

<jsp:include page="../common/footerLibrarian.jsp" />
<%-- <jsp:include page="../common/footer.jsp" /> --%>

<script>
	$(document).ready(
			function() {
				var cars = [ "bgcolor1", "bgcolor2", "bgcolor3", "bgcolor4",
						"bgcolor5", "bgcolor6", "bgcolor7", "bgcolor8",
						"bgcolor9", "bgcolor10", "bgcolor11" ];
				var count = 0;
				$('[id^=courseDetail]').each(function() {
					if (count == cars.length - 1) {
						count = 0;
					}
					$(this).addClass(cars[count]);
					count++;
				})

				$('body').addClass("dashboard_left");
			});
</script>


