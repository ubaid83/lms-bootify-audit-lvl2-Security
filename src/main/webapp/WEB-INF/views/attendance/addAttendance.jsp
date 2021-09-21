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
                                <li class="breadcrumb-item active" aria-current="page"> Attendance</li>
                            </ol>
                        </nav>
                        
						<jsp:include page="../common/alert.jsp" />
						
						
						<!-- Input Form Panel -->
						<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
                                <div class="x_panel">
								
                                    <div class="x_title">
                                        <h2>Mark Attendance</h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a></li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
									
                                    <div class="x_content">
                                        <form:form action="addAttendanceForm"  method="post"
				modelAttribute="attendance" >
				<fieldset>
				<div class="row">
						<div class="col-sm-4 column">
							<div class="form-group">

								<form:label path="courseId" for="courseId">Course</form:label>
								<form:select id="courseIdList" path="courseId" class="form-control">
									<form:option value="">Select Course</form:option>
									<c:forEach var="course" items="${courses}" varStatus="status">
										<form:option value="${course.id}">${course.courseName}</form:option>
									</c:forEach>
								</form:select>

							</div>
						</div>
						<div class="col-sm-4 column">
							<div class="form-group">

								<form:label path="facultyId" for="facultyId">Faculty</form:label>
								<form:select id="facultyId" path="facultyId"
									class="form-control">
									<form:option value="">Select Faculty</form:option>
									<c:forEach var="faculty" items="${facultyList}"
										varStatus="status">
										<form:option value="${faculty.username}"></form:option>
									</c:forEach>
								</form:select>

							</div>
						</div>
						<div class="col-sm-4 column">
							<div class="form-group">
								<form:label path="startDate" for="startDate">Start Date <span style="color: red">*</span></form:label>
								
									<div class='input-group date' id='datetimepicker1'>
															<form:input id="startDate" path="startDate" type="text"
																placeholder="Start Date" class="form-control"
																required="required" readonly="true"/>
															<span class="input-group-addon"><span
																class="glyphicon glyphicon-calendar"></span> </span>
														</div>
														
							</div>
						</div>
						<div class="col-sm-4 column">
							<div class="form-group">
								<form:label path="endDate" for="endDate">End Date <span style="color: red">*</span></form:label>
							
									<div class='input-group date' id='datetimepicker2'>
															<form:input id="endDate" path="endDate" type="text"
																placeholder="End Date" class="form-control"
																required="required" readonly="true"/>
															<span class="input-group-addon"><span
																class="glyphicon glyphicon-calendar"></span> </span>
														</div>
									
							</div>
						</div>

					</div>

					<hr>
					<div class="row">

					<div class="col-sm-12 column">
							<div class="form-group">
								<form:button id="submit" name="submit"
									class="btn btn-large btn-primary" formaction="searchStudentForAttendance">Search</form:button>
								<button type="reset" type="reset" class="btn btn-large btn-primary">Reset</button>
								<button id="cancel" name="cancel" class="btn btn-danger"
									formnovalidate="formnovalidate">Cancel</button>
							</div>
						</div>
					</div>


				</fieldset>
			</form:form>
                                    </div>
                                </div>
                            </div>
						</div>
						</div>

						<!-- Results Panel -->

						
								
							

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
$(document).ready(function() {
	$("#courseIdList").change(function() {
		var courseId = $(this).val();
		console.log(courseId);
					$.ajax({
								type : 'GET',
								url : '${pageContext.request.contextPath}/getFacultyByCourseForAttendance?'
										+ 'courseId='
										+ courseId,
								
								success : function(
										data) {
									var json = JSON
											.parse(data);
									var optionsAsString = "";

									$('#facultyId')
											.find('option')
											.remove();
									console.log(json);
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

									$('#facultyId')
											.append(
													optionsAsString);

								}
							});
				} );

			});

	

</script>  

<script>
//$(document).ready(function() {
$("#datetimepicker1").on("dp.change", function(e) {
	
	validDateTimepicks();
}).datetimepicker({
	// minDate:new Date(),
	  useCurrent: false,
	  format: 'YYYY-MM-DD HH:mm:ss'
});

$("#datetimepicker2").on("dp.change", function(e) { 	
	
	validDateTimepicks();
}).datetimepicker({
	// minDate:new Date(),
	  useCurrent: false,
	  format: 'YYYY-MM-DD HH:mm:ss'
	});
	
function validDateTimepicks(){
	 if(($('#startDate').val() !='' && $('#startDate').val() !=null)&&($('#endDate').val() !='' && $('#endDate').val() !=null)){
	        var fromDate = $('#startDate').val();
	        var toDate = $('#endDate').val();
	        var eDate = new Date(fromDate);
	        var sDate = new Date(toDate);
	        if(sDate < eDate){
	        	alert("endate cannot be smaller than startDate");
	        	$('#startDate').val("");
	        	$('#endDate').val("");       	
	        }
	    } 
}
//});
</script>