<%@page import="com.spts.lms.beans.forum.Forum"%>
<%@page import="com.spts.lms.beans.calender.Calender"%>
<%@page import="java.util.List"%>
<%@ page import="com.spts.lms.web.utils.Utils"%>
<%-- <jsp:include page="../common/NewHeader1.jsp" /> --%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<!doctype html>
<html lang="en">

<jsp:include page="../../common/css.jsp" />

<script type="text/javascript">
	var headerCourseId = null;
	function theFunction(obj) {
		return true;
	}
</script>

<body class="nav-md footer_fixed">


	<div class="loader"></div>


	<div class="container body">
		<div class="main_container">
     <jsp:include page="../../common/topHeader.jsp" />
      
   
      
<!-- page content -->
<div class="right_col" role="main">
	<div class="dashboard_container">
		<div class="dashboard_height" id="main">
			<div class="dashboard_container_spacing">
				<div class="breadcrumb">
				<c:out value="${Program_Name}" />
						
						<sec:authorize access="hasRole('ROLE_STUDENT')">
						
						<i
						class="fa fa-angle-right"></i>
											
											<c:out value="${AcadSession}" />
											
											</sec:authorize>
						<br><br>
				
				
					<a href="${pageContext.request.contextPath}/viewCourse?id=${courseId}">My Courses</a> <i
						class="fa fa-angle-right"></i><c:out value="${courseRecord.courseName}" />
				</div>
					
		</div>
		
	</div>
	
	<div class="row">
                            <div class="col-xs-12 col-sm-12">
                                <div class="x_panel">
								
                                    <div class="x_title">
                                        <h2>Select Faculty</h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a></li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
									
                                    <div class="x_content">
			<form:form action=""  method="post"
				modelAttribute="course" >
				<fieldset>
				<div class="row">
						
						<div class="col-sm-4 column">
							<div class="form-group">

								<form:label path="facultyId" for="facultyId">Faculty</form:label>
								<form:select id="facultyId" path="facultyId"
									class="form-control">
									<form:option value="">Select Faculty</form:option>
									<c:forEach var="faculty" items="${facultyDropdown}"
										varStatus="status">
										<form:option value="${faculty.username}"></form:option>
									</c:forEach>
								</form:select>

							</div>
						</div>
						
						

					</div>

					<hr>
					<div class="row">

					<div class="col-sm-12 column">
							<div class="form-group">
								<form:button id="submit" name="submit"
									class="btn btn-large btn-primary" formaction="getContentUnderAPath?courseId=${courseId}">Search</form:button>
								<button type="reset" type="reset" class="btn btn-large btn-primary">Reset</button>
								<button id="cancel" name="cancel" class="btn btn-danger"
									formnovalidate="formnovalidate">Cancel</button>
							</div>
						</div>
					</div>


				</fieldset>
			</form:form>
                                    </div>
                                </div>
                            </div>
                        </div>
                        
</div>
<!-- /page content -->
   </div>
<jsp:include page="../../common/footer.jsp" />
  </div>
  </div>
  
</body>

</html>

