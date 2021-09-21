<%@page import="com.spts.lms.beans.announcement.Announcement"%>
<%@page import="java.util.List"%>
<%@page import="com.spts.lms.beans.calender.Calender"%>
<%@ page import="com.spts.lms.web.utils.Utils"%>



<div class="dashboard_height rightpanel" id="mySidenav1">

	<div class="rightpanel_content">
		<h3>
			To Do <a href="<c:url value='viewEvents' />" > View All</a>
		</h3>
		<%
		List<Calender> toDoList = (List<Calender>)request.getAttribute("toDoList");
		for(Calender c : toDoList) {
			
		String dateInWords = Utils.formatDate("yyyy-mm-dd hh:mm:ss", "MMMMM dd, yyyy", c.getStartDate());
		String time = Utils.formatDate("yyyy-mm-dd hh:mm:ss", "hh:mm:ss a", c.getStartDate());
		
		
		%>

		<ul>
			<li class="x_panel">
				<h4>
					<a href="addCalenderEventForm?id=<%=c.getId()%>"><%=c.getEvent() %></a> <a href="#" class="close-link"><i
						class="fa fa-close"></i></a>
				</h4>
				<p>${todo.description}</p>
				<p>
					<span>19 points</span> - -<%=dateInWords %>
					at
					<%=time %>
				</p>
			</li>

		</ul>

		<%} %>

		<h3>
			Announcements <a href="<c:url value='viewUserAnnouncements' />" >View All</a>
		</h3>
		<%
			List<Announcement> announcmentList = (List<Announcement>)request.getAttribute("announcmentList");
			for(Announcement a : announcmentList) {
				
			String dateInWords = Utils.formatDate("yyyy-mm-dd hh:mm:ss", "MMMMM dd, yyyy", a.getCreatedDate().toString());
			
			
			%>
		<ul>
			<li class="x_panel">
				<h4>
					<a href="viewAnnouncement?id=<%=a.getId()%>" ><%=dateInWords %></a> <a href="#" class="close-link"><i
						class="fa fa-close"></i></a>
				</h4>
				<p><%=a.getSubject() %></p>
				<p><%=a.getDescription() %></p>
			</li>
			
		</ul>
			<%} %>
	</div>
</div>