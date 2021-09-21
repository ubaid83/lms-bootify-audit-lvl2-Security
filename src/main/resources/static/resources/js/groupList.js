define(["jquery", "jBox"], function($,jBox) {
	$(function() {
		window.showModal = function showModal(id, subject){
			var myModal = new jBox('Modal', {
			    ajax: {
			        url: 'getGroupDetails',
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