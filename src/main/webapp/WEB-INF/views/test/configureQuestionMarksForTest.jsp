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

<div class="d-flex dataTableBottom" id="facultyAssignmentPage">
<sec:authorize access="hasRole('ROLE_FACULTY')">
	<jsp:include page="../common/newLeftNavBarFaculty.jsp"/>
<jsp:include page="../common/newLeftSidebarFaculty.jsp"/>
<jsp:include page="../common/rightSidebarFaculty.jsp"/>
</sec:authorize>
<sec:authorize access="hasRole('ROLE_ADMIN')">

<jsp:include page="../common/newAdminLeftNavBar.jsp"/>
<jsp:include page="../common/rightSidebarAdmin.jsp" />
</sec:authorize>

	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper">

		<sec:authorize access="hasRole('ROLE_FACULTY')">
	<jsp:include page="../common/newTopHeaderFaculty.jsp" />
</sec:authorize>
<sec:authorize access="hasRole('ROLE_ADMIN')">
<jsp:include page="../common/newAdminTopHeader.jsp" />
</sec:authorize>

		<div class="container mt-5">
			<div class="row">
				<!-- SEMESTER CONTENT -->
				<div
					class="col-lg-9 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto">

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
							<li class="breadcrumb-item active" aria-current="page">Cofigure
								Weightage For Questions.</li>
						</ol>
					</nav>

					<jsp:include page="../common/alert.jsp" />


					<!-- Input Form Panel -->
					<div class="card bg-white border">

						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
								<c:if test="${not empty test}">
									<div class="x_panel">

										<div class="x_title">
											<h5>
												<%
													if ("true".equals((String) request.getAttribute("edit"))) {
												%>
												Edit Test For ${course.courseName }
												<%
													//System.out.println("course id isssssss"+session.getAttribute("courseId")+ && String.valueOf( session.getAttribute("courseId"))==null);
														}
												%>

												<c:if test="${not empty courseRecord.courseName }">
                                                                        Add Test for ${courseRecord.courseName}
                                                                        </c:if>
												<%--  <c:if test="${showWieghtageFor}">                                                              
                                                                              <div style="padding-top:5%;color:black;font-size:20px">
                                                                             <c:forEach var="showWieghtageForTest" items="${showWieghtageForTest}"
                                                                                                      varStatus="status">
                                                                                                      <span> || </span>
                                                                        <c:out value="${showWieghtageForTest.wieghtagetype}"/><span> - </span>
                                                                        <c:out value="${showWieghtageForTest.wieghtageassigned}%"/>
                                                                                                </c:forEach>
                                                                                                </div>
                                                                                                </c:if>
                                                                                               
                                                                       
                                                            </c:if>
                                                                       --%>
											</h5>
										</div>
									</div>
								</c:if>
								<c:if test="${empty courseRecord.courseName }">
                                                                        Add Test
                                                                        </c:if>
								<%--        <c:if test="${showWieghtageFor ne 'false'}">
                                                            <div style="color:black;font-size:20px">
                                                                        Wieghtage Not Assigned
                                                                                                </div>
                                                            </c:if>  --%>

								</h5>

							</div>
							<div class="x_content">
						<%-- <form:form action="addTest" method="post" modelAttribute="testConfiguration">
									<form:input type="hidden" path="testId" value="${ testId }" />
									
									<input type="button" id="addrows" name="addrows" class="addperson" 
        								value="Add Rows" onclick="add();">
        
									<span id="myspan"></span>
								</form:form> --%>
					<sec:authorize access="hasRole('ROLE_ADMIN')">
					<form:form id="form1" action="addTest" method="post"
							modelAttribute="testConfiguration">
							<form:input type="hidden" path="testId" value="${ testId }" />
							<div id="main">
								<input type="button" id="btAdd" value="Add Element"
									class="bt btn btn-large btn-primary" /> <input type="button"
									id="btRemove" value="Remove Element"
									class="bt btn btn-large btn-primary" /> <input type="button"
									id="btRemoveAll" value="Remove All"
									class="bt btn btn-large btn-primary" /><br />
							</div>
						</form:form>
					</sec:authorize>		
					<sec:authorize access="hasRole('ROLE_FACULTY')">		
						<form:form id="form1" action="addTest" method="post"
							modelAttribute="testConfiguration">
							<form:input type="hidden" path="testId" value="${ testId }" />
							
							<div class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
								<label class="textStrong">Configure Weightage For <span
									class="text-danger">*</span></label> <select id="weightageType"
									class="form-control facultyparameter" required="required">
									<option value="" <c:if test="${weightageTypes eq ''}">selected</c:if> >Select</option>
									<option value="questions" <c:if test="${weightageTypes eq 'questions' && weightageTypes ne ''}">selected</c:if> >Questions</option>
									<option value="poolQuestions" <c:if test="${weightageTypes eq 'poolQuestions' && weightageTypes ne ''}">selected</c:if>>Pool Questions</option>
								</select>
							</div>
							
							
							<br>

							<div id="weightageQuestion" class="d-none">
								<div id="main">

									<input type="button" id="btAdd" value="Add Element"
										class="bt btn btn-large btn-primary" /> <input type="button"
										id="btRemove" value="Remove Element"
										class="bt btn btn-large btn-primary" /> <input type="button"
										id="btRemoveAll" value="Remove All"
										class="bt btn btn-large btn-primary" /><br>

								</div>
							</div>
							<div id="weightagePoolQuestion" class="d-none">

								<input type="button" id="btAddPq" value="Add Element"
									class="bt btn btn-large btn-primary" /> 
									<input type="button"
									id="btRemovePq" value="Remove Element"
									class="bt btn btn-large btn-primary" /> 
									<input type="button"
									id="btRemoveAllPq" value="Remove All"
									class="bt btn btn-large btn-primary" />
							
								 <div id="testPoolDiv">
									<br>
									<c:if test="${edit eq 'Y'}">
								
									<c:forEach var="tpc" items="${TpcList}" varStatus="status">
										<div class="row">
											<div class="col-4">
												<select id="testPoolId${status.count}" name="testPoolId" class="form-control">
												<c:if test="${tpc.testPoolId eq  null }">
													<option value="">Select Test Pool</option>
												</c:if>
												<c:forEach var="pool" items="${testPoolsList}" varStatus="status">
													<c:if test="${tpc.testPoolId eq pool.id }">
														<option value="${pool.id}" selected>${pool.testPoolName}</option>
													</c:if>
													<c:if test="${tpc.testPoolId ne pool.id }">
														<option value="${pool.id}">${pool.testPoolName}</option>
													</c:if>
												</c:forEach>
												</select>
											</div>
											<div class="col-md-4 col-sm-6 col-xs-12 column">
												<input type=text class="input form-control" value = "${ tpc.marks }" id="tb${status.count}" name="marksPq" placeholder="Marks" />
											</div> 
											<div class="col-md-4 col-sm-6 col-xs-12 column"> 
												<input type=text class="input form-control" value = "${ tpc.noOfQuestion }" id="tbb${status.count}" name="noOfQuesPq" placeholder="no of question" /> 
											</div>
										</div>
									</c:forEach>
								
									</c:if>
									
								</div>
								<div><c:if test="${not empty TpcList}">
										<input type=button class="bt btn btn-large btn-success mt-3 mb-3" onclick="GetPoolTextValue()" id=submit value=Submit />
									</c:if></div> 
								
							</div>


						</form:form>
				</sec:authorize>
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
				<sec:authorize access="hasRole('ROLE_FACULTY')">
	         <jsp:include page="../common/newSidebar.jsp" />
	         </sec:authorize>
			<!-- SIDEBAR END -->
			<sec:authorize access="hasRole('ROLE_FACULTY')">
			<jsp:include page="../common/footer.jsp"/>
			</sec:authorize>
			<sec:authorize access="hasRole('ROLE_ADMIN')">
			<jsp:include page="../common/newAdminFooter.jsp"/>
			</sec:authorize>
				<!-- <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-json/2.6.0/jquery.json.min.js"></script>
			 -->
				<script>
					$(document)
							.ready(
									function() {
										
										var iCnt = 1;
										// CREATE A "DIV" ELEMENT AND DESIGN IT USING jQuery ".css()" CLASS.
										var container = $(
												document.createElement('div'))
												.css({
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
											<c:forEach var="testConfig" items="${TcList}">

											$(container)
													.append(
															'<div class="row"><div class="col-md-4 col-sm-6 col-xs-12 column"><input type=text class="input form-control" id=tb' + iCnt + ' ' +
	                    'name="marks" value = "${ testConfig.marks }" placeholder="Marks" /></div> <div class="col-md-4 col-sm-6 col-xs-12 column"> <input type=text class="input form-control" id=tbb' + iCnt + ' ' +
	                    'name="noOfQues" value = "${ testConfig.noOfQuestion }" placeholder="no of question" /> </div></div>');
											console.log('pqrs' + iCnt);

											if (iCnt == 1) {
												var divSubmit = $(document
														.createElement('div'));
												$(divSubmit)
														.append(
																'<input type=button class="bt btn btn-large btn-success"'
																		+ 'onclick="GetTextValue()"'
																		+ ' id=submit value=Submit />');
											}

											$('#main').after(container,
													divSubmit);
											iCnt = iCnt + 1;
											</c:forEach>
											
											
											
										}

										$('#btAdd')
												.click(
														function() {
															if (iCnt <= 30) {

																console
																		.log('abcd '
																				+ iCnt);

																// ADD TEXTBOX.
																$(container)
																		.append(
																				'<div class="row"><div class="col-md-4 col-sm-6 col-xs-12 column"><input type=text class="input form-control" id=tb' + iCnt + ' ' +
                    'name="marks" placeholder="Marks" /></div> <div class="col-md-4 col-sm-6 col-xs-12 column"> <input type=text class="input form-control" id=tbb' + iCnt + ' ' +
                    'name="noOfQues" placeholder="no of question" /> </div></div>');

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
																$('#main')
																		.after(
																				container,
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
																		.attr(
																				'class',
																				'bt-disable btn btn-large btn-primary');
																$('#btAdd')
																		.attr(
																				'disabled',
																				'disabled');
															}
														});

										// REMOVE ONE ELEMENT PER CLICK.
										$('#btRemove')
												.click(
														function() {
															if (iCnt != 0) {
																iCnt = iCnt - 1;
																$('#tb' + iCnt)
																		.remove();
																$('#tbb' + iCnt)
																		.remove();
																//iCnt = iCnt - 1;
																console
																		.log('remove '
																				+ iCnt);
															}

															if (iCnt == 1) {
																$(container)
																		.empty()
																		.remove();

																$('#submit')
																		.remove();
																$('#btAdd')
																		.removeAttr(
																				'disabled')
																		.attr(
																				'class',
																				'bt  btn btn-large btn-primary');
																iCnt = 1;
															}
														});

										// REMOVE ALL THE ELEMENTS IN THE CONTAINER.
										$('#btRemoveAll')
												.click(
														function() {
															$(container)
																	.empty()
																	.remove();

															$('#submit')
																	.remove();
															iCnt = 1;

															$('#btAdd')
																	.removeAttr(
																			'disabled')
																	.attr(
																			'class',
																			'bt  btn btn-large btn-primary');
														});

										

										console.log("iCnt--->"+iCnt)
										//NEW KAPIL
										
										var weightageType = $('#weightageType');
										if(weightageType.val() === "poolQuestions") {
											$("#weightagePoolQuestion").removeClass("d-none");
										}else if(weightageType.val() === "questions"){
											$("#weightageQuestion").removeClass("d-none");
										}
										weightageType.change(function(){
											if(weightageType.val() === "poolQuestions") {
												$("#weightagePoolQuestion").removeClass("d-none");
											}else if(weightageType.val() === "questions"){
												$("#weightageQuestion").removeClass("d-none");
											}
										})
										var dynRowCount = 0;
										if ("Y" == edit) {
											<c:forEach var="testPoolConfig" items="${TpcList}">
											dynRowCount = dynRowCount + 1;
											</c:forEach> 
											
											//Hiren
											
											 /* <c:forEach var="testPoolConfig" items="${TpcList}">

											$(container)
													.append(
															'<div class="row"><div class="col-4"><select id="testPoolId'+ dynRowCount +'" name="testPoolId" class="form-control">'+
															+'<c:if test="${tpc.testPoolId eq null }"><option value="">Select Test Pool</option></c:if>'+
															+'<c:forEach var="pool" items="${testPoolsList}" varStatus="status">'+
															+'<c:if test="${tpc.testPoolId eq pool.id }"><option value="${pool.id}" selected>${pool.testPoolName}</option></c:if>'+
															+'<c:if test="${tpc.testPoolId ne pool.id }"><option value="${pool.id}">${pool.testPoolName}</option></c:if>'+
															+'</c:forEach></select></div><div class="col-md-4 col-sm-6 col-xs-12 column">'+
															+'<input type=text class="input form-control" value = "${ tpc.marks }" id=tb'+ dynRowCount +' name="marksPq" placeholder="Marks" /></div>'+
															+' <div class="col-md-4 col-sm-6 col-xs-12 column"> <input type=text class="input form-control" value = "${ tpc.noOfQuestion }" id=tbb'+ dynRowCount +' name="noOfQuesPq" placeholder="no of question" /> </div></div>');
											
											console.log('pqrs' + iCnt);

											if (iCnt == 1) {
												var divSubmit = $(document
														.createElement('div'));
												$(divSubmit)
														.append(
																'<input type=button class="bt btn btn-large btn-success"'
																		+ 'onclick="GetTextValue()"'
																		+ ' id=submit value=Submit />');
											}

											$('#main').after(container,
													divSubmit);
											iCnt = iCnt + 1;
											</c:forEach>  */

										}
										console.log("dynRowCount"+dynRowCount);
										$("#btAddPq").click(function(){
											dynRowCount++;
											$("#testPoolDiv").append('<div class="row"><div class="col-4"><select id="testPoolId' + dynRowCount + '" name="testPoolId" class="form-control"><option>Select</option></select>'+
													'</div><div class="col-md-4 col-sm-6 col-xs-12 column"><input type=text class="input form-control" id=tb' + dynRowCount + ' ' +
								                    'name="marksPq" placeholder="Marks" /></div> <div class="col-md-4 col-sm-6 col-xs-12 column"> <input type=text class="input form-control" id=tbb' + dynRowCount + ' ' +
								                    'name="noOfQuesPq" placeholder="no of question" /> </div></div>');
											
											<c:forEach var="pool" items="${testPoolsList}"
												varStatus="status">
											$("#testPoolId"+ dynRowCount).append(new Option('${pool.testPoolName}','${pool.id}'));
											</c:forEach>
										
											if (dynRowCount == 1) {
												var divSubmit = $(document
														.createElement('div'));
												$(divSubmit)
														.append(
																'<br><input type=button class="bt btn btn-large btn-success"'
																		+ 'onclick="GetPoolTextValue()"'
																		+ ' id=submit value=Submit />');
											}

											// ADD BOTH THE DIV ELEMENTS TO THE "main" CONTAINER.
											$('#testPoolDiv').after(testPoolDiv,divSubmit);
										})
										// REMOVE ONE ELEMENT PER CLICK.
										$('#btRemovePq')
												.click(
														function() {
															if (dynRowCount != 0) {
																$('#testPoolId' + dynRowCount).remove();
																$('#tb' + dynRowCount).remove();
																$('#tbb' + dynRowCount).remove();
																dynRowCount = dynRowCount - 1;
																console
																		.log('remove '
																				+ dynRowCount);
															}

															if (dynRowCount == 0) {
																$('#testPoolDiv .row')
																		.empty()
																		.remove();

																$('#submit')
																		.remove();
																$('#btAdd')
																		.removeAttr(
																				'disabled')
																		.attr(
																				'class',
																				'bt  btn btn-large btn-primary');
																dynRowCount = 0;
															}
														});

										// REMOVE ALL THE ELEMENTS IN THE CONTAINER.
										$('#btRemoveAllPq')
												.click(
														function() {
															$('#testPoolDiv .row')
																	.empty()
																	.remove();

															$('#submit')
																	.remove();
															dynRowCount = 0;

															$('#btAdd')
																	.removeAttr(
																			'disabled')
																	.attr(
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
						var edit = '${ edit }';
						if ("Y" == edit) {
							console.log("EDIT--->");
							swal({
								  title: "Are you sure?",
								  text: "Once you submit, if any questions already configured or test allocated to student then it will be removed and you have re-configured questions and re-allocate test to student.",
								  icon: "warning",
								  buttons: true,
								  dangerMode: true,
								})
								.then((willDelete) => {
								  if (willDelete) {
									var maxMarks = ${TotalScore};
									var maxQues = ${maxQuestion};
									var id = ${testId};
			
									var hash = new Object();
									var marks_input = document.getElementsByName("marks");
									var noOfQues_input = document
											.getElementsByName("noOfQues");
									
									//alert(marks_input+noOfQues_input);
									var countMarks = 0;
			
									for (var i = 0; i < marks_input.length; i++) {
			
										hash[marks_input[i].value] = noOfQues_input[i].value;
			
										countMarks = countMarks
												+ (parseFloat(marks_input[i].value) * parseInt(noOfQues_input[i].value));
									}
									hash['testId'] = id;
			
									console.log(hash);
									console.log(id);
			
									var countQue = 0;
			
									for (var i = 0; i < noOfQues_input.length; i++) {
			
										countQue = countQue
												+ parseInt(noOfQues_input[i].value);
			
									}
			
									/* alert(countMarks);
									alert(countQue);
									alert(maxMarks);
									alert(maxQues); */
			
									if (countQue == maxQues) {
			
										if (countMarks == maxMarks) {
			
											$
													.ajax({
														type : "POST",
														contentType : 'application/json; charset=utf-8',
														/* dataType : 'json', */
														url : "${pageContext.request.contextPath}/addTestConfiguration",
														data : JSON.stringify(hash),
														success : function(result) {
			
															console.log('success : '
																	+ result);
															window.location.href = '${pageContext.request.contextPath}/viewTestDetails?testId='
																	+ id;
			
														},
														error : function(result) {
															console.log('error');
															console.log(result);
														}
													});
										} else {
			
											swal(
													'WARNING!',
													'Total Score of test is '
															+ maxMarks
															+ ', your configuration of test is not equal to total marks of test.',
													'warning');
			
										}
									} else {
			
										swal(
												'WARNING!',
												'Max Question To Show is '
														+ maxQues
														+ ', you are configuraing more questions. Make sure that total of configured question should match with max question to show ',
												'warning');
									}
						/* swal("Weightage configuration successfully created.", {
						      icon: "success",
						    }); */
						  }
						});
					}else{
						var maxMarks = ${TotalScore};
						var maxQues = ${maxQuestion};
						var id = ${testId};

						var hash = new Object();
						var marks_input = document.getElementsByName("marks");
						var noOfQues_input = document
								.getElementsByName("noOfQues");
						
						//alert(marks_input+noOfQues_input);
						var countMarks = 0;

						for (var i = 0; i < marks_input.length; i++) {

							hash[marks_input[i].value] = noOfQues_input[i].value;

							countMarks = countMarks
									+ (parseFloat(marks_input[i].value) * parseInt(noOfQues_input[i].value));
						}
						hash['testId'] = id;

						console.log(hash);
						console.log(id);
						console.log("Else--->");
						var countQue = 0;

						for (var i = 0; i < noOfQues_input.length; i++) {

							countQue = countQue
									+ parseInt(noOfQues_input[i].value);

						}

						/* alert(countMarks);
						alert(countQue);
						alert(maxMarks);
						alert(maxQues); */

						if (countQue == maxQues) {

							if (countMarks == maxMarks) {

								$
										.ajax({
											type : "POST",
											contentType : 'application/json; charset=utf-8',
											/* dataType : 'json', */
											url : "${pageContext.request.contextPath}/addTestConfiguration",
											data : JSON.stringify(hash),
											success : function(result) {

												console.log('success : '
														+ result);
												window.location.href = '${pageContext.request.contextPath}/viewTestDetails?testId='
														+ id;

											},
											error : function(result) {
												console.log('error');
												console.log(result);
											}
										});
							} else {

								swal(
										'WARNING!',
										'Total Score of test is '
												+ maxMarks
												+ ', your configuration of test is not equal to total marks of test.',
										'warning');

							}
						} else {

							swal(
									'WARNING!',
									'Max Question To Show is '
											+ maxQues
											+ ', you are configuraing more questions. Make sure that total of configured question should match with max question to show ',
									'warning');
						}
					}
					}

					$(function() {

						$('#weightageType').on('change', function() {

							var weightageType = $('#weightageType').val();
							//alert(weightageType);
							if (weightageType == 'questions') {
								$('#weightageQuestion').show();
								$('#weightagePoolQuestion').hide();
							} else if (weightageType == 'poolQuestions') {
								$('#weightagePoolQuestion').show();
								$('#weightageQuestion').hide();
							} else {
								$('#weightagePoolQuestion').hide();
								$('#weightageQuestion').hide();
							}

						});
					});
					
					function GetPoolTextValue() {
						var edit = '${ edit }';
						if ("Y" == edit) {
						swal({
							  title: "Are you sure?",
							  text: "Once you submit, if any questions already configured or test allocated to student then it will be removed and you have re-configured questions and re-allocate test to student.",
							  icon: "warning",
							  buttons: true,
							  dangerMode: true,
							})
							.then((willDelete) => {
							  if (willDelete) {
								  var maxMarks = ${TotalScore};
									var maxQues = ${maxQuestion};
									var id = ${testId};
									
									var marks_input = document.getElementsByName("marksPq");
									var noOfQues_input = document
											.getElementsByName("noOfQuesPq");
									var testPoolId = document.getElementsByName("testPoolId");					

									//var hash = new Object();
									var array =new Array();
									var countMarks = 0;
									var countQue = 0;
									
									for (var i = 0; i < marks_input.length; i++) {
										
										//hash[marks_input[i].value] = noOfQues_input[i].value;
										var mi = marks_input[i].value;
										var noi = noOfQues_input[i].value;
										var tpi = testPoolId[i].value;
										
										var arrayItem = {'testId': id, 'testPoolId': tpi,'noOfQuestion': noi,'marks':mi};
										//arrayItem.push(tpi);
										//arrayItem.push(mi);
										//arrayItem.push(noi);
										
										array.push(arrayItem);
										
										countMarks = countMarks + (parseFloat(marks_input[i].value) * parseInt(noOfQues_input[i].value));
										countQue = countQue + parseInt(noOfQues_input[i].value);

										
									}
									console.log(JSON.stringify(array));
									var json =JSON.stringify(array);
							         console.log('json--->'+json);
									
									if (countQue == maxQues) {

										if (countMarks == maxMarks) {
											//alert("Insert");
												 $.ajax({
														type : "POST",
														dataType: 'json',
														url : "${pageContext.request.contextPath}/addPoolTestConfiguration",
														data : {
												    		testId: id,
												    		testPoolConfiguration: json
												    	},
														success : function(result) {

															console.log('success : '
																	+ result);
															window.location.href = '${pageContext.request.contextPath}/viewTestDetails?testId='
																	+ id;

														},
														error : function(result) {
															console.log('error');
															console.log(result);
														}
													}); 
										} else {

											swal(
													'WARNING!',
													'Total Score of test is '
															+ maxMarks
															+ ', your configuration of test is not equal to total marks of test.',
													'warning');

										}
									} else {

										swal(
												'WARNING!',
												'Max Question To Show is '
														+ maxQues
														+ ', you are configuraing more questions. Make sure that total of configured question should match with max question to show ',
												'warning');
									}
								  
							    swal("Weightage configuration successfully created.", {
							      icon: "success",
							    });
							  }
							});
						}else{
							var maxMarks = ${TotalScore};
							var maxQues = ${maxQuestion};
							var id = ${testId};
							
							var marks_input = document.getElementsByName("marksPq");
							var noOfQues_input = document
									.getElementsByName("noOfQuesPq");
							var testPoolId = document.getElementsByName("testPoolId");					

							//var hash = new Object();
							var array =new Array();
							var countMarks = 0;
							var countQue = 0;
							
							for (var i = 0; i < marks_input.length; i++) {
								
								//hash[marks_input[i].value] = noOfQues_input[i].value;
								var mi = marks_input[i].value;
								var noi = noOfQues_input[i].value;
								var tpi = testPoolId[i].value;
								
								var arrayItem = {'testId': id, 'testPoolId': tpi,'noOfQuestion': noi,'marks':mi};
								//arrayItem.push(tpi);
								//arrayItem.push(mi);
								//arrayItem.push(noi);
								
								array.push(arrayItem);
								
								countMarks = countMarks + (parseFloat(marks_input[i].value) * parseInt(noOfQues_input[i].value));
								countQue = countQue + parseInt(noOfQues_input[i].value);

								
							}
							console.log(JSON.stringify(array));
							var json =JSON.stringify(array);
					         console.log('json--->'+json);
							
							if (countQue == maxQues) {

								if (countMarks == maxMarks) {
									//alert("Insert");
										 $.ajax({
												type : "POST",
												dataType: 'json',
												url : "${pageContext.request.contextPath}/addPoolTestConfiguration",
												data : {
										    		testId: id,
										    		testPoolConfiguration: json
										    	},
												success : function(result) {

													console.log('success : '
															+ result);
													window.location.href = '${pageContext.request.contextPath}/viewTestDetails?testId='
															+ id;

												},
												error : function(result) {
													console.log('error');
													console.log(result);
												}
											}); 
								} else {

									swal(
											'WARNING!',
											'Total Score of test is '
													+ maxMarks
													+ ', your configuration of test is not equal to total marks of test.',
											'warning');

								}
							} else {

								swal(
										'WARNING!',
										'Max Question To Show is '
												+ maxQues
												+ ', you are configuraing more questions. Make sure that total of configured question should match with max question to show ',
										'warning');
							}
						}
					}
					
					
					
				</script>