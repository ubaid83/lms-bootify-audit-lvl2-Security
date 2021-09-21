
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page import="com.spts.lms.web.utils.*"%>
<%@page import="com.spts.lms.web.utils.*"%>
<%@page import="java.util.List"%>
<%@page import="java.util.*"%>
<%@page import="com.spts.lms.beans.feedback.*"%>


<!--

//-->
</script>
<!-- <script
	src="//ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<script
	src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script> -->


<html lang="en">
<jsp:include page="../common/css.jsp" />

<body class="nav-md footer_fixed">

	<div class="loader"></div>


	<div class="container body">
		<div class="main_container">

			<jsp:include page="../common/leftSidebar.jsp">
				<jsp:param name="courseId" value="${courseId}" />
			</jsp:include>




			<jsp:include page="../common/topHeader.jsp" />


			<div class="right_col" role="main">

				<div class="dashboard_container">

					<div class="dashboard_container_spacing">

						<div class="breadcrumb">
							<a href="index.html">Dashboard</a> <i class="fa fa-angle-right"></i>
							View Each Assignment
						</div>
						<jsp:include page="../common/alert.jsp" />


						<div class="alert alert-success alert-dismissible"
							id="feedbackSuccess" style="display: none;" role="alert">
							<button type="button" class="close" data-dismiss="alert"
								aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
							<strong>Success!</strong> Feedback Response Submitted
							successfully
						</div>


						<div class="alert alert-danger alert-dismissible"
							id="feedbackError" style="display: none;" role="alert">
							<button type="button" class="close" data-dismiss="alert"
								aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
							<strong>Error!</strong> Error In Submitting Response
						</div>


						<div class="row">
							<div class="col-xs-12 col-sm-12">

								<div class="x_panel">
									<div class="x_title">
										<h2>Submit Feedback</h2>
										<ul class="nav navbar-right panel_toolbox">

											<li><a href="#"><span>View All</span></a></li>
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
											<li><a class="close-link"><i class="fa fa-close"></i></a></li>

										</ul>
										<div class="clearfix"></div>
									</div>
									<div class="x_itemCount" style="display: none;">
										<div class="image_not_found">
											<i class="fa fa-newspaper-o"></i>
											<p>
												<label class=""></label> ${fn:length(listOfFeedback)}
												Feedbacks
											</p>
										</div>
									</div>
									<div class="x_content">




										<div class="tableresponsive1">
											<table class="table  table-hover scroller" id="multiFeedback">
												<thead>
														<c:if test="${ allocatedCourseList.size() lt 3}">
														<tr class="scroll">
													</c:if>
													<c:if test="${allocatedCourseList.size() gt 2}">
														<tr>
													</c:if>

														<th>Q No.</th>
														<th>Question</th>

														<%-- <c:forEach var="entry" items="${allocatedCourseList}"
															varStatus="i">
															<th><c:out value="${entry.courseName}" /></th>
														</c:forEach> --%>

														<c:forEach var="entry" items="${allocatedCourseList}"
															varStatus="i">
															<th><c:out
																	value="${entry.courseName}  ,Faculty Name: ${entry.facultyName}" /> </th>
														</c:forEach>

														<th>Action</th>
													</tr>
												</thead>
												<tfoot>
													<tr>
														<th></th>

													</tr>
												</tfoot>
												<tbody>

													<c:forEach var="question"
														items="${feedbackQuestionstoIterate}" varStatus="i">
														<form:form action="addStudentFeedbackResponseForCourse"
															id="studentFeedbackForm-${i.index}" method="post"
															modelAttribute="feedback">


																<c:if test="${ allocatedCourseList.size() lt 3}">
														<tr class="scroll">
													</c:if>
													<c:if test="${allocatedCourseList.size() gt 2}">
														<tr>
													</c:if>
																<td><c:out value="${i.count}" /></td>
																<td><c:out value="${question.description}" /></td>




																<c:forEach var="rating"
																	items="${questionResponsemap[question.id]}"
																	varStatus="j">


																	<td>
																		<p>Rating:</p> <form:input type="hidden"
																			path="feedbackQuestions[${j.index}].studentFeedbackResponse.courseId"
																			value="${rating.courseId}" /> <form:input
																			type="hidden"
																			path="feedbackQuestions[${j.index}].studentFeedbackResponse.studentFeedbackId"
																			value="${rating.stuFeedbackId} " /> <form:input
																			type="hidden"
																			path="feedbackQuestions[${j.index}].studentFeedbackResponse.feedbackQuestionId"
																			value="${question.id} " /> <form:select
																			id="answer${i.index}${status.index}"
																			path="feedbackQuestions[${j.index}].studentFeedbackResponse.answer"
																			class="form-control select-width" required="required">
																			<form:option value="" disabled="true" selected="true">Select Rating (7 is highest)</form:option>

																			<c:if test="${not empty question.option1 }">
																				<c:choose>
																					<c:when test="${question.option1 eq rating.answer}">
																						<option value="${question.option1}" selected>${question.option1}</option>
																					</c:when>
																					<c:otherwise>
																						<option value="${question.option1}">${question.option1}</option>
																					</c:otherwise>
																				</c:choose>
																			</c:if>
																			<c:if test="${not empty question.option2 }">
																				<c:choose>
																					<c:when test="${question.option2 eq rating.answer}">
																						<option value="${question.option2}" selected>${question.option2}</option>
																					</c:when>
																					<c:otherwise>
																						<option value="${question.option2}">${question.option2}</option>
																					</c:otherwise>
																				</c:choose>
																			</c:if>
																			<c:if test="${not empty question.option3 }">
																				<c:choose>
																					<c:when test="${question.option3 eq rating.answer}">
																						<option value="${question.option3}" selected>${question.option3}</option>
																					</c:when>
																					<c:otherwise>
																						<option value="${question.option3}">${question.option3}</option>
																					</c:otherwise>
																				</c:choose>
																			</c:if>
																			<c:if test="${not empty question.option4 }">
																				<c:choose>
																					<c:when test="${question.option4 eq rating.answer}">
																						<option value="${question.option4}" selected>${question.option4}</option>
																					</c:when>
																					<c:otherwise>
																						<option value="${question.option4}">${question.option4}</option>
																					</c:otherwise>
																				</c:choose>
																			</c:if>
																			<c:if test="${not empty question.option5 }">
																				<c:choose>
																					<c:when test="${question.option5 eq rating.answer}">
																						<option value="${question.option5}" selected>${question.option5}</option>
																					</c:when>
																					<c:otherwise>
																						<option value="${question.option5}">${question.option5}</option>
																					</c:otherwise>
																				</c:choose>
																			</c:if>
																			<c:if test="${not empty question.option6 }">
																				<c:choose>
																					<c:when test="${question.option6 eq rating.answer}">
																						<option value="${question.option6}" selected>${question.option6}</option>
																					</c:when>
																					<c:otherwise>
																						<option value="${question.option6}">${question.option6}</option>
																					</c:otherwise>
																				</c:choose>
																			</c:if>
																			<c:if test="${not empty question.option7 }">
																				<c:choose>
																					<c:when test="${question.option7 eq rating.answer}">
																						<option value="${question.option7}" selected>${question.option7}</option>
																					</c:when>
																					<c:otherwise>
																						<option value="${question.option7}">${question.option7}</option>
																					</c:otherwise>
																				</c:choose>
																			</c:if>
																			<c:if test="${not empty question.option8 }">
																				<c:choose>
																					<c:when test="${question.option8 eq rating.answer}">
																						<option value="${question.option8}" selected>${question.option8}</option>
																					</c:when>
																					<c:otherwise>
																						<option value="${question.option8}">${question.option8}</option>
																					</c:otherwise>
																				</c:choose>
																			</c:if>

																		</form:select>
																	</td>


																</c:forEach>






																<c:url var="submitUrl"
																	value="addStudentFeedbackResponseForCourse">
																	<c:param name="courseId" value="${entry.key}"></c:param>
																	<c:param name="feedbackId" value="${feedbackId}"></c:param>
																</c:url>

																<td><a class="btn btn-large btn-primary"
																	onclick="submitForm('studentFeedbackForm-${i.index}',${feedbackId})">Submit</a>




																</td>
															</tr>
														</form:form>

													</c:forEach>
													<!--  -->
													<form:form action="addStudentFeedbackCommentsForCourse"
														id="studentFeedbackCommentForm" method="post"
														modelAttribute="feedback">



															<c:if test="${ allocatedCourseList.size() lt 3}">
														<tr class="scroll">
													</c:if>
													<c:if test="${allocatedCourseList.size() gt 2}">
														<tr>
													</c:if>
															<td><c:out value="${i.count}" /></td>
															<td><c:out value=" Comments Section " /></td>
															<c:forEach var="entry" items="${allocatedCourseList}"
																varStatus="i">

																<form:input type="hidden"
																	path="feedbackQuestions[${i.index}].studentFeedbackResponse.studentFeedbackId"
																	value="${entry.id} " />

																<td>
																	<p>Comments:</p> <form:textarea class="form-control"
																		path="feedbackQuestions[${i.index}].studentFeedbackResponse.comments"
																		id="comments" rows="5" placeholder="" value="" />


																</td>


															</c:forEach>

															<c:url var="submitUrl"
																value="addStudentFeedbackResponseForCourse">
																<c:param name="courseId" value="${entry.key}"></c:param>
																<c:param name="feedbackId" value="${feedbackId}"></c:param>
															</c:url>

															<td><a class="btn btn-large btn-primary"
																onclick="submitForm('studentFeedbackCommentForm',${feedbackId})">Submit</a>

															</td>
															</tr>
													</form:form>

													<!--  -->
												</tbody>
											</table>
										</div>

										<div class="clearfix"></div>

										<a class="btn btn-large btn-primary pull-right"
											href="<c:url value="/viewFeedbackDetails" />">Finish</a>


									</div>
								</div>
							</div>
						</div>


					</div>



				</div>

			</div>
			<!-- /page content -->




			<jsp:include page="../common/footer.jsp" />

		</div>
	</div>





</body>
<script>
$(document).ready(function() {
	
	/* var table = $('#multiFeedback').DataTable( {
        fixedHeader: {
            header: true,
            footer: true,
            "scrollY": "200px",
            
        }
    } ); */

	window.submitForm = function submitForm(formId,feedbackId) {
    	
    	if(formId === "studentFeedbackCommentForm"){
    	
    		
    	 	$.post('addStudentFeedbackCommentsForCourse?feedbackId='+feedbackId,$('#' + formId).serialize()).then(
					function(response) {
						
					}).done(function(response) {
						alert("Response Submited Successfully !!! ")
						$("#feedbackSuccess").show(100);
						$("#feedbackError").hide();
				if (response){
					location.reload();
				}
			}).fail(function(xhr, status, error) {
				$("#feedbackError").show(100);
				$("#feedbackSuccess").hide();
			}).always(function() {
			});
	   
    	 	
    	 	
    	}else{
    			 if ($('#'+formId)[0].checkValidity()) {
    				
							console.log("data is ====="+$('#' + formId).serialize());
							//{ courseId: courseId, feedbackId : feedbackId}
						 	$.post('addStudentFeedbackResponseForCourse?feedbackId='+feedbackId,$('#' + formId).serialize()).then(
									function(response) {
										
									}).done(function(response) {
										alert("Response Submited Successfully !!! ")
										$("#feedbackSuccess").show(100);
										$("#feedbackError").hide();
								if (response){
									location.reload();
								}
							}).fail(function(xhr, status, error) {
								$("#feedbackError").show(100);
								$("#feedbackSuccess").hide();
							}).always(function() {
							});
					   
			 }
			 else {  
			 	alert("please make selections");
			 	}
					   
    	}
	}; 
	



	});
</script>
</html>