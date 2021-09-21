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
	System.out.println("SIZE :"+courseDetailList.size());
	Map<String,List<DashBoard>> dashboardListSemesterWise = (Map<String,List<DashBoard>>)
	session.getAttribute("sessionWiseCourseListMap");
%>

<jsp:include page="../common/newDashboardHeader.jsp" />

<script type="text/javascript">
	var headerCourseId = null;
	function theFunction(obj) {

		return true;
	}
</script>
<div class="d-flex" id="facultyDashboardPage">
	<jsp:include page="../common/newLeftNavBarFaculty.jsp" />
	<jsp:include page="../common/newLeftSidebarFaculty.jsp" />
	<jsp:include page="../common/rightSidebarFaculty.jsp" />

	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper">

		<jsp:include page="../common/newTopHeaderFaculty.jsp" />

		<div class="container mt-5">
			<div class="row">
				<!-- SEMESTER CONTENT -->
				<div
					class="col-lg-9 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto">
					<div class="card">
						<div class="card-body">

                                          <div class="bg-white pb-5">
                                                <nav class="" aria-label="breadcrumb">
                                                      <ol class="breadcrumb d-flex">
                                                            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
                                                            <li class="breadcrumb-item active" aria-current="page">My
                                                                  Courses</li>

                                                            <li class="ml-auto"><select class="form-control"
                                                                  id="semDashboardFaculty">
                                                                  <option value="" selected disabled>Select Semester</option>
                                                                        <c:forEach items="${ sessionWiseCourseListMap }" var="sList"
                                                                              varStatus="count">
                                                                              
                                                                              <c:if test="${sList.key eq userBean.acadSession}">
                                                                                    <option value="${sList.key}" selected>${sList.key}</option>
                                                                              </c:if>
                                                                              <c:if test="${sList.key ne userBean.acadSession }">
                                                                                    <option value="${sList.key}">${sList.key}</option>
                                                                              </c:if>
                                                                        </c:forEach>
                                                            </select></li>
                                                      </ol>

                                                </nav>




                                                <div class="col-12">
                                                      <div class="row text-center" id="courseListSemWisefaculty">
                                                            
                                                            <c:forEach var='cList'
                                                                  items='${ sessionWiseCourseList[userBean.acadSession] }'>
                                                                  <div class="col-lg-3 col-md-3 col-sm-6 col-6 mt-5">
                                                                        <div
                                                                              class="courseAsset hoverDiv d-flex align-items-start flex-column">
                                                                              <h6 class="text-uppercase mb-auto">
                                                                                    <c:out value="${ cList.courseName }" />
                                                                              </h6>
                                                                              <span class="courseNav"> <a
                                                                                    href="${pageContext.request.contextPath}/createTestForm?courseId=${cList.id}">
                                                                                          <p class="caBg">Create Test</p>
                                                                              </a> <a
                                                                                    href="${pageContext.request.contextPath}/createTestForm?courseId=${cList.id}">
                                                                                          <p class="ctBg">View Test</p>
                                                                              </a>
                                                                              </span>
                                                                        </div>
                                                                  </div>
                                                            </c:forEach>
                                                            
                                                            
                                                            
                                                            

                                                      </div>
                                                </div>
                                          </div>






                                          <div class="row">
                                                <div class="col-12">
                                                      <h5 class="text-white p-2 mb-3 subHead">Learning</h5>
                                                </div>
                                                <div class="col-lg-3 col-md-4 col-sm-6 col-6 text-center mb-3">
                                                      <a
                                                            href="${pageContext.request.contextPath}/facultyTestDashboard">
                                                            <div class="testQuiz">
                                                                  <div class="border hoverDiv">
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
                                                      <a
                                                            href="${pageContext.request.contextPath}/facultyAssignmentDashboard">
                                                            <div class="assign">
                                                                  <div class="border hoverDiv">
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
                                                <%-- <div class="col-lg-3 col-md-4 col-sm-6 col-6 text-center mb-3">
                                                      <a href="my-courses.html">
                                                            <div class="course">
                                                                  <div class="border hoverDiv">
                                                                        <div class="row asset1">
                                                                              <div class="col-12 mb-3">
                                                                                    <img
                                                                                          src="<c:url value="/resources/images/icons/courses-icon.png" />" />
                                                                              </div>
                                                                              <div class="col-12">
                                                                                    <p>STUDENT'S COURSES</p>
                                                                              </div>
                                                                        </div>
                                                                  </div>
                                                            </div>
                                                      </a>
                                                </div> --%>
                                                <div class="col-lg-3 col-md-4 col-sm-6 col-6 text-center mb-3">
                                                      <a href="${pageContext.request.contextPath}/viewLibraryAnnouncements">
                                                            <div class="lib">
                                                                  <div class="border hoverDiv">
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
<%--                                                 <div class="col-lg-3 col-md-4 col-sm-6 col-6 text-center mb-3">
                                                      <a href="${pageContext.request.contextPath}/getContentUnderAPathForFaculty?courseId=${courseId}">
                                                            <div class="learn">
                                                                  <div class="border hoverDiv">
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
                                                </div> --%>
                                                
                                              <div class="col-lg-3 col-md-4 col-sm-6 col-6 text-center mb-3">
                                                      <a href="${pageContext.request.contextPath}/getContentUnderAPathForFacultyForModule">
                                                            <div class="learn">
                                                                  <div class="border hoverDiv">
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
                                                
                                          </div>

                                          <div class="row mt-3">
                                                <div class="col-12">
                                                      <h5 class="text-white p-2 mb-3 subHead">Social</h5>
                                                </div>
                                                <div class="col-lg-3 col-md-4 col-sm-6 col-6 text-center mb-3">
                                                      <a href="<c:url value="viewMyMessage" />">
                                                            <div class="msg">
                                                                  <div class="border hoverDiv">
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
                                                      <a href="<c:url value="viewForum" />">
                                                            <div class="forum">
                                                                  <div class="border hoverDiv">
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
                                                <c:if test="${lorStaff}">
                                                <div class="col-lg-3 col-md-4 col-sm-6 col-6 text-center mb-3">
                                                     <a href="<c:url value="viewAppliedApplicationStudentsForStaff" />">
                                                           <div class="forum">
                                                                 <div class="border hoverDiv">
                                                                       <div class="row asset1">
                                                                             <div class="col-12 mb-3">
                                                                                   <img
                                                                                         src="<c:url value="/resources/images/icons/lor-application-icon.png" />" />
                                                                             </div>
                                                                             <div class="col-12">

                                                                                   <p>LOR Applications</p>
                                                                             </div>
                                                                       </div>
                                                                 </div>
                                                           </div>
                                                     </a>
                                               </div>
                                               </c:if>
                                                <%--     <div class="col-lg-3 col-md-4 col-sm-6 col-6 text-center mb-3">
                                            <a href="group.html">
                                                <div class="grp">
                                                    <div class="border hoverDiv">
                                                        <div class="row asset1">
                                                            <div class="col-12 mb-3">
                                                                <img src="<c:url value="/resources/images/icons/group-icon.png" />" />
                                                            </div>
                                                            <div class="col-12">

                                                                <p>GROUPS</p>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </a>
                                        </div> --%>
                                          </div>

                                          <div class="row mt-3">
                                                <div class="col-12">
                                                      <h5 class="text-white p-2 mb-3 subHead">Miscellaneous</h5>
                                                </div>
                                                <%-- <div class="col-lg-3 col-md-4 col-sm-6 col-6 text-center mb-3">
                                                      <a href="student-attendance.html">
                                                            <div class="attendance">
                                                                  <div class="border hoverDiv">
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
                                                </div> --%>
                                                <%-- <div class="col-lg-3 col-md-4 col-sm-6 col-6 text-center mb-3">
                                                      <a href="grade-weightage.html">
                                                            <div class="gradew">
                                                                  <div class="border hoverDiv">
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
                                                </div> --%>
                                                <div class="col-lg-3 col-md-4 col-sm-6 col-6 text-center mb-3">
                                                      <a href="${pageContext.request.contextPath}/profileDetails">
                                                            <div class="teacher">
                                                                  <div class="border hoverDiv">
                                                                        <div class="row asset1">
                                                                              <div class="col-12 mb-3">
                                                                                    <img
                                                                                          src="<c:url value="/resources/images/icons/teacher-icon.png" />" />
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
                                                      <a href="${pageContext.request.contextPath}/gradeCenter?courseId=${courseId}">
                                                            <div class="gradec">
                                                                  <div class="border hoverDiv">
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
                                                      <a href="${pageContext.request.contextPath}/markAttendanceForm">
                                                            <div class="report">
                                                                  <div class="border hoverDiv">
                                                                        <div class="row asset1">
                                                                              <div class="col-12 mb-3">
                                                                                    <img
                                                                                          src="<c:url value="/resources/images/icons/attendance-icon.png" />" />
                                                                              </div>
                                                                              <div class="col-12">
                                                                                    <p>Mark Attendance</p>
                                                                              </div>
                                                                        </div>
                                                                  </div>
                                                            </div>
                                                      </a>
                                                </div>
                                                
                                                <div class="col-lg-3 col-md-4 col-sm-6 col-6 text-center mb-3">
                                                      <a href="${pageContext.request.contextPath}/viewAttendance">
                                                            <div class="report">
                                                                  <div class="border hoverDiv">
                                                                        <div class="row asset1">
                                                                              <div class="col-12 mb-3">
                                                                                    <img
                                                                                          src="<c:url value="/resources/images/icons/view-attendance-icon-2.jpg" />" />
                                                                              </div>
                                                                              <div class="col-12">
                                                                                    <p>View Attendance</p>
                                                                              </div>
                                                                        </div>
                                                                  </div>
                                                            </div>
                                                      </a>
                                                </div>
                                                <div class="col-lg-3 col-md-4 col-sm-6 col-6 text-center mb-3">
                                                      <a href="${pageContext.request.contextPath}/createReportsForm?courseId=${courseId}">
                                                            <div class="report">
                                                                  <div class="border hoverDiv">
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
                                                <%-- <div class="col-lg-3 col-md-4 col-sm-6 col-6 text-center mb-3">
                                            <a href="class-participation.html">
                                                <div class="class">
                                                    <div class="border hoverDiv">
                                                        <div class="row asset1">
                                                            <div class="col-12 mb-3">
                                                                <img src="<c:url value="/resources/images/icons/class-icon.png" />" />
                                                            </div>
                                                            <div class="col-12">
                                                                <p>class participation</p>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </a>
                                        </div> --%>

                                          </div>
                                    </div>
					</div>
					<!-- <a href="#">
                        <div class="alert alert-blue alert-dismissible fade show" role="alert">
                            <strong>Welcome <span id="feedUser">Kapil!</span></strong> Click me to share your feedback about the new interface?
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                    </a>  -->

					<%-- <jsp:include page="../common/dashboardSemester.jsp"></jsp:include> --%>
				</div>

				<!-- SIDEBAR START -->
				<jsp:include page="../common/newSidebar.jsp" />
				<!-- SIDEBAR END -->
				<jsp:include page="../common/footer.jsp" />







				<%-- 	<div class="loader"></div>


	<div class="container body">
		<div class="main_container">


			<jsp:include page="../common/leftSidebar.jsp" />
			<jsp:include page="../common/topHeader.jsp" />

			<!--  -->
			<div class="right_col" role="main">

				<div class="dashboard_container">

					<!-- <div class="dash-main"> -->
					<div class="dashboard_height" id="main">

						<!-- <div class="right-arrow"><img class="toggle_to_do" src="" alt="" onclick="openNav2()"></div> -->
						<div class="right-arrow">
							<img class="toggle_to_do"
								src="<c:url value="/resources/images/dash-right.gif" />" alt=""
								onclick="openNav2()">
						</div>

						<div class="dashboard_contain_specing dash-main">

							

							<div class="alert alert-warning alert-dismissible" role="alert">
								<button type="button" class="close" data-dismiss="alert"
									aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>
								<h4>
								
								
								
									<strong>Please provide your feedback about our new
										portal <a href="<c:url value="portalFeedbackForm" />">here...
									</a>
									</strong>
								</h4>
							</div>
							
							
							<div class="alert alert-success alert-dismissible" role="alert">
								<button type="button" class="close" data-dismiss="alert"
									aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>
								<h4>
								
								
							 <b> <c:out value="${subjectFaculty}"></c:out></b>
									 <marquee behavior="scroll" direction="left" onmouseover="this.stop();" onmouseout="this.start();">${notificationFaculty}</marquee>
								
									<strong></strong>
								</h4>
							</div>



	
							<%
								int divCounter=0;
																																					for(String s : dashboardListSemesterWise.keySet()){
																																						divCounter++;
																																						int count = 0;
							%>
							<input type="hidden" id="mapSize"
								value="<%=dashboardListSemesterWise.size()%>"> <a>

								<div id="flip<%=divCounter%>" class="flipClass"
									style="padding: 5px; background-color: #d9d9d9; border: solid 1px #c3c3c3;">
									<center>
										<h2>
											<b><font color="#001a33" width="30%"><%=s%></font></b>
										</h2>
									</center>

									<div class="clearfix"></div>
								</div>

							</a>

							<div class="container-fluid dashboardcon" style="padding: 50px; display: none;"
								id="dashboardData<%=divCounter%>">
								<%
									List<DashBoard> courseDetailListBySession = 
																																																dashboardListSemesterWise.get(s);
																																													System.out.println("courseDetailListBySession"+ courseDetailListBySession);
																																														for (DashBoard d : courseDetailListBySession) {
																																																
																																																count++;
								%>
								<div class="col-md-4 course_widget_height">
									<div class="dashboardcon_title" id="courseDetail<%=count%>">
										<div>
											<c:url value="/viewCourse" var="viewCourseUrl">
												<c:param name="id"
													value="<%=String.valueOf(d.getCourse().getId())%>" />
											</c:url>
											<c:url value="assignmentList" var="viewAssignmentUrl">
												<c:param name="courseId"
													value="<%=String.valueOf(d.getCourse().getId())%>" />
											</c:url>
											<c:url value="viewUserAnnouncements"
												var="viewAnnouncementUrl">
												<c:param name="courseId"
													value="<%=String.valueOf(d.getCourse().getId())%>" />
											</c:url>
											<c:url value="viewForum" var="viewForumUrl">
												<c:param name="courseId"
													value="<%=String.valueOf(d.getCourse().getId())%>" />
											</c:url>


											<c:url value="testList" var="viewTestUrl">
												<c:param name="courseId"
													value="<%=String.valueOf(d.getCourse().getId())%>" />
											</c:url>
											<c:url value="getContentUnderAPathForFaculty"
												var="viewContentUrl">
												<c:param name="courseId"
													value="<%=String.valueOf(d.getCourse().getId())%>" />
											</c:url>

											<span><a href="${viewCourseUrl}" /><%=d.getCourse().getCourseName()%>
												- <%=d.getCourse().getAcadYear()%></a></span>


											<!-- <i><a href="#" class="hide_course_submenu"
							data-toggle="dropdown" aria-expanded="false"><span></span><span></span><span></span></a></i> -->
										</div>
										<div class="show_course_submenu" style="display: none;">
											<div class="arrow-left"></div>
											<ul>
												<li><a href="<c:url value="${viewAnnouncementUrl}" />"><i
														class="fa fa-bullhorn"></i> Announcements</a></li>
												<li><a href="<c:url value="${viewAssignmentUrl}" />"><i
														class="fa fa-newspaper-o"></i> Assignments</a></li>
												<li><a href="<c:url value="${viewTestUrl}" />"><i
														class="fa fa-file-text"></i> Test</a></li>
												<li><a href="<c:url value="${viewForumUrl}" />"><i
														class="fa fa-twitch "></i> Discussions</a></li>
												<li><a href="<c:url value="${viewContentUrl}" />"><i
														class="fa fa fa-folder-open"></i> Content</a></li>

											</ul>
										</div>
									</div>
									<ul>
				    <li>Announcements <span><%=d.getPendingAssigmentCount()%></span></li>
					<li>Assignments <span><%=d.getPendingAssigmentCount()%></span></li>
					<li>Tests <span><%=d.getPendingTestCount()%></span></li>
					
				</ul>
								</div>
								<%
									}
								%>
							</div>
							
							<%
								}
							%>
						</div>

					</div>

					<%@include file="toDoDashboard.jsp"%>
					<jsp:include page="../common/studentToDo.jsp" />


					<!-- </div> -->
				</div>
			</div>

		

		</div>
	</div> --%>

				<script>
					$(document)
							.ready(
									function() {
										var cars = [ "bgcolor1", "bgcolor2",
												"bgcolor3", "bgcolor4",
												"bgcolor5", "bgcolor6",
												"bgcolor7", "bgcolor8",
												"bgcolor9" ];
										var count = 0;
										$('[id^=courseDetail]')
												.each(
														function() {
															if (count == cars.length - 1) {
																count = 0;
															}
															$(this)
																	.addClass(
																			cars[count]);
															count++;
														})

										$('body').addClass("dashboard_left");

										var mapValue = $('#mapSize').val();
										console.log('map value--->' + mapValue);

										$('[id^=flip]')
												.each(
														function() {
															var flipId = $(this)
																	.attr('id');

															$('#' + flipId)
																	.click(
																			function() {
																				console
																						.log('flip entered');

																				var counter = flipId
																						.split("flip");
																				var array = JSON
																						.parse("[1"
																								+ counter
																								+ "]");

																				console
																						.log('count--->'
																								+ array[1]);
																				$(
																						"#dashboardData"
																								+ array[1])
																						.slideToggle(
																								"slow");
																			});
														});
									});
				</script>
				<script>
				$("#semDashboardFaculty").prop("selectedIndex", 0);
				</script>