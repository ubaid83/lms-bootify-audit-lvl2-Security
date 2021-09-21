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
							 Make Inactive
						</div>
                        <jsp:include page="../common/alert.jsp" />
						<!-- Input Form Panel -->
                        <div class="row">
                            <div class="col-xs-12 col-sm-12">
                                <div class="x_panel">
								
                                    <div class="x_title">
                                        <h2>Make Inactive</h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a></li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
									
                                    <div class="x_content">
                                        <form:form action=""  method="post"
				modelAttribute="userCourse" >
				<fieldset>
				<div class="row">
						
						
						<div class="col-sm-4 column">
							<div class="form-group">
								<form:label path="" for="courseId">Course</form:label>
								<form:select id="courseId" path="" class="form-control">
									<form:option value="">Select Course</form:option>
									<c:forEach var="course" items="${courseList}" varStatus="status">
										<form:option value="${course.id}">${course.courseName}</form:option>
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
									class="btn btn-large btn-primary" formaction="searchToMakeInactive">Search</form:button>
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


    
</body>
</html>
