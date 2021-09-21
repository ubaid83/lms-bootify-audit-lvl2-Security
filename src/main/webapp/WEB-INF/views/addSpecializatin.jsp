<jsp:include page="common/header.jsp" />
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
    <section class="container">
        <%if("true".equals((String)request.getAttribute("edit"))){ %>
			<div class="row"><legend>Edit Specialization</legend></div>
		<%} else {%>
        	<div class="row"><legend>Add Specialization</legend></div>
        <%} %>
		
		<form:form  action="addSpecialization" method="post" modelAttribute="specialization" >
			<fieldset>
			<div class="row clearfix">
			
			
				<div class="col-md-6 column">
					<%if("true".equals((String)request.getAttribute("edit"))){ %>
					<form:input type="hidden" path="id" value="${specialization.id}"/>
					<%} %>
					<!-- Form Name -->
					
					
					<div class="form-group">
							<form:input id="abbr" path="abbr" type="text" required="required" placeholder="Prgoram Abbriviation" class="form-control" value="${specialization.abbr}"/>
					</div>

					<div class="form-group">
							<form:input id="name" path="name" type="text" placeholder="Specialization Full Name" class="form-control" value="${specialization.name}"/>
					</div>
						
					<div class="form-group">

							<%if("true".equals((String)request.getAttribute("edit"))){ %>
								<button id="submit" name="submit" class="btn btn-large btn-primary" formaction="updateSpecialization">Update</button>
							<%}else	{%>
								<button id="submit" name="submit" class="btn btn-large btn-primary" formaction="addSpecialization">Add</button>
							<%} %>
							<button id="cancel" name="cancel" class="btn btn-danger" formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
					</div>
				</div>
			</div>	
			</fieldset>
		</form:form>


		
	
	</section>
<jsp:include page="common/footer.jsp" />
