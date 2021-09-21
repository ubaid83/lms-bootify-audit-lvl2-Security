		<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
			pageEncoding="ISO-8859-1"%>
		
		<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
		<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
		<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
		<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
		<%@ page import="java.util.*"%>
		<%@ page import="java.util.Random"%>
		
		
		<%@ taglib prefix="sec"
			uri="http://www.springframework.org/security/tags"%>
		
		<jsp:include page="../common/newDashboardHeader.jsp" /> 
		
		<div id="customAlert1" class="alertWrapper w-100 vh-100 position-fixed d-none">
        <div class="w-100 vh-100 position-relative">
            <div class="alert-box">
                <p class="alert-text fnt-13">Are you sure you want to submit? Once submited you cannot preview or edit the answers.</p>
                
                <button type="button" class="btn btn-sm w-25 btn-light alert-ok" id="nextQuest" onclick="saveStudentsTest('${test.studentTest.id}');showModal('modal-${test.testQuestions.size()-1}','modal-result','nav-${test.testQuestions.size()}')">Submit</button>
                <!-- <button class="btn btn-sm w-25 btn-light alert-ok">Submit</button> -->
                <button class="btn btn-sm w-25 btn-danger alert-close">Cancel</button>
            </div>
        </div>
    </div>
		
		
		<div class="d-flex" id="assignmentPage">
		
			<sec:authorize access="hasRole('ROLE_FACULTY')">
		<jsp:include page="../common/newLeftNavBarFaculty.jsp"/>
		<jsp:include page="../common/newLeftSidebarFaculty.jsp"/>
		<jsp:include page="../common/rightSidebarFaculty.jsp"/>
		</sec:authorize>
		<sec:authorize access="hasRole('ROLE_STUDENT')">
		<jsp:include page="../common/newLeftNavbar.jsp" />
		<jsp:include page="../common/newLeftSidebar.jsp" />
		<jsp:include page="../common/rightSidebar.jsp" />
		</sec:authorize>
			<sec:authorize access="hasRole('ROLE_ADMIN')">
		<jsp:include page="../common/newAdminLeftNavBar.jsp"/>
	<jsp:include page="../common/rightSidebarAdmin.jsp" />
	</sec:authorize>
		
		<!-- <div class="alertWrapper w-100 vh-100 position-fixed d-none">
			<div class="w-100 vh-100 position-relative">
				<div class="alert-box">
					<p class="alert-text fnt-13"></p>
					<button class="btn btn-sm w-25 btn-light alert-ok">Ok</button>
					<button class="btn btn-sm w-25 btn-danger alert-close">Cancel</button>
				</div>
			</div>
		</div> -->
		<div class="container-fluid m-0 p-0 dashboardWraper assignmentW">
		
			<div class="container mt-5">
				<div class="row">
					<input type="hidden" id="duration" value="${durationsLeft}" /> <input
		type="hidden" id="testCompleted" value="${action}" /> <input
		type="hidden" id="durationCompletedByStudent"
		value="${durationCompletedByStudent}" />
		<!-- Modal Test -->
		<div id="modalTest">
			<div class="modal fade fnt-13" id="testModule" tabindex="-1"
				role="dialog" aria-labelledby="giveTestTitle" aria-hidden="true">
				<div
					class="modal-dialog modal-dialog-centered modal-dialog-scrollable"
					role="document">
					<div class="modal-content">
						<div class="modal-header text-white">
							<h5 class="modal-title" id="giveTestTitle">${test.testName}</h5>
		</div>
		<input type="hidden" id="dateTimeVal" value="${dateTime}" />
		<input type="hidden" id="studentFilePath" value="${relacedStudentFilePath}" />
		<div class="modal-body pt-0 bg-light">
		
			<div class="row">
				<div class="mobTimerW col-12">
					<p class="font-weight-bold text-center mb-0 mt-3">TIMER</p>
					<div class="col-12 m-auto" id="CountDownTimerMobile"
						style="width: 250px;" data-timer="7210"></div>
		</div>
		<div
			class="col-lg-2 col-md-2 col-sm-3 border-right pt-3 testQToggle scrollBlue">
			<div class="col-12 p-0 m-0">
				<h6 class="mb-0 pb-0">Questions</h6>
				<hr class="mt-1" />
		
				<div class="row">
		
					<c:forEach items="${test.testQuestions}" var="testQuestion"
			varStatus="status">
		
			<div class="col-6 mt-1">
				<c:choose>
					<c:when test="${status.first}">
						<button type="button"
							class="btn btn-sm activeQbtn btn-outline-secondary activeBtnClass"
							id="nav-${status.index}"
							onclick="showModalOnClick('modal-${status.index}','nav-${status.index}')">Q.
							${status.count}</button>
					</c:when>
					<c:otherwise>
						<button
							class="btn btn-sm  btn-outline-secondary activeBtnClass"
							type="button" id="nav-${status.index}"
							onclick="showModalOnClick('modal-${status.index}','nav-${status.index}')">Q.
							${status.count}</button>
					</c:otherwise>
				</c:choose>
			</div>
		</c:forEach>
		
		
		
				</div>
			</div>
		</div>
		<div
			class="col-lg-8 col-md-8 col-sm-9 border-right bg-white pt-3 testQA">
			<c:forEach items="${test.testQuestions}" var="testQuestion"
		varStatus="status">
		<div class="col-12 modalClass" id="modal-${status.index}">
		
		<form:form action="addStudentQuestionResponse"
			method="post" modelAttribute="test"
			id="studentTestForm-${status.index}">
			<form:input type="hidden" path="testQuestions[${status.index}].studentQuestionResponse.attemptNo" value="${test.studentTest.attempt}"/>
			<form:input type="hidden"
				path="testQuestions[${status.index}].studentQuestionResponse.studentTestId"
				value="${test.studentTest.id}" id="StudentTestIdValue" />
			<form:input type="hidden"
				path="testQuestions[${status.index}].studentQuestionResponse.questionId"
				value="${testQuestion.id}" />
			<form:input type="hidden" path="testQuestions[${status.index}].studentQuestionResponse.studentFilePath" value="${studentFileP}" /> 
			<ul class="list-unstyled">
		
		
		<%-- <c:if test="${testQuestion.questionType eq 'Numeric'}"></c:if> --%>
		
				<li>
					<p class="bg-blue subHead1 text-white p-3"
						id="testQuest"><h3>Marks: ${testQuestion.marks}</h3>${testQuestion.description}</p>
		
		
		
				</li>
		
		
		
				<li>
					<ul class="ml-4 list-unstyled moduleTestAns">
		
						<form:textarea rows="15" cols="40"
							class="form-group scrollDescription w-100"
							path="testQuestions[${status.index}].answers"
							id="checkField${status.count}1"
							style="margin-top: 30px;margin-bottom:10px resize: none;" />
		
		
		
		
		
		
		
		
		
		
					</ul>
				</li>
		
		
			</ul>
		
		
		
		
		
			<div class="modal-footer text-center">
				<div class="col-lg-7 col-md-7 col-sm-10 m-auto">
					<div class="row">
						<c:if test="${not status.first}">
							<div
								class="col-lg-3 col-md-3 col-sm-3 col-6 mr-auto ml-auto mb-1 pl-1 pr-1 prevWrap">
							<button type="button" class="w-100 btn-next"
									id="prevQuest"
									onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');showModal('modal-${status.index }','modal-${status.index - 1}','nav-${status.index - 1}')">
									Previous</button>
							</div>
						</c:if>
						<c:choose>
							<c:when test="${status.last}">
								<button type="button" class="w-100 btn-next"
									id="nextQuest"
									onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');showModal('modal-${status.index }','modal-added','nav-${status.index + 1}')">SAVE
									AND COMPLETE</button>
							</c:when>
							<c:otherwise>
								<div
									class="col-lg-3 col-md-3 col-sm-3 col-6 mr-auto ml-auto mb-1 pl-1 pr-1 nextWrap">
									<button type="button" class="w-100 btn-next"
										id="nextQuest"
										onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');showModal('modal-${status.index }','modal-${status.index +1}','nav-${status.index + 1}')">SAVE
										AND NEXT</button>
								</div>
								<div
									class="col-lg-3 col-md-3 col-sm-3 col-6 mr-auto ml-auto mb-1 pl-1 pr-1 skipWrap">
									<button type="button" class="w-100 btn-skip"
										id="skipQuest"
										onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');showModal('modal-${status.index }','modal-${status.index + 1}','nav-${status.index + 1}')">SKIP</button>
								</div>
							</c:otherwise>
						</c:choose>
					</div>
		
		
		
		
		
		
		
				</div>
			</div>
			<!-- <div class="modal-footer text-center">
				<div class="col-lg-7 col-md-7 col-sm-10 m-auto">
					<div class="row">
						<div
							class="col-lg-3 col-md-3 col-sm-3 col-6 mr-auto ml-auto mb-1 pl-1 pr-1 prevWrap d-none">
							<button class="w-100 btn-prev" disabled>PREVIOUS</button>
						</div>
						<div
							class="col-lg-3 col-md-3 col-sm-3 col-6 mr-auto ml-auto mb-1 pl-1 pr-1 nextWrap">
							<button class="w-100 btn-next" id="nextQuest">SAVE
								AND NEXT</button>
						</div>
						<div
							class="col-lg-3 col-md-3 col-sm-3 col-6 mr-auto ml-auto mb-1 pl-1 pr-1 skipWrap">
							<button class="w-100 btn-skip">SKIP</button>
						</div>
						<div
							class="col-lg-3 col-md-3 col-sm-3 col-6 mr-auto ml-auto mb-1 pl-1 pr-1 completeWrap d-none">
							<button class="w-100 btn-complete" disabled>COMPLETE</button>
						</div>
		
					</div>
				</div>
			</div> -->
		</form:form>
		
		
		</div>
		
		</c:forEach>
		
		
		<div class="col-12 modalClass" id="modal-added">
		
			<ul class="list-unstyled">
				<li>
					<p class="bg-blue subHead1 text-white p-3" id="testQuest">You
						have reached to the end of Test.</p>
				</li>
				<li>
					<ul class="ml-4 list-unstyled moduleTestAns">
						<li class="mb-3 border">
		
		
							<p>
								Please click submit button to submit your answers. <br>
								<span>If you wish to review answers,</span><br> <span>you
									can click on any of the questions.</span><br> <span>Total
									Questions:</span><br>
							<p id="totalQuestions"></p> <span>Total Question
								Answered: </span><br>
							<p id="totalQuestionAnswered"></p>
		
		
						</li>
						<li>
						<button type="button" class="w-100 btn-next" id="testSubmitFinal">Submit</button>
							<%-- <button type="button" class="w-100 btn-next"
								id="nextQuest"
								onclick="saveStudentsTest('${test.studentTest.id}');
						showModal('modal-${test.testQuestions.size()-1}','modal-result','nav-${test.testQuestions.size()}')">Submit</button>
						 --%></li>
		
		
		
		
		
					</ul>
				</li>
		
			</ul>
		
		</div>
		
		<div class="col-12 modalClass" id="modal-result">
		
			<ul class="list-unstyled">
				<li>
					<p class="bg-blue subHead1 text-white p-3" id="testQuest">
						<strong>Congratulations!</strong> You have completed test
						successfully.<br>
					</p>
				</li>
				<li>
		
		
		
					<p>
					<c:if test="${test.showResultsToStudents eq 'Y'}">
				<span>Total Score:</span><br>
				<p id="totalScoreParagraph"></p> 
				
				<span>Status: </span><br>
				<p id="passParagraph"></p> 
			</c:if>
			<span>Reattempt:</span><br>
			Allowed ${test.maxAttempt} times
		
		
		</li>
		
		
		<li>
		
			<sec:authorize access="hasAnyRole('ROLE_FACULTY','ROLE_ADMIN')">
				<a type="button" class="btn btn-modalClose" href="${pageContext.request.contextPath}/testList"
				>Close</a>
			</sec:authorize>
			<sec:authorize access="hasRole('ROLE_STUDENT')">
				<a type="button" class="btn btn-modalClose" href="${pageContext.request.contextPath}/viewTestFinal?courseId=${test.courseId}"
				>Close</a>
			</sec:authorize>
		
					</li>
		
				</ul>
		
			</div>
		
		
		</div>
		<div class="col-2 timerRight pt-3 bg-light">
			<div class="pl-4 col-12">
				<p class="font-weight-bold text-center">TIMER</p>
				<div class="row">
					<div class="col-12 ml-auto" style="width: 300px;"
			id="CountDownTimer" data-timer="${testTime}"></div>
				</div>
			</div>
		</div>
		
		<%-- 	<div class="col-xs-12 col-sm-2 text-center">
							<div id="DateCountdown" data-date=""
								style="width: 160px; height: 360px; padding: 0px; box-sizing: border-box;">
								<div class="timer_right_img">
									<img src="<c:url value="/resources/images/watch.png"/>" />
								</div>
							</div>
						</div> --%>
		
							</div>
						</div>
		
					</div>
				</div>
			</div>
		
		</div>
		<%-- <div
			class="col-lg-9 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto">
		
			<div class="bg-white pb-5">
				<nav aria-label="breadcrumb">
					<ol class="breadcrumb">
						<li class="breadcrumb-item"><a href="dashboard.html">Dashboard</a></li>
						<li class="breadcrumb-item active" aria-current="page">Assignment</li>
					</ol>
				</nav>
				<!-- FILTER STARTS -->
				<div class="col-12">
					<form>
						<div class="form-row">
							<div class="col-lg-4 col-md-4 col-sm-12 mt-3">
								<label class="sr-only">Select Semester</label> <select
									id="assignSemTest" name="semester" class="form-control">
									<c:if test="${acadSession eq  null }">
										<option value="">Select Semester</option>
									</c:if>
									<c:forEach items="${sessionWiseCourseListMap}" var="sList"
										varStatus="count">
										<c:if test="${sList.key eq acadSession}">
											<option value="${sList.key}" selected>${sList.key}</option>
										</c:if>
										<c:if test="${sList.key ne acadSession }">
											<option value="${sList.key}">${sList.key}</option>
										</c:if>
									</c:forEach>
								</select>
							</div>
							<div class="col-lg-4 col-md-4 col-sm-12 mt-3">
								<label class="sr-only">Select Course</label> <select
									id="assignCourseTest" name="courseId" class="form-control">
		
									<c:if test="${courseId ne null && acadSession ne null}">
		
										<c:forEach var='cList'
											items='${ sessionWiseCourseListMap[acadSession] }'>
											<c:if test="${cList.id eq courseId}">
												<option value="${cList.id}" selected>${cList.courseName}</option>
											</c:if>
											<c:if test="${cList.id ne courseId}">
												<option value="${cList.id}">${cList.courseName}</option>
											</c:if>
										</c:forEach>
									</c:if>
		
								</select>
							</div>
							<div class="col-lg-4 col-md-4 col-sm-12 mt-3">
								<div class="input-group mb-3">
									<input type="text" class="form-control"
										placeholder="Test">
									<div class="input-group-append">
										<button class="btn btn-outline-danger" type="button"
											id="searchTest">
											<i class="fas fa-search"></i>
										</button>
									</div>
								</div>
							</div>
						</div>
					</form>
				</div>
				<!-- FILTER ENDS -->
				<!-- GRAPH STARTS -->
				<section
					class="col-lg-12 col-md-12 col-sm-12 mb-5 fnt-13 chartWrap">
					<canvas id="testBarChartForCourse"></canvas>
				</section>
				<!-- GRAPH ENDS -->
				<section class="searchTest" id="testTableSection">
					<div class="col-12 bg-dark text-white subHead1">
						<h6 class="p-2 mb-3">SEARCH TEST</h6>
					</div>
		
					<div class="col-12">
		
						<div class="col-3 p-0 mr-auto mt-3 mb-3">
							<div class="input-group flex-nowrap input-group-sm">
								<div class="input-group-prepend">
									<span class="input-group-text cust-select-span"
										id="addon-wrapping">Filter Tests</span>
								</div>
		
								<select class="cust-select">
									<option>All</option>
									<option>Completed</option>
									<option>Pending</option>
									<option>Failed</option>
								</select>
							</div>
		
						</div>
						<div class="table-responsive mb-3 testAssignTable">
							<table class="table table-striped table-hover" id="viewTestTable">
								<thead>
									<tr>
										<th scope="col">No.</th>
										<th scope="col">Name</th>
										<th scope="col">Term</th>
										<th scope="col">Start Date</th>
										<th scope="col">End Date</th>
										<th scope="col">Type</th>
										<th scope="col">Status</th>
										<th scope="col">Action</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="test" items="${testListByCourse}"
										varStatus="status">
										<tr>
											<th scope="row">${status.count}</th>
											<td>${test.testName}</td>
											<td>${test.acadMonth}</td>
											<td>${test.startDate}</td>
											<td>${test.endDate}</td>
											<td>${test.testType}</td>
		
											<c:choose>
												<c:when test="${test.testCompleted eq 'Y'}">
													<td><i class="fas fa-check-circle text-success"></i>
														Completed</td>
												</c:when>
												<c:otherwise>
													<td><i class="fas fa-hourglass-start text-orange"></i>
														Pending</td>
												</c:otherwise>
											</c:choose>
		
											<td><input type="button" value="Give Test" onclick="callAlert(${test.id})"></td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
		
						<div class="row">
							<div class="col-3">
								<div class="input-group flex-nowrap input-group-sm">
									<div class="input-group-prepend">
										<span class="input-group-text cust-select-span"
											id="addon-wrapping">Show</span>
									</div>
		
									<select class="cust-select">
										<option>5</option>
										<option>10</option>
										<option>15</option>
										<option>20</option>
										<option>25</option>
										<option>30</option>
										<option>All</option>
									</select>
								</div>
		
							</div>
		
							<nav class="col-9" aria-label="Assignment page navigation">
								<ul class="pagination float-right">
									<li class="page-item"><a class="page-link" href="#"
										aria-label="Previous"> <span aria-hidden="true">&laquo;</span>
									</a></li>
									<li class="page-item"><a class="page-link" href="#">1</a></li>
									<li class="page-item"><a class="page-link" href="#">2</a></li>
									<li class="page-item"><a class="page-link" href="#">3</a></li>
									<li class="page-item"><a class="page-link" href="#"
										aria-label="Next"> <span aria-hidden="true">&raquo;</span>
									</a></li>
								</ul>
							</nav>
						</div>
					</div>
		
		
		
				</section>
			</div>
		</div> --%>
		
		
		<!-- SIDEBAR START -->
		<sec:authorize access="hasAnyRole('ROLE_FACULTY','ROLE_STUDENT')">
		<jsp:include page="../common/newSidebar.jsp" />
		</sec:authorize>
		<!-- SIDEBAR END -->
		<jsp:include page="../common/footer.jsp" />
		<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.4/MathJax.js?config=TeX-AMS_HTML"></script>
				
		<script>
		var modalId;
		            var prevId;
			$(document).ready(function() {
				$('#modal-added').hide();
				$('#testModule').modal({
					backdrop : 'static',
					keyboard : false
				});
		
				$(".modalClass").each(function() {
					var id = $(this).attr('id');
		
					if (id == 'modal-0') {
						$('#' + id).show();
						modalId = id;
					} else {
						$('#' + id).hide();
						
					}
		
				});
			});
		
			window.showModal = function showModal(currentModal,
					modalToView, navToSelect) {
				if(modalToView=='modal-result' || modalToView=='modal-added'){
					if(modalToView=='modal-result'){
					$('.activeBtnClass').hide();
					}
					$(".modalClass").each(function() {
						var id = $(this).attr('id');

						if (id == modalToView) {
							$('#' + modalToView).show();
						} else {
							$('#' + id).hide();
						}

					});
					/* $('#' + navToSelect).addClass('activeQbtn').siblings(
							".activeBtnClass").removeClass("activeQbtn"); */

					$(".activeBtnClass").each(function() {
						var id = $(this).attr("id");
						if (id == navToSelect) {
							$('#' + id).addClass('activeQbtn');

						} else {
							$('#' + id).removeClass('activeQbtn');
						}

					});
		        			}else{
				console.log('modals' + currentModal + 'modalView'
						+ modalToView + 'navToSelect' + navToSelect);
				prevId = modalId;
				modalId = modalToView;
				$(".modalClass").each(function() {
					var id = $(this).attr('id');
		
					if (id == modalToView) {
						$('#' + modalToView).show();
					} else {
						$('#' + id).hide();
					}
		
				});
				$(".activeBtnClass")
				.each(
						function() {
							var id = $(this).attr("id");
							if(id==navToSelect){
								$('#'+id).addClass('activeQbtn');
								
							}else{
								$('#'+id).removeClass('activeQbtn');
							}
							
						});
		        			}
			};
		
			window.showModalOnClick = function showModalOnClick(
					modalToShow, btnId) {
				
				
							prevId = modalId;
		                    modalId = modalToShow;
		
		                    var res = prevId.split("-")[1];
		                    console.log('prevId' + prevId);
		                    console.log('modalId' + modalId);
		                    console.log('res' + res);
		
		                    var id = "studentTestForm-" + res;
		                    var navId ="nav-"+res;
		
		                    $.post('addStudentQuestionResponse',
		                                                    $('#' + id).serialize()).then(
		                                                    function(response) {
		                                                        if (response.answer != null) {
		                                                                                    $('#' + navId).addClass( "submittedQbtn").removeClass('activeQbtn');
		                                                                    }
		                                                    }).fail(function() {
	                                                        	alert('Connection Lost! Please check your internet connection.');
	                                                        });
		
				$(".modalClass")
						.each(
								function() {
									var id = $(this).attr("id");
									if (id == modalToShow) {
										
		
										$('#' + modalToShow).show();
									} else {
										$('#' + id).hide();
									}
								});
				
				$(".activeBtnClass")
				.each(
						function() {
							var id = $(this).attr("id");
							if(id==navToSelect){
								$('#'+id).addClass('activeQbtn');
								
							}else{
								$('#'+id).removeClass('activeQbtn');
							}
							
						});
			};
		</script>
		
		
		
		
		<script>
		window.deleteResponse = function deleteResponse(testId) {
		
			$.post(
					'deleteStudentTestResponse?testId=' + testId
							+ '', null).then(function(response) {
		
			}).done(function(response) {
				if (response)
					location.reload();
			}).fail(function() {
			}).always(function() {
			});
		};
		window.getStudentData = function getStudentData(
				studentTestId) {
			console.log('getStudentData called successfully...');
			$
					.ajax({
						type : 'POST',
						url : '${pageContext.request.contextPath}/getStudentTestData?'
								+ 'studentTestId=' + studentTestId,
						success : function(data) {
		
							console.log("data is ------------"
									+ data);
						},
						error : function(result) {
								
						}
					});
		}
		
		function completeTest() {
		
			var studentTestId = ${test.studentTest.id};
			var studentFilePath=$('#studentFilePath').val();
			console
					.log('complete test called ajax'
							+ studentTestId);
			return $.post('completeStudentTestAjaxForSubjective', {
				studentTestId : studentTestId,
				studentFilePath : studentFilePath
			}).fail(function() {
            	alert('Connection Lost! Please check your internet connection.');
            });
		}
		
		window.submitForm = function submitForm(formId, navId,
				isComplete) {
		
			$
					.post('addStudentQuestionResponse',
							$('#' + formId).serialize())
					.then(
							function(response) {
		
								var studentTestId = ${test.studentTest.id};
								
								var testId =${test.id};
								console.log(studentTestId);
								if (response.answer != null) {
									$('#' + navId).addClass(
											"submittedQbtn")
											.removeClass(
													'activeQbtn');
								}
								getTestSummery(studentTestId,testId);
		
								if (isComplete) {
									console.log('test completed');
									var studentFilePath=$('#studentFilePath').val();
									return $
											.post(
													'completeStudentTestAjaxForSubjective',
													{
														studentTestId : response.studentTestId,
														studentFilePath:studentFilePath
													});
								}
							}).done(function(response) {
						if (response)
							location.reload();
					}).fail(function() {
						alert('Connection Lost! Please check your internet connection.');
					}).always(function() {
					});
		};
		
		window.getTestSummery = function getTestSummery(studTestId,testId) {
		
			$
					.ajax({
						type : 'POST',
						url : '${pageContext.request.contextPath}/getTestSummeryAjax?'
								+ 'studentTestId=' + studTestId
								+'&testId='+testId
								+'&studentFilePath='+$('#studentFilePath').val(),
						success : function(data) {
		
							var parsedObj = JSON.parse(data);
							console.log("totalQuestions is---"
									+ parsedObj.totalQuestions);
							console
									.log("totalQuestionAttempted is---"
											+ parsedObj.totalQuestionAttempted);
							$("#totalQuestions").text(
									parsedObj.totalQuestions);
							$("#totalQuestionAnswered")
									.text(
											parsedObj.totalQuestionAttempted);
						},
						error : function(result) {
							console.log('error');
						}
					});
		};
		
		window.saveStudentsTest = function saveStudentsTest(
				studTestId) {
		
			$
					.ajax({
						type : 'POST',
						url : '${pageContext.request.contextPath}/completeStudentTestAjaxForSubjective?'
								+ 'studentTestId=' + studTestId
								+'&studentFilePath='+$('#studentFilePath').val(),
						success : function(data) {
		
							var parsedObj = JSON.parse(data);
		
							$("#totalScoreParagraph").text(
									parsedObj.score);
							$("#passParagraph").text(
									parsedObj.status);
						},
						error : function(result) {
							console.log('error');
							alert('Connection Lost! Please check your internet connection:' + result);
						}
					});
		};
		
		window.checkFields = function checkFields(chkId, lblId,
				isRadioOrChk) {
			var checked = $('#' + chkId).is(':checked');
			console.log('checked is' + checked);
			console.log(isRadioOrChk);
			if (isRadioOrChk && checked) {
				$('#' + lblId).addClass('active1').siblings(
						".active1").removeClass("active1");
			}
			if (!checked) {
				$('#' + lblId).removeClass('active1');
			} else {
				$('#' + lblId).addClass('active1');
			}
		
		}
		
		window.saveCompletedDuration = function saveCompletedDuration(
				durationCompleted) {
			console.log('update duration function called');
			console.log('duration is==>' + durationCompleted);
			$
					.ajax({
						type : 'POST',
						url : '${pageContext.request.contextPath}/updateStudentsAttemptedDuration?'
								+ 'durationInMinute='
								+ durationCompleted
								+ '&studentTestId='
								+ $("#StudentTestIdValue").val(),
						success : function(data) {
		
							console.log('SUCCESS!');
						},
						error : function(result) {
							console.log('error');
						}
					});
		};
		
		//Final Submit
        
        $('#testSubmitFinal').click(function(){
                        $('#customAlert1').removeClass('d-none');
        });  
		</script>
		<!-- 	Timer Logic-->
		<!-- <script>
			 $("#DateCountdown").TimeCircles();
		           //For Mobile
		           $("#CountDownTimerMobile, #CountDownTimer").TimeCircles({ time: { 
		               Hours: { show: true , color: "#d53439"}, 
		               Minutes: { show: true, color: "#2e8a00" }, 
		               Seconds: { show: true, color: "#071e38" } 
		           
		           }});
		       $("#CountDownTimerMobile, #CountDownTimer").TimeCircles({circle_bg_color: "#d2d2d2"});
		
		        
		        //For larger devices
		         
		          
		           $("#PageOpenTimer").TimeCircles();
		           
		           var updateTime = function(){
		               var date = $("#date").val();
		               var time = $("#time").val();
		               var datetime = date + ' ' + time + ':00';
		               $("#DateCountdown").data('date', datetime).TimeCircles().start();
		           }
		           $("#date").change(updateTime).keyup(updateTime);
		           $("#time").change(updateTime).keyup(updateTime);
		       //Stopwatch Timer end
		</script> -->
		<!-- <script>
		function callAlert(testId){
			
			$('.alertWrapper').toggleClass('d-none');
		    $('.alert-text').html('Do you really want to start the test?');
		    $('.alert-ok').html('Yes');
		    $('.alert-close').html('No');
		    console.log('test table section entered');
		    
		    $('.alert-ok').click(function(){
		    	console.log('alert ok clicked');
		    window.location.href ='startStudentTestNew?id='+testId;	
		    });
			
		}
		
		</script> -->