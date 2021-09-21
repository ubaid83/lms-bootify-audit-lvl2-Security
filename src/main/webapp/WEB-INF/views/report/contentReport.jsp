<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>



<jsp:include page="../common/newDashboardHeader.jsp" />

<div class="d-flex" id="facultyAssignmentPage">
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
					<li class="breadcrumb-item active" aria-current="page">Content Report</li>
				</ol>
			</nav>

			<jsp:include page="../common/alert.jsp" />

			<!-- Input Form Panel -->
			<div class="card bg-white">
				<div class="card-body">

					<div class="col-12 text-center">
						<h5 class="border-bottom pb-2 text-uppercase">Download Content Report</h5>
					</div>


					<div class="row">
						<div class="col-md-4 col-sm-6 col-xs-12 column">
							<div class="form-group">
								<label for="acadYear">Academic Year</label> <select
									id="acadYear" class="form-control subjectParam">
									<option value="" selected disabled hidden>Select
										Academic Year</option>
								<%-- 	<c:forEach items="${acadYears}" var="acadYear">
										<option value="${acadYear}">${acadYear}</option>
									</c:forEach> --%>
									<c:forEach items="${acadYearCodeList}" var="acadYear">
										<option value="${acadYear}">${acadYear}</option>
									</c:forEach>
								</select>
							
								
							</div>
						</div>
						
						
						<div class="col-6">
							<label class="font-weight-bold">Campus <span
								class="text-danger">*</span></label> <select class="form-control"
								id="campusId">
								<option selected disabled>Select Campus</option>
								<c:forEach var="listValue" items="${campusList}">

									<option value="${listValue.campusId}">${listValue.campusName}</option>

								</c:forEach>
							</select>
						</div>
						<div class="col-12 mt-3">
							<a class="btn btn-large btn-primary"
								onclick="generateContentReport()"> Generate Report</a>
						</div>
					</div>




				</div>
			</div>



			<!-- /page content: END -->

		</div>

		<!-- SIDEBAR START -->

		<!-- SIDEBAR END -->

		<jsp:include page="../common/newAdminFooter.jsp" />
		<script>
			
	
			
		</script>
		<script>
		/* function generateIcaReport() {

			var acadYear = $('#acadYear').val();
			var acadSession = $('#acadSession').val();

			var programId = $('#programIds').val();
			var campusId = $('#campusId').val();
			//var acadSession = $('#acadSessionProgramWise').val();
		console.log('acadYear'+acadYear+'acadSession'+acadSession+'programId'+programId);
			if (acadYear != null && acadSession != null
					&& programId != null) {
				
				if(campusId!=null){
					window.location.href = '${pageContext.request.contextPath}/downloadIcaReport?'
						+ 'acadYear='
						+ acadYear
						+ '&acadSession='
						+ acadSession + '&programId=' + programId+'&campusId='+campusId;
				}else{
				window.location.href = '${pageContext.request.contextPath}/downloadIcaReport?'
						+ 'acadYear='
						+ acadYear
						+ '&acadSession='
						+ acadSession + '&programId=' + programId;
				}

			} else {
				alert("please fill all the fields");
			}

		} */
		
		function generateContentReport() {

			var acadYear = $('#acadYear').val();
			
			var campusId = $('#campusId').val();
			
		
			if (acadYear != null) {
				
				if(campusId!=null){
					window.location.href = '${pageContext.request.contextPath}/downloadContentReport?'
						+ 'acadYear='
						+ acadYear
						+'&campusId='+campusId;
				}else{
				window.location.href = '${pageContext.request.contextPath}/downloadContentReport?'
						+ 'acadYear='
						+ acadYear;
				}

			} else {
				alert("Acad Year Field is Mandatory");
			}

		}
		
		</script>