<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
	
	
	    <jsp:include page="../common/newDashboardHeader.jsp" /> 

    <div class="d-flex adminPage dataTableBottom" id="adminPage">
    <jsp:include page="../common/newAdminLeftNavBar.jsp"/>
    <jsp:include page="../common/rightSidebarAdmin.jsp" />


    <!-- DASHBOARD BODY STARTS HERE -->

    <div class="container-fluid m-0 p-0 dashboardWraper">

        <jsp:include page="../common/newAdminTopHeader.jsp" />

                <!-- SEMESTER CONTENT -->    
                        <div class="col-lg-12 col-md-12 col-sm-12 dashboardBody p-5">
                        
                        			<!-- page content: START -->
                        			
                        			 <nav aria-label="breadcrumb">
                    <ol class="breadcrumb">
                        <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
                        <li class="breadcrumb-item" aria-current="page">
                            <c:out value="${Program_Name}" />
                        </li>
                        <sec:authorize access="hasRole('ROLE_STUDENT')">
                            <c:out value="${AcadSession}" />
                        </sec:authorize>
                        <li class="breadcrumb-item active" aria-current="page"> Announcement List</li>
                    </ol>
                </nav>
                        			



						<jsp:include page="../common/alert.jsp" />
						<!-- Input Form Panel -->
						<div class="card bg-white border">
							<div class="card-body">
									<div class="text-center border-bottom pb-2 mb-3">
										<h5>Announcement List</h5>
										<c:url value="addAnnouncementForm" var="addAnnouncementUrl">

										</c:url>

										<%--
										
										<sec:authorize
											access="hasAnyRole('ROLE_FACULTY', 'ROLE_ADMIN', 'ROLE_DEAN')">
											<a class="btn btn-primary btn-sm pull-right" role="button"
												href="${addAnnouncementUrl}"><i class="fa fa-plus"
												aria-hidden="true"></i> Add Announcement</a>
										</sec:authorize>
										
										
										--%>
							
									</div>

										<form:form action="searchAnnouncement" method="post"
											modelAttribute="announcement">
											<div class="row">
												<div class="col-md-4 col-sm-6">
													<div class="form-group">
														<form:label path="subject" for="subject">Announcement Title</form:label>
														<form:input id="subject" path="subject" type="text"
															placeholder="Announcement Title" class="form-control" />
													</div>
												</div>
												<div class="col-md-4 col-sm-6">
													<div class="form-group">
														<form:label path="announcementSubType"
															for="announcementSubType">Announcement SubType</form:label>
														<form:select id="announcementSubType"
															path="announcementSubType" class="form-control">
															<form:option value="">Select Announcement Subtype</form:option>
															<form:option value="EXAM">EXAM</form:option>
															<form:option value="EVENT">EVENT</form:option>
															<form:option value="ASSIGNMENT">ASSIGNMENT</form:option>
															<form:option value="Internal">Internal Marks</form:option>
															<form:option value="Academics">Academics</form:option>
															<form:option value="WeCare">We Care</form:option>
															<form:option value="FROMTHECOUNSELLORSDESK">From the Counsellor's Desk</form:option>
														</form:select>
													</div>
												</div>
												<div class="col-md-4 col-sm-6">
													<div class="form-group">
														<form:label path="startDate" for="startDate">Display From</form:label>

														<div class='input-group date' id='datetimepicker1'>
															<form:input id="startDate" path="startDate" type="text"
																placeholder="Start Date" class="form-control"
																readonly="true" />
															<span class="input-group-addon"><span
																class="glyphicon glyphicon-calendar"></span> </span>
														</div>
													</div>
												</div>
												<div class="col-md-4 col-sm-6">
													<div class="form-group">
														<form:label path="endDate" for="endDate">Display Until</form:label>

														<div class='input-group date' id='datetimepicker2'>
															<form:input id="endDate" path="endDate" type="text"
																placeholder="End Date" class="form-control"
																readonly="true" />
															<span class="input-group-addon"><span
																class="glyphicon glyphicon-calendar"></span> </span>
														</div>

													</div>
												</div>

												<div class="col-md-4 col-sm-6">
													<div class="form-group">
														<form:label path="announcementType" for="announcementType">Announcement Type</form:label>
														<form:select id="announcementType" path="announcementType"
															class="form-control"
															itemValue="${announcement.announcementType}">
															<form:option value="">Select Announcement Type</form:option>
															<form:option value="COURSE">Course Related</form:option>
															<%-- <form:option value="PROGRAM">Program Related</form:option> Commented by Sanket. Program level Announcements not provided in backend logic--%>
															<form:option value="INSTITUTE">Institute Related</form:option>
															<form:option value="LIBRARY">Library Related</form:option>
															<form:option value="COUNSELOR">Counselor Related</form:option>

														</form:select>
													</div>
												</div>

												<div class="col-sm-12">
													<div class="form-group">
														<button id="submit" name="submit"
															class="btn btn-large btn-primary">Search</button>
														<button type="reset" value="reset"
															class="btn btn-large btn-dark">Reset</button>
<!-- 
														<button id="cancel" name="cancel" class="btn btn-danger"
															formnovalidate="formnovalidate">Cancel</button> -->
													</div>
												</div>
											</div>


										</form:form>
							</div>
						</div>

						<!-- Results Panel -->
						<c:choose>
							<c:when test="${page.rowCount > 0}">
								<div class="card bg-white border">
									<div class="card-body">

												<h5 class="text-center border-bottom pb-2">Announcements | ${page.rowCount} Records Found</h5>
			
												<div class="table-responsive testAssignTable">
													<table class="table table-striped table-hover">
														<thead>
															<tr>
																<th>Sr. No.</th>
																<th>Announcement Title</th>
																<th>Type</th>
																<th>SubType</th>
																<th>Course</th>
																<th>Program</th>
																<th>Start Date</th>
																<th>End Date</th>

																<th>Actions</th>
															</tr>
														</thead>
														<tbody>

															<c:forEach var="announcement" items="${page.pageItems}"
																varStatus="status">
																<tr>
																	<td><c:out value="${status.count}" /></td>
																	<td><c:out value="${announcement.subject}" /></td>
																	<td><c:out
																			value="${announcement.announcementType}" /></td>
																	<td><c:out
																			value="${announcement.announcementSubType}" /></td>
																	<%-- <td><c:out value="${announcement.courseName}" /></td>
																	<td><c:out value="${announcement.programId}" /></td> --%>
																	<td><c:if
																			test="${announcement.announcementType eq 'INSTITUTE' }">
									ALL COURSES
								</c:if> <c:if test="${announcement.announcementType eq 'COURSE' }">
																			${announcement.courseName}
																		</c:if></td>
																	<td><c:if
																			test="${announcement.announcementType eq 'INSTITUTE' }">
									ALL PROGRAMS
								</c:if> <c:if test="${announcement.announcementType eq 'COURSE' }">
																			${announcement.programName}
																		</c:if> <c:if
																			test="${announcement.announcementType eq 'PROGRAM' || announcement.announcementType eq 'TIMETABLE' }">
																			${announcement.programName}
																		</c:if></td>
																	<td><c:out value="${announcement.startDate}" /></td>
																	<td><c:out value="${announcement.endDate}" /></td>

																	<td><c:url value="viewAnnouncement"
																			var="detailsUrl">
																			<c:param name="id" value="${announcement.id}" />
																		</c:url> <c:choose>

																			<c:when
																				test="${announcement.announcementType eq 'PROGRAM' }">
																				<c:url value="addAnnouncementFormMultiProgram"
																					var="editurl">
																					<c:param name="id" value="${announcement.id}" />
																				</c:url>
																			</c:when>
																			<c:otherwise>
																			 <c:choose>
																				  <c:when
																				  test="${announcement.announcementType eq 'TIMETABLE'}">
																				  <c:url value="addTimeTableForm"
																						var="editurl">
																				
																						<c:param name="id" value="${announcement.id}" />
																					</c:url>
																				  </c:when>
																				  <c:otherwise>
																					<c:url value="addAnnouncementForm" var="editurl">
																						<c:param name="id" value="${announcement.id}" />
																					</c:url>
																					</c:otherwise>
																			</c:choose>
																			</c:otherwise>
																		</c:choose> <c:url value="deleteAnnouncement" var="deleteurl">
																			<c:param name="id" value="${announcement.id}" />
																		</c:url> <a href="${detailsUrl}" title="Details"><i
																			class="fa fa-info-circle fa-lg"></i></a>&nbsp; 
																			<c:if test="${fn:containsIgnoreCase(announcement.createdBy, username)}">
																			
																			<c:choose>
																				<c:when test="${announcement.announcementType eq 'TIMETABLE'}">
																				
																				</c:when>
																				<c:otherwise>
																				<a href="${editurl}" title="Edit"><i class="fas fa-edit"></i></a>&nbsp;
																				</c:otherwise>
																			</c:choose>
																			
																				
																				
																			<%-- <a href="${editurl}" title="Edit"><i class="fas fa-edit"></i></a>&nbsp; --%>
																			
																			 <a href="${deleteurl}" title="Delete"
																				onclick="return confirm('Are you sure you want to delete this record?')"><i class="fas fa-trash-alt"></i></a>
																		</c:if>
																		<%-- <c:if test="${announcement.announcementType eq 'TIMETABLE'}">
																	    <a href="downloadStudentsReport?id=${announcement.id}"><i class="fa fa-download"></i></a>
																		</c:if> --%>
																		</td>
																</tr>
															</c:forEach>


														</tbody>
													</table>
												</div>
									</div>
								</div>
							</c:when>
						</c:choose>

			<!-- /page content: END -->



                        </div>

                <!-- SIDEBAR START -->

                <!-- SIDEBAR END -->
        <jsp:include page="../common/newAdminFooter.jsp"/>
        
        
        	<script>	
        	
        	$(function() {
        		
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

        		});
        	
        	
        	
        	$(function() {

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
       
        

	<!-- <script>
		//$(document).ready(function() {
		$("#datetimepicker1").on("dp.change", function(e) {

			validDateTimepicks();
		}).datetimepicker({
			// minDate:new Date(),
			useCurrent : false,
			format : 'YYYY-MM-DD HH:mm:ss'
		});

		$("#datetimepicker2").on("dp.change", function(e) {

			validDateTimepicks();
		}).datetimepicker({
			// minDate:new Date(),
			useCurrent : false,
			format : 'YYYY-MM-DD HH:mm:ss'
		});

		function validDateTimepicks() {
			if (($('#startDate').val() != '' && $('#startDate').val() != null)
					&& ($('#endDate').val() != '' && $('#endDate').val() != null)) {
				var fromDate = $('#startDate').val();
				var toDate = $('#endDate').val();
				var eDate = new Date(fromDate);
				var sDate = new Date(toDate);
				if (sDate < eDate) {
					alert("endate cannot be smaller than startDate");
					$('#startDate').val("");
					$('#endDate').val("");
				}
			}
		}
		//});
	</script>
 -->

