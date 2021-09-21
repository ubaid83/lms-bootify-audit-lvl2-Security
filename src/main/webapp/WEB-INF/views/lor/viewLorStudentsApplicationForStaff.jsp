<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%
	boolean isEdit = "true".equals((String) request.getAttribute("edit"));
%>


<jsp:include page="../common/newDashboardHeader.jsp" />


<div class="d-flex" id="facultyAssignmentPage">



	<!-- DASHBOARD BODY STARTS HERE -->


	<sec:authorize access="hasRole('ROLE_FACULTY')">
		<jsp:include page="../common/newLeftNavBarFaculty.jsp" />
		<jsp:include page="../common/newLeftSidebarFaculty.jsp" />
		<jsp:include page="../common/rightSidebarFaculty.jsp" />
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_FACULTY')">
		<jsp:include page="../common/newTopHeaderFaculty.jsp" />
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_STAFF')">
		<div class="container-fluid m-0 p-0 dashboardWraper">
			<header class="container-fluid sticky-top">
				<nav class="navbar navbar-expand-lg navbar-light p-0">
					<!--                     <i class="fas fa-bars fa-2x mr-3 leftnav-btn" data-toggle="modal" data-target="#leftnav"></i> -->
					<a class="navbar-brand" href="homepage"> <c:choose>
							<c:when test="${instiFlag eq 'nm'}">
								<img src="<c:url value="/resources/images/logo.png" />"
									class="logo" title="NMIMS logo" alt="NMIMS logo" />
							</c:when>
							<c:otherwise>
								<img src="<c:url value="/resources/images/svkmlogo.png" />"
									class="logo" title="SVKM logo" alt="SVKM logo" />
							</c:otherwise>
						</c:choose>

					</a>
					<button class="adminNavbarToggler" type="button"
						data-toggle="collapse" data-target="#adminNavbarCollapse">
						<i class="fas fa-bars"></i>
					</button>

					<div class="collapse navbar-collapse" id="adminNavbarCollapse">
						<ul class="navbar-nav ml-auto">

							<li id="program" class="nav-item active" data-toggle="tooltip"
								data-placement="bottom" title="homepage"><a
								class="nav-link" href="homepage"><i class="fas fa-home"></i>
									<span>Home</span></a></li>

						</ul>
					</div>
				</nav>
			</header>
	</sec:authorize>
	<!-- SEMESTER CONTENT -->
	<sec:authorize access="hasRole('ROLE_FACULTY')">
		<div class="container mt-5">
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_STAFF')">
		<div class="container">
	</sec:authorize>
	<div class="row">
		<div
			class="col-lg-12 col-md-12 col-sm-12 dashboardBody p-5 ml-auto mr-auto">

			<!-- page content: START -->
			<nav aria-label="breadcrumb">
				<ol class="breadcrumb">
					<li class="breadcrumb-item"><a
						href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
					<li class="breadcrumb-item active" aria-current="page">LOR
						Application</li>
				</ol>
			</nav>

			<jsp:include page="../common/alert.jsp" />

			<!-- Input Form Panel -->

			<div class="card bg-white border">
				<div class="card-body">
					<h5 class="text-center pb-2 ">LOR Application</h5>
					<!-- Input Form Panel -->
					<div class="card bg-white border">

						<div class="table-responsive mt-3 mb-3 lorTable">
							<table class="table table-striped table-hover">
								<thead>
									<tr>
										<th>Sr. No.</th>
										<th>Username</th>
										<th>Roll No.</th>
										<th>Student Name</th>
										<th>Department</th>
										<th>Program Name</th>
										<th>Acad Year</th>
										<th>No of Hard Copy</th>
										<th>Expected Date</th>
										<th>Status</th>
										<th>Action</th>
									</tr>
								</thead>

								<tbody>

									<c:forEach var="student" items="${lorRegDetailsList}"
										varStatus="status">
										<tr>
											<td><c:out value="${status.count}" /></td>
											<td><c:out value="${student.username}" /></td>
											<td><c:out value="${student.rollNo}" /></td>
											<td><c:out value="${student.name}" /></td>
											<td><c:out value="${student.department}" /></td>
											<td><c:out value="${programMap[student.programEnrolledId]}" /></td>
											<td><c:out value="${student.acadYear}" /></td>
											<td><c:out value="${student.noOfCopies}" /></td>
											<td>
												
												<div class="input-group" style="width: 150px">
													<input type="text" value="${student.expectedDate}"
														id="expectedDate${student.lorRegStaffId}"
														data-lorRegStaffId="${student.lorRegStaffId}"
														placeholder="Date" class="form-control " readonly />
													<div class="input-group-append">
														<button
															class="btn btn-outline-secondary initiateDatePicker"
															data-lorRegStaffId="${student.lorRegStaffId}"
															type="button">
															<i class="fas fa-calendar"></i>
														</button>
													</div>
												</div>
											
											</td>
											<td>
											<c:if test = "${empty student.appApproval}"><b>Application Approval</b></c:if>
											<c:if test = "${not empty student.appApproval && empty student.lorDocsFilePath}"><b>Student Document Pending</b></c:if>
											<c:if test = "${not empty student.appApproval && not empty student.lorDocsFilePath && empty student.docApproval}"><b>Document Approval</b></c:if>
											<c:if test = "${not empty student.lorDocsFilePath && not empty student.docApproval && empty student.lorFormatFilePath}"><b>Upload LOR Format</b></c:if>
											<c:if test = "${not empty student.lorFormatFilePath && empty student.finalLorFilePath}"><b>Final LOR Pending</b></c:if>
											<c:if test = "${not empty student.appApproval && not empty student.docApproval && not empty student.finalLorFilePath}"><b>Final LOR Approval</b></c:if>
											</td>
											
											<td>
											<a href="#" title="Details"
												data-lorRegId="${student.id}"
												class="lorDetailsModalBtn" data-toggle="modal"
												data-target="#lorDetailsModal"> <i
													class="fas fa-info-circle"></i>
											</a>
											<c:if test = "${student.appApproval eq 'Approve' && not empty student.lorDocsFilePath}">
												<c:url value="downloadLorFiles" var="downloadurl">
													<c:param name="id" value="${student.lorRegStaffId}" />
													<c:param name="fileType" value="lorDocsFilePath" />
												</c:url>
										           <a href="${downloadurl}" title="Download Student Document"><i
													class="fa fa-download" aria-hidden="true"
													style="font-size: medium;"></i></a>
											</c:if>
											<c:if test = "${student.docApproval eq 'Approve' && not empty student.lorDocsFilePath}">
												<a href="#" title="Upload LOR Format"><i
													class="fa fa-upload lorFormatFileUploadData" aria-hidden="true"
													style="font-size: medium;" data-lorRegStaffId="${student.lorRegStaffId}" 
													data-toggle="modal" data-target="#lorFormatFileUploadModal"></i></a>
											</c:if>
											<c:if test = "${not empty student.finalLorFilePath}">
												<c:url value="downloadLorFiles" var="downloadurl">
													<c:param name="id" value="${student.lorRegStaffId}" />
													<c:param name="fileType" value="finalLorFilePath" />
												</c:url>
										           <a href="${downloadurl}" title="Download Final LOR"><i
													class="fa fa-download" aria-hidden="true"
													style="font-size: medium;"></i></a>
											</c:if>
											</td>
										</tr>
									</c:forEach>
								</tbody>
								<tfoot>
									<tr>
										<th></th>
										<th>--</th>
										<th>--</th>
										<th>--</th>
										<th>--</th>
										<th>--</th>
										<th>--</th>
										<th>--</th>
										<th>--</th>
										<th>--</th>
									</tr>
								</tfoot>
							</table>
						</div>


					</div>
				</div>
			</div>

			<!-- /page content: END -->

		</div>
	</div>
</div>
<sec:authorize access="hasRole('ROLE_FACULTY')">
	<jsp:include page="../common/footer.jsp" />
</sec:authorize>
<sec:authorize access="hasRole('ROLE_STAFF')">
	<jsp:include page="../common/newAdminFooter.jsp" />
</sec:authorize>

<div id="lorFormatFileUpload">
	<div class="modal fade fnt-13" id="lorFormatFileUploadModal"
		tabindex="-1" role="dialog" aria-labelledby="lorFormatFileUploadModal"
		aria-hidden="true">
		<div
			class="modal-dialog modal-dialog-centered modal-dialog-scrollable"
			role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title">Upload LOR Format File</h5>
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<form:form action="uploadLORFormatFile" id="uploadLORFormatFileForm"
					method="post" enctype="multipart/form-data"
					modelAttribute="lorRegStaff">
					<form:hidden id="lorRegStaffId" path="id" />
					<div class="modal-body">
						<div class="row">
							<div class="col-12">
								<h6>Upload file</h6>
								<input type="file" name="file" class="form-control-file mb-1"
									id="lorFormatFile" multiple>
							</div>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-modalClose"
							data-dismiss="modal">Close</button>
						<button id="submitBtn" class="btn btn-modalSub" name="submitBtn"
							formaction="uploadLORFormatFile">Submit</button>
					</div>
				</form:form>
			</div>
		</div>
	</div>
</div>

<div class="modal lorDetailsModal" id="lorDetailsModal" tabindex="-1"
	role="dialog">
	<div
		class="modal-dialog modal-dialog-centered modal-dialog-scrollable modal-xl">

		<div class="modal-content">

			<div class="modal-header">
				<h5 class="text-center" id="complaintitle">LOR Details</h5>
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">
				<div class="row">
					<div id="sapIdDiv" class="col-md-4 col-sm-6 col-xs-12">
						<div class="form-group">
							<label class="textStrong">SAPID:</label>
							<p id="sapIdLDM"></p>
						</div>
					</div>
					<div id="nameDiv" class="col-md-4 col-sm-6 col-xs-12">
						<div class="form-group">
							<label class="textStrong">NAME:</label>
							<p id="nameLDM"></p>
						</div>
					</div>
					<div id="emailDiv" class="col-md-4 col-sm-6 col-xs-12">
						<div class="form-group">
							<label class="textStrong">EMAIL:</label>
							<p id="emailLDM"></p>
						</div>
					</div>
					<div id="mobileDiv" class="col-md-4 col-sm-6 col-xs-12">
						<div class="form-group">
							<label class="textStrong">MOBILE:</label>
							<p id="mobileLDM"></p>
						</div>
					</div>
					<div id="programDiv" class="col-md-4 col-sm-6 col-xs-12">
						<div class="form-group">
							<label class="textStrong">PROGRAM:</label>
							<p id="programLDM"></p>
						</div>
					</div>
					<div id="countryForHigherStudyDiv"
						class="col-md-4 col-sm-6 col-xs-12">
						<div class="form-group">
							<label class="textStrong">COUNTRY FOR HIGHER STUDIES:</label>
							<p id="countryForHigherStudyLDM"></p>
						</div>
					</div>
					<div id="universityNameDiv" class="col-md-4 col-sm-6 col-xs-12">
						<div class="form-group">
							<label class="textStrong">UNIVERSITY FOR HIGHER STUDIES:</label>
							<p id="universityNameLDM"></p>
						</div>
					</div>
					<div id="programToEnrollDiv" class="col-md-4 col-sm-6 col-xs-12">
						<div class="form-group">
							<label class="textStrong">PROGRAM TO ENROLL:</label>
							<p id="programToEnrollLDM"></p>
						</div>
					</div>
					<div id="tentativeDOJDiv" class="col-md-4 col-sm-6 col-xs-12">
						<div class="form-group">
							<label class="textStrong">TENTATIVE DATE OF JOINING:</label>
							<p id="tentativeDOJLDM"></p>
						</div>
					</div>
					<div id="competitiveExamDiv" class="col-md-4 col-sm-6 col-xs-12">
						<div class="form-group">
							<label class="textStrong">COMPETITIVE EXAM TAKEN:</label>
							<p id="competitiveExamLDM"></p>
						</div>
					</div>


					<div id="examMarksheetDiv" class="col-md-4 col-sm-6 col-xs-12 d-none">
						<div class="form-group">
							<label class="textStrong">COMPETITIVE EXAM MARKSHEET:</label>
							<p id="examMarksheetLDM"></p>
						</div>
					</div>

					<div id="toeflOrIeltsDiv" class="col-md-4 col-sm-6 col-xs-12">
						<div class="form-group">
							<label class="textStrong">TOEFL Or IELTS:</label>
							<p id="toeflOrIeltsLDM"></p>
						</div>
					</div>
					<div id="toeflOrIeltsMarksheetDiv" class="col-md-4 col-sm-6 col-xs-12 d-none">
						<div class="form-group">
							<label class="textStrong">TOEFL Or IELTS EXAM MARKSHEET:</label>
							<p id="toeflOrIeltsMarksheetLDM"></p>
						</div>
					</div>
					<!-- <div id="toeflScoreDiv" class="col-md-4 col-sm-6 col-xs-12 d-none">
						<div class="form-group">
							<label class="textStrong">TOEFL Score:</label>
							<p id="toeflScoreLDM"></p>
						</div>
					</div>
					<div id="ieltsReadingScoreDiv"
						class="col-md-4 col-sm-6 col-xs-12 ieltsDiv d-none">
						<div class="form-group">
							<label class="textStrong">IELTS Reading Score:</label>
							<p id="ieltsReadingScoreLDM"></p>
						</div>
					</div>
					<div id="ieltsWritingScoreDiv"
						class="col-md-4 col-sm-6 col-xs-12 ieltsDiv d-none">
						<div class="form-group">
							<label class="textStrong">IELTS Writing Score:</label>
							<p id="ieltsWritingScoreLDM"></p>
						</div>
					</div>
					<div id="ieltsSpeakingScoreDiv"
						class="col-md-4 col-sm-6 col-xs-12 ieltsDiv d-none">
						<div class="form-group">
							<label class="textStrong">IELTS Speaking Score:</label>
							<p id="ieltsSpeakingScoreLDM"></p>
						</div>
					</div>
					<div id="ieltsListeningScoreDiv"
						class="col-md-4 col-sm-6 col-xs-12 ieltsDiv d-none">
						<div class="form-group">
							<label class="textStrong">IELTS Listening Score:</label>
							<p id="ieltsListeningScoreLDM"></p>
						</div>
					</div> -->
					<div id="isNmimsPartnerUniversityDiv"
						class="col-md-4 col-sm-6 col-xs-12">
						<div class="form-group">
							<label class="textStrong">IS NMIMS PARTNER UNIVERSITY:</label>
							<p id="isNmimsPartnerUniversityLDM"></p>
						</div>
					</div>
					<div id="nmimsPartnerUniversityDiv"
						class="col-md-4 col-sm-6 col-xs-12 d-none">
						<div class="form-group">
							<label class="textStrong">NMIMS PARTNER UNIVERSITY NAME:</label>
							<p id="nmimsPartnerUniversityLDM"></p>
						</div>
					</div>
					<div id="applicationStatusDiv"
						class="col-md-4 col-sm-6 col-xs-12 d-none">
						<label class="textStrong">APPLICATION STATUS:</label>
						<p id="appApprovalLDM"></p>
					</div>
					<div id="documentStatusDiv"
						class="col-md-4 col-sm-6 col-xs-12 d-none">
						<label class="textStrong">DOCUMENT STATUS:</label>
						<p id="docApprovalLDM"></p>
					</div>
					<div id="lorStatusDiv" class="col-md-4 col-sm-6 col-xs-12 d-none">
						<label class="textStrong">LOR STATUS:</label>
						<p id="lorApprovalLDM"></p>
					</div>
				</div>
				<hr>
				<div class="row">

					<div id="applicationApprovalDiv"
						class="col-lg-6 col-md-10 col-sm-12 mt-2 d-none">
						<div>
							<label class="textStrong"> <strong>APPLICATION
									STATUS:</strong></label>
							<div class="pretty p-switch p-fill p-toggle">
								<input type="checkbox" value="Approve" id="appApproval" />
								<div class="state p-success p-on">
									<label>Approve</label>
								</div>
								<div class="state p-danger p-off">
									<label>Reject</label>
								</div>
							</div>
						</div>
						<div id="appRejectionReasonDiv" class="d-none">
							<label class="textStrong"> <strong>REASON FOR
									REJECT APPLICATION STATUS:</strong></label>
							<textarea class="form-control" id="appRejectionReason" rows="3"></textarea>
						</div>
					</div>

					<div id="documentApprovalDiv"
						class="col-lg-6 col-md-10 col-sm-12 mt-3 d-none">
						<div>
							<label class="textStrong"> <strong>DOCUMENT
									STATUS:</strong></label>
							<div class="pretty p-switch p-fill p-toggle">
								<input type="checkbox" value="Approve" id="docApproval" />
								<div class="state p-success p-on">
									<label>Approve</label>
								</div>
								<div class="state p-danger p-off">
									<label>Reject</label>
								</div>
							</div>
						</div>
						<div id="docRejectionReasonDiv" class=" d-none">
							<label class="textStrong"> <strong>REASON FOR
									REJECT DOCUMENT STATUS:</strong></label>
							<textarea class="form-control" id="docRejectionReason" rows="3"></textarea>
						</div>
					</div>

					<div id="lorApprovalDiv"
						class="col-lg-6 col-md-10 col-sm-12 mt-3 d-none">
						<div>
							<label class="textStrong"> <strong>LOR STATUS:</strong></label>
							<div class="pretty p-switch p-fill p-toggle">
								<input type="checkbox" value="Approve" id="lorApproval" />
								<div class="state p-success p-on">
									<label>Approve</label>
								</div>
								<div class="state p-danger p-off">
									<label>Reject</label>
								</div>
							</div>
						</div>
						<div id="lorRejectionReasonDiv" class="d-none">
							<label class="textStrong"> <strong>REASON FOR
									REJECT LOR STATUS:</strong></label>
							<textarea class="form-control" id="lorRejectionReason" rows="3"></textarea>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button id="saveStatus" type="button"
					class="btn btn-modalSub d-none">Save</button>
				<button id="closeModal" type="button" class="btn btn-modalClose"
					data-dismiss="modal">Close</button>
			</div>
		</div>

	</div>
</div>
<script>
/* on click get lor details of student */
	$('.lorDetailsModalBtn').on('click', function () {
		
	    let lrId =  $(this).attr("data-lorRegId");
	   
	    $.ajax({
	    	type : 'POST',
			dataType: 'JSON',
			data: { lorRegId : lrId },
			url : '${pageContext.request.contextPath}/getStudentLorDetailsByLorRegId',
         	success : function(data) {
         		$('#sapIdLDM').html(data.username);
   	  			$('#nameLDM').html(data.name);
   	  			if(data.email === ""){
   	  				$('#emailLDM').html("NA");
   	  			}else{
   	  				$('#emailLDM').html(data.email);
   	  			}
   	  			if(data.mobile === ""){
   	  				$('#mobileLDM').html("NA");
   	  			}else{
   	  				$('#mobileLDM').html(data.mobile);
   	  			}
   	 			
   	 			$('#programLDM').html(data.programName);
   	 			$('#countryForHigherStudyLDM').html(data.countryForHigherStudy);
	  			$('#universityNameLDM').html(data.universityName);
	  			
	  			if(data.programToEnroll.includes("Other")){
	  				$('#programToEnrollLDM').html(data.programToEnroll.substring(6));
	  			}else{
	  				$('#programToEnrollLDM').html(data.programToEnroll);
	  			}
	 			$('#tentativeDOJLDM').html(data.tentativeDOJ);
	 			
	 			if(data.competitiveExam === "Not Taken"){
	 				$('#competitiveExamLDM').html(data.competitiveExam);
	 			}else if(data.competitiveExam.includes("Other")){
	 				$('#competitiveExamLDM').html(data.competitiveExam.substring(6));
	 				$('#examMarksheetDiv').removeClass('d-none');
	 				$('#examMarksheetDiv p').html('<a href="downloadLorFiles?id='+lrId+'&fileType=examMarksheet">Download</a>');
	 			}else{
	 				$('#examMarksheetDiv').removeClass('d-none');
	 				$('#competitiveExamLDM').html(data.competitiveExam);
	 				$('#examMarksheetDiv p').html('<a href="downloadLorFiles?id='+lrId+'&fileType=examMarksheet">Download</a>');
	 			}
	 			
   	  			$('#toeflOrIeltsLDM').html(data.toeflOrIelts);
	   	  		if(data.toeflOrIelts === "Not Taken"){
	 				$('#toeflOrIeltsLDM').html(data.toeflOrIelts);
					$('#toeflOrIeltsMarksheetDiv').addClass('d-none');
	 			}else if(data.toeflOrIelts === "TOEFL"){
	 				$('#toeflOrIeltsMarksheetDiv').removeClass('d-none');
	 				$('#toeflOrIeltsMarksheetDiv p').html('<a href="downloadLorFiles?id='+lrId+'&fileType=toeflOrIeltsMarksheet">Download</a>');
	 				$('#toeflScoreLDM').html(data.toeflScore);
	 			}else if(data.toeflOrIelts === "IELTS"){
	 				$('#toeflOrIeltsMarksheetDiv').removeClass('d-none');
					$('#toeflOrIeltsMarksheetDiv  p').html('<a href="downloadLorFiles?id='+lrId+'&fileType=toeflOrIeltsMarksheet">Download</a>');
	 			}
	   	  		
	   	  		$('#isNmimsPartnerUniversityLDM').html(data.isNmimsPartnerUniversity);
	   	  		if(data.isNmimsPartnerUniversity === "Yes"){
	   	  			$('#nmimsPartnerUniversityDiv').removeClass('d-none');
	   	  			$('#nmimsPartnerUniversityLDM').html(data.nmimsPartnerUniversity);
	   	  		}
	 			
	   	  		if(data.appApproval == null){
	   	  			$('#applicationApprovalDiv').removeClass('d-none');
	   	  			$('#saveStatus').removeClass('d-none');
	   	  			$('#saveStatus').attr('data-lorRegStaffId',data.lorRegStaffId);
	   	  			$('#saveStatus').attr('data-approval',"application");
					$('#saveStatus').prop('disabled',false);
		   	  		$('#appApproval').prop('checked', true);
		   	  		$('#appApproval').val('Approve');
			   	  	$("#appRejectionReasonDiv").addClass('d-none');
					$('#appRejectionReason').val("");
	   	  		}else if(data.appApproval != null && data.lorDocsFilePath == null && data.docApproval == null){
		   	  		$('#applicationApprovalDiv').removeClass('d-none');
			   	  	$('#saveStatus').removeClass('d-none');
	   	  			$('#saveStatus').attr('data-lorRegStaffId',data.lorRegStaffId);
	   	  			$('#saveStatus').attr('data-approval',"application");
		   	  		if (data.appApproval == 'Approve') {
			   	 		$('#appApproval').prop('checked', true);
			   	 		$('#appApproval').val('Approve');
				   	 	$("#appRejectionReasonDiv").addClass('d-none');
						$('#appRejectionReason').val("");
			   	 	} else {
			   	 		$('#appApproval').prop('checked', false);
				   	 	$('#appApproval').val('Reject');
						$("#appRejectionReasonDiv").removeClass('d-none');
						$('#appRejectionReason').val(data.appRejectionReason);
			   	 	}
	   	  		}else{
	   	  			$('#applicationApprovalDiv').addClass('d-none');
	   	  			$('#applicationStatusDiv').removeClass('d-none');
	   	  			$('#saveStatus').addClass('d-none');
   	  				$('#appApprovalLDM').html(data.appApproval);
   	  				if(data.appApproval == "Reject" && data.appRejectionReason){
   	  					$('#appApprovalLDM').html(data.appApproval+" - "+ data.appRejectionReason);
   	  				}
	   	  		}
	   	  		
	   	  		if(data.appApproval == 'Approve' && data.lorDocsFilePath != null && data.docApproval == null){
	   	  			$('#documentApprovalDiv').removeClass('d-none');
	   	  			$('#saveStatus').removeClass('d-none');
	   	  			$('#saveStatus').attr('data-lorRegStaffId',data.lorRegStaffId);
	  				$('#saveStatus').attr('data-approval',"document");
					$('#saveStatus').prop('disabled',false);
	  				$('#docApproval').prop('checked', true);
		   	  		$('#docApproval').val('Approve');
		   	  		$("#docRejectionReasonDiv").addClass('d-none');
					$('#docRejectionReason').val("");
	   	  		}else if(data.appApproval == 'Approve' && data.docApproval != null && data.lorDocsFilePath != null && data.lorFormatFilePath == null){
		   	  		$('#documentApprovalDiv').removeClass('d-none');
	   	  			$('#saveStatus').removeClass('d-none');
	   	  			$('#saveStatus').attr('data-lorRegStaffId',data.lorRegStaffId);
	  				$('#saveStatus').attr('data-approval',"document");
	  				$('#saveStatus').prop('disabled',false);
	  				if (data.docApproval == 'Approve') {
	  					$('#docApproval').prop('checked', true);
	  					$('#docApproval').val('Approve');
	  					$("#docRejectionReasonDiv").addClass('d-none');
	  					$('#docRejectionReason').val("");
	  				} else {
	  					$('#docApproval').prop('checked', false);
	  					$('#docApproval').val('Reject');
	  					$("#docRejectionReasonDiv").removeClass('d-none');
	  					$('#docRejectionReason').val(data.docRejectionReason);
	  				}
	  				$('#docApproval').prop('disabled',false);
	  				$('#docRejectionReason').prop('disabled',false);
	   	  		}else if(data.docApproval != null && data.lorFormatFilePath != null){
	   	  			$('#documentApprovalDiv').addClass('d-none');
	   	  			$('#documentStatusDiv').removeClass('d-none');
	   	  			$('#saveStatus').addClass('d-none');
	  				$('#docApprovalLDM').html(data.docApproval);
	  				if(data.docApproval == "Reject" && data.docRejectionReason){
	  					$('#docApprovalLDM').html(data.docApproval+" - "+ data.docRejectionReason);
	  				}
	   	  		}
	   	  		
		   	  	if(data.docApproval == 'Approve' && data.finalLorFilePath != null && data.lorApproval == null){
	   	  			$('#lorApprovalDiv').removeClass('d-none');
	   	  			$('#saveStatus').removeClass('d-none');
	   	  			$('#saveStatus').attr('data-lorRegStaffId',data.lorRegStaffId);
	  				$('#saveStatus').attr('data-approval',"lor");
					$('#saveStatus').prop('disabled',false);
	  				$('#lorApproval').prop('checked', true);
		   	  		$('#lorApproval').val('Approve');
		   	  		$("#lorRejectionReasonDiv").addClass('d-none');
					$('#lorRejectionReason').val("");
	   	  		}else if(data.finalLorFilePath == null && data.lorApproval != null && data.docApproval == 'Approve'){
	   	  			$('#lorApprovalDiv').removeClass('d-none');
	   	  			$('#saveStatus').removeClass('d-none');
	   	  			$('#saveStatus').attr('data-lorRegStaffId',data.lorRegStaffId);
	  				$('#saveStatus').attr('data-approval',"lor");
	  				$('#saveStatus').prop('disabled', true);
	  				if (data.lorApproval == 'Approve') {
	  					$('#lorApproval').prop('checked', true);
	  					$('#lorApproval').val('Approve');
	  					$("#lorRejectionReasonDiv").addClass('d-none');
	  					$('#lorRejectionReason').val("");
	  				} else {
	  					$('#lorApproval').prop('checked', false);
	  					$('#lorApproval').val('Reject');
	  					$("#lorRejectionReasonDiv").removeClass('d-none');
	  					$('#lorRejectionReason').val(data.lorRejectionReason);
	  				}
	  				$('#lorApproval').prop('disabled', true);
	  				$('#lorRejectionReason').prop('disabled', true);
	   	  		}else if(data.lorApproval != null){
	   	  			$('#lorApprovalDiv').addClass('d-none');
	   	  			$('#lorStatusDiv').removeClass('d-none');
	   	  			$('#saveStatus').addClass('d-none');
	  				$('#lorApprovalLDM').html(data.lorApproval);
	  				/* if(data.lorApproval == "Reject" && data.lorRejectionReason){
	  					$('#lorApprovalLDM').html(data.lotApproval+" - "+ data.lorRejectionReason);
	  				} */
	   	  		}
	   	  			
         	},
         	error: function(data){
         		$('#sapIdLDM').html("");
   	  			$('#nameLDM').html("");
   	  			$('#emailLDM').html("");
   	 			$('#mobileLDM').html("");
   	 			$('#programLDM').html("");
   	 			$('#countryForHigherStudyLDM').html("");
	  			$('#universityNameLDM').html("");
	  			$('#programToEnrollLDM').html("");
	 			$('#tentativeDOJLDM').html("");
	 			$('#competitiveExamLDM').html("");
   	  			$('#examMarksheetLDM').html("");
   	  			$('#toeflOrIeltsLDM').html("");
   	 			$('#toeflOrIeltsMarksheetLDM').html("");
	 			$('#isNmimsPartnerUniversityLDM').html("");
	 			$('#nmimsPartnerUniversityLDM').html("");
         	}
	    })
	});
	 
/*  file upload modal */	
	$('.lorFormatFileUploadData').on('click', function () {
        var lorFileUploadModal = $('#lorFormatFileUploadModal');
        var lorRegStaffId =  $(this).attr("data-lorRegStaffId");
        //alert(lorRegId+"   "+stUsername);
        $('#lorRegStaffId').val(lorRegStaffId);
    });
    $('.lorFormatFileUploadModal').on('hidden.bs.modal', function(e) {
    	$('#lorRegStaffId').val("");
    	$('#file').val("");
   	})

$('.lorDetailsModal').on('hidden.bs.modal', function(e) {
	$('.lorDetailsModal p').html('');
	$('.lorDetailsModal input').val('');
})
/*  expected date js */ 	
   	var TommorowDate = new Date();
   	var expectedDate = "";
   	var lorRegStaffId =  "";
	
	$('.initiateDatePicker').click(function(){
		lorRegStaffId =  $(this).attr("data-lorRegStaffId");
	
	})
		$('.initiateDatePicker').daterangepicker({
			autoUpdateInput : false,
			minDate : TommorowDate,
			locale : {
				cancelLabel : 'Clear'
			},
			"singleDatePicker" : true,
			"showDropdowns" : true,
			"showCustomRangeLabel" : false,
			"alwaysShowCalendars" : true,
			"opens" : "center"
		});
	
	$('.initiateDatePicker').on(
		'apply.daterangepicker',
		function(ev, picker) {

			var expectedDate = picker.startDate
					.format('YYYY-MM-DD');

			$
					.ajax({
						type : 'POST',
						dataType : 'JSON',
						url : '${pageContext.request.contextPath}/saveExpectedDate?expectedDate='
								+ expectedDate
								+ '&id='
								+ lorRegStaffId,
						success : function(data) {
							if (data.status = 'success') {
								$("#expectedDate" + lorRegStaffId)
										.val(expectedDate);
							}
						}
					})

		});

/* approval toggle btns */
	var appApproval = $('#appApproval').val();
	if (appApproval == 'Approve') {
		$('#appApproval').prop('checked', true);
	} else {
		$('#appApproval').prop('checked', false);
	}

	$('#appApproval').click(function() {
		var appApproval = $('#appApproval').val();
		if (appApproval == 'Approve') {
			$('#appApproval').val('Reject');
			$("#appRejectionReasonDiv").removeClass('d-none');
		} else {
			$('#appApproval').val('Approve');
			$("#appRejectionReasonDiv").addClass('d-none');
			$('#appRejectionReason').val("");
		}
	});
	var docApproval = $('#docApproval').val();
	if (docApproval == 'Approve') {
		$('#docApproval').prop('checked', true);
	} else {
		$('#docApproval').prop('checked', false);
	}

	$('#docApproval').click(function() {
		var docApproval = $('#docApproval').val();
		if (docApproval == 'Approve') {
			$('#docApproval').val('Reject');
			$("#docRejectionReasonDiv").removeClass('d-none');
		} else {
			$('#docApproval').val('Approve');
			$("#docRejectionReasonDiv").addClass('d-none');
			$('#docRejectionReason').val("");
		}
	});
	
	let lorApproval = $('#lorApproval').val();
	if (lorApproval == 'Approve') {
		$('#lorApproval').prop('checked', true);
	} else {
		$('#lorApproval').prop('checked', false);
	}

	$('#lorApproval').click(function() {
		let lorApproval = $('#lorApproval').val();
		if (lorApproval == 'Approve') {
			$('#lorApproval').val('Reject');
			$("#lorRejectionReasonDiv").removeClass('d-none');
		} else {
			$('#lorApproval').val('Approve');
			$("#lorRejectionReasonDiv").addClass('d-none');
			$('#lorRejectionReason').val("");
		}
	});

/* save all approval status */	
	$('#saveStatus').on('click', function(){
		console.log("save");
		let lrsId =  $(this).attr("data-lorRegStaffId");
		let approval =  $(this).attr("data-approval");
		let appApprovalText = $('#appApproval').val();
		let appReason = $('#appRejectionReason').val();
		let docApprovalText = $('#docApproval').val();
		let docReason = $('#docRejectionReason').val();
		let lorApprovalText = $('#lorApproval').val();
		let lorReason = $('#lorRejectionReason').val();
		console.log("appReason--->"+appReason)
		
		if(approval == "application"){
			if((appApprovalText == "Reject" && !appReason)){
				swal({
	   			  title: "Error",
	   			  text: "Please write Rejection Reason!!",
	   			  icon: "error",
	   			  button: "Ok",
	   			});
				return false;
			}else{
				let json = JSON.stringify({ 'id': lrsId,'appApproval': appApprovalText,'appRejectionReason': appReason });
				$.ajax({
			    	type : 'POST',
					dataType: 'JSON',
					data: { 
						json: json
					},
					url : '${pageContext.request.contextPath}/saveApplicationApprovalStatus',
		         	success : function(data) {
		         		if(data.status=="success"){
		         			swal({
			         			  title: "Success",
			         			  text: data.msg,
			         			  icon: "success",
			         			  button: "Ok",
			         			}).then((value) => {
			         				$('#lorDetailsModal').modal('hide');
			         			});
		         		}else{
		         			swal({
			         			  title: "Error",
			         			  text: data.msg,
			         			  icon: "error",
			         			  button: "Ok",
			         			});
		         		}
		         		
		         	},
		         	error: function(data){
		         		alert("Unable to save Application status.");
		         	}
				});
			}
		}else if(approval == "document"){
			if((docApprovalText == "Reject" && !docReason)){
				swal({
	   			  title: "Error",
	   			  text: "Please write Rejection Reason!!",
	   			  icon: "error",
	   			  button: "Ok",
	   			});
				return false;
			}else{
				let json = JSON.stringify({ 'id': lrsId,'docApproval': docApprovalText,'docRejectionReason': docReason });
				$.ajax({
			    	type : 'POST',
					dataType: 'JSON',
					data: { 
						json: json
					},
					url : '${pageContext.request.contextPath}/saveDocumentApprovalStatus',
		         	success : function(data) {
		         		if(data.status=="success"){
		         			swal({
			         			  title: "Success",
			         			  text: data.msg,
			         			  icon: "success",
			         			  button: "Ok",
			         			}).then((value) => {
			         				$('#lorDetailsModal').modal('hide');
			         			});
		         		}else{
		         			swal({
			         			  title: "Error",
			         			  text: data.msg,
			         			  icon: "error",
			         			  button: "Ok",
			         			});
		         		}
		         	},
		         	error: function(data){
		         		alert("Unable to save Document status.");
		         	}
				});
			}
			
		}else if(approval == "lor"){
			if((lorApprovalText == "Reject" && !lorReason)){
				swal({
	   			  title: "Error",
	   			  text: "Please write Rejection Reason!!",
	   			  icon: "error",
	   			  button: "Ok",
	   			});
				return false;
			}else{
				let json = JSON.stringify({ 'id': lrsId,'lorApproval': lorApprovalText,'lorRejectionReason': lorReason });
				$.ajax({
			    	type : 'POST',
					dataType: 'JSON',
					data: { 
						json: json
					},
					url : '${pageContext.request.contextPath}/saveLorApprovalStatus',
		         	success : function(data) {
		         		if(data.status=="success"){
		         			swal({
			         			  title: "Success",
			         			  text: data.msg,
			         			  icon: "success",
			         			  button: "Ok",
			         			}).then((value) => {
			         				$('#lorDetailsModal').modal('hide');
			         			});
		         		}else{
		         			swal({
			         			  title: "Error",
			         			  text: data.msg,
			         			  icon: "error",
			         			  button: "Ok",
			         			});
		         		}
		         	},
		         	error: function(data){
		         		alert("Unable to save LOR status.");
		         	}
				});
			}
		}
	});
	
	$(".table").DataTable().destroy();
	var table = $('.table').DataTable({
		//"dom" : '<"top"i>rt<"bottom"flp><"clear">',
		"lengthMenu" : [ [ 10, 25, 50, -1 ],
				[ 10, 25, 50, "All" ] ],
		
		
		initComplete : function() {
			this.api().columns().every(function() {
				var column = this;
				var headerText = $(column.header()).text();
				if (headerText == "Sr. No." || headerText == "Select To Allocate")
					return;
				var select = $('<select class="form-control"><option value="">All</option></select>')
						.appendTo($(column.footer()).empty())
						.on('change',function() {
							var val = $.fn.dataTable.util.escapeRegex($(this).val());
							column.search(val ? '^' + val + '$' : '',true,false).draw();
				});

				column.data().unique().sort().each(function(d, j) {
					select.append('<option value="' + d + '">' + d + '</option>')
				});
			});
		}
	});
	
	$('#lorFormatFile').bind('change',
			function() {
		//let fileInput = document.getElementById('file');
		let fileInput = $('#lorFormatFile');
        //console.log("fileInput--->",fileInput);
        var lg = fileInput[0].files.length; // get length
        var items = fileInput[0].files;
        for (var i = 0; i < lg; i++) {
        	//console.log("fileInput--->",items[i].name);
        	let filename = items[i].name;
    	   // Allowing file type
	        let allowedExtensions = /(\.doc|\.docx|\.pdf)$/i;
	        if (!allowedExtensions.exec(filename)) {
	            alert('Only DOC or PDF file allowed!!');
	            fileInput.val('');
	            return false;
	        } 
        }
			});
</script>


