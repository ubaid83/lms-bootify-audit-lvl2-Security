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
        <%-- <jsp:include page="../common/alert.jsp" /> --%>
            
            
            <!-- page content: START -->
            <div class="right_col" role="main">
                
                <div class="dashboard_container">
                    
                    <div class="dashboard_container_spacing">
                        
                        <div class="breadcrumb">
							<a href="${pageContext.request.contextPath}/homepage">Dashboard</a>
							<i class="fa fa-angle-right"></i>  
							Upload Student Program Session
						</div>
						<jsp:include page="../common/alert.jsp" />
                        
						<!-- Input Form Panel -->
                        <div class="row">
                            <div class="col-xs-12 col-sm-12">
                                <div class="x_panel">
								
                                    <div class="x_title">
                                        <h2>Upload Student Program Session</h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a></li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
									
                                    <div class="x_content">
                                       <form:form action="uploadStudentProgramSession" method="post" modelAttribute="studentProgramSession" enctype="multipart/form-data">
				
					
					<div class="row">
						<div class="col-sm-6 col-md-4 col-xs-12 column">
							<div class="form-group">
								<form:label path="acadMonth" for="acadMonth">Academic Month <span style="color: red">*</span></form:label>
								<form:select id="acadMonth" path="acadMonth"
									class="form-control" required="required"
									itemValue="${studentProgramSession.acadMonth}">
									<form:option value="">Select Academic Month</form:option>
									<form:options items="${acadMonths}" />
								</form:select>
							</div>
						</div>
						<div class="col-sm-6 col-md-4 col-xs-12 column">
							<div class="form-group">
								<form:label path="acadYear" for="acadYear">Academic Year <span style="color: red">*</span></form:label>
								<form:select id="acadYear" path="acadYear"
									class="form-control" required="required"
									itemValue="${studentProgramSession.acadYear}">
									<form:option value="">Select Academic Year</form:option>
									<form:options items="${acadYears}" />
								</form:select>
							</div>
						</div>
						<div class="col-sm-6 col-md-4 col-xs-12 column">
							<div class="form-group">
								<label for="abbr">Upload file</label>
								<input id="abbr" name="file" type="file"
									class="form-control" />
							</div>
					  </div>
					</div>
					<div class="row">
						<div class="col-sm-12 column">
							<div class="form-group">

								<button id="submit"	class="btn btn-large btn-primary" formaction="uploadStudentProgramSession">Upload</button>
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
                        
						
                       
                        
                    </div>
                    
                    
                        
                </div>
                
            </div>
            <!-- /page content: END -->
            
            
            <jsp:include page="../common/footer.jsp" />
            
        </div>
    </div>


    
    
    
</body>
</html>
