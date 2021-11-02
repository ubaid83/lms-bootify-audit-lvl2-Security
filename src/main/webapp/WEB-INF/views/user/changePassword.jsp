<%@page import="com.spts.lms.web.utils.Utils"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@page import="java.util.*"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<sec:authorize access="hasAnyRole('ROLE_EXAM', 'ROLE_IT')">
<html lang="en">
<jsp:include page="../common/css.jsp" />

<body class="nav-md footer_fixed">

	<div class="loader"></div>


	<div class="container body">
		<div class="main_container">

			<jsp:include page="../common/topHeaderLibrian.jsp" />

			<!-- page content: START -->
			<div class="right_col" role="main">

				<div class="dashboard_container">

					<div class="dashboard_container_spacing">

						<div class="breadcrumb">
							<a href="${pageContext.request.contextPath}/homepage">Dashboard</a>
							<i class="fa fa-angle-right"></i> Profile
						</div>
						<jsp:include page="../common/alert.jsp" />

						<!-- Input Form Panel -->
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>Change Password</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
										<form:form action="changePassword" id="" method="post"
											modelAttribute="user">

											<form:input path="username" type="hidden" />
											<form:input path="id" type="hidden" />

											<div class="row">
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">

														<form:password path="oldPassword" cssClass="form-control"
															placeholder="Current Password" id="oldPassword"
															required="required" />
														<label><i class="fa fa-key"></i> <span style="color: red">*</span></label>

													</div>
												</div>

												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:password path="newPassword" cssClass="form-control"
															placeholder="Enter New Password" id="password"
															required="required" />
														<label><i class="fa fa-lock"></i> <span style="color: red">*</span></label>
													</div>
												</div>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:password path="reenterPassword"
															name="reenterPassword"
															placeholder="Re Enter New Password" id="reenterPassword"
															class="form-control" required="required" />
														<label><i class="fa fa-lock"></i> <span style="color: red">*</span></label>
													</div>
												</div>

											</div>



											<br>
											<button id="submit" class="btn btn-danger"
												formaction="changePassword" formnovalidate="formnovalidate"
												onclick="generateHashKey()">Change
												Password Details</button>

											<button id="cancel" class="btn btn-danger"
												formaction="homepage" formnovalidate="formnovalidate">BACK</button>
										</form:form>
									</div>
								</div>
							</div>
						</div>


					</div>



				</div>

			</div>
			<!-- /page content: END -->


			<jsp:include page="../common/footerLibrarian.jsp" />

		</div>
	</div>





</body>
</html>

</sec:authorize>

<sec:authorize access="hasAnyRole('ROLE_STUDENT', 'ROLE_FACULTY', 'ROLE_PARENT','ROLE_STAFF')">
<jsp:include page="../common/newDashboardHeader.jsp" /> 

<div class="d-flex dataTableBottom" id="facultyAssignmentPage">
	<sec:authorize access="hasRole('ROLE_FACULTY')">
		<jsp:include page="../common/newLeftNavBarFaculty.jsp"/>
		<jsp:include page="../common/newLeftSidebarFaculty.jsp"/>
		<jsp:include page="../common/rightSidebarFaculty.jsp"/>
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_STUDENT')">
		<jsp:include page="../common/newLeftNavbar.jsp" />
		<jsp:include page="../common/newLeftSidebar.jsp" />
		<jsp:include page="../common/rightSidebar.jsp" />
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_PARENT')">
	<jsp:include page="../common/newLeftNavbar.jsp"/>
		<jsp:include page="../common/newLeftSidebarParent.jsp" />
	</sec:authorize>

<!-- DASHBOARD BODY STARTS HERE -->

<div class="container-fluid m-0 p-0 dashboardWraper">

	<sec:authorize access="hasRole('ROLE_FACULTY')">
		<jsp:include page="../common/newTopHeaderFaculty.jsp" />
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_STUDENT')">
		<jsp:include page="../common/newTopHeader.jsp" />
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_PARENT')">
	<jsp:include page="../common/newTopHeader.jsp" />
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_STAFF')">
		<div class="container-fluid m-0 p-0 dashboardWraper">
			<header class="container-fluid sticky-top">
				<nav class="navbar navbar-expand-lg navbar-light p-0">
					<!--                     <i class="fas fa-bars fa-2x mr-3 leftnav-btn" data-toggle="modal" data-target="#leftnav"></i> -->
					<a class="navbar-brand" href="homepage"> <c:choose>
							<c:when test="${instiFlag eq 'nm'}">
								<img src="<c:url value="/resources/images/logo.png" />"
									class="logo" title="NMIMS logo" alt="NMIMS logo" />
							</c:when>
							<c:otherwise>
								<img src="<c:url value="/resources/images/svkmlogo.png" />"
									class="logo" title="SVKM logo" alt="SVKM logo" />
							</c:otherwise>
						</c:choose>

					</a>
					<button class="adminNavbarToggler" type="button"
						data-toggle="collapse" data-target="#adminNavbarCollapse">
						<i class="fas fa-bars"></i>
					</button>

					<div class="collapse navbar-collapse" id="adminNavbarCollapse">
						<ul class="navbar-nav ml-auto">

							<li id="program" class="nav-item active" data-toggle="tooltip"
								data-placement="bottom" title="homepage"><a
								class="nav-link" href="homepage"><i class="fas fa-home"></i>
									<span>Home</span></a></li>

						</ul>
					</div>
				</nav>
			</header>
	</sec:authorize>
     
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
                                <li class="breadcrumb-item active" aria-current="page">Profile</li>
                            </ol>
                        </nav>
                        
						<jsp:include page="../common/alert.jsp" />
						
						
						<!-- Input Form Panel -->
						<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h5>Change Password</h5>
										
									</div>

									<div class="x_content">
										<form:form action="changePassword" id="" method="post"
											modelAttribute="user">

											<form:input path="username" type="hidden" />
											<form:input path="id" type="hidden" />

											<div class="row">
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">

														<form:password path="oldPassword" cssClass="form-control"
															placeholder="Current Password" id="oldPassword"
															required="required" />
														<label><i class="fa fa-key"></i> <span style="color: red">*</span></label>

													</div>
												</div>

												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:password path="newPassword" cssClass="form-control"
															placeholder="Enter New Password" id="password"
															required="required" />
														<label><i class="fa fa-lock"></i> <span style="color: red">*</span></label>
													</div>
												</div>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:password path="reenterPassword"
															name="reenterPassword"
															placeholder="Re Enter New Password" id="reenterPassword"
															class="form-control" required="required" />
														<label><i class="fa fa-lock"></i> <span style="color: red">*</span></label>
													</div>
												</div>

											</div>


											<br>
											<span id="errMessage" style="color: red"></span>
											<br>
											<button id="submit" class="btn btn-danger"
												formaction="changePassword" formnovalidate="formnovalidate"
												onclick="generateHashKey()" disabled="true">Change
												Password Details</button>

											<button id="cancel" class="btn btn-danger"
												formaction="homepage" formnovalidate="formnovalidate">Back</button>
										</form:form>
									</div>
								</div>
							</div>
						</div>
						</div>

						<!-- Results Panel -->

						
								
							

					<%-- 	<jsp:include page="../common/paginate.jsp">
							<jsp:param name="baseUrl" value="searchFacultyGroups" />
						</jsp:include> --%>

			<!-- /page content: END -->
                   
                    </div>
			
			<!-- SIDEBAR START -->
			<sec:authorize access="hasAnyRole('ROLE_FACULTY','ROLE_STUDENT', 'ROLE_PARENT')">
	         <jsp:include page="../common/newSidebar.jsp" />
	         </sec:authorize>
	        <!-- SIDEBAR END -->
	<sec:authorize access="hasAnyRole('ROLE_FACULTY','ROLE_STUDENT', 'ROLE_PARENT')">
		<jsp:include page="../common/footer.jsp" />
	</sec:authorize>
	
	<sec:authorize access="hasRole('ROLE_STAFF')">
		<jsp:include page="../common/newAdminFooter.jsp" />
	</sec:authorize>
	
	</sec:authorize>
	
	
	<script>
	 $('#password').on('change',function () {
		 var passwd = $('#password').val();
		 var oldPasswd = $('#oldPassword').val();
		 var reg = new RegExp("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$");
		 if (oldPasswd && reg.test(passwd)) {
		    console.log("Valid");
			$('span[id="errMessage"]').text("");
		 } else {
		    console.log("Invalid");
		    $('#submit').attr('disabled',true);
			$('span[id="errMessage"]').text("Password should have atleast 1 digit, 1 upper case alphabet, 1 lower case alphabet, 1 special character & atleast 8 characters and at most 20 characters!");
		 }
	 })
	 $('#password').on('keyup',function () {
		 var passwd = $('#password').val();
		 var oldPasswd = $('#oldPassword').val();
		 var reg = new RegExp("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$");
		 if (oldPasswd && reg.test(passwd)) {
		    console.log("Valid");
			$('span[id="errMessage"]').text("");
		 } else {
		    console.log("Invalid");
		    $('#submit').attr('disabled',true);
			$('span[id="errMessage"]').text("Password should have atleast 1 digit, 1 upper case alphabet, 1 lower case alphabet, 1 special character & atleast 8 characters and at most 20 characters!");
		 }
	 })
	  $('#reenterPassword').on('change',function () {
		  var passwd = $('#password').val();
			var oldPasswd = $('#oldPassword').val();
			var reenterPasswd = $('#reenterPassword').val();
		   var reg = new RegExp("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$");
		   /* if(oldPasswd){
			   $('span[id="errMessage"]').text("Fill all fields!");
		   }else  */if(oldPasswd && passwd.localeCompare(reenterPasswd) === 0){
			
			if (reg.test(reenterPasswd)) {
			    console.log("Valid");
			    $('#submit').attr('disabled',false);
				$('span[id="errMessage"]').text("");
			} else {
			    console.log("Invalid");
			    $('#submit').attr('disabled',true);
				$('span[id="errMessage"]').text("Unable to change the password. Password should have atleast 1 digit, 1 upper case alphabet, 1 lower case alphabet, 1 special character & atleast 8 characters and at most 20 characters!");
				
			}
			} else {
				console.log("Invalid Pass");
				$('#submit').attr('disabled',true);
				$('span[id="errMessage"]').text("Password didn't Match!");
				
			}
		});
	  $('#reenterPassword').on('keyup',function () {
		   var passwd = $('#password').val();
			var oldPasswd = $('#oldPassword').val();
			var reenterPasswd = $('#reenterPassword').val();
		   var reg = new RegExp("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$");
		   /* if(oldPasswd){
			   $('span[id="errMessage"]').text("Fill all fields!");
		   }else  */
		   if(oldPasswd && passwd.localeCompare(reenterPasswd) === 0){
			
			if (reg.test(passwd)) {
			    console.log("Valid");
			    $('#submit').attr('disabled',false);
				$('span[id="errMessage"]').text("");
			} else {
			    console.log("Invalid");
			    $('#submit').attr('disabled',true);
				$('span[id="errMessage"]').text("Unable to change the password. Password should have atleast 1 digit, 1 upper case alphabet, 1 lower case alphabet, 1 special character & atleast 8 characters and at most 20 characters!");
				
			}
			} else {
				console.log("Invalid Pass");
				$('#submit').attr('disabled',true);
				$('span[id="errMessage"]').text("Password didn't Match!");
				
			}
		});
	function generateHashKey(){
		//var salt = $('#salt').val();
		
		var passwd = $('#password').val();
		var oldPasswd = $('#oldPassword').val();
		var reenterPasswd = $('#reenterPassword').val();
		
		var encPasswd = SHA256(passwd);
		var encOp = SHA256(oldPasswd);
		var encRp = SHA256(reenterPasswd);
		
		document.getElementById("password").value=encPasswd;
		document.getElementById("oldPassword").value=encOp;
		document.getElementById("reenterPassword").value=encRp;
		
		
	}
 
 </script>
        
<script>

function SHA256(s){
	 var chrsz = 8;
	 var hexcase = 0;

	 function safe_add (x, y) {
	 var lsw = (x & 0xFFFF) + (y & 0xFFFF);
	 var msw = (x >> 16) + (y >> 16) + (lsw >> 16);
	 return (msw << 16) | (lsw & 0xFFFF);
	 }

	 function S (X, n) { return ( X >>> n ) | (X << (32 - n)); }
	 function R (X, n) { return ( X >>> n ); }
	 function Ch(x, y, z) { return ((x & y) ^ ((~x) & z)); }
	 function Maj(x, y, z) { return ((x & y) ^ (x & z) ^ (y & z)); }
	 function Sigma0256(x) { return (S(x, 2) ^ S(x, 13) ^ S(x, 22)); }
	 function Sigma1256(x) { return (S(x, 6) ^ S(x, 11) ^ S(x, 25)); }
	 function Gamma0256(x) { return (S(x, 7) ^ S(x, 18) ^ R(x, 3)); }
	 function Gamma1256(x) { return (S(x, 17) ^ S(x, 19) ^ R(x, 10)); }

	 function core_sha256 (m, l) {
	 var K = new Array(0x428A2F98, 0x71374491, 0xB5C0FBCF, 0xE9B5DBA5, 0x3956C25B, 0x59F111F1, 0x923F82A4, 0xAB1C5ED5, 0xD807AA98, 0x12835B01, 0x243185BE, 0x550C7DC3, 0x72BE5D74, 0x80DEB1FE, 0x9BDC06A7, 0xC19BF174, 0xE49B69C1, 0xEFBE4786, 0xFC19DC6, 0x240CA1CC, 0x2DE92C6F, 0x4A7484AA, 0x5CB0A9DC, 0x76F988DA, 0x983E5152, 0xA831C66D, 0xB00327C8, 0xBF597FC7, 0xC6E00BF3, 0xD5A79147, 0x6CA6351, 0x14292967, 0x27B70A85, 0x2E1B2138, 0x4D2C6DFC, 0x53380D13, 0x650A7354, 0x766A0ABB, 0x81C2C92E, 0x92722C85, 0xA2BFE8A1, 0xA81A664B, 0xC24B8B70, 0xC76C51A3, 0xD192E819, 0xD6990624, 0xF40E3585, 0x106AA070, 0x19A4C116, 0x1E376C08, 0x2748774C, 0x34B0BCB5, 0x391C0CB3, 0x4ED8AA4A, 0x5B9CCA4F, 0x682E6FF3, 0x748F82EE, 0x78A5636F, 0x84C87814, 0x8CC70208, 0x90BEFFFA, 0xA4506CEB, 0xBEF9A3F7, 0xC67178F2);
	 var HASH = new Array(0x6A09E667, 0xBB67AE85, 0x3C6EF372, 0xA54FF53A, 0x510E527F, 0x9B05688C, 0x1F83D9AB, 0x5BE0CD19);
	 var W = new Array(64);
	 var a, b, c, d, e, f, g, h, i, j;
	 var T1, T2;

	 m[l >> 5] |= 0x80 << (24 - l % 32);
	 m[((l + 64 >> 9) << 4) + 15] = l;

	 for ( var i = 0; i<m.length; i+=16 ) {
	 a = HASH[0];
	 b = HASH[1];
	 c = HASH[2];
	 d = HASH[3];
	 e = HASH[4];
	 f = HASH[5];
	 g = HASH[6];
	 h = HASH[7];

	 for ( var j = 0; j<64; j++) {
	 if (j < 16) W[j] = m[j + i];
	 else W[j] = safe_add(safe_add(safe_add(Gamma1256(W[j - 2]), W[j - 7]), Gamma0256(W[j - 15])), W[j - 16]);

	 T1 = safe_add(safe_add(safe_add(safe_add(h, Sigma1256(e)), Ch(e, f, g)), K[j]), W[j]);
	 T2 = safe_add(Sigma0256(a), Maj(a, b, c));

	 h = g;
	 g = f;
	 f = e;
	 e = safe_add(d, T1);
	 d = c;
	 c = b;
	 b = a;
	 a = safe_add(T1, T2);
	 }

	 HASH[0] = safe_add(a, HASH[0]);
	 HASH[1] = safe_add(b, HASH[1]);
	 HASH[2] = safe_add(c, HASH[2]);
	 HASH[3] = safe_add(d, HASH[3]);
	 HASH[4] = safe_add(e, HASH[4]);
	 HASH[5] = safe_add(f, HASH[5]);
	 HASH[6] = safe_add(g, HASH[6]);
	 HASH[7] = safe_add(h, HASH[7]);
	 }
	 return HASH;
	 }

	 function str2binb (str) {
	 var bin = Array();
	 var mask = (1 << chrsz) - 1;
	 for(var i = 0; i < str.length * chrsz; i += chrsz) {
	 bin[i>>5] |= (str.charCodeAt(i / chrsz) & mask) << (24 - i % 32);
	 }
	 return bin;
	 }

	 function Utf8Encode(string) {
	 string = string.replace(/\r\n/g,'\n');
	 var utftext = '';

	 for (var n = 0; n < string.length; n++) {

	 var c = string.charCodeAt(n);

	 if (c < 128) {
	 utftext += String.fromCharCode(c);
	 }
	 else if((c > 127) && (c < 2048)) {
	 utftext += String.fromCharCode((c >> 6) | 192);
	 utftext += String.fromCharCode((c & 63) | 128);
	 }
	 else {
	 utftext += String.fromCharCode((c >> 12) | 224);
	 utftext += String.fromCharCode(((c >> 6) & 63) | 128);
	 utftext += String.fromCharCode((c & 63) | 128);
	 }

	 }

	 return utftext;
	 }

	 function binb2hex (binarray) {
	 var hex_tab = hexcase ? '0123456789ABCDEF' : '0123456789abcdef';
	 var str = '';
	 for(var i = 0; i < binarray.length * 4; i++) {
	 str += hex_tab.charAt((binarray[i>>2] >> ((3 - i % 4)*8+4)) & 0xF) +
	 hex_tab.charAt((binarray[i>>2] >> ((3 - i % 4)*8 )) & 0xF);
	 }
	 return str;
	 }

	 s = Utf8Encode(s);
	 return binb2hex(core_sha256(str2binb(s), s.length * chrsz));
	}
	
	</script>