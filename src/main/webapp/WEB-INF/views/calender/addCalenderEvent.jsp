<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<html lang="en">
<jsp:include page="../common/css.jsp" />
<link
	href="<c:url value="/resources/css/froala/froala_editor.min.css" />"
	rel="stylesheet">
<link
	href="<c:url value="/resources/css/froala/froala_style.min.css" />"
	rel="stylesheet">
<link
	href="<c:url value="/resources/css/froala/froala_content.min.css" />"
	rel="stylesheet">

<link href="<c:url value="/resources/css/froala/themes/dark.min.css" />"
	rel="stylesheet">
<link href="<c:url value="/resources/css/froala/themes/grey.min.css" />"
	rel="stylesheet">
<link href="<c:url value="/resources/css/froala/themes/red.min.css" />"
	rel="stylesheet">
<link
	href="<c:url value="/resources/css/froala/themes/royal.min.css" />"
	rel="stylesheet">
<link href="<c:url value="/resources/css/froala/themes/blue.min.css" />"
	rel="stylesheet">

<link
	href="https://gitcdn.github.io/bootstrap-toggle/2.2.2/css/bootstrap-toggle.min.css"
	rel="stylesheet">
<script
	src="https://gitcdn.github.io/bootstrap-toggle/2.2.2/js/bootstrap-toggle.min.js"></script>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>	
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
							Add Event
						</div>
                        <jsp:include page="../common/alert.jsp" />
						<!-- Input Form Panel -->
                        <div class="row">
                            <div class="col-xs-12 col-sm-12">
                                <div class="x_panel">
								
                                    <div class="x_title">
                                        <h2>Add Event</h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a></li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
									
                                    <div class="x_content">
                                        <form:form action="addCalenderEvent" method="post"
			modelAttribute="calender">
			
				<div class="row page-body">

					 <%if ("true".equals((String) request.getAttribute("edit"))) {%>
					
				<%}else{%>
					
				<%}%>


					

				 <%
						if ("true".equals((String) request.getAttribute("edit"))) {
					%>
					<form:input type="hidden" path="id" value="${calender.id}" />
					<%
						}
					%> 

					<%-- <div class="col-sm-4 col-md-4 col-xs-12 column">
					<div class="form-group">
						<form:input id="abbr" path="abbr" type="text" required="required"
							placeholder="Course Abbriviation" class="form-control" />
					</div>
					</div> --%>
					<div class="row ">
						<div class="col-sm-4 col-md-4 col-xs-12 column">
							<div class="form-group">
								<form:label path="event" for="event">Event Name</form:label>
								<form:input id="event" path="event" type="text"
									placeholder="Event Name" class="form-control" />
							</div>
						</div>

						<div class="col-sm-4 col-md-4 col-xs-12 column">
							<div class="form-group">
								<label class="control-label" for="courseId">Course <span style="color: red">*</span></label>
								<form:select id="course" path="courseId" placeholder="Course"
									class="form-control facultyparameter" required="required">
									<form:option value="">Select Course</form:option>
									<form:options items="${courseList}" itemLabel="courseName"
										itemValue="id" />
								</form:select>
							</div>
						</div>





						<div class="col-sm-4 col-md-4 col-xs-12 column">
							<div class="form-group">
								<form:label path="startDate" for="startDate">Start Date <span style="color: red">*</span></form:label>
								
									
									<div class='input-group date' id='datetimepicker1'>
															<form:input id="startDate" path="startDate" type="text"
																placeholder="Start Date" class="form-control"
																required="required" readonly="true"/>
															<span class="input-group-addon"><span
																class="glyphicon glyphicon-calendar"></span> </span>
														</div>
														
							</div>
						</div>
					</div>
					<div class="row" style="margin-right: -60%;">





						<div class="col-sm-4 col-md-4 col-xs-12 column">
							<div class="form-group">
								<form:label path="endDate" for="endDate">End Date <span style="color: red">*</span></form:label>
									
									<div class='input-group date' id='datetimepicker2'>
															<form:input id="endDate" path="endDate" type="text"
																placeholder="End Date" class="form-control"
																required="required" readonly="true"/>
															<span class="input-group-addon"><span
																class="glyphicon glyphicon-calendar"></span> </span>
														</div>
														
							</div>
						</div>
					</div>
					<div class="row" style="margin-right: -60%;">
						<div class="col-sm-6 column">
						<div class="form-group">
								<form:label path="description" for="editor">Description</form:label>
							
									<form:textarea class="form-group" path="description" name="editor1" id="editor1" rows="10" cols="80" />
							</div>
						</div>
					</div>
					<div class="row " style="margin-right: -60%;">
						
						<div class="col-sm-4 col-md-4 col-xs-12 column">
							<div class="form-group">

								<!-- <button id="submit" name="submit"
									class="btn btn-large btn-primary" formaction="addCalenderEvent">Add</button> -->
								<%
									if ("true".equals((String) request.getAttribute("edit"))) {
								%>
								<button id="submit"	class="btn btn-large btn-primary" formaction="updateCalenderEvent">Update Event</button>
								<%
									} else {
								%>
								<button id="add" name="submit"
									class="btn btn-large btn-primary" formaction="addCalenderEvent">Add Event</button>
								<%
									}
								%>
								<button id="cancel" name="cancel" class="btn btn-danger"
									formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
							</div>
						</div>


					</div>
				</div>


			
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
            
            <jsp:include page="../common/paginate.jsp">
		<jsp:param name="baseUrl" value="searchList" />
	</jsp:include>
            <jsp:include page="../common/footer.jsp" />
            
        </div>
    </div>


    

<script type="text/javascript" src="<c:url value="/resources/js/customDateTimePicker.js" />" ></script>
    
    
</body>
<!-- <script src="//cdn.ckeditor.com/4.8.0/standard/ckeditor.js"></script>
 
<script>
                // Replace the <textarea id="editor1"> with a CKEditor
                // instance, using default configuration.
                CKEDITOR.replace( 'editor1' );
            </script> -->
            
<script type="text/javascript" src="http://cdn.ckeditor.com/4.8.0/standard-all/ckeditor.js"></script>
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/MathJax.js?config=TeX-AMS_HTML"></script>

<script type="text/javascript">
	CKEDITOR.replace( 'editor1' , {
			extraPlugins: 'mathjax',
			mathJaxLib: 'https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/MathJax.js?config=TeX-AMS_HTML'
		});
		
</script>
            
            
            <script>
           // $(document).ready(function() {
            $("#datetimepicker1").on("dp.change", function(e) {
            	
            	validDateTimepicks();
            }).datetimepicker({
            	// minDate:new Date(),
           	  useCurrent: false,
           	  format: 'YYYY-MM-DD HH:mm:ss'
            });

            $("#datetimepicker2").on("dp.change", function(e) { 	
            	
            	validDateTimepicks();
            }).datetimepicker({
            	// minDate:new Date(),
           	  useCurrent: false,
           	  format: 'YYYY-MM-DD HH:mm:ss'
            	});

function validDateTimepicks(){
	 if(($('#startDate').val() !='' && $('#startDate').val() !=null)&&($('#endDate').val() !='' && $('#endDate').val() !=null)){
	        var fromDate = $('#startDate').val();
	        var toDate = $('#endDate').val();
	        var eDate = new Date(fromDate);
	        var sDate = new Date(toDate);
	        if(sDate < eDate){
	        	alert("endate cannot be smaller than startDate");
	        	$('#startDate').val("");
	        	$('#endDate').val("");       	
	        }
	    } 
}
           // });
</script>
</html>
