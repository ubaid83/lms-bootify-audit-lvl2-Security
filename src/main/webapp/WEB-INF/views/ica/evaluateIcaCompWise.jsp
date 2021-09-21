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
<head>
<style>
.osTable {
	overflow-x: scroll;
}

table.scroll {
	width: 100%;
	/* Optional */
	/* border-collapse: collapse; */
	border-spacing: 0;
	/*border: 2px solid black;*/
}

table.scroll tbody, table.scroll thead {
	display: block;
}

thead tr th {
	height: 30px;
	line-height: 30px;
	/*text-align: left;*/
}

table.scroll tbody {
	height: 500px;
	overflow-y: auto;
	overflow-x: hidden;
}

tbody {
	border-top: 2px solid black;
}

tbody td, thead th {
	width: 50%;
	/* Optional */
	border-right: 1px solid black;
}

tbody td:last-child, thead th:last-child {
	border-right: none;
}


/* LOADER CSS */
.newLoaderWrap {
            height: 100vh;
            position: fixed;
            background: rgba(0, 0, 0, 0.40);;
            top: 0;
            left: 0;
            width: 100%;
            z-index: 20000;
            display: none;
        }

        .newLoader {
            position: fixed;
            z-index: 20001;
            border: 8px solid #000000;
            border-top: 8px solid #ffffff;
            height: 120px;
            width: 120px;
            border-radius: 50%;
            top: calc(50% - 60px);
            left: calc(50% - 60px);
             -webkit-animation: spin 2s linear infinite;
        /* Safari */
        animation: spin 2s linear infinite;
        }


       

        /* Safari */
        @-webkit-keyframes spin {
            0% {
                -webkit-transform: rotate(0deg);
            }

            100% {
                -webkit-transform: rotate(360deg);
            }
        }

        @keyframes spin {
            0% {
                transform: rotate(0deg);
            }

            100% {
                transform: rotate(360deg);
            }
        }


</style>
</head>
<div class="d-flex dataTableBottom" id="facultyAssignmentPage">

<sec:authorize access="hasRole('ROLE_FACULTY')">
	<jsp:include page="../common/newLeftNavBarFaculty.jsp" />
	<jsp:include page="../common/newLeftSidebarFaculty.jsp" />
	<jsp:include page="../common/rightSidebarFaculty.jsp" />
</sec:authorize>
<sec:authorize access="hasRole('ROLE_ADMIN')">
<jsp:include page="../common/newAdminLeftNavBar.jsp" />
	<jsp:include page="../common/rightSidebarAdmin.jsp" />
</sec:authorize>

	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper">
		<sec:authorize access="hasRole('ROLE_FACULTY')">
		<jsp:include page="../common/newTopHeaderFaculty.jsp" />
		</sec:authorize>
		<sec:authorize access="hasRole('ROLE_ADMIN')">
		<jsp:include page="../common/newAdminTopHeader.jsp" />
		</sec:authorize>

		<div class="container mt-5">
			<div class="row">
			<!-- LOADER HTML -->
      <div class="newLoaderWrap position-fixed">
        <div class="newLoader">

        </div>
    </div>
			
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
							<li class="breadcrumb-item active" aria-current="page">Add
								Subjective Test Question</li>
						</ol>
					</nav>

					<jsp:include page="../common/alert.jsp" />


					<!-- Input Form Panel -->
					<div class="card bg-white border">
						<div class="card-body">
							<h5 class="text-center border-bottom pb-2 text-uppercase">Internal
								Mark Assignment For <span>${moduleName}</span></h5>

							<div class="row">

								<div class="col-md-4 col-sm-6 mt-3">
									<strong>Acad year:</strong> <span>${ica.acadYear}</span>
								</div>
								<div class="col-md-4 col-sm-6 mt-3">
									<strong>Semester:</strong> <span>${acadSession}</span>
								</div>
								<div class="col-md-4 col-sm-6 mt-3">
									<strong>Program:</strong> <span>${programName}</span>
								</div>
								<div class="col-md-4 col-sm-6 mt-3">
									<strong>Module:</strong> <span>${moduleName}</span>
								</div>

								<%-- <div class="col-md-4 col-sm-6 mt-3">
									<strong>Start Date:</strong> <span>${ica.startDate}</span>
								</div>
								<div class="col-md-4 col-sm-6 mt-3">
									<strong>End Date:</strong> <span>${ica.endDate}</span>
								</div> --%>
								<div class="col-md-4 col-sm-6 mt-3">
									<strong>Total Internal Marks:</strong> <span>${ica.internalMarks}</span>
								</div>
								<div class="col-md-4 col-sm-6 mt-3">
									<strong>Internal Passing Marks:</strong> <span>${ica.internalPassMarks}</span>
								</div>

								<c:if test="${icaQuery eq 'true'}">
									<input type="hidden" id="icaQ" value='Y'>
								</c:if>
								<c:if test="${icaQuery ne 'true'}">
									<input type="hidden" id="icaQ" value='N'>
									<div class="col-md-4 col-sm-6 mt-3">
										<strong>How do you want to evaluate?</strong>
										<form>
											<input type="radio" name="opt" value="manual" id="manualRad" checked="checked">
											Manual<br> <input type="radio" name="opt" value="excel"
												id="excelRad"> Via Excel<br>

										</form>
									</div>
								</c:if>
							</div>

						</div>
					</div>


					<div class="card bg-white border" id="manual">

						<div class="card-body">
							<form id="icaGrade"
								action="${pageContext.request.contextPath}/submitIcaCompWise"
								method="post">

								<div class="col-12 testAssignTable osTable">
									<table id="evaulateMarks" class="scroll mt-3 table-bordered text-center">

										<thead>
											<tr>
												<th>Sr. No</th>
												<th>Roll No.</th>
												<th>Name</th>
												<th>SAP ID</th>
												<th>Mark Absent?</th>
												<c:forEach var="comp" varStatus="status"
													items="${icaComponents}">
													<th>${comp.componentName}(${comp.marks})</th>
												</c:forEach>

												
												
												<c:if test="${icaQuery eq 'true'}">
												<th>Query Raised</th>
												</c:if>
												<th>Remarks</th>
												<c:if test="${icaQuery eq 'true'}">
												<th>Approve?</th></c:if>
												<!-- <th rowspan="2">Action</th> -->
											</tr>
										</thead>
										<tbody>
											<input type="hidden" id="internalM"
												value="${ica.internalMarks}">
											<input type="hidden" id="icaIdValue" name="icaIdValue"
												value="${ica.id}">
											<c:choose>
												<c:when test="${icaQuery eq 'true'}">
												
													
													<c:forEach var="stud" varStatus="statusStud"
														items="${studentsListForIca}">
														<%-- <c:if test="${'Y' ne stud.isQueryRaise || 'Y' eq stud.isAbsent}">
															<tr class="readOnlyClass">
														</c:if> --%>
														<c:if test="${'Y' eq stud.isQueryRaise}">
															<tr class="tr${statusStud.count}">
														
														<td>${statusStud.index+1}</td>
														<td>${stud.rollNo}</td>
														<td>${stud.studentName}</td>
														<td>${stud.username}</td>
														<%-- <td  style="min-width:60px;">
														<input type="hidden" name="${stud.username}isAbsent" value="${stud.isAbsent}" id="isAbsentY"/>${stud.isAbsent}
														</td> --%>
														<td style="min-width:60px;"  class="isAbsent">
															<c:if test="${stud.isAbsent eq 'Y'}">
																<input type="radio" name="${stud.username}isAbsent" value="Y" id="isAbsentY" checked="checked" onclick="disableTr('readOnlyClass');" class="absentStatus">Yes<br/>
																<input type="radio" name="${stud.username}isAbsent" value="N" id="isAbsentN" style="margin-right: 4px;" onclick="enableTr('readOnlyClass');" class="absentStatus">No
															</c:if>
															<c:if test="${stud.isAbsent ne 'Y'}">
																<input type="radio" name="${stud.username}isAbsent" value="Y" id="isAbsentY" onclick="disableTr('tr${statusStud.count}');" class="absentStatus">Yes<br/>
																<input type="radio" name="${stud.username}isAbsent" value="N" checked="checked" id="isAbsentN" style="margin-right: 4px;" onclick="enableTr('tr${statusStud.count}');" class="absentStatus">No
															</c:if>
														</td>

														<c:forEach var="comp" varStatus="status"
															items="${icaComponents}">
															<td><c:set var="key"
																	value="${stud.username}-${comp.componentId}" /> <input
																type="hidden" id="${comp.componentId}-${stud.username}"
																value="${studentMarksMap[key]}" /> <input type="number"
																data-tier-id="1"
																id="${stud.username}-${comp.componentId}-${comp.marks}"
																name="${stud.username}-${comp.componentId}-${comp.marks}"
																class="text-center componentMarks marks"
																value="${studentMarksMap[key]}"></td>
														</c:forEach>
													<%-- 	<td><c:set var="keyTotal"
																value="${stud.username}totalM" /> <c:set
																var="keyRemark" value="${stud.username}remark" /> 
																<c:set
																var="keyQuery" value="${stud.username}query" />
																
																<input
															class="text-center marks" type="text" placeholder="Total Marks"
															readonly="readonly" id="total${stud.username}"
															name="total${stud.username}"
															value="${studentMarksMap[keyTotal]}">
															
															
															</td> --%>
														
														<c:if test="${icaQuery eq 'true'}">
														<td>
														<c:set
																var="keyRemark" value="${stud.username}remark" /> 
																<c:set
																var="keyQuery" value="${stud.username}query" />
														<textarea  disabled="disabled"
																name="query${stud.username}">${studentMarksMap[keyQuery]}
														</textarea>
														
														</td>
														</c:if>
														<td><textarea class="remarkTextArea" placeholder="Enter remark"
																name="remark${stud.username}">${studentMarksMap[keyRemark]}</textarea></td>
														<td style="min-width:60px;"  class="isApproved">
															<c:if test="${stud.isQueryApproved eq 'Y'}">
														<input type="radio" name="${stud.username}isApproved" value="Y" id="isApprovedY" checked="checked"
														onclick="approvedQuery('tr${statusStud.count}');" class="loadApprovalStatus">Yes<br/>
														<input type="radio" name="${stud.username}isApproved" value="N"
														id="isApprovedN" style="margin-right: 4px;" onclick="rejectQuery('tr${statusStud.count}');" class="loadApprovalStatus">No
														</c:if>
														<c:if test="${empty stud.isQueryApproved}">
														<input type="radio" name="${stud.username}isApproved" value="Y" id="isApprovedY" checked="checked" 
														onclick="approvedQuery('tr${statusStud.count}');" class="loadApprovalStatus">
														Yes<br/><input type="radio" name="${stud.username}isApproved" value="N" 
														id="isApprovedN" style="margin-right: 4px;" onclick="rejectQuery('tr${statusStud.count}');" class="loadApprovalStatus">No
														</c:if>
														<c:if test="${stud.isQueryApproved eq 'N'}">
														<input type="radio" name="${stud.username}isApproved" value="Y" id="isApprovedY" 
														onclick="approvedQuery('tr${statusStud.count}');" class="loadApprovalStatus">
														Yes<br/><input type="radio" name="${stud.username}isApproved" value="N" checked="checked" 
														id="isApprovedN" style="margin-right: 4px;" onclick="rejectQuery('tr${statusStud.count}');" class="loadApprovalStatus">No
														</c:if>
														</td>
														
														</tr>
														</c:if>
													</c:forEach>
												</c:when>
												<c:otherwise>


													
													<c:forEach var="stud" varStatus="statusStud"
														items="${studentsListForIca}">
														<c:if test="${stud.isAbsent eq 'Y'}">
															<tr class="readOnlyClass">
														</c:if>
														<c:if test="${stud.isAbsent ne 'Y'}">
															<tr class="tr${statusStud.count}">
														</c:if>
														

															<td>${statusStud.index+1}</td>
															<td>${stud.rollNo}</td>
															<td>${stud.studentName}</td>
															<td>${stud.username}</td>
															<td style="min-width:60px;">
															<c:if test="${stud.isAbsent eq 'Y'}">
														<input type="radio" name="${stud.username}isAbsent" value="Y" id="isAbsentY" checked="checked"
														onclick="disableTr('readOnlyClass');" class="absentStatus">
														Yes<br/><input type="radio" name="${stud.username}isAbsent" value="N"
														id="isAbsentN" style="margin-right: 4px;" onclick="enableTr('readOnlyClass');" class="absentStatus">No
														</c:if>
														<c:if test="${stud.isAbsent ne 'Y'}">
														<input type="radio" name="${stud.username}isAbsent" value="Y" id="isAbsentY" onclick="disableTr('tr${statusStud.count}');" class="absentStatus">
														Yes<br/><input type="radio" name="${stud.username}isAbsent" value="N" checked="checked"
														id="isAbsentN" style="margin-right: 4px;" onclick="enableTr('tr${statusStud.count}');" class="absentStatus">No
														</c:if>
														</td>
															<c:forEach var="comp" varStatus="status"
																items="${icaComponents}">
																<td><c:set var="key"
																		value="${stud.username}-${comp.componentId}" /> <input
																	type="hidden" id="${comp.componentId}-${stud.username}"
																	value="${studentMarksMap[key]}}" /> <input
																	type="number" data-tier-id="1"
																	id="${stud.username}-${comp.componentId}-${comp.marks}"
																	name="${stud.username}-${comp.componentId}-${comp.marks}"
																	class="text-center componentMarks marks"
																	value="${studentMarksMap[key]}"></td>
															</c:forEach>
															
															<td>
															<c:set
																	var="keyRemark" value="${stud.username}remark" />
															<textarea class="remarkTextArea" placeholder="Enter remark"
																	name="remark${stud.username}">${studentMarksMap[keyRemark]}</textarea></td>														
															
															</tr>
													</c:forEach>

												</c:otherwise>
											</c:choose>
										</tbody>
									</table>
								</div>


								<%-- <div class="col-12 table-responsive osTable testAssignTable">
								<table class="scroll mt-3 table-bordered text-center">
									<thead>
										<tr>
											<th rowspan="1">Sr. No</th>
											<th rowspan="1">Roll No.</th>
											<th rowspan="1">Name</th>
											<th rowspan="1">SAP ID</th>
											<c:forEach var="comp" varStatus="status"
												items="${icaComponents}">
												<th>${comp.componentName}(${comp.marks})</th>
											</c:forEach>
											<th >Total
												(Out of ${ica.internalMarks})
											</th>
											<c:if test="${ica.scaledReq eq 'Y'}">
												<th ><i class="fas fa-long-arrow-alt-down text-danger"></i>
													Scale Marks
													(Out of ${ica.scaledMarks})</th>
											</c:if>
											<th>Remarks</th>
											<c:choose>
												<c:when test="${ica.scaledReq eq 'Y'}">
													<th colspan="${icaComponents.size()+2}"
														style="width: 100px;">Marks</th>
												</c:when>
												<c:otherwise>
													<th colspan="${icaComponents.size()+1}">Marks</th>
												</c:otherwise>
											</c:choose>
										
											<!-- <th rowspan="2">Status</th>  -->
											<!-- <th rowspan="2">Action</th> -->

										</tr>
									
									</thead>
									<tbody>
										<c:forEach var="stud" varStatus="statusStud"
											items="${studentsListForIca}">
											<tr>

												<td>${statusStud.index+1}</td>
												<td>${stud.rollNo}</td>
												<td>${stud.studentName}</td>
												<td>${stud.username}</td>
												

												 <c:forEach var="comp" varStatus="status"
													items="${icaComponents}">
													<td><input type="number" data-tier-id="1"
														onkeypress="return isNumberKey(event)"
														id="${comp.id}${stud.username}"
														name="${comp.id}${stud.username}"
														class="form-control notes rounded-0" value=""></td>
												</c:forEach> 
												<td><input class="text-center" type="text"
													placeholder="Total Marks"></td>
												<c:if test="${ica.scaledReq eq 'Y'}">
													<td><input class="text-center" type="text"
														placeholder="Scaled Marks" disabled="disabled"></td>
												</c:if>

												<td><textarea placeholder="Enter remark"></textarea></td>
												<!-- <td>Pass/Fail</td> -->
												<!-- 	<td><button class="btn btn-md btn-success">Submit</button></td> -->
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div> --%>

								<div class="col-12 mt-3">
									<!-- 	<button class="btn btn-secondary mt-2">Save Changes As
										Draft</button>
									<button class="btn btn-dark mt-2">Submit</button> -->
									<c:if test="${icaQuery ne 'true'}">
									<input type="button" id="saveas" class="btn btn-secondary mt-2"
										value="Save as Draft" /> 
									</c:if>	
									<input type="button" id="genId"
										value="Submit" class="btn btn-dark mt-2" /> <a id="dlink"
										style="display: none;"></a>

								</div>
							</form>


						</div>
					</div>

					<div class="card bg-white border" id="excel">
						<div class="card-body">
							<h5 class="text-center pb-2 border-bottom">Upload Student
								ICA Marks Via Excel</h5>
							<form:form id="uploadIcaFromExcel"
								action="uploadStudentCompMarksExcel" method="post"
								enctype="multipart/form-data">
								<div class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
									<input id="file" name="file" type="file" class="form-control"
										required="required" />
								</div>
								<div class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">

									<!-- <input type="button" id="excelsaveas" class="btn btn-secondary mt-2"
										value="Save as Draft" /> <input type="button" id="excelsubmit"
										value="Submit" class="btn btn-dark mt-2"/> <a id="dlink"
										style="display: none;"></a> -->

									<button id="submit" class="btn btn-secondary mt-2"
										formaction="uploadStudentCompMarksExcel?saveAs=draft&icaId=${icaBean.id}&compId=${compId}"
										onclick="return confirm('Are you sure you want to save?')">Save
										As Draft</button>
									<button id="submit" class="btn btn-dark mt-2"
										formaction="uploadStudentCompMarksExcel?saveAs=submit&icaId=${icaBean.id}&compId=${compId}"
										onclick="return confirm('Are you sure? once submitted you cannot edit marks')">Final
										Submit</button>

									<%-- <a href="uploadStudentMarksExcel?saveAs=draft&icaId=${icaBean.id}" 
										class="btn btn-secondary mt-2"
										onclick="return confirm('Are you sure you want to save?')">Save As Draft</a>
										<a href="uploadStudentMarksExcel?saveAs=submit&icaId=${icaBean.id}"
										onclick="return confirm('Are you sure? once submitted you cannot edit marks')" 
										class="btn btn-dark mt-2">Final Submit</a> --%>

								</div>
								<div class="col-12 mt-3 position-relative text-left">
									<h6>Excel Format:</h6>
									<a class="text-danger"
										href="downloadIcaMarksUploadTemplate?icaId=${icaBean.id}">Download
										Template</a>
								</div>
							</form:form>
						</div>
					</div>

					<!-- Results Panel -->




					<%-- 	<jsp:include page="../common/paginate.jsp">
							<jsp:param name="baseUrl" value="searchFacultyGroups" />
						</jsp:include> --%>

					<!-- /page content: END -->

				</div>
				<sec:authorize access="hasRole('ROLE_FACULTY')">
				<!-- SIDEBAR START -->
				<jsp:include page="../common/newSidebar.jsp" />
				<!-- SIDEBAR END -->
				<jsp:include page="../common/footer.jsp" />
				</sec:authorize>
				<sec:authorize access="hasRole('ROLE_ADMIN')">
				<jsp:include page="../common/newAdminFooter.jsp" />
				</sec:authorize>



				<!-- <script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/MathJax.js?config=TeX-AMS_HTML"></script> -->

				<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>

				<script>
					// Change the selector if needed
					var $table = $('table.scroll'), $bodyCells = $table.find(
							'tbody tr:first').children(), colWidth;

					// Adjust the width of thead cells when window resizes
					$(window).resize(function() {
						// Get the tbody columns width array
						colWidth = $bodyCells.map(function() {
							return $(this).width();
						}).get();

						// Set the width of thead columns
						$table.find('thead tr').children().each(function(i, v) {
							$(v).width(colWidth[i]);
						});
					}).resize(); // Trigger resize handler
				</script>

				<script>
					$(".componentMarks")
							.change(
									function() {

										//alert("On change called")

										var idComp = $(this).attr("id");
										var splitId = idComp.split('-');
										var total = 0;
										var scaledTotal = 0;
										var oldValueId = splitId[1] + '-'
												+ splitId[0];
										var oldValue = $('#' + oldValueId)
												.val();
										console.log('old value id' + oldValueId
												+ 'old value id' + oldValue);
										if (!isNumber($(this).val(), splitId[2])) {

											$(this).val(oldValue);

											alert('mark entered is not a number or not in range. Please check again');
											return;
										}
										//alert("idOfCriteria ------ "+idOfCriteria)
									
									});
				</script>
				<script>
					function isNumber(n, maxMarks) {
						if (!isNaN(parseFloat(n)) && isFinite(n)) {
							var value = parseFloat(n);
							if (value >= 0 && value <= maxMarks) {
								return true;
							} else {
								return false;
							}
						} else {
							return false;
						}
					}

					function multiply(x, y) {
						var one = parseFloat(x);
						var two = parseFloat(y);
						return one * two;
					}

				
				</script>

				<script>
					$("#genId")
							.click(
									function() {

										/* 		var cf = confirm("Warning! Ensure grading of all students has been done?");
												if (cf == true) {
													$("#grade").submit();
												} else {

												} */
										var errCount = 0;
										$('.componentMarks').each(function() {
											if ($(this).val() == '') {
												errCount++;
											}
										});

										if (errCount > 0) {
											swal({
												title : 'cannot submit, grading of all student has not been done.',
												// text: "It will permanently deleted !",
												type : 'warning',
												//icon : 'success',
												showCancelButton : true,
												confirmButtonColor : '#3085d6',
												cancelButtonColor : '#d33',
											// confirmButtonText: 'Yes, delete it!'
											});
										} else {
											swal(
													{
														title : 'Are you sure you want to submit?',
														// text: "It will permanently deleted !",
														//type: 'warning',
														icon : 'success',
														showCancelButton : true,
														confirmButtonColor : '#3085d6',
														cancelButtonColor : '#d33',
													// confirmButtonText: 'Yes, delete it!'
													}).then(function() {
												$("#icaGrade").submit();

											});
										}

									});
					
					
					 

					 $("#saveas")
							.click(
									function() {
										$(".newLoaderWrap").css('display','block');
										//alert("here");
										var datastring = $("#icaGrade").serialize();
										$.ajax({
													type : "POST",
													url : "${pageContext.request.contextPath}/saveIcaCompMarksAsDraft",
													data : datastring,
													success : function(data) {
														$(".newLoaderWrap").css('display','none');
														swal('data saved successfully').then(function() {
														document.location.reload();
														});
													},
													error : function() {
														alert('Error here');
													}
												});
									}); 
				</script>

				<script>
					$(function() {
						$('table tr.readOnlyClass')
								.each(
										function() {
											$(this).css('background-color',
													'#e8dddd');

											$(this)
													.find('td')
													.each(
															function() {

																console
																		.log('td found---');
																$(this)
																		.find(
																				'input,textarea')
																		.attr(
																				"readonly",
																				true);
																$(this)
																		.find(
																				'input,textarea')
																		.css(
																				'background-color',
																				'#e8dddd');
															});
										});
					});
				</script>
				<script>
					$(document).ready(function() {
						
						var icaQ = $('#icaQ').val();
						
						if (icaQ == 'Y') {
							$('#excel').hide();
						} else {
							
							$('#manual').show();
							$('#excel').hide();
							$('#manualRad').click(function() {
								$('#manual').show();
								$('#excel').hide();
							});

							$('#excelRad').click(function() {
								$('#excel').show();
								$('#manual').hide();
							});
						}
						//var rowCount = $('table tr').length - 1;
						$('#evaulateMarks tbody tr').each(function(index) {
							console.log("TR being looped")
							//alert('${icaQuery}');
							if('${icaQuery}' == 'true'){
							var isApproved = $( this) .find(".isApproved input[type=radio]:checked").val();
                            var isAbsent = $(this).find(".isAbsent input[type=radio]:checked").val();
							//alert(index+" "+isApproved);
							if(isApproved != undefined){
							//alert(index+" "+isApproved);
							$(this).css('background-color','#e8dddd');
							$(this).find('td').each(function() {
								console.log('td found---');
								if(isAbsent == 'Y'){
                            		$(this).find('input,textarea').attr("readonly", true);
                                    $(this).find('.remarkTextArea').attr("readonly",false);
                                    $(this).find('.remarkTextArea').attr('required','required');
                                   	//$(this).find('input').val('0');
                                    //$(this).find('input[type=radio].absentStatus').val('N');
                                   	//$(this).find('textarea').val('absent');
                                    $(this).find('input,textarea').css('background-color','#e8dddd');
                                    $(this).find('.remarkTextArea').css('background-color','#ffffff');
                            	}
								if(isApproved == 'N'){
									$(this).find('input,textarea').attr("readonly",true);
									$(this).find('.remarkTextArea').attr("readonly",false);
									$(this).find('.remarkTextArea').attr('required','required');
									//$(this).find('input').val('0');
									$(this).find('input[type=radio].loadApprovalStatus').val('N');
									//$(this).find('textarea').val('absent');
									$(this).find('input,textarea').css('background-color','#e8dddd');
									$(this).find('.remarkTextArea').css('background-color','#ffffff');		
								}else if (isApproved == 'Y'){
									$(this).css('background-color','#ffffff');
									$(this).find('td').each(function() {
										console.log('td found---');
										$(this).find('input,textarea').attr("readonly",false);
										$(this).find('input,textarea').css('background-color','#ffffff');
										$(this).find('input[type=radio].loadApprovalStatus').val('Y');
										$(this).find('.remarkTextArea').removeAttr('required');
									});
								}
							});
							}
							}
						});
						
					});

				/* 	$("#genId").click(function() {
						swal({
							title : 'Are you sure you want to submit?',
							// text: "It will permanently deleted !",
							//type: 'warning',
							icon : 'success',
							showCancelButton : true,
							confirmButtonColor : '#3085d6',
							cancelButtonColor : '#d33',
						// confirmButtonText: 'Yes, delete it!'
						}).then(function() {
							$("#excelsubmit").submit();

						});
					}); */
					
					function disableTr(trClass){
						console.log('function called');
						$('table tr.'+trClass).each(function() {
							$(this).css('background-color','#e8dddd');
							$(this).find('td').each(function() {
								console.log('td found---');
								$(this).find('input,textarea').attr("readonly",true);
								$(this).find('input.marks').val('0');
								$(this).find('input[type=radio].absentStatus').val('Y');
								$(this).find('textarea.remarkTextArea').val('absent');
								$(this).find('input,textarea').css('background-color','#e8dddd');
							});
						});
			
					}
					function rejectQuery(trClass){
						console.log('function called');
						$('table tr.'+trClass).each(function() {
							$(this).css('background-color','#e8dddd');
							$(this).find('td').each(function() {
								console.log('td found---');
								$(this).find('input,textarea').attr("readonly",true);
								$(this).find('.remarkTextArea').attr("readonly",false);
								$(this).find('.remarkTextArea').attr('required','required');
								//$(this).find('input').val('0');
								$(this).find('input[type=radio].loadApprovalStatus').val('N');
								//$(this).find('textarea').val('absent');
								$(this).find('input,textarea').css('background-color','#e8dddd');
								$(this).find('.remarkTextArea').css('background-color','#ffffff');
							});
						});
			
					}
					function approvedQuery(trClass){
						console.log('enable function called'+trClass);
						$('table tr.'+trClass).each(function() {
							$(this).css('background-color','#ffffff');
							var isAbsent = $(this).find(".isAbsent input[type=radio]:checked").val();
							$(this).find('td').each(function() {
								console.log('td found---');
								 if(isAbsent == 'Y'){
                                 	$(this).find('input[type=radio].loadApprovalStatus').val('Y');
                                 }else{
									$(this).find('input,textarea').attr("readonly",false);
									$(this).find('input,textarea').css('background-color','#ffffff');
									$(this).find('input[type=radio].loadApprovalStatus').val('Y');
									$(this).find('.remarkTextArea').removeAttr('required');
                                 }
							});
						});
					}
					function enableTr(trClass){
						console.log('enable function called'+trClass);
						$('table tr.'+trClass)
						.each(
								function() {
									
									$(this).css('background-color','#ffffff');
									var isApproved = $(this).find(".isApproved input[type=radio]:checked").val();
									$(this).find('td').each(function() {
										console.log('td found---',isApproved);
										if(isApproved == 'Y'){
											$(this).find('input,textarea').attr("readonly",false);
											$(this).find('input.marks,.remarkTextArea').val('');
											$(this).find('input,textarea').css('background-color','#ffffff');
											$(this).find('input[type=radio].absentStatus').val('N');
										}else{
											$(this).find('input,textarea').attr("readonly",false);
											$(this).find('input.marks,.remarkTextArea').val('');
											$(this).find('input,textarea').css('background-color','#ffffff');
											$(this).find('input[type=radio].absentStatus').val('N');
                                       		//$(this).find('input[type=radio].absentStatus').val('N');
                                       	}
										
									});
								});
						}
			
					
				</script>