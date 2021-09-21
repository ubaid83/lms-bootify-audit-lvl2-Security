define(["jquery", "dataTables.bootstrap"], function($) {
	$(function() {
	    $('.table').DataTable( {
	    	"dom": '<"top"i>rt<"bottom"flp><"clear">',
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
	    
	    window.checkAll = function checkAll(){
	    	$('input:checkbox[name=students]').prop('checked', true);
	    	return false;
	    }


	    window.uncheckAll = function uncheckAll(){
	    	$('input:checkbox[name=students]').prop('checked', false);
	    	return false;
	    }
	    
	} );
});




