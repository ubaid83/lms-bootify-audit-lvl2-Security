
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>



<jsp:include page="../common/newDashboardHeader.jsp" />

<div class="d-flex" id="facultyAssignmentPage">

	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper">


		<header class="container-fluid sticky-top">
			<nav class="navbar navbar-expand-lg navbar-light p-0">
				<!--                     <i class="fas fa-bars fa-2x mr-3 leftnav-btn" data-toggle="modal" data-target="#leftnav"></i> -->
				<a class="navbar-brand" href="searchTestListBySupportAdminForm"> <c:choose>
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
							data-placement="bottom" title="My Program"><a
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

					<sec:authorize access="hasRole('ROLE_STUDENT')">
						<c:out value="${AcadSession}" />
					</sec:authorize>
					<li class="breadcrumb-item active" aria-current="page"> Student Test
						List</li>
				</ol>
			</nav>

			<jsp:include page="../common/alert.jsp" />

			<!-- Input Form Panel -->
			<div class="card bg-white border">
				<div class="card-body">
					<h5 class="text-center pb-2 border-bottom">Student Test List</h5>



					<div class="table-responsive mt-3 mb-3 testAssignTable">
						<table class="table table-striped table-hover" id="studentTableId">
							<thead>
								<tr>
									<th>Student Test Id</th>
									<th>Username</th>
									<th>Test Start Time</th>
									<th>Test End Time</th>
									<th>Test Completed</th>
									<th>Duration Completed</th>
									<th>Score</th>
									<th>Last Modified By</th>
									<th>Action</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="studentTList" items="${studentTList}"
									varStatus="status">
									<tr>


										<td>${studentTList.id}</td>
										<td>${studentTList.username}</td>

										<td>${studentTList.testStartTime}</td>
										<td>${studentTList.testEndTime}</td>
										<td>${studentTList.testCompleted}</td>
										<td>${studentTList.durationCompleted}</td>
										<td>${studentTList.score}</td>
										
										<td>${studentTList.lastModifiedBy}</td>
												<td>
													<c:choose>
													<c:when test="${studentTList.testType eq 'Objective'}">
													<button class="btn btn-success rounded-0 text-light objectiveTestUrl" data-obTest-id="${studentTList.id}" >Complete Test</button>
													</c:when>
									
													<c:when test="${studentTList.testType eq 'Subjective'}">  
													<button class="btn btn-success rounded-0 text-light subjectiveTestUrl" data-subTest-id="${studentTList.id}" >Complete Test</button>
													</c:when>
													</c:choose>
													</td>
									</tr>

								</c:forEach>
							</tbody>
							
						</table>
					</div>
				</div>
			</div>



			<!-- /page content: END -->

		</div>

		<!-- SIDEBAR START -->

		<!-- SIDEBAR END -->

		<jsp:include page="../common/newAdminFooter.jsp" />

<script>
$(document).ready(function(){
	
	
	   $("#studentTableId").on("click", ".objectiveTestUrl", function(){
		   
		   console.log("Id ---->>>>", $(this).attr("data-obTest-id"))
		   
		  	let objectiveTestId = $(this).attr("data-obTest-id");
		  	
			   console.log('objectiveTestId---------------------------->',objectiveTestId)
		    $.ajax({
		           url: "${pageContext.request.contextPath}/api/completeStudentTestAjaxForObjectiveExt",
		       	type: "POST",
		           data: {
		           	"studentTestId": objectiveTestId
		           },
		           success: function (data) {
		        	   
		        	   console.log('data-------------->',data)
		        	  
		        	   if(data=='Success'){
		        		   
		        		  
		        		   alert('SuccessFully Submitted')
		        		   location.reload();
		        	   }
		        	   else
	        		   {
		        		   alert('Fail')
		        		}		        	
			           }
		       });
	   })
	
	   
	 //Subjective Test
	   $("#studentTableId").on("click", ".subjectiveTestUrl", function(){
		   
		   console.log("Id ---->>>>", $(this).attr("data-subTest-id"))
		   
		  	let subjectiveTestId = $(this).attr("data-obTest-id");
		  	
			   console.log('subjectiveTestId---------------------------->',subjectiveTestId)
		    $.ajax({
		           url: "${pageContext.request.contextPath}/api/completeStudentTestAjaxForSubjectiveExt",
		       	type: "POST",
		           data: {
		           	"studentTestId": subjectiveTestId
		           },
		           success: function (data) {
		        	   
		        	   console.log('data-------------->',data)
		        	  
		        	   if(data=='Success'){
		        		   
		        		  
		        		   alert('SuccessFully Submitted')
		        		   location.reload();
		        	   }
		        	   else
	        		   {
		        		   alert('Fail')
		        		}		        	
			           }
		       });
	   })
 
})
</script>

		<script>
			$('input[type="checkbox"]').click(function() {
				if ($(this).prop("checked") == true) {
					console.log('checked!!!');
					var id = $(this).attr('id');
					$('#mark' + id).prop('disabled', false);
					$('#mark' + id).prop('required', true);
				} else if ($(this).prop("checked") == false) {
					var id = $(this).attr('id');
					$('#mark' + id).prop('disabled', true);
					$('#mark' + id).prop('required', false);
					$('#mark' + id).val('');
				}
			});
		</script>