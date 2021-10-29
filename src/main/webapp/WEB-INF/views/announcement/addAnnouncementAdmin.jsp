<%@page import="java.util.*"%>
<%@page import="com.spts.lms.beans.dashboard.DashBoard"%>
<%-- <jsp:include page="../common/DashboardHeader.jsp" /> --%>
<%-- <jsp:include page="../common/NewHeader1.jsp" /> --%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>


<%
	boolean isEdit = "true".equals((String) request
      .getAttribute("edit"));
%>
    
        <jsp:include page="../common/newDashboardHeader.jsp" /> 

    <div class="d-flex adminPage" id="adminPage">
    <jsp:include page="../common/newAdminLeftNavBar.jsp"/>
    <jsp:include page="../common/rightSidebarAdmin.jsp" />


    <!-- DASHBOARD BODY STARTS HERE -->

    <div class="container-fluid m-0 p-0 dashboardWraper">

        <jsp:include page="../common/newAdminTopHeader.jsp" />

                <!-- SEMESTER CONTENT -->    
                        <div class="col-lg-12 col-md-12 col-sm-12 dashboardBody p-5">
                        
                        <nav aria-label="breadcrumb">
								<ol class="breadcrumb">
									<li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
									<li class="breadcrumb-item active" aria-current="page">Create
										Announcement</li>
								</ol>
							</nav>
							<!-- 14-06-2021 -->
							<jsp:include page="../common/alert.jsp" />
							<div class="card bg-white">
								<div class="card-body">

									<form:form action="addAnnouncement" id="addAnnouncement"
										method="post" modelAttribute="announcement"
										enctype="multipart/form-data">
										<form:input path="courseId" type="hidden" />
										<form:input path="programId" type="hidden" />
										<form:input path="announcementType" type="hidden" />
										<h4 class="text-uppercase">
											<%
												if (isEdit) {
											%>
											<form:input type="hidden" path="id" />
											Edit ${announcement.announcementType} Announcement
											<%
												} else {
											%>
											Add ${announcement.announcementType} Announcement
											<%
												}
											%>
										</h4>
										<hr />
										<div class="form-row">

											<div class="col-12 text-center">
												<h5>
													<sec:authorize access="hasRole('ROLE_ADMIN')">
														<c:if
															test="${announcement.announcementType == 'INSTITUTE'}">
															(Will be visible to All Students
																of Institute)
														</c:if>
													</sec:authorize>
													<c:if test="${announcement.announcementType == 'COURSE'}">
         ${courseIdNameMap[announcement.courseId] } 
        <font size="3px">(Will be visible to All Students
															Enrolled in this Course)</font>
													</c:if>
												</h5>
											</div>

											<!--FORM ITEMS STARTS HERE-->

											<sec:authorize access="hasAnyRole('ROLE_ADMIN', 'ROLE_EXAM')">
												<div
													class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
													<label>Sub Type <span class="text-danger">*</span></label>
													<%-- <form:input id="assignmentName" path="assignmentName"
                                                                              type="text" placeholder="Assignment Name"
                                                                              class="form-control" required="required" /> --%>
													<form:select id="announcementSubType"
														path="announcementSubType"
														placeholder="Announcement Subtype" class="form-control">
														<form:option value="" >Select Announcement Subtype</form:option>
														<form:option value="EXAM">EXAM</form:option>
														<form:option value="EVENT">EVENT</form:option>
														<form:option value="ASSIGNMENT">ASSIGNMENT</form:option>
														<form:option value="Internal">Internal Marks</form:option>
														<form:option value="Academics">Academics</form:option>
														<form:option value="WeCare">We Care</form:option>
														<form:option value="FROMTHECOUNSELLORSDESK">From the Counsellor's Desk</form:option>

													</form:select>
												</div>
											</sec:authorize>


											<div
												class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
												<label>Title <span class="text-danger">*</span></label>
												<form:input id="subject" path="subject" type="text"
													placeholder="Announcement Title" class="form-control"
													required="required" />
											</div>
											<div
												class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
												<div class="form-group">
													<label for="file"> <%
 	if (isEdit) {
 %> Select if you wish to override earlier file <%
 	} else {
 %> Announcement File <%
 	}
 %>
													</label> <input id="file" name="file" type="file"
														class="form-control" multiple="multiple" />
												</div>
												<div id=fileSize></div>
											</div>

											<div class="col-sm-6 col-md-4 col-xs-12 column">
																<div class="form-group">

																	<form:label path="startDate" for="startDate">Display From <span style="color: red">*</span></form:label>

																	<div class='input-group date' id='datetimepicker1'>
																		<form:input id="startDate" path="startDate"
																			type="text" placeholder="Start Date"
																			class="form-control" required="required"
																			readonly="true" />

																		<span class="input-group-addon"><span
																			class="glyphicon glyphicon-calendar"></span> </span>
																	</div>


																</div>
															</div>
															<div class="col-sm-6 col-md-4 col-xs-12 column">
																<div class="form-group">
																	<form:label path="endDate" for="endDate">Display Until <span style="color: red">*</span></form:label>

																	<div class='input-group date' id='datetimepicker2'>
																		<form:input id="endDate" path="endDate" type="text"
																			placeholder="End Date" class="form-control"
																			required="required" readonly="true" />
																		<span class="input-group-addon"><span
																			class="glyphicon glyphicon-calendar"></span> </span>
																	</div>

																</div>
															</div>



											<c:if test="${allCampuses.size() > 0}">
												<div
													class="col-lg-4 col-md-4 col-sm-12 position-relative">
													<div class="form-group">
														<form:label path="campusId" for="campusId">Select Campus</form:label>

														<form:select id="campusId" path="campusId" type="text"
															placeholder="campus" class="form-control">
															<form:option value="">Select Campus</form:option>
															<c:forEach var="campus" items="${allCampuses}"
																varStatus="status">
																<form:option value="${campus.campusId}">${campus.campusName}</form:option>
															</c:forEach>
														</form:select>
													</div>
												</div>
											</c:if>

										</div>


										<hr />

										<div class="form-row">
											<table
												class="table table-bordered table-striped mt-5 font-weight-bold">
												<tbody>
													<tr>
														<td>Send Email Alert for New Announcement?</td>
														<td>
															<div class="pretty p-switch p-fill p-toggle">
																<form:checkbox value="Yes" id="sendEmailAlert"
																	path="sendEmailAlert" />
																<div class="state p-success p-on">
																	<label>Yes</label>
																</div>
																<div class="state p-danger p-off">
																	<label>No</label>
																</div>

															</div>
														</td>
													</tr>
													<tr>
														<td>Send SMS Alert for New Announcement?</td>
														<td>
															<div class="pretty p-switch p-fill p-toggle">
																<form:checkbox value="Yes" id="sendSmsAlert"
																	path="sendSmsAlert" />
																<div class="state p-success p-on">
																	<label>Yes</label>
																</div>
																<div class="state p-danger p-off">
																	<label>No</label>
																</div>

															</div>
														</td>
													</tr>
													<c:if test="${sendAlertsToParents eq true}">
														<tr>
															<td>Send Email Alert To Parents?</td>
															<td>
																<div class="pretty p-switch p-fill p-toggle">
																	<form:checkbox value="Yes" id="sendEmailAlertParents"
																		path="sendEmailAlertToParents" />
																	<div class="state p-success p-on">
																		<label>Yes</label>
																	</div>
																	<div class="state p-danger p-off">
																		<label>No</label>
																	</div>

																</div>
															</td>
														</tr>
														<tr>
															<td>Send SMS Alert To Parents?</td>
															<td>
																<div class="pretty p-switch p-fill p-toggle">
																	<form:checkbox value="Yes" id="sendEmailAlertParents"
																		path="sendSmsAlertToParents" />
																	<div class="state p-success p-on">
																		<label>Yes</label>
																	</div>
																	<div class="state p-danger p-off">
																		<label>No</label>
																	</div>

																</div>
															</td>
														</tr>
													</c:if>
												</tbody>
											</table>
										</div>


										<div class="col-12 mt-5">
											<h5>Announcement Text / Instructions</h5>
											<form:textarea class="testDesc ckeditorClass" name="testDesc"
												path="description"></form:textarea>
										</div>

										<div class="col-6 m-auto">
											<div class="form-row mt-5">


												<%
													if (isEdit) {
												%>
												<button id="submit"
													class="btn btn-info col-md-5 col-sm-12  ml-auto mr-auto mb-3"
													formaction="updateAnnouncement">Update Announcement</button>
												<%
													} else {
												%>
												<button id="submit" onclick="myFunction()"
													class="btn btn-info col-md-5 col-sm-12  ml-auto mr-auto mb-3"
													formaction="addAnnouncement">Add Announcement</button>
												<%
													}
												%>




											</div>
										</div>



									</form:form>



								</div>
							</div>
                        



                        </div>

                <!-- SIDEBAR START -->

                <!-- SIDEBAR END -->
        <jsp:include page="../common/newAdminFooter.jsp"/>
        
        <script>
		function myFunction(){
			console.log("fdghdhyrfdhuytuytrutryutrruht");
			var dateinput = $('#startDate').val();
			console.log(dateinput);
		}
		
		$(function() {
			
			$('#submit').on('click', function(e){ 
				var dateinput = $('#startDate').val();
				console.log(dateinput);
				if(dateinput == ''){
					alert('Error: Enter proper Date inputs.');
					
					    e.preventDefault();
				
				} else {
					
				}
				
			});
			
		});
        </script>
        
        <script type="text/javascript">
					$('#file').bind(
							'change',
							function() {
								// alert('This file size is: ' + this.files[0].size/1024/1024 + "MB");
								var fileSize = this.files[0].size / 1024 / 1024
										+ " MB";
								$('#fileSize').html("File Size:" + (fileSize));
							});
				</script>
				
	<script>
	
		$(window).bind("pageshow", function(){

			$("#startDate").val('');
			$('#endDate').val('');
			  		  $('#startDate').daterangepicker({
			  		      autoUpdateInput: false,
			  		      locale: {
			  		          cancelLabel: 'Clear'
			  		      },
			  		      "singleDatePicker": true,
			          	  "showDropdowns" : true,
			                "timePicker" : true,
			                "showCustomRangeLabel" : false,
			                "alwaysShowCalendars" : true,
			                "opens" : "center"
			  		  });

			  		  $('#startDate').on('apply.daterangepicker', function(ev, picker) {
			  		      $(this).val(picker.startDate.format('YYYY-MM-DD HH:mm:ss'));
			  		  });

			  		  $('#startDate').on('cancel.daterangepicker', function(ev, picker) {
			  		      $(this).val('');
			  		  });
			  		  
			  		  
			  		$('#endDate').daterangepicker({
			  		      autoUpdateInput: false,
			  		      locale: {
			  		          cancelLabel: 'Clear'
			  		      },
			  		      "singleDatePicker": true,
			          	  "showDropdowns" : true,
			                "timePicker" : true,
			                "showCustomRangeLabel" : false,
			                "alwaysShowCalendars" : true,
			                "opens" : "center"
			  		  });

			  		  $('#endDate').on('apply.daterangepicker', function(ev, picker) {
			  		      $(this).val(picker.startDate.format('YYYY-MM-DD HH:mm:ss'));
			  		  });

			  		  $('#endDate').on('cancel.daterangepicker', function(ev, picker) {
			  		      $(this).val('');
			  		  });

		}); 
		
		</script>