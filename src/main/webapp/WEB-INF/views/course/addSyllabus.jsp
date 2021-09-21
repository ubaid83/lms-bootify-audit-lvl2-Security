<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<html lang="en">
<jsp:include page="../common/css.jsp" />

<body class="nav-md footer_fixed">
    
    <div class="loader"></div>
    
    
    <div class="container body">
        <div class="main_container">
            
            <jsp:include page="../common/leftSidebar.jsp">
	<jsp:param name="courseId" value="${courseId}" />
</jsp:include>
       
		<jsp:include page="../common/topHeader.jsp" />
        
            
            
            <!-- page content: START -->
            <div class="right_col" role="main">
                
                <div class="dashboard_container">
                    
                    <div class="dashboard_container_spacing">
                        
                        <div class="breadcrumb">
                        <c:out value="${Program_Name}" />
						
						<sec:authorize access="hasRole('ROLE_STUDENT')">
						
						<i
						class="fa fa-angle-right"></i>
											
											<c:out value="${AcadSession}" />
											
											</sec:authorize>
                        
                       
						<br><br>
                        
							<a href="${pageContext.request.contextPath}/homepage">Dashboard</a>
							<i class="fa fa-angle-right"></i>  
							Add Syllabus
						</div>
                        <jsp:include page="../common/alert.jsp" />
						<!-- Input Form Panel -->
                        <div class="row">
                            <div class="col-xs-12 col-sm-12">
                                <div class="x_panel">
								
                                    <div class="x_title">
                                        <h3>${courseName}</h3>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a></li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
									
                                    <div class="x_content">
                                       <form action="" id="addSyllabusForm" method="post">
				

					<fieldset>

					
					
					<input id="courseId" type="hidden" />
<div id="POItablediv">
    <input type="button" id="addPOIbutton" value="Add Row"/><br/><br/>
    <table id="POITable" border="1">
        <tr>
        	<td>Sr.No</td>
            <td>Unit</td>
            <td>Description</td>
            <td>Duration</td>
            <td>Text Books  </td>
            <td>Reference Books </td>
             <td>Delete?</td>
            <td>Add Rows?</td>
        </tr>
        <tr>
            <td>1</td>
            <td><input class="form-control" name="unit" placeholder="Unit" id="unit"/></td>
            <td><input class="form-control" name="description" placeholder="description" id="description"/></td>
            <td><input class="form-control" name="duration" placeholder="duration" id="duration"/></td>
            <td><input class="form-control" name="textbooks" placeholder="Text Books"/></td>
			<td><input class="form-control" name="referbooks" placeholder="Reference Books"/></td>
                <td><input type="button" id="delPOIbutton" value="Delete" onclick="deleteRow(this)"/></td>
            <td><input type="button" id="addmorePOIbutton" value="Add More " onclick="insRow()"/></td>
        </tr>
    </table>
    </div>
    <button id="submit" class="btn btn-large btn-primary" formaction=addSyllabus>Submit</button>

					<button id="cancel" class="btn btn-danger" formaction="homepage"
						formnovalidate="formnovalidate">Cancel</button>
						
		
				</fieldset>

			</form>
                                    </div>
                                </div>
                            </div>
                        </div>
                        
						<!-- Results Panel -->
                        
                        
                    </div>
                    
                    
                        
                </div>
                
            </div>
            <!-- /page content: END -->
              </div>
    </div>
            
       
            <jsp:include page="../common/footer.jsp" />
            <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script>

	function deleteRow(row)
	{
	    var i=row.parentNode.parentNode.rowIndex;
	    document.getElementById('POITable').deleteRow(i);
	}


	function insRow()
	{

	    var x=document.getElementById('POITable');
	    var new_row = x.rows[1].cloneNode(true);
	    var len = x.rows.length;
	    new_row.cells[0].innerHTML = len;
	    
	    var inp1 = new_row.cells[1].getElementsByTagName('input')[0];
	    inp1.id += len;
	    inp1.value = '';
	    var inp2 = new_row.cells[2].getElementsByTagName('input')[0];
	    inp2.id += len;
	    inp2.value = '';
	    var inp3 = new_row.cells[3].getElementsByTagName('input')[0];
	    inp3.id += len;
	    inp3.value = '';
	    var inp4 = new_row.cells[4].getElementsByTagName('input')[0];
	    inp4.id += len;
	    inp4.value = '';
	    var inp5 = new_row.cells[5].getElementsByTagName('input')[0];
	    inp5.id += len;
	    inp5.value = '';
	    x.appendChild( new_row );
	}



</script>
            
      


    
    
    
</body>
</html>
