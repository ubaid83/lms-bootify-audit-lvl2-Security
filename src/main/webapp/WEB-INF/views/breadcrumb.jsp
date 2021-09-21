<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${not empty breadcrumbs}">
<ol class="breadcrumb">
<c:forEach items="${breadcrumbs}" var="breadcrumbs" varStatus="satus">
		<c:choose>
		<c:when test="${status.last}">
			<li class="active">${breadcrumbs.label}</li>
		</c:when>
		<c:otherwise>
			<li><a href="<c:url value="${breadcrumbs.link}" />">${breadcrumbs.label}</a></li>
		</c:otherwise>
		</c:choose>
		
</c:forEach>
</ol>
</c:if>