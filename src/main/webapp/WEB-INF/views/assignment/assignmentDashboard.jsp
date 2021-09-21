<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<jsp:include page="../common/newDashboardHeader.jsp" />


<div class="d-flex" id="facultyDashboardPage">
	<jsp:include page="../common/newLeftNavBarFaculty.jsp" />
	<jsp:include page="../common/newLeftSidebarFaculty.jsp" />
	<jsp:include page="../common/rightSidebarFaculty.jsp" />
	

	<div class="container-fluid m-0 p-0 dashboardWraper assignmentW">
		<jsp:include page="../common/newTopHeaderFaculty.jsp" />

		<div class="container mt-5">
			<div class="row">
				<div
					class="col-lg-9 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto">

					<div class="bg-white pb-5">
						<nav class="" aria-label="breadcrumb">
							<ol class="breadcrumb d-flex">
								<li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
								<li class="breadcrumb-item active" aria-current="page">My
									Courses</li>

								<li class="ml-auto">
									<select class="form-control" id="semSelectFacultyAssignment">
									<option value="" selected disabled>Select Semester</option>
										<c:forEach  items="${ sessionWiseCourseListMap }"
											var="sList" varStatus="count">
											
											<c:if test="${sList.key eq userBean.acadSession}">
												<option value="${sList.key}" selected>${sList.key}</option>
											</c:if>
											<c:if test="${sList.key ne userBean.acadSession }">
												<option value="${sList.key}">${sList.key}</option>
											</c:if>
										</c:forEach>
									</select>
								</li>
							</ol>

						</nav>




						<div class="col-12">
							<div class="row text-center" id="courseListSemWisefacultyAssignment">
							
								<c:forEach var='cList' items='${ sessionWiseCourseList[userBean.acadSession] }'>
									<div class="col-lg-3 col-md-3 col-sm-6 col-6 mt-5">
										<div class="courseAsset d-flex align-items-start flex-column">
											<h6 class="text-uppercase mb-auto"><c:out value="${ cList.courseName }" /></h6>
											<span class="courseNav"> <a href="${pageContext.request.contextPath}/createAssignmentFromGroup?courseId=${cList.id}">
													<p class="caBg">Create Group Assignment</p>
											</a> <a href="${pageContext.request.contextPath}/createAssignmentFromMenu?courseId=${cList.id}">
													<p class="ctBg">Create Student Assignment</p>
											</a>
											
											<a href="${pageContext.request.contextPath}/addAnnouncementForm?courseId=${cList.id}">
													<p class="ctBg">Create Announcement For Course</p>
											</a>
											</span>
										</div>
									</div>
								</c:forEach>
								
							</div>
						</div>
					</div>
				</div>


				<!-- SIDEBAR START -->
				<jsp:include page="../common/newSidebar.jsp" />
				<!-- SIDEBAR END -->
				<jsp:include page="../common/footer.jsp" />