<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
      uri="http://www.springframework.org/security/tags"%>
      
      
      
      <jsp:include page="../common/newDashboardHeader.jsp" /> 

<div class="d-flex" id="facultyAssignmentPage">
<jsp:include page="../common/newAdminLeftNavBar.jsp"/>
<jsp:include page="../common/rightSidebarAdmin.jsp" />


<!-- DASHBOARD BODY STARTS HERE -->

<div class="container-fluid m-0 p-0 dashboardWraper">

      <jsp:include page="../common/newAdminTopHeader.jsp" />
     
            <!-- SEMESTER CONTENT -->    
                    <div class="col-lg-12 col-md-12 col-sm-12 dashboardBody p-5">
                        
                     <!-- page content: START -->
                     <nav aria-label="breadcrumb">
                            <ol class="breadcrumb">
                                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
                                <li class="breadcrumb-item" aria-current="page"><c:out value="${Program_Name}" /></li>
                                <sec:authorize access="hasRole('ROLE_STUDENT')">
                                                <c:out value="${AcadSession}" />
                                          </sec:authorize>
                                <li class="breadcrumb-item active" aria-current="page"> Add Feedback</li>
                            </ol>
                        </nav>

                                    <jsp:include page="../common/alert.jsp" />

                                    <!-- Input Form Panel -->
                                    <div class="card bg-white border">
                                    <div class="card-body">
                                    <h5 class="text-center pb-2 border-bottom">
                                                                  <%
                                                                        if ("true".equals((String) request.getAttribute("edit"))) {
                                                                  %>
                                                                  Edit Feedback
                                                                  <%
                                                                        } else {
                                                                  %>
                                                                  Add Feedback
                                                                  <%
                                                                        }
                                                                  %>
                                                            </h5>

                                                            <form:form action="addFeedback" method="post"
                                                                  modelAttribute="feedback">


                                                                  <div class="row">
                                                                        <div class="col-md-4 col-sm-12">
                                                                              <%
                                                                                    if ("true".equals((String) request.getAttribute("edit"))) {
                                                                              %>
                                                                              <form:input type="hidden" path="id" />
                                                                              <%
                                                                                    }
                                                                              %>
                                                                              <div class="form-group">
                                                                                    <form:label path="feedbackName" for="feedbackName">Feedback Name <span style="color: red">*</span></form:label>
                                                                                    
                                                                                    <form:input id="feedbackName" path="feedbackName"
                                                                                          type="text" placeholder="Feedback Name"
                                                                                          class="form-control" required="required" />
                                                                              </div>
                                                                        </div>
                                                                        <c:if test="${feedbackTypeMakeLive eq 'Y'}">
                                                                        <div class="col-md-4 col-sm-12">
                                                                              <div class="form-group">
                                                                                    <label path="feedbackType" for="feedbackType">Select Feedback Type <span
																						style="color: red">*</span></label> 
																					<form:select
																						id="feedbackType" path="feedbackType" required="required"
																						class="form-control">
																						
																						<form:option value="">Select Feedback Type</form:option>
																						<c:if test="${feedback.feedbackType eq 'mid-term' }">
                                                                                            <form:option value="mid-term" selected="true">Mid-Term</form:option>
																							<form:option value="end-term">End-Term</form:option>
																							<form:option value="it-feedback">IT Feedback</form:option>
                                                                                        </c:if>
                                                                                        <c:if test="${feedback.feedbackType eq 'end-term' }">
                                                                                            <form:option value="mid-term">Mid-Term</form:option>
																							<form:option value="end-term" selected="true">End-Term</form:option>
																							<form:option value="it-feedback">IT Feedback</form:option>
                                                                                        </c:if>
                                                                                         <c:if test="${feedback.feedbackType eq 'it-feedback' }">
                                                                                            <form:option value="mid-term">Mid-Term</form:option>
																							<form:option value="end-term">End-Term</form:option>
																							<form:option value="it-feedback" selected="true">IT Feedback</form:option>
                                                                                        </c:if>
                                                                                        <c:if test="${empty feedback.feedbackType }">
																							<form:option value="mid-term">Mid-Term</form:option>
																							<form:option value="end-term">End-Term</form:option>
																							<form:option value="it-feedback">IT Feedback</form:option>
																						</c:if>
																					</form:select>
                                                                              </div>
                                                                        </div>
                                                                        </c:if>
                                                                  </div>
                                                                  <c:if test="${edit eq true and showDates eq true}">
                                                                  <p> <Strong>Note : Once allocated to students feedback name and type cannot be changed!</Strong></p>
                                                                        <div class="row">
                                                                              <div class="col-md-4 col-sm-12">
                                                                                    <div class="form-group">
                                                                                          <form:label path="startDate" for="startDate">Start Date <span style="color: red">*</span></form:label>
                                                                                          <%-- <form:input path="endDate" type="datetime-local"
                                                                                          class="form-control" value="${assignment.endDate}"
                                                                                          required="required" /> --%>

                                                                                          <div class='input-group date' id='datetimepicker2'>
                                                                                                <form:input id="startDate" path="startDate" type="text"
                                                                                                      placeholder="Start Date" class="form-control"
                                                                                                      required="required" readonly="true" />
                                                                                                <span class="input-group-addon"><span
                                                                                                      class="glyphicon glyphicon-calendar"></span> </span>
                                                                                          </div>

                                                                                    </div>
                                                                              </div>



                                                                              <div class="col-md-4 col-sm-12">
                                                                                    <div class="form-group">
                                                                                          <form:label path="endDate" for="endDate">End Date <span style="color: red">*</span></form:label>
                                                                                          <%-- <form:input path="endDate" type="datetime-local"
                                                                                          class="form-control" value="${assignment.endDate}"
                                                                                          required="required" /> --%>

                                                                                          <div class='input-group date' id='datetimepicker2'>
                                                                                                <form:input id="endDate" path="endDate" type="text"
                                                                                                      placeholder="End Date" class="form-control"
                                                                                                      required="required" readonly="true" />
                                                                                                <span class="input-group-addon"><span
                                                                                                      class="glyphicon glyphicon-calendar"></span> </span>
                                                                                          </div>

                                                                                    </div>
                                                                              </div>






                                                                        </div>
                                                                  </c:if>
                                                                  <div class="row">

                                                                        <div class="col-sm-12 column">
                                                                              <div class="form-group">
                                                                                    <%
                                                                                          if ("true".equals((String) request.getAttribute("edit"))) {
                                                                                    %>
                                                                                    <button id="submit" class="btn btn-large btn-primary"
                                                                                          formaction="updateFeedback">Update</button>
                                                                                    <%
                                                                                          } else {
                                                                                    %>
                                                                                    <button id="submit" class="btn btn-large btn-primary"
                                                                                          formaction="addFeedback">Add</button>
                                                                                    <%
                                                                                          }
                                                                                    %>
                                                                                    <button id="cancel" class="btn btn-danger"
                                                                                          formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
                                                                              </div>
                                                                        </div>

                                                                  </div>


                                                            </form:form>
                                                            </div>
                                                            </div>
                  <!-- /page content: END -->
                   
                    </div>
                  
                  <!-- SIDEBAR START -->

              <!-- SIDEBAR END -->
              
      <jsp:include page="../common/newAdminFooter.jsp"/>

<script>
$(window).bind("pageshow", function(){
	$("#startDate").val('');
	$('#endDate').val('');
	  		  $('#startDate').daterangepicker({
	  		      autoUpdateInput: false,
	  		      locale: {
	  		          cancelLabel: 'Clear'
	  		      },
	  		      "singleDatePicker": true,
	          	  "showDropdowns" : true,
	                "timePicker" : true,
	                "showCustomRangeLabel" : false,
	                "alwaysShowCalendars" : true,
	                "opens" : "center"
	  		  });

	  		  $('#startDate').on('apply.daterangepicker', function(ev, picker) {
	  		      $(this).val(picker.startDate.format('YYYY-MM-DD HH:mm:ss'));
	  		  });

	  		  $('#startDate').on('cancel.daterangepicker', function(ev, picker) {
	  		      $(this).val('');
	  		  });
	  		  
	  		  
	  		$('#endDate').daterangepicker({
	  		      autoUpdateInput: false,
	  		      locale: {
	  		          cancelLabel: 'Clear'
	  		      },
	  		      "singleDatePicker": true,
	          	  "showDropdowns" : true,
	                "timePicker" : true,
	                "showCustomRangeLabel" : false,
	                "alwaysShowCalendars" : true,
	                "opens" : "center"
	  		  });

	  		  $('#endDate').on('apply.daterangepicker', function(ev, picker) {
	  		      $(this).val(picker.startDate.format('YYYY-MM-DD HH:mm:ss'));
	  		  });

	  		  $('#endDate').on('cancel.daterangepicker', function(ev, picker) {
	  		      $(this).val('');
	  		  });

}); 
</script>

      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      

                  

