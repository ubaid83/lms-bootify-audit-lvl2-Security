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
							Mark Attendance
						</div>
                        <jsp:include page="../common/alert.jsp" />
						<!-- Input Form Panel -->
                        <div class="row">
                            <div class="col-xs-12 col-sm-12">
                                <div class="x_panel">
								
                                    <div class="x_title">
                                        <h2>Select Students to mark Attendance</h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a></li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
									
                                    <div class="x_content">
                                        <form:form action="searchStudentForAttendance" id="searchStudentForAttendance" method="post" modelAttribute="attendance">
				<fieldset>
					
			
					<form:input path="courseId" type="hidden" />
					<form:input path="facultyId" type="hidden" />
					<form:input path="startDate" type="hidden" />
					<form:input path="endDate" type="hidden" />
					
						<div class="table-responsive">
						<table class="table table-striped table-hover" style="font-size:12px">
							<thead>
							<tr>
								<th>Sr. No.</th>
								<th>Select (<a onclick="checkAll()">All</a> | <a onclick="uncheckAll()">None</a>)</th>
								<th>Check To Send</th>
							</tr>
							</thead>
							<tfoot>
							<tr>
								<th></th>
								<th></th>
								
							</tr>
							</tfoot>
							<tbody>
							
							<c:forEach var="studentsForAttendance" items="${listOfStudents}" varStatus="status">
						        <tr>
						            <td><c:out value="${status.count}" /></td>
						            <td>
						            <c:if test="${empty studentsForAttendance.id }">
						            	<form:checkbox path="students" value="${studentsForAttendance.username}"/> 
						            </c:if>
						            <c:if test="${not empty studentsForAttendance.id }">
						            	Attendance Marked
						            </c:if>
						            </td>
									<td><c:out value="${studentsForAttendance.firstname} ${studentsForAttendance.lastname}" /></td>
						        </tr>   
						    </c:forEach>
							</tbody>
					</table>
					</div>
					
					<div class="col-sm-4 column">
						<div class="form-group">

							<button id="submit"	class="btn btn-large btn-primary" formaction="saveStudentAttendance">Add To Mark Attendance</button>
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
