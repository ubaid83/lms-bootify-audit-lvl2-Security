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



<div class="card bg-white border">
      <div class="card-body">
                        <div class="text-center">
                              <sec:authorize access="hasRole('ROLE_STUDENT')">
                                    <h5>Pending Assignments</h5>
                              </sec:authorize>
                              <sec:authorize access="hasRole('ROLE_FACULTY')">
                                    <h5>Assignments</h5>
                              </sec:authorize>
                        </div>

                        <div class="x_itemCount text-center" style="display: none;">
                              <div class="image_not_found">
                                    <i class="fas fa-newspaper-o"></i>
                                    <p>
                                          <label class="x_count"></label>Assignments
                                    </p>
                              </div>
                        </div>

                        <div class="x_content">
                              <div class="table-responsive testAssignTable">
                                    <c:choose>
                                          <c:when test="${ not empty assignments}">

                                                <table class="table table-striped table-hover">
                                                      <thead>
                                                            <tr>

                                                                  <th>Name</th>
                                                                  <th>Start Date</th>
                                                                  <th>End Date</th>
                                                                  <th>Total Marks</th>
                                                                  <sec:authorize access="hasAnyRole('ROLE_STUDENT')">
                                                                        <th>Submitted</th>
                                                                  </sec:authorize>
                                                                  <th>Action</th>
                                                            </tr>
                                                      </thead>
                                                      <tbody>
                                                            <c:forEach var="assignment" items="${assignments}"
                                                                  varStatus="status">
                                                                  <sec:authorize access="hasAnyRole('ROLE_STUDENT')">
                                                                        <c:url value="assignmentDetails" var="assignmentDetailsurl">
                                                                              <c:param name="id" value="${assignment.id}" />
                                                                        </c:url>
                                                                  </sec:authorize>

                                                                  <tr>

                                                                        <td><sec:authorize access="hasAnyRole('ROLE_STUDENT')">
                                                                                    <a href="${assignmentDetailsurl}"> <c:out
                                                                                                value="${assignment.assignmentName}" /></a>
                                                                              </sec:authorize> <sec:authorize access="hasAnyRole('ROLE_FACULTY')">
                                                                                    <c:out value="${assignment.assignmentName}" />
                                                                              </sec:authorize></td>
                                                                        <td><c:out value="${assignment.startDate}" /></td>
                                                                        <td><c:out value="${assignment.endDate}" /></td>
                                                                        <td><c:out value="${assignment.maxScore}" /></td>
                                                                        <sec:authorize access="hasRole('ROLE_STUDENT')">
                                                                              <td><c:if
                                                                                          test="${assignment.submissionStatus eq 'Y' }">
                                                                                          <i class="check_ellipse fa fa-check bg-green"></i>
                                                                                          <c:out value="Submitted" />
                                                                                    </c:if> <c:if test="${assignment.submissionStatus ne 'Y' }">
                                                                                          <i class="check_ellipse fa fa-check"></i>
                                                                                          <c:out value="Not Submitted" />
                                                                                    </c:if></td>
                                                                        </sec:authorize>
                                                                        <td><sec:authorize access="hasRole('ROLE_STUDENT')">
                                                                                    <c:if test="${assignment.submissionStatus eq 'Y' }">
                                                                                          <i class="check_ellipse fa fa-location-arrow"></i>
                                                                                          <!-- <a href="#" title="Assignment Submitted">Submitted</a>&nbsp; -->
                                                                                          <c:out value="Submitted" />
                                                                                    </c:if>

                                                                                    <c:if test="${assignment.submissionStatus ne 'Y' }">

                                                                                          <c:url value="submitAssignmentForm" var="submiturl">
                                                                                                <c:param name="id" value="${assignment.id}" />
                                                                                          </c:url>
                                                                                          <i class="check_ellipse fa fa-location-arrow"></i>

                                                                                          <a href="${submiturl}" title="Submit Assignment">Submit</a>&nbsp;
                                                                                    </c:if>

                                                                              </sec:authorize> <sec:authorize
                                                                                    access="hasAnyRole('ROLE_FACULTY','ROLE_DEAN')">
                                                                                    <c:url value="viewAssignment" var="editAssignmentUrl">
                                                                                          <c:param name="id" value="${assignment.id}" />
                                                                                    </c:url>
                                                                                    <i class="check_ellipse fa fa-location-arrow"></i>
                                                                                    <a href="${editAssignmentUrl}" title="View Assignment">View
                                                                                          Assignment</a>&nbsp;
                                                                        </sec:authorize></td>


                                                                  </tr>
                                                            </c:forEach>

                                                      </tbody>
                                                </table>
                                          </c:when>
                                          <c:otherwise>
                                                <div class="text-center mt-3">
                                                      <div class="image_not_found">
                                                            <i class="fas fa-file-signature"></i>
                                                            <p>No Assignment Data</p>
                                                      </div>
                                                </div>
                                          </c:otherwise>
                                    </c:choose>

                              </div>
                        </div>
      </div>
</div>

