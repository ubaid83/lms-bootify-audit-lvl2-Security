<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.4/MathJax.js?config=TeX-AMS_HTML"></script>
<jsp:include page="../common/newDashboardHeader.jsp" />

<div class="d-flex dataTableBottom" id="facultyAssignmentPage">
<jsp:include page="../common/newLeftNavbar.jsp" />
<jsp:include page="../common/newLeftSidebar.jsp" />
<jsp:include page="../common/rightSidebar.jsp" />

	<div class="container-fluid m-0 p-0 dashboardWraper">
	<jsp:include page="../common/newTopHeader.jsp" />
	<div class="container mt-5">
		<div class="row">
			<!-- SEMESTER CONTENT -->
			<div class="col-lg-9 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto">
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
						<li class="breadcrumb-item active" aria-current="page">Test Result</li>
					</ol>
				</nav>

				<jsp:include page="../common/alert.jsp" />
				<!-- Input Form Panel -->
					<div class="card bg-white border">
						<div class="card-body">
						<h5 class="pb-2 border-bottom pb-2">Test Name: ${testDetails.testName} (${testDetails.maxScore} Marks)</h5>
						<c:forEach var="test" items="${testQuestionDetailsWithResponse}" varStatus="status">
						<table>
							<tbody>
								<tr>
									<td valign="top" align="left" rowspan="2"><i class="fas fa-chevron-right"></i>&nbsp</td>
									<td valign="top">${test.description}</td>
								</tr>
								<tr>
									<td valign="top">
									<table>
									<tbody>
									<c:if test="${test.questionType eq 'MCQ'}">
										<c:if test="${not empty test.option1}">
										<tr>
											<td><span class="font-weight-bold p-1">Option 1: </span></td>
											<td><span class="p-1">${test.option1}</span>
											<c:if test="${fn:contains(test.correctOption, '1')}">
												<i class="fa fa-check-circle text-success"></i>
											</c:if></td>
										</tr>
										</c:if>
										<c:if test="${not empty test.option2}">
										<tr>
											<td><span class="font-weight-bold p-1">Option 2: </span></td>
											<td><span class="p-1">${test.option2}</span>
											<c:if test="${fn:contains(test.correctOption, '2')}">
												<i class="fa fa-check-circle text-success"></i>
											</c:if></td>
										</tr>
										</c:if>
										<c:if test="${not empty test.option3}">
										<tr>
											<td><span class="font-weight-bold p-1">Option 3: </span></td>
											<td><span class="p-1">${test.option3}</span>
											<c:if test="${fn:contains(test.correctOption, '3')}">
												<i class="fa fa-check-circle text-success"></i>
											</c:if></td>
										</tr>
										</c:if>
										<c:if test="${not empty test.option4}">
										<tr>
											<td><span class="font-weight-bold p-1">Option 4: </span></td>
											<td><span class="p-1">${test.option4}</span>
											<c:if test="${fn:contains(test.correctOption, '4')}">
												<i class="fa fa-check-circle text-success"></i>
											</c:if></td>
										</tr>
										</c:if>
										<c:if test="${not empty test.option5}">
										<tr>
											<td><span class="font-weight-bold p-1">Option 5: </span></td>
											<td><span class="p-1">${test.option5}</span>
											<c:if test="${fn:contains(test.correctOption, '5')}">
												<i class="fa fa-check-circle text-success"></i>
											</c:if></td>
										</tr>
										</c:if>
										<c:if test="${not empty test.option6}">
										<tr>
											<td><span class="font-weight-bold p-1">Option 6: </span></td>
											<td><span class="p-1">${test.option6}</span>
											<c:if test="${fn:contains(test.correctOption, '6')}">
												<i class="fa fa-check-circle text-success"></i>
											</c:if></td>
										</tr>
										</c:if>
										<c:if test="${not empty test.option7}">
										<tr>
											<td><span class="font-weight-bold p-1">Option 7: </span></td>
											<td><span class="p-1">${test.option7}</span>
											<c:if test="${fn:contains(test.correctOption, '7')}">
												<i class="fa fa-check-circle text-success"></i>
											</c:if></td>
										</tr>
										</c:if>
										<c:if test="${not empty test.option8}">
										<tr>
											<td><span class="font-weight-bold p-1">Option 8: </span></td>
											<td><span class="p-1">${test.option8}</span>
											<c:if test="${fn:contains(test.correctOption, '8')}">
												<i class="fa fa-check-circle text-success"></i>
											</c:if></td>
										</tr>
										</c:if>
									</c:if>
									<c:if test="${test.questionType eq 'Numeric'}">
										<c:if test="${not empty test.answerRangeFrom}">
										<tr>
											<td><span class="font-weight-bold p-1">Answer Range From: </span></td>
											<td><span class="p-1">${test.answerRangeFrom}</span></td>
										</tr>
										</c:if>
										<c:if test="${not empty test.answerRangeTo}">
										<tr>
											<td><span class="font-weight-bold p-1">Answer Range To: </span></td>
											<td><span class="p-1">${test.answerRangeTo}</span></td>
										</tr>
										</c:if>
									</c:if>
									</tbody>
									</table>
									<div>
										<p><span class="text-success font-weight-bold p-1 mt-2">Your Answer: </span>
										<c:if test="${test.questionType eq 'MCQ'}">Option </c:if>${test.studentQuestionResponse.answer}</p>
									</div>
									</td>
								</tr>
								
							</tbody>
						</table>
						
						
						
						<hr>
						</c:forEach>
						
						</div>
					</div>

					<!-- Results Panel -->
									<!-- /page content: END -->

			</div>

			<!-- SIDEBAR START -->
			<jsp:include page="../common/newSidebar.jsp" />
			<!-- SIDEBAR END -->
			<jsp:include page="../common/footer.jsp" />
			
