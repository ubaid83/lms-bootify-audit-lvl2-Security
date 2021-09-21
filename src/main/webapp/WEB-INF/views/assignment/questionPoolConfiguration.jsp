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
							<form:form id="form1" action="addAssignmentQuestionPoolConfiguration" method="post"
								modelAttribute="assignmentConfiguration">
								<form:input type="hidden" path="assignmentId" value="${ assignmentId }" />
								<!-- <div id="main">
									<input type="button" id="btAdd" value="Add Element"
										class="bt btn btn-large btn-primary" /> <input type="button"
										id="btRemove" value="Remove Element"
										class="bt btn btn-large btn-primary" /> <input type="button"
										id="btRemoveAll" value="Remove All"
										class="bt btn btn-large btn-primary" /><br />
								</div> -->
								<div id="weightagePoolQuestion">
								<input type="button" id="btAddPq" value="Add Element"
									class="bt btn btn-large btn-primary" /> 
									<input type="button"
									id="btRemovePq" value="Remove Element"
									class="bt btn btn-large btn-primary" /> 
									<input type="button"
									id="btRemoveAllPq" value="Remove All"
									class="bt btn btn-large btn-primary" />
								
								<div id="assignmentPoolDiv">
									<br>
									<c:if test="${edit eq 'Y'}">
								
									<c:forEach var="apc" items="${apcList}" varStatus="status">
									
										<div class="row">
											<div class="col-4">
												<select id="testPoolId${status.count}" name="testPoolId" class="form-control">
												<c:if test="${apc.testPoolId eq  null }">
													<option value="">Select Test Pool</option>
												</c:if>
												<c:forEach var="pool" items="${testPoolsList}">
													<c:if test="${apc.testPoolId eq pool.id }">
														<option value="${pool.id}" selected>${pool.testPoolName}</option>
													</c:if>
													<c:if test="${apc.testPoolId ne pool.id }">
														<option value="${pool.id}">${pool.testPoolName}</option>
													</c:if>
												</c:forEach>
												</select>
												
											</div> 
											<div class="col-md-4 col-sm-6 col-xs-12 column"> 
												<input type=text class="input form-control" value = "${ apc.noOfQuestion }" id="tbb${status.count}" name="noOfQuesPq" placeholder="no of question" /> 
											</div>
										</div>
									</c:forEach>
								
									</c:if>
									
								</div>
								<div><c:if test="${not empty apcList}">
										<input type=button class="bt btn btn-large btn-success mt-3 mb-3" onclick="GetPoolTextValue()" id=submit value=Submit />
									</c:if></div> 
									
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
	<script src="<c:url value="/resources/js/sweetalert.min.js" />"></script>
<script>

	$(document).ready(function() {

		var dynRowCount = 0;
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
			<c:forEach var="testPoolConfig" items="${apcList}">
			dynRowCount = dynRowCount + 1;
			</c:forEach> 

		}
		console.log("dynRowCount"+dynRowCount);
		$("#btAddPq").click(function(){
			dynRowCount++;
			$("#assignmentPoolDiv").append('<div class="row"><div class="col-4"><select id="testPoolId' + dynRowCount + '" name="testPoolId" class="form-control"><option>Select</option></select>'+
					'</div><div class="col-md-4 col-sm-6 col-xs-12 column"> <input type=text class="input form-control" id=tbb' + dynRowCount + ' ' +
                    'name="noOfQuesPq" placeholder="no of question" /> </div></div>');
			
			<c:forEach var="pool" items="${testPoolsList}" varStatus="status">
				$("#testPoolId"+ dynRowCount).append(new Option('${pool.testPoolName}','${pool.id}'));
			</c:forEach>
		
			if (dynRowCount == 1) {
				var divSubmit = $(document.createElement('div'));
				$(divSubmit).append('<br><input type=button class="bt btn btn-large btn-success"'
					+ 'onclick="GetPoolTextValue()"'
					+ ' id=submit value=Submit />');
			}

			// ADD BOTH THE DIV ELEMENTS TO THE "main" CONTAINER.
			$('#assignmentPoolDiv').after(assignmentPoolDiv,divSubmit);
		})
		// REMOVE ONE ELEMENT PER CLICK.
		$('#btRemovePq').click(function() {
			if (dynRowCount != 0) {
				$('#testPoolId' + dynRowCount).remove();
				$('#tbb' + dynRowCount).remove();
				console.log('remove '+ dynRowCount);
				dynRowCount = dynRowCount - 1;
				
			}

			if (dynRowCount == 0) {
				$('#assignmentPoolDiv .row').empty().remove();
				$('#submit').remove();
				$('#btAdd').removeAttr('disabled').attr('class','bt  btn btn-large btn-primary');
				dynRowCount = 0;
			}
		});

		// REMOVE ALL THE ELEMENTS IN THE CONTAINER.
		$('#btRemoveAllPq').click(function() {
			$('#assignmentPoolDiv .row').empty().remove();
			$('#submit').remove();
			dynRowCount = 0;
			$('#btAdd').removeAttr('disabled').attr('class','bt  btn btn-large btn-primary');
		});


		
	});

	function GetPoolTextValue() {
		var edit = '${ edit }';
		if ("Y" == edit) {
		swal({
			  title: "Are you sure?",
			  text: "Once you submit, if any questions already configured or Assignment allocated to student then it will be removed and you have to re-configured questions and re-allocate assignment to student.",
			  icon: "warning",
			  buttons: true,
			  dangerMode: true,
			})
			.then((willDelete) => {
			  if (willDelete) {
				  var maxMarks = ${TotalScore};
					var maxQues = ${maxQuestion};
					var id = ${assignmentId};
					
					
					var noOfQues_input = document.getElementsByName("noOfQuesPq");
					var testPoolId = document.getElementsByName("testPoolId");					

					//var hash = new Object();
					var array =new Array();
					var countMarks = 0;
					var countQue = 0;
					
					for (var i = 0; i < testPoolId.length; i++) {
						
						var noi = noOfQues_input[i].value;
						var tpi = testPoolId[i].value;
						
						var arrayItem = {'assignmentId': id, 'testPoolId': tpi,'noOfQuestion': noi};
						array.push(arrayItem);
						
						countQue = countQue + parseInt(noOfQues_input[i].value);

						
					}
					console.log(JSON.stringify(array));
					var json =JSON.stringify(array);
			         console.log('json--->'+json);
					
					if (countQue == maxQues) {

								 $.ajax({
										type : "POST",
										dataType: 'json',
										url : "${pageContext.request.contextPath}/addAssignmentQuestionPoolConfiguration",
										data : {
											assignmentId: id,
								    		questionConfiguration: json
								    	},
										success : function(result) {
											swal({
												text : "Question Pool Configuration is sucessfully configured.",
												type : "success"
											})
											.then(function() {
												window.location.href = '${pageContext.request.contextPath}/viewAssignment?id='+ id;
											});
										},
										error : function(result) {
											console.log('error');
											console.log(result);
											swal("Error!", "Something went wrong.", "error");
										}
									}); 
					
					} else {
						alert(maxQues);
						swal("WARNING!","Max Question To Show is "
								+ maxQues
								+ ", you are configuraing more questions. Make sure that total of configured question should match with max question to show ",
								"warning");
					}
				  
			    /* swal("Question Pool Configuration is sucessfully configured.", {
			      icon: "success",
			    }); */
			  }
			});
		}else{
			var maxMarks = ${TotalScore};
			var maxQues = ${maxQuestion};
			var id = ${assignmentId};
			
			
			var noOfQues_input = document.getElementsByName("noOfQuesPq");
			var testPoolId = document.getElementsByName("testPoolId");					

			var array =new Array();
			var countMarks = 0;
			var countQue = 0;
			
			for (var i = 0; i < testPoolId.length; i++) {
				
				var noi = noOfQues_input[i].value;
				var tpi = testPoolId[i].value;
				
				var arrayItem = {'assignmentId': id, 'testPoolId': tpi,'noOfQuestion': noi};
				array.push(arrayItem);
				
				countQue = countQue + parseInt(noOfQues_input[i].value);

				
			}
			console.log(JSON.stringify(array));
			var json =JSON.stringify(array);
	         console.log('json--->'+json);
			
			if (countQue == maxQues) {
				 $.ajax({
						type : "POST",
						dataType: 'json',
						url : "${pageContext.request.contextPath}/addAssignmentQuestionPoolConfiguration",
						data : {
				    		assignmentId: id,
				    		questionConfiguration: json
				    	},
						success : function(result) {
							swal({
								text : "Question Pool Configuration is sucessfully configured.",
								type : "success"
							})
							.then(function() {
								window.location.href = '${pageContext.request.contextPath}/viewAssignment?id='+ id;
							});
						},
						error : function(result) {
							swal("Error!", "Something went wrong.", "error");
							console.log('error');
							console.log(result);
						}
					}); 
				
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