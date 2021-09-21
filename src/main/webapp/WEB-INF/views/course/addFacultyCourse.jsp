<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
							<a href="${pageContext.request.contextPath}/homepage">Dashboard</a>
							<i class="fa fa-angle-right"></i>  
							Add Faculty Course Mapping
						</div>
                        <jsp:include page="../common/alert.jsp" />
						<!-- Input Form Panel -->
                        <div class="row">
                            <div class="col-xs-12 col-sm-12">
                                <div class="x_panel">
								
                                    <div class="x_title">
                                        <h2>Add Faculty Course Mapping</h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a></li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
									
                                    <div class="x_content">
                                       <form:form action="addFacultyCourse" id="addFacultyCourseForm" method="post" modelAttribute="userCourse">
				
					<div class="row">
						<div class="col-sm-6 col-md-4 col-xs-12 column">
							<div class="form-group">
								<form:label path="acadMonth" for="acadMonth">Academic Month <span style="color: red">*</span></form:label>
								<form:select id="acadMonth" path="acadMonth"
									class="form-control" required="required">
									<form:option value="">Select Academic Month</form:option>
									<form:options items="${acadMonths}" />
								</form:select>
							</div>
						</div>
						<div class="col-sm-6 col-md-4 col-xs-12 column">
							<div class="form-group">
								<form:label path="acadYear" for="acadYear">Academic Year <span style="color: red">*</span></form:label>
								<form:select id="acadYear" path="acadYear"
									class="form-control" required="required">
									<form:option value="">Select Academic Year</form:option>
									<form:options items="${acadYears}" />
								</form:select>
							</div>
						</div>
						<div class="col-sm-6 col-md-4 col-xs-12 column">
								<div class="form-group">
									<form:label path="courseId" for="course">Course <span style="color: red">*</span></form:label>
									<form:select id="course" path="courseId" placeholder="Course" class="form-control" required="required">
										<form:option value="">Select Course</form:option>
										<form:options items="${courseList}" itemLabel="courseName" itemValue="id"/>
									</form:select>
								</div>
						</div>
						<div class="col-sm-6 col-md-4 col-xs-12 column">
							<div class="form-group">
								<form:label path="username" for="user">Faculty <span style="color: red">*</span></form:label>
								<form:select id="user" path="username"
									class="form-control" required="required">
									<form:option value="">Select Faculty</form:option>
									<form:options items="${facultyList}" itemLabel="fullname" itemValue="username"/>
								</form:select>
							</div>
							<select id="dummy" name="dummy" class="hidden" disabled="disabled"></select>
						</div>
					</div>
					<div class="row">

						<div class="col-sm-12 column">
							<div class="form-group">

								<button id="submit"	class="btn btn-large btn-primary" formaction="addFacultyCourse">Add</button>
								<button id="cancel" class="btn btn-danger"
									formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
							</div>
						</div>

					</div>


				
			</form:form>
                                    </div>
                                </div>
                            </div>
                        </div>
                        
						<!-- Results Panel -->
                        <div class="row">
                            <div class="col-xs-12 col-sm-12">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h2>Faculty Allocation Details</h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a></li>
                                            <li><a class="close-link"><i class="fa fa-close"></i></a></li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
										<div class="table-responsive">
						<table class="table table-hover" id="facultyCourseTable">
							<thead>
								<tr>
									<th>Sr. No.</th>
									<th>Faculty</th>
									<th>Course</th>
									<th>Action</th>
								</tr>
							</thead>
							<tfoot>
								<tr>
									<th></th>
									<th>Faculty</th>
									<th>Course</th>
									<th></th>
								</tr>
							</tfoot>
							<tbody>

								<c:forEach var="userCourse" items="${userCourses}" varStatus="status">
									<tr>
										<td><c:out value="${status.count}" /></td>
										<td><c:out value="${userCourse.firstname} ${userCourse.lastname}" /></td>
										<td><c:out value="${userCourse.courseName}" /></td>
										<td><c:url value="deleteUserCourse" var="deleteurl">
											<c:param name="id" value="${userCourse.id}" />
										</c:url><a
										href="${deleteurl}" title="Delete"
										onclick="return confirm('Are you sure you want to delete this record?')"><i
											class="fa fa-trash-o fa-lg"></i></a></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        
                    </div>
                    
                    
                        
                </div>
                
            </div>
            <!-- /page content: END -->
            
            
            <jsp:include page="../common/footer.jsp" />
            
        </div>
    </div>


    
    
    
</body>
</html>
