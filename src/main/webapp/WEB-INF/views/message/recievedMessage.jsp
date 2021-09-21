<%@page import="com.spts.lms.web.utils.Utils"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@page import="java.util.*"%>



<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<jsp:include page="../common/newDashboardHeader.jsp" />

<div class="d-flex dataTableBottom" id="facultyAssignmentPage">
	<jsp:include page="../common/newLeftNavBarFaculty.jsp" />
	<jsp:include page="../common/newLeftSidebarFaculty.jsp" />
	<jsp:include page="../common/rightSidebarFaculty.jsp" />

	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper">

		<jsp:include page="../common/newTopHeaderFaculty.jsp" />

		<div class="container mt-5">
			<div class="row">
				<!-- SEMESTER CONTENT -->
				<div
					class="col-lg-9 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto">

					<!-- page content: START -->

					<nav aria-label="breadcrumb">
						<ol class="breadcrumb">
							<li class="breadcrumb-item"><a
								href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
							<li class="breadcrumb-item" aria-current="page"><c:out
									value="${Program_Name}" /></li>
							<sec:authorize access="hasRole('ROLE_STUDENT')">
								<c:out value="${AcadSession}" />
							</sec:authorize>
							<li class="breadcrumb-item active" aria-current="page">Add
								Subjective Test Question</li>
						</ol>
					</nav>

					<jsp:include page="../common/alert.jsp" />


					<!-- Input Form Panel -->
					<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h5>Messages Received</h5>
										
									</div>
									<c:forEach var="message" items="${listOfAllMessages}"
										varStatus="status">
										<button type="button" class="btn btn-danger"
											style="width: 12%" data-target="#demo${status.count}">${message.createdBy}
										</button>
										<button type="button" class="btn btn-info" style="width: 40%"
											data-toggle="collapse" data-target="#demo${status.count}">${message.subject}
										</button>
										<button type="button" class="btn btn-primary"
											style="width: 12%" data-target="#demo${status.count}">${message.description}
										</button>

										<textarea id="demo${status.count}" class="collapse"
											style="width: 60%; margin-left: 20%" class="form-control">${message.subject}</textarea>
										<textarea id="demo${status.count}" class="collapse"
											style="width: 20%" class="form-control">${message.description}</textarea>
									</c:forEach>

								</div>
							</div>
							<div class="col-sm-4 column">
								<div class="form-group">

									<a href="${pageContext.request.contextPath}/homepage"><button
											id="cancel" class="btn btn-large btn-danger"
											style="margin-left: 150%">Back</button></a>
								</div>
							</div>


						</div>
					</div>

					<!-- Results Panel -->



					<%-- 	<jsp:include page="../common/paginate.jsp">
							<jsp:param name="baseUrl" value="searchFacultyGroups" />
						</jsp:include> --%>

					<!-- /page content: END -->

				</div>

				<!-- SIDEBAR START -->
				<jsp:include page="../common/newSidebar.jsp" />
				<!-- SIDEBAR END -->
				<jsp:include page="../common/footer.jsp" />