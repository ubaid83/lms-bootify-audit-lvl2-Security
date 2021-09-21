/**
 * Left Navigation only
 */
define(["jquery", "bootstrap", "simpler-sidebar"], function($) {
	$(function() {
		$('#sidebar').show().simplerSidebar({
			opener : '#toggle-sidebar',
			attr : 'simplersidebar',
			top : 60,
			animation : {
				easing : 'easeOutQuint'
			},
			sidebar : {
				align : 'left',
				width : 200,
				closingLinks : '.close-sb',
			},
			mask: {
		        display: false
			}
		});
	});
});