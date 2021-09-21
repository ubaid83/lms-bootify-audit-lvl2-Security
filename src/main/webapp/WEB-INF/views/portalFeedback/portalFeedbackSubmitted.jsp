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
<jsp:include page="../common/newLeftNavBarFaculty.jsp"/>
<jsp:include page="../common/newLeftSidebarFaculty.jsp"/>
<jsp:include page="../common/rightSidebarFaculty.jsp"/>

<!-- DASHBOARD BODY STARTS HERE -->

<div class="container-fluid m-0 p-0 dashboardWraper">

	<jsp:include page="../common/newTopHeaderFaculty.jsp" />
	
	<%
	boolean isEdit = "true".equals((String) request
			.getAttribute("edit"));
%>
     
     <div class="container mt-5">
                <div class="row">
            <!-- SEMESTER CONTENT -->    
                    <div class="col-lg-9 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto">
                        
          <!-- page content: START -->
          
                        <nav aria-label="breadcrumb">
                            <ol class="breadcrumb">
                                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
                                <li class="breadcrumb-item" aria-current="page"><c:out value="${Program_Name}" /></li>
                                <sec:authorize access="hasRole('ROLE_STUDENT')">
								<c:out value="${AcadSession}" />
							</sec:authorize>
                                <li class="breadcrumb-item active" aria-current="page"> Portal Feedback</li>
                            </ol>
                        </nav>
                        
						<jsp:include page="../common/alert.jsp" />
						
						
						<!-- Input Form Panel -->
						<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">

										<h2>Portal Feedback Form</h2>

										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>

										<div class="clearfix"></div>

									</div>

									<div class="x_content">
										<form:form action="replyToQueryAnswer" method="post"
											modelAttribute="portalFeedbackQuery"
											enctype="multipart/form-data">


											<%-- <sec:authorize access="hasRole('ROLE_FACULTY')"> --%>
											<c:forEach var="query" items="${queryList}"
												varStatus="status">
												<c:if test="${query.createdBy eq username }">
													<div class="contain" style="text-align: -webkit-right;">

														<p
															style="font-size: medium; color: black; overflow: auto;">${query.answer }</p>
														<c:if test="${status.count == 1 }">
															<span class="time-right"> <a href="#"
																title="Reply" class="fa fa-mail-reply"
																onclick="myFunction()" id="${query.id}"></a>

															</span>
														</c:if>
														You | ${query.createdOn }
														<div id="replyText${query.id}" style="display: none;">


															<form:label path="answer" for="editor">Enter Your Reply</form:label>
															<%-- <form:textarea class="form-group" path="reply" id="editor"
														style="margin-top: 30px;margin-bottom:10px; white-space: pre-wrap;" /> --%>
															<form:textarea class="form-group" path="answer"
																name="editor1" id="editor1" />




															<%-- <button id="submit" class="btn btn-large btn-primary"
																formaction="replyToQueryAnswer?qId=${query.id}&url=${url}&dbName=${dbName}&dbUsername=${dbUsername}&password=${password}">Reply</button> --%>

															<button id="submit" class="btn btn-large btn-primary"
																formaction="replyToQueryAnswer?qId=${query.id}">Reply</button>


															<button id="close${query.id}" class="btn btn-danger"
																formaction="" onclick="closeFunction()">Close</button>
														</div>
													</div>
												</c:if>
												<c:if test="${query.createdBy ne username }">
													<div class="contain darker">

														<p
															style="font-size: medium; color: black; overflow: auto;">${query.answer }</p>
														<c:if test="${status.count == 1 }">
															<span class="time-right"><a href="#" title="Reply"
																class="fa fa-mail-reply" onclick="myFunction()"
																id="${query.id}"></a></span>
														</c:if>${query.firstname }
														${query.lastname } | ${query.createdOn }
														<div id="replyText${query.id}" style="display: none;">


															<form:label path="answer" for="editor">Enter Your Reply</form:label>
															<%-- <form:textarea class="form-group" path="reply" id="editor"
														style="margin-top: 30px;margin-bottom:10px; white-space: pre-wrap;" /> --%>
															<form:textarea class="form-group" path="answer"
																name="editor1" id="editor1" style="" />




															<%-- <button id="submit" class="btn btn-large btn-primary"
																formaction="replyToQueryAnswer?qId=${query.id}&url=${url}&dbName=${dbName}&dbUsername=${dbUsername}&password=${password}">Reply</button> --%>
															<button id="submit" class="btn btn-large btn-primary"
																formaction="replyToQueryAnswer?qId=${query.id}">Reply</button>


															<button id="close${query.id}" class="btn btn-danger"
																formaction="" onclick="closeFunction()">Close</button>
														</div>
													</div>
												</c:if>







											</c:forEach>




											<%-- <c:forEach var="query"
																items="${portalFeedback.queryList}" varStatus="status">
																${query.answer }
															</c:forEach> --%>
											<%-- </sec:authorize> --%>

											<button id="cancel" class="btn btn-danger" type="button"
												formaction="homepage" formnovalidate="formnovalidate"
												onclick="history.go(-1);">Back</button>



										</form:form>
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
	<jsp:include page="../common/footer.jsp"/>

	
<script>
				function myFunction() {

					var clicked_id = event.srcElement.id;

					var id = '#replyText' + clicked_id
					//alert(id);
					$(id).show();

				}
				function closeFunction() {
					//alert(event.srcElement.id);
					var clicked_id = event.srcElement.id;

					var id = '#replyText' + clicked_id
					$(id).hide();

				}
			</script>












