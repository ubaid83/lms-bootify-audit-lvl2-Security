<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>	
<c:if test="${error}">
	<div class="alert alert-danger alert-dismissible" role="alert">
		<button type="button" class="close" data-dismiss="alert"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
		<strong>Error!</strong> ${errorMessage}
	</div>
	<!-- <script>
		window.setTimeout(function() { $('.alert-danger').alert('close'); }, 5000);
	</script> -->
</c:if>
<c:if test="${success}">
	<div class="alert alert-success alert-dismissible" role="alert">
		<button type="button" class="close" data-dismiss="alert"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
		<strong>Success!</strong> ${successMessage}
	</div>
	<script>
		window.setTimeout(function() { $('.alert-success').alert('close'); }, 5000);
	</script>
</c:if>