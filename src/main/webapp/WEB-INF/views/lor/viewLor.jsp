<html>
<head>
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
</head>
<body>
	<jsp:include page="../common/newDashboardHeader.jsp" />

	<div class="d-flex dataTableBottom" id="facultyAssignmentPage">
		<sec:authorize access="hasRole('ROLE_FACULTY')">
			<jsp:include page="../common/newLeftNavBarFaculty.jsp" />
			<jsp:include page="../common/newLeftSidebarFaculty.jsp" />
			<jsp:include page="../common/rightSidebarFaculty.jsp" />
		</sec:authorize>
		<sec:authorize access="hasRole('ROLE_STUDENT')">
			<jsp:include page="../common/newLeftNavbar.jsp" />
			<jsp:include page="../common/newLeftSidebar.jsp" />
			<jsp:include page="../common/rightSidebar.jsp" />
		</sec:authorize>
	</div>
	<%
		boolean isEdit = "true".equals((String) request.getAttribute("edit"));
	%>


	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper">

		<sec:authorize access="hasRole('ROLE_FACULTY')">
			<jsp:include page="../common/newTopHeaderFaculty.jsp" />
		</sec:authorize>
		<sec:authorize access="hasRole('ROLE_STUDENT')">
			<jsp:include page="../common/newTopHeader.jsp" />
		</sec:authorize>

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
							<li class="breadcrumb-item active" aria-current="page">Lor
								Application</li>
						</ol>
					</nav>

					<jsp:include page="../common/alert.jsp" />


					<!-- Input Form Panel -->
					<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">
									<div class="x_content">
										<table class="table table-hover" id="POITable">
											<thead>
												<tr>
													<td><b>Sr No</b></td>
													<td><b>Application Id</b></td>
													<td><b>Department</b></td>
													<td><b>Assigned Faculty</b></td>
													<td><b>No of Hard Copy</b></td>
													<td><b>Status</b></td>

												</tr>
											</thead>
											<tbody>
												<c:forEach items="${lorRegStaff}" var="list" varStatus="status">
													<tr>
														<td>${status.count}</td>
														<td>${list.lorRegId}</td>
														<td>${list.department}</td>
														<td>${list.name}</td>
														<td>${list.noOfCopies}</td>
														<td>
											
										<c:if test="${list.appApproval eq 'Approve' }">
											<c:if test="${empty list.lorDocsFilePath && empty list.docApproval}">
												<a data-toggle="modal" data-lorRegId="${list.lorRegId}"
													data-id="${list.id}" data-staff-id="${list.staffId}"
													title="Add this item"
													class="fa fa-upload open-AddBookDialog"
													href="#addBookDialog">Upload documents </a> 
											</c:if>
											<c:if test="${empty list.lorDocsFilePath && list.docApproval eq 'Reject'}">
												<b>	Document Rejected Please upload Again.</b>
												<span data-toggle="tooltip" data-placement="top" title="Reason: ${list.docRejectionReason}">
                                						<i  class="fa fa-info-circle" aria-hidden="true"></i></span>
												<a data-toggle="modal" data-lorRegId="${list.lorRegId}"
													data-id="${list.id}" data-staff-id="${list.staffId}"
													title="Add this item"
													class="fa fa-upload open-AddBookDialog"
													href="#addBookDialog">Upload documents </a>
												<br>
											</c:if>
											<c:if test="${not empty list.lorDocsFilePath}">
												<c:if test="${empty list.docApproval}">
													<b>Document Under Verification.</b>
												</c:if>
												<c:if test="${list.docApproval eq 'Approve'}">
													<c:if test="${list.lorApproval eq 'Approve'}">
														<c:url value="downloadLorFiles" var="downloadurl">
															<c:param name="id" value="${list.id}" />
															<c:param name="fileType" value="finalLorFilePath" />
														</c:url>
										          		 <a href="${downloadurl}" title="Download">
										          		 <i class="fa fa-download" aria-hidden="true"style="font-size: medium;">
										          		 Download Final LOR</i></a>
													</c:if>
										 			<c:if test="${empty list.lorApproval || list.lorApproval eq 'Reject'}">
														Your Documents  are Approved  and Application pending under Administration office 
													</c:if>  
												</c:if>
												<c:if test="${list.docApproval eq 'Reject'}">
													
												</c:if>
											</c:if>
	 									</c:if>
										<c:if test="${empty list.appApproval }">
											Application Pending for Approval
										</c:if>
										<c:if test="${list.appApproval eq 'Reject'}">
											Application is rejected 
											<span data-toggle="tooltip" data-placement="top" title="Reason: ${list.appRejectionReason}">
                                						<i  class="fa fa-info-circle" aria-hidden="true"></i></span>
									 	</c:if>	
														</td>
													</tr>
												</c:forEach>
											</tbody>
										</Table>

						<div class="modal" id="addBookDialog">
						  <div class="modal-dialog">
						    <div class="modal-content">
						
						      <!-- Modal Header -->
						      <div class="modal-header">
						        <h4 class="modal-title">Upload Students Documents</h4>
						        <button type="button" class="close" data-dismiss="modal">&times;</button>
						      </div>
						
						      <!-- Modal body -->
						      <div class="modal-body">
						       <form action="uploadStudentDocuments" method="post" enctype="multipart/form-data">
						       <div class="row pl-3 pr-3">
							       <input type="hidden" name="id" id="id" />
							       <input type="hidden" name="lorRegId" id="lorRegId" />
							       <input type="hidden" name="staffId" id="staffId" />
							       <input type="file" name="file" id="file" multiple>
						       </div>
						       <div class="row mt-3 pl-3 pr-3">
						       <p><strong>Upload following documents to proceed with the application:</strong></p>
							       <ul>
							       <li>Soft copy of all mark sheets of all semesters.</li>
							       <li>Soft copies of all certificates of co-curricular/extra-curricular activities participated in.</li>
							       <li>Certificated of all awards received.</li>
							       <li>Brief note about the student's prominent achievements/Activities: in Academics, sports, Technical projects, Internships, Social activities involved in, Communication /soft skills. [All Relevant certificates must be uploaded on the portal.</li>
							       </ul>
						       </div>
						      <div class="modal-footer">
						        <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
						        <button type="submit" class="btn btn-modalSub">Upload</button>
						      </div>
						      
						       </form>
						      </div>
				      <!-- Modal footer -->
				    </div>
				  </div>
				</div>				
							</div>
						</div>
					</div>
				</div>
			</div> 

					<!-- Results Panel -->

					


					<%-- 	<jsp:include page="../common/paginate.jsp">
							<jsp:param name="baseUrl" value="searchFacultyGroups" />
						</jsp:include> --%>

					<!-- /page content: END -->

				</div>

				<!-- SIDEBAR START -->
				<jsp:include page="../common/newSidebar.jsp" />
				<!-- SIDEBAR END -->
				<jsp:include page="../common/footer.jsp" />

<script type="text/javascript">
	$(document).on("click", ".open-AddBookDialog", function() {
		let id = $(this).attr("data-id")
		let lorregId = $(this).attr("data-lorregId")
		let staffId = $(this).attr("data-staff-id")
		console.log("lorregId------", lorregId);
		console.log("staffId--------", staffId);
		//	var lorregid = $(this).data('id');

		$(".modal-body #lorRegId").val(lorregId);
		$(".modal-body #staffId").val(staffId);
		$(".modal-body #id").val(id);

	});
	 $('#addBookDialog').on('hidden.bs.modal', function(e) {
		 $(".modal-body #lorRegId").val("");
			$(".modal-body #staffId").val("");
			$(".modal-body #id").val("");
	    	$('#file').val("");
	   	});
	$("#previous").on('click', function() {

		$("#lor1").removeClass('d-none');
		$("#lor2").addClass('d-none');

	});
	$("#next").on('click', function() {

		$("#lor1").addClass('d-none');

		$("#lor2").removeClass('d-none');

	});
	
	$('#file').bind(
			'change',
			function() {
				//let fileInput = document.getElementById('file');
				let fileInput = $('#file');
		        //console.log("fileInput--->",fileInput);
		        var lg = fileInput[0].files.length; // get length
	            var items = fileInput[0].files;
	            for (var i = 0; i < lg; i++) {
	            	console.log("fileInput--->",items[i].name);
	            	let filename = items[i].name;
            	   // Allowing file type
			        let allowedExtensions = /(\.doc|\.docx|\.pdf)$/i;
			        if (!allowedExtensions.exec(filename)) {
			            alert('Invalid file type!!');
			            fileInput.val('');
			            return false;
			        } 
                }
			});
</script>
														</body>
</html>						