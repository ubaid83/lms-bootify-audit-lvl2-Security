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

							<li class="breadcrumb-item active" aria-current="page">Internal
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
											<h2>Internal Marks</h2>
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
															<c:forEach var="components" items="${componentsMap}">
																<th>${components.value}</th>
															</c:forEach>
															<th>Marks Obained</th>
															<th>Remarks</th>
															<th>Status</th>
															<th>Published Date</th>
															<sec:authorize access="hasRole('ROLE_STUDENT')">
																<th>Due Date For Query-Raise</th>
															</sec:authorize>

															<th>Action</th>

														</tr>
													</thead>
													<tbody>


														<c:forEach var="ica" items="${totalMarks}"
															varStatus="status">
															<tr>
																<td>${ica.moduleName}</td>
																<td>${ica.acadYear}</td>

																<td>${ica.acadSession}</td>
																<c:set var="icaComp" value="${mapComponent[ica.icaId]}" />
																<c:forEach var="components" items="${componentsMap}">

																	<td>${icaComp[components.key]}</td>

																</c:forEach>
																<c:if test="${ica.isComponentMark ne 'Y'}">
																	<td>${ica.icaTotalMarks}/${ica.internalMarks}</td>
																</c:if>
																<c:if test="${ica.isComponentMark eq 'Y'}">
																	<td>NA</td>
																</c:if>

																<td>${ica.remarks}</td>
																<c:if test="${ica.isComponentMark ne 'Y'}">
																	<td>${ica.passFailStatus}</td>
																</c:if>
																<c:if test="${ica.isComponentMark eq 'Y'}">
																	<td>NA</td>
																</c:if>
																<td>${ica.publishedDate}</td>
																<td>${ica.dueDate}</td>
																<td><c:choose>
																		<c:when test="${ica.isComponentMark ne 'Y'}">
																			<c:if
																				test="${dateSpanMap[ica.icaId] eq 'showButton'}">
																				<c:choose>
																					<%-- <c:when test="${ica.isAbsent eq 'Y'}">
																				<button class="btn btn-sm btn-danger disabled" onclick="return alert('cannot raise a request you are marked as an absent')">Cannot Raise</button>
																			</c:when> --%>
																					<c:when test="${ica.isQueryRaise eq 'Y'}">
																						<%--<c:if test="${not empty raiseQueryStatus[ica.icaId] && (raiseQueryStatus[ica.icaId].isApproved eq 'N' || empty raiseQueryStatus[ica.icaId].isApproved) }">
																					<input data-toggle="modal" id="raiseQHideAlt${ica.icaId}"
																					class="btn btn-sm btn-danger text-primary"
																					data-target="#showRaisedReq${status.count}" type="button"
																					value="Query Raised"/>
																					</c:if>--%>

																						<c:if
																							test="${not empty raiseQueryStatus[ica.icaId] 
																							&& raiseQueryStatus[ica.icaId].isReEvaluated ne 'Y' || ica.isQueryApproved eq null}">
																							<input data-toggle="modal"
																								id="raiseQHideAlt${ica.icaId}"
																								class="btn btn-sm btn-danger text-primary"
																								data-target="#showRaisedReq${status.count}"
																								type="button" value="Query Raised" />
																						</c:if>

																						<%-- <c:if test="${raiseQueryStatus[ica.icaId].isApproved eq 'Y' && empty raiseQueryStatus[ica.icaId].isReEvaluated}">
																					<a href="" title="Query Approved"><i class="fa fa-users fa-lg"></i></a>
																					<input data-toggle="modal" id="raiseQHideAlt${ica.icaId}"
																					class="btn btn-sm btn-danger text-primary"
																					data-target="#showRaisedReq${status.count}" type="button"
																					value="Query Approved"/>
																					</c:if> --%>

																						<%-- 																					<c:if test="${raiseQueryStatus[ica.icaId].isReEvaluated eq 'Y'}">
																					<input data-toggle="modal" id="raiseQHideAlt${ica.icaId}"
																					class="btn btn-sm btn-danger text-primary"
																					data-target="#showRaisedReq${status.count}" type="button"
																					value="Marks Evaluated"/>
																					</c:if>
 --%>



																						<c:if
																							test="${raiseQueryStatus[ica.icaId].isReEvaluated eq 'Y' && ica.isQueryApproved eq 'Y'}">
																							<input data-toggle="modal"
																								id="raiseQHideAlt${ica.icaId}"
																								class="btn btn-sm btn-danger text-primary"
																								data-target="#showRaisedReq${status.count}"
																								type="button"
																								value="Query Approved and Marks Evaluated" />
																						</c:if>
																						<c:if
																							test="${raiseQueryStatus[ica.icaId].isReEvaluated eq 'Y' && ica.isQueryApproved eq 'N'}">
																							<input data-toggle="modal"
																								id="raiseQHideAlt${ica.icaId}"
																								class="btn btn-sm btn-danger text-primary"
																								data-target="#showRaisedReq${status.count}"
																								type="button" value="Query rejected" />
																						</c:if>

																					</c:when>

																					<c:otherwise>
																						<%-- <button class="btn btn-sm btn-danger" id="raiseQ"
																					value="${ica.icaId}">Raise Request</button> --%>
																						<input data-toggle="modal" id="raiseQ${ica.icaId}"
																							class="btn btn-sm btn-danger text-primary"
																							data-target="#icaId${status.count}" type="button"
																							value="Raise Query" />

																						<%-- <button class="btn btn-sm btn-danger disabled"
																					id="raiseQHide" value="${ica.icaId}"
																					style="display: none">Query Raised</button> --%>

																						<input data-toggle="modal"
																							id="raiseQHide${ica.icaId}"
																							class="btn btn-sm btn-danger text-primary"
																							data-target="#showRaisedReq${status.count}"
																							type="button" value="Query Raised"
																							style="display: none" />

																					</c:otherwise>
																				</c:choose>
																			</c:if>
																			<c:if
																				test="${dateSpanMap[ica.icaId] eq 'disableButton'}">
																				<button class="btn btn-sm btn-danger disabled">Raise
																					Request</button>

																			</c:if>
																		</c:when>
																		<c:otherwise>
																				
																		<c:if
																				test="${dateSpanMap[ica.pageKey] eq 'showButton'}">
																				<c:choose>
																					<%-- <c:when test="${ica.isAbsent eq 'Y'}">
																				<button class="btn btn-sm btn-danger disabled" onclick="return alert('cannot raise a request you are marked as an absent')">Cannot Raise</button>
																			</c:when> --%>
																					<c:when test="${ica.isQueryRaise eq 'Y'}">
																						<%--<c:if test="${not empty raiseQueryStatus[ica.icaId] && (raiseQueryStatus[ica.icaId].isApproved eq 'N' || empty raiseQueryStatus[ica.icaId].isApproved) }">
																					<input data-toggle="modal" id="raiseQHideAlt${ica.icaId}"
																					class="btn btn-sm btn-danger text-primary"
																					data-target="#showRaisedReq${status.count}" type="button"
																					value="Query Raised"/>
																					</c:if>--%>

																						<c:if
																							test="${not empty raiseQueryStatus[ica.pageKey]
																							&& raiseQueryStatus[ica.pageKey].isReEvaluated ne 'Y'  || ica.isQueryApproved eq null}">
																							<input data-toggle="modal"
																								id="raiseQHideAlt${ica.icaId}${ica.compId}"
																								class="btn btn-sm btn-danger text-primary"
																								data-target="#showRaisedReq${status.count}"
																								type="button" value="Query Raised" />
																						</c:if>

																						<%-- <c:if test="${raiseQueryStatus[ica.icaId].isApproved eq 'Y' && empty raiseQueryStatus[ica.icaId].isReEvaluated}">
																					<a href="" title="Query Approved"><i class="fa fa-users fa-lg"></i></a>
																					<input data-toggle="modal" id="raiseQHideAlt${ica.icaId}"
																					class="btn btn-sm btn-danger text-primary"
																					data-target="#showRaisedReq${status.count}" type="button"
																					value="Query Approved"/>
																					</c:if> --%>

																						<%-- 																					<c:if test="${raiseQueryStatus[ica.icaId].isReEvaluated eq 'Y'}">
																					<input data-toggle="modal" id="raiseQHideAlt${ica.icaId}"
																					class="btn btn-sm btn-danger text-primary"
																					data-target="#showRaisedReq${status.count}" type="button"
																					value="Marks Evaluated"/>
																					</c:if>
 --%>



																						<c:if
																							test="${raiseQueryStatus[ica.pageKey].isReEvaluated eq 'Y' && ica.isQueryApproved eq 'Y'}">
																							<input data-toggle="modal"
																								id="raiseQHideAlt${ica.icaId}${ica.compId}"
																								class="btn btn-sm btn-danger text-primary"
																								data-target="#showRaisedReq${status.count}"
																								type="button"
																								value="Query Approved and Marks Evaluated" />
																						</c:if>
																						<c:if
																							test="${raiseQueryStatus[ica.pageKey].isReEvaluated eq 'Y' && ica.isQueryApproved eq 'N'}">
																							<input data-toggle="modal"
																								id="raiseQHideAlt${ica.icaId}${ica.compId}"
																								class="btn btn-sm btn-danger text-primary"
																								data-target="#showRaisedReq${status.count}"
																								type="button" value="Query rejected" />
																						</c:if>

																					</c:when>

																					<c:otherwise>
																						<%-- <button class="btn btn-sm btn-danger" id="raiseQ"
																					value="${ica.icaId}">Raise Request</button> --%>
																						<input data-toggle="modal"
																							id="raiseQ${ica.icaId}${ica.compId}"
																							class="btn btn-sm btn-danger text-primary"
																							data-target="#icaId${status.count}" type="button"
																							value="Raise Query" />

																						<%-- <button class="btn btn-sm btn-danger disabled"
																					id="raiseQHide" value="${ica.icaId}"
																					style="display: none">Query Raised</button> --%>

																						<input data-toggle="modal"
																							id="raiseQHide${ica.icaId}${ica.compId}"
																							class="btn btn-sm btn-danger text-primary"
																							data-target="#showRaisedReq${status.count}"
																							type="button" value="Query Raised"
																							style="display: none" />

																					</c:otherwise>
																				</c:choose>
																			</c:if>
																			<c:if
																				test="${dateSpanMap[ica.pageKey] eq 'disableButton'}">
																				<button class="btn btn-sm btn-danger disabled">Raise
																					Request</button>

																			</c:if>


																		</c:otherwise>
																	</c:choose></td>
																<td>
																	<div id="modalTestPassword">
																		<div class="modal fade fnt-13"
																			id="icaId${status.count}" tabindex="-1" role="dialog"
																			aria-labelledby="query" aria-hidden="true">
																			<div
																				class="modal-dialog modal-dialog-centered modal-dialog-scrollable"
																				role="document">
																				<div class="modal-content">
																					<div class="modal-header text-white">
																						<h6 class="modal-title" id="query">Mention
																							Your Query</h6>
																						<button type="button" class="close"
																							data-dismiss="modal" aria-label="Close">
																							<span aria-hidden="true">&times;</span>
																						</button>
																					</div>
																					<div class="modal-body pt-0 bg-light text-center">
																						<h6 class="mt-2 text-capitalize">${ica.moduleName}</h6>
																						<hr />
																						<p>
																							Query: <span class="text-danger">* 
																						</p>
																						<c:if test="${ica.isComponentMark ne 'Y'}">
																							<textarea rows="5" cols="30"
																								id="icaQueryText${ica.icaId}"
																								required="required" class="w-100"
																								maxlength="200">
																						</textarea>
																						</c:if>
																						<c:if test="${ica.isComponentMark eq 'Y'}">
																							<textarea rows="5" cols="30"
																								id="icaQueryText${ica.icaId}${ica.compId}"
																								required="required" class="w-100"
																								maxlength="200">
																						</textarea>
																						</c:if>

																					</div>
																					<c:if test="${ica.isComponentMark ne 'Y'}">
																						<div class="modal-footer text-center">
																							<a class="btn btn-primary text-white"
																								onclick="raiseQuery('${ica.icaId}')">Submit</a>
																							<button type="button" class="btn btn-modalClose"
																								data-dismiss="modal">Cancel</button>
																						</div>
																					</c:if>
																					<c:if test="${ica.isComponentMark eq 'Y'}">
																						<div class="modal-footer text-center">
																							<a class="btn btn-primary text-white"
																								onclick="raiseCompQuery('${ica.icaId}','${ica.compId}')">Submit</a>
																							<button type="button" class="btn btn-modalClose"
																								data-dismiss="modal">Cancel</button>
																						</div>
																					</c:if>
																				</div>
																			</div>
																		</div>
																	</div>


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
																						<h6 class="mt-2 text-capitalize">${ica.moduleName}</h6>
																						<hr />
																						<p>
																							Query: <span class="text-danger">* 
																						</p>
																						<c:choose>
																							<c:when test="${ica.isQueryRaise eq 'Y'}">
																								<textarea rows="5" cols="30"
																									id="icaQueryTextDB${ica.icaId}"
																									required="required" class="w-100"
																									disabled="disabled">${ica.query}
																						</textarea>
																							</c:when>
																							<c:otherwise>
																								<c:if test="${ica.isComponentMark ne 'Y'}">
																									<textarea rows="5" cols="30"
																										id="icaQueryTextDB${ica.icaId}"
																										required="required" class="w-100"
																										disabled="disabled">
																							</textarea>
																								</c:if>
																								<c:if test="${ica.isComponentMark eq 'Y'}">
																									<textarea rows="5" cols="30"
																										id="icaQueryTextDB${ica.icaId}${ica.compId}"
																										required="required" class="w-100"
																										disabled="disabled">
																							</textarea>
																								</c:if>
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
					function raiseQuery(icaId){
						var query = $('#icaQueryText'+icaId).val();
						//query = query.replace(/[^a-zA-Z ]/g, "");
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
                                                                      type : 'POST',
                                                                     /* url : '${pageContext.request.contextPath}/raiseQueryForStudent?'
                                                                                  + 'id='
                                                                                  + icaId
                                                                                  + '&query='
                                                                                  + query,*/
                                                                         url: '${pageContext.request.contextPath}/raiseQueryForStudent',
                                                                         data: {id:icaId,query:query},
                                                                      success : function(data) {
                                                                    	  const obj = JSON.parse(data);
                                                                    	  if(obj.Status === 'ValidationException'){
                                                                    		  $(".newLoaderWrap").css('display','none');
                                                                              swal(obj.msg);
                                                                    	  } else if(obj.Status === 'Success') {
                                                                    	  
                                                                    	  console.log('data is ' + data);
                                                                            $(".newLoaderWrap").css('display','none');
                                                                            $(
                                                                                        '#raiseQHide'
                                                                                                    + icaId)
                                                                                        .show();
                                                                            $('#raiseQ' + icaId)
                                                                                        .hide();
                                                                            $('.btn-modalClose')
                                                                                        .trigger(
                                                                                                    'click');
                                                                            //$('#icaQueryText'+icaId).val(query);
                                                                            console
                                                                                        .log('icaQueryTextDB'
                                                                                                    + icaId
                                                                                                    + '--?'
                                                                                                    + query);
                                                                            document
                                                                                        .getElementById('icaQueryTextDB'
                                                                                                    + icaId).value = query;
                                                                            swal('query raised successfully');
                                                                            //$("#raiseQ").addClass('disabled');
                                                                    	  } else {
                                                                    		  swal('Error in raising query');
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
					
					

                    function raiseCompQuery(icaId,compId){
                    	
						var query = $('#icaQueryText'+icaId+compId).val();
						
						//query = query.replace(/[^a-zA-Z ]/g, "");
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
                                                                      type : 'POST',
                                                                      /*
                                                                      url : '${pageContext.request.contextPath}/raiseQueryForStudent?'
                                                                                  + 'id='
                                                                                  + icaId
                                                                                  +'&compId='
                                                                                  +compId
                                                                                  + '&query='
                                                                                  + query,*/
                                                                       url : '${pageContext.request.contextPath}/raiseQueryForStudent',
                                                                       data: {id:icaId,compId:compId,query:query},
                                                                      success : function(data) {
                                                                            $(".newLoaderWrap").css('display','none');
                                                                            $(
                                                                                        '#raiseQHide'
                                                                                                    + icaId+compId)
                                                                                        .show();
                                                                            $('#raiseQ' + icaId+compId)
                                                                                        .hide();
                                                                            $('.btn-modalClose')
                                                                                        .trigger(
                                                                                                    'click');
                                                                            //$('#icaQueryText'+icaId).val(query);
                                                                            
                                                                            document
                                                                                        .getElementById('icaQueryTextDB'
                                                                                                    + icaId+compId).value = query;
                                                                            swal('query raised successfully');
                                                                            //$("#raiseQ").addClass('disabled');
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