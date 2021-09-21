<%@page import="com.spts.lms.beans.announcement.Announcement"%>
<%@ page import="com.spts.lms.web.utils.Utils"%>
<%@page import="java.util.*"%>
<%@page import="com.spts.lms.beans.dashboard.DashBoard"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>


<%
	List<DashBoard> courseDetailList = (List<DashBoard>) session
.getAttribute("courseDetailList");

Map<String,List<DashBoard>> dashboardListSemesterWise = (Map<String,List<DashBoard>>)
session.getAttribute("sessionWiseCourseListMap");
%>

<jsp:include page="../common/newDashboardHeader.jsp" /> 

<script type="text/javascript">
	var headerCourseId = null;
	function theFunction(obj) {

		return true;
	}
</script>
<div class="d-flex" id="dashboardPage">
<%-- <jsp:include page="../common/newLeftNavbar.jsp"/> --%>
<%-- <sec:authorize access="hasRole('ROLE_STUDENT')">
<jsp:include page="../common/newLeftSidebar.jsp"/>
</sec:authorize> --%>
<%-- <sec:authorize access="hasRole('ROLE_PARENT')">
<jsp:include page="../common/newLeftSidebarParent.jsp"/>
</sec:authorize> --%>
<jsp:include page="../common/rightSidebar.jsp"/>

<!-- DASHBOARD BODY STARTS HERE -->

<div class="container-fluid m-0 p-0 dashboardWraper">

	<%-- <jsp:include page="../common/newTopHeader.jsp" /> --%>
	<jsp:include page="../common/failStudentTopHeader.jsp" />
	
     
     <div class="container mt-5">

                <div class="row" style="padding-top:50px;">
                
                <%-- <%
							List<Announcement> announcmentList = (List<Announcement>) session
									.getAttribute("announcmentList");

							if (announcmentList.size() > 0) {
								int count = 1;

								for (Announcement a : announcmentList) {
									
										String dateInWords = Utils.formatDate(
												"yyyy-mm-dd hh:mm:ss", "MMMMM dd, yyyy", a
														.getCreatedDate().toString());
						%>
						<% if (a.getAnnouncementType().equals("TIMETABLE")){%>
                <div class="col-12">
                          <sec:authorize access="hasRole('ROLE_STUDENT')">
                          
     <marquee class="w-100 p-3 timeTableMarquee font-weight-bold">Time table has arrived </marquee>  
        
     </sec:authorize>
                </div>
                <% break; }
                	count++;
                		} 
								}%> --%>
            <!-- SEMESTER CONTENT -->    
                    <div class="col-lg-9 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto">
                    

                    <!-- <a href="#">
                        <div class="alert alert-blue alert-dismissible fade show" role="alert">
                            <strong>Welcome <span id="feedUser">Kapil!</span></strong> Click me to share your feedback about the new interface?
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                    </a>  -->
						<%-- <sec:authorize access="hasRole('ROLE_STUDENT')">
                        <jsp:include page="../common/dashboardSemester.jsp"></jsp:include>
                        </sec:authorize>
                        <sec:authorize access="hasRole('ROLE_PARENT')">
                        <jsp:include page="../homepage/dashboardSemesterParent.jsp"></jsp:include>
                        </sec:authorize> --%>
                    </div>
			
			<!-- SIDEBAR START -->
	        <%--  <jsp:include page="../common/newSidebar.jsp" /> --%>
	        <!-- SIDEBAR END -->
	<jsp:include page="../common/footer.jsp"/>

<script>
	$(document).ready(
			function() {
				var cars = [ "bgcolor1", "bgcolor2", "bgcolor3", "bgcolor4",
						"bgcolor5", "bgcolor6", "bgcolor7", "bgcolor8",
						"bgcolor9" ];
				var count = 0;
				$('[id^=courseDetail]').each(function() {
					if (count == cars.length - 1) {
						count = 0;
					}
					$(this).addClass(cars[count]);
					count++;
				})

				$('body').addClass("dashboard_left");

				var mapValue = $('#mapSize').val();
				console.log('map value--->' + mapValue);

				$('[id^=flip]').each(function() {
					var flipId = $(this).attr('id');

					$('#' + flipId).click(function() {
						console.log('flip entered');

						var counter = flipId.split("flip");
						var array = JSON.parse("[1" + counter + "]");

						console.log('count--->' + array[1]);
						$("#dashboardData" + array[1]).slideToggle("slow");
					});
				});
			});
</script>

<script type="text/javascript">
						$("#result")
								.click(
										function() {
											console
													.log("calling ajax.111");
											//$(this).css('color', 'black');
											var username = $(this).attr("username");
											var announcementId =$(this).attr("announcement_id");
											console.log('úsername'+username+'id'+announcementId);
	
											$
													.ajax({
														type : 'GET',
														url : '${pageContext.request.contextPath}/timetableResult?'
																+'username='+username & 'announcement_id=' + announcement_id +'',
														success : function(data) {

															 $('#result').html(data.notification);
															   if(data.unseen_notification > 0)
															   {
															    $('#count').html(data.unseen_notification);
															   }

														}

													});

										});
					</script>
					

