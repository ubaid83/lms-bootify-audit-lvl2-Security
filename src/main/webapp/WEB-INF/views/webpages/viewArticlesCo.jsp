<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<html lang="en">

<jsp:include page="../common/css.jsp" />
<%
	String libraryName = (String) session.getAttribute("libraryName");
%>
<head>
<style>
ul li:hover {
	background: #cccccc;
}
</style>


</head>

<body class="nav-md footer_fixed">

	<div class="loader"></div>

	<div class="container body">

		<div class="main_container">

			<jsp:include page="../common/leftSidebar.jsp" />

			<jsp:include page="../common/topHeaderLibrian.jsp">
				<jsp:param value="Message" name="activeMenu" />
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


						</div>

						<jsp:include page="../common/alert.jsp" />

						<!-- Input Form Panel -->

						<div class="row">

							<div class="col-xs-12 col-sm-12">

								<div class="x_panel">

									<div class="ui-105-content">

										<ul class="nav nav-tabs nav-justified"
											style="border-bottom: 1px solid #000000; font-size: 15;">


											<li role="presentation" id="link-two"><a
												href="#resources-block" aria-controls="profile" role="tab"
												data-toggle="tab"><i class="fa fa-newspaper-o"
													aria-hidden="true"></i> Articles</a></li>



										</ul>

										<div class="tab-content">



											<!-- Results Panel -->


											<div role="tabpanel" class="tab-pane" id="resources-block">

												<!-- Login Block Form -->

												<div class="register-block-form">

													<form:form cssClass="form" role="form"
														action="viewArticles" method="post"
														modelAttribute="webpages" id="viewArticles">


														<sec:authorize access="hasAnyRole('ROLE_COUNSELOR')">
															<div class="row">
																<a class="btn btn-primary btn-sm pull-right"
																	role="button" href="createArticleForm"><i
																	class="fa fa-plus" aria-hidden="true"></i> Create
																	Article</a>
															</div>
														</sec:authorize>


														<!-- <div class="table-responsive"> -->

														<table id="myTable"
															class="table table-striped table-hover"
															style="font-size: 16px; font-family: serif;">

															<thead>

																<tr>
																	<th>Sr. No.</th>
																	<th>Name</th>
																	<th>Description</th>


																	<sec:authorize access="hasAnyRole('ROLE_COUNSELOR')">

																		<th>Actions</th>
																	</sec:authorize>
																</tr>

															</thead>

															<tbody class="panel">

																<c:forEach var="tabs" items="${tabsList}"
																	varStatus="status">

																	<tr data-toggle="collapse" data-parent="#myTable"
																		data-target="#cores${status.count}">
																		<td><c:out value="${status.count}" /></td>
																		<td
																			style="color: #4b94d9; font-size: 18px; cursor: pointer;"><c:out
																				value="${tabs.name}" /></td>
																		<td><c:out value="${tabs.description}" /></td>



																		<td><sec:authorize
																				access="hasAnyRole('ROLE_COUNSELOR')">
																				<%-- <c:if test="${tabs.createdBy eq username }"> --%>

																				<%-- <a href="createArticleForm?id=${tabs.id}"
																					title="Edit">Edit</a>&nbsp; --%> <a
																					href="deleteArticle?id=${tabs.id}" title="Delete"
																					onclick="return confirm('Are you sure you want to delete this record?')">Delete</a>
																				<%-- </c:if> --%>
																			</sec:authorize> <!-- <div class="row">
																					<div class="col-sm-12 column"> --> <c:if
																				test="${tabs.filePath ne null}">
																				<a href="downloadWebpageFile?id=${tabs.id}">Download
																					attachment</a>

																			</c:if></td>
																	</tr>
																	<tr id="cores${status.count}" class="collapse">

																		<td colspan="4" class="hiddenRow">
																			<div class="row">
																				<div class="col-sm-12 column">
																					Created By - ${tabs.firstname} ${tabs.lastname}
																					<div id=content
																						style="border-style: solid; border-width: 1px; padding: 10px; margin-top: 10px">${tabs.content}</div>
																				</div>
																			</div>
																		</td>
																	</tr>












																	</tr>




																</c:forEach>


															</tbody>

														</table>



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

			</div>

			<!-- /page content: END -->

			<jsp:include page="../common/footerLibrarian.jsp" />

		</div>

	</div>

</body>


<script>
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
<script>
	$(document)

	.ready(function() {

		$("#resources-block").show();

	});
</script>



</html>