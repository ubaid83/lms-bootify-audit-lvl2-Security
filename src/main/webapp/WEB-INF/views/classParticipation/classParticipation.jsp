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

<div class="d-flex dataTableBottom" id="facultyAssignmentPage">
<jsp:include page="../common/newLeftNavBarFaculty.jsp"/>
<jsp:include page="../common/newLeftSidebarFaculty.jsp"/>
<jsp:include page="../common/rightSidebarFaculty.jsp"/>

<!-- DASHBOARD BODY STARTS HERE -->

<div class="container-fluid m-0 p-0 dashboardWraper">

	<jsp:include page="../common/newTopHeaderFaculty.jsp" />
     
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
                                <li class="breadcrumb-item active" aria-current="page">Class Participation</li>
                            </ol>
                        </nav>
                        
                        
                        
						<jsp:include page="../common/alert.jsp" />
						
						
						<!-- Input Form Panel -->
						<div class="card bg-white border">
						<div class="card-body">
								<div class="x_title">
											<h5 class="text-center border-bottom pb-2">Class Participation for ${courseName }</h5>

										</div>

										<div class="x_content">
											<form:form action="" method="post"
												modelAttribute="classParticipation">


												<form:input path="courseId" type="hidden" />
												<div class="row">
													<div class="col-md-6 col-sm-12">
														<div class="form-group">
															<form:label path="courseId" for="courseId">Select Course</form:label>
															<form:select id="courseIdForForum" path="courseId"
																class="form-control">
																<form:option value="">Select Course</form:option>
																<c:forEach var="course" items="${allCourses}"
																	varStatus="status">
																	<form:option value="${course.id}">${course.courseName}</form:option>
																</c:forEach>
															</form:select>
														</div>
													</div>
												</div>
											</form:form>
											
											
											<button class="btn btn-md btn-info" data-toggle="modal" data-target="#uploadStudentsMarks">Upload Students Marks</button>
										</div>
						</div>
						</div>

						<!-- Results Panel -->

						
								<div class="card bg-white border">
								<div class="card-body">								
									<div class="col-xs-12 col-sm-12">
									<div class="x_panel">
										<div class="x_title">
											<%-- <h2>Students | ${fn:length(students)} Records found</h2> --%>
											<h5 class="text-center border-bottom pb-2">
												Students

												<c:if test="${showWieghtageForCP}">
															Weight Assigned : <c:out value="${cpWieghtage}"></c:out>


												</c:if>
												<c:if test="${showWieghtageForCP eq 'false'}">
															
															Weight not assigned!
															</c:if>

											</h5>
										
										</div>

										<div class="x_content">
											<form:form action="" method="post"
												modelAttribute="classParticipation">
												<form:input path="courseId" type="hidden" />
												<div class="table-responsive testAssignTable">
													<table class="table table-striped table-hover">
														<thead>
															<tr>
																<th>Sr. No.</th>

																<th>Pictures</th>
																<th>SAP IDs <i class="fa fa-sort"
																	aria-hidden="true" style="cursor: pointer"></i></th>
																<th>Student Name <i class="fa fa-sort"
																	aria-hidden="true" style="cursor: pointer"></i></th>
																<th>Roll No. <i class="fa fa-sort"
																	aria-hidden="true" style="cursor: pointer"></i></th>
																	<th>Campus</th>
																<th>Assign Score <span style="color: red">*</span></th>
																<th>Assign Remarks <span style="color: red">*</span></th>
																<th>Action</th>
															</tr>
														</thead>
														<tfoot>
															<tr>
																<th></th>
																<th>Pictures</th>
																<th>SAP IDs</th>
																<th>Roll No.</th>
																<th>Campus</th>
																<th>Student Name</th>
															</tr>
														</tfoot>
														<tbody>

															<c:forEach var="student" items="${students}"
																varStatus="status">
																<tr>
																	<td><c:out value="${status.count}" /></td>
																		<td><img
																		src="${pageContext.request.contextPath}/savedImages/${student.username}.JPG"
																		alt="No image"
																		onerror="this.src='<c:url value="/resources/images/download.png" />'"></td>

																	<td><c:out value="${student.username}" /></td>
																	<td><c:out
																			value="${student.firstname} ${student.lastname}" /></td>
																	<td><c:out value="${student.rollNo}" /></td>
																	<td><c:out value="${student.campusName}" /></td>
																	<td><form:input path="score" class="form-control"
																			id="saveScore${student.username}"
																			value="${student.score}" type="number" min="0"
																			max="${cpWieghtage}" required="required" /></td>
																	<td><form:input path="remarks"
																			id="saveRemarks${student.username}"
																			class="form-control" value="${student.remarks}"
																			type="text" required="required" /></td>
																	<td><a href="#" id="like${student.username}"
																		class="likeClass"><i
																			class="fa fa-check-square fa-lg"></i></a></td>

																</tr>
															</c:forEach>
														</tbody>
													</table>
												</div>
											</form:form>
										</div>
									</div>
								</div>
								</div>
								</div>
							<div class="modal fade" id="uploadStudentsMarks" tabindex="-1" role="dialog" aria-labelledby="createGroupModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered modal-dialog-scrollable modal-lg" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="uploadStudentsMarksTitle">Upload Students Marks</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
               
        <form:form id="uploadStudentsMarks" action="uploadStudentsMarks" modelAttribute="classParticipation" enctype="multipart/form-data">
						<div class="row page-body">
							<div class="col-sm-12 column">


								
								<div class="row">
									<div class="col-md-8">
									<form:input path="file" type="file"  required="required"  />
									
									<label class="textStrong">Class participation template : </label>
									
									<a href="${pageContext.request.contextPath}/downloadStudents?courseId=${courseId}"  class="btn btn-link"><i>Download Class participation template </i></a>
										
										<form:hidden path="courseId" value="${courseId}"/>
										<button type="Submit" formaction="uploadStudentsMarks" class="btn btn-primary">SUBMIT</button>
										 <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
										</div>
								</div>
							</div>
							  
					</form:form>
        
      </div>
      <div class="modal-footer">
       
      
      </div>
    </div>
  </div>
</div>

					<%-- 	<jsp:include page="../common/paginate.jsp">
							<jsp:param name="baseUrl" value="searchFacultyGroups" />
						</jsp:include> --%>

			<!-- /page content: END -->
                   
                    </div>
			
			<!-- SIDEBAR START -->
	         <jsp:include page="../common/newSidebar.jsp" />
	        <!-- SIDEBAR END -->
	<jsp:include page="../common/footer.jsp"/>

	
<script>
					$("#courseIdForForum")
							.on(
									'change',
									function() {

										//alert("Onchange Function called!");
										var selectedValue = $(this).val();
										window.location = '${pageContext.request.contextPath}/classParticipation?courseId='
												+ encodeURIComponent(selectedValue);
										return false;
									});

					$(".likeClass")
							.click(
									function() {
										$('#likeClass').click(function() {
											change(1);
										});
										console
												.log("called ........................................................000000.");

										var likeId = $(this).attr("id");

										var username = likeId.substr(4);
										console.log(username);

										var str1 = "saveScore".concat(username);
										console.log("str1" + str1);
										var score = $('#' + str1).val();
										console.log("Score is " + score);

										var str2 = "saveRemarks"
												.concat(username);
										console.log("str2" + str2);
										
										
										/*****By sandip 26/10/2021*****/
										
										var value = $('#' + str2).val();
										
										var remarks = value.replaceAll('+', '%2B');
									
										console.log("value replaced " + remarks);
										
										/*****By sandip 26/10/2021*****/
										
										//var remarks = $('#' + str2).val();
										
										
										console.log("Remarks " + remarks);
                                        
										var courseid = $('#courseId').val();
										
										console.log("courseid " + courseid);
										//var maxScore = ${cpWieghtage};

										//	alert(courseid);
										//alert('Course Id is '+courseid);

										//alert(score);
										//alert(remarks);

										//if ((score > 0) && (score<=maxScore)) {
											if ((score > 0)) {
											
											
													$.ajax({
														type : 'GET',
														url : '${pageContext.request.contextPath}/saveClassParticipation?'
																+ 'studentUsername='
																+ username
																+ '&score='
																+ score
																+ '&remarks='
																+ remarks
																+ '&courseId='
																+ courseid,

														success : function(data) {
															console
																	.log("sucess messsgae e like "
																			+ likeId)
															console.log(data);
															const obj = JSON.parse(data);
															
															if(obj.Status === "Success"){
																
																alert("Saved");
															}else{
																alert(obj.msg);
															}
															     
														},
														
														/***By sandip 26/10/2021****/
														
														error: function(data){
															alert("Something went wrong!");
														 }
													    
														/***By sandip 26/10/2021****/

													});
										} else {
											alert("Marks should be greater than zero and less than or equal to assigned weight!");
										}

									});
				</script>
