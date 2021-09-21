<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<html lang="en">
<jsp:include page="../common/css.jsp" />

<script
	src="https://gitcdn.github.io/bootstrap-toggle/2.2.2/js/bootstrap-toggle.min.js"></script>
<body class="nav-md footer_fixed">

	<div class="loader"></div>


	<div class="container body">
		<div class="main_container">

			<jsp:include page="../common/leftSidebar.jsp">
				<jsp:param name="courseId" value="${courseId}" />
			</jsp:include>

			<jsp:include page="../common/topHeader.jsp" >
				<jsp:param value="Grievance" name="activeMenu"/>
			</jsp:include>

			<%
				boolean isEdit = "true".equals((String) request
						.getAttribute("edit"));
			%>

			<!-- page content: START -->
			<div class="right_col" role="main">

				<div class="dashboard_container">

					<div class="dashboard_container_spacing">

						<div class="breadcrumb">
							<c:out value="${Program_Name}" />
						
						<sec:authorize access="hasRole('ROLE_STUDENT')">
						
						<i
						class="fa fa-angle-right"></i>
											
											<c:out value="${AcadSession}" />
											
											</sec:authorize>
											
						<br><br>
							<a href="${pageContext.request.contextPath}/homepage">Dashboard</a> <i class="fa fa-angle-right"></i>
							Raise Grievance
						</div>
						<jsp:include page="../common/alert.jsp" />

						<!-- Input Form Panel -->
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>Raise Grievance</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
										<div class="row page-body">
											<div class="col-sm-12 column">
												<form:form action="grievanceForm" method="post"
													modelAttribute="grievances">


													<div class="row">

														<div class="col-md-4 col-sm-6 col-xs-12 column">
															<div class="form-group">
																<form:label path="typeOfGrievance" for="typeOfGrievance">Type Of Grievances <span style="color: red">*</span></form:label>
																<form:select id="typeOfGrievance" path="typeOfGrievance"
																	class="form-control" required="required">
																	<form:option value="">Select Type Of Grievances</form:option>
																	<c:forEach var="g" items="${allTypesOfGrievances}">
																		<form:option value="${g}">${g}</form:option>
																	</c:forEach>
																</form:select>
															</div>
														</div>



														<div class="col-md-4 col-sm-6 col-xs-12 column">
															<div class="form-group">
																<form:label path="grievanceCase" for="grievanceCase">Type Of Cases <span style="color: red">*</span></form:label>
																<form:select required="required" id="grievanceCase"
																	path="grievanceCase" class="form-control">
																	<form:option value="">Select Case</form:option>

																</form:select>
															</div>
														</div>


														<div class="col-sm-12 column">
															<div class="form-group">
																<!--<form:label path="description" for="description">Example textarea</form:label>-->
																<form:label path="description" for="description">Description <span style="color: red">*</span></form:label>
																<form:textarea required="required" class="form-control"
																	path="description" id="description" rows="10" />

															</div>
															<button type="submit" class="btn btn-primary"
																formaction="saveGrievanceForm">Submit</button>
															<a href="/homepage"><button id="cancel" name="cancel"
																	class="btn btn-primary" formnovalidate="formnovalidate">Back</button></a>
														</div>

													</div>

													
												</form:form>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>




					</div>



				</div>

			</div>
			<!-- /page content: END -->


			<jsp:include page="../common/footer.jsp" />
			<script>
      $(document)
                  .ready(
                              function() {
 
                                    $("#typeOfGrievance")
                                                .on(
                                                            'change',
                                                            function() {
                                                                  var grievanceType = $(this).val();
                                                                  if (grievanceType) {
                                                                        $
                                                                                    .ajax({
                                                                                          type : 'GET',
                                                                                          url : '${pageContext.request.contextPath}/getTypeBasedOnGroup?'
                                                                                                      + 'typeOfGrievance='
                                                                                                      + grievanceType,
                                                                                          success : function(
                                                                                                      data) {
                                                                                                var json = JSON
                                                                                                            .parse(data);
                                                                                                var optionsAsString = "";
 
                                                                                                $('#grievanceCase')
                                                                                                            .find(
                                                                                                                        'option')
                                                                                                            .remove();
                                                                                                console
                                                                                                            .log(json);
                                                                                                for (var i = 0; i < json.length; i++) {
                                                                                                      var idjson = json[i];
                                                                                                      console
                                                                                                                  .log(idjson);
 
                                                                                                            optionsAsString += "<option value='" +idjson + "'>"
                                                                                                                        + idjson
                                                                                                                        + "</option>";
                                                                                                     
                                                                                                }
                                                                                                console
                                                                                                            .log("optionsAsString"
                                                                                                                        + optionsAsString);
 
                                                                                                $('#grievanceCase')
                                                                                                            .append(
                                                                                                                        optionsAsString);
 
                                                                                          }
                                                                                    });
                                                                  } else {
                                                                        alert('Error no grievance');
                                                                  }
                                                            });
 
                              });
</script>
 

		</div>
	</div>





</body>
</html>
