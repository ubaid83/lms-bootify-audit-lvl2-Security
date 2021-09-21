define(["jquery", "bootstrap"], function($) {
	$(function() {
		window.singleSelectClicked = function singleSelectClicked(formId) {
			$('#'+formId+" .testOptions").find('input[type="checkbox"]').each(function(index, element) {
				var elem = $(element);
				elem.replaceWith(
				$("<input>",{
				    type:'radio',
				    name: elem.attr('name'),
				    value: elem.val()
				}));
			});
		}
		window.multipleSelectClicked = function multipleSelectClicked(formId) {
			$('#'+formId+" .testOptions").find('input[type="radio"]').each(function(index, element) {
				var elem = $(element);
				elem.replaceWith(
				$("<input>",{
				    type:'checkbox',
				    name: elem.attr('name'),
				    value: elem.val()
				}));
			});
		}

	});
	
	$(function() {
		window.singleSelectClicked1 = function singleSelectClicked(formId) {
			console.log('clicked'+formId);
			$('#' + formId + " .testOptions").find('input[type="checkbox"]')
					.each(function(index, element) {
						var elem = $(element);
						console.log("enterd111");
						elem.replaceWith($("<input>", {
							type : 'radio',
							name : elem.attr('name'),
							value : elem.val()
						}));
					});
		}
		window.multipleSelectClicked1 = function multipleSelectClicked(formId) {
			console.log('clicked1 '+formId);
			$('#' + formId + " .testOptions").find('input[type="radio"]')
					.each(function(index, element) {
						var elem = $(element);
						console.log("enterd221");
						elem.replaceWith($("<input>", {
							type : 'checkbox',
							name : elem.attr('name'),
							value : elem.val()
						}));
					});
		}

	});

});