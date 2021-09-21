<!doctype html>
<jsp:include page="../common/css.jsp" />
<jsp:include page="../common/topHeaderLibrian.jsp" />
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<sec:authorize access="hasAnyRole('ROLE_SUPPORT_ADMIN_REPORT')">
<body class="nav-md footer_fixed dashboard_left">

	<!-- Example row of columns -->
	<div class="loader"></div>
	<div class="container body">
		<div class="main_container">


			<!--  -->
			<div class="right_col" role="main">

				<div class="dashboard_container">

					<!-- <div class="dash-main"> -->
					<div class="dashboard_height" id="main">


						<div class="dashboard_contain_specing dash-main">

							<div class="alert alert-success alert-dismissible" role="alert">
								<button type="button" class="close" data-dismiss="alert"
									aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>
								<h4>
									<strong>Welcome to Support Admin Login </strong>

								</h4>
							</div>

							<div class="container-fluid dashboardcon">


								<form action="downloadAttendanceReport"
											method="get">
											<div class="row">

												<div class="col-md-6 col-sm-12">
													<div class="form-group">

														<label path="" for="ofDate">Select Date <span
															style="color: red">*</span></label>

														<div class='col-md-6 input-group date' style="width:300px;">
															<input id="ofDate" type="date"
																placeholder="Select Date" class="form-control"
																required="required"/>
															<p class="text-danger errMsg">Please select a date.</p>
														</div>

													</div>
												</div>
											</div>
											<div class="row">
												<div class="col-md-6 col-sm-12">
													<a class="btn btn-large btn-primary" id="genReport">
														Generate Report</a>
												</div>

												<!-- <button id="submit"
                                                                                                                              class="btn btn-large btn-primary"
                                                                                                                              formaction="downloadFeedbackReportRangeWise">Generate Report</button> -->
											</div>
										</form>

								<!--end dashboard-->




							</div>
						</div>
					</div>
				</div>


			</div>
		</div>
	</div>
</body>


<%-- <jsp:include page="../common/downloadAttendenceFooter.jsp" />--%>
<jsp:include page="../common/footerLibrarian.jsp" /> 
<%-- <jsp:include page="../common/footer.jsp" /> --%>

</sec:authorize>



<script>
			$(document).ready(function() {
				$('.errMsg').css('display','none');
				
				
				$('#JSResult').hide();
				$('#JSProgramResult').hide();

				//var programObj = eval('('+'${programList}'+')'); 

				/* $("#programIdForProgramWise option").remove();
				  for(var i=0 ;i<programObj.length;i++){
				   var a = JSON.stringify(programObj[i]);
				  
				   $("#programIdForProgramWise").append($('<option>', {value:programObj[i].value, text: programObj[i].text}));
				
				  }  */

			});

			function submitForm() {

				var term1 = $('#term1AcadSession').val();//document.getElementById("term2AcadSession");
				var term2 = $('#term2AcadSession').val();
				var courseQuestCount = $('#courseQuestCount').val();
				var acadYear = $('#acadYearRWise').val();
				var campusId = $('#campusR').val();

				if (term1 != null && term2 != null && courseQuestCount != null
						&& acadYear != null && courseQuestCount > 0) {

					window.location.href = '${pageContext.request.contextPath}/downloadFeedbackReportRangeWiseNew?'
							+ 'term1AcadSession='
							+ term1
							+ '&term2AcadSession='
							+ term2
							+ '&courseQuestnCount='
							+ courseQuestCount
							+ '&acadYear=' + acadYear + '&campusId=' + campusId;
				} else {
					alert("please fill all the fields");
				}

			}

			/* function submitForm() {

				var term1 = $('#term1AcadSession').val();//document.getElementById("term2AcadSession");
				var term2 = $('#term2AcadSession').val();
				var courseQuestCount = $('#courseQuestCount').val();

				if (term1 != null && term2 != null && courseQuestCount != null
						&& courseQuestCount > 0) {

					window.location.href = '${pageContext.request.contextPath}/downloadFeedbackReportRangeWise?'
							+ 'term1AcadSession='
							+ term1
							+ '&term2AcadSession='
							+ term2 + '&courseQuestnCount=' + courseQuestCount;
				} else {
					alert("please fill all the fields");
				}

			} */

			/* function submitFormtop5Feedback() {
				var acadSession = $('#acadSessionTopFive').val();

				if (acadSession != null) {

					window.location.href = '${pageContext.request.contextPath}/downloadTop5FacultyFeedback?'
							+ 'acadSession=' + acadSession;

				} else {
					alert("please fill all the fields");
				}

			} */

			function submitFormtop5Feedback() {
				var acadSession = $('#acadSessionTopFive').val();

				var acadYear = $('#acadYearQWiseTop5').val();

				var campusId = $('#campusIdTop5').val();

				if (acadSession != null && acadYear != null) {

					if (campusId != null) {
						window.location.href = '${pageContext.request.contextPath}/downloadTop5FacultyFeedbackNew?'
								+ 'acadSession='
								+ acadSession
								+ '&acadYear='
								+ acadYear + '&campusId=' + campusId;
					} else {
						window.location.href = '${pageContext.request.contextPath}/downloadTop5FacultyFeedbackNew?'
								+ 'acadSession='
								+ acadSession
								+ '&acadYear='
								+ acadYear;
					}

				} else {
					alert("please fill all the fields");
				}

			}

			function submitFormDepartFeedbackReport() {

				var acadSession = $('#acadSessionDepart').val();

				if (acadSession != null) {

					window.location.href = '${pageContext.request.contextPath}/downloadDepartmentWiseFeedback?'
							+ 'acadSession=' + acadSession;

				} else {
					alert("please fill all the fields");
				}

			}

			function submitFormDepartFacultyFeedbackReport() {
				var acadSessionTerm1 = $('#term1AcadSessionDeptFaculty').val();
				var acadSessionTerm2 = $('#term2AcadSessionDeptFaculty').val();

				if (acadSessionTerm1 != null && acadSessionTerm2 != null) {

					window.location.href = '${pageContext.request.contextPath}/downloadDepartmentFacultyTermWiseFeedback?'
							+ 'term1AcadSession='
							+ acadSessionTerm1
							+ '&term2AcadSession=' + acadSessionTerm2;

				} else {
					alert("please fill all the fields");
				}

			}

			function myFunctionCampusResult(element, event) {

				var result = document.getElementById('JSResultCampus');
				while (result.firstChild)
					result.removeChild(result.firstChild);
				var values = [];
				for (var i = 0; i < element.options.length; i++) {
					if (element.options[i].selected) {
						var li = document.createElement('li');
						li.appendChild(document
								.createTextNode(element.options[i].value));
						result.appendChild(li);
					}
				}

				var acadList = document.getElementById('JSResultCampus')
						.getElementsByTagName("LI");

				var programObj = [];
				console.log(acadList.length);
				var acadSession = [];

				for (var i = 0; i < acadList.length; i++) {
					acadSession.push(acadList[i].innerHTML);
				}
				console.log("acadSessionList" + acadSession);

				$
						.ajax(
								{
									url : "${pageContext.request.contextPath}/GetProgramsFromAcadSession?acadSession="
											+ acadSession
								}).done(function(data) {
							console.log("data:" + data);
							programObj = JSON.parse(data);
							console.log("Size:" + programObj.length);
							$("#programIdCampus option").remove();
							for (var i = 0; i < programObj.length; i++) {
								var a = JSON.stringify(programObj[i]);
								console.log("a is " + a);
								$("#programIdCampus").append($('<option>', {
									value : programObj[i].value,
									text : programObj[i].text
								}));

							}

						});

			}

			function myFunction(element, event) {

				var result = document.getElementById('JSResult');
				while (result.firstChild)
					result.removeChild(result.firstChild);
				var values = [];
				for (var i = 0; i < element.options.length; i++) {
					if (element.options[i].selected) {
						var li = document.createElement('li');
						li.appendChild(document
								.createTextNode(element.options[i].value));
						result.appendChild(li);
					}
				}

				var acadList = document.getElementById('JSResult')
						.getElementsByTagName("LI");

				var programObj = [];
				console.log(acadList.length);
				var acadSession = [];

				for (var i = 0; i < acadList.length; i++) {
					acadSession.push(acadList[i].innerHTML);
				}
				console.log("acadSessionList" + acadSession);

				$
						.ajax(
								{
									url : "${pageContext.request.contextPath}/GetProgramsFromAcadSession?acadSession="
											+ acadSession
								}).done(function(data) {
							console.log("data:" + data);
							programObj = JSON.parse(data);
							console.log("Size:" + programObj.length);
							$("#programId option").remove();
							for (var i = 0; i < programObj.length; i++) {
								var a = JSON.stringify(programObj[i]);
								console.log("a is " + a);
								$("#programId").append($('<option>', {
									value : programObj[i].value,
									text : programObj[i].text
								}));

							}

						});

			}

			function myFunctionProgramWise(element, event) {
				var result = document.getElementById('JSProgramResult');
				while (result.firstChild)
					result.removeChild(result.firstChild);
				var values = [];
				for (var i = 0; i < element.options.length; i++) {
					if (element.options[i].selected) {
						var li = document.createElement('li');
						li.appendChild(document
								.createTextNode(element.options[i].value));
						result.appendChild(li);
					}
				}

				var acadList = document.getElementById('JSProgramResult')
						.getElementsByTagName("LI");

				var programObj = [];
				console.log(acadList.length);
				var acadSession = [];

				for (var i = 0; i < acadList.length; i++) {
					acadSession.push(acadList[i].innerHTML);
				}
				console.log("acadSessionList" + acadSession);

				$
						.ajax(
								{
									url : "${pageContext.request.contextPath}/GetProgramsFromAcadSession?acadSession="
											+ acadSession
								})
						.done(
								function(data) {
									console.log("data:" + data);
									programObj = JSON.parse(data);
									console.log("Size:" + programObj.length);
									$("#programIdForProgramWise option")
											.remove();
									for (var i = 0; i < programObj.length; i++) {
										var a = JSON.stringify(programObj[i]);
										console.log("a is " + a);
										$("#programIdForProgramWise")
												.append(
														$(
																'<option>',
																{
																	value : programObj[i].value,
																	text : programObj[i].text
																}));

									}

								});

			}

			function submitFormQuestionWiseReport() {

				var courseCountQuestion = $('#courseQuestCountForQuestionwise')
						.val();
				var acadSession = $('#acadSessionQuestionWise').val();
				var programId = $('#programId').val();
				var acadYear = $('#acadYearQWise').val();

				if (courseCountQuestion != null && acadSession != null
						&& programId != null && acadYear != null) {

					window.location.href = '${pageContext.request.contextPath}/downloadFacultyFeedbackReportQuestionWise?'
							+ 'acadSession='
							+ acadSession
							+ '&courseQuestnCount='
							+ courseCountQuestion
							+ '&programId='
							+ programId
							+ '&acadYear='
							+ acadYear;
				} else {
					alert("please fill all the fields");
				}

			}
			function submitFormQuestionWiseReportForFacultyFeedback() {
				//alert('function callled')

				var courseCountQuestion = $('#courseQuestCountForQuestionwise')
						.val();
				//alert('courseCountQuestion --- ', courseCountQuestion)
				//var acadSession = $('#acadSessionQuestionWise').val();
				//var programId = $('#programId').val();
				var acadYear = $('#acadYearQWise').val();

				//alert('courseCountQuestion -- ' + courseCountQuestion + 'acadYear - '
				//		+ acadYear)

				if (courseCountQuestion != null && acadYear != null) {

					window.location.href = '${pageContext.request.contextPath}/downloadFacultyFeedbackReportQuestionWiseForAllPrograms?'
							+ '&courseQuestnCount='
							+ courseCountQuestion
							+ '&acadYear=' + acadYear;
				} else {
					alert("please fill all the fields");
				}

			}

			function submitFormQuestionWiseReportForFaculty() {

				var courseCountQuestion = $('#courseQuestCountForQuestionwise')
						.val();
				var acadSession = $('#acadSessionQuestionWise').val();
				var programId = $('#programId').val();
				var acadYear = $('acadYearQWise').val();

				if (courseCountQuestion != null && acadSession != null
						&& programId != null) {

					window.location.href = '${pageContext.request.contextPath}/downloadFacultyFeedbackReportQuestionWise?'
							+ 'acadSession='
							+ acadSession
							+ '&courseQuestnCount='
							+ courseCountQuestion
							+ '&programId='
							+ programId
							+ '&acadYear='
							+ acadYear;
				} else {
					alert("please fill all the fields");
				}

			}

			function submitFormProgramWiseReport() {

				var courseCountQuestion = $('#courseQuestCountForProgramwise')
						.val();

				var programId = $('#programIdForProgramWise').val();
				var acadSession = $('#acadSessionProgramWise').val();

				if (courseCountQuestion != null && acadSession != null
						&& programId != null) {

					window.location.href = '${pageContext.request.contextPath}/downloadFeedbackReportProgramWise?'
							+ 'courseQuestnCount='
							+ courseCountQuestion
							+ '&acadSession='
							+ acadSession
							+ '&programId='
							+ programId;

				} else {
					alert("please fill all the fields");
				}

			}

			function submitFormCampusWiseReport() {

				var courseCountQuestion = $(
						'#courseQuestCountForQuestionwiseAndCampus').val();
				var acadSession = $('#acadSessionQuestionWiseAndCampus').val();
				var programId = $('#programIdCampus').val();
				var acadYear = $('#acadYearForAcadSessionForCampus').val();
				var campusId = $('#campus').val();

				console.log("entreies---->");
				console.log("count--->" + courseCountQuestion + "session--->"
						+ acadSession + "programId" + programId + "year"
						+ acadYear + "campus" + campusId);
				if (courseCountQuestion != null && acadSession != null
						&& programId != null && acadYear != null) {

					if (campusId != null || campusId != '') {
						console.log("campsId is not empty");
						window.location.href = '${pageContext.request.contextPath}/downloadFacultyFeedbackReportQWise?'
								+ 'acadSession='
								+ acadSession
								+ '&courseQuestnCount='
								+ courseCountQuestion
								+ '&programId='
								+ programId
								+ '&acadYear='
								+ acadYear + '&campusId=' + campusId;
					} else {
						console.log("campsId is  empty");
						window.location.href = '${pageContext.request.contextPath}/downloadFacultyFeedbackReportQWise?'
								+ 'acadSession='
								+ acadSession
								+ '&courseQuestnCount='
								+ courseCountQuestion
								+ '&programId='
								+ programId
								+ '&acadYear='
								+ acadYear;
					}

				} else {
					alert("please fill all the fields");
				}

			}

			 function submitFeatureWiseUtilisationReport() {
				var fromDate = $('#startDate').val();
				var endDate = $('#endDate').val();
				var campus = $('#campus1').val();

				if (fromDate != null && endDate != null) {

					window.location.href = '${pageContext.request.contextPath}/featureWiseSummaryUtilityReport?'
							+ 'fromDate='
							+ fromDate
							+ '&toDate='
							+ endDate
							+ '&campus=' + campus;

				} else {
					alert("please fill all the fields");
				}
			} 

			function submitFeatureWiseDetailedUtilisationReport() {
				var fromDate = $('#startDate1').val();
				var endDate = $('#endDate1').val();

				if (fromDate != null && endDate != null) {

					window.location.href = '${pageContext.request.contextPath}/featureWiseDetailedUtilityReport?'
							+ 'fromDate=' + fromDate + '&toDate=' + endDate;

				} else {
					alert("please fill all the fields");
				}
			}
			
			function submitMarkAttendanceReport() {
				var ofDate = $('#ofDate').val();

				if (ofDate != null) {

					window.location.href = '${pageContext.request.contextPath}/downloadAttendanceReport?'
							+ 'ofDate=' + ofDate;

				} else {
					alert("please fill all the fields");
				}
			}

			function submitFormByAcadMonthYearAndSessionFeedbackReport() {

				var courseCountQuestion = $('#courseQuestCountForFaculty')
						.val();

				var acadYear = $('#acadYear').val();
				var acadMonth = $('#acadMonth').val();
				var acadSessionValue = $('#acadSessionId').val();
				console.log('acadYear--->' + acadYear);
				console.log('acadMonth--->' + acadMonth);
				console.log('acadSessionId--->' + acadSessionValue);
				console.log('courseCountQuestion--->' + courseCountQuestion);
				console.log('acadSession value()' + $('#acadSessionId').val())

				if (courseCountQuestion != null && acadSessionValue != null
						&& acadYear != null && acadMonth != null) {

					window.location.href = '${pageContext.request.contextPath}/downloadFacultyFeedbackReportQuestionWise?'
							+ 'courseQuestnCount='
							+ courseCountQuestion
							+ '&acadSession='
							+ acadSessionValue

							+ '&acadYear='
							+ acadYear
							+ '&acadMonth='
							+ acadMonth;

				} else {
					alert("please fill all the field!!!!!!");
				}

			}

			function submitForFacultyLetter() {

				var username = $('#facultyId').val();

				var acadYear = $('#acadYearFeedbackLetter').val();
				var campusId = $('#campusIdFeedbackLetter').val();
				var acadSessionList = $('#acadSessionForTerm').val();
				var term = $('#term').val();
				var dean = $('#dean').val();

				console.log('username-->' + username + 'acadYear--->'
						+ acadYear + 'campusId--->' + campusId);

				if (acadYear != null && acadSessionList != null && term != null
						&& term != '' && dean != null && dean != '') {

					if (campusId != null) {

						if (username != null && username != '') {
							window.location.href = '${pageContext.request.contextPath}/downloadFacultyFeedbackLetterForAllPrograms?'
									+ 'acadYear='
									+ acadYear
									+ '&acadSessionList='
									+ acadSessionList
									+ '&term='
									+ term
									+ '&dean='
									+ dean
									+ '&campusId='
									+ campusId
									+ '&username='
									+ username;
						} else {
							window.location.href = '${pageContext.request.contextPath}/downloadFacultyFeedbackLetterForAllPrograms?'
									+ 'acadYear='
									+ acadYear
									+ '&acadSessionList='
									+ acadSessionList
									+ '&term='
									+ term
									+ '&dean='
									+ dean
									+ '&campusId=' + campusId;
						}
					} else {
						if (username != null && username != '') {
							window.location.href = '${pageContext.request.contextPath}/downloadFacultyFeedbackLetterForAllPrograms?'
									+ 'acadYear='
									+ acadYear
									+ '&acadSessionList='
									+ acadSessionList
									+ '&term='
									+ term
									+ '&dean='
									+ dean
									+ '&username=' + username;
						} else {
							window.location.href = '${pageContext.request.contextPath}/downloadFacultyFeedbackLetterForAllPrograms?'
									+ 'acadYear='
									+ acadYear
									+ '&acadSessionList='
									+ acadSessionList
									+ '&term=' + term + '&dean=' + dean;

						}
					}

				} else {
					alert("please note: acadYear,session,term & dean are the required fields");
				}

			}
			
			
			
			$('#genReport').click(function(){
				if($('#ofDate').val() != ''){
				submitMarkAttendanceReport();
				$('.errMsg').css('display', 'none');
				$('#ofDate').css('border', '1px solid #dddddd');
				} else {
					$('.errMsg').css('display', 'block');
					$('#ofDate').css('border', '1px solid red');
				}
			});
			
			$(function(){
    var dtToday = new Date();
    
    var month = dtToday.getMonth() + 1;
    var day = dtToday.getDate();
    var year = dtToday.getFullYear();
    if(month < 10)
        month = '0' + month.toString();
    if(day < 10)
        day = '0' + day.toString();
    
    var maxDate = year + '-' + month + '-' + day;
    //alert(maxDate);
    $('#ofDate').attr('max', maxDate);
});
			
		</script>
