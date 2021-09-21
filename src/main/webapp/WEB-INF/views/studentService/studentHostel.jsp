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
.fontSize {
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
							<br> <br> <a
								href="${pageContext.request.contextPath}/homepage">Dashboard</a>
							<i class="fa fa-angle-right"></i> Student Hostel
						</div>

						<jsp:include page="../common/alert.jsp" />

						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">
									<form:form action="sendStudentHostelData"
										modelAttribute="hostelForm">
										<h4 align="center">HOSTEL APPLICATION FORM</h4>
										<p align="center">(To be filled in by the applicant in
											his/her own handwriting clearly and carefully)</p>
										<p align="center">(Before submission, Pl. Confirm the
											availability from NMIMS)</p>
										<p align="center">Admission (Final) Payment Receipt Number
											_____________</p>
										<p align="center">(For first year students)</p>
										<%-- <p class="fontSize" align="right">Date:<form:input type="text" path="createdDate" value="${currentDate}" readonly="true"/></p>   --%>
										<br>
										<p align="center" style="font-weight: bold;">
											<form:input type="text" path="campus" required="required" />
											<span style="color: red">*</span> CAMPUS
										</p>
										<p class="fontSize">
											The Registrar<br> SVKM's NMIMS<br>
										</p>
										<br>
										<br>

										<br>
										<br>
										<div class="fontSize">
											<p>
												Sir,<br> I wish to apply for providing accommodation in
												any Hostel/Residential Flats (converted into hostel)
												(preference ticked as above) managed by SVKM's NMIMS
											</p>
											<p>
												for the academic year
												<form:input type="text" path="acadYear"
													value="${student.acadYear}" readonly="true" />
											</p>
											<br>
											<p>I hereby agree that I have read and will abide by the
												Rules and Regulations of the hostel in force from time to
												time. I furnish the following particulars :</p>
											<br>
											<p>(Strike out whichever is not applicable)</p>
											<br>
											<p style="font-weight: bold;">PERSONAL DATA :</p>
											<br>
											<ol>
												<li>Full Name (with Surname) <form:input type="text"
														path="lastname" value="${student.lastname}"
														readonly="true" /> <form:input type="text"
														path="firstname" value="${student.firstname}"
														readonly="true" /> <form:input type="text"
														path="fatherName" value="${student.fatherName}"
														readonly="true" /></li>
												<br>

												<li>Residential Address <span style="color: red">*</span>
												<form:textarea class="form-group" path="address"
														required="required" name="editor1" id="editor1" />
													(Email)<form:input type="text" path="email"
														value="${student.email}" /><br> Tel. No. (M) <span
													style="color: red">*</span> <form:input type="number"
														path="mobile" required="required" /> (R)<form:input
														type="text" path="telephone" />
												</li>
												<br>
												<li>Date of Birth <span style="color: red">*</span> <form:input
														type="date" path="dob" value="" required="required" /></li>
												<br>
												<li>Nationality <span style="color: red">*</span> <form:input
														type="text" path="nationality" required="required" />
												</li>
												<br>
											</ol>
											<p>I declare that the information given above is true to
												the best of my knowledge. I agree that if any information
												furnished above found incorrect my admission is liable to be
												cancelled.</p>

										</div>
										<br>
										<br>
										<div class="fontSize">
											<p style="font-weight: bold;">FAMILY BACKGROUND :</p>
											<br>
											<ol>
												<li>Full name of the Parent/Guardian <span
													style="color: red">*</span> <form:input type="text"
														path="parentName" required="required" />
												</li>
												<br>
												<li>Relationship <span style="color: red">*</span> <form:input
														type="text" path="relationship" required="required" /></li>
												<br>
												<li>Occupation <span style="color: red">*</span> <form:input
														type="text" path="occupation" required="required" />
													Designation <span style="color: red">*</span> <form:input
														type="text" path="designation" required="required" /></li>
												<br>
												<li>Office Address <span style="color: red">*</span> <form:textarea
														class="form-group" path="officeAddress"
														required="required" /> Email <span style="color: red">*</span>:
													<form:input type="text" path="parentEmail"
														required="required" /><br> Tel. No. (with STD Code) <span
													style="color: red">*</span> <form:input type="number"
														path="parentMobile" required="required" />
												</li>
												<br>
											</ol>

											<br> <br>

											<p style="font-weight: bold;">NEAREST LOCAL GUARDIAN</p>
											<br>

											<p>Name and address of contact person who should be
												contacted (in case of emergency)</p>
											<br>
											<ol>
												<li>Name <span style="color: red">*</span> <form:input
														type="text" path="localName1" required="required" />
													Address <span style="color: red">*</span> <form:textarea
														class="form-group" path="localAddress1"
														required="required" /><br> Tel. No. (Mob / Res) <span
													style="color: red">*</span> <form:input type="number"
														path="localMobile1" required="required" />
												</li>
												<br>
												<li>Name <span style="color: red">*</span> <form:input
														type="text" path="localName2" required="required" />
													Address <span style="color: red">*</span> <form:textarea
														class="form-group" path="localAddress2"
														required="required" /><br> Tel. No. (Mob / Res) <span
													style="color: red">*</span> <form:input type="number"
														path="localMobile2" required="required" />
												</li>
												<br>
											</ol>
											<p>
												I request you to admit my ward Mr. / Ms.
												<input type="text" 
													value="${student.firstname}" readonly="true" />
												<input type="text" 
													value="${student.lastname}" readonly="true" />
												to the SVKM's NMIMS Hostel / Residential Flat.
											</p>
											<p>I give an undertaking that he / she has read and will
												observe all Rules & Regulation of the Hostel.</p>
										</div>
										<br>
										<div class="col-sm-5">
											<div class="form-group"></div>
										</div>
										<div class="col-sm-4">
											<div class="form-group">
												<button id="cancel" name="cancel"
													formaction="viewHostelServices" class="btn btn-success"
													formnovalidate="formnovalidate">Cancel</button>
												<!-- send service id from here -->
												<form:input path="serviceId" value="${serviceId}"
													type="hidden" />
												<button id="submit" name="submit"
													formaction="sendStudentHostelData" class="btn btn-success">Submit</button>
											</div>
										</div>
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
