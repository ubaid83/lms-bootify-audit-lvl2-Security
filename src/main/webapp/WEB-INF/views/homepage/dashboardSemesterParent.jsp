<%@page import="org.springframework.ui.Model"%>
<%@page import="com.sun.mail.handlers.message_rfc822"%>
<%@page import="java.util.*"%>
<%@page import="com.spts.lms.beans.dashboard.DashBoard"%>
<%-- <jsp:include page="../common/DashboardHeader.jsp" /> --%>
<%-- <jsp:include page="../common/NewHeader1.jsp" /> --%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>


<%
	List<DashBoard> courseDetailList = (List<DashBoard>) session
			.getAttribute("courseDetailList");

	Map<String, List<DashBoard>> dashboardListSemesterWise = (Map<String, List<DashBoard>>) session
			.getAttribute("sessionWiseCourseListMap");
	
%>


 <%
	int divCounter = 0;
	for (String s : dashboardListSemesterWise.keySet()) {
		divCounter++;
		int count = 0;
%> 
<input type="hidden" id="mapSize"
	value="<%=dashboardListSemesterWise.size()%>">
<%} %>
<div class="card">
	<div class="col-12 dbCard">
		<div class="card-header">
			<h5 class="mb-0">
				<button class="btn btn-link w-100" data-toggle="collapse"
					data-target="#semChart" aria-expanded="true"
					aria-controls="collapsesemCurrent">
					<h5>
						SUMMARY DATA 
					</h5>
					<i class="fas fa-angle-double-up"></i>
				</button>
			</h5>
		</div>
	</div>
 


	<!-- CHART START -->
			<div id="semChart" class="col-12 chartHolder collapse show">
			<div class="row mt-5 mb-3">
		<div class="col-lg-3 col-md-3 col-sm-12">
		<!-- <p class="mb-0 chartP">Overall Data Of Selected Semester</p> -->
		</div>

		<div class="col-lg-9 col-md-9 col-sm-12">
			<ul class="pagination float-right mb-0">
				<div class="input-group flex-nowrap input-group-sm">
					<div class="input-group-prepend">
						<span class="input-group-text cust-select-span"
							id="addon-wrapping">Semester</span>
					</div>

					<select class="cust-select" id="selectSem">
						<c:forEach  items="${ acadSessLst }"
							var="acadSess" varStatus="count">
							<c:if test="${acadSess eq userBeanParent.acadSession}">
								<option value="${acadSess}" selected>${acadSess}</option>
							</c:if>
							<c:if test="${acadSess ne userBeanParent.acadSession }">
								<option value="${acadSess}">${acadSess}</option>
							</c:if>
						</c:forEach>
					</select>
				</div>
			</ul>
		</div>
	</div>
				<div class="row">
					<div class="col-lg-6 col-md-12 col-sm-12 mt-3">
						<div class="col-12">
							<canvas class="" id="testPieChart"></canvas>
						</div>
					</div>
					<div class="col-lg-6 col-md-12 col-sm-12 mt-3">
						<div class="col-12">
							<canvas id="assignBarChart"></canvas>
						</div>
					</div>
				</div>
			</div>
			<!-- CHART END -->
	</div>		

<div class="card">
	<div class="col-12 dbCard">
		<div class="card-header">
			<h5 class="mb-0">
				<button class="btn btn-link w-100" data-toggle="collapse"
					data-target="#semCurrent" aria-expanded="true"
					aria-controls="collapsesemCurrent">
					<h5>
						DASHBOARD ITEMS
					</h5>
					<i class="fas fa-angle-double-up"></i>
				</button>
			</h5>
		</div>
	</div>
	
      <div id="semCurrent" class="collapse show"
	aria-labelledby="semesterTwo">
		<div class="card-body">
			<div class="row mt-5">

				
				<div class="col-lg-3 col-md-4 col-sm-6 col-6 text-center mb-3">
					<a href="${pageContext.request.contextPath}/reportFormForParents">
						<div class="report">
							<div class="border">
								<div class="row asset1">
									<div class="col-12 mb-3">
										<img
											src="<c:url value="/resources/images/icons/report-icon.png" />" />
									</div>
									<div class="col-12">
										<p>Report</p>
									</div>
								</div>
							</div>
						</div>
					</a>
				</div>
				<div class="col-lg-3 col-md-4 col-sm-6 col-6 text-center mb-3">
					<a href="${pageContext.request.contextPath}/viewDailyAttendanceByStudent">
						<div class="attendance">
							<div class="border">
								<div class="row asset1">
									<div class="col-12 mb-3">
										<img
											src="<c:url value="/resources/images/icons/attendance-icon.png" />" />
									</div>
									<div class="col-12">
										<p>Daily Attendance</p>
									</div>
								</div>
							</div>
						</div>
					</a>
				</div>
				<div class="col-lg-3 col-md-4 col-sm-6 col-6 text-center mb-3">
					<a href="${pageContext.request.contextPath}/gradeCenterFormForParents">
						<div class="gradec">
							<div class="border">
								<div class="row asset1">
									<div class="col-12 mb-3">
										<img
											src="<c:url value="/resources/images/icons/gradecenter-icon.png" />" />
									</div>
									<div class="col-12">
										<p>grade center</p>
									</div>
								</div>
							</div>
						</div>
					</a>
				</div>

			</div>

			<%-- <div class="row mt-3">
				<div class="col-12">
					<h5 class="text-white p-2 mb-3 subHead">Learning</h5>
				</div>
				<div class="col-lg-3 col-md-4 col-sm-6 col-6 text-center mb-3">
					<a href="${pageContext.request.contextPath}/myCourseForm">
						<div class="course">
							<div class="border">
								<div class="row asset1">
									<div class="col-12 mb-3">
										<img
											src="<c:url value="/resources/images/icons/courses-icon.png" />" />
									</div>
									<div class="col-12">
										<p>MY COURSES</p>
									</div>
								</div>
							</div>
						</div>
					</a>
				</div>
				<div class="col-lg-3 col-md-4 col-sm-6 col-6 text-center mb-3">
					<a href="library.html">
						<div class="lib">
							<div class="border">
								<div class="row asset1">
									<div class="col-12 mb-3">
										<img
											src="<c:url value="/resources/images/icons/library-icon.png" />" />
									</div>
									<div class="col-12">

										<p>Library</p>
									</div>
								</div>
							</div>
						</div>
					</a>
				</div>
				<div class="col-lg-3 col-md-4 col-sm-6 col-6 text-center mb-3">
					<a href="learning-resources.html">
						<div class="learn">
							<div class="border">
								<div class="row asset1">
									<div class="col-12 mb-3">
										<img
											src="<c:url value="/resources/images/icons/learning-resources-icon.png" />" />
									</div>
									<div class="col-12">

										<p>Learning Resources</p>
									</div>
								</div>
							</div>
						</div>
					</a>
				</div>
				<div class="col-lg-3 col-md-4 col-sm-6 col-6 text-center mb-3">
					<a href="test-quiz.html">
						<div class="testQuiz">
							<div class="border">
								<div class="row asset1">
									<div class="col-12 mb-3">
										<img
											src="<c:url value="/resources/images/icons/test-icon.png" />" />
									</div>
									<div class="col-12">
										<p>Test/Quiz</p>
									</div>
								</div>
							</div>
						</div>
					</a>
				</div>
				<div class="col-lg-3 col-md-4 col-sm-6 col-6 text-center mb-3">
					<a href="assignment.html">
						<div class="assign">
							<div class="border">
								<div class="row asset1">
									<div class="col-12 mb-3">
										<img
											src="<c:url value="/resources/images/icons/assignment-icon.png" />" />
									</div>
									<div class="col-12">
										<p>Assignment</p>
									</div>
								</div>
							</div>
						</div>
					</a>
				</div>
			</div> --%>

			<%-- <div class="row mt-3">
				<div class="col-12">
					<h5 class="text-white p-2 mb-3 subHead">Social</h5>
				</div>
				<div class="col-lg-3 col-md-4 col-sm-6 col-6 text-center mb-3">
					<a href="message.html">
						<div class="msg">
							<div class="border">
								<div class="row asset1">
									<div class="col-12 mb-3">
										<img
											src="<c:url value="/resources/images/icons/msg-icon.png" />" />
									</div>
									<div class="2 col-12">

										<p>MESSAGES</p>
									</div>
								</div>
							</div>
						</div>
					</a>
				</div>

				<div class="col-lg-3 col-md-4 col-sm-6 col-6 text-center mb-3">
					<a href="forum.html">
						<div class="forum">
							<div class="border">
								<div class="row asset1">
									<div class="col-12 mb-3">
										<img
											src="<c:url value="/resources/images/icons/forum-icon.png" />" />
									</div>
									<div class="col-12">

										<p>FORUM</p>
									</div>
								</div>
							</div>
						</div>
					</a>
				</div>
				<div class="col-lg-3 col-md-4 col-sm-6 col-6 text-center mb-3">
					<a href="group.html">
						<div class="grp">
							<div class="border">
								<div class="row asset1">
									<div class="col-12 mb-3">
										<img
											src="<c:url value="/resources/images/icons/group-icon.png" />" />
									</div>
									<div class="col-12">

										<p>GROUP</p>
									</div>
								</div>
							</div>
						</div>
					</a>
				</div>
			</div> --%>

			<%-- <div class="row mt-3">
				<div class="col-12">
					<h5 class="text-white p-2 mb-3 subHead">Miscellaneous</h5>
				</div>
				<div class="col-lg-3 col-md-4 col-sm-6 col-6 text-center mb-3">
					<a href="feedback.html">
						<div class="feed">
							<div class="border">
								<div class="row asset1">
									<div class="col-12 mb-3">
										<img
											src="<c:url value="/resources/images/icons/feedback-icon.png" />" />
									</div>
									<div class="col-12">

										<p>feedback</p>
									</div>
								</div>
							</div>
						</div>
					</a>
				</div>
				<div class="col-lg-3 col-md-4 col-sm-6 col-6 text-center mb-3">
					<a href="student-attendance.html">
						<div class="attendance">
							<div class="border">
								<div class="row asset1">
									<div class="col-12 mb-3">
										<img
											src="<c:url value="/resources/images/icons/attendance-icon.png" />" />
									</div>
									<div class="col-12">
										<p>Daily Attendance</p>
									</div>
								</div>
							</div>
						</div>
					</a>
				</div>
				<div class="col-lg-3 col-md-4 col-sm-6 col-6 text-center mb-3">
					<a href="grade-weightage.html">
						<div class="gradew">
							<div class="border">
								<div class="row asset1">
									<div class="col-12 mb-3">
										<img
											src="<c:url value="/resources/images/icons/grade-weightage-icon.png" />" />
									</div>
									<div class="col-12">
										<p>grade weightage</p>
									</div>
								</div>
							</div>
						</div>
					</a>
				</div>
				<div class="col-lg-3 col-md-4 col-sm-6 col-6 text-center mb-3">
					<a href="teachers-profile.html">
						<div class="teacher">
							<div class="border">
								<div class="row asset1">
									<div class="col-12 mb-3">
										<img
											src="<c:url value="/resources/images/icons/profile-icon.png" />" />
									</div>
									<div class="col-12">
										<p>teachers&#39; profile</p>
									</div>
								</div>
							</div>
						</div>
					</a>
				</div>
				<div class="col-lg-3 col-md-4 col-sm-6 col-6 text-center mb-3">
					<a href="student-grade-center.html">
						<div class="gradec">
							<div class="border">
								<div class="row asset1">
									<div class="col-12 mb-3">
										<img
											src="<c:url value="/resources/images/icons/gradecenter-icon.png" />" />
									</div>
									<div class="col-12">
										<p>grade center</p>
									</div>
								</div>
							</div>
						</div>
					</a>
				</div>
				<div class="col-lg-3 col-md-4 col-sm-6 col-6 text-center mb-3">
					<a href="class-participation.html">
						<div class="class">
							<div class="border">
								<div class="row asset1">
									<div class="col-12 mb-3">
										<img
											src="<c:url value="/resources/images/icons/class-icon.png" />" />
									</div>
									<div class="col-12">
										<p>class participation</p>
									</div>
								</div>
							</div>
						</div>
					</a>
				</div>

			</div> --%>
		</div>

	</div>

</div>


<%-- <%
	}
%> --%>