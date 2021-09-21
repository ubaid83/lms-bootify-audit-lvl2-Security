/*define(["jquery", "bootstrap"], function($) {*/
	$(function() {
		
		

		
		
		$(".table_overflow").each(function(){
			var id = $(this).attr("id");
			console.log("id -->"+id);
			if(id == 'feedBackTable-0'){
				$('#' + id).show();
			}else{
				$('#' + id).hide();
			}
		});
		window.showModal = function showModal(currentModal, modalToView, navToSelect) {
			$('#' + currentModal).hide();
			$('#' + modalToView).show();
			$('#' + navToSelect).addClass('active').siblings(".active").removeClass("active");
			
		};

		window.submitForm = function submitForm(formId, navId, isComplete) {
			$('#' + navId).addClass("submitted");
			$.post('addStudentFeedbackResponse',
					$('#' + formId).serialize()).then(function(response) {
				if (isComplete) {
					
					return $.post('completeStudentFeedback', {id : response.studentFeedbackId, username : response.username});
					
				}
			}).done(function(response) {
				if(response)
					
					window.location.href = 'homepage';
					//location.reload();
			}).fail(function() {
			}).always(function() {
			});
		};
	});
/*});*/

