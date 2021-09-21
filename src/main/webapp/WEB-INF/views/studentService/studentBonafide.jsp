<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>


<!doctype html>
<html lang="en">

<jsp:include page="../common/headerCss.jsp" />
<style>
.fontSize{
font-size: 15px;
padding-left: 40px;
}
</style>
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
								<form:form action="sendStudentBonafideData" modelAttribute="bonafideForm">
									<h4 align="center">APPLICATION FOR GETTING BONAFIDE CERTIFICATE</h4>
									<p class="fontSize" align="right">Date:<form:input type="text" path="createdDate" value="${currentDate}" readonly="true"/></p>  
						<p class="fontSize">To,<br> 
						The Dean,<br> 
						<!-- School of Business Management<br> -->
						SVKM's NMIMS University<br>
						Mumbai 
						</p><br><br>
							<p class="fontSize" style="padding-left: 170px">Sub: Regarding Bonafide Certificate. </p>
							<br><br>
							<div class="fontSize">
							<p>Sir / Madam,<br>
I am student of your Institute and studying in <input type="text" value="" /> class for the academic year <form:input type="text" path="acadYear" value="${student.acadYear}" readonly="true"/>. 
							</p><br>
							<p>My personal details are as follows - </p><br>
							<ol>
							<li>	SAP ID. <form:input type="text" path="username" value="${student.username}" readonly="true"/>
							Roll No. <form:input type="text" path="rollNo" value="${student.rollNo}" readonly="true"/></li><br>
							
							  <li>	Name in full : <form:input type="text" path="lastname" value="${student.lastname}" readonly="true"/>
							  		<form:input type="text" path="firstname" value="${student.firstname}" readonly="true"/>
							  		<form:input type="text" path="fatherName" value="${student.fatherName}" readonly="true"/></li><br> 
								<li>Class: <form:input type="text" path="studyClass" value="" required="required"/>  Div: <form:input type="text" path="division" value="" required="required"/> 
								Program <form:input size="${fn:length(student.programName)+1}" type="text" path="programName" value="${student.programName}" readonly="true"/></li><br>
								<li>Reason for Requirement of certificate : <form:input type="text" path="reason" value="" style="width: 50%;" required="required"/> </li><br>
							<p>Please issue me a bonafide certificate as early as possible. Thanking You, </p>
							</ol>
							</div><br><br>
							<div class="fontSize">
							<p >Yours sincerely,</p>
							<input type="text" value="${student.firstname} ${student.lastname}" readonly="readonly"/>
							</div>
							<br>
							<div class="col-sm-5">
													<div class="form-group">
													</div></div>
													<div class="col-sm-4">
													<div class="form-group">
							<button id="cancel" name="cancel" formaction="viewServices" class="btn btn-success" formnovalidate="formnovalidate">Cancel</button>
							<!-- send service id from here -->
							<form:input path="serviceId" value="${serviceId}" type="hidden"/>
							<button id="submit" name="submit" formaction="sendStudentBonafideData" class="btn btn-success">Submit</button>
							</div></div>
							</form:form>
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
