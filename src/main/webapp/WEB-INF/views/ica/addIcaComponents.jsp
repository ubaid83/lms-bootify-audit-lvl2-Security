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
					<li class="breadcrumb-item active" aria-current="page">Add ICA
						Components</li>
				</ol>
			</nav>

			<jsp:include page="../common/alert.jsp" />

			<!-- Input Form Panel -->
			<div class="card bg-white border">
				<div class="card-body">
					<h5 class="text-center pb-2 border-bottom">Add ICA Components
					</h5>

					<div class="col-12">
						<h5>Evaluation Details:</h5>
						<hr />
					</div>

					<div class="col-12 mt-3">
						<strong>ICA Name:</strong> <span>${ica.icaName}</span>
					</div>
					
					<div class="col-12 mt-3">
						<strong>Program:</strong> <span>${programName}</span>
					</div>
					
					<div class="col-12 mt-3">
						<strong>Subject:</strong> <span>${moduleName}</span>
					</div>
					<div class="col-12 mt-3">
						<strong>Year:</strong> <span>${ica.acadYear}</span>
					</div>
					<div class="col-12 mt-3">
						<strong>Acad Session:</strong> <span>${ica.acadSession}</span>
					</div>
					<div class="col-12 mt-3">
						<strong>Internal Marks:</strong> <span>${ica.internalMarks}</span>
					</div>
					<div class="col-12 mt-3">
						<strong>Internal Passing Marks:</strong> <span>${ica.internalPassMarks}</span>
					</div>
				</div>
			</div>


			<div class="card bg-white border">
				<div class="card-body">

					<form:form action="saveComponentsForIca" id="saveComponentsForIcaForm"
						method="post" modelAttribute="ica">
						<form:input path="id"
														type="hidden" id="idIca" value="${ica.id}" />
						<!--FORM ITEMS STARTS HERE-->
						<div class="col-12 mt-5">
							<h5>Select Components:</h5>
							<hr />
						</div>
						<div class="table-responsive">
							<table class="w-100 table-striped">
								<tbody>
									<c:forEach var="component" items="${componentList}"
										varStatus="status">
										<c:choose>
									
											<c:when test="${component.icaCompId eq null}">
												
												<tr>
													<form:input path="componentList[${status.index}].icaId"
														type="hidden" id="iceId" value="${ica.id}" />
													<td class="pl-2"><form:checkbox
															path="componentList[${status.index}].componentId"
															id="${component.componentId}"
															value="${component.componentId}" class="checkboxClass" /></td>
													<td class="pl-2"><label for="assignmentchk">${component.componentName}</label></td>
													<td class="pl-2"><form:input
															class="form-control border-0 marks"
															id="mark${component.componentId}" type="number"
															path="componentList[${status.index}].marks"
															placeholder="Enter marks" disabled="true" step=".01"/>
															
															
															</td>
															
															<td class="pl-2"><form:input
															class="form-control border-0 inputSeq"
															id="sequenceNo${component.componentId}" type="number"
															path="componentList[${status.index}].sequenceNo"
															 value="${component.sequenceNo}"
															placeholder="Sequence No" disabled="true" min="1" />
															
															
															</td>

												</tr>
											</c:when>
											<c:otherwise>
	
												<tr>
													<form:input path="componentList[${status.index}].icaId"
														type="hidden" id="iceId" value="${ica.id}" 	/>
													<td class="pl-2"><form:checkbox
															path="componentList[${status.index}].componentId"
															id="${component.componentId}"
															value="${component.componentId}" class="checkboxClass" checked="true"/></td>
													<td class="pl-2"><label for="assignmentchk">${component.componentName}</label></td>
													<td class="pl-2"><form:input
															class="form-control border-0 marks"
															id="mark${component.componentId}" type="number"
															path="componentList[${status.index}].marks"
															placeholder="Enter marks"  value="${component.icaCompMarks}" step=".01"/>
															
															</td>
													<td class="pl-2"><form:input
															class="form-control border-0 inputSeq" 
															id="sequenceNo${component.componentId}" type="number"
															path="componentList[${status.index}].sequenceNo"
															 value="${component.sequenceNo}"
															placeholder="Sequence No"  min="1"/>
															
															
															</td>
												</tr>
											</c:otherwise>
										</c:choose>
									</c:forEach>

								</tbody>
							</table>
						</div>


						<div class="col-12 mt-4">
							<button id="saveComponentsForIca"
								class="btn btn-large btn-primary mt-2">Add Components</button>
						</div>
						<!--FORM ITEMS ENDS HERE-->
					</form:form>




				</div>
			</div>
			<!-- /page content: END -->

		</div>

		<!-- SIDEBAR START -->

		<!-- SIDEBAR END -->

		<jsp:include page="../common/newAdminFooter.jsp" />

		<script>
		
		
			$('input[type="checkbox"]').click(function() {
				console.log("CheckBox--->Clicked");
				if ($(this).prop("checked") == true) {
					console.log("CheckBox--->True");
					var id = $(this).attr('id');
					$(this).parent().parent().addClass("tr")
					$('#mark' + id).prop('disabled', false);
					$('#mark' + id).prop('required', true);
					$('#sequenceNo' + id).prop('disabled', false);
					/* $('#sequenceNo' + id).prop('required', true); */
					
				} else if ($(this).prop("checked") == false) {
					console.log("CheckBox--->false");
					var id = $(this).attr('id');
					console.log('No       =-=====chec');
					$('#mark' + id).prop('disabled', true);
					$('#mark' + id).prop('required', false);
					$('#mark' + id).val('');
					$('#sequenceNo' + id).prop('disabled', true);
				/* 	 $('#sequenceNo' + id).prop('required', true);  */
				
				}
			});
			
			$("#saveComponentsForIca").click(function(e) {
	
				
			
				let isValid = true;
				let isReq = false;

				$(".inputSeq:enabled").each(function(){
					if($(this).val().length > 0) {
						console.log(" set required ");	
						$(".inputSeq:enabled").attr('required', true);
						isReq = true;
						
						console.log("isValid inside req func====>>>> ", isValid)
						return 0;
					}
					else{
						
						$(".inputSeq:enabled").attr('required', false);
						return 0;
					}
				})
				
				$(".inputSeq:enabled").each(function(){
					if(isReq == true && $(this).val().length == 0) {
						
						isValid = false;
						console.log("isValid inside check empty fnc====>>>> ", isValid)
						return 0;
					}
				})
				
				
                console.log("isValid -----"+isValid)
			    
			    if(!isValid) {
			    	
			      console.log("Invalid")
			      e.preventDefault(); //prevent the default action
			      alert(" Please Fill All The Sequence  Numbers ");
			    }
			});
			
			
		</script>