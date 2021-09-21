<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>


<!doctype html>
<html lang="en">

<jsp:include page="../common/headerCss.jsp" />

<body class="nav-md ">


	<div class="container body">
		<div class="main_container">


			<jsp:include page="../common/leftSidebar.jsp" />
			<jsp:include page="../common/topHeader.jsp" />





			<!-- page content -->
			<div class="right_col" role="main">

				<div class="dashboard_container">



					<div class="dashboard_container_spacing">
						<div class="breadcrumb">

							<%-- <c:out value="${Program_Name}" /> --%>

							<sec:authorize access="hasRole('ROLE_STUDENT')">

								<!-- <i class="fa fa-angle-right"></i> -->

								<%-- <c:out value="${AcadSession}" /> --%>

							</sec:authorize>
							<br>
							<br> <a href="${pageContext.request.contextPath}/homepage">Dashboard</a>
							<i class="fa fa-angle-right"></i> Student Bonafide
						</div>

						<jsp:include page="../common/alert.jsp" />

						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">
									<div class="x_title">
										<h2>Service List</h2>
										<div class="clearfix"></div>
									</div>
									<div class="x_content">
										<h2>Form Submitted Successfully!!!</h2>
										<h3>your application id is <c:out value="${formId}"/> </h3>
									<form:form >	
								<button id="cancel" name="cancel" formaction="viewBFServices" class="btn btn-success" formnovalidate="formnovalidate">OK</button>
									</form:form>
									</div>
								</div>
							</div>
						</div>

					</div>
					<%-- <jsp:include page="../common/paginate.jsp">
						<jsp:param name="baseUrl" value="viewFeedbackDetails" />
					</jsp:include> --%>
				</div>


				<%-- <jsp:include page="../common/studentToDo.jsp" /> --%>
			</div>


			<!-- /page content -->

			<jsp:include page="../common/DashboardFooter.jsp" />

		</div>
	</div>

</body>
</html>
