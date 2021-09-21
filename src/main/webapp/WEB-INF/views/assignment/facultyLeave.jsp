<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
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
        
            
            
            <!-- page content: START -->
            <div class="right_col" role="main">
                
                <div class="dashboard_container">
                    
                    <div class="dashboard_container_spacing">
                        
                        <div class="breadcrumb">
                        
                      <c:out value="${Program_Name}" />
						
						<sec:authorize access="hasRole('ROLE_STUDENT')">
						
						<i
						class="fa fa-angle-right"></i>
											
											<c:out value="${AcadSession}" />
											
											</sec:authorize>
						<br><br>
                        
							<a href="${pageContext.request.contextPath}/homepage">Dashboard</a>
							<i class="fa fa-angle-right"></i>  
							 Faculty Change
						</div>
                        <jsp:include page="../common/alert.jsp" />
						<!-- Input Form Panel -->
                        <div class="row">
                            <div class="col-xs-12 col-sm-12">
                                <div class="x_panel">
								
                                    <div class="x_title">
                                        <h2>Faculty Leaves</h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a></li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
									
                                    <div class="x_content">
                                   
                                        <form:form action="facultyAllocation"  method="post"
				modelAttribute="assignment" >
				<fieldset>
				<div class="row">
						<div class="col-sm-4 column">
							<div class="form-group">

								<form:label path="courseId" for="courseId">Course</form:label>
								<form:select id="courseIdList" path="courseId" class="form-control">
									<form:option value="">Select Course</form:option>
									<c:forEach var="course" items="${courseIdList}" varStatus="status">
										<form:option value="${course.id}">${course.courseName}</form:option>
									</c:forEach>
								</form:select>

							</div>
						</div>
						<div class="col-sm-4 column">
							<div class="form-group">

								<form:label path="assignmentId" for="assignmentId">Assignments</form:label>
								<form:select id="assignmentId" path="assignmentId"
									class="form-control">
									<form:option value="">Select Assignment</form:option>
									<c:forEach var="assignment" items="${assignmentOnCourse}"
										varStatus="status">
										<form:option value="${assignment.assignedId}">${assignment.assignmentName}</form:option>
									</c:forEach>
								</form:select>

							</div>
						</div>
						
						
					</div>
					<hr>
					<div class="row">
					<div class="col-sm-12 column">
							<div class="form-group">
								<form:button id="submit" name="submit"
									class="btn btn-large btn-primary" formaction="searchFacultyForAssignment">Search</form:button>
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
                        
						<!-- Results Panel -->
                        
                        
                    </div>
                    
                    
                        
                </div>
                
            </div>
            <!-- /page content: END -->
            
            
            <jsp:include page="../common/footer.jsp" />
            
            
        </div>
    </div>


    
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js">
</script>

<script>
$(document).ready(function() {
	$("#courseIdList").change(function() {
		var courseId = $(this).val();
		console.log(courseId);
					$.ajax({
								type : 'GET',
								url : '${pageContext.request.contextPath}/getAssignmentBasedOnCourse?'
										+ 'courseId='
										+ courseId,
								
								success : function(
										data) {
									var json = JSON
											.parse(data);
									var optionsAsString = "";

									$('#assignmentId')
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

									$('#assignmentId')
											.append(
													optionsAsString);

								}
							});
				} );
	

			});

	

</script>  
    
</body>
</html>
