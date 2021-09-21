<%@page import="java.util.*"%>
<%@page import="com.spts.lms.beans.dashboard.DashBoard"%>
<%-- <jsp:include page="../common/DashboardHeader.jsp" /> --%>
<%-- <jsp:include page="../common/NewHeader1.jsp" /> --%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>


<%
	List<DashBoard> courseDetailList = (List<DashBoard>) session
.getAttribute("courseDetailList");

Map<String,List<DashBoard>> dashboardListSemesterWise = (Map<String,List<DashBoard>>)
session.getAttribute("sessionWiseCourseListMap");
%>
							<%
								int divCounter=0;
for(String s : dashboardListSemesterWise.keySet()){
divCounter++;
int count = 0;
							%>
							<input type="hidden" id="mapSize"
								value="<%=dashboardListSemesterWise.size()%>"> <a>

											  <div class="card">
                                <div class="card-header" id="flip<%=divCounter%>" class="flipClass"
									style="padding: 5px; background-color: #d9d9d9; border: solid 1px #c3c3c3;">
                                    <h5 class="mb-0">
                                        <button class="btn btn-link w-100" data-toggle="collapse" data-target="#semCurrent" aria-expanded="true" aria-controls="collapsesemCurrent">
                                            <h5>Current Semester <span>&#40;&nbsp;<%=s%>&nbsp;&#41;</span></h5>
                                        </button>
                                    </h5>
                                 </div>
                                </div>

									<div class="clearfix"></div>
								</div>

							</a>
							<%
								}
							%>