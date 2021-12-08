<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="sec"
      uri="http://www.springframework.org/security/tags"%>
      
      
      
      <jsp:include page="../common/newDashboardHeader.jsp" /> 

<div class="d-flex adminPage dataTableBottom" id="adminPage">
<jsp:include page="../common/newAdminLeftNavBar.jsp"/>
<jsp:include page="../common/rightSidebarAdmin.jsp" />


<!-- DASHBOARD BODY STARTS HERE -->

<div class="container-fluid m-0 p-0 dashboardWraper">

      <jsp:include page="../common/newAdminTopHeader.jsp" />
     
            <!-- SEMESTER CONTENT -->    
                    <div class="col-lg-12 col-md-12 col-sm-12 dashboardBody p-5">
                        
                     

                  <!-- page content: START -->
                  
                  
                              <nav aria-label="breadcrumb">
                    <ol class="breadcrumb">
                        <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
                        
                        <c:if test = "${not empty Program_Name}">
                        <li class="breadcrumb-item" aria-current="page">
                            <c:out value="${Program_Name}" />
                        </li>
                        </c:if>
                        
                        <sec:authorize access="hasRole('ROLE_STUDENT')">
                            <c:out value="${AcadSession}" />
                        </sec:authorize>
                        <li class="breadcrumb-item active" aria-current="page"> Allocate Student Feedback By
                                          Course</li>
                    </ol>
                </nav>

                                    <jsp:include page="../common/alert.jsp" />

                                    <!-- Input Form Panel -->
                                    <div class="card border bg-white">
                                          <div class="card-body">
                                          
                                          <div class="text-center border-bottom pb-2">

                                                            <h5>Allocate Feedback To Semester</h5>
                                                            <p class="text-danger">(Please choose the year and month when the module booking was done.)</p>
</div>
                                                      <form:form id="studentFeedbackForm"
                                                            action="saveStudentFeedback" method="post"
                                                            modelAttribute="feedback">

                                                                  <div class="row mt-4">
                                                                        <div class="col-md-4 col-sm-12">
                                                                              <div class="form-group">
                                                                                    <label class="control-label" for="feedback">Feedback <span style="color: red">*</span></label>
                                                                                    <form:select id="feedbackAcadSession" path="id"
                                                                                          placeholder="Feedback Name"
                                                                                          class="form-control feedbackdropdownAcadSession"
                                                                                          required="required">
                                                                                          <form:option value="">Select Feedback</form:option>
                                                                                          <form:options items="${feedbackList}"
                                                                                                itemLabel="feedbackName" itemValue="id" />
                                                                                    </form:select>
                                                                              </div>
                                                                        </div>
                                                                        <div class="col-md-4 col-sm-12">
                                                                              <div class="form-group">
                                                                                    <label class="control-label" for="course">acadYear <span style="color: red">*</span></label>
                                                                                    <form:select id="acadYearForAcadSession" path="acadYear"
                                                                                          placeholder="acad Year"
                                                                                          class="form-control facultyparameter" required="required">
                                                                                          <form:option value="">Select Acad Year</form:option>
                                                                                          <form:option value="2014">2014</form:option>
                                                                                          <form:options items="${yearList}" />
                                                                                          <%-- <form:option value="2017">2017</form:option>
                                                                                          <form:option value="2018">2018</form:option> --%>
                                                                                    </form:select>
                                                                              </div>
                                                                        </div>
                                                                        
                                                                        <div class="col-md-4 col-sm-12">
                                                                              <div class="form-group">
                                                                                    <label class="control-label" for="acadMonth">acadMonth <span style="color: red">*</span></label>
                                                                                    <form:select id="acadMonthForAcadSession" path="acadMonth"
                                                                                          placeholder="Acad Month"
                                                                                          class="form-control facultyparameter" required="required">
                                                                                          <form:option value="">Select Acad Month</form:option>
                                                                                          <form:options items="${acadMonths}" />
                                                                                          <%-- <form:option value="2017">2017</form:option>
                                                                                          <form:option value="2018">2018</form:option> --%>
                                                                                    </form:select>
                                                                              </div>
                                                                        </div>


                                                                        <div class="col-md-4 col-sm-12">
                                                                              <div class="form-group">
                                                                                    <form:label path="acadSession" for="acadSession">Semester <span style="color: red">*</span></form:label>
                                                                                    <form:select id="acadSessionId" path="acadSession"
                                                                                          class="form-control" required="required">

                                                                                          <form:option value="">Select Semester</form:option>

                                                                                          <c:if test="${not empty feedback.acadSession }">
                                                                                                <form:option value="${feedback.acadSession }">${feedback.acadSession }</form:option>
                                                                                          </c:if>
                                                                                    </form:select>
                                                                              </div>
                                                                        </div>
                                                                        
                                                                        <div class="col-md-4 col-sm-12">
                                                                              <div class="form-group">
                                                                                    <label path="programId" for="groupId">Select Program <span
																						style="color: red">*</span></label> 
																					<form:select
																						id="programId" path="programId" required="required"
																						class="form-control">
																						
																						<form:option value="">Select Program</form:option>
																						
																						<c:if test="${not empty feedback.programId }">
                                                                                              <form:option value="${feedback.programId }">${feedback.programName }</form:option>
                                                                                        </c:if>
																					</form:select>
                                                                              </div>
                                                                        </div>

                                                                        <div class="col-md-4 col-sm-12">
                                                                              <div class="form-group">
                                                                                    <form:label path="startDate" for="startDate">Start Date Of Feedback <span style="color: red">*</span></form:label>

                                                                                    <div class='input-group date' id='datetimepicker3'>
                                                                                          <form:input id="startDateAcadSession" path="startDate"
                                                                                                type="text" placeholder="Start Date"
                                                                                                class="form-control"  required="required" readonly="true"/>
                                                                                          <!-- <span class="input-group-addon"><span
                                                                                                class="glyphicon glyphicon-calendar"></span> </span> -->
                                                                                                <button class="btn btn-outline-secondary iniSDatePickerForCourse"
																									type="button">
																									<i class="fas fa-calendar"></i>
																								</button>
                                                                                    </div>


                                                                              </div>
                                                                        </div>

                                                                        <div class="col-md-4 col-sm-12">
                                                                              <div class="form-group">
                                                                                    <form:label path="endDate" for="endDate">End Date Of Feedback <span style="color: red">*</span></form:label>

                                                                                    <div class='input-group date' id='datetimepicker4'>
                                                                                          <form:input id="endDateAcadSession" path="endDate"
                                                                                                type="text" placeholder="End Date" class="form-control"
                                                                                                required="required" readonly="true"/>
                                                                                          <!-- <span class="input-group-addon"><span
                                                                                                class="glyphicon glyphicon-calendar"></span> </span> -->
                                                                                                <button class="btn btn-outline-secondary iniEDatePickerForCourse"
																									type="button">
																									<i class="fas fa-calendar"></i>
																								</button>
                                                                                    </div>

                                                                              </div>
                                                                        </div>
                                                                        
                                                                  </div>
                                                                  <div class="row mt-4">

                                                                        <div class="col-md-4 col-sm-12">
                                                                              <div class="form-group">

                                                                                    <!-- <button id="search" class="btn btn-large btn-primary"
                                                                                          formaction="addStudentFeedbackForm">Search</button> -->

                                                                                    <button id="searchCoursesByInputParams"
                                                                                          class="btn btn-large btn-primary"
                                                                                          formaction="searchCoursesByInputParams">Search
                                                                                          Courses</button>

                                                                                    <button id="cancel" class="btn btn-danger"
                                                                                          formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
                                                                              </div>
                                                                        </div>
                                                                  </div>
                                                      </form:form>
                                          </div>
                                    </div>

                                    <!-- Results Panel -->
                                    <c:choose>
                                          <c:when test="${showCourses eq true}">

                                                <div class="card bg-white border mt-5">
                                                      <div class="card-body">

                                                                        <h5 class="text-center border-bottom pb-2">Select Courses to Allocate Feedback</h5>


                                                                        <form:form action="saveStudentFeedbackByCourse"
                                                                              id="saveStudentFeedbackByCourse" method="post"
                                                                              modelAttribute="feedback">

                                                                              <form:input path="id" type="hidden" />
                                                                              <form:input path="acadYear" type="hidden" />
                                                                              <form:input path="acadSession" type="hidden" />
                                                                              <form:input path="startDate" type="hidden" />
                                                                              <form:input path="endDate" type="hidden" />

                                                                              <div class="table-responsive testAssignTable">
                                                                                    <table class="table table-striped table-hover">
                                                                                          <thead>
                                                                                                <tr>
                                                                                                      <th>Sr. No.</th>
                                                                                                      <th>Select (<a onclick="checkAll()">All</a> | <a
                                                                                                            onclick="uncheckAll()">None</a>)
                                                                                                      </th>


                                                                                                      <th>Course Name</th>
                                                                                                </tr>
                                                                                          </thead>
                                                                                          <tfoot>
                                                                                                <tr>
                                                                                                      <th></th>
                                                                                                      <th></th>

                                                                                                      <th>Course Name</th>
                                                                                                </tr>
                                                                                          </tfoot>
                                                                                          <tbody>

                                                                                                <c:forEach var="course"
                                                                                                      items="${feedback.studentFeedbacks}" varStatus="status">
                                                                                                      <tr>
                                                                                                            <td><c:out value="${status.count}" /></td>
                                                                                                            <td><c:if test="${empty course.feedbackId }">
                                                                                                                        <form:checkbox path="courseIds"
                                                                                                                              value="${course.courseId}" />
                                                                                                                  </c:if> <c:if test="${not empty course.feedbackId }">
                                                      Feedback Allocated For Course
                                                </c:if></td>
                                                                                                            <td><c:out value="${course.courseName}" /></td>
                                                                                                      </tr>
                                                                                                </c:forEach>
                                                                                          </tbody>
                                                                                    </table>
                                                                              </div>

                                                                              <div class="col-12 text-left">
                                                                                    <div class="form-group">
                                                                                          <sec:authorize access="hasRole('ROLE_ADMIN')">
                                                                                                <button id="submit" class="btn btn-large btn-success"
                                                                                                      onclick="return clicked();"
                                                                                                      formaction="saveStudentFeedbackByCourse">Allocate
                                                                                                      Feedback</button>


                                                                                          </sec:authorize>
                                                                                          <button id="cancel" class="btn btn-danger"
                                                                                                formaction="homepage" formnovalidate="formnovalidate">Cancel</button>


                                                                                    </div>
                                                                              </div>


                                                                        </form:form>
                                                </div>
                                                </div>

                                          </c:when>
                                    </c:choose>
                  <!-- /page content: END -->
                   
                    </div>
                  
                  <!-- SIDEBAR START -->

              <!-- SIDEBAR END -->
      <jsp:include page="../common/newAdminFooter.jsp"/>


<script>

/* $('#searchCoursesByInputParams').click(function(){
    $('#startDateAcadSession').val('');
    $('#endDateAcadSession').val('');
}) */


$(".facultyparameter")
.on(
            'change',
            function() {
                  var acadYear = $('#acadYearForAcadSession').val();

                  console.log(acadYear);

                  if (acadYear) {
                        console.log("called acad session")
                        $
                                    .ajax({
                                          type : 'GET',
                                          url : '${pageContext.request.contextPath}/getSemesterByAcadYearForFeedback?'
                                                      + 'acadYear=' + acadYear,
                                          success : function(data) {

                                                var json = JSON.parse(data);
                                                var optionsAsString = "";

                                                $('#acadSessionId').find('option')
                                                            .remove();
                                                
                                                $('#programId').find('option')
                                                			  .remove();
                                                var optionsAsStringForProgram = "<option value=''>Select Program</option>";
                                                $('#programId').append(
                                              		  optionsAsStringForProgram);
                                                
                                                console.log(json);
                                                
                                                optionsAsString += "<option value=''>Select Semester</option>";
                                                
                                                for (var i = 0; i < json.length; i++) {
                                                      var idjson = json[i];
                                                      console.log(idjson);

                                                      for ( var key in idjson) {
                                                            console.log(key + ""
                                                                        + idjson[key]);
                                                            optionsAsString += "<option value='" +key + "'>"
                                                                        + idjson[key]
                                                                        + "</option>";
                                                      }
                                                }
                                                console.log("optionsAsString"
                                                            + optionsAsString);

                                                $('#acadSessionId').append(
                                                            optionsAsString);

                                          }
                                    });
                  } else {
                        //  alert('Error no faculty');
                  }
            });
            
            
$("#acadSessionId")
.on('change',
function() {
      var acadSession = $('#acadSessionId').val();

      console.log(acadSession);

      if (acadSession) {

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
									var optionsAsStringForProgram = "<option value=''>Select Program</option>";
                                  $('#programId').append(
                                		  optionsAsStringForProgram);
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
      
});

/*    $("#datetimepicker3").on("dp.change", function(e) {

            validDateTimepicks1();

      }).datetimepicker({

            // minDate:new Date(),
            useCurrent : false,
            format : 'YYYY-MM-DD HH:mm:ss'

      });

      $("#datetimepicker4").on("dp.change", function(e) {

            validDateTimepicks1();

      }).datetimepicker({

            //    minDate:new Date(),
            useCurrent : false,
            format : 'YYYY-MM-DD HH:mm:ss'

      });

      function validDateTimepicks1() {

            //alert("called ");

            if (($('#startDateAcadSession').val() != '' && $(

            '#startDateAcadSession').val() != null)

            && ($('#endDateAcadSession').val() != '' && $(

            '#endDateAcadSession').val() != null)) {

                  var fromDate = $('#startDateAcadSession').val();

                  var toDate = $('#endDateAcadSession').val();

                  var eDate = new Date(fromDate);

                  var sDate = new Date(toDate);

                  if (sDate < eDate) {

                        alert("endate cannot be smaller than startDate");

                        $('#startDateAcadSession').val("");

                        $('#endDateAcadSession').val("");

                  }

            }

      } */
</script>

		<!-- Peter Start 07/12/2021-->
		<script>
			$(window)
					.bind(
							"pageshow",
							function() {
								var TommorowDate = new Date();
								//start Date picker
								$('.iniSDatePicker, .iniSDatePickerForCourse').daterangepicker({
									autoUpdateInput : false,
									minDate : TommorowDate,
									locale : {
										cancelLabel : 'Clear'
									},
									"singleDatePicker" : true,
									"showDropdowns" : true,
									"timePicker" : true,
									"showCustomRangeLabel" : false,
									"alwaysShowCalendars" : true,
									"opens" : "center"
								});

								$(".iniSDatePicker, .iniSDatePickerForCourse")
										.on(
												'apply.daterangepicker',
												function(ev, picker) {
													//validDateTimepicks();
													var fromDate = $(
															'#starDProgram')
															.val();
													var toDate = $(
															'#enDProgram')
															.val();
													var sDate = new Date(
															fromDate);
													var eDate = new Date(toDate);
													console.log('validate called'
																	+ sDate
																	+ ','
																	+ eDate);
													if (sDate > eDate) {
														alert("endate cannot be smaller than startDate");
														$('#startDateTimeTable')
																.val(
																		$(
																				'#startDateDB')
																				.val());
														$('#endDateTimeTable').val(
																$('#endDateDB')
																		.val());
													} else {
														$(this)
																.parent()
																.parent()
																.find('input')
																.val(
																		picker.startDate
																				.format('YYYY-MM-DD HH:mm:ss'));
													}
												});

								$('.iniSDatePicker .iniSDatePickerForCourse').on(
										'cancel.daterangepicker',
										function(ev, picker) {
											$(this).parent().parent().find(
													'input').val(
													$('#startDateDB').val());

										});

								//end Date picker

								$('.iniEDatePicker, .iniEDatePickerForCourse').daterangepicker({
									autoUpdateInput : false,
									minDate : TommorowDate,
									locale : {
										cancelLabel : 'Clear'
									},
									"singleDatePicker" : true,
									"showDropdowns" : true,
									"timePicker" : true,
									"showCustomRangeLabel" : false,
									"alwaysShowCalendars" : true,
									"opens" : "center"
								});

								$('.iniEDatePicker, .iniEDatePickerForCourse')
										.on(
												'apply.daterangepicker',
												function(ev, picker) {
													//validDateTimepicks();
													var fromDate = $(
															'#starDProgram')
															.val();
													var toDate = $(
															'#enDProgram')
															.val();
													var eDate = new Date(
															fromDate);
													var sDate = new Date(toDate);
													console
															.log('validate called'
																	+ sDate
																	+ ','
																	+ eDate);
													/* if (sDate > eDate) {
														alert("endate cannot be smaller than startDate");
														$('#startDateTimeTable')
																.val(
																		$(
																				'#startDateDB')
																				.val());
														$('#endDateTimeTable').val(
																$('#endDateDB')
																		.val());
													} else { */
														$(this)
																.parent()
																.parent()
																.find('input')
																.val(
																		picker.startDate
																				.format('YYYY-MM-DD HH:mm:ss'));
													//}
												});

								$('.iniEDatePicker, .iniEDatePickerForCourse').on(
										'cancel.daterangepicker',
										function(ev, picker) {
											$(this).parent().parent().find(
													'input').val(
													$('#endDateDB').val());

										});

							});
		</script>
		<!-- Peter End 07/12/2021-->




