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
					<li class="breadcrumb-item active" aria-current="page">ICA
						List</li>
				</ol>
			</nav>

			<jsp:include page="../common/alert.jsp" />

			<!-- Input Form Panel -->
			<div class="card bg-white">
				<div class="card-body">

					<div class="col-12 text-center">
						<h5 class="border-bottom pb-2 text-uppercase">Student
							Evaluation Report</h5>
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
						<div class="col-md-4 col-sm-6 col-xs-12 column">
							<div class="form-group">
								<label path="acadSession" for="acadSession">Acad Session</label>
								<select id="acadSession" path="acadSession"
									class="form-control subjectParam">
									<option value="">Select Session</option>
									<%--  <c:forEach var="course" items="${allCourses}"
                                                            varStatus="status">
                                                            <form:option value="${course.id}">${course.courseName}</form:option>
                                                      </c:forEach> --%>
								</select>
							</div>
						</div>
						<div class="col-8">
							<label class="font-weight-bold">Program <span
								class="text-danger">*</span></label> <select class="form-control"
								id="programIds" required="required" multiple>
								<option selected disabled>Select Program</option>
								<c:forEach var="program" items="${programList}">
									<option value="${program.id}">${program.programName}</option>
								</c:forEach>
							</select>
						</div>
						<div class="col-md-4 col-sm-6 col-xs-12 column">
							<div class="form-group">
								<label for="acadYear">Report Type</label> <select
									id="reportType" class="form-control subjectParam">
									<option value="" selected disabled hidden>Select
										Report Type</option>
								
										<option value="ProgWise">Program-Wise</option>
										<option value="subjWise">Subject-Wise without Bifurcation</option>
										<option value="subjWiseComp">Subject-Wise with Bifurcation</option>
										<!-- <option value="subjWiseCompSaveAsDraft">Draft</option> -->
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
								onclick="generateIcaReport()"> Generate Report</a>
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
			
	
			$(".subjectParam")
			.on(
					'change',
					function() {
						var acadYear = $('#acadYear').val();
						var acadSession = $('#acadSession').val();
						var campusId = $('#campusId').val();
			if (acadYear && !acadSession) {

				$
						.ajax({
							type : 'GET',
							url : '${pageContext.request.contextPath}/getSessionByParamForIcaReport?'
									+ 'acadYear='
									+ acadYear
									+ '&campusId='
									+ campusId,

							success : function(data) {
								var json = JSON.parse(data);
								console.log('json--->' + json);
								var optionsAsString = "";

								$('#acadSession').find('option').remove();
								optionsAsString += "<option value=\"\" selected disabled> Select Session</option>";
								console.log(json);
								for (var i = 0; i < json.length; i++) {
									var idjson = json[i];
									console.log(idjson);

									for ( var key in idjson) {
										console.log(key + "" + idjson[key]);

										console.log('else entered00');
										optionsAsString += "<option value='" +key + "'>"
												+ idjson[key] + "</option>";

									}
								}

								$('#acadSession').append(optionsAsString);

							}
						});
			}
					});	
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
		
		function generateIcaReport() {

			var acadYear = $('#acadYear').val();
			var acadSession = $('#acadSession').val();

			var programId = $('#programIds').val();
			var campusId = $('#campusId').val();
			var reportType = $('#reportType').val();
			//var acadSession = $('#acadSessionProgramWise').val();
		console.log('acadYear'+acadYear+'acadSession'+acadSession+'programId'+programId);
			if (acadYear != null && acadSession != null
					&& programId != null && reportType!=null) {
				
				if(campusId!=null){
					window.location.href = '${pageContext.request.contextPath}/downloadIcaReport?'
						+ 'acadYear='
						+ acadYear
						+ '&acadSession='
						+ acadSession + '&programId=' + programId+'&campusId='+campusId+'&reportType='+reportType;
				}else{
				window.location.href = '${pageContext.request.contextPath}/downloadIcaReport?'
						+ 'acadYear='
						+ acadYear
						+ '&acadSession='
						+ acadSession + '&programId=' + programId+'&reportType='+reportType;
				}

			} else {
				alert("please fill all the fields");
			}

		}
		
		</script>