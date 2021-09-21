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
							<%-- <% if(isEdit){ %>
                                <li class="breadcrumb-item active" aria-current="page"> Update Forum</li>
                                <% } else { %> --%>
                                <li class="breadcrumb-item active" aria-current="page"> Update Forum</li>
                               <%--  <% } %> --%>
                            </ol>
                        </nav>
                        
						<jsp:include page="../common/alert.jsp" />
						
						
						<!-- Input Form Panel -->
						<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<!-- <div class="x_title">
										<h2>Create Forum</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div> -->

									<div class="x_title">
										<c:if test="${showDropDown}">
											<h5>Update Forum</h5>
										</c:if>
										<c:if test="${showDropDown ne 'true'}">

											<h5>Update Forum for ${forum.course.courseName }</h5>
										</c:if>

										
									</div>

									<div class="x_content">
										<form:form action="createForum" method="post"
											modelAttribute="forum" enctype="multipart/form-data">

											<form:input path="id" type="hidden" />
										<%-- 	<c:if test="${showDropDown}"> --%>
												<div class="row">
													<div class="col-sm-6 col-md-4 col-xs-12 column">
														<div class="form-group">
															<form:label path="courseId" for="courseId">Course <span style="color: red">*</span></form:label>
															<form:select id="courseId" path="courseId"
																required="required" class="form-control">
																<form:option value="">Select Course</form:option>
																<c:forEach var="course" items="${allCourses}"
																	varStatus="status">
																	<form:option value="${course.id}">${course.courseName}</form:option>
																</c:forEach>
															</form:select>
														</div>
													</div>
												</div>
											<%-- </c:if> --%>


											<%-- 	<div  class="row">
						<label path="question" for="editor">Add your Question</label>
						<textarea class="form-group" path="question" required="required"
							id="editor" style="margin-top: 30px;margin-bottom:10px" />

					</div --%>
											<div class="row">
												<div class="col-sm-6 col-md-4 col-xs-12 column">
													<div class="form-group">
														<form:label path="topic" for="topic">Topic <span style="color: red">*</span></form:label>
														<form:input path="topic" type="text" class="form-control"
															required="required" />
													</div>
												</div>
											</div>
											<div class="col-sm-12 column">
												<div class="form-group">



													<form:label path="question" for="editor">Add Your Question</form:label>
													
														
														<form:textarea class="form-group" path="question" name="editor1" id="editor1" rows="10" cols="80" />

													<%-- <form:label path="question" for="question">Add Your Question</form:label>
													<form:textarea class="form-control" path="question"
														id="question" rows="10" placeholder="ENTER QUESTION" /> --%>


												</div>
											</div>

											<div>
												<div class="col-sm-12 column">
													<div class="form-group">
														<button id="submit" class="btn btn-large btn-primary"
															formaction="updateForum">Update Forum</button>
														<!-- <input id="reset" type="reset" class="btn btn-danger"> -->
													</div>
												</div>
											</div>


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

	
<script type="text/javascript">
	CKEDITOR.replace( 'editor1' , {
			extraPlugins: 'mathjax',
			mathJaxLib: 'https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/MathJax.js?config=TeX-AMS_HTML'
		});
		
</script>












