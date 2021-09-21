define(["jquery", "jBox"], function($,jBox) {
	$(function() {
		window.showModal = function showModal(id, subject){
			var myModal = new jBox('Modal', {
			    ajax: {
			        url: 'getAnnouncementDetails',
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