<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<jsp:include page="../common/newDashboardHeader.jsp" />

<div class="d-flex" id="facultyAssignmentPage">
	<%-- <jsp:include page="../common/newAdminLeftNavBar.jsp" /> --%>
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
					<li class="breadcrumb-item active" aria-current="page">Term End Exam Evaluation
						List</li>
				</ol>
			</nav>

			<jsp:include page="../common/alert.jsp" />

			<!-- Input Form Panel -->
			<div class="card bg-white border">
				<div class="card-body">
					<h5 class="text-center pb-2 border-bottom">Term End Exam Evaluation List</h5>

				

					<div class="table-responsive mt-3 mb-3 testAssignTable">
						<table class="table table-striped table-hover">
							<thead>
								<tr>
									<th scope="col">Sl. No.</th>
									<th scope="col">TEE Name</th>
									<th scope="col">TEE Description</th>
									<th scope="col">Program</th>
									<th scope="col">Module Name</th>
									<th scope="col">Assign Faculty</th>
									<th scope="col">Acad Session</th>
									<th scope="col">Acad Year</th>
									<th scope="col">Start Date</th>
									<th scope="col">End Date</th>
									<th scope="col">Campus</th>
									<th scope="col">Total External Marks</th>
									<th scope="col">Status</th>
									<th scope="col">Actions</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="tee" items="${teeList}" varStatus="status">
									<tr>
										<td><c:out value="${status.count}" /></td>
										<td><c:out value="${tee.teeName}" /></td>
										<td><c:out value="${tee.teeDesc}" /></td>
										<td><c:out value="${tee.programName}" /></td>

										<c:if test="${tee.courseName ne null}">
											<td><c:out value="${tee.moduleName}(${tee.courseName})" /></td>
										</c:if>
										<c:if test="${tee.courseName eq null}">
											<td><c:out value="${tee.moduleName}" /></td>
										</c:if>
										<td><c:out value="${tee.facultyName}" /></td>
										<td><c:out value="${tee.acadSession}" /></td>

										<td><c:out value="${tee.acadYear}" /></td>
										<td><c:out value="${tee.startDate}" /></td>
										<td><c:out value="${tee.endDate}" /></td>
										<td><c:out value="${tee.campusName}" /></td>
										<td><c:out value="${tee.externalMarks}" /></td>
                                        <c:choose>
                                            <c:when test="${tee.isSubmitted eq 'Y'}">
                                                <td><c:out value="Evaluated" /></td>
                                            </c:when>
                                            <c:otherwise>
                                                <td><c:out value="Pending" /></td>
                                            </c:otherwise>
                                        </c:choose>
										<td><c:url value="deleteTee" var="deleteurl">
												<c:param name="id" value="${tee.id}" />
											</c:url>
											<c:url
												value="/addTeeForm" var="editurl">
												<c:param name="id" value="${tee.id}" />
											</c:url>
								         <c:url value="/evaluateTee" var="evaluateurl">
												<c:param name="teeId" value="${tee.id}" />
												</c:url>
                                        <c:url value="/showEvaluatedTeeMarks"
                                            var="showTeeMarks">
                                            <c:param name="teeId" value="${tee.id}" />
                                        </c:url>
                                        <c:url value="/searchTeeList"
													var="teeListDivision">
													<c:param name="teeId" value="${tee.id}" />
												</c:url>
										<c:url value="/createStudentGroupFormForTee"
													var="addStudentsFacultyWise">
													<c:param name="id" value="${tee.id}" />
												</c:url>		
											 <sec:authorize access="hasRole('ROLE_ADMIN')">
												<a href="${deleteurl}" title="Delete"
													onclick="return confirm('Are you sure you want to delete this record?')"><i
													class="fas fa-trash-alt fa-lg text-danger"></i></a>
												
												<a href="${editurl}" title="Edit"><i
													class="fas fa-edit fa-lg text-primary"></i></a>
												<c:if test="${fn:contains(tee.assignedFaculty, ',')}">
												<a href="${addStudentsFacultyWise}" title="Add Students FacultyWise"><i
													class="fa fa-cog fa-lg"></i></a>
												<a href="#" id="evaluationStatusOfFaculty" data-id="${tee.id}" title="Evaluation Status Of Faculty"><i class="fas fa-tasks"></i></a>
												</c:if>
												<c:if test="${tee.isTeeDivisionWise eq 'Y' && empty tee.parentTeeId}">
												<a href="${teeListDivision}" title="Show All Divisions TEE"><i
													class="fa fa-users fa-lg"></i></a>
												</c:if>
												<c:if test="${tee.isApproved eq 'Y'}">
													<a href="${evaluateurl}" title="Evaluate TEE"><i
														class="fas fa-check-square"></i></a>
												</c:if>
												<c:if test="${tee.isSubmitted eq 'Y' && tee.isTeeDivisionWise ne 'Y'}">
													<a href="${showTeeMarks}" title="Show TEE Marks"><i
														class="fa fa-info-circle fa-lg"></i></a>
											    </c:if>
											    <c:if test="${tee.isSubmitted eq 'Y' && tee.isTeeDivisionWise eq 'Y' && not empty tee.parentTeeId }">
													<a href="${showTeeMarks}" title="Show TEE Marks"><i
														class="fa fa-info-circle fa-lg"></i></a>
											    </c:if>
											</sec:authorize>
											<!--<c:if test="${tee.isNonEventModule eq 'Y'}">
												<sec:authorize access="hasRole('ROLE_ADMIN')">
													<a href="${evaluateIcaStudentsByAdmin}" title="Evaluate ICA"><i
														class="fas fa-check-square"></i></a>

												</sec:authorize>
											</c:if>-->
											</td>
									</tr>
								</c:forEach>
							</tbody>
							<tfoot>
								<tr>
									<th>--</th>
									<th>--</th>
									<th>--</th>
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



			<!-- /page content: END -->

		</div>

		<!-- SIDEBAR START -->

		<!-- SIDEBAR END -->

		<jsp:include page="../common/newAdminFooter.jsp" />

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
			$("#evaluationStatusOfFaculty").on('click', function(){
				let id = $(this).attr('data-id');
				console.log("id--->"+id)
				$.ajax({
					type : 'GET',
					url : "${pageContext.request.contextPath}/getEvaluationStatusOfTEEByFaculty?id="+id,
					success : function(data) {
						console.log("data-->"+data)
						var json = JSON.parse(data);
						let facultyStatusString = "";
						for (var i = 0; i < json.length; i++) {
							var obj = json[i];
							console.log("obj--->"+obj.assignedFaculty)
							facultyStatusString += obj.assignedFaculty + " - ";
							if(obj.saveAsDraft != null){
								facultyStatusString += "Draft Mode."
							}else
							if(obj.finalSubmit == "Y"){
								facultyStatusString += "Evaluated."
							}else
							if(obj.saveAsDraft == null && obj.finalSubmit == null){
								facultyStatusString += "Pending."
							}
							facultyStatusString += "\n"
						}
						console.log("facultyStatusString--->"+facultyStatusString)
						swal('INFO!', facultyStatusString);

					}
				});
					
			});
			
		</script>