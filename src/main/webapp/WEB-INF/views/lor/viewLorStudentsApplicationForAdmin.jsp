<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%
	boolean isEdit = "true".equals((String) request
			.getAttribute("edit"));
%>


<jsp:include page="../common/newDashboardHeader.jsp" />

<div class="d-flex" id="facultyAssignmentPage">



	<!-- DASHBOARD BODY STARTS HERE -->

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

		<!-- SEMESTER CONTENT -->
		<div class="col-lg-12 col-md-12 col-sm-12 dashboardBody p-5">

			<!-- page content: START -->
			<nav aria-label="breadcrumb">
				<ol class="breadcrumb">
					<li class="breadcrumb-item"><a
						href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
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
										<th>Student Sap Id</th>
										<th>Department</th>
										<th>Faculty Name</th>
										<th>Expected Date</th>
										<th>No. Of Copies</th>
										<th>LOR Format</th>
										<th>Action</th>
									</tr>
								</thead>

								<tbody>

									<c:forEach var="student" items="${lorApplicationList}"
										varStatus="status">
										<tr>
											<td><c:out value="${status.count}"/></td>
											<td>${student.username}</td>
											<td>${student.department}</td>
											<td>${student.fullName}</td>
											<td><c:out value="${fn:substring(student.expectedDate, 0, 10)}"/></td>
											<td>${student.noOfCopies}</td>
											<td><c:url value="downloadLorFiles" var="downloadurl">
													<c:param name="id" value="${student.id}" />
													<c:param name="fileType" value="lorFormatFilePath" />
												</c:url>
									           <a href="${downloadurl}" title="Download"><i
													class="fa fa-download" aria-hidden="true">Download</i></a></td>
											<td>
												<c:if test="${empty student.lorApproval && not empty student.finalLorFilePath}">
													<b>Approval Pending</b>
												</c:if>
												<c:if test="${empty student.lorApproval && empty student.finalLorFilePath && not empty student.lorFormatFilePath}">
									           	<a href="#" title="Upload"><i
													class="fa fa-upload lorFormatFileUploadData" aria-hidden="true"
													style="font-size: medium;" data-lorRegStaffId="${student.id}" 
													data-toggle="modal" data-target="#lorFormatFileUploadModal">Upload Final LOR</i></a>&nbsp;
										        </c:if>
  										     	<c:if test="${student.lorApproval eq 'Reject'}">
	       										<b>	Application is Rejected by Faculty Please Upload Again.</b>
	       										<span data-toggle="tooltip" data-placement="top" title="Reason: ${student.lorRejectionReason}">
                                						<i  class="fa fa-info-circle" aria-hidden="true"></i></span>
	       										<a href="#" title="Upload"><i
													class="fa fa-upload lorFormatFileUploadData" aria-hidden="true"
													style="font-size: medium;" data-lorRegStaffId="${student.id}" 
													data-toggle="modal" data-target="#lorFormatFileUploadModal">Upload Final Lor</i></a>&nbsp;
	       										</c:if>
	       										
										   
										        
										        
											</td>
											
											
											<%-- <td>
										        <c:if test = "${not empty student.finalLorFilePath}">
										        <c:url value="downloadLorFiles" var="downloadurl">
													<c:param name="id" value="${student.lorRegId}" />
													<c:param name="fileType" value="finalLorFilePath" />
												</c:url>
										           <a href="" title="Download"><i
													class="fa fa-download" aria-hidden="true"
													style="font-size: medium;"></i></a>
										        </c:if>
											</td>
											<td>
										        <c:if test = "${not empty student.finalLorFilePath}">
										           <a href="#" class="editable" id="lorApproval"
													data-type="select" data-pk="${student.lorRegStaffId}"
													data-source="[{value: 'Approve', text: 'Approve'},{value: 'Reject', text: 'Reject'}]" 
													data-url="saveLORApprovalStatus"
													data-title="LOR Status">${student.lorApproval}</a>
										        </c:if>
											</td>
											<td>
												<a href="#" title="Details" data-lorRegId="${student.lorRegId}" class="lorDetailsModalBtn"
													data-toggle="modal" data-target="#lorDetailsModal">
													<i class="fas fa-info-circle"></i> </a>
											</td> --%>
										</tr>
									</c:forEach>
								</tbody>
								<tfoot>
									<th></th>
									<th></th>
									<th></th>
									<th></th>
									<th></th>
								</tfoot>
							</table>
						</div>


					</div>
				</div>
			</div>

			<!-- /page content: END -->

		</div>

		<!-- SIDEBAR START -->

		<!-- SIDEBAR END -->

		<jsp:include page="../common/newAdminFooter.jsp" />
<div id="lorFormatFileUpload">
<div class="modal fade fnt-13"
	id="lorFormatFileUploadModal" tabindex="-1" role="dialog"
	aria-labelledby="lorFormatFileUploadModal" aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered modal-dialog-scrollable"
		role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" >Upload Final LOR  File</h5>
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<form:form action="uploadLORFinalFile" id="uploadLORFinalFile"
					method="post" enctype="multipart/form-data" modelAttribute="lorRegStaff">
			<form:hidden id="lorRegStaffId" path="id" />
			
			<div class="modal-body">
					<div class="row">
						<div class="col-12">
							<h6>Upload file</h6>
							<input type="file" name="file" class="form-control-file mb-1" id="finalLorFile" multiple>
						</div>
					</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-modalClose"
					data-dismiss="modal">Close</button>
				<button id="submitBtn" class="btn btn-modalSub" name="submitBtn" 
						formaction="uploadLORFinalFile">Submit</button>
			</div>
			</form:form>
		</div>
	</div>
</div>
</div>	


<<script>
	
	
	$('.lorFormatFileUploadData').on('click', function () {
        var lorFileUploadModal = $('#lorFormatFileUploadModal');
        var lorRegStaffId =  $(this).attr("data-lorRegStaffId");
        //alert(lorRegId+"   "+stUsername);
        $('#lorRegStaffId').val(lorRegStaffId);
        
        
    });
    $('.lorFormatFileUploadModal').on('hidden.bs.modal', function(e) {
    	$('#lorRegStaffId').val("");
    	$('#finalLorFile').val("");
   	})
   	
	$('#finalLorFile').bind('change',
		function() {
			//let fileInput = document.getElementById('file');
			let fileInput = $('#finalLorFile');
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
		
		
		