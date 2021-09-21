<jsp:include page="../common/header.jsp" />
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<section class="container-fluid">
<jsp:include page="../common/alert.jsp" />
	<div class="row page-body">
		<div class="col-sm-12 column">
			<form:form action="addProgramSessionCourse" id="addProgramSessionCourseForm" method="post" modelAttribute="programSessionCourse">
				<fieldset>
					<legend>Map Program & Session with Courses</legend>
					<div class="row">
						<div class="col-sm-4 column">
								<div class="form-group">
									<label class="control-label" for="program">Program <span style="color: red">*</span></label>
									<form:select id="program" path="programId" type="text"
										placeholder="Program" class="form-control" required="required" >
										<form:option value="">Select Program</form:option>
										<form:options items="${programs}" itemLabel="programName" itemValue="id"/>
									</form:select>
								</div>
						</div>
						<div class="col-sm-8 column">
								<div class="form-group">
									<label class="control-label" for="session">Session <span style="color: red">*</span></label>
									<div id="sessionSelect" >
										<form:select id="session" path="programSessionId" type="text"
											placeholder="Session" class="form-control" required="required" data-value="${programSessionCourse.programSessionId }">
										</form:select>
									</div>
								</div>
						</div>
						<div class="clearfix"></div>
						<div class="col-sm-12 column">
								<div class="form-group">
									<label class="control-label" for="course">Course <span style="color: red">*</span></label>
									<div id="courseSelect" >
										<form:select id="course" path="courseIds" type="text"
											placeholder="course" class="form-control" required="required" multiple="multiple">
										</form:select>
									</div>
								</div>
						</div>
					</div>
					<div class="row">

						<div class="col-sm-12 column">
							<div class="form-group">

								<button id="submit"	class="btn btn-large btn-primary" formaction="addProgramSessionCourse">Add</button>
								<button id="cancel" class="btn btn-danger"
									formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
							</div>
						</div>

					</div>


				</fieldset>
			</form:form>
		</div>
	</div>

</section>
<jsp:include page="../common/footer.jsp" />
