<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<jsp:include page="../common/newDashboardHeader.jsp" />

<div class="d-flex" id="facultyAssignmentPage">
	<!-- DASHBOARD BODY STARTS HERE -->
	<div class="container-fluid m-0 p-0 dashboardWraper">
		<header class="container-fluid sticky-top">
			<nav class="navbar navbar-expand-lg navbar-light p-0">
				<!--                     <i class="fas fa-bars fa-2x mr-3 leftnav-btn" data-toggle="modal" data-target="#leftnav"></i> -->
				<a class="navbar-brand" href="homepage"> <c:choose>
						<c:when test="${instiFlag eq 'nm'}">
							<img src="<c:url value="/resources/images/logo.png" />"
								class="logo" title="NMIMS logo" alt="NMIMS logo" />
						</c:when>
						<c:otherwise>
							<img src="<c:url value="/resources/images/svkmlogo.png" />"
								class="logo" title="SVKM logo" alt="SVKM logo" />
						</c:otherwise>
					</c:choose>
				</a>
				<button class="adminNavbarToggler" type="button"
					data-toggle="collapse" data-target="#adminNavbarCollapse">
					<i class="fas fa-bars"></i>
				</button>

				<div class="collapse navbar-collapse" id="adminNavbarCollapse">
					<ul class="navbar-nav ml-auto">

						<li id="program" class="nav-item active" data-toggle="tooltip"
							data-placement="bottom" title="My Program"><a
							class="nav-link" href="homepage"><i class="fas fa-home"></i>
								<span>Home</span></a></li>
					</ul>
				</div>
			</nav>
		</header>
		<!-- SEMESTER CONTENT -->
		<div class="col-lg-12 col-md-12 col-sm-12 dashboardBody p-5">
			<!-- page content: START -->
			<nav aria-label="breadcrumb">
				<ol class="breadcrumb">
					<li class="breadcrumb-item"><a
						href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>

	
					<li class="breadcrumb-item active" aria-current="page">UserCourse
						List</li>
				</ol>
			</nav>

			<jsp:include page="../common/alert.jsp" />

			<!-- Input Form Panel -->
			
									<div class="card border bg-white">
							<div class="card-body">
								<div class="x_panel">
										<h5 class="text-center border-bottom pb-2">Search Course Enrollments</h5>


									<div class="x_content">
										<form:form action="deleteUserCourseEnrollmentSupport" method="post"
											modelAttribute="userCourse">



											<div class="row">
												<div class="col-sm-6 col-md-4 col-xs-12 column">
													<div class="form-group">
														<form:label path="acadMonth" for="acadMonth">Academic Month</form:label>
														<form:select id="acadMonth" path="acadMonth"
															class="form-control">
															<option value="" selected disabled hidden>Select Acad Month</option>
															<form:option value="Jan">Jan</form:option>
															<form:option value="Feb">Feb</form:option>
															<form:option value="Mar">Mar</form:option>
															<form:option value="Apr">Apr</form:option>
															<form:option value="May">May</form:option>
															<form:option value="June">June</form:option>
															<form:option value="July">July</form:option>
															<form:option value="Aug">Aug</form:option>
															<form:option value="Sept">Sept</form:option>
															<form:option value="Oct">Oct</form:option>
															<form:option value="Nov">Nov</form:option>

															<form:option value="Dec">Dec</form:option>
														</form:select>
													</div>
												</div>
												<div class="col-sm-6 col-md-4 col-xs-12 column">
													<div class="form-group">
														<form:label path="acadYear" for="acadYear">Academic Year</form:label>
														<form:select id="acadYear" path="acadYear"
															class="form-control">
															<option value="" selected disabled hidden>Select Academic Year</option>
															<form:options items="${acadYears}" />
														</form:select>
													</div>
												</div>

												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<label class="control-label" for="courses">Courses</label>
														<form:select id="courses" path="courseId" type="text"
															placeholder="course" class="form-control"
															>
															<option value="" selected disabled hidden>Select Course</option>
															<c:forEach var="course" items="${allCourses}"
																varStatus="status">
																<form:option value="${course.id}">${course.courseName}</form:option>
															</c:forEach>
														</form:select>
													</div>
												</div>

												<div class="col-sm-6 col-md-4 col-xs-12 column">
													<div class="form-group">
													<form:label path="username" for="username">SAPID</form:label>
														<form:input id="username" path="username" type="text"
															placeholder="User Name (Student/Faculty)"
															class="form-control" />
													</div>
												</div>
												
												<div class="col-sm-6 col-md-4 col-xs-12 column">
													<div class="form-group">
														<form:label path="acadSession" for="acadSession">Academic Session</form:label>
														<form:select id="acadSession" path="acadSession"
															class="form-control">
															<option value="" selected disabled hidden>Select Acad Session</option>
															<form:options items="${acadSessionList}" />
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
											</div>

										</form:form>
									</div>
								</div>
							</div>
						</div>
			
			
			
			<div class="card bg-white border">
				<div class="card-body">
				
						<c:choose>
							<c:when test="${page.rowCount > 0}">
								<!-- Results Panel -->
								<div class="card border bg-white">
									<div class="card-body">
												<h5 class="text-center border-bottom pb-2">
													&nbsp;Enrollment List <font size="2px"> |
														${page.rowCount} Records Found &nbsp; </font>
												</h5>

											<div class="x_content">
												<div class="table-responsive testAssignTable">
													<table class="table table-striped table-hover supportAdmin">
														<thead>
															<tr>
																<th>Sr. No.</th>
																<th>Acad Month</th>
																<th>Acad Year</th>
																<th>Acad Session</th>
																<th>Course</th>
																<th>User Name</th>
																<th>Role</th>
																<th>Delete All<input type="checkbox" id="checkAll"></th>
															</tr>
														</thead>
														<tbody>
															<c:forEach var="userCourse" items="${page.pageItems}"
																varStatus="status">
																<tr>
																	<td><c:out value="${status.count}" /></td>
																	<td><c:out value="${userCourse.acadMonth}" /></td>
																	<td><c:out value="${userCourse.acadYear}" /></td>
																	<td><c:out value="${userCourse.acadSession}" /></td>
																	<td><c:out value="${userCourse.courseName}" /></td>
																	<td id="<c:out value="${userCourse.username}" />"><c:out value="${userCourse.username}" /></td>
																	<td id="role"><c:out value="${userCourse.role}" /></td>
																	<c:url value="deleteUserCourse"
																			var="deleteUserCourseurl">
																			<c:param name="courseId"
																				value="${userCourse.courseId}" />
																			<c:param name="username"
																				value="${userCourse.username}" />
																			<c:param name="role"
																				value="${userCourse.role}" />
																	</c:url>  
																		<td>
																			<input type="checkbox" class="delete_customer" 
																			data-username ="<c:out value="${userCourse.username}" />"  
																			data-role="<c:out value="${userCourse.role}" />" 
																			data-courseId="<c:out value="${userCourse.courseId}" />"/>
																		</td>
																		<%--  <td><input type="checkbox" id="userCheck" class="delete_customer" data-username="<c:out value="${userCourse.username}" />" /></td> --%>
																</tr>
															</c:forEach>


														</tbody>
													</table>
												</div>
											</div>
									</div>
								</div>
							</c:when>
						</c:choose>
					
						<c:choose>
							<c:when test="${page.rowCount > 0}">
								<button type="button" name="btn_delete" id="btn_delete" class="btn btn-success">Delete</button>
							</c:when>
						</c:choose>
						
						
						                            
				</div>
				
			</div>



			<!-- /page content: END -->

		</div>

		<!-- SIDEBAR START -->

		<!-- SIDEBAR END -->

		<jsp:include page="../common/newAdminFooter.jsp" />

<script>
$(document).ready(function(){
 
 $('#btn_delete').click(function(){
	 

  if(confirm("Are you sure you want to delete this?"))
  {
   let username = [];
   let role = [];   
   let courseId = [];   
   
   $('.supportAdmin input[type="checkbox"]:checked').each(function(){
	let $this = $(this);
	console.log("This username", $this.attr("data-username"))
	
    username.push($this.attr("data-username"));
    role.push($this.attr("data-role"));
    courseId.push($this.attr("data-courseId"));
   });
   console.log('role------------->'+role);
   console.log('courseId------------->'+courseId);
   console.log('username------------->'+username);
   if(username.length === 0) //tell you if the array is empty
   {
    alert("Please Select atleast one checkbox");
   }
   else
   {

    $.ajax({
    url: 'deleteuserEnrolmentSupportAdmin',
     method:'POST',
     data: "&username=" +username+ "&role=" +role+ "&courseId=" +courseId,
     success:function(data)
     {
    	 let datObj = JSON.parse(data);
    	 
    	 console.log("Data====>>", data)
    	 
      if(datObj.successDescription==="Successfully Deleted"){
    	 alert(datObj.successDescription)
    	 location.reload()
     	}  
    	 
      for(var i=0; i<username.length; i++)
      {
       $('tr#'+username[i]+'').css('background-color', '#ccc');
       $('tr#'+username[i]+'').fadeOut('slow');
      }
     }
     
    });
   }
   
  }
  else
  {
   return false;
  }
 });
 
});
</script>

<script>
$("#checkAll").click(function () {
    $('.supportAdmin input:checkbox').not(this).prop('checked', this.checked);
});
</script>
