<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
	
	
	
	

<jsp:include page="../common/newDashboardHeader.jsp" />
<style>
    .disabled {
        pointer-events: none;
        cursor: default;
    }
</style> 

<div class="d-flex dataTableBottom" id="gradeListPage">
<jsp:include page="../common/newLeftNavBarFaculty.jsp"/>
<jsp:include page="../common/newLeftSidebarFaculty.jsp"/>
<jsp:include page="../common/rightSidebarFaculty.jsp"/>

<!-- DASHBOARD BODY STARTS HERE -->

<div class="container-fluid m-0 p-0 dashboardWraper">

	<jsp:include page="../common/newTopHeaderFaculty.jsp" />
     <jsp:include page="../common/newLoaderWrap.jsp"/>
     <div class="container mt-5">
                <div class="row">
            <!-- SEMESTER CONTENT -->    
                    <div class="col-lg-9 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto">
                        
                      <!-- page content: START -->
                      
                      <nav aria-label="breadcrumb">
                            <ol class="breadcrumb">
                                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
                                <li class="breadcrumb-item" aria-current="page"><c:out value="${Program_Name}" /></li>
                                <sec:authorize access="hasRole('ROLE_STUDENT')">
								<c:out value="${AcadSession}" />
							</sec:authorize>
                                <li class="breadcrumb-item active" aria-current="page"> Search Assignment To Evaluate</li>
                            </ol>
                        </nav>

						<jsp:include page="../common/alert.jsp" />
						<!-- Input Form Panel -->
						<div class="card bg-white border">
							<div class="card-body">
									<div class="text-center border-bottom pb-2">
										<h5>Search Assignment To Evaluate</h5>
									</div>

									<div class="x_content">
										<form:form action="evaluateByStudent" method="post"
											modelAttribute="assignment">
											<fieldset>
                                               <div class="form-row">
												<div class="col-lg-6 col-md-6 col-sm-6 mt-3">
													<div class="form-group">
														<form:label path="courseId" for="courseId">Course</form:label>
														<form:select id="courseId" path="courseId"
															class="form-control">
															<form:option value="">Select Course</form:option>
															<c:forEach var="course" items="${allCourses}"
																varStatus="status">
																<form:option value="${course.id}">${course.courseName}</form:option>
															</c:forEach>
														</form:select>
													</div>
												</div>

												<div class="col-lg-6 col-md-6 col-sm-6 mt-3">
													<div class="form-group">
														<form:label path="id" for="id">Assignment</form:label>
														<form:select id="assid" path="id" class="form-control">
															<form:option value="">Select Assignment</form:option>

															<c:forEach var="preAssigment" items="${preAssigments}"
																varStatus="status">
																
																<form:option value="${preAssigment.id}">${preAssigment.assignmentName}</form:option>
																
															</c:forEach>

														</form:select>
													</div>
												</div>
												</div>



												<div class="text-center mt-3">
													<div class="form-group">
														<button id="submit" name="submit"
															class="btn btn-large btn-success">Search</button>
														<input id="reset" type="reset" class="btn btn-dark">
														<!-- <button id="cancel" name="cancel" class="btn btn-danger"
															formnovalidate="formnovalidate">Cancel</button> -->
													</div>
												</div>
											</fieldset>
										</form:form>
									</div>
							</div>
						</div>
						<c:choose>
							<c:when test="${assignmentsList.size() > 0}">
								<!-- Results Panel -->
								<div class="card bg-white border">
									<div class="card-body">
											<div class="text-center">
												<h5 class="border-bottom pb-2">Assignments | ${fn:length(assignmentsList)} Records
													Found</h5>
												<!-- <div class="col-sm-4 col-md-4 col-xs-12 column">
													<div class="form-group"> -->
												<a href="gradeCenterForm"><i
													class="btn btn-large btn-info mt-2">Go to Grade Center</i></a> 
													<a
													href="downloadAllFile?assignmentId=${assignment.id}"><i
													class="btn btn-large btn-danger mt-2">Download all Files</i></a>
												<a href="#"><i class="btn btn-large btn btn-dark mt-2" data-toggle="modal" 
													data-target="#uploadRemarkModal">Upload Remark Files</i></a>
												
													<a
														onclick="return confirm('Only applicable for PDF files')"
														href="checkCopiedAssignment?assignmentId=${assignment.id}"><i
														class="btn btn-large btn-secondary mt-2">Download Copy Case
															Report</i></a>

												
											 <c:if test="${true}">
                                                    <a 
													href="#" class="disabled"><i
													class="btn btn-large btn-primary mt-2 showloader" >Plagiarism Checked.</i></a>
                                                </c:if> 
												<c:if test="${false}"> 
												<a
													href="checkForPlagiarismAll?assignmentId=${assignment.id}"><i
													class="btn btn-large btn-primary mt-2 showloader">Check for Plagiarism</i></a>
												</c:if>
												
												<button
														type="button" class="btn btn-success mt-2" role="button"
														onclick="tableToExcel('showMyStudents', '${assignment.assignmentName }', 'Evaluate Students for ${assignment.assignmentName }.xls')"
														value="Export to Excel">Export To Excel</button>
											<a href="#">
							<i class="btn btn-large btn btn-dark mt-2" data-toggle="modal" data-target="#uploadmarkModal">Upload Marks For Evaluation</i></a>
											
											<c:if test="${showQuestionwiseEvaluate eq true && not empty showQuestionwiseEvaluate}">
												<a href="#"><i class="btn btn-large btn btn-dark mt-2" data-toggle="modal" data-target="#uploadQuestionwiseMarksModal">Evaluate Questionwise</i></a>
											</c:if>
											
											</div>
											
											<div class="x_content">
											 
												<div class="table-responsive testAssignTable">
													<table id="showMyStudents" class="table table-hover ">
														<thead>
															<tr>
																<th>Sr. No.</th>
																<th>Student User Name</th>
																<th>Roll No</th>
																<th>Faculty ID</th>
																<th>Group</th>
																<th>Assignment Name</th>
																<th>Assignment File</th>
																<!-- <th>Assignment Details</th> -->
																<th>Student Submitted File</th>
																<th>Threshold</th>
																<th>Submission Status</th>
																<th>Submission Date</th>
																<th>Evaluation Status</th>
																<th>Save Score</th>
																<th>Save Remarks</th>
																<th>Save Low Score Reason</th>
																<th>Start Date</th>
																<th>End Date</th>

															</tr>
														</thead>
														<tbody>

															<c:forEach var="assignment" items="${assignmentsList}"
																varStatus="status">
																<div class="modal fade" id="myModal${assignment.id}"
																	role="dialog">
																	<div class="modal-dialog">

																		<!-- Modal content-->
																		<div class="modal-content">
																			<div class="modal-header">
																				<button type="button" class="close"
																					data-dismiss="modal">&times;</button>
																				<h4 class="modal-title">Plagiarism Report</h4>
																			</div>
																			<div class="modal-body">


																				<c:if test="${assignment.threshold ne null}">
																					<c:if test="${assignment.threshold eq '0'}">
																						Plagiarim check is done! Assignment is not Copied!
																						</c:if>
																					<c:if test="${assignment.threshold ne '0'}">
																						<p>${assignment.threshold}
																							% copied from
																							<c:if
																								test="${assignment.url eq 'Copyleaks internal database'}">${assignment.url}</c:if>
																							<c:if
																								test="${assignment.url ne 'Copyleaks internal database'}">
																								<a href="${assignment.url}" target="_blank">${assignment.url}
																								</a>
																							</c:if>
																						</p>
																					</c:if>
																				</c:if>
																				<c:if test="${assignment.threshold eq null and assignment.url eq null}">
																					Plagiarim check is not done!
																				</c:if>
																				<c:if test="${assignment.threshold eq null and assignment.url ne null}">
																					${assignment.url}
																				</c:if>



																			</div>
																			<div class="modal-footer">
																				<button type="button" class="btn btn-default"
																					data-dismiss="modal">Close</button>
																			</div>
																		</div>

																	</div>
																</div>
																<tr>
																	<td><c:out value="${status.count}" /></td>
																	<td><c:out value="${assignment.username}" /></td>
																	<td><c:out value="${assignment.rollNo}" /></td>
																	<td><c:out value="${assignment.evaluatedBy}" /></td>
																	<td><c:out value="${assignment.groupName}" /></td>
																	<td><c:out value="${assignment.assignmentName}" /></td>
																	<td><c:if
																			test="${assignment.showFileDownload eq 'true'}">
																			<a href="downloadFile?id=${assignment.assignmentId}">Download</a>
																		</c:if> <c:if
																			test="${assignment.showFileDownload eq 'false'}">No File</c:if>
																	</td>
																	<%-- <td><a href="#"
																		onClick="showModal('${assignment.assignmentId}', '${assignment.assignmentName}');">Assignment
																			Details</a></td> --%>
																	<td><c:if
																			test="${assignment.showStudentFileDownload eq 'true'}">
																			<a href="downloadFile?saId=${assignment.id }">Download
																				Answer File</a>
																		</c:if> <c:if
																			test="${assignment.showStudentFileDownload eq 'false'}">No File</c:if>

																	</td>
																	<td>
																		<button type="button" class="btn btn-info"
																			data-toggle="modal"
																			data-target="#myModal${assignment.id}">

																			<c:out value="${assignment.threshold}"></c:out>
																		</button>

																	</td>
																	<td><c:out value="${assignment.submissionStatus}" /></td>
																	<td><c:out value="${assignment.submissionDate}" /></td>
																	<td><c:out value="${assignment.evaluationStatus}" /></td>
																	<td>
																	<%-- <a href="#" class="editable" id="score"
																		data-type="text" data-pk="${assignment.id}"
																		data-url="saveAssignmentScore"
																		data-title="Enter Score">${assignment.score}</a> --%>

																	<c:if test="${showQuestionwiseEvaluate eq true && not empty showQuestionwiseEvaluate}">
																	<p>${assignment.score}</p>
																	</c:if>
																	<c:if test="${showQuestionwiseEvaluate eq false}">
																	<a href="#" class="editable" id="score"
																		data-type="text" data-pk="${assignment.id}"
																		data-url="saveAssignmentScore"
																		data-title="Enter Score">${assignment.score}</a>
																	</c:if>
																	
																	</td>

																	<td><a href="#" class="editable" id="remarks"
																		data-type="textarea" data-pk="${assignment.id}"
																		data-url="saveAssignmentRemarks"
																		data-title="Enter Remarks">${assignment.remarks}</a></td>

																	<td><a href="#" class="editable" id="remarks"
																		data-type="select" data-pk="${assignment.id}"
																		data-source="[{value: 'Copy Case-Internet', text: 'Copy Case-Internet'},{value: 'Copy Case-Other Student', text: 'Copy Case-Other Student'},{value: 'Wrong Answers', text: 'Wrong Answers'},{value: 'Other subject Assignment', text: 'Other subject Assignment'},{value: 'Scanned/Handwritten Assignment', text: 'Scanned/Handwritten Assignment'},{value: 'Only Questions written', text: 'Only Questions written'},{value: 'Question Paper Uploaded', text: 'Question Paper Uploaded'},{value: 'Blank Assignment', text: 'Blank Assignment'},{value: 'Corrupt file uploaded', text: 'Corrupt file uploaded'}]"
																		data-url="saveLowScoreReason"
																		data-title="Select Low Marks Reason">${assignment.lowScoreReason}</a>
																	</td>

																	<td><a href="#" id="date"
																		data-type="datetime-local" data-pk="${assignment.id}"
																		data-url="saveStartDate" data-title="Start Date">${assignment.startDate}</a></td>




																	<td><a href="#" id="date"
																		data-type="datetime-local" data-pk="${assignment.id}"
																		data-url="saveEndDate" data-title="End Date">${assignment.endDate}</a></td>

																</tr>
															</c:forEach>

														</tbody>
													</table>
													<a id="dlink" style="display: none;"></a>
												</div>
											</div>
									</div>
								</div>
							</c:when>
						</c:choose>
						
<div id="modalUploadMarks">
<div class="modal fade fnt-13"
	id="uploadmarkModal" tabindex="-1" role="dialog"
	aria-labelledby="uploadmarkModal" aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered modal-dialog-scrollable"
		role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="uploadmarkModal">Upload Mark Files</h5>
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<form:form action="uploadStudentAssignmentMarksExcel" id="uploadStudentAssignmentMarksExcel"
					method="post" enctype="multipart/form-data" >
			<div class="modal-body">
					<div class="row">
						<div class="col-12">
							<h6>Upload file</h6>
							<input type="file" name="file" class="form-control-file mb-1" id="uploadAssignmentmark" >
							<p><Strong>Note:</Strong> Upload Marks For students</p>
						<a href="api/downloadStudentAssignmentThresholdReport?assignmentId=${assignment.id}"><i class="text-danger">Download Students Data For Evaluation</i></a>
						
						
						
						</div>
						
					</div>
					<div class="row">
						<div class="col-12 mt-3" id="subDisplay"></div>
					</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-modalClose"
					data-dismiss="modal">Close</button>
				<button id="submitRemarkFiles" class="btn btn-modalSub" name="submitRemarkFiles" 
						formaction="uploadStudentAssignmentMarksExcel?assignmentId=${assignment.id}">Submit</button>
			</div>
			</form:form>
		</div>
	</div>
</div>
</div>						
						<jsp:include page="../common/paginate.jsp">
							<jsp:param name="baseUrl" value="evaluateByStudent" />
						</jsp:include>

			<!-- /page content: END -->
                   
                    </div>
                    
<div id="modalUploadRemark">
<div class="modal fade fnt-13"
	id="uploadRemarkModal" tabindex="-1" role="dialog"
	aria-labelledby="uploadRemarkTitle" aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered modal-dialog-scrollable"
		role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="uploadRemarkTitle">Upload Remark Files</h5>
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<form:form action="uploadStudentAssignmentRemarks" id="uploadStudentAssignmentRemarks"
					method="post" enctype="multipart/form-data" >
			<div class="modal-body">
					<div class="row">
						<div class="col-12">
							<h6>Upload file</h6>
							<input type="file" name="file" class="form-control-file mb-1" id="uploadRemarkZipFile" accept=".zip">
							<p><Strong>Note:</Strong> File should be ZIP and inside that student assignment remark file name with student's SAPID.</p>
						</div>
						
					</div>
					<div class="row">
						<div class="col-12 mt-3" id="subDisplay"></div>
					</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-modalClose"
					data-dismiss="modal">Close</button>
				<button id="submitRemarkFiles" class="btn btn-modalSub" name="submitRemarkFiles" 
						formaction="uploadStudentAssignmentRemarks?assignmentId=${assignment.id}">Submit</button>
			</div>
			</form:form>
		</div>
	</div>
</div>
</div>	
	
<!-- QUESTIONWISE MODAL -->
<div id="modalUploadMarksQuestionwise">
<div class="modal fade fnt-13"
	id="uploadQuestionwiseMarksModal" tabindex="-1" role="dialog"
	aria-labelledby="uploadmarkModalQues" aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered modal-dialog-scrollable"
		role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="uploadmarkModalQues">Upload Mark Files</h5>
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<form:form action="uploadStudentQuestionwiseMarksExcel" id="uploadStudentAssignmentQuestionwiseMarksModal"
					method="post" enctype="multipart/form-data" >
			<div class="modal-body">
					<div class="row">
						<div class="col-12">
							<h6>Upload file</h6>
							<input type="file" name="file" class="form-control-file mb-1" id="uploadAssignmentQuestionwisemark" >
							<p><Strong>Note:</Strong> Do not Change any header or student's SAPID</p>
						
						<a href="${pageContext.request.contextPath}/generateTemplateForEvaluate?assignmentId=${assignment.id}"><i class="text-danger">Download Template</i></a>
						</div>
					</div>
					<div class="row">
						<div class="col-12 mt-3" id="subDisplay"></div>
					</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-modalClose"
					data-dismiss="modal">Close</button>
				<button id="submitQuestionwiseMarksFile" class="btn btn-modalSub" name="submitQuestionwiseMarksFile" 
						formaction="uploadStudentQuestionwiseMarksExcel?assignmentId=${assignment.id}">Submit</button>
			</div>
			</form:form>
		</div>
	</div>
</div>
</div>					
			<!-- SIDEBAR START -->
	         <jsp:include page="../common/newSidebar.jsp" />
	        <!-- SIDEBAR END -->
	<jsp:include page="../common/footer.jsp"/>




	
	
	
	
	
	
	
	
	
	
	
	
	
	
			<script>
				$(document)
						.ready(
								function() {

									$('#reset')
											.on(
													'click',
													function() {
														$('#assid').empty();
														var optionsAsString = "";

														optionsAsString = "<option value=''>"
																+ "Select Assignment"
																+ "</option>";
														$('#assid')
																.append(
																		optionsAsString);

														$("#courseId")
																.each(
																		function() {
																			this.selectedIndex = 0
																		});

													});

									$("#courseId")
											.on(
													'change',
													function() {
														var courseid = $(this)
																.val();
														if (courseid) {
															$
																	.ajax({
																		type : 'GET',
																		url : '${pageContext.request.contextPath}/getAssigmentByCourse?'
																				+ 'courseId='
																				+ courseid,
																		success : function(
																				data) {
																			var json = JSON
																					.parse(data);
																			var optionsAsString = "";

																			$(
																					'#assid')
																					.find(
																							'option')
																					.remove();
																			console
																					.log(json);
																			for (var i = 0; i < json.length; i++) {
																				var idjson = json[i];
																				console
																						.log(idjson);

																				for ( var key in idjson) {
																					console
																							.log(key
																									+ ""
																									+ idjson[key]);
																					optionsAsString += "<option value='" +key + "'>"
																							+ idjson[key]
																							+ "</option>";
																				}
																			}
																			console
																					.log("optionsAsString"
																							+ optionsAsString);

																			$(
																					'#assid')
																					.append(
																							optionsAsString);

																		}
																	});
														} else {
															//alert('Error no course');
														}
													});
									/* $('#courseId').trigger('change'); */
									
									var clicked = false;
									$('.plagCheckButton').click(function(){
									console.log('inside click event');
									if(clicked===false){
									console.log('inside click false');
									         clicked=true;
									         return true;
									    }else{
									    console.log('inside click true');
									         return false;
									    }
									});
									
									$('.showloader').on('click',function() {
										$(".newLoaderWrap .loader-text").html("Processing...<br>Do not close the browser or refresh the page.")
			                            $(".newLoaderWrap").removeClass("d-none");
									});
								});
			</script>
			<script type="text/javascript">
				var tableToExcel = (function() {
					var uri = 'data:application/vnd.ms-excel;base64,', template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--></head><body><table>{table}</table></body></html>', base64 = function(
							s) {
						return window.btoa(unescape(encodeURIComponent(s)))
					}, format = function(s, c) {
						return s.replace(/{(\w+)}/g, function(m, p) {
							return c[p];
						})
					}
					return function(table, name, filename) {
						if (!table.nodeType)
							table = document.getElementById(table)
						var ctx = {
							worksheet : name || 'Worksheet',
							table : table.innerHTML
						};
						// window.location.href = uri + base64(format(template, ctx));

						document.getElementById("dlink").href = uri
								+ base64(format(template, ctx));
						document.getElementById("dlink").download = filename;
						document.getElementById("dlink").click();
					}
				})()
			</script>
			
				<script>
				 $('.table')
			        .on( 'order.dt',  function () { 
			        	callEditable();
			        })
			        .on( 'search.dt', function () {
			        	callEditable();
			        })
			        .on( 'page.dt',   function () {
			        	setTimeout(function(){ callEditable(); }, 100);
			        })
			        .dataTable();
				 
		function callEditable(){		
		$.fn.editable.defaults.mode = 'inline';
		$('.editable').each(function() {
			$(this).editable({
				success : function(response, newValue) {
					obj = JSON.parse(response);
					if (obj.status == 'error') {
						return obj.msg; // msg will be shown in editable
						// form
					}
				}
			});
		})};
		callEditable();
	
	</script>
			
