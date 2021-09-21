define(["jquery", "bootstrap"], function($) {
	$(function() {
		$('#feedbackModal').modal('show');
		$('#modal-0').show();
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
					location.reload();
			}).fail(function() {
			}).always(function() {
			});
		};
	});
});