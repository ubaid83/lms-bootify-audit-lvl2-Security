<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<c:if test="${webPage.leftNav}">
	<style type="text/css">
		#sidebar {
			display : none;
			left : -200px;
		}
		.bg-ablue {
			background: rgba(50, 200, 222, 0.66);
		}
		.ui-173 .ui-content {
		  padding-left: 0px;
		}
		.ui-173 .ui-btn {
			top: 50%;
			z-index: 2000;
		}
		.tree ul ul {
		    padding-left: 40px;
		}
		.tree li {
		    margin: 0px 0;
		    list-style-type: none;
		    position: relative;
		    padding: 5px 5px 0px 5px;
		}
		.tree li::before {
		    content:'';
		    position: absolute;
		    top: 0;
		    width: 1px;
		    height: 100%;
		    right: auto;
		    left: -20px;
		    border-left: 1px solid #ccc;
		    bottom: 50px;
		}
		.tree li::after {
		    content:'';
		    position: absolute;
		    top: 15px;
		    width: 25px;
		    height: 15px;
		    right: auto;
		    left: -20px;
		    border-top: 1px solid #ccc;
		}
		.tree li a {
		    display: inline-block;
		    border: 1px solid #ccc;
		    padding: 5px 10px;
		    text-decoration: none;
		    background: rgba(0, 145, 167, 0.64);
		    color: #fff;
		    font-family: arial, verdana, tahoma;
		    font-size: 11px;
		    /* border-radius: 5px;
		    -webkit-border-radius: 5px;
		    -moz-border-radius: 5px; */
		}
		/*Remove connectors before root*/
		 .tree > ul > li::before, .tree > ul > li::after {
		    border: 0;
		}
		/*Remove connectors after last child*/
		 .tree li:last-child::before {
		    height: 15px;
		}
		/*Time for some hover effects*/
		
		/*We will apply the hover effect the the lineage of the element also*/
		 .tree li a:hover, .tree li a:hover+ul li a {
		    background: #c8e4f8;
		    color: #000;
		    border: 1px solid #94a0b4;
		}
		/*Connector styles on hover*/
		 .tree li a:hover+ul li::after, .tree li a:hover+ul li::before, .tree li a:hover+ul::before, .tree li a:hover+ul ul::before {
		    border-color: #94a0b4;
		}
		
		.tree li > ul > li {
			height : 0;
			opacity : 0;
			transition: height 1s ease-out, opacity 1.3s ease-out;
	       -webkit-transition: height 1s ease-out, opacity 1.3s ease-out;
	       -moz-transition: height 1s ease-out, opacity 1.3s ease-out;
	       -o-transition: height 1s ease-out, opacity 1.3s ease-out;
	       -ms-transition: height 1s ease-out, opacity 1.3s ease-out;
		}
		
		.tree li:hover > ul > li {
			opacity: 1;
			height: 32px;
		}
	</style>
<!-- UI # -->
		<div class="ui-173">
		
			<!-- Button -->
			<div id="toggle-sidebar" class="ui-btn">
				<i class="fa fa-bars"></i>
			</div>
			<div id="sidebar" class="bg-lblue">
				<!-- UI Content -->
				<div class="ui-content clearfix">		
					<!-- Heading -->
					<div class="tree">
					<ul>
					<c:forEach items="${currentCourses}" var="course">
					<li>
						<c:url value="viewCourse" var="courseUrl">
							<c:param name="id">${course.id}</c:param>
						</c:url>
						<c:url value="assignmentList" var="assignmentUrl">
							<c:param name="courseId">${course.id}</c:param>
						</c:url>
						<c:url value="testList" var="testUrl">
							<c:param name="courseId">${course.id}</c:param>
						</c:url>
						<c:url value="studentContentList" var="contentUrl">
							<c:param name="courseId">${course.id}</c:param>
						</c:url>
						<a href="${courseUrl }">${course.courseName } </a>
						<ul>
							<li><a href="${assignmentUrl }"><i class="fa fa-file-text-o red"></i> Assignments </a></li>
							<li><a href="${testUrl }">Tests </a></li>
							<li><a href="${contentUrl }"><i class="fa fa-pencil-square-o purple"></i> Content </a></li>
						</ul>
					</li>
					</c:forEach>
					</ul>
					</div>
				</div>
			</div>
		</div>

	</c:if>		