<%@page import="com.spts.lms.beans.announcement.Announcement"%>
<%@page import="com.spts.lms.beans.message.Message"%>
<%@page import="java.util.List"%>
<%@page import="com.spts.lms.beans.calender.Calender"%>
<%@page import="com.spts.lms.beans.pending.*"%>
<%@ page import="com.spts.lms.web.utils.Utils"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%
if(session.getAttribute("studentFeedbackActive")!= "Y"){
%>
<div class="sidebar col-lg-3 col-md-3 col-sm-12 text-center pt-0 mb-5">
	<div class="col-12 sideWrap">
		<!-- Start TO DO LIST -->
		<div class="card sidebar1">
			<div class="card-header">
				<h5 class="mb-0">
					<button class="btn btn-link w-100 d-flex" data-toggle="collapse"
						data-target="#toToList" aria-expanded="true"
						aria-controls="collapsesemCurrent">
						<h6 class="text-uppercase mr-auto">to do list</h6>
						<i class="fas fa-angle-double-up lh-30"></i>
					</button>
				</h5>
			</div>
			<%-- <%
			List<Announcement> announcmentList = (List<Announcement>)  session.getAttribute("announcmentList");
			if (announcmentList != null && announcmentList.size() > 0) {
				for (Announcement a : announcmentList) {
					System.out.println("AnnouncementList" + a);
					String dateInWords = Utils.formatDate(
							"yyyy-MM-dd hh:mm:ss", "MMMMM dd, yyyy", a
									.getCreatedDate().toString());
		%> 
		<ul>
			<li class="x_panel">
				<h4>
					
				</h4>
				<p><%=a.getSubject()%></p>
				<%-- <p><%=a.getDescription()%></p> --%>
			<!-- </li>

		</ul> -->

			<div id="toToList" class="collapse show"
				aria-labelledby="semesterTwo">
				<div class="card-body">
					<div class="list-group list-group-flush">
					
					<!-- Timetable todo list -->
					 <c:forEach var="tt" varStatus="status" items="${timetableToDoList}">
					 	<c:if test="${tt.isAttendanceMarked eq 'N'}">
					 		<a href="markAttendanceForm" class="list-group-item list-group-item-action">${tt.start_time} To ${tt.end_time}</a>
				 		</c:if>
					 </c:forEach>
					<!-- Timetable todo list -->
						<%
							List<PendingTask> toDoDaily = (List<PendingTask>) session
									.getAttribute("toDoDaily");

							if (toDoDaily != null && toDoDaily.size() > 0) {

								int count = 1;
								for (PendingTask c : toDoDaily) {

									if (count <= 3) {
										String dateInWords = Utils.formatDate("MMMMM dd, yyyy",
												c.getStartDate());
										String time = Utils.formatDate("hh:mm:ss a",
												c.getStartDate());
						%>


						<a href="pendingTask"
							class="list-group-item list-group-item-action"> <%=c.getTaskName()%>-${todo.description}-<%=dateInWords%>


							at <%=time%>
						</a>

						<%
							count++;
									}
						%>

						<%
							}
								if (toDoDaily.size() > 3) {
						%>
						<a href="pendingTask"
							class="list-group-item list-group-item-action"> View All</a>
						<%
							}
						%>
						<%
							} else {
						%>

						<c:if test="${icaListDashboard.size()>0}">
							<a href="searchIcaList" class="list-group-item list-group-item-action"> ICA Template Created
							For You!!! </a>
						</c:if>
						
						<c:if test="${icaListDashboard.size()==0 && timetableToDoList.size() == 0}">
						<a href="#" class="list-group-item list-group-item-action"> No
							To Do Items... </a>
						</c:if>
						<%
							}
						%>
					</div>
					
				</div>
			</div>
		</div>
		<!-- End TO DO LIST-->

		<!-- Start Announcement -->
		<div class="card sidebar2">
			<div class="card-header">
				<h5 class="mb-0">
					<button class="btn btn-link w-100 d-flex" data-toggle="collapse"
						data-target="#sideAnnouncement" aria-expanded="true"
						aria-controls="collapsesemCurrent">
						<h6 class="text-uppercase mr-auto word-wrap-all">announcements</h6>
						<p class="ml-3"></p>
						<i class="fas fa-angle-double-up lh-30"></i>
					</button>
				</h5>
			</div>

			<div id="sideAnnouncement" class="collapse show"
				aria-labelledby="semesterTwo">
				<div class="card-body">
					<div class="list-group list-group-flush">
						<a href="#" class="list-group-item list-group-item-action"></a>
						<!-- <a href="#" class="list-group-item list-group-item-action">
                                                Some example to do list 111...
                                            </a>
                                            <a href="#" class="list-group-item list-group-item-action">
                                                Some example to do list...
                                            </a>
                                            <a href="#" class="list-group-item list-group-item-action">
                                                View All
                                            </a> -->

						<%
							List<Announcement> announcmentList = (List<Announcement>) session
									.getAttribute("announcmentList");

							if (announcmentList.size() > 0) {
								int count = 1;

								for (Announcement a : announcmentList) {
									if (count <= 3) {
										String dateInWords = Utils.formatDate(
												"yyyy-mm-dd hh:mm:ss", "MMMMM dd, yyyy", a
														.getCreatedDate().toString());
						%>
						<%-- <a class="list-group-item list-group-item-action"
							href="viewAnnouncement?id=<%=a.getId()%>"><%=a.getSubject()%></a> --%>
							
							<!-- //Nafeesa -->
							
						 <% if (a.getAnnouncementType().equals("TIMETABLE")){%>
						<a class="list-group-item list-group-item-action blink"
							data-toggle="modal" id="result"
							data-target="#modalAnnounceDash<%=a.getId()%>" href="#" onclick="insertStudent('<%=a.getId()%>');"><%=a.getSubject()%></a>
						<%}	else { %>	
						<a class="list-group-item list-group-item-action"
							data-toggle="modal"
							data-target="#modalAnnounceDash<%=a.getId()%>" href="#"><%=a.getSubject()%></a>
						<%} %>
						<div id="modalAnnouncement position-fixed">
							<div class="modal fade fnt-13"
								id="modalAnnounceDash<%=a.getId()%>" tabindex="-1" role="dialog"
								aria-labelledby="submitAssignmentTitle" aria-hidden="true">
								<div
									class="modal-dialog modal-dialog-centered modal-dialog-scrollable"
									role="document">
									<div class="modal-content">
										<div class="modal-header">
											<h6 class="modal-title"><%=a.getSubject()%></h6>
											<button type="button" class="close" data-dismiss="modal"
												aria-label="Close">
												<span aria-hidden="true">&times;</span>
											</button>
										</div>
										<div class="modal-body">
											<div class="d-flex font-weight-bold">
												<p class="mr-auto">
													Start Date: <span><%=a.getStartDate()%></span>
												</p>
												<p>
													End Date: <span><%=a.getEndDate()%></span>
												</p>
											</div>
											<%
												if (a.getFilePath() != null) {
											%>
											<p class="font-weight-bold text-dark">
												Attachment: <a class="text-primary" target="_blank" href="sendAnnouncementFile?id=<%=a.getId()%>">Download</a>
											</p>
											<%
												}
											%>

											<p class="announcementDetail"><%=a.getDescription()%></p>
										</div>
										<div class="modal-footer">
											<button type="button" class="btn btn-modalClose"
												data-dismiss="modal">Close</button>
										</div>
									</div>
								</div>
							</div>
							
						</div>

						<%
							count++;
									}
								}
								if (announcmentList.size() > 3) {
						%>
						<a href="viewUserAnnouncementsSearchNew"
							class="list-group-item list-group-item-action"> View All</a>
						<%
							}
							}

							else {
						%>
						<a href="#" class="list-group-item list-group-item-action"> No
							Announcements</a>
						<%
							}
						%>
					</div>
					
				</div>
			</div>
		</div>
		<!-- End Announcement-->

		<!-- Start Events -->
		<div class="card sidebar3">
			<div class="card-header">
				<h5 class="mb-0">
					<button class="btn btn-link w-100 d-flex" data-toggle="collapse"
						data-target="#sideEvents" aria-expanded="true"
						aria-controls="collapsesemCurrent">
						<h6 class="text-uppercase mr-auto">events</h6>
						<i class="fas fa-angle-double-up lh-30"></i>
					</button>
				</h5>
			</div>

			<div id="sideEvents" class="collapse show"
				aria-labelledby="semesterTwo">
				<div class="card-body">
					<div class="list-group list-group-flush">
						<a href="#" class="list-group-item list-group-item-action"></a>


						<%
							List<Calender> toDoList = (List<Calender>) session
									.getAttribute("toDoList");
							if (toDoList != null && toDoList.size() > 0) {

								for (Calender c : toDoList) {

									String dateInWords = Utils.formatDate(
											"yyyy-MM-dd hh:mm:ss", "MMMMM dd, yyyy",
											c.getStartDate());
									String time = Utils.formatDate("yyyy-MM-dd hh:mm:ss",
											"hh:mm:ss a", c.getStartDate());
						%>

						<a href="#" class="list-group-item list-group-item-action"> <%=c.getEvent()%>-
							<%=dateInWords%> at <%=time%>
						</a>
						<%
							}
							} else {
						%>
						<a href="#" class="list-group-item list-group-item-action"> No
							Events</a>
						<%
							}
						%>
					</div>
				</div>
			</div>
		</div>
		<!-- End Events-->
			<!-- Download Manual Start -->
		<div class="card sidebar3">
			<div class="card-header">
				<h5 class="mb-0">
					<button class="btn btn-link w-100 d-flex" data-toggle="collapse"
						data-target="#sideDownloadManual" aria-expanded="true"
						aria-controls="collapsesemCurrent">
						<h6 class="text-uppercase mr-auto">Download Manual</h6>
						<i class="fas fa-angle-double-up lh-30"></i>
					</button>
				</h5>
			</div>

			<div id="sideDownloadManual" class="collapse hide">
				<div class="card-body">
					<div class="list-group list-group-flush">
						
						<sec:authorize access="hasRole('ROLE_STUDENT')">
						<a href="downloadPortalManuals" class="list-group-item list-group-item-action">
						Student Manual  <i class="fa fa-download" aria-hidden="true"></i>
						</a>
						</sec:authorize>
						<sec:authorize access="hasRole('ROLE_FACULTY')">
						<a href="downloadPortalManuals" class="list-group-item list-group-item-action">
						Faculty Manual  <i class="fa fa-download" aria-hidden="true"></i>
						</a>
						</sec:authorize>

					</div>
				</div>
			</div>
		</div>
		<!-- Download Manual End-->
		
		
	</div>
</div>
<%
}
%>

<!-- RIGHT SIDEBAR END -->
</div>
</div>
<script>
function insertStudent(announcementId){
	
	
	$
	.ajax({
		type : 'GET',
		url : '${pageContext.request.contextPath}/timetableResult?'
				+ 'announcementId=' + announcementId ,
		success : function(data) {

			 $('#result').html(data.notification);
			   if(data.unseen_notification > 0)
			   {
			    $('#count').html(data.unseen_notification);
			   }

		}

	});
}


</script>