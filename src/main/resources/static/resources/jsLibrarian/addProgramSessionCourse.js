define(["jquery", "bootstrap", "jquery.cascadingdropdown", "Multi-Column-Select"], function($) {
	$(function() {
		$('#addProgramSessionCourseForm').cascadingDropdown({
			selectBoxes: [
	              {
	            	  selector : '#program',
	            	  paramName: 'programId'
	              },
	              {
	            	  selector : '#session',
	            	  paramName: 'programSessionId',
	            	  requires : ['#program'],
	            	  source: function(request, response) {
	            		  $('#sessionSelect').MultiColumnSelectDestroy();
	            		  var val = this.el.data('value');
	            		  var self = this;
	            		  $.getJSON('programSessionList', request, function(data) {
	            			  response($.map(data, function(item, index) {
	                              return {
	                                  label: item.sessionNumber,
	                                  value: item.id,
	                                  selected: val == item.id
	                              };
	                          }));
	            			  if(data.length > 0)
	            				$('#sessionSelect').MultiColumnSelect({idprefix : 'sessionId-'});
	            			  $('#courseSelect').MultiColumnSelectDestroy();
	            			  self.el.change();
	                      });
	            	  }
	              },
	              {
	            	  selector : '#course',
	            	  paramName: 'courseId',
	            	  requires : ['#program','#session'],
	            	  requireAll: true,
	            	  source: function(request, response) {
	            		  $('#courseSelect').MultiColumnSelectDestroy();
	            		  $.getJSON('getCoursesForProgramSession', request, function(data) {
	            			  response($.map(data, function(item, index) {
	                              return {
	                                  label: item.courseName,
	                                  value: item.courseId,
	                                  selected: item.id != null
	                              };
	                          }));
	            			  if(data.length > 0)
	            				$('#courseSelect').MultiColumnSelect({multiple: true, idprefix : 'courseID-'});
	                      });
	            	  }
	              }
	              
			              ]
		});
		
	});
});