<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>	

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
							Search Assignment To Re-Allocate
						</div>
                        <jsp:include page="../common/alert.jsp" />
						<!-- Input Form Panel -->
                        <div class="row">
                            <div class="col-xs-12 col-sm-12">
                                <div class="x_panel">
								
                                    <div class="x_title">
                                        <h2> Search Assignment To Re-Allocate</h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a></li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
									
                                    <div class="x_content">
                                       <form:form action="searchFacultyAllocation"  method="post"
				modelAttribute="assignment" >
				<%-- <form:input path="id" type="hidden" />
				<form:input path="courseId" type="hidden" /> --%>
				<fieldset>
					



					<div class="col-md-4 col-sm-6 col-xs-12 column">
						<div class="form-group">
							<form:label path="courseId" for="courseId">Course</form:label>
							<form:select id="courseId" path="courseId" class="form-control">
								<form:option value="">Select Course</form:option>
								<c:forEach var="course" items="${allCourses}" varStatus="status">
									<form:option value="${course.id}">${course.courseName}</form:option>
								</c:forEach>
							</form:select>
						</div>
					</div>

					<div class="col-md-4 col-sm-6 col-xs-12 column">
						<div class="form-group">
							<form:label path="id" for="id">Assignment</form:label>
							<form:select id="assid" path="id" class="form-control">
								<form:option value="">Select Assignment</form:option>

								<c:forEach var="preAssigment" items="${preAssigments}" varStatus="status">
									<form:option value="${preAssigment.id}">${preAssigment.assignmentName}</form:option>
								</c:forEach>
				
							</form:select>
						</div>
					</div>



					<div class="col-sm-12 column">
						<div class="form-group">
							<button id="submit" name="submit"
								class="btn btn-large btn-primary">Search</button>
							<input id="reset" type="reset" class="btn btn-danger">
							<button id="cancel" name="cancel" class="btn btn-danger"
								formnovalidate="formnovalidate">Cancel</button>
						</div>
					</div>
				</fieldset>
			</form:form>
                                    </div>
                                </div>
                            </div>
                        </div>
       	
                        
                    </div>
                    
                    
                        
                </div>
                
            </div>
            <!-- /page content: END -->
            
            
            <jsp:include page="../common/footer.jsp" />
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
											$('#assid').append(optionsAsString);

											$("#courseId").each(function() {
												this.selectedIndex = 0
											});

										});

						$("#courseId")
								.on(
										'change',
										function() {
											var courseid = $(this).val();
											if (courseid) {
												$
														.ajax({
															type : 'GET',
															url : '${pageContext.request.contextPath}/getAssigmentByCourseOnly?'
																	+ 'courseId='
																	+ courseid,
															success : function(
																	data) {
																var json = JSON
																		.parse(data);
																var optionsAsString = "";

																$('#assid')
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

																$('#assid')
																		.append(
																				optionsAsString);

															}
														});
											} else {
												alert('Error no course');
											}
										});

					});
</script>
            
        </div>
    </div>


    
    
    
</body>
</html>
