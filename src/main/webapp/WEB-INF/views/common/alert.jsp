<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>	
<c:if test="${error}">
	<div class="alert alert-danger alert-dismissible" role="alert">
		<button type="button" class="close" data-dismiss="alert"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
		<h4><strong>Error!</strong> ${errorMessage}</h4>
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
		<h4><strong>Success!</strong> ${successMessage}</h4>
	</div>
	<script>
		window.setTimeout(function() { $('.alert-success').alert('close'); }, 60000);
	</script>
</c:if>
<c:if test="${note}">
	<div class="alert alert-warning alert-dismissible" role="alert">
		<button type="button" class="close" data-dismiss="alert"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
		<h4><strong>Note!</strong> ${noteMessage}</h4>
	</div>
	<script>
		//window.setTimeout(function() { $('.alert-warning').alert('close'); }, 5000);
		
		$('.alert-warning').alert('close');
	</script>
</c:if>
