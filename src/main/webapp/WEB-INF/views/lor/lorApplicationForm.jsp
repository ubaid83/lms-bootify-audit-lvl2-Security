<%@page import="java.util.*"%>
<%@page import="com.spts.lms.web.utils.Utils"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<head>

<%-- <link href="<c:url value="resources/css/lor/bootstrap.min.css" />" rel="stylesheet"> --%>
<%-- <link href="<c:url value="resources/css/lor/bootstrap-select.min.css" />" rel="stylesheet"> --%>
<%-- <link href="<c:url value="resources/css/lor/font-awesome.min.css" />" rel="stylesheet"> --%>
</head>
	
<jsp:include page="../common/newDashboardHeader.jsp" />

<div class="d-flex" id="lorApplicationPage">
<sec:authorize access="hasRole('ROLE_STUDENT')">
	<jsp:include page="../common/newLeftNavbar.jsp" />
	<jsp:include page="../common/newLeftSidebar.jsp" />
	<jsp:include page="../common/rightSidebar.jsp" />
</sec:authorize>

<% boolean isEdit = "true".equals((String) request.getAttribute("edit")); %>

<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper">

		<sec:authorize access="hasRole('ROLE_STUDENT')">
			<jsp:include page="../common/newTopHeader.jsp" />
		</sec:authorize>
		<div class="container mt-5">
			<div class="row">
				<!-- SEMESTER CONTENT -->
				<div class="col-lg-9 col-md-9 col-sm-12 dashboardBody">
					<!-- page content: START -->
					<nav aria-label="breadcrumb">
						<ol class="breadcrumb">
							<li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
							<li class="breadcrumb-item active" aria-current="page">Lor Application</li>
						</ol>
					</nav>
					<jsp:include page="../common/alert.jsp" />
					<!-- Input Form Panel -->
					
					<div class="card bg-white border">
					<form:form action="saveLorApplicationDetails" id="lorForm" method="post"
											modelAttribute="lorRegDetails" enctype="multipart/form-data">
						<div class="card-header-lor">
							<h4 id="title" class="text-center">Application For LOR</h4>
							<h5 id="stepTitle" class="text-center">Step 1- Fill your entrance exam details</h5>
						</div>
						<hr>
						<div class="card-body" id="stepOneLor">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">
									<div class="x_content">
										
											<form:hidden path="username" value="${userdetails.username}" />
											<form:hidden path="name"
												value="${userdetails.firstname} ${userdetails.fatherName} ${userdetails.lastname}" />
											<form:hidden path="email" value="${userdetails.email}" />
											<form:hidden path="mobile" value="${userdetails.mobile}" />
											<form:hidden path="programEnrolledId" value="${programName.id}" />

						                        <div class="row">
						                            <div class="col-lg-4">
						                                <label for=""> <b>SAP ID</b></label>
						                                <input type="text" class="form-control" readonly value="${userdetails.username}">
						                            </div>
						                            <div class="col-lg-4">
						                                <label for=""><b>NAME</b></label>
						                                <input type="text" class="form-control" readonly value="${userdetails.firstname} ${userdetails.fatherName} ${userdetails.lastname}">
						                            </div>
						                            <div class="col-lg-4">
						                                <label for=""><b>EMAIL</b></label>
						                                <input type="text" class="form-control" readonly value="${userdetails.email}">
						                            </div>
						                            <div class="col-lg-4 pt-4">
						                                <label for=""><b>MOBILE NUMBER</b></label>
						                                <input type="text" class="form-control" readonly value="${userdetails.mobile}">
						                            </div>
						                            <div class="col-lg-4 pt-4">
						                                <label for=""><b>PROGRAM ENROLLED</b></label>
						                                <textarea class="form-control" readonly>${programName.programName}</textarea>
						                            </div>
						                        </div>
						                        
							                <hr>
							                
											
                   								<div class="row" id="countryAndUniversity">
                   									
                           			 				<div class="col-lg-6 col-md-12 col-sm-12 mt-2 mb-2" id="c1">
                               	 						<label for=""> <b>COUNTRY FOR HIGHER STUDIES&nbsp;</strong> 
                               	 						<span class="text-danger">*</span></b> 
                               	 						<span data-toggle="tooltip" data-placement="top" title="You Can Choose Multiple Countries for Higher studies">
                               	 						<i class="fa fa-info-circle"  aria-hidden="true"></i></span></label>
                                						<select id="countryForHigherStudy" class="form-control" name="countryForHigherStudy"
                                								name="countryForHigherStudy" path="countryForHigherStudy" required="true">
                                    						<option value="" disabled="true" selected>Select Country</option>
						                                    <option value="Afganistan">Afghanistan</option>
						                                    <option value="Albania">Albania</option>
						                                    <option value="Algeria">Algeria</option>
						                                    <option value="American Samoa">American Samoa</option>
						                                    <option value="Andorra">Andorra</option>
						                                    <option value="Angola">Angola</option>
						                                    <option value="Anguilla">Anguilla</option>
						                                    <option value="Antigua &amp; Barbuda">Antigua &amp; Barbuda</option>
						                                    <option value="Argentina">Argentina</option>
						                                    <option value="Armenia">Armenia</option>
						                                    <option value="Aruba">Aruba</option>
						                                    <option value="Australia">Australia</option>
						                                    <option value="Austria">Austria</option>
						                                    <option value="Azerbaijan">Azerbaijan</option>
						                                    <option value="Bahamas">Bahamas</option>
						                                    <option value="Bahrain">Bahrain</option>
						                                    <option value="Bangladesh">Bangladesh</option>
						                                    <option value="Barbados">Barbados</option>
						                                    <option value="Belarus">Belarus</option>
						                                    <option value="Belgium">Belgium</option>
						                                    <option value="Belize">Belize</option>
						                                    <option value="Benin">Benin</option>
						                                    <option value="Bermuda">Bermuda</option>
						                                    <option value="Bhutan">Bhutan</option>
						                                    <option value="Bolivia">Bolivia</option>
						                                    <option value="Bonaire">Bonaire</option>
						                                    <option value="Bosnia &amp; Herzegovina">Bosnia &amp; Herzegovina</option>
						                                    <option value="Botswana">Botswana</option>
						                                    <option value="Brazil">Brazil</option>
						                                    <option value="British Indian Ocean Ter">British Indian Ocean Ter</option>
						                                    <option value="Brunei">Brunei</option>
						                                    <option value="Bulgaria">Bulgaria</option>
						                                    <option value="Burkina Faso">Burkina Faso</option>
						                                    <option value="Burundi">Burundi</option>
						                                    <option value="Cambodia">Cambodia</option>
						                                    <option value="Cameroon">Cameroon</option>
						                                    <option value="Canada">Canada</option>
						                                    <option value="Canary Islands">Canary Islands</option>
						                                    <option value="Cape Verde">Cape Verde</option>
						                                    <option value="Cayman Islands">Cayman Islands</option>
						                                    <option value="Central African Republic">Central African Republic</option>
						                                    <option value="Chad">Chad</option>
						                                    <option value="Channel Islands">Channel Islands</option>
						                                    <option value="Chile">Chile</option>
						                                    <option value="China">China</option>
						                                    <option value="Christmas Island">Christmas Island</option>
						                                    <option value="Cocos Island">Cocos Island</option>
						                                    <option value="Colombia">Colombia</option>
						                                    <option value="Comoros">Comoros</option>
						                                    <option value="Congo">Congo</option>
						                                    <option value="Cook Islands">Cook Islands</option>
						                                    <option value="Costa Rica">Costa Rica</option>
						                                    <option value="Cote DIvoire">Cote DIvoire</option>
						                                    <option value="Croatia">Croatia</option>
						                                    <option value="Cuba">Cuba</option>
						                                    <option value="Curaco">Curacao</option>
						                                    <option value="Cyprus">Cyprus</option>
						                                    <option value="Czech Republic">Czech Republic</option>
						                                    <option value="Denmark">Denmark</option>
						                                    <option value="Djibouti">Djibouti</option>
						                                    <option value="Dominica">Dominica</option>
						                                    <option value="Dominican Republic">Dominican Republic</option>
						                                    <option value="East Timor">East Timor</option>
						                                    <option value="Ecuador">Ecuador</option>
						                                    <option value="Egypt">Egypt</option>
						                                    <option value="El Salvador">El Salvador</option>
						                                    <option value="Equatorial Guinea">Equatorial Guinea</option>
						                                    <option value="Eritrea">Eritrea</option>
						                                    <option value="Estonia">Estonia</option>
						                                    <option value="Ethiopia">Ethiopia</option>
						                                    <option value="Falkland Islands">Falkland Islands</option>
						                                    <option value="Faroe Islands">Faroe Islands</option>
						                                    <option value="Fiji">Fiji</option>
						                                    <option value="Finland">Finland</option>
						                                    <option value="France">France</option>
						                                    <option value="French Guiana">French Guiana</option>
						                                    <option value="French Polynesia">French Polynesia</option>
						                                    <option value="French Southern Ter">French Southern Ter</option>
						                                    <option value="Gabon">Gabon</option>
						                                    <option value="Gambia">Gambia</option>
						                                    <option value="Georgia">Georgia</option>
						                                    <option value="Germany">Germany</option>
						                                    <option value="Ghana">Ghana</option>
						                                    <option value="Gibraltar">Gibraltar</option>
						                                    <option value="Great Britain">Great Britain</option>
						                                    <option value="Greece">Greece</option>
						                                    <option value="Greenland">Greenland</option>
						                                    <option value="Grenada">Grenada</option>
						                                    <option value="Guadeloupe">Guadeloupe</option>
						                                    <option value="Guam">Guam</option>
						                                    <option value="Guatemala">Guatemala</option>
						                                    <option value="Guinea">Guinea</option>
						                                    <option value="Guyana">Guyana</option>
						                                    <option value="Haiti">Haiti</option>
						                                    <option value="Hawaii">Hawaii</option>
						                                    <option value="Honduras">Honduras</option>
						                                    <option value="Hong Kong">Hong Kong</option>
						                                    <option value="Hungary">Hungary</option>
						                                    <option value="Iceland">Iceland</option>
						                                    <option value="Indonesia">Indonesia</option>
						                                    <option value="India">India</option>
						                                    <option value="Iran">Iran</option>
						                                    <option value="Iraq">Iraq</option>
						                                    <option value="Ireland">Ireland</option>
						                                    <option value="Isle of Man">Isle of Man</option>
						                                    <option value="Israel">Israel</option>
						                                    <option value="Italy">Italy</option>
						                                    <option value="Jamaica">Jamaica</option>
						                                    <option value="Japan">Japan</option>
						                                    <option value="Jordan">Jordan</option>
						                                    <option value="Kazakhstan">Kazakhstan</option>
						                                    <option value="Kenya">Kenya</option>
						                                    <option value="Kiribati">Kiribati</option>
						                                    <option value="Korea North">Korea North</option>
						                                    <option value="Korea Sout">Korea South</option>
						                                    <option value="Kuwait">Kuwait</option>
						                                    <option value="Kyrgyzstan">Kyrgyzstan</option>
						                                    <option value="Laos">Laos</option>
						                                    <option value="Latvia">Latvia</option>
						                                    <option value="Lebanon">Lebanon</option>
						                                    <option value="Lesotho">Lesotho</option>
						                                    <option value="Liberia">Liberia</option>
						                                    <option value="Libya">Libya</option>
						                                    <option value="Liechtenstein">Liechtenstein</option>
						                                    <option value="Lithuania">Lithuania</option>
						                                    <option value="Luxembourg">Luxembourg</option>
						                                    <option value="Macau">Macau</option>
						                                    <option value="Macedonia">Macedonia</option>
						                                    <option value="Madagascar">Madagascar</option>
						                                    <option value="Malaysia">Malaysia</option>
						                                    <option value="Malawi">Malawi</option>
						                                    <option value="Maldives">Maldives</option>
						                                    <option value="Mali">Mali</option>
						                                    <option value="Malta">Malta</option>
						                                    <option value="Marshall Islands">Marshall Islands</option>
						                                    <option value="Martinique">Martinique</option>
						                                    <option value="Mauritania">Mauritania</option>
						                                    <option value="Mauritius">Mauritius</option>
						                                    <option value="Mayotte">Mayotte</option>
						                                    <option value="Mexico">Mexico</option>
						                                    <option value="Midway Islands">Midway Islands</option>
						                                    <option value="Moldova">Moldova</option>
						                                    <option value="Monaco">Monaco</option>
						                                    <option value="Mongolia">Mongolia</option>
						                                    <option value="Montserrat">Montserrat</option>
						                                    <option value="Morocco">Morocco</option>
						                                    <option value="Mozambique">Mozambique</option>
						                                    <option value="Myanmar">Myanmar</option>
						                                    <option value="Nambia">Nambia</option>
						                                    <option value="Nauru">Nauru</option>
						                                    <option value="Nepal">Nepal</option>
						                                    <option value="Netherland Antilles">Netherland Antilles</option>
						                                    <option value="Netherlands">Netherlands (Holland, Europe)</option>
						                                    <option value="Nevis">Nevis</option>
						                                    <option value="New Caledonia">New Caledonia</option>
						                                    <option value="New Zealand">New Zealand</option>
						                                    <option value="Nicaragua">Nicaragua</option>
						                                    <option value="Niger">Niger</option>
						                                    <option value="Nigeria">Nigeria</option>
						                                    <option value="Niue">Niue</option>
						                                    <option value="Norfolk Island">Norfolk Island</option>
						                                    <option value="Norway">Norway</option>
						                                    <option value="Oman">Oman</option>
						                                    <option value="Pakistan">Pakistan</option>
						                                    <option value="Palau Island">Palau Island</option>
						                                    <option value="Palestine">Palestine</option>
						                                    <option value="Panama">Panama</option>
						                                    <option value="Papua New Guinea">Papua New Guinea</option>
						                                    <option value="Paraguay">Paraguay</option>
						                                    <option value="Peru">Peru</option>
						                                    <option value="Phillipines">Philippines</option>
						                                    <option value="Pitcairn Island">Pitcairn Island</option>
						                                    <option value="Poland">Poland</option>
						                                    <option value="Portugal">Portugal</option>
						                                    <option value="Puerto Rico">Puerto Rico</option>
						                                    <option value="Qatar">Qatar</option>
						                                    <option value="Republic of Montenegro">Republic of Montenegro</option>
						                                    <option value="Republic of Serbia">Republic of Serbia</option>
						                                    <option value="Reunion">Reunion</option>
						                                    <option value="Romania">Romania</option>
						                                    <option value="Russia">Russia</option>
						                                    <option value="Rwanda">Rwanda</option>
						                                    <option value="St Barthelemy">St Barthelemy</option>
						                                    <option value="St Eustatius">St Eustatius</option>
						                                    <option value="St Helena">St Helena</option>
						                                    <option value="St Kitts-Nevis">St Kitts-Nevis</option>
						                                    <option value="St Lucia">St Lucia</option>
						                                    <option value="St Maarten">St Maarten</option>
						                                    <option value="St Pierre &amp; Miquelon">St Pierre &amp; Miquelon</option>
						                                    <option value="St Vincent &amp; Grenadines">St Vincent &amp; Grenadines
						                                    </option>
						                                    <option value="Saipan">Saipan</option>
						                                    <option value="Samoa">Samoa</option>
						                                    <option value="Samoa American">Samoa American</option>
						                                    <option value="San Marino">San Marino</option>
						                                    <option value="Sao Tome &amp; Principe">Sao Tome &amp; Principe</option>
						                                    <option value="Saudi Arabia">Saudi Arabia</option>
						                                    <option value="Senegal">Senegal</option>
						                                    <option value="Seychelles">Seychelles</option>
						                                    <option value="Sierra Leone">Sierra Leone</option>
						                                    <option value="Singapore">Singapore</option>
						                                    <option value="Slovakia">Slovakia</option>
						                                    <option value="Slovenia">Slovenia</option>
						                                    <option value="Solomon Islands">Solomon Islands</option>
						                                    <option value="Somalia">Somalia</option>
						                                    <option value="South Africa">South Africa</option>
						                                    <option value="Spain">Spain</option>
						                                    <option value="Sri Lanka">Sri Lanka</option>
						                                    <option value="Sudan">Sudan</option>
						                                    <option value="Suriname">Suriname</option>
						                                    <option value="Swaziland">Swaziland</option>
						                                    <option value="Sweden">Sweden</option>
						                                    <option value="Switzerland">Switzerland</option>
						                                    <option value="Syria">Syria</option>
						                                    <option value="Tahiti">Tahiti</option>
						                                    <option value="Taiwan">Taiwan</option>
						                                    <option value="Tajikistan">Tajikistan</option>
						                                    <option value="Tanzania">Tanzania</option>
						                                    <option value="Thailand">Thailand</option>
						                                    <option value="Togo">Togo</option>
						                                    <option value="Tokelau">Tokelau</option>
						                                    <option value="Tonga">Tonga</option>
						                                    <option value="Trinidad &amp; Tobago">Trinidad &amp; Tobago</option>
						                                    <option value="Tunisia">Tunisia</option>
						                                    <option value="Turkey">Turkey</option>
						                                    <option value="Turkmenistan">Turkmenistan</option>
						                                    <option value="Turks &amp; Caicos Is">Turks &amp; Caicos Is</option>
						                                    <option value="Tuvalu">Tuvalu</option>
						                                    <option value="Uganda">Uganda</option>
						                                    <option value="United Kingdom">United Kingdom</option>
						                                    <option value="Ukraine">Ukraine</option>
						                                    <option value="United Arab Erimates">United Arab Emirates</option>
						                                    <option value="United States of America">United States of America</option>
						                                    <option value="Uraguay">Uruguay</option>
						                                    <option value="Uzbekistan">Uzbekistan</option>
						                                    <option value="Vanuatu">Vanuatu</option>
						                                    <option value="Vatican City State">Vatican City State</option>
						                                    <option value="Venezuela">Venezuela</option>
						                                    <option value="Vietnam">Vietnam</option>
						                                    <option value="Virgin Islands (Brit)">Virgin Islands (Brit)</option>
						                                    <option value="Virgin Islands (USA)">Virgin Islands (USA)</option>
						                                    <option value="Wake Island">Wake Island</option>
						                                    <option value="Wallis &amp; Futana Is">Wallis &amp; Futana Is</option>
						                                    <option value="Yemen">Yemen</option>
						                                    <option value="Zaire">Zaire</option>
						                                    <option value="Zambia">Zambia</option>
						                                    <option value="Zimbabwe">Zimbabwe</option>
						                                </select>
                            						</div>

                            						<div class="col-lg-6 col-md-12 col-sm-12 mt-2 mb-2" id="u1">
                                						<label for=""><b>UNIVERSITY FOR HIGHER STUDIES&nbsp;</b> 
	                                						<span class="text-danger">*</span>&nbsp;
	                                						<!-- <span data-toggle="tooltip" data-placement="top" title="You Can Choose Multiple Universities for Higher studies">
	                                						<i  class="fa fa-info-circle"  aria-hidden="true"></i></span></b> -->
                                						</label>
						                                <!-- <select multiple id="universityName" class="form-control"
						                                    name="universityName" path="universityName" data-live-search="true" required>
						                                    <option value="" disabled="true">Choose University For Higher studies</option>
						                                    <option value="Purdue USA">Purdue USA</option>
						                                    <option value="Western Sydney University Australia">Western Sydney University Australia</option>
						                                    <option value="Other">Other</option>
						                                </select>
                                						 -->
						                                <input type="text" class="form-control" id="universityName" value="" name="universityName" path="universityName" 
						                                    placeholder="Enter University Name" maxlength="50"/>
                           	 						</div>
                           	 						</div>
                           	 						<div class="row"><div class="col-12"><span id="addMoreCountry" style="color:#1457a7"><strong><i class="fas fa-plus"></i> Add More</strong></span>
                           	 						&nbsp;&nbsp;<span id="removeCountry" style="color:#a7a7a7"><strong><i class="fas fa-times"></i> Remove</strong></span></div></div>
													<hr>
													<div class="row">
						                            <div class="col-lg-6 col-md-12 col-sm-12 mt-2 mb-2">
						                                <label for=""><b>PROGRAM TO ENROLL &nbsp;<span class="text-danger">*</span></b></label>
						                                <select id="programToEnroll" class="form-control" name="programToEnroll" path="programToEnroll" required>
						                                    <option selected value="" disabled>Choose Program Enroll </option>
						                                    <option value="BS">BS</option>
						                                    <option value="MS">MS</option>
						                                    <option value="MS-Eng">MS-Eng</option>
						                                    <option value="MBA">MBA</option>
						                                    <option value="P.hd">P.hd</option>
						                                    <option value="Other">Other</option>
						                                </select>
						                               <br>
						                                <input type="text" class="form-control d-none" id="programToEnrollOther" value="" name="programToEnroll" path="programToEnroll"
						                                    placeholder="Write Other Program Enroll" maxlength="50" disabled="true"/>
						
						                            </div>
													
						                            <div class="col-lg-6 col-md-12 col-sm-12 mt-2 mb-2">
						                                <label for=""><b>TENTATIVE DATE OF JOINING&nbsp;<span
						                                            class="text-danger">*</span></b></label>
			                                            <span data-toggle="tooltip" data-placement="top" title="Date should be one month later">
                                						<i  class="fa fa-info-circle" aria-hidden="true"></i></span>
						                                <div class="input-group">
						                                    <form:input id="tentativeDOJ" type="text" value="" path="tentativeDOJ" name="tentativeDOJ"
						                                        placeholder="Date" class="form-control" required="true" readOnly="true"/>
						                                    <div class="input-group-append">
						                                        <button class="btn btn-outline-secondary initiateSDatePicker" type="button" id="tentativeDateBtn">
						                                            <i class="fas fa-calendar"></i>
						                                        </button>
						                                    </div>
						                                </div>
						                            </div>
													</div>
													<hr>
													<div class="row">
						                            <div class="col-lg-6 col-md-12 col-sm-12 mt-2 mb-2">
						                                <label for=""><b>COMPETITIVE EXAM TAKEN&nbsp;<span
						                                            class="text-danger">*</span></b></label>
						                                <select id="competitiveExam" class="form-control" name="competitiveExam" path="competitiveExam" required>
						                                    <option value="" selected disabled>Choose Competitive Exam</option>
						                                    <option value="GRE">GRE</option>
						                                    <option value="GMAT">GMAT</option>
						                                    <option value="GATE">Gate</option>
						                                    <option value="NMAT">NMAT</option>
						                                    <option value="CAT">CAT</option>
						                                    <option value="ZATT">ZATT</option>
						                                    <option value="Not Taken">Not Taken</option>
						                                    <option value="Other">Other</option>
						                                </select>
						                                <br>
						                                <input type="text" class="form-control d-none" id="competitiveExamOther" value="" name="competitiveExam" path="competitiveExam"
						                                    placeholder="Write Other Exam Name" disabled="true"/>
														
						                            </div>
						                            <div class="col-lg-6 col-md-12 col-sm-12 mt-2 mb-2">
						                            	<div id="competitiveExamScoreDiv" class="d-none">
							                                <label for="examScore"><b>UPLOAD MARKSHEET&nbsp;<span class="text-danger">*</span></b></label>
							                                <span data-toggle="tooltip" data-placement="top" title="File should be image/pdf/document">
                                							<i  class="fa fa-info-circle" aria-hidden="true"></i></span>
							                                <!-- <input id="examScore" name="examScore" placeholder="Score" type="number" class="form-control" path="examScore"/> -->
							                                <input type="file" name="file1" class="form-control-file" id="examMarksheet">
							                            </div>
						                            </div>
						                            </div>
						                            <div class="row">
						                             <div class="col-lg-6 col-md-12 col-sm-12 mt-2 mb-2">
						                                <label for="toeflOrIelts"><b>TOEFL/IELTS&nbsp;<span class="text-danger">*</span></b></label>
						                                <select id="toeflOrIelts" class="form-control" required name="toeflOrIelts" path="toeflOrIelts">
						                                    <option selected value="" disabled>Choose TOEFL/IELTS</option>
						                                    <option value="TOEFL">TOEFL</option>
						                                    <option value="IELTS">IELTS</option>
						                                    <option value="Not Taken">Not Taken</option>
						                                </select>
						                                <!-- <br>
						                                <div id="toeflScoreDiv" class="mt-1 d-none">
						                                    <label for="toeflScore"><b>TOEFL Score&nbsp;<span class="text-danger">*</span></b></label>
						                                    <input id="toeflScore" name="toeflScore" min="0" max="999" placeholder="Score" type="number" path="toeflScore" value="0.0"
						                                        class="form-control"/>
						
						                                </div>
						                                <div id="ieltsScoreDiv" class="mt-1 d-none">
						                                    <label for=""><b>IELTS SCORE&nbsp;<span class="text-danger">*</span></b></label><br>
						
						                                    <lable>Reading Score:&nbsp; </lable><input id="ieltsReadingScore" name="ieltsReadingScore" min="0" max="7" placeholder="Reading Score" type="number" path="ieltsReadingScore" value="0.0"
						                                        class="form-control ieltsScore" step=".01"/>
						                                    <br>
						
						                                    <lable>Writing Score:&nbsp; </lable><input id="ieltsWritingScore" name="ieltsWritingScore" min="0" max="7" placeholder="Writing Score" type="number" path="ieltsWritingScore" value="0.0"
						                                        class="form-control ieltsScore" step=".01"/>
						                                    <br>
						
						                                    <lable>Speaking Score:&nbsp; </lable><input id="ieltsSpeakingScore" name="ieltsSpeakingScore" min="0" max="7" placeholder="Speaking Score" type="number" path="ieltsSpeakingScore" value="0.0"
						                                        class="form-control ieltsScore" step=".01"/>
						                                    <br>
						
						                                    <lable>Listening Score:&nbsp; </lable><input id="ieltsListeningScore" name="ieltsListeningScore" min="0" max="7" placeholder="Listening Score" type="number" path="ieltsListeningScore" value="0.0"
						                                        class="form-control ieltsScore" step=".01"/>
						                                    <br>
						                                </div> -->
						                            </div>
						                            <div class="col-lg-6 col-md-12 col-sm-12 mt-2 mb-2">
						                            	<div id="toeflOrIeltsExamScoreDiv" class="d-none">
							                                <label for="examScore"><b>UPLOAD MARKSHEET&nbsp;<span class="text-danger">*</span></b></label>
							                                <span data-toggle="tooltip" data-placement="top" title="File should be image/pdf/document">
                                							<i  class="fa fa-info-circle" aria-hidden="true"></i></span>
							                                <!-- <input id="examScore" name="examScore" placeholder="Score" type="number" class="form-control" path="examScore"/> -->
							                                <input type="file" name="file2" class="form-control-file" id="toeflOrIeltsMarksheet">
							                            </div>
						                            </div>
						                        </div>
						                        <div class="row">
						                        <div class="col-lg-6 col-md-12 col-sm-12 mt-2 mb-2">
						                                <label for="isNmimsPartnerUniversity"><b>ARE YOU APPLYING IN NMIMS PARTNER UNIVERSITIES&nbsp;<span
						                                            class="text-danger">*</span></b>
						                                </label>
						                                <select id="isNmimsPartnerUniversity" class="form-control" required name="isNmimsPartnerUniversity" path="isNmimsPartnerUniversity">
						                                    <option selected value="" disabled>Choose</option>
						                                    <option value="Yes">Yes</option>
						                                    <option value="No">No</option>
						                                </select>
						                                <br>
						                                <div id="nmimsPartnerUniversityDiv" class="d-none">
						                                    <select id="nmimsPartnerUniversity" class="form-control" name="nmimsPartnerUniversity" path="nmimsPartnerUniversity">
						                                        <option selected value="" disabled>Choose University</option>
						                                        <option value="Purdue USA">Purdue USA</option>
						                                        <option value="Virginia Tech. USA">Virginia Tech. USA</option>
						                                        <option value="Western SydneyUniversity Australia">Western SydneyUniversity Australia</option>
						                                        <option value="New South Wales Australia">New South Wales Australia</option>
						                                        <option value="Canberra University Australia">Canberra University Australia</option>
						                                        <option value="Stevens Institute of Tech, USA.">Stevens Institute of Tech, USA.</option>
						                                    </select>
						                                </div>
						                            </div>
						                            </div>
									</div>
								</div>
							</div>
							<hr>
						</div>
						
						<div class="card-body d-none" id="stepTwoLor">
	                        <div class="row">
	                            <div class="col-lg-6 col-md-6 col-sm-12 pt-2">
	                                <label for=""><b>DEPARTMENT&nbsp;<span class="text-danger">*</span></b></label>
	                                <select id="lorDepartment" class="form-control" data-live-search="true" name = "department" path="department" required>
	                                    <option value="" selected>Choose Department</option>
	                                    <c:forEach items="${deptlist}" var="list">
											<option value="${list}">${list} </option>
										</c:forEach>
	                                </select>
	                            </div>
	
	                            <div class="col-lg-6 col-md-6 col-sm-12 pt-2">
	                                <label for=""><b>Faculty Name&nbsp;</strong> <span class="text-danger">*</span>&nbsp;<span
	                                            data-toggle="tooltip" data-placement="right" title="You Can Choose Multiple Faculty Name and maximum 5"><i
	                                                class="fa fa-info-circle" aria-hidden="true"></i></span></b>
	                                </label>
	                                <select multiple id="lorStaffSelection" class="form-control"
	                                    name="staffId" path="staffId" data-live-search="true" required>
	                                    <option value="" disabled="true">Choose Faculty</option>
	                                </select>
	                            </div>
							</div>
							<div class="row">
	                            <div class="col-lg-12 col-md-12 col-sm-12 mt-3 pt-2">
	                                    <table id="staffAndCopies" class="table-bordered d-none" style="width:100%;">
	                                    	<thead>
	                                    		<tr>
	                                                <th>Sr No</th>
	                                                <th>Faculty Name</th>
	                                                <th>No. of Hard Copy if required</th>
	                                            </tr>
	                                    	</thead>
	                                        <tbody>
	                                            
	                                        </tbody>
	                                    </table>
	                            </div>
	                        </div>
			            </div>
			            </form:form>
			            <div class="text-center mt-2 mb-4">
		        			<button id="prevBtn" class="prevBtnLor d-none"> Previous</button>
							<button id="nextBtn" class="nextBtnLor">Next</button>
		                    <button id="subBtn" class="subBtnLor d-none text-center" form="lorForm"> Apply for LOR</button>
		                    <button class="cancelBtnLor text-center">Cancel</button>
				        </div>
					</div>
			
			
					<!-- Results Panel -->
					<!-- /page content: END -->
				</div>
				<!-- SIDEBAR START -->
				<jsp:include page="../common/newSidebar.jsp" />
				<!-- SIDEBAR END -->
				<jsp:include page="../common/footer.jsp" />
<script>
$(document).ready(function () {
    $('[data-toggle="tooltip"]').tooltip();
	/* $('#countryForHigherStudy').select2({
		placeholder: "Choose Country",
    }); */
	/* $('#universityName').select2({
		placeholder: "Choose Univesity",
    }); */
	
	
	/* let isOtherUni = false; */
	let isOtherProgram = false;
	let isOtherExam = false;
	let isExamScore = false;
	let istoeflOrIelts = false;
	let isPartnerUniversity = false;

	/* $('#universityName').on('select2:select', function (e) {
		let data = e.params.data;
		console.log(data)
		if(data.selected) {
			if(data.id == "Other"){
				$('#universityNameOther').removeClass('d-none');
				isOtherUni = true;
			 	$('#universityNameOther').attr('disabled', false);
			 	$('#universityNameOther').val('');
			}
	    } else {
	    	$('#universityNameOther').addClass('d-none');
	    	isOtherUni = false;
 			$('#universityNameOther').attr('disabled', true);
		 	$('#universityNameOther').val('');
	    }
	});
	$('#universityName').on('select2:unselect',function(e) {
		let data = e.params.data
		if(data.id == "Other"){
			$('#universityNameOther').addClass('d-none');
			isOtherUni = false;
 			$('#universityNameOther').attr('disabled', true);
		 	$('#universityNameOther').val('');
		}
	}); */
	
	$('#programToEnroll').on('change', function(){
 		let appProgram = $("#programToEnroll").val();
 		if(appProgram == "Other"){
 			$('#programToEnrollOther').removeClass('d-none');
 			isOtherProgram = true;
 			$('#programToEnrollOther').attr('disabled', false);
		 	$('#programToEnrollOther').val('');
 		}else{
 			$('#programToEnrollOther').addClass('d-none');
 			isOtherProgram = false;
 			$('#programToEnrollOther').attr('disabled', true);
		 	$('#programToEnrollOther').val('');
 		}
	});
	
	$('#competitiveExam').on('change', function(){
 		let competitiveExam = $("#competitiveExam").val();
 		if(competitiveExam == "Other"){
 			$('#competitiveExamOther').removeClass('d-none');
 			$('#competitiveExamScoreDiv').removeClass('d-none');
 			isOtherExam = true;
 			$('#competitiveExamOther').attr('disabled', false);
		 	$('#competitiveExamOther').val('');
		 	isExamScore = true;
		 	$('#examMarksheet').val('');
 		}else if(competitiveExam == "Not Taken"){
 			$('#competitiveExamOther').addClass('d-none');
 			$('#competitiveExamScoreDiv').addClass('d-none');
 			isOtherExam = false;
 			$('#competitiveExamOther').attr('disabled', true);
		 	$('#competitiveExamOther').val('');
		 	isExamScore = false;
		 	$('#examMarksheet').val('');
 		}else{
 			$('#competitiveExamOther').addClass('d-none');
 			$('#competitiveExamScoreDiv').removeClass('d-none');
 			isOtherExam = false;
 			$('#competitiveExamOther').attr('disabled', true);
		 	$('#competitiveExamOther').val('');
		 	isExamScore = true;
		 	$('#examMarksheet').val('');
 		}
	});
	
	$('#toeflOrIelts').on('change', function(){
 		let toeflOrIelts = $("#toeflOrIelts").val();
 		if(toeflOrIelts == "TOEFL" || toeflOrIelts == "IELTS"){
 			$('#toeflOrIeltsExamScoreDiv').removeClass('d-none');
 			$('#toeflOrIeltsMarksheet').val('');
 			istoeflOrIelts = true;
 		}else{
 			$('#toeflOrIeltsExamScoreDiv').addClass('d-none');
 			$('#toeflOrIeltsMarksheet').val('');
 			istoeflOrIelts = false;
 		}
	});
	
	$('#isNmimsPartnerUniversity').on('change', function(){
 		let isNmimsPartnerUniversity = $("#isNmimsPartnerUniversity").val();
 		if(isNmimsPartnerUniversity == "Yes"){
 			$('#nmimsPartnerUniversityDiv').removeClass('d-none');
 			isPartnerUniversity = true;
 			$('#nmimsPartnerUniversity').val('');
 		}else{
 			$('#nmimsPartnerUniversityDiv').addClass('d-none');
 			isPartnerUniversity = false;
 			$('#nmimsPartnerUniversity').val('');
 		}
	});
	
		var minDate = new Date();
		/* var maxDate = new Date(); */
		minDate.setMonth( minDate.getMonth() + 1 );
		/* maxDate.setMonth( minDate.getMonth() + 1 ); */
		//start Date picker
		$('.initiateSDatePicker').daterangepicker({
			autoUpdateInput : false,
			locale : {
				cancelLabel : 'Clear'
			},
			"singleDatePicker" : true,
			"showDropdowns" : true,
			"timePicker" : false,
			"showCustomRangeLabel" : false,
			"alwaysShowCalendars" : true,
			"opens" : "center",
			"minDate": minDate
			/* "maxDate": maxDate */
		});

		$('.initiateSDatePicker').on('apply.daterangepicker',function(ev, picker) {
			$(this).parent().parent().find('input').val(picker.startDate.format('YYYY-MM-DD'));
		});

		$('.initiateSDatePicker').on('cancel.daterangepicker',function(ev, picker) {
			$(this).parent().parent().find('input').val($('#tentativeDOJ').val());
		});

	
	$("#lorDepartment").on('change',function() {
		let departmentName = $('#lorDepartment').val();
		console.log("departmentName--->",departmentName);
		$.ajax({
			type : 'POST',
			url : '${pageContext.request.contextPath}/getfacultyByDept',
			data : { department: departmentName },
			success : function(data) {
				console.log("Data ====> ", data)
					adminJson =JSON.parse(data);
						var optionsAsString = "";
						 $('#lorStaffSelection').find('option').remove();
						 for(var j=0;j<adminJson.length;j++){
							optionsAsString += "<option data-id="+adminJson[j].value+" value='" +adminJson[j].value + "' >"
							+adminJson[j].value + "-"+ adminJson[j].text  
							+ "</option>";
						}
						$('#lorStaffSelection').append(optionsAsString);
				}
		});				
	});
	
	
	
	$("#lorStaffSelection").on('change',function() {
		let lorStaffSelection = $('#lorStaffSelection').val();
		for(let i=0;i<lorStaffSelection.size;i++){
			console.log("lorStaffSelection--->",lorStaffSelection[i]);
		}
	});
	
	let facultyObj = [];
	
	$('#lorStaffSelection').on('select2:select', function (e) {
	    let data = e.params.data;
	    if(data.selected) {
	    	facultyObj.push(data);
	    	$('#staffAndCopies').removeClass('d-none');
	    	renderFacultyData(facultyObj);
	    } else {
	    	facultyObj = facultyObj.filter(elem => elem.id !== data.id);
	    	if(facultyObj.length < 1){
	    		$('#staffAndCopies').addClass('d-none');
	    	}
			renderFacultyData(facultyObj);
	    }
	});
	
	
	$('#lorStaffSelection').on('select2:unselect',function(e) {
		let data = e.params.data;
		facultyObj = facultyObj.filter(elem => elem.id !== data.id);
		$('#lorStaffSelection option[data-id='+ data.id +']').val(data.id);
		if(facultyObj.length < 1){
    		$('#staffAndCopies').addClass('d-none');
    	}
		
		renderFacultyData(facultyObj);
	});
	
	$('#lorDepartment').bind('change',function(){
		facultyObj = [];
		renderFacultyData(facultyObj);
	});
	
	function renderFacultyData(facultyObj){
		let str= "";
	    for(let i=0; i< facultyObj.length; i++) {
	    	let copyValue = facultyObj[i].element.value;
	    	console.log("BcopyValue-->",facultyObj[i].id,"--->",copyValue)
	    	if(copyValue.includes("-")){
	    		copyValue = facultyObj[i].element.value.substring(facultyObj[i].element.value.indexOf('-') + 1);
	    	}else{
	    		copyValue = 0;
	    	}

	    	console.log("AcopyValue-->",facultyObj[i].id,"--->",copyValue)
	    	str=str +'<tr>'+
            '<td scope="row">'+(i+1)+'</td>'+
            '<td>'+facultyObj[i].text+'</td>'+
            '<td><input id="hc'+facultyObj[i].id+'" class="copyInput" name="noOfCopies" path="noOfCopies" placeholder="Number Of Hard Copy" value='+copyValue+' min=0 max=10 type="number" class="form-control" required="required"/>'+
            '</td></tr>';	
	    	$('#lorStaffSelection option[data-id='+ facultyObj[i].id +']').val(facultyObj[i].id+'-'+copyValue);
	    }
	    $('#staffAndCopies tbody').html(str);
	}
	
	$('#nextBtn').on('click',function() {

	
	
	  let countryForHigherStudy = $("#countryForHigherStudy").val();
  	  let universityName = $("#universityName").val();
  	  let countryArray = document.getElementsByName("countryForHigherStudy");
	  let universityArray = document.getElementsByName("universityName");
  	 /*  let universityNameOther = $("#universityNameOther").val().trim(); */
  	  let programToEnroll = $("#programToEnroll").val();
  	  let programToEnrollOther = $("#programToEnrollOther").val();
  	  let tentativeDOJ = $("#tentativeDOJ").val();
  	  let competitiveExam = $("#competitiveExam").val();
  	  let competitiveExamOther = $("#competitiveExamOther").val(); 
  	  //let examScore = $("#examScore").val(); 
  	  let examMarksheet = $("#examMarksheet").val(); 
  	  let isNmimsPartnerUniversity = $("#isNmimsPartnerUniversity").val()
  	  let nmimsPartnerUniversity = $("#nmimsPartnerUniversity").val();
  	  let toeflOrIeltsMarksheet = $("#toeflOrIeltsMarksheet").val()
  	  /* let ieltsReadingScore= $('#ieltsReadingScore').val();
  	  let ieltsWritingScore= $('#ieltsWritingScore').val();
  	  let ieltsSpeakingScore=	$('#ieltsSpeakingScore').val();
  	  let ieltsListeningScore=	$('#ieltsListeningScore').val(); */
  	  let toeflScore = $("#toeflScore").val();
  	  let toeflOrIelts = $("#toeflOrIelts").val();
  	
		if(!countryForHigherStudy) {
			alert('Please select country for higher studies.')
			return false;
		}
		for (let element of countryArray)    {
        	if (!element.value){
        		alert('Please select country for higher studies.')
                return false;
            }
        }
		
	  /* 	if(universityName.length == 0) {
			alert('Please select university for higher studies.')
			return false;
		} */
		if(!universityName) {
			alert("Please specify university name.")
			return false;
		}
		for (let element of universityArray)    {
        	if (!element.value){
        		alert('Please select country for higher studies.')
                return false;
            }
        }
		if(!programToEnroll){
			alert("Please select program to enroll.")
			return false;
		}
		if(isOtherProgram && !programToEnrollOther) {
			alert("Please specify other program to enroll name.")
			return false;
		}
		if(!tentativeDOJ){
			alert("Please select tentative date of joining.")
			return false;
		}
		if(!competitiveExam){
			alert("Please select competitive exam given.")
			return false;
		}
		if(isOtherExam && !competitiveExamOther){
			alert("Please specify other exam name.")
			return false;
		}
		if(isExamScore && !examMarksheet){
			alert("Please upload exam marksheet file.")
			return false;
		}
		if(!toeflOrIelts){
			alert("Please select TOEFL/IELTS.")
			return false;
		}
		if(istoeflOrIelts && !toeflOrIeltsMarksheet){
			alert("Please upload exam marksheet file.")
			return false;
		}
		
		/* if(isToeflScore && !toeflScore){
			alert("Please specify TOEFL exam score.")
			return false;
		}else if(isToeflScore && (parseFloat(toeflScore) < 0 || parseFloat(toeflScore) >= 999)){
			alert("TOEFL exam score between 0 to 999.")
			return false;
		}
		if(isIeltsScore && (!ieltsReadingScore || !ieltsWritingScore || !ieltsSpeakingScore || !ieltsListeningScore)){
			alert("Please specify IELTS score.")
			return false;
		} else if((isIeltsScore) && ((parseFloat(ieltsReadingScore) < 0 || parseFloat(ieltsReadingScore) > 7.00) || (parseFloat(ieltsWritingScore) < 0 || parseFloat(ieltsWritingScore) > 7.00) || (parseFloat(ieltsSpeakingScore) < 0 || parseFloat(ieltsSpeakingScore) > 7.00) || (parseFloat(ieltsListeningScore) < 0 || parseFloat(ieltsListeningScore) > 7.00))){
			alert("IELTS exam score between 0 to 7.")
			return false;
		} */
		if(!isNmimsPartnerUniversity){
			alert("Please select applying for NMIMS partner university.")
			return false;
		}
		if(isPartnerUniversity && !nmimsPartnerUniversity){
			alert("Please select NMIMS partner university name.")
			return false;
		}
  	  
  	  
  	 /*  if(countryForHigherStudy == "" || universityName == "" || programToEnroll == "" || tentativeDOJ == "" || competitiveExam == "" || isNmimsPartnerUniversity == "" || toeflOrIelts == ""){
  		  alert("Please fill out all field.1");
		  return false;
  	  }else if(universityName == "Other" || programToEnroll == "Other" || competitiveExam == "Other"){
  		  if(universityNameOther == "" || programToEnrollOther == "" || competitiveExamOther == ""){
 		  	alert("Please fill out all field.2");
		  	return false;
  		  }
  	  }else if(isNmimsPartnerUniversity == "Yes"){
  		  if(nmimsPartnerUniversity == ""){
  			alert("Please fill out all field.3");
		  	return false;
  		  }
  	  }else if(examScore == "" && competitiveExam !== "Not Given"){
  		  alert("Please fill out all field.4");
		  return false;
  	  }else if(toeflOrIelts == "TOEFL") {
  		if(toeflScore == ""){
    	  alert("Please fill out all field.5");
  		  return false;
  		 }
  	  }else if(toeflOrIelts == "IELTS") {
		  if(ieltsReadingScore == "" || ieltsWritingScore == "" || ieltsSpeakingScore == "" || ieltsListeningScore == ""){
			  alert("Please fill out all field.6");
		  	  return false;
		  }
  		}else{	
  		  console.log("Else")
			
  		} */
  	$('#stepTitle').html("Step 2- Select Faculty for LOR");
	$('#stepOneLor').addClass('d-none');
	$('#stepTwoLor').removeClass('d-none');
	$('#lorStaffSelection').select2({
		placeholder: "Choose Facutly",
		maximumSelectionLength: 5,
    });
	$('#prevBtn').removeClass('d-none');
	$('#nextBtn').addClass('d-none');
	$('#subBtn').removeClass('d-none');
	});
	
	function getCopiesValue(){
		let noOfCopies = document.getElementsByName("noOfCopies");
		$('#staffAndCopies tbody tr').find("td").each(function(){
            console.log($(this).html());
        });
	}
	$(document).on('input', 'input[name=noOfCopies]', function() {
		console.log("onInputcalled")
		let copyId = $(this).attr('id');
		let facultyId = copyId.substring(2);
		//console.log(facultyId)
        let copies = $(this).val();
		console.log($(this).val())
		$('#lorStaffSelection option[data-id='+ facultyId +']').val(facultyId+'-'+copies);
	});
	$('#prevBtn').on('click',function() {
		$('#stepTitle').html("Step 1- Fill your entrance exam details");
		$('#stepOneLor').removeClass('d-none');
		$('#stepTwoLor').addClass('d-none');
		$('#prevBtn').addClass('d-none');
		$('#nextBtn').removeClass('d-none');
		$('#subBtn').addClass('d-none');
	});
	let countryIndex = 1;
	$('#addMoreCountry').on('click',function(){
		let cnu= $('#countryAndUniversity').html();
		countryIndex++;
		$('#countryAndUniversity').append('<div class="col-lg-6 col-md-12 col-sm-12 mt-2 mb-2" id="c'+countryIndex+'"> <select id="countryForHigherStudy" class="form-control" name="countryForHigherStudy" name="countryForHigherStudy" path="countryForHigherStudy" required="true"> <option value="" disabled="true" selected>Select Country</option> <option value="Afganistan">Afghanistan</option> <option value="Albania">Albania</option> <option value="Algeria">Algeria</option> <option value="American Samoa">American Samoa</option> <option value="Andorra">Andorra</option> <option value="Angola">Angola</option> <option value="Anguilla">Anguilla</option> <option value="Antigua &amp; Barbuda">Antigua &amp; Barbuda</option> <option value="Argentina">Argentina</option> <option value="Armenia">Armenia</option> <option value="Aruba">Aruba</option> <option value="Australia">Australia</option> <option value="Austria">Austria</option> <option value="Azerbaijan">Azerbaijan</option> <option value="Bahamas">Bahamas</option> <option value="Bahrain">Bahrain</option> <option value="Bangladesh">Bangladesh</option> <option value="Barbados">Barbados</option> <option value="Belarus">Belarus</option> <option value="Belgium">Belgium</option> <option value="Belize">Belize</option> <option value="Benin">Benin</option> <option value="Bermuda">Bermuda</option> <option value="Bhutan">Bhutan</option> <option value="Bolivia">Bolivia</option> <option value="Bonaire">Bonaire</option> <option value="Bosnia &amp; Herzegovina">Bosnia &amp; Herzegovina</option> <option value="Botswana">Botswana</option> <option value="Brazil">Brazil</option> <option value="British Indian Ocean Ter">British Indian Ocean Ter</option> <option value="Brunei">Brunei</option> <option value="Bulgaria">Bulgaria</option> <option value="Burkina Faso">Burkina Faso</option> <option value="Burundi">Burundi</option> <option value="Cambodia">Cambodia</option> <option value="Cameroon">Cameroon</option> <option value="Canada">Canada</option> <option value="Canary Islands">Canary Islands</option> <option value="Cape Verde">Cape Verde</option> <option value="Cayman Islands">Cayman Islands</option> <option value="Central African Republic">Central African Republic</option> <option value="Chad">Chad</option> <option value="Channel Islands">Channel Islands</option> <option value="Chile">Chile</option> <option value="China">China</option> <option value="Christmas Island">Christmas Island</option> <option value="Cocos Island">Cocos Island</option> <option value="Colombia">Colombia</option> <option value="Comoros">Comoros</option> <option value="Congo">Congo</option> <option value="Cook Islands">Cook Islands</option> <option value="Costa Rica">Costa Rica</option> <option value="Cote DIvoire">Cote DIvoire</option> <option value="Croatia">Croatia</option> <option value="Cuba">Cuba</option> <option value="Curaco">Curacao</option> <option value="Cyprus">Cyprus</option> <option value="Czech Republic">Czech Republic</option> <option value="Denmark">Denmark</option> <option value="Djibouti">Djibouti</option> <option value="Dominica">Dominica</option> <option value="Dominican Republic">Dominican Republic</option> <option value="East Timor">East Timor</option> <option value="Ecuador">Ecuador</option> <option value="Egypt">Egypt</option> <option value="El Salvador">El Salvador</option> <option value="Equatorial Guinea">Equatorial Guinea</option> <option value="Eritrea">Eritrea</option> <option value="Estonia">Estonia</option> <option value="Ethiopia">Ethiopia</option> <option value="Falkland Islands">Falkland Islands</option> <option value="Faroe Islands">Faroe Islands</option> <option value="Fiji">Fiji</option> <option value="Finland">Finland</option> <option value="France">France</option> <option value="French Guiana">French Guiana</option> <option value="French Polynesia">French Polynesia</option> <option value="French Southern Ter">French Southern Ter</option> <option value="Gabon">Gabon</option> <option value="Gambia">Gambia</option> <option value="Georgia">Georgia</option> <option value="Germany">Germany</option> <option value="Ghana">Ghana</option> <option value="Gibraltar">Gibraltar</option> <option value="Great Britain">Great Britain</option> <option value="Greece">Greece</option> <option value="Greenland">Greenland</option> <option value="Grenada">Grenada</option> <option value="Guadeloupe">Guadeloupe</option> <option value="Guam">Guam</option> <option value="Guatemala">Guatemala</option> <option value="Guinea">Guinea</option> <option value="Guyana">Guyana</option> <option value="Haiti">Haiti</option> <option value="Hawaii">Hawaii</option> <option value="Honduras">Honduras</option> <option value="Hong Kong">Hong Kong</option> <option value="Hungary">Hungary</option> <option value="Iceland">Iceland</option> <option value="Indonesia">Indonesia</option> <option value="India">India</option> <option value="Iran">Iran</option> <option value="Iraq">Iraq</option> <option value="Ireland">Ireland</option> <option value="Isle of Man">Isle of Man</option> <option value="Israel">Israel</option> <option value="Italy">Italy</option> <option value="Jamaica">Jamaica</option> <option value="Japan">Japan</option> <option value="Jordan">Jordan</option> <option value="Kazakhstan">Kazakhstan</option> <option value="Kenya">Kenya</option> <option value="Kiribati">Kiribati</option> <option value="Korea North">Korea North</option> <option value="Korea Sout">Korea South</option> <option value="Kuwait">Kuwait</option> <option value="Kyrgyzstan">Kyrgyzstan</option> <option value="Laos">Laos</option> <option value="Latvia">Latvia</option> <option value="Lebanon">Lebanon</option> <option value="Lesotho">Lesotho</option> <option value="Liberia">Liberia</option> <option value="Libya">Libya</option> <option value="Liechtenstein">Liechtenstein</option> <option value="Lithuania">Lithuania</option> <option value="Luxembourg">Luxembourg</option> <option value="Macau">Macau</option> <option value="Macedonia">Macedonia</option> <option value="Madagascar">Madagascar</option> <option value="Malaysia">Malaysia</option> <option value="Malawi">Malawi</option> <option value="Maldives">Maldives</option> <option value="Mali">Mali</option> <option value="Malta">Malta</option> <option value="Marshall Islands">Marshall Islands</option> <option value="Martinique">Martinique</option> <option value="Mauritania">Mauritania</option> <option value="Mauritius">Mauritius</option> <option value="Mayotte">Mayotte</option> <option value="Mexico">Mexico</option> <option value="Midway Islands">Midway Islands</option> <option value="Moldova">Moldova</option> <option value="Monaco">Monaco</option> <option value="Mongolia">Mongolia</option> <option value="Montserrat">Montserrat</option> <option value="Morocco">Morocco</option> <option value="Mozambique">Mozambique</option> <option value="Myanmar">Myanmar</option> <option value="Nambia">Nambia</option> <option value="Nauru">Nauru</option> <option value="Nepal">Nepal</option> <option value="Netherland Antilles">Netherland Antilles</option> <option value="Netherlands">Netherlands (Holland, Europe)</option> <option value="Nevis">Nevis</option> <option value="New Caledonia">New Caledonia</option> <option value="New Zealand">New Zealand</option> <option value="Nicaragua">Nicaragua</option> <option value="Niger">Niger</option> <option value="Nigeria">Nigeria</option> <option value="Niue">Niue</option> <option value="Norfolk Island">Norfolk Island</option> <option value="Norway">Norway</option> <option value="Oman">Oman</option> <option value="Pakistan">Pakistan</option> <option value="Palau Island">Palau Island</option> <option value="Palestine">Palestine</option> <option value="Panama">Panama</option> <option value="Papua New Guinea">Papua New Guinea</option> <option value="Paraguay">Paraguay</option> <option value="Peru">Peru</option> <option value="Phillipines">Philippines</option> <option value="Pitcairn Island">Pitcairn Island</option> <option value="Poland">Poland</option> <option value="Portugal">Portugal</option> <option value="Puerto Rico">Puerto Rico</option> <option value="Qatar">Qatar</option> <option value="Republic of Montenegro">Republic of Montenegro</option> <option value="Republic of Serbia">Republic of Serbia</option> <option value="Reunion">Reunion</option> <option value="Romania">Romania</option> <option value="Russia">Russia</option> <option value="Rwanda">Rwanda</option> <option value="St Barthelemy">St Barthelemy</option> <option value="St Eustatius">St Eustatius</option> <option value="St Helena">St Helena</option> <option value="St Kitts-Nevis">St Kitts-Nevis</option> <option value="St Lucia">St Lucia</option> <option value="St Maarten">St Maarten</option> <option value="St Pierre &amp; Miquelon">St Pierre &amp; Miquelon</option> <option value="St Vincent &amp; Grenadines">St Vincent &amp; Grenadines </option> <option value="Saipan">Saipan</option> <option value="Samoa">Samoa</option> <option value="Samoa American">Samoa American</option> <option value="San Marino">San Marino</option> <option value="Sao Tome &amp; Principe">Sao Tome &amp; Principe</option> <option value="Saudi Arabia">Saudi Arabia</option> <option value="Senegal">Senegal</option> <option value="Seychelles">Seychelles</option> <option value="Sierra Leone">Sierra Leone</option> <option value="Singapore">Singapore</option> <option value="Slovakia">Slovakia</option> <option value="Slovenia">Slovenia</option> <option value="Solomon Islands">Solomon Islands</option> <option value="Somalia">Somalia</option> <option value="South Africa">South Africa</option> <option value="Spain">Spain</option> <option value="Sri Lanka">Sri Lanka</option> <option value="Sudan">Sudan</option> <option value="Suriname">Suriname</option> <option value="Swaziland">Swaziland</option> <option value="Sweden">Sweden</option> <option value="Switzerland">Switzerland</option> <option value="Syria">Syria</option> <option value="Tahiti">Tahiti</option> <option value="Taiwan">Taiwan</option> <option value="Tajikistan">Tajikistan</option> <option value="Tanzania">Tanzania</option> <option value="Thailand">Thailand</option> <option value="Togo">Togo</option> <option value="Tokelau">Tokelau</option> <option value="Tonga">Tonga</option> <option value="Trinidad &amp; Tobago">Trinidad &amp; Tobago</option> <option value="Tunisia">Tunisia</option> <option value="Turkey">Turkey</option> <option value="Turkmenistan">Turkmenistan</option> <option value="Turks &amp; Caicos Is">Turks &amp; Caicos Is</option> <option value="Tuvalu">Tuvalu</option> <option value="Uganda">Uganda</option> <option value="United Kingdom">United Kingdom</option> <option value="Ukraine">Ukraine</option> <option value="United Arab Erimates">United Arab Emirates</option> <option value="United States of America">United States of America</option> <option value="Uraguay">Uruguay</option> <option value="Uzbekistan">Uzbekistan</option> <option value="Vanuatu">Vanuatu</option> <option value="Vatican City State">Vatican City State</option> <option value="Venezuela">Venezuela</option> <option value="Vietnam">Vietnam</option> <option value="Virgin Islands (Brit)">Virgin Islands (Brit)</option> <option value="Virgin Islands (USA)">Virgin Islands (USA)</option> <option value="Wake Island">Wake Island</option> <option value="Wallis &amp; Futana Is">Wallis &amp; Futana Is</option> <option value="Yemen">Yemen</option> <option value="Zaire">Zaire</option> <option value="Zambia">Zambia</option> <option value="Zimbabwe">Zimbabwe</option> </select> </div> <div class="col-lg-6 col-md-12 col-sm-12 mt-2 mb-2" id="u'+countryIndex+'"> <input type="text" class="form-control" id="universityName" value="" name="universityName" path="universityName" placeholder="Enter University Name" maxlength="50"/> </div>');
	});
	$('#removeCountry').on('click',function(){
		if(countryIndex > 1){
			$('#countryAndUniversity #c'+countryIndex).remove();
			$('#countryAndUniversity #u'+countryIndex).remove();
			countryIndex--;
		}
	});
	$('.cancelBtnLor').on('click',function(){
		window.location = '${pageContext.request.contextPath}/homepage'
	});
	
	$('#toeflOrIeltsMarksheet').bind(
			'change',
			function() {
				let fileInput = document.getElementById('toeflOrIeltsMarksheet');
		          
		        let filePath = fileInput.value;
		      
		        // Allowing file type
		        let allowedExtensions = /(\.doc|\.docx|\.pdf|\.jpg|\.jpeg|\.png)$/i;
		          
		        if (!allowedExtensions.exec(filePath)) {
		            alert('Invalid file type!!');
		            fileInput.value = '';
		            return false;
		        } 
			});
	$('#examMarksheet').bind(
			'change',
			function() {
				let fileInput = document.getElementById('examMarksheet');
		          
		        let filePath = fileInput.value;
		      
		        // Allowing file type
		        let allowedExtensions = /(\.doc|\.docx|\.pdf|\.jpg|\.jpeg|\.png)$/i;
		          
		        if (!allowedExtensions.exec(filePath)) {
		            alert('Invalid file type!!');
		            fileInput.value = '';
		            return false;
		        } 
			});
	
	
	
});
</script>


