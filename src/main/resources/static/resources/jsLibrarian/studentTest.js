define(["jquery", "bootstrap", "TimeCircles"], function($) {
	$(function() {
		$(".timer").TimeCircles({
		    "animation": "smooth",
		    "bg_width": 1.2,
		    "fg_width": 0.1,
		    "count_past_zero": false,
		    "circle_bg_color": "#60686F",
		    "time": {
		        "Days": {
		            "text": "Days",
		            "color": "#FFCC66",
		            "show": false
		        },
		        "Hours": {
		            "text": "Hours",
		            "color": "#99CCFF",
		            "show": true
		        },
		        "Minutes": {
		            "text": "Minutes",
		            "color": "#BBFFBB",
		            "show": true
		        },
		        "Seconds": {
		            "text": "Seconds",
		            "color": "#FF9999",
		            "show": true
		        }
		    }
		}).addListener(function(unit, value, total) {
			if(total == 0) {
				submitForm('studentTestForm-0','nav-0', true);
				window.location.href ='testList';
				alert("Times up!!!");
			}
		});
		$('#modal-0').show();
		$(':checked').parent().addClass('active');
		$('.nav-tabs-vert li a').click(function(event){
			$(".modal-content").hide();
			$(this).parent().addClass('active').siblings(".active").removeClass("active");
			$($(this).data('href')).show();
		});
		window.showModal = function showModal(currentModal, modalToView, navToSelect) {
			$('#' + currentModal).hide();
			$('#' + modalToView).show();
			$('#' + navToSelect).addClass('active').siblings(".active").removeClass("active");
		};

		window.submitForm = function submitForm(formId, navId, isComplete) {
			$('#' + navId).addClass("submitted");
			$.post('addStudentQuestionResponse',
					$('#' + formId).serialize()).then(function(response) {
				if (isComplete) {
					/*return $.post('completeStudentTest', {id : response.studentTestId, username : response.username});*/
				}
			}).done(function(response) {
				if(response)
					location.reload();
			}).fail(function() {
			}).always(function() {
			});
		};
		
		var tabsFn = (function() {
			  
			  function init() {
			    setHeight();
			  }
			  
			  function setHeight() {
			    var $tabPane = $('.tab-pane'),
			        tabsHeight = $('.nav-tabs-vert').height();
			    
			    /*$tabPane.css({
			      height: tabsHeight
			    });*/
			  }
			    
			  $(init);
			})();
	});
});