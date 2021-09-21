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


			<%
				boolean isEdit = "true".equals((String) request.getAttribute("edit"));
			%>
			<jsp:include page="../common/leftSidebar.jsp">
				<jsp:param name="courseId" value="${courseId}" />
			</jsp:include>

			<jsp:include page="../common/topHeader.jsp">
				<jsp:param value="Feedback" name="activeMenu" />
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
							<i class="fa fa-angle-right"></i> Customize Menu Rights
						</div>
						<jsp:include page="../common/alert.jsp" />

						<!-- Input Form Panel -->
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>Customize Menu Rights</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>


									<div class="x_content">

										<form:form action="addNewFeaturesAdvance" method="post"
											modelAttribute="announcement">

											<div class="col-sm-6 col-md-4 col-xs-12 column">
												<div class="form-group">
													<form:label path="subject" for="subject">Announcement Title <span
															style="color: red">*</span>
													</form:label>
													<form:input path="subject" type="text" class="form-control"
														required="required" />
												</div>
											</div>
											
											<div class="col-sm-6 col-md-4 col-xs-12 column">
												<div class="form-group">
													<form:label path="accessType" for="accessType">Role Type <span
															style="color: red">*</span>
													</form:label>
													<%-- <c:if test="${ not empty content.accessType }">
															<form:hidden path="accessType" />
															: ${content.accessType}
														</c:if>
														<c:if test="${ empty content.accessType }"> --%>
													<form:select id="accessType" path="accessType"
														class="form-control" required="required">
														<form:option value="">Select Access Type</form:option>
														<form:option value="Student">Student</form:option>
														<form:option value="Faculty">Faculty</form:option>
														<form:option value="Admin">Admin</form:option>
													</form:select>
													<%-- </c:if> --%>
												</div>
											</div>
											<div class="col-sm-6 col-md-4 col-xs-12 column">
												<div class="form-group">

													<form:label path="startDate" for="startDate">Display From <span
															style="color: red">*</span>
													</form:label>

													<div class='input-group date' id='datetimepicker1'>
														<form:input id="startDate" path="startDate" type="text"
															placeholder="Start Date" class="form-control"
															required="required" readonly="true" />

														<span class="input-group-addon"><span
															class="glyphicon glyphicon-calendar"></span> </span>
													</div>


												</div>
											</div>

											<div class="col-sm-6 col-md-4 col-xs-12 column">
												<div class="form-group">
													<form:label path="endDate" for="endDate">Display Until <span
															style="color: red">*</span>
													</form:label>

													<div class='input-group date' id='datetimepicker2'>
														<form:input id="endDate" path="endDate" type="text"
															placeholder="End Date" class="form-control"
															required="required" readonly="true" />
														<span class="input-group-addon"><span
															class="glyphicon glyphicon-calendar"></span> </span>
													</div>

												</div>
											</div>
											
											
											


											<div class="row">
												<form:label path="description" for="editor">Announcement Description</form:label>

												<form:textarea class="form-group" path="description"
													name="editor" id="editor" rows="10" cols="80" />

											</div>



											<div class="row">

												<div class="col-sm-8 column">
													<div class="form-group">

														<%
															if (isEdit) {
														%>
														<button id="submit" class="btn btn-large btn-primary"
															formaction="updateAnnouncement">Update
															Notification</button>
														<%
															} else {
														%>
														<button id="submit" class="btn btn-large btn-primary"
															formaction="addNewFeaturesAdvance">Publish
															Notification</button>
														<%
															}
														%>
														<button id="cancel" class="btn btn-danger"
															formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
													</div>
												</div>
											</div>
										</form:form>

									</div>

















									<!-- Ends Here -->



									<%-- <div class="x_content">
										<form:form action="customizeMenuAdvance" method="post"
											modelAttribute="programCampus">


											<div class="row">

												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="campusName" for="campusName">Select School</form:label>
														<form:select id="campusName" path="campusName"
															class="form-control">
															<form:option value="">Select School</form:option>
															<c:forEach var="campus" items="${listSchool}"
																varStatus="status">
																<form:option value="${campus.campusName}">${campus.campusName}</form:option>
															</c:forEach>
														</form:select>
													</div>



												</div>





											</div>

											<table id="table1" class="table table-hover ">

												<thead>

													<tr>

														<th>Sr No</th>
														<th>Menus</th>
														<th>Check</th>


													</tr>

												</thead>

												<tbody>

													<c:forEach var="menu" items="${list2}" varStatus="status">

														<tr>
															<td><c:out value="${status.count}" /></td>



															<td><c:out value="${menu}" /></td>


															<td>
																<form:checkbox path="customMenuRight" id="${status.count}Check" value=""/>


																<form:select path="menuRight" items="${menu}"  multiple="true" />
																<input type="checkbox" class="removeLater"
																id="${status.count}Check">
																
																
															</td>



														</tr>
													</c:forEach>

												</tbody>
											</table>


											<div class="form-group">
												<button id="submit" name="submit"
													formaction="customizeMenuAdvance"
													class="btn btn-large btn-primary">Submit</button>



												<button id="cancel" name="cancel" class="btn btn-danger"
													formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
											</div>

										</form:form>
									</div> --%>






									<!-- Ends Here -->

								</div>
							</div>
						</div>



					</div>



				</div>

			</div>
			<!-- /page content: END -->


			<jsp:include page="../common/footer.jsp" />
			<script type="text/javascript"
				src="<c:url value="/resources/js/customDateTimePicker.js" />"></script>

		</div>
	</div>






	<!-- <script>
		$("#campusNameCustom")
				.on(
						'change',
						function() {

							alert("Onchange Function called!",$(this).attr('id'));

							var campusname = $(this).val();
							alert(campusname)

							/*method 1  var checkbox_value = "";
							$(":checkbox").each(function() {
								var ischecked = $(this).is(":checked");
								if (ischecked) {
									checkbox_value += $(this).val() + "|";
								}
							});
							alert(checkbox_value); */
							
							
							
							
							
							
							/*method 2 var check123=$("input[type='checkbox']").val();
 */
							/* alert(check123) */
							
							
							
							
							
							/* method 3 $("input[name='checkbox']").change(function() {
								  var checked = $(this).val();
								  if ($(this).is(':checked')) {
								    tmp.push(checked);
								  } else {
								    tmp.splice($.inArray(checked, tmp),1);
								  }
								});
							alert(tmp); */
							
							
						 	 var checkboxValues = [];
							$('input.removeLater:checked').map(function() {
								checkboxValues.push($(this).val());
							});
							alert(checkboxValues); 

							var customMenuuRight = checkboxValues.toString();

							alert(customMenuuRight); 

							var selectedValue = $(this).val();

							alert(selectedValue)

							console.log(selectedValue);

							window.location = '${pageContext.request.contextPath}/customizeMenuAdvance?campusName='
									+ encodeURIComponent(selectedValue);
							return false;
							
						});
	</script> -->



	<!--  
<script>

$("#campusNameCustom")
.on(
		'click',
		function() {
			alert('Change function called')
			var campusname = $(this).val();
			alert(campusname)
			 var checkboxValues = [];
		     $('input.removeLater:checked').map(function() {
		                 checkboxValues.push($(this).val());
		     });
		     alert(checkboxValues);
		     var customMenuuRight = checkboxValues.toString();
			/* var campusname = $(this)
					.val(); */
					
					/* var campusname=$('campusName').val(); */
			
		/* 	var checkMenu=$(this).val(); */
			
			console.log("campusname is" + campusname)
			if (campusname) {
				$
						.ajax({
							type : 'POST',
							url  : '${pageContext.request.contextPath}/customizeMenuAdvance?'
									+ 'campusName='
									+  campusName
									+ '&customMenuuRight='
									+ checkboxValues
							success : function(
									data) {
								
								console.log("success")
								var json = JSON
										.parse(data);
								alert(data);
								
								
								var optionsAsString = "";
/* 
								$(
										'#assid')
										.find(
												'option')
										.remove(); */
								/* console
							/* 			.log(json); */
								for (var i = 0; i < json.length; i++) {
									var idjson = json[i];
									console.log(idjson);

									for (var key in idjson) {
										console
												.log(key
														+ ""
														+ idjson[key]);
										optionsAsString += "<option value='" +key + "'>"
												+ idjson[key]
												+ "</option>";
									}
								}
								console
										.log("optionsAsString"
												+ optionsAsString);
/* 
								$(
										'#assid')
										.append(
												optionsAsString); */

							}
						});
			} else {
				
				alert('Error,No School Selected from Dropdown !!! ..');
			}
		});
</script>

-->
	<script>
		
	</script>


</body>
</html>
