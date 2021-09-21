define(["jquery", "bootstrap-editable", "jBox"], function($) {
	$(function() {
	    //toggle `popup` / `inline` mode
	    $.fn.editable.defaults.mode = 'inline';     
	    
	    $('.editable').each(function() {
	        $(this).editable({
	        	success: function(response, newValue) {
	        		obj = JSON.parse(response);
	                if(obj.status == 'error') {
	                	return obj.msg; //msg will be shown in editable form
	                }
	            }
	        });
	    });
	    
	});
	
	$(function() {
		window.showModal = function showModal(id, subject){
			var myModal = new jBox('Modal', {
			    ajax: {
			        url: 'getAssignmentDetails',
			        data: 'id='+id,
			        reload: true
			    },
			    animation: 'flip',
			    draggable:true,
			    title:subject
			    
			});
			
			myModal.open();
		}
	});
	
});