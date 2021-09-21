define(["jquery", "dataTables.bootstrap","froala_tables","froala_lists","froala_colors","froala_font_family",
        "froala_font_size","froala_block_styles","froala_media_manager","froala_video",
        "froala_inline_styles","froala_fullscreen","froala_char_counter","froala_entities",
        "froala_file_upload","froala_urls"], function($) {
	$(function() {
		$('#editor').editable({inlineMode: false,
            buttons: ['bold', 'italic', 'underline', 'sep', 
                      'strikeThrough', 'subscript', 'superscript', 'sep',
                      'fontFamily', 'fontSize', 'color', 'formatBlock', 'blockStyle', 'inlineStyle','sep',
                      'align', 'insertOrderedList', 'insertUnorderedList', 'outdent', 'indent', 'selectAll','sep',
                      'createLink', 'table','sep',
                      'undo', 'redo', 'sep',
                      'insertHorizontalRule', 'removeFormat', 'fullscreen'],
            minHeight: 200,
            paragraphy: false,
            placeholder: 'Enter here',
            theme: 'blue',
            key:'vA-16ddvvzalxvB-13C2uF-10A-8mG-7eC5lnmhuD3mmD-16==',
            toolbarFixed: false
        });
		
	    $('.table').DataTable( {
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