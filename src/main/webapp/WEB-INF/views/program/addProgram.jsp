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
      <%--   <jsp:include page="../common/alert.jsp" /> --%>
            
            
            <!-- page content: START -->
            <div class="right_col" role="main">
                
                <div class="dashboard_container">
                    
                    <div class="dashboard_container_spacing">
                        
                        <div class="breadcrumb">
							<a href="${pageContext.request.contextPath}/homepage">Dashboard</a>
							<i class="fa fa-angle-right"></i>  
							Add Program
						</div>
						<jsp:include page="../common/alert.jsp" />
                        
						<!-- Input Form Panel -->
                        <div class="row">
                            <div class="col-xs-12 col-sm-12">
                                <div class="x_panel">
								
                                    <div class="x_title">
                                        <h2>
					<%
						if ("true".equals((String) request.getAttribute("edit"))) {
					%>
					Edit Program
					<%
						} else {
					%>
					Add Program
					<%
						}
					%></h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a></li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
									
                                    <div class="x_content">
                                        <form:form action="addProgram" method="post" modelAttribute="program">
			
					<div class="row">


						<div class="col-sm-6 col-md-4 col-xs-12 column">
							<%
								if ("true".equals((String) request.getAttribute("edit"))) {
							%>
							<form:input type="hidden" path="id" value="${program.id}" />
							<%
								}
							%>

							<div class="form-group">
								<form:label path="abbr" for="abbr">Program Abbreviation <span style="color: red">*</span></form:label>
								<form:input id="abbr" path="abbr" type="text"
									required="required" placeholder="Prgoram Abbreviation"
									class="form-control" value="${program.abbr}" />
							</div>
						</div>
						<div class="col-sm-6 col-md-4 col-xs-12 column">
							<div class="form-group">
								<form:label path="programName" for="programName">Program Name</form:label>
								<form:input id="programName" path="programName" type="text"
									placeholder="Program Full Name" class="form-control" />
							</div>
						</div>
						<div class="col-sm-6 col-md-4 col-xs-12 column">
							<div class="form-group">
								<form:label path="sessionType" for="sessionType">Program Type <span style="color: red">*</span></form:label>
								<form:select id="sessionType" path="sessionType"
									class="form-control" required="required"
									itemValue="${program.sessionType}">
									<form:option value="">Select Program Type</form:option>
									<form:option value="ANNUAL">ANNUAL</form:option>
									<form:option value="SEMESTER">SEMESTER</form:option>
									<form:option value="TRIMESTER">TRIMESTER</form:option>
								</form:select>
							</div>
						</div>
						<div class="col-sm-6 col-md-4 col-xs-12 column">
							<div class="form-group">
								<form:label path="durationInMonths" for="durationInMonths">Program Duration</form:label>
								<form:input id="durationInMonths" path="durationInMonths"
									type="number" placeholder="Program Duration in Months"
									class="form-control" value="${program.durationInMonths}" />
							</div>
						</div>
						<div class="col-sm-6 col-md-4 col-xs-12 column">
							<div class="form-group">
								<form:label path="maxDurationInMonths" for="maxDurationInMonths">Program Max Duration</form:label>
								<form:input id="maxDurationInMonths" path="maxDurationInMonths"
									type="number"
									placeholder="Maximum Duration of Program in Months"
									class="form-control" value="${program.maxDurationInMonths}" />
							</div>
						</div>
						<div class="col-sm-6 col-md-4 col-xs-12 column">
							<div class="form-group">
								<form:label path="revisedFromMonth" for="revisedFromMonth">Revision Month <span style="color: red">*</span></form:label>
								<form:select id="revisedFromMonth" path="revisedFromMonth"
									class="form-control" required="required"
									itemValue="${program.revisedFromMonth}">
									<form:option value="">Select Month of Program Syllabus Revision</form:option>
									<form:option value="Jan">January</form:option>
									<form:option value="Feb">February</form:option>
									<form:option value="Mar">March</form:option>
									<form:option value="Apr">April</form:option>
									<form:option value="May">May</form:option>
									<form:option value="Jun">June</form:option>
									<form:option value="Jul">July</form:option>
									<form:option value="Aug">August</form:option>
									<form:option value="Sep">September</form:option>
									<form:option value="Oct">October</form:option>
									<form:option value="Nov">November</form:option>
									<form:option value="Dec">December</form:option>
								</form:select>
							</div>
						</div>
						<div class="col-sm-6 col-md-4 col-xs-12 column">
							<div class="form-group">
								<form:label path="revisedFromYear" for="revisedFromYear">Revision Year <span style="color: red">*</span></form:label>
								<form:select id="revisedFromYear" path="revisedFromYear"
									class="form-control" required="required"
									itemValue="${program.revisedFromYear}">
									<form:option value="">Select Year of Program Syllabus Revision</form:option>
									<form:option value="2010">2010</form:option>
									<form:option value="2011">2011</form:option>
									<form:option value="2012">2012</form:option>
									<form:option value="2013">2013</form:option>
									<form:option value="2014">2014</form:option>
									<form:option value="2015">2015</form:option>
									<form:option value="2016">2016</form:option>
									<form:option value="2017">2017</form:option>
									<form:option value="2018">2018</form:option>
									<form:option value="2019">2019</form:option>
									<form:option value="2020">2020</form:option>
								</form:select>
							</div>

						</div>
					</div>
					<div class="row">

						<div class="col-sm-12 column">
							<div class="form-group">

								<%
									if ("true".equals((String) request.getAttribute("edit"))) {
								%>
								<button id="submit" name="submit"
									class="btn btn-large btn-primary" formaction="updateProgram">Update</button>
								<%
									} else {
								%>
								<button id="submit" name="submit"
									class="btn btn-large btn-primary" formaction="addProgram">Add</button>
								<%
									}
								%>
								<button id="cancel" name="cancel" class="btn btn-danger"
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
