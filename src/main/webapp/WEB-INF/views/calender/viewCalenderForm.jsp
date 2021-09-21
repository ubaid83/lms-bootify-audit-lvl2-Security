<%@page import="com.spts.lms.web.utils.Utils"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@page import="java.util.*"%>



<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<jsp:include page="../common/newDashboardHeader.jsp" /> 

<div class="d-flex dataTableBottom" id="facultyAssignmentPage">
<jsp:include page="../common/newLeftNavBarFaculty.jsp"/>
<jsp:include page="../common/newLeftSidebarFaculty.jsp"/>
<jsp:include page="../common/rightSidebarFaculty.jsp"/>

<!-- DASHBOARD BODY STARTS HERE -->

<div class="container-fluid m-0 p-0 dashboardWraper">

	<jsp:include page="../common/newTopHeaderFaculty.jsp" />
     
     <div class="container mt-5">
                <div class="row">
            <!-- SEMESTER CONTENT -->    
                    <div class="col-lg-9 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto">
                        
          <!-- page content: START -->
          
                        <nav aria-label="breadcrumb">
                            <ol class="breadcrumb">
                                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
                                <li class="breadcrumb-item" aria-current="page"><c:out value="${Program_Name}" /></li>
                                <sec:authorize access="hasRole('ROLE_STUDENT')">
								<c:out value="${AcadSession}" />
							</sec:authorize>
                                <li class="breadcrumb-item active" aria-current="page">Calendar</li>
                            </ol>
                        </nav>
                        
						<jsp:include page="../common/alert.jsp" />
						
						
						<!-- Input Form Panel -->
						<div class="card bg-white border">
						<div class="card-body">
							<div class="col-md-12">
								<div class="x_panel">
									<div class="x_title">
										<h2>
											Calendar Events 
										</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
											<!-- <li class="dropdown"><a href="#" class="dropdown-toggle"
												data-toggle="dropdown" role="button" aria-expanded="false"><i
													class="fa fa-wrench"></i></a>
												<ul class="dropdown-menu" role="menu">
													<li><a href="#">Settings 1</a></li>
													<li><a href="#">Settings 2</a></li>
												</ul></li> -->
											<li><a class="close-link"><i class="fa fa-close"></i></a>
											</li>
										</ul>
										<div class="clearfix"></div>
									</div>
									<div class="x_content">

										<div id='calendar'></div>

									</div>
								</div>
							</div>
						</div>
						</div>

						<!-- Results Panel -->
					
						
							<div id="CalenderModalNew" class="modal fade" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<form:form id="eventForm" method="get" action="addCalenderEvent"
					modelAttribute="calender" class="form-horizontal calender">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-hidden="true">×</button>
						<h4 class="modal-title" id="myModalLabel">New Calendar Entry</h4>
					</div>
					<div class="modal-body">

						<div id="testmodal" style="padding: 5px 20px;">
							<form id="antoform" class="form-horizontal calender" role="form">


								<%
								if ("true".equals((String) request.getAttribute("edit"))) {
							%>
							<legend>Edit Event</legend>
							<%
								} else {
							%>
							<legend>Add Event</legend>
							<%
								}
							%>

							<%
								if ("true".equals((String) request.getAttribute("edit"))) {
							%>
							<form:input type="hidden" path="id" value="${calender.id}" />
							<%
								}
							%>
							<form:input type="hidden" id="calenderId" path="id" value="${calender.id}" />
							
							<div class="form-group">

								<form:label class="col-sm-3 control-label" path="event"
									for="event">Event Name <span style="color: red">*</span></form:label>
								<div class="col-sm-9">


									<form:input id="event" path="event" type="text"
										placeholder="Event Name" class="form-control control-label"
										required="required" />

								</div>
							</div>
							<div class="form-group">

								<form:label class="col-sm-3 control-label" path="description"
									for="editor">Description</form:label>

								<div class="col-sm-9">

									<form:textarea class="form-group form-control"
										path="description" id="description"
										style="margin-top: 30px;margin-bottom:10px" />

								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label">Course <span style="color: red">*</span></label>
								<div class="col-sm-9">
									<form:select class="form-control" id="courseDropDown"
										path="courseId" placeholder="Course" required="required">
										<form:option value="">Choose option</form:option>
										<form:options items="${courses}" itemLabel="courseName"
											itemValue="id" />

									</form:select>
								</div>
							</div>

							<sec:authorize access="hasAnyRole('ROLE_FACULTY','ROLE_ADMIN','ROLE_CORD')">
							<div class="form-group">
								<label class="col-sm-3 control-label">Can be viewed by <span style="color: red">*</span></label>
								<div class="col-sm-9">
									<form:select class="form-control" id="showStatusDropDown"
										path="showStatus" placeholder="viewed by" required="required">
										<form:option value="Y" >viewed by all</form:option>
										<form:option value="N" selected="selected">Only me</form:option>
										
									</form:select>
								</div>
							</div>
							</sec:authorize>

							<div class="form-group">
								<form:label class="col-sm-3 control-label" path="startDate"
									for="startDate">Start Date <span style="color: red">*</span></form:label>
								<div class="col-sm-9">
									
										
										<div class='input-group date' >
															<form:input id="startDate" path="startDate" type="text"
																placeholder="Start Date" class="form-control"
																required="required" />
															<span class="input-group-addon"><span
																class="glyphicon glyphicon-calendar"></span> </span>
														</div>
														

								</div>
							</div>

							<div class="form-group">
								<form:label class="col-sm-3 control-label" path="endDate"
									for="endDate">End Date <span style="color: red">*</span></form:label>
								<div class="col-sm-9">
										<div class='input-group date' >
															<form:input id="endDate" path="endDate" type="text"
																placeholder="End Date" class="form-control"
																required="required" />
															<span class="input-group-addon"><span
																class="glyphicon glyphicon-calendar"></span> </span>
														</div>

								</div>
							</div>


							 </form>
						</div>


					</div>
					<div class="modal-footer text-center">

						<%-- 	<%
									if ("true".equals((String) request.getAttribute("edit"))) {
								%>
								
								<button type="button" id="submit"	class="btn btn-large btn-primary antosubmit" formaction="updateCalenderEvent" >Update Event</button>
								<%
									} else {
								%>
									
										<button type="button" id="add"	class="btn btn-large btn-primary antosubmit" formaction="addCalenderEvent" >A Event</button>
								<%
									}
								%> --%>
					<sec:authorize access="hasAnyRole('ROLE_FACULTY','ROLE_ADMIN','ROLE_CORD')">
						<button type="button" id="addEvent"
							class="btn  btn-primary antosubmit callEvent " 
							>Add Event</button>
							
							<button type="button" id="updateEvent"
							class="btn  btn-primary antosubmit  callEvent"
							 style="display:none;">Update Event</button>
							 
							 <button type="button" id="deleteEvent"
							class="btn  btn-primary antosubmit  callEvent"
							 style="display:none;">Delete Event</button>

							<button type="button" id="inviteEvent"
							class="btn  btn-primary antosubmit  callEvent" 
							 style="display:none;">Send Invitation</button>
							</sec:authorize>
							
						<button type="button" id="cancel"
							class="btn btn-primary antoclose " formaction="viewevent"
							formnovalidate="formnovalidate" data-dismiss="modal">Cancel</button>

					</div>
				</form:form>

			</div>
		</div>
	</div>


	<div id="fc_create" data-toggle="modal" data-target="#CalenderModalNew"></div>
	<div id="fc_edit" data-toggle="modal" data-target="#CalenderModalNew"></div>	
							

					<%-- 	<jsp:include page="../common/paginate.jsp">
							<jsp:param name="baseUrl" value="searchFacultyGroups" />
						</jsp:include> --%>

			<!-- /page content: END -->
                   
                    </div>
			
			<!-- SIDEBAR START -->
	         <jsp:include page="../common/newSidebar.jsp" />
	        <!-- SIDEBAR END -->
	<jsp:include page="../common/footer.jsp"/>

	
<script src="<c:url value="/resources/js/calender/calendarEvents.js" />"
	type="text/javascript"></script>
<script type="text/javascript">
	var headerCourseId = null;
	function theFunction(obj) {
		return true;
	}
	var events = ${json};
	
</script>

<script>
		
		
		$(".callEvent").click(function(){
			/* console.log("called modal ");
			document.getElementById("eventForm").reset(); */
			if($('#eventForm')[0].checkValidity()){
				
			var form = document.getElementById ('eventForm');
			var url;
			var buttonId = $(this).attr("id");
			console.log('buttonId'+buttonId);
			if(buttonId =='addEvent'){
				url = '${pageContext.request.contextPath}/addCalenderEvent';
				form.setAttribute("action", url);
			}else if(buttonId =='updateEvent'){
				url = '${pageContext.request.contextPath}/updateCalenderEvent';
				form.setAttribute("action", url);
				
			}else if(buttonId =='deleteEvent'){
				url = '${pageContext.request.contextPath}/deleteCalenderEvent';
				form.setAttribute("action", url);
				
			}else if(buttonId =='inviteEvent'){
				//alert("event id is "+$('#calenderId').val());
				url = '${pageContext.request.contextPath}/saveStudentEvents?eventId=';
				form.setAttribute("action", url);
			}
			form.submit();
			
			}else{
				alert("Field Empty");
				$('.antoclose2').click();
				$('.antoclose').click();
			}
			
		});

		$(document).ready(function() {
			init_calendar();

		});
	</script>












