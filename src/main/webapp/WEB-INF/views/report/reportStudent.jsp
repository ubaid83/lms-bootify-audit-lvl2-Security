<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>


<jsp:include page="../common/newDashboardHeader.jsp" />


<div class="d-flex" id="myCoursesPage">
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
					<div class="bg-white pb-5">
						<nav class="" aria-label="breadcrumb">
							<ol class="breadcrumb d-flex">
								<li class="breadcrumb-item"><a
									href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
								<li class="breadcrumb-item active" aria-current="page">Reports
									</li>
							</ol>

						</nav>
	

						<div class="form-row">
							

							<div class="col-lg-5 col-md-5 col-sm-12 mt-3">
								<label class="sr-only">Select Course</label> <select
									class="form-control" id="reportCourseStudent">
									<c:if test="${empty courseId}">
										<option value="" disabled selected>--SELECT COURSE--</option>
									</c:if>
									<c:forEach var='cList'
										items='${ courseList }'>
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
					<section class="assignWrap">
					
                                <div class="col-12 bg-dark subHead1 text-white mt-5">
                                    <h6 class="p-2 mb-3">GRAPHICAL ASSIGNMENTS REPORT</h6>
                                </div>
                                <section class="col-12 mb-5 fnt-13 chartWrap">
                                <canvas id="assignReportChartStudent"></canvas>
                                </section>
                               
                            </section>
                            
                            
                     <section class="mt-5 testWrap">
                                <div class="col-12 bg-dark text-white subHead1">
                                    <h6 class="p-2 mb-3">GRAPHICAL TEST REPORT</h6>
                                </div>
                                
                                <section class="col-12 mb-5 fnt-13 chartWrap">
                                <canvas id="testReportChartStudent"></canvas>
                                </section>

                                




                            </section>
					
				</div>


				<!-- SIDEBAR START -->
				<jsp:include page="../common/newSidebar.jsp" />
				<!-- SIDEBAR END -->
				<jsp:include page="../common/footer.jsp" />