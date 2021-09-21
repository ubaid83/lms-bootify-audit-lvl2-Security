define(["jquery", "dataTables.bootstrap", "jquery.cascadingdropdown"], function($) {
	$(function() {
		function createDataTable() {
			$('.table').DataTable( {
				"lengthMenu" : [ [ 10, 25, 50, -1 ],
									[ 10, 25, 50, "All" ] ],
		        initComplete: function () {
		            this.api().columns().every( function () {
		                var column = this;
		                var headerText = $(column.header()).text();
		                if(headerText == "Sr. No." || headerText == "Select To Allocate")
		                	return;
		                var select = $('<select class="form-control"><option value="">All</option></select>')
		                    .appendTo( $(column.footer()).empty() )
		                    .on( 'change', function () {
		                        var val = $.fn.dataTable.util.escapeRegex(
		                            $(this).val()
		                        );
		 
		                        column
		                            .search( val ? '^'+val+'$' : '', true, false )
		                            .draw();
		                    } );
		 
		                column.data().unique().sort().each( function ( d, j ) {
		                    select.append( '<option value="'+d+'">'+d+'</option>' )
		                } );
		            } );
		        }
		    } );
		}
		
		window.checkAll = function checkAll(){
            $('input:checkbox[name=students]').prop('checked', true);
            return false;
          }


          window.uncheckAll = function uncheckAll(){
            $('input:checkbox[name=students]').prop('checked', false);
            return false;
          }
		function destroyDataTable() {
			$('.table').DataTable().destroy();
		}
		function clearDataTable() {
			$('.table').DataTable().clear().draw();
		}
		createDataTable();
		$('#studentFeedbackForm').cascadingDropdown({
			selectBoxes: [
	              {
	            	  selector : '#course',
	            	  paramName: 'courseId'
	              },
	              {
	            	  selector : '#acadMonth',
	            	  paramName: 'acadMonth'
	              },
	              {
	            	  selector : '#acadYear',
	            	  paramName: 'acadYear'
	              },
	              {
	            	  selector : '#faculty',
	            	  paramName: 'facultyId',
	            	  requires : ['#course', '#acadMonth', '#acadYear'],
	            	  requireAll: true,
	            	  onChange: function(event, value, requiredValues) {
	            		  var feedback = $('#feedback').val();
	            		  if(feedback && feedback.length && value && value.length)
		            		  $.getJSON('getStudentsForFeedback', $.extend({feedbackId: feedback, facultyId: value}, requiredValues), function(data) {
		            			destroyDataTable();
		            			$('#studentFeedbackTable tbody').html( 
		            			 $.map(data,function(item, index){
		            				 var allocated = '';
		            				 if(item.id) {
		            					 allocated = 'Feedback Allocated';
		            				 } else {
		            					 allocated = '<input type="checkbox" name="students" value="'+item.username+'" />';
		            				 }
		            				 var $tr = $('<tr>').append(
		            				            $('<td>').text(index+1),
		            				            $('<td>').html(allocated),
		            				            $('<td>').text(item.programName),
		            				            $('<td>').text(item.firstname + " " + item.lastname)
		            				        );
		            				 return $tr;
		            			 })
		            			);
		            			createDataTable();
		                      });
	                  },
	            	  source: function(request, response) {
	            		  $.getJSON('courseFacultyList', request, function(data) {
	            			  var faculty = $('#faculty').data('value');
	            			  response($.map(data, function(item, index) {
	            				  if(item.username == faculty) {
	            					  return {
	                                      label: item.firstname + " " + item.lastname,
	                                      value: item.username,
	                                      selected: true
	                                  };  
	            				  }
	                              return {
	                                  label: item.firstname + " " + item.lastname,
	                                  value: item.username
	                              };
	                          }));
	                      });
	            	  }
	              }
	              
			             ],
	         onChange: function(event, dropdownData) {
	        	 if(dropdownData.facultyId.length == 0) {
	        		 clearDataTable();
	        	 }
	         }
		});
		
	});
});