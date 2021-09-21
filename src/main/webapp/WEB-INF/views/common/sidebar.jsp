<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<link href="<c:url value="/resources/css/style-nice.css" />"
	rel="stylesheet">


<script type="text/javascript">
	function checkCourse(obj) {
		console.log('headerCourseId' + headerCourseId);
		if (headerCourseId) {
			obj.href = obj.href + "?id=" + headerCourseId;
		}
		return true;
	}
</script>

<c:if test="${sessionScope.courseRecord != null}">
	<section:container>
		<%
			String courseId = "";
					if (request.getParameter("courseId") != null) {
						courseId = request.getParameter("courseId");
					}

					pageContext.setAttribute("courseId", courseId);
		%>

		<aside>
			<div id="aside" width="16%">

				<!-- aside menu start-->

				<ul class="aside-menu">
					<sec:authorize access="hasRole('ROLE_STUDENT')">
						<li class="active"><a class=""
							href="${pageContext.request.contextPath}/overview?courseId=${courseId}">
								<img src="resources/images/support.png" height="30%"
								alt="Support"
								style="border-radius: 10px; height: 26px; width: 23px; padding: 1px;">
								<span>Overview</span>
						</a></li>
					</sec:authorize>


					<sec:authorize access="hasRole('ROLE_STUDENT')">


						<li class="sub-menu"><c:if test="${not empty courseId}">
								<a
									href="${pageContext.request.contextPath}/assignmentList?courseId=${courseId}"
									class=""> <img src="resources/images/task.png" height="30%"
									alt="Task"
									style="border-radius: 10px; height: 26px; width: 23px; padding: 1px;">

									<span>Assignments</span> <span
									class="menu-arrow arrow_carrot-right"></span>
								</a>
							</c:if> <c:if test="${empty courseId}">
								<a href="${pageContext.request.contextPath}/assignmentList"
									class=""> <img src="resources/images/task.png" height="30%"
									alt="Task"
									style="border-radius: 10px; height: 26px; width: 23px; padding: 1px;">

									<span>Assignments</span> <span
									class="menu-arrow arrow_carrot-right"></span>
								</a>
							</c:if></li>


					</sec:authorize>

					<sec:authorize access="hasRole('ROLE_STUDENT')">
						<li class="sub-menu"><c:if test="${not empty courseId}">
								<a
									href="${pageContext.request.contextPath}/testList?courseId=${courseId}"
									class=""> <img src="resources/images/task.png" height="30%"
									alt="Task"
									style="border-radius: 10px; height: 26px; width: 23px; padding: 1px;">
									<span>Tests</span> <span class="menu-arrow arrow_carrot-right"></span>
								</a>
							</c:if> <c:if test="${empty courseId}">
								<a href="${pageContext.request.contextPath}/testList" class="">
									<img src="resources/images/task.png" height="30%" alt="Task"
									style="border-radius: 10px; height: 26px; width: 23px; padding: 1px;">

									<span>Tests</span> <span class="menu-arrow arrow_carrot-right"></span>
								</a>
							</c:if></li>
					</sec:authorize>



					<sec:authorize access="hasRole('ROLE_FACULTY')">

						<li class="sub-menu"><a href="javascript:;" class=""> <span>Tests</span>
								<span class="menu-arrow arrow_carrot-right"></span>
						</a>

							<ul class="sub">
								<c:if test="${courseRecord.id eq null}">
									<li><a class=""
										href="${pageContext.request.contextPath}/addTestFromMenu">Create
											Tests</a></li>
								</c:if>
								<c:if test="${courseRecord.id ne null}">
									<li><a class=""
										href="${pageContext.request.contextPath}/addTestForm?courseId=${courseRecord.id}">Create
											Tests</a></li>
								</c:if>
								<li><a class=""
									href="${pageContext.request.contextPath}/testList?courseId=${courseRecord.id}">View
										Tests</a></li>

								<li><a class=""
									href="${pageContext.request.contextPath}/configureQuestions?courseId=${courseRecord.id}">Configure
										Questions</a></li>
							</ul></li>

					</sec:authorize>


					<sec:authorize access="hasRole('ROLE_FACULTY')">
						<li class="sub-menu"><a href="javascript:;" class=""> <span>Groups</span>
								<span class="menu-arrow arrow_carrot-right"></span>
						</a>
							<ul class="sub">
								<li><a class=""
									href="${pageContext.request.contextPath}/createGroupForm?courseId=${courseRecord.id}">Create
										Group</a></li>
								<li><a class=""
									href="${pageContext.request.contextPath}/searchFacultyGroups">View
										Group</a></li>

							</ul></li>
					</sec:authorize>

					<sec:authorize access="hasRole('ROLE_FACULTY')">
						<li class="sub-menu"><c:if
								test="${courseRecord.id ne '' or not empty courseRecord.id}">
								<a href="javascript:;" class=""> <span>Assignments</span> <span
									class="menu-arrow arrow_carrot-right"></span>
								</a>

								<ul class="sub">
									<li><a class=""
										href="${pageContext.request.contextPath}/createAssignmentFromGroup?courseId=${courseRecord.id}">For
											Group</a></li>
									<li><a class=""
										href="${pageContext.request.contextPath}/createAssignmentForm?courseId=${courseRecord.id}">For
											Student</a></li>
									<li><a class=""
										href="${pageContext.request.contextPath}/searchFacultyAssignment?courseId=${courseRecord.id}">View</a></li>
									<li><a class=""
										href="${pageContext.request.contextPath}/searchAssignmentToEvaluate?courseId=${courseRecord.id}">Evaluate</a></li>
									<li><a class=""
										href="${pageContext.request.contextPath}/evaluateByStudentForm?courseId=${courseRecord.id}">Evaluate
											For Student</a></li>
								</ul>
							</c:if> <c:if test="${courseRecord.id eq '' and  empty courseRecord.id}">
								<a href="javascript:;" class=""> <span>Assignments</span> <span
									class="menu-arrow arrow_carrot-right"></span>
								</a>
								<ul class="sub">
									<li><a class=""
										href="${pageContext.request.contextPath}/createAssignmentFromGroup">For
											Group</a></li>
									<li><a class=""
										href="${pageContext.request.contextPath}/createAssignmentFromMenu">For
											Student</a></li>
									<li><a class=""
										href="${pageContext.request.contextPath}/searchFacultyAssignment">View</a></li>
									<li><a class=""
										href="${pageContext.request.contextPath}/searchAssignmentToEvaluate">Evaluate</a></li>
									<li><a class=""
										href="${pageContext.request.contextPath}/evaluateByStudentForm">Evaluate
											For Student</a></li>
								</ul>
							</c:if></li>
					</sec:authorize>







					<sec:authorize access="hasRole('ROLE_STUDENT')">
						<li class="sub-menu"><c:if test="${not empty courseId}">
								<a
									href="${pageContext.request.contextPath}/studentContentList?courseId=${courseId}"
									class=""> <img src="resources/images/library.png"
									height="30%" alt="Library"
									style="border-radius: 10px; height: 26px; width: 23px; padding: 1px;">
									<span>Learning Resources</span> <span
									class="menu-arrow arrow_carrot-right"></span>
								</a>
							</c:if> <c:if test="${empty courseId}">
								<a href="${pageContext.request.contextPath}/studentContentList"
									class=""> <img src="resources/images/library.png"
									height="30%" alt="Library"
									style="border-radius: 10px; height: 26px; width: 23px; padding: 1px;">

									<span>Learning Resources</span> <span
									class="menu-arrow arrow_carrot-right"></span>
								</a>
							</c:if></li>
					</sec:authorize>


					<%-- <sec:authorize access="hasRole('ROLE_STUDENT')">
					<li>
						<!-- 					<a class="" href="">  --> <img
						src="resources/images/support.png" height="30%" alt="Support"
						style="border-radius: 10px; height: 26px; width: 23px; padding: 1px;">
						<span>Discussion Forums</span> <!-- 					</a> -->

					</li>
				</sec:authorize> --%>



					<sec:authorize access="hasRole('ROLE_STUDENT')">

						<li><c:if test="${not empty courseId}">
								<a class=""
									href="${pageContext.request.contextPath}/groupList?courseId=${courseId}">
									<img src="resources/images/support.png" height="30%"
									alt="Support"
									style="border-radius: 10px; height: 26px; width: 23px; padding: 1px;">
									<span>My Groups</span>

								</a>

							</c:if> <c:if test="${empty courseId}">
								<a class="" href="${pageContext.request.contextPath}/groupList">
									<img src="resources/images/support.png" height="30%"
									alt="Support"
									style="border-radius: 10px; height: 26px; width: 23px; padding: 1px;">
									<span>My Groups</span>

								</a>

							</c:if></li>
					</sec:authorize>

					<sec:authorize access="hasRole('ROLE_STUDENT')">
						<li class="sub-menu"><c:if test="${not empty courseId}">
								<a
									href="${pageContext.request.contextPath}/gradeCenter?courseId=${courseId}"
									class=""> <img src="resources/images/support.png"
									height="30%" alt="Support"
									style="border-radius: 10px; height: 26px; width: 23px; padding: 1px;">
									<span>Grade Dashboard</span> <span
									class="menu-arrow arrow_carrot-right"></span>
								</a>
							</c:if> <c:if test="${empty courseId}">
								<a href="${pageContext.request.contextPath}/gradeCenter"
									class=""> <img src="resources/images/support.png"
									height="30%" alt="Support"
									style="border-radius: 10px; height: 26px; width: 23px; padding: 1px;">

									<span>Grade Dashboard</span> <span
									class="menu-arrow arrow_carrot-right"></span>
								</a>
							</c:if></li>
					</sec:authorize>


					<sec:authorize access="hasRole('ROLE_STUDENT')">
						<li><c:if test="${not empty courseId}">
								<a class=""
									href="${pageContext.request.contextPath}/searchStudentFeedback?courseId=${courseId}">
									<img src="resources/images/feedback.png" height="30%"
									alt="Feedback"
									style="border-radius: 10px; height: 26px; width: 23px; padding: 1px;">
									<span>Feedback</span>

								</a>
							</c:if> <c:if test="${empty courseId}">
								<a class=""
									href="${pageContext.request.contextPath}/searchStudentFeedback">
									<img src="resources/images/feedback.png" height="30%"
									alt="Feedback"
									style="border-radius: 10px; height: 26px; width: 23px; padding: 1px;">
									<span>Feedback</span>
								</a>
							</c:if></li>
					</sec:authorize>
				</ul>
			</div>
		</aside>
	</section:container>
</c:if>