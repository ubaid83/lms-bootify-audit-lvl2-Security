define(["jquery", "bootstrap", "Multi-Column-Select", "jquery.cascadingdropdown"], function($) {
	$(function() {
		$('#uploadUserCourseForm').cascadingDropdown({
			selectBoxes: [
	              {
	            	  selector : '#program',
	            	  paramName: 'programId'
	              },
	              {
	            	  selector : '#session',
	            	  paramName: 'sessionId',
	            	  requires : ['#program'],
	            	  source: function(request, response) {
	            		  $('#sessionSelect').MultiColumnSelectDestroy();
	            		  var self = this;
	            		  $.getJSON('programSessionList', request, function(data) {
	            			  response($.map(data, function(item, index) {
	                              return {
	                                  label: item.sessionNumber,
	                                  value: item.sessionNumber
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
	            	  requires : ['#session', '#program'],
	            	  requireAll: true,
	            	  source: function(request, response) {
	            		  $('#courseSelect').MultiColumnSelectDestroy();
	            		  $.getJSON('sessionCourseList', request, function(data) {
	            			  response($.map(data, function(item, index) {
	                              return {
	                                  label: item.courseName,
	                                  value: item.id,
	                                  selected: true
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