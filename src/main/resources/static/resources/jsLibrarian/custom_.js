// JavaScript Document


//balnce height custom
	$(window).load(function() {
		$('.login_Height').balance() ;
		$('.dashboxhedHeight').balance() ;
		$('.course_widget_height').balance() ;
		$('.dashboard_height').balance() ;
		$('.conHeight').balance() ;
		
	}) ;
// End blance height



// Toggle  js custom
	$(".hide_user_profile").click(function(){
		$(".show_user_profile").toggle();
	});
	
	
	$(".conleft").click(function(){
		$(".conright").toggle();
	});
	
	
	
	$(".hide_course_submenu").click(function (e) {
	   $(".show_course_submenu").hide();
	   var showiconDiv = $(this).closest('div').next();
	   showiconDiv.css("display", "block");
	});
	$(function () {
	   $(window).click(function (e) {
		   if (e.target.class != 'hide_course_submenu') {
			   $(".show_course_submenu").hide();
		   }
	   });
	});
	/*$(".hide_course_submenu").click(function (e) {
	   $(".show_course_submenu").hide();
	   var showiconDiv = $(this).closest('div').next();
	   showiconDiv.css("display", "block");
	});*/
// Toggle End Js
