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
							Faculty Allocation
						</div>
                        <jsp:include page="../common/alert.jsp" />
						<!-- Input Form Panel -->
                        <div class="row">
                            <div class="col-xs-12 col-sm-12">
                                <div class="x_panel">
								
                                    <div class="x_title">
                                        <h2>Faculty Allocation</h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a></li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
									
                                    <div class="x_content">
                                        <form:form action="saveFacultyAllocation" method="post" modelAttribute="assignment">
				<fieldset>
					
					<form:input path="courseId" type="hidden" />
					<form:input path="id" type="hidden" />
			
				
						<div class="table-responsive">
						<table class="table table-striped table-hover" style="font-size:12px">
							<thead>
							<tr>
								<th>Sr. No.</th>
								<th>Assignments</th>
								<th>Created By</th>
								<th>Assign To New Faculty</th>
							</tr>
							</thead>
							<tbody>
							
							<c:forEach var="assignment" items="${list1}" varStatus="status">
						        <tr>
						            <td><c:out value="${status.count}" /></td>
									<td><c:out value=" ${assignment.assignmentName}"/> </td>
									<td><c:out value=""/> ${assignment.createdBy} </td>
									<td>
									<form:label path="facultyId" for="facultyId">Set Faculty</form:label>
								<form:select id="facultyId" path="facultyId" class="form-control">
									<form:option value="">Select Faculty</form:option>
									<c:forEach var="faculty" items="${allFaculty}" varStatus="status">
										<form:option value="${faculty.username}">${faculty.username}</form:option>
									</c:forEach>
								</form:select></td>
						        </tr>   
						    </c:forEach> 
							</tbody>
					</table>
					</div>
					
					<div class="col-sm-4 column">
						<div class="form-group">
							<button id="cancel" class="btn btn-large btn-danger"
								formaction="saveFacultyAllocation" formnovalidate="formnovalidate">Save</button>
						
							<button id="cancel" class="btn btn-large btn-danger"
								formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
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


    
    
    
</body>
</html>
