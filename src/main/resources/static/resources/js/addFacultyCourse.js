define(["jquery", "dataTables.bootstrap", "jquery.cascadingdropdown"], function($) {
	$(function() {
		function createDataTable() {
			$('.table').DataTable( {
		        initComplete: function () {
		            this.api().columns().every( function () {
		                var column = this;
		                var headerText = $(column.header()).text();
		                if(headerText == "Sr. No." || headerText == "Action")
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
		function destroyDataTable() {
			$('.table').DataTable().destroy();
		}
		function clearDataTable() {
			$('.table').DataTable().clear().draw();
		}
		createDataTable();
		$('#addFacultyCourseForm').cascadingDropdown({
			selectBoxes: [
	              {
	            	  selector : '#acadMonth',
	            	  paramName: 'acadMonth'
	              },
	              {
	            	  selector : '#acadYear',
	            	  paramName: 'acadYear'
	              },{
	            	  selector : '#dummy',
	            	  paramName: 'dummy',
	            	  requires : ['#acadMonth', '#acadYear'],
	            	  requireAll: true,
	            	  source: function(request, response) {
	            		  $.getJSON('searchCourseFaculty',request, function(data) {
	                 			destroyDataTable();
	                 			$('#facultyCourseTable tbody').html( 
	                 			 $.map(data,function(item, index){
	                 				var $del = $('<a>')
	                 							.prop({
	                 								'href': 'deleteFacultyCourse?id='+item.id+'&acadMonth='+item.acadMonth+'&acadYear='+item.acadYear,
	                 								'title': 'Delete'
	                 							}).click(function() {
	                 								return confirm("Are you sure you want to delete this record?");
	                 							})
	                 							.append('<i class="fa fa-trash-o fa-lg"></i>');
	                 				 var $tr = $('<tr>').append(
	                 				            $('<td>').text(index+1),
	                 				            $('<td>').text(item.firstname + " " + item.lastname),
	                 				            $('<td>').text(item.courseName),
	                 				           $('<td>').html($del)
	                 				        );
	                 				 return $tr;
	                 			 })
	                 			);
	                 			createDataTable();
	                 			response([{
	                                label: "Dummy",
	                                value: "Dummy"
	                            }]);
	                           });
	            	  }
	              }
			             ]/*,
	         onChange: function(event, dropdownData) {
	        	 if(dropdownData.acadMonth.length && dropdownData.acadYear.length) {
	        		 $.getJSON('searchCourseFaculty',{acadMonth : dropdownData.acadMonth, acadYear : dropdownData.acadYear}, function(data) {
	          			destroyDataTable();
	          			$('#facultyCourseTable tbody').html( 
	          			 $.map(data,function(item, index){
	          				 var $tr = $('<tr>').append(
	          				            $('<td>').text(index+1),
	          				            $('<td>').text(item.firstname + " " + item.lastname),
	          				            $('<td>').text(item.courseName),
	          				           $('<td>').text("")
	          				        );
	          				 return $tr;
	          			 })
	          			);
	          			createDataTable();
	                  });        	 
	        	}
	         }*/
		});
		
	});
});
