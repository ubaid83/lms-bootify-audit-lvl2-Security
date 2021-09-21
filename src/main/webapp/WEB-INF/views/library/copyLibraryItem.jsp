<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<html lang="en">
<jsp:include page="../common/css.jsp" />

<body class="nav-md footer_fixed">

	<div class="loader"></div>


	<div class="container body">
		<div class="main_container">


			<jsp:include page="../common/leftSidebar.jsp" />
			<jsp:include page="../common/topHeaderLibrian.jsp">
				<jsp:param value="Library" name="activeMenu" />
			</jsp:include>


			<!-- page content: START -->
			<div class="right_col" role="main">

				<div class="dashboard_container">

					<div class="dashboard_container_spacing">

						<div class="breadcrumb">
							<c:out value="${Program_Name}" />

							<sec:authorize access="hasRole('ROLE_STUDENT')">

								<i class="fa fa-angle-right"></i>

								<c:out value="${AcadSession}" />

							</sec:authorize>

							<br> <br> <a
								href="${pageContext.request.contextPath}/homepage">Dashboard</a>
							<i class="fa fa-angle-right"></i> Library
						</div>
						<jsp:include page="../common/alert.jsp" />



						<!-- Results Panel -->
						<div class="row">
							<div class="col-xs-12 col-sm-12 text-right">
								<div class="x_panel">
									<div class="x_title">
										<h2> Library Document : ${content.contentName}</h2>
										<c:url value="/viewLibraryAnnouncements" var="rootFolder">
										</c:url>
										<a id="submit" class="btn btn-xs btn-primary "
											href="${rootFolder}">Back to Library Home</a>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
											<li><a class="close-link"><i class="fa fa-close"></i></a></li>
										</ul>
										<div class="clearfix"></div>

									</div>
									<div class="x_content">
										<form:form action="copyLibraryItem" id="copyLibraryItem"
											method="post" modelAttribute="content"
											enctype="multipart/form-data">


											<form:input path="id" type="hidden" />
											<form:input path="folderPath" type="hidden" />
											<form:input path="contentType" type="hidden" />
											<form:input path="contentName" type="hidden" />


											<div class="col-sm-4 column" style="width: 270px;">
												<div class="form-group">

													<label>Select Destination Library Document :</label>


												</div>

											</div>
											<div class="col-sm-4 column">
												<div class="form-group">

													<form:input type="text" id="txtName" class="form-control"
														path="copyPath" readonly="readonly" />


												</div>

											</div>
											<div class="col-sm-4 column">
												<div class="form-group">
													<input type="button" value="Select Name"
														onclick="SelectName()" />

												</div>

											</div>

											<div class="clearfix"></div>
											<div class="col-md-4 col-sm-6 col-xs-12 column">
												<div class="form-group">
													<sec:authorize access="hasRole('ROLE_LIBRARIAN')">
														<c:if test="${action eq 'copy' }">
															<button id="submit" class="btn btn-large btn-primary"
																formaction="copyLibraryItem">Copy</button>
														</c:if>
														<c:if test="${action eq 'move' }">
															<button id="submit" class="btn btn-large btn-primary"
																formaction="moveLibraryItem">Move</button>
														</c:if>
													</sec:authorize>
													<button id="cancel" class="btn btn-large btn-danger"
														formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
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
			<!-- /page content: END -->


			<jsp:include page="../common/footerLibrarian.jsp" />
			<script>
				var popup;
				var folderPath = null;
				var parentId = null;
				function SelectName() {

					popup = window
							.open(
									"${pageContext.request.contextPath}/viewLibraryForCopy",
									"myWindow", "width=500,height=500");
					popup.focus();
				}
				/* function clicked() {
					
					var folderPath = null;
					var parentId = null;
					var myWindow = window
							.open(
									"${pageContext.request.contextPath}/viewLibraryForCopy",
									"myWindow", "width=500,height=500");

					if (myWindow.closed) {
						alert('window is closed')
						var a = sessionStorage.getItem("copyvalue");
						alert(a);
					}
				} */
				function myFunction() {
					var input, filter, table, tr, td, i;
					input = document.getElementById("myInput");
					filter = input.value.toUpperCase();
					table = document.getElementById("contentTree");
					tr = table.getElementsByTagName("tr");
					for (i = 0; i < tr.length; i++) {
						td = tr[i].getElementsByTagName("td")[0];
						if (td) {
							if (td.innerHTML.toUpperCase().indexOf(filter) > -1) {
								tr[i].style.display = "";
							} else {
								tr[i].style.display = "none";
							}
						}
					}
				}
			</script>

		</div>
	</div>





</body>
</html>
