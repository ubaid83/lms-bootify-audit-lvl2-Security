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
	<%
		if (session.getAttribute("studentFeedbackActive") != "Y") {
	%>
	<jsp:include page="../common/newLeftNavbar.jsp" />
	<jsp:include page="../common/newLeftSidebar.jsp" />
	<jsp:include page="../common/rightSidebar.jsp" />
	<%
		}
	%>

	<!-- DASHBOARD BODY STARTS HERE -->

	<!-- CUSTOM ALERT -->
	<div id="customAlert1"
		class="alertWrapper w-100 vh-100 position-fixed d-none">
		<div class="w-100 vh-100 position-relative">
			<div class="alert-box">
				<p class="alert-text fnt-13"></p>
				<button class="btn btn-sm w-25 btn-light alert-ok">Ok</button>
				<button class="btn btn-sm w-25 btn-danger alert-close">Cancel</button>
			</div>
		</div>
	</div>

	<div class="container-fluid m-0 p-0 dashboardWraper">

		<jsp:include page="../common/newTopHeader.jsp" />

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

							<li class="breadcrumb-item active" aria-current="page">
								Submit Feedback</li>
						</ol>
					</nav>

					<%-- <jsp:include page="../common/alert.jsp" /> --%>

					<div class="alert alert-success alert-dismissible"
						id="feedbackSuccess" style="display: none;" role="alert">
						<button type="button" class="close" data-dismiss="alert"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<strong>Success!</strong> Feedback Response Submitted successfully
					</div>


					<div class="alert alert-danger alert-dismissible"
						id="feedbackError" style="display: none;" role="alert">
						<button type="button" class="close" data-dismiss="alert"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<strong>Error!</strong> Error In Submitting Response
					</div>

					<!-- Input Form Panel -->
					<div class="card bg-white border">
						<div class="card-body">
							<div class="row">
								<div class="col-xs-12 col-sm-12">

									<div class="x_panel">
										<div class="wizard">


											<div class="wizard-inner">
												<div class="connecting-line"></div>
												<ul class="nav nav-tabs" role="tablist" id="listOfQuestnNum">

													<c:forEach var="question"
														items="${feedbackQuestionstoIterate}" varStatus="i">

														<c:choose>
															<c:when test="${1 eq i.count}">
																<li role="presentation" class="nav-item"
																	id="list${i.count}"><a href="#step${i.count}"
																	data-toggle="tab" aria-controls="step${i.count}"
																	role="tab" title="Question ${i.count}"
																	class="nav-link active" id="link${i.count}"> <span
																		class="round-tab"> 0${i.count} </span>
																</a></li>
															</c:when>
															<c:otherwise>

																<c:if test="${i.count lt 10}">
																	<li role="presentation" class="nav-item"
																		id="list${i.count}"><a href="#step${i.count}"
																		data-toggle="tab" aria-controls="step${i.count}"
																		role="tab" title="Question ${i.count}"
																		class="nav-link disabled" id="link${i.count}"> <span
																			class="round-tab"> 0${i.count} </span>
																	</a></li>
																</c:if>
																<c:if test="${i.count gt 9}">
																	<li role="presentation" class="nav-item"
																		id="list${i.count}"><a href="#step${i.count}"
																		data-toggle="tab" aria-controls="step${i.count}"
																		role="tab" title="Question ${i.count}"
																		class="nav-link disabled" id="link${i.count}"> <span
																			class="round-tab"> ${i.count} </span>
																	</a></li>
																</c:if>
															</c:otherwise>
														</c:choose>
													</c:forEach>

													<li role="presentation" class="nav-item"
														id="list${feedbackQuestionstoIterate.size() + 1}"><a
														href="#step${feedbackQuestionstoIterate.size() + 1}"
														data-toggle="tab"
														aria-controls="step${feedbackQuestionstoIterate.size() + 1}"
														role="tab"
														title="Question ${feedbackQuestionstoIterate.size() + 1}"
														class="nav-link disabled"
														id="link${feedbackQuestionstoIterate.size() + 1}"> <span
															class="round-tab"><c:out
																	value="${feedbackQuestionstoIterate.size() + 1}" /></span>
													</a></li>
												</ul>
											</div>


											<div class="clearfix"></div>

											<div class="tab-content">
												<%
													int i = 1;
												%>
												<c:forEach var="question"
													items="${feedbackQuestionstoIterate}" varStatus="i">





													<div class="tab-pane" role="tabpanel" id="step${i.count}">
														<form:form action="addStudentFeedbackResponseForCourse"
															id="studentFeedbackForm-${i.count}" method="post"
															modelAttribute="feedback">
															<input type="hidden" id="countValue" value="${i.count}" />
															<div class="card">
																<div class="card-body p-2">
																	<div
																		class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center border-bottom border-gray mb-2">
																		<p class="pb-2 mb-0 text-danger h4">
																			<i class="far fa-file-alt fa-lg text-secondary"></i>
																			Q. ${i.count} ${question.description}
																		</p>
																	</div>
																	<div class="clearfix"></div>
																	<div class="row flightbody-scroll">
																		<c:forEach var="entry"
																			items="${studentFeedbackMap[question.id]}"
																			varStatus="j">
																			<div class="col-md-8 col-sm-12">
																				<div class="form-group row">
																					<label class="col-sm-12 col-form-label h5">${j.count}
																						${entry.courseName} ,<br />Faculty Name:
																						${entry.facultyName}
																					</label>
																				</div>
																			</div>

																			<div class="col-md-4 col-sm-12">
																				<div class="form-group row">
																					<!--<label class="col-sm-12 col-form-label">Rating:</label>-->
																					<div class="col-sm-12">
																						<form:input type="hidden"
																							path="feedbackQuestions[${j.index}].studentFeedbackResponse.courseId"
																							value="${entry.courseId}" />
																						<form:input type="hidden"
																							path="feedbackQuestions[${j.index}].studentFeedbackResponse.studentFeedbackId"
																							value="${entry.id} " />
																						<form:input type="hidden"
																							path="feedbackQuestions[${j.index}].studentFeedbackResponse.feedbackQuestionId"
																							value="${question.id} " />

																						<c:if test="${'SINGLESELECT' eq question.type}">
																							<form:select id="answer${i.index}${j.index}"
																								path="feedbackQuestions[${j.index}].studentFeedbackResponse.answer"
																								class="form-control form-control-sm"
																								required="required">
																								<c:choose>
																									<c:when
																										test="${feedback.feedbackType eq 'it-feedback'}">
																										<form:option value="" disabled="true"
																											selected="true">Select Any One Option</form:option>
																									</c:when>
																									<c:otherwise>
																										<form:option value="" disabled="true"
																											selected="true">Select Rating (7 is highest)</form:option>
																									</c:otherwise>
																								</c:choose>

																								<c:if test="${not empty question.option1 }">
																									<c:choose>
																										<c:when
																											test="${question.option1 eq entry.answer}">
																											<option value="${question.option1}" selected>${question.option1}</option>
																										</c:when>
																										<c:otherwise>
																											<option value="${question.option1}">${question.option1}</option>
																										</c:otherwise>
																									</c:choose>
																								</c:if>
																								<c:if test="${not empty question.option2 }">
																									<c:choose>
																										<c:when
																											test="${question.option2 eq entry.answer}">
																											<option value="${question.option2}" selected>${question.option2}</option>
																										</c:when>
																										<c:otherwise>
																											<option value="${question.option2}">${question.option2}</option>
																										</c:otherwise>
																									</c:choose>
																								</c:if>
																								<c:if test="${not empty question.option3 }">
																									<c:choose>
																										<c:when
																											test="${question.option3 eq entry.answer}">
																											<option value="${question.option3}" selected>${question.option3}</option>
																										</c:when>
																										<c:otherwise>
																											<option value="${question.option3}">${question.option3}</option>
																										</c:otherwise>
																									</c:choose>
																								</c:if>
																								<c:if test="${not empty question.option4 }">
																									<c:choose>
																										<c:when
																											test="${question.option4 eq entry.answer}">
																											<option value="${question.option4}" selected>${question.option4}</option>
																										</c:when>
																										<c:otherwise>
																											<option value="${question.option4}">${question.option4}</option>
																										</c:otherwise>
																									</c:choose>
																								</c:if>
																								<c:if test="${not empty question.option5 }">
																									<c:choose>
																										<c:when
																											test="${question.option5 eq entry.answer}">
																											<option value="${question.option5}" selected>${question.option5}</option>
																										</c:when>
																										<c:otherwise>
																											<option value="${question.option5}">${question.option5}</option>
																										</c:otherwise>
																									</c:choose>
																								</c:if>
																								<c:if test="${not empty question.option6 }">
																									<c:choose>
																										<c:when
																											test="${question.option6 eq entry.answer}">
																											<option value="${question.option6}" selected>${question.option6}</option>
																										</c:when>
																										<c:otherwise>
																											<option value="${question.option6}">${question.option6}</option>
																										</c:otherwise>
																									</c:choose>
																								</c:if>
																								<c:if test="${not empty question.option7 }">
																									<c:choose>
																										<c:when
																											test="${question.option7 eq entry.answer}">
																											<option value="${question.option7}" selected>${question.option7}</option>
																										</c:when>
																										<c:otherwise>
																											<option value="${question.option7}">${question.option7}</option>
																										</c:otherwise>
																									</c:choose>
																								</c:if>
																								<c:if test="${not empty question.option8 }">
																									<c:choose>
																										<c:when
																											test="${question.option8 eq entry.answer}">
																											<option value="${question.option8}" selected>${question.option8}</option>
																										</c:when>
																										<c:otherwise>
																											<option value="${question.option8}">${question.option8}</option>
																										</c:otherwise>
																									</c:choose>
																								</c:if>

																							</form:select>
																						</c:if>

																						<c:if test="${'MULTISELECT' eq question.type}">
																							<form:select id="answer${i.index}${j.index}"
																								path="feedbackQuestions[${j.index}].studentFeedbackResponse.answer"
																								class="form-control form-control-sm"
																								required="required" multiple="multiple">
																								<c:choose>
																									<c:when
																										test="${feedback.feedbackType eq 'it-feedback'}">
																										<form:option value="" disabled="true"
																											selected="true">Select One or More Options</form:option>
																									</c:when>
																									<c:otherwise>
																										<form:option value="" disabled="true"
																											selected="true">Select Rating (7 is highest)</form:option>
																									</c:otherwise>
																								</c:choose>
																								<c:if test="${not empty question.option1 }">
																									<c:choose>
																										<c:when
																											test="${fn:contains(entry.multipleAnswers,question.option1)}">
																											<option value="${question.option1}" selected>${question.option1}</option>
																										</c:when>
																										<c:otherwise>
																											<option value="${question.option1}">${question.option1}</option>
																										</c:otherwise>
																									</c:choose>
																								</c:if>
																								<c:if test="${not empty question.option2 }">
																									<c:choose>
																										<c:when
																											test="${fn:contains(entry.multipleAnswers,question.option2)}">
																											<option value="${question.option2}" selected>${question.option2}</option>
																										</c:when>
																										<c:otherwise>
																											<option value="${question.option2}">${question.option2}</option>
																										</c:otherwise>
																									</c:choose>
																								</c:if>
																								<c:if test="${not empty question.option3 }">
																									<c:choose>
																										<c:when
																											test="${fn:contains(entry.multipleAnswers,question.option3)}">
																											<option value="${question.option3}" selected>${question.option3}</option>
																										</c:when>
																										<c:otherwise>
																											<option value="${question.option3}">${question.option3}</option>
																										</c:otherwise>
																									</c:choose>
																								</c:if>
																								<c:if test="${not empty question.option4 }">
																									<c:choose>
																										<c:when
																											test="${fn:contains(entry.multipleAnswers,question.option4)}">
																											<option value="${question.option4}" selected>${question.option4}</option>
																										</c:when>
																										<c:otherwise>
																											<option value="${question.option4}">${question.option4}</option>
																										</c:otherwise>
																									</c:choose>
																								</c:if>
																								<c:if test="${not empty question.option5 }">
																									<c:choose>
																										<c:when
																											test="${fn:contains(entry.multipleAnswers,question.option5)}">
																											<option value="${question.option5}" selected>${question.option5}</option>
																										</c:when>
																										<c:otherwise>
																											<option value="${question.option5}">${question.option5}</option>
																										</c:otherwise>
																									</c:choose>
																								</c:if>
																								<c:if test="${not empty question.option6 }">
																									<c:choose>
																										<c:when
																											test="${fn:contains(entry.multipleAnswers,question.option6)}">
																											<option value="${question.option6}" selected>${question.option6}</option>
																										</c:when>
																										<c:otherwise>
																											<option value="${question.option6}">${question.option6}</option>
																										</c:otherwise>
																									</c:choose>
																								</c:if>
																								<c:if test="${not empty question.option7 }">
																									<c:choose>
																										<c:when
																											test="${fn:contains(entry.multipleAnswers,question.option7)}">
																											<option value="${question.option7}" selected>${question.option7}</option>
																										</c:when>
																										<c:otherwise>
																											<option value="${question.option7}">${question.option7}</option>
																										</c:otherwise>
																									</c:choose>
																								</c:if>
																								<c:if test="${not empty question.option8 }">
																									<c:choose>
																										<c:when
																											test="${fn:contains(entry.multipleAnswers,question.option8)}">
																											<option value="${question.option8}" selected>${question.option8}</option>
																										</c:when>
																										<c:otherwise>
																											<option value="${question.option8}">${question.option8}</option>
																										</c:otherwise>
																									</c:choose>
																								</c:if>

																							</form:select>
																						</c:if>
																					</div>
																				</div>
																			</div>

																		</c:forEach>

																		<div class="col-sm-12">
																			<hr class="my-1" />
																		</div>

																	</div>
																</div>
																<%
																	if (i % 5 == 0) {
																%>

																<div class="card-footer text-right bg-white">


																	<%
																		if (i != 1) {
																	%>
																	<button type="button" id="btn${i.count}"
																		class="btn btn-secondary btn-common prev-step prev-button"
																		style="font-size: 12px;">Previous Question</button>
																	<%
																		}
																	%>

																	<button type="button" id="btn${i.count}"
																		class="btn btn-success btn-common next-step next-button"
																		style="font-size: 12px;"
																		onclick="showNextElements('<%=i%>');submitForm('studentFeedbackForm-${i.count}',${feedbackId},btn${i.count});
																	">
																		Submit and Next Question</button>

																</div>

																<%
																	} else {
																%>

																<div class="card-footer text-right bg-white">

																	<%
																		if (i != 1) {
																	%>
																	<%
																		int j = i - 1;
																						if (j % 5 == 0) {
																	%>

																	<button type="button" id="btn${i.count}"
																		class="btn btn-secondary btn-common prev-step prev-button"
																		onclick="showPrevElements('<%=j%>')">Previous
																		Question</button>

																	<%
																		} else {
																	%>

																	<button type="button" id="btn${i.count}"
																		class="btn btn-secondary btn-common prev-step prev-button">
																		Previous Question</button>

																	<%
																		}
																					}
																	%>


																	<button type="button" id="btn${i.count}"
																		class="btn btn-success btn-common next-step next-button"
																		onclick="submitForm('studentFeedbackForm-${i.count}',${feedbackId},'btn${i.count}')">
																		Submit and Next Question</button>


																</div>
																<%
																	}
																			i++;
																%>

															</div>
														</form:form>
													</div>


												</c:forEach>
												<div class="tab-pane" role="tabpanel"
													id="step${feedbackQuestionstoIterate.size() + 1}">
													<form:form action="addStudentFeedbackCommentsForCourse"
														id="studentFeedbackCommentForm" method="post"
														modelAttribute="feedback">
														<div class="card">
															<div class="card-body p-2">
																<div
																	class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center border-bottom border-gray mb-2">
																	<p class="pb-2 mb-0 text-danger h6">
																		<i class="far fa-file-alt fa-lg text-secondary"></i>Give
																		Your Comments
																	</p>
																</div>
																<div class="row flightbody-scroll">
																	<c:forEach var="entry" items="${allocatedCourseList}"
																		varStatus="j">

																		<form:input type="hidden"
																			path="feedbackQuestions[${j.index}].studentFeedbackResponse.studentFeedbackId"
																			value="${entry.id} " />


																		<div class="col-md-8 col-sm-12">
																			<div class="form-group row">
																				<label class="col-sm-12 col-form-label">${j.count}
																					${entry.courseName} ,<br />Faculty Name:
																					${entry.facultyName}
																				</label>
																			</div>
																		</div>

																		<div class="col-md-4 col-sm-12">
																			<div class="form-group row">
																				<!--<label class="col-sm-12 col-form-label">Comments</label>-->
																				<div class="col-sm-12">
																					<form:textarea class="form-control form-control-sm"
																						path="feedbackQuestions[${j.index}].studentFeedbackResponse.comments"
																						id="comments" rows="5" placeholder="" value="" />
																				</div>
																			</div>
																		</div>
																		<div class="col-sm-12">
																			<hr class="my-1" />
																		</div>
																	</c:forEach>
																</div>
															</div>
															<div class="card-footer text-right bg-white">
																<%
																	int j = i - 1;
																		if (j % 5 == 0) {
																%>

																<button type="button" id="btn${i.count}"
																	class="btn  btn-secondary btn-common prev-step prev-button"
																	onclick="showPrevElements('<%=j%>')">Previous
																	Question</button>

																<%
																	} else {
																%>

																<button type="button" id="btn${i.count}"
																	class="btn  btn-secondary btn-common prev-step prev-button">
																	Previous Question</button>

																<%
																	}
																%>



																<button type="button"
																	class="btn  btn-success btn-common next-step next-button"
																	onclick="submitForm('studentFeedbackCommentForm',${feedbackId})">Finish</button>
															</div>
														</div>
													</form:form>
												</div>
												<div class="clearfix"></div>
											</div>

										</div>
									</div>
								</div>
							</div>
						</div>
					</div>

					<!-- Results Panel -->


					<!-- <div class="card bg-white border">
								<div class="card-body">								
									
								</div>
								</div> -->




				</div>

				<!-- SIDEBAR START -->
				<jsp:include page="../common/newSidebar.jsp" />
				<!-- SIDEBAR END -->
				<jsp:include page="../common/footer.jsp" />

				<script>
$(document).ready(function() {
	
	var counterValue = $('#countValue').val();
	console.log('value received counter--->'+counterValue);
	if(counterValue==1){
		$('#step1').addClass('active');
		
		var limit = 5;
		var more = 0;
		$("#listOfQuestnNum li").each(function(index) {
			if (index >= limit) {
				$(this).hide();
				more++;
			}
		});
	}
});





function showNextElements(id) {
		var limit = 5;
		var more = 0;
		console.log('id got--->'+ id)
		$("#listOfQuestnNum li").each(function(index) {
			console.log('index--->'+index);
			if (index+1 <= id) {
				$(this).hide();

			} else {
				if (more < limit) {
					$(this).show();
					more++;
				}
			}
		});
	};
	
	
	function showPrevElements(id) {
		var firstElement = id -4;
		var lastElement = id;
		var limit = 5;
		var more = 0;
		console.log('id got--->'+ id)
		$("#listOfQuestnNum li").each(function(index) {
			console.log('index--->'+index);
			if (index+1 == firstElement) {
				if(more<limit){
					$(this).show();
					firstElement =  firstElement +1;
					more++;
					console.log('more--->'+more);
				}
				else{
					$(this).hide();
				}
			} else {
				
					$(this).hide();
				
			}
		});
	};

</script>

				<script>

//Initialize tooltips
$('.nav-tabs > li a[title]').tooltip();

//Wizard
$('a[data-toggle="tab"]').on('show.bs.tab', function(e) {

	//alert('function called');

	var $target = $(e.target);

	if ($target.hasClass('disabled')) {
		return false;
	}
});





/* $('.flightbody-scroll').slimScroll({
	height : '38vh',
	size : '5px',
	BorderRadius : '5px'
}); */

/* $('.swal-button--confirm').click(function(){
	console.log('swal button clicked');
	window.location.href = '${pageContext.request.contextPath}/viewFeedbackDetails';
}); */


$(".prev-step").click(
		function(e) {

			var $active = $('.wizard .nav-tabs .nav-item .active');
			var $activeli = $active.parent("li");

			$($activeli).prev().find('a[data-toggle="tab"]')
					.removeClass("disabled");
			$($activeli).prev().find('a[data-toggle="tab"]').click();

		});
		

window.submitForm = function submitForm(formId, feedbackId, btnId) {
	
	if( formId === "studentFeedbackCommentForm"){
	
		
	 	$.post('addStudentFeedbackCommentsForCourse?feedbackId='+feedbackId,$('#' + formId).serialize()).then(
				function(response) {
					
				}).done(function(response) {
					//swal('SUCCESS!',' Your Feedback Is Completed', 'success');
					
					 
					/* swal({
					    title: "SUCCESS!",
					    text: "Your Feedback Is Completed!",
					    type: "success"
					}).then(function() {
					    window.location = "${pageContext.request.contextPath}/viewFeedbackDetails";
					}); */
					
					
					$('.alertWrapper').removeClass('d-none');
				    $('.alert-text').html('Your Feedback Is Completed!');
				    $('.alert-ok').html('Ok');
				    $('.alert-close').addClass('d-none');
				    
				     $('.alert-ok').click(function(){
				    	console.log('alert ok clicked'); 
				    	window.location = "${pageContext.request.contextPath}/viewFeedbackDetails";
				    });
 					
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
						localStorage.setItem("lastname", formId+"Smith"+$('#' + formId).serialize());
						console.log("val :"+localStorage.getItem("lastname"));
						//{ courseId: courseId, feedbackId : feedbackId}
					 	$.post('addStudentFeedbackResponseForCourse?feedbackId='+feedbackId,$('#' + formId).serialize()).then(
								function(response) {
									
								}).done(function(response) {
									
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
				   
					 	myfunction();
		  }
		 else {  
			 
		 	alert("please make selections");
		 	} 
				   
	}
}; 


function myfunction(){
/* 	
$(".next-step").click(
		function(e) { */
			var $active = $('.wizard .nav-tabs .nav-item .active');
			var $activeli = $active.parent("li");
			
			
			
			
			console.log('csdlfjsd'+$active.attr('id'));
			console.log('css id--->'+ $($activeli).next().attr('id'));
			
			
			
		
			$($activeli).next().find('a[data-toggle="tab"]')
					.removeClass("disabled");
			
			$($activeli).next().find('a[data-toggle="tab"]')
			.click();
			console.log('css id--->'+ $($activeli).next().attr('id'));
		
			
			console.log('css id 1222--->'+ $($activeli).next().attr('id'));
		/* }); */
}



	

</script>