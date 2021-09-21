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
							Upload Institute Programs
						</div>
						<jsp:include page="../common/alert.jsp" />
                        
						<!-- Input Form Panel -->
                        <div class="row">
                            <div class="col-xs-12 col-sm-12">
                                <div class="x_panel">
								
                                    <div class="x_title">
                                        <h2>Upload All Institute Programs</h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a></li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
									
                                    <div class="x_content">
                                        <form:form action="uploadProgram" id="uploadProgram" method="post" modelAttribute="program" enctype="multipart/form-data">
				
					
					<div class="row">
						
						<div class="col-sm-6 col-md-4 col-xs-12 column">
							<div class="form-group">
								<label for="abbr">Select Programs File as per template</label>
								<input id="abbr" name="file" type="file"
									class="form-control" />
							</div>
					  </div>
					  
					  <div class="col-sm-8 column">
					  <b>Template Format:</b><br>
					  Program Abbreviation | Program Full Name | Duration In Months | Max Duration of Program in Months | Session Type (ANNUAL, SEMESTER, TRIMESTER) | Program Revised From Month | Program Revised From Year
					  <br><br>
					  <a href="resources/templates/Program_Upload_Template.xlsx">Download a sample template</a>
					  </div>
					</div>
					<div class="row">

						<div class="col-sm-12 column">
							<div class="form-group">

								<button id="submit"	class="btn btn-large btn-primary" formaction="uploadProgram">Upload</button>
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
