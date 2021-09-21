<%@page import="com.spts.lms.web.utils.Utils"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@page import="java.util.*"%>



<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<jsp:include page="../common/newDashboardHeader.jsp" />

<div class="d-flex adminPage" id="adminPage">
	<jsp:include page="../common/newAdminLeftNavBar.jsp" />
	<jsp:include page="../common/rightSidebarAdmin.jsp" />


	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper">

		<jsp:include page="../common/newAdminTopHeader.jsp" />

		<div class="container">
			<div class="row">
				<!-- SEMESTER CONTENT -->
				<div
					class="col-lg-12 col-md-12 col-sm-12 dashboardBody ml-auto mr-auto">

					<!-- page content: START -->

					<nav aria-label="breadcrumb">
						<ol class="breadcrumb">
							<li class="breadcrumb-item"><a
								href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
							<li class="breadcrumb-item" aria-current="page"><c:out
									value="${Program_Name}" /></li>
							<sec:authorize access="hasRole('ROLE_STUDENT')">
								<c:out value="${AcadSession}" />
							</sec:authorize>
							<li class="breadcrumb-item active" aria-current="page">Cofigure Weightage For Questions.</li>
						</ol>
					</nav>

					<jsp:include page="../common/alert.jsp" />


					<!-- Input Form Panel -->
					<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
									<div class="x_panel">
										<div class="x_title">
											<h5>Configure weightage For questions</h5>
                                        </div>
                                    </div>
							</div>
							
							<div class="x_content">
							<form:form id="form1" action="addQuestionConfigurationForm" method="post"
								modelAttribute="assignmentConfiguration">
								<form:input type="hidden" path="assignmentId" value="${ assignmentId }" />
								<div id="main">
									<input type="button" id="btAdd" value="Add Element"
										class="bt btn btn-large btn-primary" /> <input type="button"
										id="btRemove" value="Remove Element"
										class="bt btn btn-large btn-primary" /> <input type="button"
										id="btRemoveAll" value="Remove All"
										class="bt btn btn-large btn-primary" /><br />
								</div>
							</form:form>
							</div>
						</div>
					</div>
				</div>

			
	

	<!-- Results Panel -->




	<%-- 	<jsp:include page="../common/paginate.jsp">
							<jsp:param name="baseUrl" value="searchFacultyGroups" />
						</jsp:include> --%>

	<!-- /page content: END -->



<!-- SIDEBAR START -->
<%-- <jsp:include page="../common/newSidebar.jsp" /> --%>
<!-- SIDEBAR END -->
	<jsp:include page="../common/newAdminFooter.jsp"/>
<script>
	$(document)
			.ready(
					function() {

						var iCnt = 1;
						// CREATE A "DIV" ELEMENT AND DESIGN IT USING jQuery ".css()" CLASS.
						var container = $(document.createElement('div')).css({
							padding : '5px',
							margin : '20px',
							borderTopColor : '#999',
							borderBottomColor : '#999',
							borderLeftColor : '#999',
							borderRightColor : '#999'
						});

						var edit = '${ edit }';

						if ("Y" == edit) {

							console.log('xyz' + iCnt);
							<c:forEach var="assignmentConfig" items="${assignConfigList}">

							$(container)
									.append(
											'<div class="row"><div class="col-md-4 col-sm-6 col-xs-12 column"> <input type=text class="input form-control" id=tbb' + iCnt + ' ' +
						                    'name="questionNumber" value = "${ assignmentConfig.questionNumber }" placeholder="Question Number" /> </div><div class="col-md-4 col-sm-6 col-xs-12 column"><input type=text class="input form-control" id=tb' + iCnt + ' ' +
	                    'name="marks" value = "${ assignmentConfig.marks }" placeholder="Marks" /></div> </div>');
							console.log('pqrs' + iCnt);

							if (iCnt == 1) {
								var divSubmit = $(document.createElement('div'));
								$(divSubmit).append(
										'<input type=button class="bt btn btn-large btn-success"'
												+ 'onclick="GetTextValue()"'
												+ ' id=submit value=Submit />');
							}

							$('#main').after(container, divSubmit);
							iCnt = iCnt + 1;
							</c:forEach>

						}

						$('#btAdd')
								.click(
										function() {
											if (iCnt <= 30) {

												console.log('abcd ' + iCnt);

												// ADD TEXTBOX.
												$(container)
														.append(
																'<div class="row"><div class="col-md-4 col-sm-6 col-xs-12 column"> <input type=text class="input form-control" id=tbb' + iCnt + ' ' +
											                    'name="questionNumber" placeholder="Question Number" /> </div><div class="col-md-4 col-sm-6 col-xs-12 column"><input type=text class="input form-control" id=tb' + iCnt + ' ' +
                   												'name="marks" placeholder="Marks" /></div> </div>');

												// SHOW SUBMIT BUTTON IF ATLEAST "1" ELEMENT HAS BEEN CREATED.
												if (iCnt == 1) {
													var divSubmit = $(document
															.createElement('div'));
													$(divSubmit)
															.append(
																	'<input type=button class="bt btn btn-large btn-success"'
																			+ 'onclick="GetTextValue()"'
																			+ ' id=submit value=Submit />');
												}

												// ADD BOTH THE DIV ELEMENTS TO THE "main" CONTAINER.
												$('#main').after(container,
														divSubmit);

												iCnt = iCnt + 1;
											}
											// AFTER REACHING THE SPECIFIED LIMIT, DISABLE THE "ADD" BUTTON.
											// (20 IS THE LIMIT WE HAVE SET)
											else {
												$(container)
														.append(
																'<label>Reached the limit</label>');
												$('#btAdd')
														.attr('class',
																'bt-disable btn btn-large btn-primary');
												$('#btAdd').attr('disabled',
														'disabled');
											}
										});

						// REMOVE ONE ELEMENT PER CLICK.
						$('#btRemove')
								.click(
										function() {
											if (iCnt != 0) {
												iCnt = iCnt - 1;
												$('#tb' + iCnt).remove();
												$('#tbb' + iCnt).remove();
												//iCnt = iCnt - 1;
												console.log('remove ' + iCnt);
											}

											if (iCnt == 1) {
												$(container).empty().remove();

												$('#submit').remove();
												$('#btAdd')
														.removeAttr('disabled')
														.attr('class',
																'bt  btn btn-large btn-primary');
												iCnt = 1;
											}
										});

						// REMOVE ALL THE ELEMENTS IN THE CONTAINER.
						$('#btRemoveAll').click(
								function() {
									$(container).empty().remove();

									$('#submit').remove();
									iCnt = 1;

									$('#btAdd').removeAttr('disabled').attr(
											'class',
											'bt  btn btn-large btn-primary');
								});
					});

	// PICK THE VALUES FROM EACH TEXTBOX WHEN "SUBMIT" BUTTON IS CLICKED.
	var divValue, values = '';

	function GetTextValue() {
		/* $(divValue) 
		    .empty() 
		    .remove(); 
		
		values = '';

		$('.input').each(function() {
		    divValue = $(document.createElement('div')).css({
		        padding:'5px', width:'200px'
		    });
		    values += this.value + '<br />'
		});

		$(divValue).append('<p><b>Your selected values</b></p>' + values);
		$('body').append(divValue); */

		var maxMarks = ${TotalScore};
		var id = ${assignmentId};
		
		var hash = new Object();
		var marks_input = document.getElementsByName("marks");
		var questionNumber_input = document.getElementsByName("questionNumber");

		var countMarks = 0;

		for (var i = 0; i < marks_input.length; i++) {

			hash[marks_input[i].value] = questionNumber_input[i].value;

			countMarks = countMarks
					+ (parseInt(marks_input[i].value) * parseInt(questionNumber_input[i].value));
		}
		hash['assignmentId'] = id;

		console.log(hash);
		console.log(id);

		var countQue = 0;

		for (var i = 0; i < questionNumber_input.length; i++) {

			countQue = countQue + parseInt(questionNumber_input[i].value);

		}
		var array =new Array();
		for (var i = 0; i < marks_input.length; i++) {
			
			//hash[marks_input[i].value] = noOfQues_input[i].value;
			var mi = marks_input[i].value;
			var qni = questionNumber_input[i].value;
			
			
			var arrayItem = {'assignmentId': id, 'questionNumber': qni,'marks':mi};
			//arrayItem.push(tpi);
			//arrayItem.push(mi);
			//arrayItem.push(noi);
			
			array.push(arrayItem);
		}
		
		//alert(maxMarks)
		/* if (countMarks == maxMarks) { */

				$
						.ajax({
							type : "POST",
							dataType: 'json',
							/* dataType : 'json', */
							url : "${pageContext.request.contextPath}/addAssignmentConfiguration",
							data : {
								assignmentId: id,
					    		questionConfiguration: JSON.stringify(array)
					    	},
							success : function(result) {
								console.log(JSON.stringify(result))
								var parseObj = JSON.parse(JSON.stringify(result));
								console.log(parseObj.msg)
								if(parseObj.msg == "Already evaluated"){
									alert("Assignment already evaluated so you can't change weightage configuration.")
									
								}else if(parseObj.msg == "success"){
									alert("Successfully configured.");
								}else if(parseObj.msg == "failed"){
									alert("Error while configuring.");
								}
								location.reload();
								/* console.log('success : ' + result);
								window.location.href = '${pageContext.request.contextPath}/viewTestDetails?testId='
										+ id; */

							},
							error : function(result) {
								
								console.log('error');
								alert("Error in configured.");
								console.log(result);
							}
						});
			/* } else {

				swal(
						'WARNING!',
						'Total Score of test is '
								+ maxMarks
								+ ', you are configuring assignment with more than total marks of assignment.',
						'warning');

			} */
		

	}
</script>