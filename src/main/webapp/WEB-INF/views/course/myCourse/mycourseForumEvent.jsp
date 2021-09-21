<%@page import="java.util.HashMap"%>
<%@page import="com.spts.lms.beans.forum.Forum"%>
<%@page import="java.util.List"%>
<%@ page import="com.spts.lms.web.utils.Utils"%>
<%@page import="com.spts.lms.beans.calender.Calender"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
	List<Calender> listOfEvents = (List<Calender>) request
			.getAttribute("toDoList");
	List<Forum> listOfForum = (List<Forum>) request
			.getAttribute("allForums");
	HashMap<Long, String> mapOfForumIdAndMostRecentActivityDate = (HashMap<Long, String>) request
			.getAttribute("mapOfForumIdAndMostRecentActivityDate");
%>




<!-- Input Form Panel -->
<div class="card bg-white border">
	<div class="card-body">
				<div class="text-center">
					<h5>Discussion Forums</h5>
				<%-- 	<ul class="nav navbar-right panel_toolbox">
						<li><a href="<c:url value="viewForum?courseId=${courseId}"/>"><span>View
									All</span></a></li>
						<li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a></li>
						<li><a class="close-link"><i class="fa fa-close"></i></a></li>
					</ul>
					<div class="clearfix"></div> --%>
				</div>
				<div class="x_itemCount" style="display: none;">
					<div class="image_not_found">
						<i class="fab fa-twitch"></i>
						<p>
							<label class="x_count"></label> Discussion Forums
						</p>
					</div>
				</div>
				<c:choose>
					<c:when test="${not empty allForums}">
						<div class="x_content courses_event_height">
							<div class="event_content">

								<%
									for (Forum f : listOfForum) {

												String createdDate = f.getCreatedDate().toString();
												System.out.println("Created Date "
														+ f.getCreatedDate().toString());
												String[] dateParts = createdDate.split(" ");
												int daysAgo = Utils.calculateDaysAgo(dateParts[0]);

												String modifiedDate = f.getCreatedDate().toString();
												String[] dateParts2 = createdDate.split(" ");

												Integer replyCount = f.getReplyCount();
												System.out.println("replyCount " + replyCount);
								%>
								<ul>
									<c:url value="/replyToQuestionForm" var="viewForumUrl">
										<c:param name="id" value="<%=String.valueOf(f.getId())%>" />
									</c:url>
									<li class="x_panel">
										<h4>
											<a class="text-dark" href="${viewForumUrl}"><c:out
													value="<%=f.getTopic()%>" /></a>
										</h4>
										<p>
											Posted <span><c:out value="<%=daysAgo%>" /> Days ago</span>
											by <span><c:out value="<%=f.getCreatedBy()%>" /></span>
										</p>
										<p>

											<span> <c:out value="<%=replyCount%>" /> Replies
											</span> | Last activity: <span> <!-- 2016-09-09</span> at <span>17:16:55.0 -->
												<%=mapOfForumIdAndMostRecentActivityDate.get(f
								.getId())%></span>
										</p>
									</li>
								</ul>

								<%
									}
								%>

							</div>
						</div>
					</c:when>
					<c:otherwise>
						<div class="text-center mt-3">
							<div class="image_not_found">
								<i class="fab fa-twitch "></i>
								<p>No Discussion Data</p>
							</div>
						</div>
					</c:otherwise>
				</c:choose>
	</div>
</div>



<div class="card bg-white border">
	<div class="card-body">
				<div class="text-center">
					<h5>Events</h5>
					<%-- <ul class="nav navbar-right panel_toolbox">
						<li><a href="<c:url value="viewEvents" />"><span>View
									All</span></a></li>
						<li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a></li>
						<li><a class="close-link"><i class="fa fa-close"></i></a></li>
					</ul>
					<div class="clearfix"></div> --%>
				</div>
				<div class="x_itemCount" style="display: none;">
					<div class="image_not_found">
						<i class="fa fa-calendar"></i>
						<p>
							<label class="x_count"></label> Events
						</p>
					</div>
				</div>
				<c:choose>
					<c:when test="${not empty toDoList}">

						<div class="x_content courses_event_height">
							<div class="table-responsive">
								<div class="event_content">


									<%
										for (Calender c : listOfEvents) {
													String dateInWords = Utils.formatDate(
															"yyyy-MM-dd hh:mm:ss", "MMMMM dd, yyyy",
															c.getStartDate());
									%>
									<ul>
										<li class="x_panel">

											<h4>
												<a class="text-dark" href="<c:url value="viewEvents"/>"><c:out
														value="<%=dateInWords%>" /> </a>
											</h4>
											<p>
												<c:out value="<%=c.getDescription()%>" />

											</p>
										</li>
									</ul>

									<%
										}
									%>


								</div>
							</div>
						</div>
					</c:when>
					<c:otherwise>
						<div class="text-center mt-3">
							<div class="image_not_found">
								<i class="fa fa-calendar"></i>
								<p>No Event Data</p>
							</div>
						</div>
					</c:otherwise>
				</c:choose>


	</div>
</div>
