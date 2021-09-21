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
      List<DashBoard> courseDetailList = (List<DashBoard>) session
      .getAttribute("courseDetailList");
      System.out.println("SIZE :"+courseDetailList.size());
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

 
                    <div class="col-12" role="main">
    <div id="main">
            <div class="alert alert-warning alert-dismissible" role="alert">
                <button type="button" class="close" data-dismiss="alert" aria-label="Close"> <span aria-hidden="true">&times;</span> </button>
                <h4> <strong>Please provide your feedback about our new portal <a href="<c:url value=" portalFeedbackForm"/>">here... </a> </strong> </h4> 
                </div>
            <div class="alert alert-success alert-dismissible" role="alert">
                <button type="button" class="close" data-dismiss="alert" aria-label="Close"> <span aria-hidden="true">&times;</span> </button>
                <h4> <b> <c:out value="${subjectFaculty}"></c:out> </b> <marquee behavior="scroll" direction="left" onmouseover="this.stop();" onmouseout="this.start();">${notificationFaculty}</marquee> <strong></strong> </h4> 
                </div>
            <% int divCounter=0; for(String s : dashboardListSemesterWise.keySet()){divCounter++; int count=0; %>
                <input type="hidden" id="mapSize" value="<%=dashboardListSemesterWise.size()%>"> <a> <div id="flip<%=divCounter%>" class="flipClass" style="padding: 5px; background-color: #d9d9d9; border: solid 1px #c3c3c3;"> <center> <h2> <b> <font color="#001a33" width="30%"><%=s%></font> </b> </h2> </center> <div class="clearfix"></div></div></a>
                <div class="container-fluid dashboardcon" style="padding: 50px; display: none;" id="dashboardData<%=divCounter%>">
                    <% List<DashBoard> courseDetailListBySession=dashboardListSemesterWise.get(s); System.out.println("courseDetailListBySession"+ courseDetailListBySession); for (DashBoard d : courseDetailListBySession){count++; %>
                        <div class="col-md-4 course_widget_height">
                            <div class="dashboardcon_title" id="courseDetail<%=count%>">
                                <div>
                                    <c:url value="/viewCourse" var="viewCourseUrl">
                                        <c:param name="id" value="<%=String.valueOf(d.getCourse().getId())%>" /> </c:url>
                                    <c:url value="assignmentList" var="viewAssignmentUrl">
                                        <c:param name="courseId" value="<%=String.valueOf(d.getCourse().getId())%>" /> </c:url>
                                    <c:url value="viewUserAnnouncements" var="viewAnnouncementUrl">
                                        <c:param name="courseId" value="<%=String.valueOf(d.getCourse().getId())%>" /> </c:url>
                                    <c:url value="viewForum" var="viewForumUrl">
                                        <c:param name="courseId" value="<%=String.valueOf(d.getCourse().getId())%>" /> </c:url>
                                    <c:url value="testList" var="viewTestUrl">
                                        <c:param name="courseId" value="<%=String.valueOf(d.getCourse().getId())%>" /> </c:url>
                                    <c:url value="getContentUnderAPathForFaculty" var="viewContentUrl">
                                        <c:param name="courseId" value="<%=String.valueOf(d.getCourse().getId())%>" /> </c:url> <span><a href="${viewCourseUrl}"/><%=d.getCourse().getCourseName()%> - <%=d.getCourse().getAcadYear()%></a></span>
                                    <!-- <i><a href="#" class="hide_course_submenu" data-toggle="dropdown" aria-expanded="false"><span></span><span></span><span></span></a></i> --></div>
                                <div class="show_course_submenu" style="display: none;">
                                    <div class="arrow-left"></div>
                                    <ul>
                                        <li><a href="<c:url value=" ${viewAnnouncementUrl} "/>"><i class="fa fa-bullhorn"></i> Announcements</a></li>
                                        <li><a href="<c:url value=" ${viewAssignmentUrl} "/>"><i class="fa fa-newspaper-o"></i> Assignments</a></li>
                                        <li><a href="<c:url value=" ${viewTestUrl} "/>"><i class="fa fa-file-text"></i> Test</a></li>
                                        <li><a href="<c:url value=" ${viewForumUrl} "/>"><i class="fa fa-twitch "></i> Discussions</a></li>
                                        <li><a href="<c:url value=" ${viewContentUrl} "/>"><i class="fa fa fa-folder-open"></i> Content</a></li>
                                    </ul>
                                </div>
                            </div>
                            <ul>
                                <li>Announcements <span><%=d.getPendingAssigmentCount()%></span></li>
                                <li>Assignments <span><%=d.getPendingAssigmentCount()%></span></li>
                                <li>Tests <span><%=d.getPendingTestCount()%></span></li>
                            </ul>
                        </div>
                        <%}%>
                </div>
                <%}%>
    </div>
</div>
                    
                    </div>
                  
                  <!-- SIDEBAR START -->
               <jsp:include page="../common/newSidebar.jsp" />
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


