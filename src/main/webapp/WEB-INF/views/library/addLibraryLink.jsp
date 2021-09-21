<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<html lang="en">
<jsp:include page="../common/css.jsp" />
<%
	boolean isEdit = "true".equals((String) request
			.getAttribute("edit"));
	String label = "";
	String updateOrCreate = "";
	String link="";
	if (isEdit) {
		label = "Update External Link";
		updateOrCreate = "Update External Link";
		link="updateLibraryLink";
	} else {
		label = "Add External Link";
		updateOrCreate = "Create External Link";
		link="addLibraryLink";
	}
%>
<body class="nav-md footer_fixed">

	<div class="loader"></div>


	<div class="container body">
		<div class="main_container">


			<jsp:include page="../common/leftSidebar.jsp" />
<jsp:include page="../common/topHeaderLibrian.jsp" >
			<jsp:param value="Library" name="activeMenu"/>
		</jsp:include>			



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
							<a href="${pageContext.request.contextPath}/viewLibrary">Library</a> <i
								class="fa fa-angle-right"></i>
							<%=label%>
						</div>
						<jsp:include page="../common/alert.jsp" />

						<!-- Input Form Panel -->
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2><%=label%></h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
										<form:form action="addLink" id="createAssignment"
											method="post" modelAttribute="library"
											enctype="multipart/form-data">
											<fieldset>
												<form:hidden path="contentType" />
												<form:hidden path="folderPath" />
												<form:hidden path="parentId" />
												<%
													if (isEdit) {
												%>
												<form:input type="hidden" path="id" />
												<%
													}
												%>





												<div class="row">

													<div class="col-sm-4 column">
														<div class="form-group">
															<form:label path="contentName" for="contentName">Link Name <span style="color: red">*</span></form:label>
															<form:input path="contentName" type="text"
																class="form-control" required="required" />

														</div>
													</div>

													<div class="col-sm-4 column">
														<div class="form-group">
															<form:label path="contentDescription"
																for="contentDescription">Link Details</form:label>
															<form:textarea path="contentDescription"
																class="form-control" />

														</div>
													</div>

													<div class="col-sm-4 column">
														<div class="form-group">
															<form:label path="" for="file">Link URL <span style="color: red">*</span> <i
																	class="fa fa-link"></i>
															</form:label>
															<form:input path="linkUrl" type="url"
																class="form-control" required="required" />
														</div>
													</div>
												</div>

												<div class="row">
													<div class="col-sm-8 column">
														<div class="form-group">


															<button id="submit" class="btn btn-large btn-primary"
																formaction="<%=link%>"><%=updateOrCreate%></button>


															<button id="cancel" class="btn btn-danger"
																formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
														</div>
													</div>
												</div>



											</fieldset>
										</form:form>
									</div>
								</div>
							</div>
						</div>



					</div>



				</div>

			</div>
			<!-- /page content: END -->


			<jsp:include page="../common/footerLibrarian.jsp" />

		</div>
	</div>





</body>
</html>
