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
<!-- <div class="d-flex dataTableBottom" id="facultyAssignmentPage"> -->
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
							<li class="breadcrumb-item active" aria-current="page">ICA
								LIST</li>
						</ol>
					</nav>

					<jsp:include page="../common/alert.jsp" />


					<!-- Input Form Panel -->
					<div class="card bg-white border">
						<div class="card-body">
							<h5 class="border-bottom pb-2 text-uppercase"> Report</h5>
						<div class="col-md-4 col-sm-6 col-xs-12">
							<div class="form-group">
								<label for="acadYear">Academic Year</label> <select
									id="acadYear" class="form-control subjectParam">
									<option value="" selected disabled hidden>Select
										Academic Year</option>
									<c:forEach items="${acadYearCodeList}" var="acadYear">
										<option value="${acadYear}">${acadYear}</option>

									</c:forEach>
								</select>
							</div>
						</div>						
						<div class="col-12 mt-3">
							<a class="btn btn-large btn-primary"
								onclick="generateIcaReportForFaculty1()"> Generate Report</a>
						</div>
						</div>
					</div>

				</div>

				<!-- SIDEBAR START -->
				<jsp:include page="../common/newSidebar.jsp" />
				<!-- SIDEBAR END -->
				<jsp:include page="../common/footer.jsp" />



				<!-- <script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/MathJax.js?config=TeX-AMS_HTML"></script> -->

				<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>

		<script>
		
		
		function generateIcaReportForFaculty1() {

			var acadYear = $('#acadYear').val();
			
			//var acadSession = $('#acadSessionProgramWise').val();
		console.log('acadYear'+acadYear);
			if (acadYear != null  ) {
				
				
				window.location.href = '${pageContext.request.contextPath}/downloadIcaFacultyReport?'
						+ 'acadYear='
						+ acadYear
						+'&reportType=subjWiseComp';
				

			} else {
				alert("please fill all the fields");
			}

		}
		
		</script>		
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				