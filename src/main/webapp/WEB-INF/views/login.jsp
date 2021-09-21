<%-- <jsp:include page="header.jsp" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<meta http-equiv="refresh" content="${pageContext.session.maxInactiveInterval};url=http://localhost:8080/usermgmt/login"> --%>

    <%
    String redirectURL = "http://localhost:8080";
   response.setStatus(response.SC_MOVED_TEMPORARILY);  
    response.setHeader("Location",redirectURL);
 
%> 
<%-- 
	<!--
    you can substitue the span of reauth email for a input with the email and
    include the remember me checkbox
    -->
    <html>
    <body onload='document.f.username.focus();'>
	<section class="container">
		<c:if test="${not empty param.error}">
		<div class="alert alert-danger alert-dismissible" role="alert">
			<button type="button" class="close" data-dismiss="alert" aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>
			
		</div>
		</c:if>
		<div class="card card-container">
			<img id="profile-img" class="profile-img-card"
				src="<c:url value="../resources/images/avatar_2x.png" />">
			<p id="profile-name" class="profile-name-card"></p>
					<h3>Login with Username and Password</h3><form name='f' action='/login' method='POST'>
					<table>
				<tr><td>User:</td><td><input type='text' name='username' value=''></td></tr>
				<tr><td>Password:</td><td><input type='password' name='password'/></td></tr>
				<tr><td colspan='2'><input name="submit" type="submit" value="Login"/></td></tr>
				<tr><td><input name="_csrf" type="hidden" value="12aff8c9-f0bc-4b02-80cb-441f3b69a265" /></td></tr>
				</table>
				</form>
			<!-- /form -->
			<a href="<c:url value="/resetPasswordForm" />" class="forgot-password"> Forgot the password? </a> <span
				class="help-block"></span>
		</div>
		<!-- /card-container -->
	</section>
	</html>
	<!-- /container -->
<jsp:include page="footer.jsp"/> --%>