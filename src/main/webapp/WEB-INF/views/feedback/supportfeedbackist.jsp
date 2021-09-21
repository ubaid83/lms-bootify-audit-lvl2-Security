<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<jsp:include page="../common/newDashboardHeader.jsp" />

<div class="d-flex" id="facultyAssignmentPage">

	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper">

		<header class="container-fluid sticky-top">
			<nav class="navbar navbar-expand-lg navbar-light p-0">
				<a class="navbar-brand" href="homepage"> 
				<c:choose>
						<c:when test="${instiFlag eq 'nm'}">
							<img src="<c:url value="/resources/images/logo.png" />"
								class="logo" title="NMIMS logo" alt="NMIMS logo" />
						</c:when>
						<c:otherwise>
							<img src="<c:url value="/resources/images/svkmlogo.png" />"
								class="logo" title="SVKM logo" alt="SVKM logo" />
						</c:otherwise>
				</c:choose>
				</a>
				<button class="adminNavbarToggler" type="button"
					data-toggle="collapse" data-target="#adminNavbarCollapse">
					<i class="fas fa-bars"></i>
				</button>

				<div class="collapse navbar-collapse" id="adminNavbarCollapse">
					<ul class="navbar-nav ml-auto">
						<li id="program" class="nav-item active" data-toggle="tooltip"
							data-placement="bottom" title="My Program"><a
							class="nav-link" href="homepage"><i class="fas fa-home"></i>
								<span>Home</span></a></li>
					</ul>
				</div>
			</nav>
		</header>

		<!-- SEMESTER CONTENT -->
		<div class="col-lg-12 col-md-12 col-sm-12 dashboardBody p-5">
			<nav aria-label="breadcrumb">
				<ol class="breadcrumb">
					<li class="breadcrumb-item"><a
						href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
					<li class="breadcrumb-item active" aria-current="page">Student Master</li>
				</ol>
			</nav>

			<jsp:include page="../common/alert.jsp" />
				<div class="card bg-white border">
				<div class="card-body">
			
					<h5 class="text-left pb-2 border-bottom">Search Feedback</h5>
					<form:form action="checkFeedbackSupportAdmin" method="post" modelAttribute="FeedbackQuestion">
										<div class="row">
												<div class="col-md-2">
													<div class="form-group">
														<form:label path="acadSession" for="acadSession">AcadSession<span style="color: red">*</span></form:label>
														<form:select path="acadSession" class="form-control rounded-0" required="required">
														<option>Select AcadSession</option>
														<c:forEach var="SessionList" items="${SessionList}">
														<form:option value="${SessionList}" ></form:option>
														
													<%-- 	<c:if test="${FeedbackQuestion.acadSession ne null}">
																		<c:if test="${FeedbackQuestion.acadSession eq acadSession}">
																			<option value="${SessionList}" selected>${SessionList}</option>
																		</c:if>
																		<c:if test="${FeedbackQuestion.acadSession ne acadSession}">
																			<option value="${SessionList}">${SessionList}</option>
																		</c:if>
																	</c:if>
																	<c:if test="${FeedbackQuestion.acadSession eq null}">
																		<form:option value="${SessionList}">${SessionList}</form:option>
														</c:if> --%>
														
													
														
														
														</c:forEach>
														</form:select>
													</div>
												</div>

												<div class="col-md-2">
													<div class="form-group">
														<form:label path="acadYear" for="acadYear">acad Year<span style="color: red">*</span></form:label>
														<form:select path="acadYear" class="form-control rounded-0" required="required">
														<option>Select Acad Year</option>
														<c:forEach var="acadYearList" items="${acadYearList}">
														<form:option value="${acadYearList }"></form:option>
														</c:forEach>
														</form:select>
													</div>
												</div>
												
												<div class="col-md-4">
													<div class="form-group">
														<form:label path="programId" for="programId">Program Name<span style="color: red">*</span></form:label>
														<form:select path="programId" class="form-control rounded-0" required="required">
														<option>Select Program Name</option>
														<c:forEach var="list" items="${programList}">
														<form:option value="${list.id}">${list.programName}</form:option>
														</c:forEach>
														</form:select>
													</div>
												</div>
												
												
												<div class="col-md-2">
													<div class="form-group">
														<form:label path="createdBy" for="createdBy">Created By<span style="color: red">*</span></form:label>
														<form:select path="createdBy" class="form-control rounded-0" required="required">
														<option>Created By</option>
														<c:forEach var="usernameList" items="${usernameList}">
														<form:option value="${usernameList}">${usernameList}</form:option>
														</c:forEach>
														</form:select>
													</div>
												</div>
												
												
												<div class="col-md-2">
													<div class="form-group">
														<form:label path="username" for="username">CampusId<span style="color: red">*</span></form:label>
														<form:select path="campusId" class="form-control rounded-0" required="required">
														<option value="">CampusId</option>
														<c:forEach var="list" items="${campusList}">
														<form:option value="${list.campusId}">${list.campusName}</form:option>
														</c:forEach>
														</form:select>
													</div>
												</div>


												<div class="col-sm-12 column">
													<div class="form-group">
														<button id="submit" name="submit"
															class="btn btn-large btn-primary rounded-0">Search</button>
														<!-- <input id="reset" type="reset" class="btn btn-danger"> -->
														<button id="cancel" name="cancel" class="btn btn-danger rounded-0"
															formaction="homepage"
															formnovalidate="formnovalidate">Cancel</button>
													</div>
												</div>
										
										</div>
											
										</form:form>

				</div>
				</div>
			
			<div class="card bg-white border">
				<div class="card-body">
				
			
				
<h5 class="text-left pb-2 border-bottom">View Feedback List/Update</h5>
<div class="accordion" id="accordionExample">
  <div class="card">
  <c:forEach var="entry" items="${getListOfMapForFeedbackReport}" varStatus="loop">
    <div class="card-header" style="border-radius: 0px!important;" id="headingOne">
      <h2 class="mb-0">
        <button class="btn btn-link btn-block text-left" type="button" data-toggle="collapse" data-target="#collapseOne${loop.index}" aria-expanded="true" aria-controls="collapseOne">
          <c:out value="${entry.value}"/>
          <span class="float-right">${loop.count}</span>
        </button>
      </h2>
    </div>
    <div id="collapseOne${loop.index}" class="collapse hide" aria-labelledby="headingOne" data-parent="#accordionExample">
      <div class="card-body">
     <%--  <c:if test="${fn:contains(entry.value, ',')}">
      <textarea class="form-control rounded-0" id="exampleFormControlTextarea1" rows="3"><c:out value="${entry.key}"/></textarea>
      </c:if> --%>
      <c:choose>
      <c:when test="${fn:contains(entry.value, ',')}">
       <textarea class="form-control rounded-0 " id="exampleFormControlTextarea1" rows="3" disabled><c:out value="${entry.key}" /></textarea>
      </c:when>
      <c:otherwise>
      <textarea  class="form-control rounded-0" id="exampleFormControlTextarea1" rows="3" disabled><c:out value="${entry.key}" /></textarea>
      </c:otherwise>
      </c:choose>
     
      </div>
       <div class="text-right">
       
       
           <c:url var="viewupdateFeedBackQuestionSpAdmin" value="viewupdateFeedBackQuestionSpAdmin">
           <c:set var = "replaceone" value = "${fn:replace(entry.value, '[', '')}" />
           <c:set var = "finalreplacevalue" value = "${fn:replace(replaceone, ']', '')}" />
		   <c:param name="id" value="${finalreplacevalue}"/>
		   </c:url> 
       
		<c:choose>
     	 <c:when test="${fn:contains(entry.value, ',')}">
     	 <!--  <a class="btn btn-success rounded-0 my-2" href="#">You Can Not Update This Field</a> -->
      	  </c:when>
      	  <c:otherwise>
      	  <a class="btn btn-success rounded-0 my-2" href="${viewupdateFeedBackQuestionSpAdmin}">Update Question</a>
      	  </c:otherwise>
       </c:choose>
       </div>
    </div>
    </c:forEach>
  </div>
</div>	
				</div>
	
			</div>
		</div>


		<jsp:include page="../common/newAdminFooter.jsp" />
