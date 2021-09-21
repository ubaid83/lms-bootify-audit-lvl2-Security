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

			<jsp:include page="../common/leftSidebar.jsp">
				<jsp:param name="courseId" value="${courseId}" />
			</jsp:include>

			<jsp:include page="../common/topHeaderLibrian.jsp">
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
							<i class="fa fa-angle-right"></i> Add Admin Program Mapping
						</div>
						<jsp:include page="../common/alert.jsp" />

						<!-- Input Form Panel -->
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>Add Admin Program Mapping</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
										<form:form action="addAdminProgram" method="post"
											modelAttribute="user">


											<div class="row">
												<div class="col-sm-4 column">

													<div class="form-group">
														<form:label path="username" for="username">Username <span style="color: red">*</span></form:label>

														<form:input id="username" path="username" type="text"
															placeholder="Username" class="form-control"
															required="required" />
													</div>
												</div>
												
											</div>

										<div class="row">
											
												<div class="col-sm-4 column">

													<div class="form-group">
														<label  for="schoolObjId">School Object ID <span style="color: red">*</span></label>

														<input id="schoolObjId"  type="number"
															placeholder="schoolObjId" class="form-control"
															/>
													</div>
												</div>
											</div>
											
											
											<div class="row">
											
												<div class="col-sm-8 column">

													<div class="form-group">
														
													<form:select class="form-control" id="programNameDropDown" path="schoolObjId" multiple="multiple" required="required" ></form:select>
													</div>
												</div>
											</div>
											
											

											<div class="row">

												<div class="col-sm-12 column">
													<div class="form-group">


														<button id="submit" class="btn btn-large btn-primary"
															formaction="addAdminProgram">Add</button>

														<button id="cancel" class="btn btn-danger"
															formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
													</div>
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
			<script type="text/javascript"
				src="<c:url value="/resources/js/customDateTimePicker.js" />"></script>

		</div>
	</div>


<script>
$('#schoolObjId').on('change', function() {
	let schoolObjId =  $('#schoolObjId').val();
	var Did= $("DLState").val();
	console.log("schoolObjId------------->"+schoolObjId);
	    $.ajax({
   	    	url: "getProgramByObjectId?schoolObjId=" + schoolObjId,
   	    	type: "POST",
   	    	//data: {
   	    	//	schoolObjId: schoolObjId
   	    	//},
   	            success: function (data) { 
   	            	let parseData =  JSON.parse(data);
   	            	console.log("Data------------------->"+data.id)
               var s = '<option value="-1">Select Program Name</option>';  
               for (var i = 0; i < parseData.length; i++) { 
            	   console.log('key---------->'+data[i].id)
            	
                   s += '<option value="' + parseData[i].id + '">' + parseData[i].programName + '</option>';  
               }  
               $("#programNameDropDown").html(s);  
           }
   	    })
});
</script>


</body>
</html>
