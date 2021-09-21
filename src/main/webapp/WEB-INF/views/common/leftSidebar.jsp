
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<div class="col-md-3 left_col">
	<div class="left_col scroll-view">
		<div class="navbar nav_title" style="border: 0;">
			<c:if test="${instiFlag eq 'nm'}">
				<a href="${pageContext.request.contextPath}/homepage"><img
					src="<c:url value="/resources/images/logo.gif" />" alt=""></a>
			</c:if>
			<c:if test="${instiFlag eq 'sv'}">
				<a href="${pageContext.request.contextPath}/homepage"><img
					src="<c:url value="/resources/images/svkmlogo.png" />" alt=""></a>
			</c:if>
		</div>

		<div class="clearfix"></div>


		<sec:authorize access="hasRole('ROLE_FACULTY')">
			<jsp:include page="adminLeftSidebar.jsp" />

		</sec:authorize>
		<sec:authorize access="hasRole('ROLE_STUDENT')">
			<jsp:include page="studentLeftSidebar.jsp" />

		</sec:authorize>
		<sec:authorize access="hasRole('ROLE_HOD')">
			<jsp:include page="hodLeftSidebar.jsp" />

		</sec:authorize>


	</div>
</div>