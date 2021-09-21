<jsp:include page="../views/common/header.jsp" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
    <!-- UI # -->
		<section class="container">
				   <jsp:include page="../views/common/alert.jsp" />
						<div class="row page-body">
								<div>
									<form:form id="changePasswordForm" modelAttribute="user" action="changePassword" method="post">
										<fieldset>
										<legend>Change Password</legend>
										<div class="column col-sm-4">
										<div class="form-group">
											<!-- Input Box -->
											<form:password path="oldPassword" cssClass="form-control" placeholder="Current Password" id="oldPassword" required="required"/>
											<label ><i class="fa fa-key"></i></label>
										</div>
										<div class="form-group">
											<form:password path="password" cssClass="form-control" placeholder="Enter New Password" id="password" required="required"/>
											<label ><i class="fa fa-lock"></i></label>
										</div>
										<div class="form-group">
											<input type="password" name="password2" placeholder="Re Enter New Password" class="form-control" required="required"/>
											<label ><i class="fa fa-lock"></i></label>
										</div>
										<input type="submit" value="Change Password" class="btn btn-large btn-primary  ">
										<input type="submit" value="Cancel" formaction="homepage" class="btn btn-large  btn-danger  ">
										</div>
										</fieldset>
									</form:form>
								</div>
						</div>
		</section>
<jsp:include page="../views/common/footer.jsp" />