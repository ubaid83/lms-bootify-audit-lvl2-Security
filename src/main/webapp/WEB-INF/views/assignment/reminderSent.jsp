<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"
      uri="http://www.springframework.org/security/tags"%>
      
      
      
      <jsp:include page="../common/newDashboardHeader.jsp" /> 

<div class="d-flex" id="facultyAssignmentPage">
<jsp:include page="../common/newLeftNavBarFaculty.jsp"/>
<jsp:include page="../common/newLeftSidebarFaculty.jsp"/>
<jsp:include page="../common/rightSidebarFaculty.jsp"/>

<!-- DASHBOARD BODY STARTS HERE -->

<div class="container-fluid m-0 p-0 dashboardWraper">

      <jsp:include page="../common/newTopHeaderFaculty.jsp" />
     
     <div class="container mt-5">
                <div class="row">
            <!-- SEMESTER CONTENT -->    
                    <div class="col-lg-9 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto">
                        
                        <!-- page content: START -->
                       <nav aria-label="breadcrumb">
                            <ol class="breadcrumb">
                                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
                                <li class="breadcrumb-item" aria-current="page"><c:out value="${Program_Name}" /></li>
                                <sec:authorize access="hasRole('ROLE_STUDENT')">
                                                <c:out value="${AcadSession}" />
                                          </sec:authorize>
                                <li class="breadcrumb-item active" aria-current="page"> Search Assignment</li>
                            </ol>
                        </nav>

                                    <jsp:include page="../common/alert.jsp" />

                                    <!-- Input Form Panel -->
                                    <div class="card bg-white border">
                                          <div class="card-body">

                                                            <h5 class="text-center border-bottom pb-2">Not submitted assignments</h5>
                                                            <form:form modelAttribute="assignment" />




                                                            <!-- 
                                                <button id="cancel" class="btn btn-danger" value="viewForum"
                                                      formnovalidate="formnovalidate">Back</button> -->

                                                            <a href="searchAssignmentToEvaluate"><button
                                                                        class="btn btn-danger"
                                                                        style="width: 150px; height: 50px; margin-top; 20%; margin-left: 40%;">BACK</button></a>
                                          </div>
                                    </div>
                  <!-- /page content: END -->
                   
                    </div>
                  
                  <!-- SIDEBAR START -->
               <jsp:include page="../common/newSidebar.jsp" />
              <!-- SIDEBAR END -->
      <jsp:include page="../common/footer.jsp"/>

