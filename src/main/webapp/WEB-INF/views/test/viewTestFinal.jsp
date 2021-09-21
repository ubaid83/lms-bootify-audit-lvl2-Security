<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page import="java.util.*"%>
<%@page import="com.spts.lms.web.utils.Utils"%>
<jsp:useBean id="now" class="java.util.Date"/>
<fmt:formatDate var="dateTimeValue" type = "both" value = "${now}" pattern="yyyy-MM-dd HH:mm:ss"/>
<%
	Date currentTime = Utils.getInIST();
%>
<fmt:formatDate var="currentDateTimeValue" type="both" value="<%=currentTime%>"
	pattern="yyyy-MM-dd HH:mm:ss" />
<jsp:include page="../common/newDashboardHeader.jsp" />


    <div class="d-flex dataTableBottom paddingFixAssign" id="testPage">
	<jsp:include page="../common/newLeftNavbar.jsp" />
	<jsp:include page="../common/newLeftSidebar.jsp" />
	<jsp:include page="../common/rightSidebar.jsp" />
	
	 <div id="customAlert1" class="alertWrapper w-100 vh-100 position-fixed d-none">
        <div class="w-100 vh-100 position-relative">
            <div class="alert-box">
                <p class="alert-text fnt-13"></p>
                <button class="btn btn-sm w-25 btn-light alert-ok">Ok</button>
                <button class="btn btn-sm w-25 btn-danger alert-close">Cancel</button>
            </div>
        </div>
    </div>

	<div class="container-fluid m-0 p-0 dashboardWraper testW">
	<jsp:include page="../common/newTopHeader.jsp" />

            <div class="container mt-5">
                <div class="row">
                    <div class="col-lg-9 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto">
						
						<jsp:include page="../common/alert.jsp" />
						
                        <div class="bg-white pb-5 mb-5">
                            <nav aria-label="breadcrumb">
                                <ol class="breadcrumb">
                                    <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
                                    <li class="breadcrumb-item active" aria-current="page">Test/quiz</li>
                                </ol>
                            </nav>
                            <section class="col-12 mb-5 fnt-13">
                                <form>
                                    <div class="form-row">
                                        <div class="col-lg-4 col-md-4 col-sm-12 mt-3">
                                            <label class="sr-only">Select Semester</label>
                                            <select class="form-control" id="testSem">
                                            	<c:if test="${empty course.acadSession}">
                                            		<option value="" disabled selected>--SELECT SEMESTER--</option>
                                            	</c:if>
                                               	<c:forEach  items="${ sessionWiseCourseList }"
													var="sList" varStatus="count">
													<c:if test="${sList.key eq course.acadSession}">
														<option value="${sList.key}" selected>${sList.key}</option>
													</c:if>
													<c:if test="${sList.key ne course.acadSession }">
														<option value="${sList.key}">${sList.key}</option>
													</c:if>
												</c:forEach>
                                            </select>
                                        </div>
                                        <div class="col-lg-4 col-md-4 col-sm-12 mt-3">
                                            <label class="sr-only">Select Course</label>
                                            <select class="form-control" id="testCourse">
                                            	<c:if test="${empty course.id}">
                                            		<option value="" disabled selected>--SELECT COURSE--</option>
                                            	</c:if>
                                               	<c:forEach var='cList' items='${ sessionWiseCourseList[course.acadSession] }'>
                                                  		<c:if test="${cList.id eq course.id}">
														<option value="${cList.id}" selected>${cList.courseName}</option>
													</c:if>
													<c:if test="${cList.id ne course.id }">
														<option value="${cList.id}">${cList.courseName}</option>
													</c:if>
                                                   </c:forEach>
                                            </select>
                                        </div>
                                        
                                       <!--  <div class="col-lg-4 col-md-4 col-sm-12 mt-3">
                                            <div class="input-group mb-3">
                                                <input type="text" class="form-control" placeholder="Search Test">
                                                <div class="input-group-append">
                                                    <button class="btn btn-outline-danger" type="button" id="searchTest"><i class="fas fa-search"></i></button>
                                                </div>
                                            </div>
                                        </div> -->
                                    </div>
                                </form>

                                
                            </section>
                            <section class="col-lg-12 col-md-12 col-sm-12 mb-5 fnt-13 chartWrap">
                                <canvas id="testBarChart"></canvas>
                            </section>

                            
                            
					</div>
					<div class="bg-white pb-5 mb-5">
                            <section class="searchTest mt-5" id="testTable">
                                <div class="col-12 bg-dark text-white subHead1">
                                    <h6 class="p-2 mb-3">SEARCH TESTS</h6>
                                </div>
                                

                                <div class="col-12">
    <div class="col-12 p-0 mr-auto mb-3">
    <!-- <div class="input-group flex-nowrap input-group-sm">
        <div class="input-group-prepend">
            <span class="input-group-text cust-select-span" id="addon-wrapping">Filter Tests</span>
        </div>

        <select class="cust-select">
            <option>All</option>
            <option>Completed</option>
            <option>Pending</option>
            <option>Failed</option>
        </select>
    </div> -->

</div>
                                    <div class="table-responsive mb-3 testAssignTable">
                                        <table class="table table-striped table-hover" id="viewTestTable">
                                            <thead>
                                                <tr>
                                                    <th scope="col">No.</th>
                                                    <th scope="col">Name</th>
                                                    <th scope="col">Academic Month</th>
                                                    <th scope="col">Academic Year</th>
                                                    <th scope="col">Start Date</th>
                                                    <th scope="col">End Date</th>
                                                    <th scope="col">Type</th>
                                                    <th scope="col">Status</th>
                                                    <th scope="col">Action</th>
                                                    <th scope="col">Result</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                            	<c:forEach var="test" items="${page.pageItems}"
																varStatus="status">
	                                            	<tr>
	                                                    <th scope="row"><c:out value="${status.count}" /></th>
	                                                    <td><c:out value="${test.testName}" /></td>
	                                                    <td><c:out value="${test.acadMonth}" /></td>
	                                                    <td><c:out value="${test.acadYear}" /></td>
	                                                    <td><c:out
																			value="${fn:replace(test.startDate, 
                                	'T', ' ')}"></c:out></td>
	                                                    <td><c:out
																			value="${fn:replace(test.endDate, 
                                	'T', ' ')}"></c:out></td>
	                                                    <td><c:out value="${test.testType}" /></td>
	                                                    <td>
		                                                    <c:if test="${test.testCompleted eq 'Y'}">
		                                                    	<i class="fas fa-check-circle text-success"></i> Completed
		                                                    </c:if>
		                                                    <c:if test="${empty test.testCompleted}">
		                                                    	<i class="fas fa-hourglass-start text-orange"></i> Pending
		                                                    </c:if>
	                                                    </td>
	                                                    <td>
	                                                    	<c:if test="${test.testCompleted eq 'Y' && test.studentTest.attempt eq 0}">
		                                                    	<input type="button" value="Give Test" disabled /> 
		                                                    </c:if>
		                                                    <c:if test="${(empty test.testCompleted) || test.studentTest.attempt gt 0}">
		                                                    	<c:if test="${ test.isPasswordForTest eq 'N' }">
			                                                    	<input type="button" value="Give Test" 
			                                                    		onclick="callAlert(${test.id},'${test.testType}')" class="text-info"/>
		                                                    	</c:if>
		                                                    	<c:if test="${ test.isPasswordForTest eq 'Y' }">
			                                                    	<input data-toggle="modal"
																			data-target="#testPassword${status.count}"
																			type="button" value="Give Test" class="text-info"/>
				                                                </c:if>
		                                                    </c:if>
														</td>
	                                                    <td>
	                                                        <!-- <input type="button" value="View" />  -->
	                                                        <input data-toggle="modal"
																	data-target="#testResult${status.count}"
																	type="button" value="View" />
	                                                    </td>
	                                                </tr>
                                            	</c:forEach>
                                            </tbody>
                                        </table>
                                    </div>

                                   
                                </div>




                            </section>
                        </div>
                    </div>
   

    <!-- TEST RESULT -->
    
    <c:forEach var="test" items="${page.pageItems}"
			varStatus="status">
    <div id="modalTestResult">
        <div class="modal fade fnt-13" id="testResult${status.count}" tabindex="-1" role="dialog" aria-labelledby="giveTestTitle" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered modal-dialog-scrollable" role="document">
                <div class="modal-content">
                    <div class="modal-header text-white">
                        <h5 class="modal-title" id="giveTestTitle">Test Result</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body pt-0 bg-light">
                        <h6 class="text-center mt-2">${test.testName}</h6>
                        <hr/>
                        <div class="row">
                            <div class="col-lg-6 col-md-6 col-sm-12"><p>Date allocated: ${test.createdDate}</p></div>
                            <div class="col-lg-6 col-md-6 col-sm-12"><p>Date of Completion: ${test.studentTest.testEndTime}</p></div>
                            <div class="col-lg-6 col-md-6 col-sm-12"><p>Remaining Attempts: ${ test.maxAttempt - test.studentTest.attempt }</p></div>
                            <div class="col-lg-6 col-md-6 col-sm-12"><p>Total Marks: ${	test.maxScore }</p></div>
                            <c:if test="${test.showResultsToStudents eq 'Y'}">
                            	<div class="col-lg-6 col-md-6 col-sm-12"><p>Mark Secured: ${ test.studentTest.score }</p></div>
                            </c:if>
                            <c:if test="${test.showResultsToStudents eq 'N'}">
                            	<div class="col-lg-6 col-md-6 col-sm-12"><p>Mark Secured: Not permitted by faculty to show marks</p></div>
                            </c:if>
                            <div class="col-lg-6 col-md-6 col-sm-12"><p>Passing Score: ${ test.passScore }</p></div>
                            <div class="col-lg-6 col-md-6 col-sm-12"><p>Total Questions: ${ test.maxQuestnToShow }</p></div>
                            <div class="col-lg-12 col-md-12 col-sm-12">
							<c:if test="${fn:replace(test.endDate, 'T', ' ') lt currentDateTimeValue && test.showReportsToStudents eq 'Y'}">
							<c:if test="${(appNameForTee eq 'CNMS')}">
							<a href="${pageContext.request.contextPath}/viewTestResultQuestionwise?testId=${test.id}" class="btn btn-primary mb-2">View Test Report</font></a> &nbsp
							</c:if>
							<c:if test="${(appNameForTee ne 'CNMS')}">
							<a href="${pageContext.request.contextPath}/downloadTestReportByTestIdAndUsername?testId=${test.id}" class="btn btn-primary">Download a Test Report</font></a>
							<a href="${pageContext.request.contextPath}/downloadTestReportByTestIdAndUsernameAttemptWise?testId=${test.id}" class="btn btn-primary">Download a Test Report Attempt-Wise
							</a>
							</c:if>
							</c:if>
							</div>
                            
                            </div>
                                  
                        </div>
                    <div class="modal-footer text-center">
                      <button type="button" class="btn btn-modalClose" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <div id="modalTestPassword">
        <div class="modal fade fnt-13" id="testPassword${status.count}" tabindex="-1" role="dialog" aria-labelledby="giveTestPassword" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered modal-dialog-scrollable" role="document">
                <div class="modal-content">
                    <div class="modal-header text-white">
                        <h6 class="modal-title" id="giveTestTitle">Test Is Password Protected, Please Enter a Password Provided by Faculty</h6>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body pt-0 bg-light text-center">
                        <h6 class="mt-2 text-capitalize">${test.testName}</h6>
                        <hr/>
                        <p>Please enter test password. <span class="text-danger">*</p>
                            <input class="form-control text-center" type="password" value=""
									id="passwordOfTest${test.id}"
									required="required" placeholder="Enter a Password" />
                            
                    </div>
                    <div class="modal-footer text-center">
                    	<a class="btn btn-modalSub text-white"
							onclick="checkPassword(${test.id},'${test.testType}')">Go</a>
                      <button type="button" class="btn btn-modalClose" data-dismiss="modal">Cancel</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    </c:forEach>

				<!-- SIDEBAR START -->
				<jsp:include page="../common/newSidebar.jsp" />
				<!-- SIDEBAR END -->
				<jsp:include page="../common/footer.jsp" />
				



<script>
function callAlert(testId,testType){
	console.log('call alert timer jsp');
	$('.alertWrapper').removeClass('d-none');
    $('.alert-text').html('Do you really want to start the test?');
    $('.alert-ok').html('Yes');
    $('.alert-close').html('No'); 
    console.log('test table section entered');
    console.log('test id--->' + testId);
     $('.alert-ok').click(function(){
    	console.log('alert ok clicked'); 
    
	    if(testType == 'Objective')
	    {	
	    	window.location.href ='startStudentTestNew?id='+testId;	
	    }else if(testType=='Subjective'){
	    	window.location.href ='startStudentTestUpdatedForSubjective?id='+testId;
	    }else{
	    	window.location.href ='startStudentTestUpdatedForMix?id='+testId;
	    }
    });
    
    $('.alert-close').click(function(){
     	console.log('alert-close clicked'); 
     	$('.alertWrapper').addClass('d-none');
    });
}

</script>

<script>
	
	window.checkPassword = function checkPassword(testId,testType){
		console.log("entered--->"+ testId);
		var passwordEnterdByUser = $("#passwordOfTest"+testId).val();
		$param = {testId:testId,password:passwordEnterdByUser};
		console.log("password entered--->"+ passwordEnterdByUser);
		$.ajax({
			type : 'POST',
			url : '${pageContext.request.contextPath}/getPasswordForTest',
			data:$param,
			success : function(data) {
				console.log('success'+ data);
				//var parsedObj = JSON.parse(data);
				//console.log("score is---" + parsedObj);
				
			 	if(data == "SUCCESS"){
					console.log('corerect password');
					
					if(testType=='Objective'){
						window.location.href = '${pageContext.request.contextPath}/startStudentTestNew?id='+testId;
				 	}
				 	if(testType=='Subjective'){
				 		window.location.href = '${pageContext.request.contextPath}/startStudentTestUpdatedForSubjective?id='+testId;
				 	}
				 	if(testType=='Mix'){
				 		window.location.href = '${pageContext.request.contextPath}/startStudentTestUpdatedForMix?id='+testId;
				 	}
					//$("#testCompleted").text(addedStatus);
				}	
				else{
					alert(data + "! Password is incorrect");
				} 
				
			},
			error : function(result) {
				console.log('error:' + result);
				alert("Connection Lost! Please check your internet connection.");
			}
		});
		
	}

	
</script>
