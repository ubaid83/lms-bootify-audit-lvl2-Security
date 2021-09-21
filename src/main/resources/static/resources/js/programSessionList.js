define(["jquery", "bootstrap"], function($) {
	$(function() {
		function enable_disable_chkbx() {
			$(this).parent().prevAll().find('input[type="checkbox"]').prop('checked', 'checked');
			$(this).parent().nextAll().find('input[type="checkbox"]').prop('checked', false);
		}
		
		$('input[type="checkbox"]').click(enable_disable_chkbx);
		
		// Select the current session checkboxes 
		$('input[type="checkbox"]').each(function(index, elem){
			var selected = "" + $(this).data('value');
			if(selected && selected.length && selected.indexOf($(this).val()) > -1){
				$(this).prop('checked', 'checked');
			}
		});
	});
});