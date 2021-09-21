
<%-- <jsp:include page="../common/header.jsp" /> --%>

<%@page import="com.spts.lms.beans.forum.Forum"%>
<%@page import="com.spts.lms.beans.calender.Calender"%>
<%@page import="java.util.List"%>
<%@ page import="com.spts.lms.web.utils.Utils"%>
<%-- <jsp:include page="../common/NewHeader1.jsp" /> --%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>




<!doctype html>
<html lang="en">

<jsp:include page="../../common/css.jsp" />

<script type="text/javascript">
	var headerCourseId = null;
	function theFunction(obj) {
		return true;
	}
</script>

<body class="nav-md footer_fixed">


	<div class="container body">
	<div class="main_container">

     <jsp:include page="../../common/topHeader.jsp" />
      
      
      
<!-- page content -->
<div class="right_col" role="main">

	<div class="dashboard_container">

		<div class="dashboard_height" id="main">
			<div class="dashboard_container_spacing">
				<div class="breadcrumb">
				<c:out value="${Program_Name}" />
						
						<sec:authorize access="hasRole('ROLE_STUDENT')">
						
						<i
						class="fa fa-angle-right"></i>
											
											<c:out value="${AcadSession}" />
											
											</sec:authorize>
						<br><br>
				
					<a href="${pageContext.request.contextPath}/viewCourse?id=${courseId}">My Courses</a> <i
						class="fa fa-angle-right"></i><c:out value="${courseRecord.courseName}" />
						  



<div class="row">
	<div class="col-xs-12 col-sm-12">
		<div class="x_panel">
			<div class="x_title">
				<h2>Assignments</h2>
				<ul class="nav navbar-right panel_toolbox">
					<li><a href="<c:url value="assignmentList" />"><span>View
								All</span></a></li>
					<li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a></li>
					<li><a class="close-link"><i class="fa fa-close"></i></a></li>
				</ul>
				<div class="clearfix"></div>
			</div>

			<div class="x_itemCount" style="display: none;">
				<div class="image_not_found">
					<i class="fa fa-newspaper-o"></i>
					<p>
						<label class="x_count"></label> Assignments
					</p>
				</div>
			</div>

			<div class="x_content">
				<div class="table-responsive">
				<c:choose>
						<c:when test="${ not empty assignments}">
						
					<table class="table table-hover">
						<thead>
							<tr>

								<th>Name</th>
								<th>End Date</th>
								<th>Points</th>
								<th>Submitted</th>
								<th>Action</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="assignment" items="${assignments}"
								varStatus="status">

								<c:url value="assignmentDetails" var="submiturl">
									<c:param name="id" value="${assignment.id}" />
								</c:url>

								<tr>

									<td><a href="${submiturl}"><c:out
												value="${assignment.assignmentName}" /></a></td>
									<td><c:out value="${assignment.endDate}" /></td>
									<td>30</td>
									<td><c:if test="${assignment.submissionStatus eq 'Y' }">
											<i class="check_ellipse fa fa-check bg-green"></i>
											<c:out value="Submitted" />
										</c:if> <c:if test="${assignment.submissionStatus ne 'Y' }">
											<i class="check_ellipse fa fa-check"></i>
											<c:out value="Not Submitted" />
										</c:if></td>
									<td><sec:authorize access="hasRole('ROLE_STUDENT')">
											<c:if test="${assignment.submissionStatus eq 'Y' }">
												<i class="check_ellipse fa fa-location-arrow"></i>
												<a href="#" title="Assignment Submitted">Submitted</a>&nbsp;
															</c:if>

											<c:if test="${assignment.submissionStatus ne 'Y' }">

												<c:url value="submitAssignmentForm" var="submiturl">
													<c:param name="id" value="${assignment.id}" />
												</c:url>
												<i class="check_ellipse fa fa-location-arrow"></i>

												<a href="${submiturl}" title="Submit Assignment">Submit</a>&nbsp;
														</c:if>

										</sec:authorize> <sec:authorize access="hasRole('ROLE_FACULTY')">
											<c:url value="viewAssignment" var="editAssignmentUrl">
												<c:param name="id" value="${assignment.id}" />
											</c:url>
											<i class="check_ellipse fa fa-location-arrow bg-red"></i>
											<a href="${editAssignmentUrl}" title="Submit Assignment">Edit
												Assignment</a>&nbsp;
												</sec:authorize></td>


								</tr>
							</c:forEach>
							
						</tbody>
					</table>
					</c:when>
						<c:otherwise>
						<div class="image_not_found">
								<i class="fa fa-newspaper-o"></i>
								<p>No Assignment Data </p>
						</div>
						</c:otherwise>
					</c:choose>
					
				</div>
			</div>
		</div>
	</div>
</div>
				</div>
			
			</div>
		</div>
	<%@include file="facultyLearningResource.jsp" %>
	
	</div>
</div>
<!-- /page content -->


<jsp:include page="../../common/footer.jsp" />

   </div>
  </div>
</body>
</html>

