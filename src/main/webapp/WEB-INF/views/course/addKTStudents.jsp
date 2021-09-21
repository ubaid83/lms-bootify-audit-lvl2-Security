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
        <%-- <jsp:include page="../common/alert.jsp" /> --%>
            
            <form:form action="saveKtStudentsAllocation" id="saveKtStudentsAllocation" method="post" modelAttribute="userCourse">
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
							Make K.T Students Active for Next Cycle
						</div>
						<jsp:include page="../common/alert.jsp" />
                        
						<!-- Input Form Panel -->
                        <div class="row">
                            <div class="col-xs-12 col-sm-12">
                                <div class="x_panel">
								
                                    <div class="x_title">
                                        <h2>Select Month and year for Re-attempt of Submission</h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a></li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
									
                                    <div class="x_content">
                                       
                  
                        
                        
                        <div class="col-sm-6 col-md-4 col-xs-12 column">
                                  <div class="form-group">
                                      <form:label path="acadMonth" for="acadMonth">Academic Month <span style="color: red">*</span></form:label>
                                      <form:select id="acadMonth" path="acadMonth"
                                           class="form-control" required="required">
                                           <form:option value="">Select Academic Month for 2nd attempt</form:option>
                                           <form:options items="${acadMonths}" />
                                      </form:select>
                                  </div>
                             </div>

                             <div class="col-sm-6 col-md-4 col-xs-12 column">
                                  <div class="form-group">
                                      <form:label path="acadYear" for="acadYear">Academic Year <span style="color: red">*</span></form:label>
                                      <form:select id="acadYear" path="acadYear" class="form-control"
                                           required="required">
                                           <form:option value="">Select Academic Year for 2nd attempt</form:option>
                                           <form:options items="${acadYears}" />
                                      </form:select>
                                  </div>
                             </div>
                             
                          
                             
                             
                             
                                    </div>
                                </div>
                            </div>
                        </div>
                        
						<!-- Results Panel -->
                        <div class="row">
                            <div class="col-xs-12 col-sm-12">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h2>Select KT Students to make Active</h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a></li>
                                            <li><a class="close-link"><i class="fa fa-close"></i></a></li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                    
                                      <form:input path="id" type="hidden" />
                        <form:input path="courseId" type="hidden" />
                        <form:input path="acadMonth" type="hidden" />
                        <form:input path="acadYear" type="hidden" />
										<div class="table-responsive">
                             <table class="table table-hover">
                                  <thead>
                                  <tr>
                                      <th>Sr. No.</th>
                                      <th>Select (<a onclick="checkAll()">All</a> | <a onclick="uncheckAll()">None</a>)</th>
                                      <th>Course</th>
                                      <th>Year</th>
                                      <th>Month</th>
                                      <th>Student Name</th>
                                  </tr>
                                  </thead>
                                 <!--  <tfoot>
                                  <tr>
                                      <th></th>
                                      <th></th>
                                  
                                      <th>Course</th>
                                      <th>Student Name</th>
                                  </tr>
                                  </tfoot> -->
                                  <tbody>
                                  
                                 	
							<c:forEach var="student" items="${students}" varStatus="status">
						        <tr>
						            <td><c:out value="${status.count}" /></td>
						            <td>
						            <c:if test="${ not empty student.id }">
						            	 Allocated
						            </c:if>
						            <c:if test="${ empty student.id }">
						            	
						            	<form:checkbox path="students" value="${student.username}"/> 
						            </c:if>
						            </td>
									<td><c:out value="${student.courseName}" /></td>
									<td><c:out value="${student.acadYear}" /></td>
									<td><c:out value="${student.acadMonth}" /></td>
									<td><c:out value="${student.firstname} ${student.lastname}" /></td>
						        </tr>   
						    </c:forEach>
                                  </tbody>
                        </table>
                        </div>
                        
                         <div class="col-sm-6 col-md-4 col-xs-12 column">
                             <div class="form-group">

                                  <button id="submit" class="btn btn-large btn-primary" formaction="saveKtStudentsAllocation">Make Active</button>
                                  <button id="cancel" class="btn btn-large btn-danger"
                                      formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
                             </div>
                        </div>
                      
                                    </div>
                                </div>
                            </div>
                        </div>
                        
                    </div>
                    
                    
                        
                </div>
                
            </div>
            </form:form>
            <!-- /page content: END -->
            
            
            <jsp:include page="../common/footer.jsp" />
            
        </div>
    </div>


    
    
    
</body>
</html>
