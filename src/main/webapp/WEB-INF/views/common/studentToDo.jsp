<%@page import="com.spts.lms.beans.announcement.Announcement"%>
<%@page import="com.spts.lms.beans.message.Message"%>
<%@page import="java.util.List"%>
<%@page import="com.spts.lms.beans.calender.Calender"%>
<%@page import="com.spts.lms.beans.pending.*"%>
<%@ page import="com.spts.lms.web.utils.Utils"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>


<div class="dashboard_height rightpanel" id="mySidenav1">

	<div class="rightpanel_content">
		<h3>

			Messages <a href="<c:url value='viewMyMessage' />"> View All</a>

		</h3>

		<%
			List<Message> receivedMessage = (List<Message>) request
					.getAttribute("receivedMessage");

			for (Message m : receivedMessage) {

				String Subject = m.getSubject();

				String Description = m.getDescription();
		%>

		<ul>

			<li class="x_panel">

				<h4>

					<a href="giveResponseToMessage?id=<%=m.getId()%>"><%=m.getFirstname()%>
						<%=m.getLastname()%></a> <a href="#" class="close-link"><i
						class="fa fa-close"></i></a>



				</h4>

				<p><%=Subject%></p>

				<p><%=Description%></p>



			</li>



		</ul>



		<%
			}
		%>


		<h3>
			To Do <a href="${pageContext.request.contextPath}/pendingTask">
				View All</a>
		</h3>

		<%
			List<PendingTask> toDoDaily = (List<PendingTask>) request
					.getAttribute("toDoDaily");
			if (toDoDaily != null && toDoDaily.size() > 0) {
				for (PendingTask c : toDoDaily) {

					String dateInWords = Utils.formatDate("MMMMM dd, yyyy",
							c.getStartDate());
					String time = Utils.formatDate("hh:mm:ss a",
							c.getStartDate());
		%>

		<ul>
			<li class="x_panel">
				<h4>

					<a href="pendingTask"><%=c.getTaskName()%></a> <a href="#"
						class="close-link"><i class="fa fa-close"></i></a>

				</h4>
				<p>${todo.description}</p>
				<p>
					<!-- <span>19 points</span> -->
					<%=dateInWords%>
					at
					<%=time%>
				</p>
			</li>

		</ul>

		<%
			}
			}
		%>
		<%
			List<Calender> toDoList = (List<Calender>) request
					.getAttribute("toDoList");
			if (toDoList != null && toDoList.size() > 0) {
				int count = 0;
				for (Calender c : toDoList) {
					String dateInWords = "";
					String time = "";
					if (count <= 3) {
						dateInWords = Utils.formatDate(
								"yyyy-MM-dd hh:mm:ss", "MMMMM dd, yyyy",
								c.getStartDate());
						time = Utils.formatDate("yyyy-MM-dd hh:mm:ss",
								"hh:mm:ss a", c.getStartDate());
					}
					count++;
		%>

		<ul>
			<li class="x_panel">
				<h4>
					<a href="viewEvents"><%=c.getEvent()%></a> <a href="#"
						class="close-link"><i class="fa fa-close"></i></a>
				</h4>
				<p>${todo.description}</p>
				<p>
					<!-- <span>19 points</span> -->
					<%=dateInWords%>
					at
					<%=time%>
				</p>
			</li>

		</ul>

		<%
			}
			}
		%>



		<h3>
			New Announcements <a
				href="${pageContext.request.contextPath}/viewUserAnnouncements">View
				All</a>
		</h3>
		<%
			List<Announcement> announcmentList = (List<Announcement>) session
					.getAttribute("announcmentList");
			if (announcmentList != null && announcmentList.size() > 0) {
				for (Announcement a : announcmentList) {

					String dateInWords = Utils.formatDate(
							"yyyy-MM-dd hh:mm:ss", "MMMMM dd, yyyy", a
									.getCreatedDate().toString());
		%>
		<ul>
			<li class="x_panel">
				<h4>
					<a href="viewAnnouncement?id=<%=a.getId()%>"><%=dateInWords%></a> <a
						href="#" class="close-link"><i class="fa fa-close"></i></a>
				</h4>
				<p><%=a.getSubject()%></p> <%-- <p><%=a.getDescription()%></p> --%>
			</li>

		</ul>
		<%
			}
			}
		%>
	</div>
</div>