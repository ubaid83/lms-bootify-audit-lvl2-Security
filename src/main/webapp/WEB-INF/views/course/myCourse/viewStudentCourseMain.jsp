<%@page import="com.spts.lms.web.utils.Utils"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@page import="java.util.*"%>

<%@page import="com.spts.lms.beans.forum.Forum"%>
<%@page import="com.spts.lms.beans.calender.Calender"%>
<%@page import="java.util.List"%>


<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<jsp:include page="../../common/newDashboardHeader.jsp" /> 

<div class="d-flex dataTableBottom" id="facultyAssignmentPage">
<jsp:include page="../../common/newLeftNavBarFaculty.jsp"/>
<jsp:include page="../../common/newLeftSidebarFaculty.jsp"/>
<jsp:include page="../../common/rightSidebarFaculty.jsp"/>

<!-- DASHBOARD BODY STARTS HERE -->
<script type="text/javascript">
	var headerCourseId = null;
	function theFunction(obj) {
		return true;
	}
</script>
<div class="container-fluid m-0 p-0 dashboardWraper">

	<jsp:include page="../../common/newTopHeaderFaculty.jsp" />
     
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
                                <li class="breadcrumb-item active" aria-current="page"> My Courses </li>
                            </ol>
                        </nav>
                        
						<jsp:include page="../../common/alert.jsp" />
						
						
						<!-- Input Form Panel -->
						<div class="card bg-white border">
						<div class="card-body">
				<%@include file="mycourseAssignments.jsp" %>
				<%@include file="mycourseTest.jsp" %>
				
				<%-- <%@include file="mycourseForumEvent.jsp" %> --%>
				<%@include file="mycourseLearningResources.jsp" %>
						</div>
						</div>

				<%-- 	<%@include file="../../common/studentToDo.jsp"%>  --%>

					<%-- 	<jsp:include page="../common/paginate.jsp">
							<jsp:param name="baseUrl" value="searchFacultyGroups" />
						</jsp:include> --%>

			<!-- /page content: END -->
                   
                    </div>
			
			<!-- SIDEBAR START -->
	         <jsp:include page="../../common/newSidebar.jsp" />
	        <!-- SIDEBAR END -->
	<jsp:include page="../../common/footer.jsp"/>
