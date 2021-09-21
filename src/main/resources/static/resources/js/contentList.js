define(["jquery", "bootstrap", "jquery.treetable"], function($) {
	$(function(){
		$("#contentTree").treetable({ expandable: true });
		var expandNode = $("#contentTree tbody tr:first-child");
		if(expandNode.length) {
			$("#contentTree").treetable("expandNode", expandNode.data("tt-id"));
		}
	});
});
