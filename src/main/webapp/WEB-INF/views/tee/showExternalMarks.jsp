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
	<jsp:include page="../common/newLeftNavbar.jsp" />
	<jsp:include page="../common/newLeftSidebar.jsp" />
	<jsp:include page="../common/rightSidebar.jsp" />

	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper">

		<jsp:include page="../common/newTopHeader.jsp" />

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

							<li class="breadcrumb-item active" aria-current="page">External
								Marks</li>
						</ol>
					</nav>

					<jsp:include page="../common/alert.jsp" />


					<!-- Input Form Panel -->
					<div class="card bg-white border">
						<div class="card-body">
							<div class="row">
								<div class="col-xs-12 col-sm-12">
									<div class="x_panel">
										<div class="x_title">
											<h2>External Marks</h2>
											<div class="clearfix"></div>
										</div>
										<div class="x_content">
											<div class="table-responsive testAssignTable">
												<table class="table table-hover" id="viewFeedbackTable">
													<thead>
														<tr>
															<th>Subject Name</th>
															<th>Year</th>
															<th>Semester</th>
															<th>Marks Obained</th>
															<th>Remarks</th>
															<th>Status</th>
															<th>Published Date</th>
															<sec:authorize access="hasRole('ROLE_STUDENT')"><th>Due Date For Query-Raise</th></sec:authorize>
															<th>Action</th>
														</tr>
													</thead>
													<tbody>
                                                       <c:forEach var="tee" items="${totalMarks}"
															varStatus="status">
                                                        <tr>
                                                            <td>${tee.moduleName}</td>
                                                            <td>${tee.acadYear}</td>
                                                            <td>${tee.acadSession}</td>
                                                            <td>${tee.teeTotalMarks}/${tee.externalMarks}</td>
                                                            <td>${tee.remarks}</td>
                                                            <td>${tee.passFailStatus}</td>
                                                            <td>${tee.publishedDate}</td>
                                                            <td>${tee.dueDate}</td>
                                                            <td><c:if test="${dateSpanMap[tee.teeIdStr] eq 'showButton'}">
																		<c:choose>
																			<c:when test="${tee.isQueryRaise eq 'Y'}">
																			
																					<c:if test="${not empty raiseQueryStatus[tee.teeIdStr] && empty tee.isQueryApproved }">
																					<input data-toggle="modal" id="raiseQHideAlt${tee.teeIdStr}"
																					class="btn btn-sm btn-danger text-primary"
																					data-target="#showRaisedReq${status.count}" type="button"
																					value="Query Raised"/>
																					</c:if>
																					
																					
																					<c:if test="${raiseQueryStatus[tee.teeIdStr].isReEvaluated eq 'Y' && tee.isQueryApproved eq 'Y'}">
																					<input data-toggle="modal" id="raiseQHideAlt${tee.teeIdStr}"
																					class="btn btn-sm btn-danger text-primary"
																					data-target="#showRaisedReq${status.count}" type="button"
																					value="Query Approved and Marks Evaluated"/>
																					</c:if>
																					<c:if test="${raiseQueryStatus[tee.teeIdStr].isReEvaluated eq 'Y' && tee.isQueryApproved eq 'N'}">
																					<input data-toggle="modal" id="raiseQHideAlt${tee.teeIdStr}"
																					class="btn btn-sm btn-danger text-primary"
																					data-target="#showRaisedReq${status.count}" type="button"
																					value="Query rejected"/>
																					</c:if>
																					
																			</c:when>
																			<c:otherwise>
																				
																				<input data-toggle="modal" id="raiseQ${tee.teeIdStr}"
																					class="btn btn-sm btn-danger text-primary"
																					data-target="#teeId${status.count}" type="button"
																					value="Raise Query" />

																				
																				
																				<input data-toggle="modal" id="raiseQHide${tee.teeIdStr}"
																					class="btn btn-sm btn-danger text-primary"
																					data-target="#showRaisedReq${status.count}" type="button"
																					value="Query Raised" style="display: none"/>
																				
																			</c:otherwise>
																		</c:choose>
																	</c:if> <c:if
																		test="${dateSpanMap[tee.teeIdStr] eq 'disableButton'}">
																		<button class="btn btn-sm btn-danger disabled">Raise
																			Request</button>

																	</c:if></td>
                                                      <td>
																	<div id="modalTestPassword">
																		<div class="modal fade fnt-13"
																			id="teeId${status.count}" tabindex="-1"
																			role="dialog" aria-labelledby="query"
																			aria-hidden="true">
																			<div
																				class="modal-dialog modal-dialog-centered modal-dialog-scrollable"
																				role="document">
																				<div class="modal-content">
																					<div class="modal-header text-white">
																						<h6 class="modal-title" id="query">Mention Your Query</h6>
																						<button type="button" class="close"
																							data-dismiss="modal" aria-label="Close">
																							<span aria-hidden="true">&times;</span>
																						</button>
																					</div>
																					<div class="modal-body pt-0 bg-light text-center">
																						<h6 class="mt-2 text-capitalize">${tee.moduleName}</h6>
																						<hr />
																						<p>
																							Query: <span
																								class="text-danger">*
																						</p>
																						<textarea rows="5" cols="30" id="teeQueryText${tee.teeIdStr}"
																						required="required" class="w-100" maxlength="200">
																						</textarea>

																					</div>
																					<div class="modal-footer text-center">
																						<a class="btn btn-primary text-white"
																							onclick="raiseQuery('${tee.teeIdStr}')">Submit</a>
																						<button type="button" class="btn btn-modalClose"
																							data-dismiss="modal">Cancel</button>
																					</div>
																				</div>
																			</div>
																		</div>
																	</div>
																	
																	<td>
																	<div id="modalTestPassword">
																		<div class="modal fade fnt-13"
																			id="showRaisedReq${status.count}" tabindex="-1"
																			role="dialog" aria-labelledby="query"
																			aria-hidden="true">
																			<div
																				class="modal-dialog modal-dialog-centered modal-dialog-scrollable"
																				role="document">
																				<div class="modal-content">
																					<div class="modal-header text-white">
																						<h6 class="modal-title" id="query">Your Query</h6>
																						<button type="button" class="close"
																							data-dismiss="modal" aria-label="Close">
																							<span aria-hidden="true">&times;</span>
																						</button>
																					</div>
																					<div class="modal-body pt-0 bg-light text-center">
																						<h6 class="mt-2 text-capitalize">${tee.moduleName}</h6>
																						<hr />
																						<p>
																							Query: <span
																								class="text-danger">*
																						</p>
																						<c:choose>
																						<c:when test="${tee.isQueryRaise eq 'Y'}">
																						<textarea rows="5" cols="30" id="teeQueryTextDB${tee.teeIdStr}"
																						required="required" class="w-100" disabled="disabled">${tee.query}
																						</textarea>
																						</c:when>
																						<c:otherwise>
																						<textarea rows="5" cols="30" id="teeQueryTextDB${tee.teeIdStr}"
																						required="required" class="w-100" disabled="disabled">
																						</textarea>
																						</c:otherwise>
																						</c:choose>

																					</div>
																					<div class="modal-footer text-center">
																						
																						<button type="button" class="btn btn-modalClose"
																							data-dismiss="modal">Cancel</button>
																					</div>
																				</div>
																			</div>
																		</div>
																	</div>

																</td>
                                                        </tr>
                                                        </c:forEach>
													</tbody>
												</table>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>

					<!-- Results Panel -->


					<%-- 	<jsp:include page="../common/paginate.jsp">
							<jsp:param name="baseUrl" value="searchFacultyGroups" />
						</jsp:include> --%>

					<!-- /page content: END -->

				</div>

				<!-- SIDEBAR START -->
				<jsp:include page="../common/newSidebar.jsp" />
				<!-- SIDEBAR END -->
				<jsp:include page="../common/footer.jsp" />
				<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
				<script>
					function raiseQuery(teeId){
						var query = $('#teeQueryText'+teeId).val();
						query = query.replace(/[^a-zA-Z ]/g, "");
						if(query.trim()==''){
                            swal('mention your query!!!!');
                      }else{
                            swal({
                                      title: "Are you sure?",
                                      text: "You will not be able to edit the query once saved!!",
                                      icon: "warning",
                                      buttons: ['Cancel','Confirm']
                                  }).then((confirm) => {
                                        if(confirm){  
                                               $(".newLoaderWrap").css('display','block');
                                                    $
                                                                .ajax({
                                                                      type : 'GET',
                                                                      url : '${pageContext.request.contextPath}/raiseQueryTeeForStudent?'
                                                                                  + 'id='
                                                                                  + teeId
                                                                                  + '&query='
                                                                                  + query,
                                                                      success : function(data) {
                                                                    	  if(data=='validationError'){
                                                                    		  $(".newLoaderWrap").css('display','none');
                                                                              swal('Special characters are not allowed to enter except underscore(_) and hyphen(-)');
                                                                    	  } else {
                                                                            $(".newLoaderWrap").css('display','none');
                                                                            $(
                                                                                        '#raiseQHide'
                                                                                                    + teeId)
                                                                                        .show();
                                                                            $('#raiseQ' + teeId)
                                                                                        .hide();
                                                                            $('.btn-modalClose')
                                                                                        .trigger(
                                                                                                    'click');
                                                                            
                                                                            console
                                                                                        .log('teeQueryTextDB'
                                                                                                    + teeId
                                                                                                    + '--?'
                                                                                                    + query);
                                                                            document
                                                                                        .getElementById('teeQueryTextDB'
                                                                                                    + teeId).value = query;
                                                                            swal('query raised successfully');
                                                                            //$("#raiseQ").addClass('disabled');
                                                                    	  }
                                                                      },
                                                                      error : function() {
                                                                            $(".newLoaderWrap").css('display','none');
                                                                            swal('Error in raising query');
                                                                      }
                                                                });
                                        }
                   });
                            
                            
                      
                      }

                }

					
				</script>