<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>	
<jsp:include page="../common/newDashboardHeader.jsp" /> 

<div class="d-flex adminPage" id="adminPage">
<jsp:include page="../common/newAdminLeftNavBar.jsp"/>
<jsp:include page="../common/rightSidebarAdmin.jsp" />


<!-- DASHBOARD BODY STARTS HERE -->

<div class="container-fluid m-0 p-0 dashboardWraper">

	<jsp:include page="../common/newAdminTopHeader.jsp" />
     
            <!-- SEMESTER CONTENT -->    
                    <div class="col-lg-12 col-md-12 col-sm-12 dashboardBody p-5">
                        
                        	<!-- page content: START -->

						<nav aria-label="breadcrumb">
                    <ol class="breadcrumb">
                        <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
                        <li class="breadcrumb-item" aria-current="page">
                            <c:out value="${Program_Name}" />
                        </li>
                        <sec:authorize access="hasRole('ROLE_STUDENT')">
                            <c:out value="${AcadSession}" />
                        </sec:authorize>
                        <li class="breadcrumb-item active" aria-current="page"> De-Allocate Feedback</li>
                    </ol>
                </nav>
						<jsp:include page="../common/alert.jsp" />

						<!-- Input Form Panel -->
						<div class="card bg-white border">
							<div class="card-body">

										<h5 class="border-bottom text-center pb-2">De-Allocate Feedback</h5>
										
										<%-- <c:if test="${showProceed}">
										<div class="col-12 text-right">
											<a href="addStudentFeedbackForm"><i
												class="btn btn-large btn-primary">Proceed
													to allocate students</i></a>
													</div>
										</c:if> --%>

										<form:form method="post"
											modelAttribute="feedback">
											<h5 class="border-bottom pb-2 mt-5">Feedback De-Allocation Details</h5>
											<div class="table-responsive testAssignTable">
												<table class="table table-striped table-hover" id="studentFeedbackTable">
													<thead>
														<tr>
															<th>Sr. No.</th>
															<th><input name="select_all" value="1"
																	id="example-select-all" type="checkbox" />Select to De-allocate</th>
															<!-- <th>Feedback Status</th> -->

															<th>SAPID</th>
															<th>Faculty Name</th>
															<th>Course Name</th>
															<th>Acad Year</th>
															<th>Faculty Status</th>
															<!-- <th>View Completed Feedback</th> -->
														</tr>
													</thead>
													<tbody>
													<c:forEach var="faculty" items="${facultyList}"
															varStatus="status">
															<tr>
															<td><c:out value="${status.count}"/></td>
															<td><form:checkbox path="facultyId" value="${faculty.facultyId}_${faculty.courseId}"/></td>
															<td><c:out value="${faculty.facultyId}" /></td>
															<td><c:out value="${faculty.facultyName}" /></td>
															<td><c:out value="${faculty.courseName}" /></td>
															<td><c:out value="${faculty.acadYear}" /></td>
															<td>
																<c:choose>
																	<c:when test = "${faculty.enabled eq 1}">
																	Active
		            												</c:when>
																
																	<c:when test = "${faculty.enabled eq 0}">
																	Inactive
		            												</c:when>
		            												<c:otherwise>
		            												Invalid User
		            												</c:otherwise>
																</c:choose>
															</td>
															</tr>
													</c:forEach>
													</tbody>
													<!-- <tfoot>
														<tr>
															<th></th>
															<th></th>
															<th></th>
															<th></th>
															<th>Program</th>
															<th>Student Name</th>
															<th>Course Name</th>
															<th>Acad Year</th>
															<th>Acad Session</th>

														</tr>
													</tfoot> -->
													
												</table>
											</div>
											<div class="form-group">
												<sec:authorize access="hasRole('ROLE_ADMIN')">
													<c:url value="removeFacultyFeedback"
														var="removeFacultyFeedbackURL">
													</c:url>
													<c:if test="${fn:length(facultyList) gt 0}">
														<button id="submit" type="button" class="btn btn-large btn-primary"
															onclick="clicked();">
															De-Allocate Feedback</button>
													</c:if>
												</sec:authorize>

											</div>
							</form:form>
							</div>
						</div>

			<!-- /page content: END -->
                     
                   
                    </div>
			
			<!-- SIDEBAR START -->

	        <!-- SIDEBAR END -->
	<jsp:include page="../common/newAdminFooter.jsp"/>
	
	<script>
	function clicked() {
		var name = document.querySelectorAll('input[type="checkbox"]:checked').length
				+ " " + "Faculty selected";
		console.log("Faculty" + name);

		var str = new Array();
		var feedbackId =${feedback.id};	

		var checked = document
				.querySelectorAll('input[type="checkbox"]:checked');

		for (var i = 0; i < checked.length; i++) {
			if (checked.length == 1) {
				str = checked[i].value;
			} else {
				if (i == checked.length - 1) {
					str += checked[i].value;
				} else {
					str += checked[i].value + ",";
				}

			}
		}
		console.log("str------>" + str);

		 $.ajax({
					url : "${pageContext.request.contextPath}/removeFacultyFeedback?ids="
						+ str+"&feedbackId="+feedbackId,
					type : "POST",
					dataType : 'json',
					data : str,
					beforeSend : function() {
					},
					success : function(data, status, jqXHR) {
						
						console.log("success");
					},
					error : function(jqXHR, status, err) {
						console.log("error " + jqXHR +" => "+ status +" => "+ err);
					},
					complete : function(jqXHR, status) {
						//alert("status"+status);
						window.location.reload();
						//$('.card').prepend('<h5 style="padding:5px;" class="bg-success right-success">Rights applied successfully.</h5>');
						//window.location = "${pageContext.request.contextPath}/removeFacultyFeedbackForm?feedbackId="+feedbackId;
					}
				}); 

		console.log("checked asadws");
				//alert("");
		return confirm(name);
		//return str;

	}
	
	
  var table = $('.table').DataTable();
  $('#example-select-all').on(
			'click',
			function() {
				// Check/uncheck all checkboxes in the table
				var rows = table.rows({
					'search' : 'applied'
				}).nodes();
				$('input[type="checkbox"]', rows).prop('checked',
						this.checked);
			});

</script>