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
	String link = "";
	if (isEdit) {
		label = "Edit File Details";
		updateOrCreate = "Update File";
		link = "updateLibraryFile";
	} else {
		label = "Add File";
		updateOrCreate = "Add File";
		link = "addLibraryZip";
	}
%>

<style>
#output {
	width: 80%;
	margin: 0 auto;
	padding: 1em;
}

#output>li {
	width: 100%;
	list-style-type: disc;
	line-height: 2em;
	border-bottom: 1px solid #ccc;
}

#output>li span {
	float: right;
}
</style>
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
							<i class="fa fa-angle-right"></i> <a
								href="${pageContext.request.contextPath}/viewLibrary">Library</a>
							<i class="fa fa-angle-right"></i>
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
										<form:form action="createAssignment" id="createAssignment"
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
												<form:input type="hidden" path="filePath" />
												<%
													}
												%>


												<div class="row">

													<%-- <div class="col-sm-4 column">
														<div class="form-group">
															<form:label path="contentName" for="contentName">File Name</form:label>
															<form:input path="contentName" type="text"
																class="form-control" required="required" />
														</div>
													</div> --%>

													<div class="col-sm-4 column">
														<div class="form-group">
															<form:label path="contentDescription"
																for="contentDescription">File Description</form:label>
															<form:textarea path="contentDescription"
																class="form-control" />

														</div>
													</div>


													<div class="col-sm-4 column">
														<div class="form-group">
															<form:label path="" for="file">
																<%
																	if (isEdit) {
																%>
																Select File only if you wish to override earlier file <i
																	class="fa fa-file-o"></i>
																<%
																	} else {
																%>
																Select File from Computer <i class="fa fa-file-o"></i>
																<%
																	}
																%>

															</form:label>
															<input id="file" name="file" type="file" id="file"
																class="form-control" accept=".zip"/>
														</div>
														<div id=fileSize></div>
														<ul id="output">
														</ul>
													</div>

												</div>


												<div class="row">

													<div class="col-sm-8 column">
														<div class="form-group">


															<button id="submit" class="btn btn-large btn-primary"
																formaction="<%=link%>"><%=updateOrCreate%></button>

															<button id="cancel" class="btn btn-danger"
																formaction="homepage" formnovalidate="formnovalidate">Cancel</button>

															<button type="reset" value="reset"
																class="btn btn-large btn-primary resetBtn">Reset</button>
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
			<script type="text/javascript">
				$('#file').bind('change', function() {
					// alert('This file size is: ' + this.files[0].size/1024/1024 + "MB");
					var fileSize = this.files[0].size / 1024 / 1024 + " MB";
					$('#fileSize').html("File Size:" + (fileSize));
				});
			</script>
			<script>
				$('input:file[multiple]')
						.change(
								function(e) {
									console.log(e.currentTarget.files);
									$('#output').empty();
									var numFiles = e.currentTarget.files.length;
									for (i = 0; i < numFiles; i++) {
										fileSize = parseInt(
												e.currentTarget.files[i].size,
												10) / 1024;
										filesize = Math.round(fileSize);
										$('<li />').text(
												e.currentTarget.files[i].name)
												.appendTo($('#output'));
										$('<span />').addClass('filesize')
												.text('(' + filesize + 'kb)')
												.appendTo($('#output li:last'));
									}
								});
				
				$(".resetBtn").click(function(){
					$('#output').empty();
					$('#fileSize').empty();
				});
			</script>
		</div>
	</div>





</body>
</html>
