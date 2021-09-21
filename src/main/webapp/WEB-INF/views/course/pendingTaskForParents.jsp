<jsp:include page="../common/header.jsp" />

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<section class="container">
	<style type="text/css">
.outer {
	position: relative;
}

.table-responsive {
	padding-top: 33px;
}

table caption {
	position: absolute;
	top: 0;
	left: 0;
}
</style>
	<script type="text/javascript">
		var tableToExcel = (function() {
			var uri = 'data:application/vnd.ms-excel;base64,', template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--></head><body><table>{table}</table></body></html>', base64 = function(
					s) {
				return window.btoa(unescape(encodeURIComponent(s)))
			}, format = function(s, c) {
				return s.replace(/{(\w+)}/g, function(m, p) {
					return c[p];
				})
			}
			return function(table, name, filename) {
				if (!table.nodeType)
					table = document.getElementById(table)
				var ctx = {
					worksheet : name || 'Worksheet',
					table : table.innerHTML
				};
				// window.location.href = uri + base64(format(template, ctx));

				document.getElementById("dlink").href = uri
						+ base64(format(template, ctx));
				document.getElementById("dlink").download = filename;
				document.getElementById("dlink").click();
			}
		})()
	</script>
	<jsp:include page="../common/alert.jsp" />
	<sec:authorize
		access="hasAnyRole('ROLE_STUDENT','ROLE_FACULTY','ROLE_ADMIN')">
		<div class="col-sm-4 column">
			<div class="form-group">

				<label for="courseId">Course</label> <select id="courseId"
					name="courseId" class="form-control">
					<c:if test="${courseId eq  null }">
						<option value="">Select Course</option>
					</c:if>
					<c:forEach var="course" items="${listOfCoursesAssigned}"
						varStatus="status">
						<c:if test="${courseId eq course.id }">
							<option value="${course.id}" selected>${course.courseName}</option>
						</c:if>
						<c:if test="${courseId ne course.id }">
							<option value="${course.id}">${course.courseName}</option>
						</c:if>
					</c:forEach>
				</select>

			</div>
		</div>
	</sec:authorize>


	<sec:authorize access="hasRole('ROLE_PARENT')">
		<div class="col-sm-4 column">
			<div class="form-group">

				<label for="username">Student UserName</label> <select id="username"
					name="username" class="form-control">
					<c:if test="${username eq  null }">
						<option value="">Select Username</option>

					</c:if>
					<c:forEach var="students" items="${listOfStudents}"
						varStatus="status">
						<c:if test="${username eq students.stud_username }">
							<option value="${students.stud_username}" selected>${students.stud_username}</option>
						</c:if>
						<c:if test="${username ne students.stud_username }">
							<option value="${students.stud_username}">${students.stud_username}</option>
						</c:if>
					</c:forEach>
				</select>

			</div>
		</div>

		<div class="col-sm-4 column">
			<div class="form-group">
				<label for="courseId">Course</label> <select id="courseId"
					name="courseId" class="form-control">

					<c:if test="${courseId eq  null }">
						<option value="">Select Course</option>

					</c:if>

					<c:if test="${courseId eq course.id}">

						<c:forEach var="course" items="${courseListForStudent}"
							varStatus="status">
							<c:if test="${courseId eq course.id }">
								<option value="${course.id}" selected>${course.courseName}</option>
							</c:if>
							<c:if test="${courseId ne course.id }">
								<option value="${course.id}">${course.courseName}</option>
							</c:if>
						</c:forEach>
				</select>
				</c:if>
				<c:if test="${courseId ne course.id }">

					<option value="${course.id}">${course.courseName}</option>
				</c:if>

				</select>
			</div>
		</div>


		<%-- <div class="col-sm-4 column">
			<div class="form-group">
				<label  for="courseName">Course Name</label>
				<select id="courseId"  class="form-control">
					
						<option value="">Select Course</option>
					
					<c:if test="${not empty students.courseName}">
						<form:option value="${course.id}">${students.courseName }</form:option>
					</c:if>
				</select>
			</div>
		</div> --%>

	</sec:authorize>

	<div class="clearfix"></div>

	<c:if test="${showGradeCenterList eq true}">

		<div class="row clearfix page-body">
			<div class="col-sm-12 column">
				<fieldset>
					<legend> Grade Center for ${course.courseName } </legend>
					<div class="outer">
						<div class="table-responsive">
							<table id="gradeTable"
								class="table table-striped table-hover table-bordered scrollable"
								style="font-size: 12px">
								<caption>
									<small>NA: Not Allocated, NC: Not Completed, NE: Not
										Evaluated</small>
								</caption>
								<thead>
									<tr>
										<th style="border: 1px solid #ddd;"><div></div></th>
										<th colspan="${fn:length(assignments)}"
											class="text-center bg-blue white"
											style="border: 1px solid #ddd; background-color: #609cec; color: #ffffff; text-align: center;">Assignments</th>
										<th colspan="${fn:length(tests)}"
											class="text-center bg-orange white"
											style="border: 1px solid #ddd; background-color: #f78153; color: #ffffff; text-align: center;">Tests</th>
									</tr>
									<tr>
										<th class="text-center" style="border: 1px solid #ddd;"><div>Student</div></th>
										<c:forEach items="${assignments }" var="assignment">
											<th class="text-center bg-blue white"
												style="border: 1px solid #ddd; background-color: #609cec; color: #ffffff; text-align: center;"><div>${assignment }</div></th>
										</c:forEach>
										<c:forEach items="${tests }" var="test">
											<th class="text-center bg-orange white"
												style="border: 1px solid #ddd; background-color: #f78153; color: #ffffff; text-align: center;"><div>${test }</div></th>
										</c:forEach>
									</tr>
								</thead>
								<tbody>

									<c:forEach var="student" items="${students}">
										<tr>
											<td><div class="text-nowrap">${student.key}</div></td>
											<c:forEach var="studentAssignment" items="${assignments}">
												<c:set var="myParam" value="${studentAssignment}" />
												<td><div>
														<c:out value="${student.value.assigmentToScore[myParam]}" />
													</div></td>
											</c:forEach>
											<c:forEach var="studentTest" items="${tests}">
												<c:set var="myParam" value="${studentTest}" />
												<td><div>
														<c:out value="${student.value.testToScore[myParam]}" />
													</div></td>
											</c:forEach>
										</tr>
									</c:forEach>


								</tbody>
							</table>
						</div>
					</div>
					<br> <a id="dlink" style="display: none;"></a> <input
						type="button" class="btn btn-success" role="button"
						onclick="tableToExcel('gradeTable', '${course.courseName }', 'Grades for ${course.courseName }.xls'      )"
						value="Export to Excel">
				</fieldset>
			</div>
		</div>


	</c:if>


	<jsp:include page="../common/paginate.jsp">
		<jsp:param name="baseUrl" value="searchList" />
	</jsp:include>
</section>

<jsp:include page="../common/footer.jsp" />

<script>
	$("#courseId").on(
			'change',
			function() {
				var selectedValue = $(this).val();
				var username = $('#username').val();
				window.location = '${pageContext.request.contextPath}/gradeCenterForParents?courseId='
						+ encodeURIComponent(selectedValue)

						+ '&username=' + username;

				return false;
			});

	//$(document).ready(
	//     function() {

	/*  $("#search")
	 .on(
	             'click',
	             function() {
	            	 alert("hi");
	            	 $
	                 .ajax({
	                       type : 'POST',
	                       url : 'http://localhost:8084/addStudentFeedbackForm',
	                       success : function(
	                                   data) {
	                       	
	                            console.log("called"+data);

	                       }
	                 });
	            	 
	             }) */

	$("#username")
			.on(
					'change',
					function() {

						var username = $(this).val();
						console.log("called value is " + $(this).val());
						if (username) {
							console.log("called")
							$
									.ajax({
										type : 'GET',
										url : '${pageContext.request.contextPath}/getCoursesByUsername?'
												+ 'username=' + username,

										success : function(data) {

											var json = JSON.parse(data);
											var optionsAsString = "";

											$('#courseId').find('option')
													.remove();
											console.log(json);

											for (var i = 0; i < json.length; i++) {
												var idjson = json[i];
												console.log(idjson);

												for ( var key in idjson) {
													console.log(key + ""
															+ idjson[key]);
													optionsAsString += "<option value='" +key + "'>"
															+ idjson[key]
															+ "</option>";
												}
											}
											console.log("optionsAsString"
													+ optionsAsString);

											$('#courseId').append(
													optionsAsString);

										}
									});
						} else {
							//  alert('Error no faculty');
						}
					});

	//  });
</script>
