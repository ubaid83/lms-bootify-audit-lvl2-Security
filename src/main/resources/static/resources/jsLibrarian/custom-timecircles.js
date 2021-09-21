// JavaScript Document

function disableF5(e) {
	if ((e.which || e.keyCode) == 116)
		e.preventDefault();
};

// $("#DateCountdown").TimeCircles();
/*
 * $("#CountDownTimer").TimeCircles({ time: { Days: { show: false }, Hours: {
 * show: false } }});
 */
$("#PageOpenTimer").TimeCircles();

$('.btntestquiz').click(function() {
	$(document).bind("keydown", disableF5);
	setTimeout(loadTimeCircle, 40);
});

/*
 * function loadTimeCircle() {
 * 
 * var d = new Date(); var date = d.getFullYear() + '-' + (d.getMonth() + 1) +
 * '-' + d.getDate(); var time = (d.getHours() + 1) + ':' + (d.getMinutes() +
 * 1); //var datetime = date + ' ' + time + ':00'; var datetime=
 * $("#duration").val(); console.log("datetime--"+ datetime); //
 * $("#DateCountdown").data('date', datetime).TimeCircles().start();
 * $('#DateCountdown').attr('data-date', datetime).TimeCircles().start();
 * $("#DateCountdown").TimeCircles().destroy();
 * $("#DateCountdown").TimeCircles({ count_past_zero : false, time : {
 * 
 * Days : { show : false, text : "Days", color : "#FC6" }, Hours : { show :
 * true, text : "Hours", color : "#8f3e97" }, Minutes : { show : true, text :
 * "Minutes", color : "#15a3f9" }, Seconds : { show : true, text : "Seconds",
 * color : "#f8971c" } } }).addListener(countdownComplete); }
 */
function loadTimeCircle() {

	// var d = new Date();
	var d = new Date($("#dateTimeVal").val());
	console.log("received date" + d);

	var testCompletedOrNot = $("#testCompleted").val();

	console.log("is test completed??" + testCompletedOrNot);
	if (testCompletedOrNot == 'view') {
		console.log("if entered")
		console.log("test completed");
	}
	if (testCompletedOrNot == 'add') {
		console.log("else entered");
		var datetime = $("#duration").val();

		var newDate = new Date(datetime);
		console.log("dateTime received--->" + new Date(datetime));
		console.log("datetime--" + datetime);

		var systemDate = new Date();

		console.log("newdDate after adding duration--->" + newDate);
		console.log("systemDate--->" + systemDate);
		console.log("server date--->" + d);
		console.log("d time--->" + d.getTime());
		console.log("sys time--->" + systemDate.getTime());

		var milliseconds = '';

		if (d.getTime() > systemDate.getTime()) {
			console.log("server time is greater");
			milliseconds = (d.getTime() - systemDate.getTime());
		} else if (d.getTime() < systemDate.getTime()) {

			console.log("system time is greater");
			milliseconds = (systemDate.getTime() - d.getTime());
		} else {
			console.log(" time is same");
			milliseconds = newDate.getTime();
		}

		console.log("second difference" + milliseconds);

		/*
		 * int sec = (int) (seconds / 1000) % 60 ; int minutes = (int) ((seconds /
		 * (1000*60)) % 60); int hours = (int) ((seconds / (1000*60*60)) % 24);
		 * console.log("datedd"+hours+":"+minutes+":"+seconds );
		 */

		var day, hour, minute, seconds;
		seconds = Math.floor(milliseconds / 1000);
		minute = Math.floor(seconds / 60);
		seconds = seconds % 60;
		hour = Math.floor(minute / 60);
		minute = minute % 60;
		day = Math.floor(hour / 24);
		hour = hour % 24;

		console.log("datedd" + hour + ":" + minute + ":" + seconds);

		var date = d.getFullYear() + '-' + (d.getMonth() + 1) + '-'
				+ d.getDate();
		var time = "";
		var newDateTime = "";

		/*
		 * if (seconds < 0) { console.log("negative time") time =
		 * (newDate.getHours()) + ':' + ((newDate.getMinutes()) - (minute)) +
		 * ':' + ((newDate.getSeconds()) - (seconds)); newDateTime = date + ' ' +
		 * time; } else { console.log("positive time"); console.log("hours--->" +
		 * newDate.getHours()); console.log("minutes--->" +
		 * newDate.getMinutes());
		 * 
		 * time = (newDate.getHours()) + ':' + ((newDate.getMinutes()) -
		 * (minute)) + ':' + ((newDate.getSeconds()) - (seconds)); newDateTime =
		 * date + ' ' + time; }
		 */

		if (d.getTime() > systemDate.getTime()) {
			console.log("server time is greater111");

			var milSec = newDate.getTime() - milliseconds;
			newDate.setTime(milSec);
			console.log("time--->" + newDate.getHours() + newDate.getMinutes()
					+ newDate.getSeconds() + "");
			time = (newDate.getHours()) + ':' + ((newDate.getMinutes())) + ':'
					+ ((newDate.getSeconds()));

			newDateTime = date + ' ' + time;
		} else if (d.getTime() < systemDate.getTime()) {
			var milSec = newDate.getTime() + milliseconds;
			newDate.setTime(milSec);
			console.log("system time is greater111");
			console.log("time--->" + newDate.getHours() + newDate.getMinutes()
					+ newDate.getSeconds() + "");
			time = (newDate.getHours()) + ':' + ((newDate.getMinutes())) + ':'
					+ ((newDate.getSeconds()));

			newDateTime = date + ' ' + time;
		} else {
			console.log("same time ");
			newDateTime = datetime;
		}

		console.log("newDateTime:" + newDateTime);

		// $('#DateCountdown').attr('data-date',
		// datetime).TimeCircles().start();

		$('#DateCountdown').attr('data-date', newDateTime).TimeCircles()
				.start();
		$("#DateCountdown").TimeCircles().destroy();
		$("#DateCountdown").TimeCircles({
			count_past_zero : false,
			time : {

				Days : {
					show : false,
					text : "Days",
					color : "#FC6"
				},
				Hours : {
					show : true,
					text : "Hours",
					color : "#8f3e97"
				},
				Minutes : {
					show : true,
					text : "Minutes",
					color : "#15a3f9"
				},
				Seconds : {
					show : true,
					text : "Seconds",
					color : "#f8971c"
				}
			}
		}).addListener(countdownComplete);
	}
}

var i = 0;
var j = $('#durationCompletedByStudent').val();

function countdownComplete(unit, value, total) {
	console.log("function called---");

	i++;

	if (i % 60 == 0) {

		j++;
		console.log(j + ' minute completed');
		saveCompletedDuration(j);
	}

	if (total == 0) {
		
		submitForm('studentTestForm-0', 'nav-0', true);
	}

	if (total == 0) {
		console.log("total " + total);
		/*
		 * $("#DateCountdown").TimeCircles().stop();
		 * $(this).fadeOut('slow').replaceWith( "<h2 style='margin-top:5%;'>Time
		 * Over!</h2>");
		 */

		alert("Times up2!");
		window.location.href = 'testList';
		$('#test_quiz_pop2').fadeOut('slow');
	}
}

var updateTime = function() {
	var date = $("#date").val();
	var time = $("#time").val();
	var datetime = date + ' ' + time + ':00';
	$("#DateCountdown").data('date', datetime).TimeCircles().start();
}
$("#date").change(updateTime).keyup(updateTime);
$("#time").change(updateTime).keyup(updateTime);

// Start and stop are methods applied on the public TimeCircles instance
$(".startTimer").click(function() {
	$("#CountDownTimer").TimeCircles().start();
});
$(".stopTimer").click(function() {
	$("#CountDownTimer").TimeCircles().stop();
});

// Fade in and fade out are examples of how chaining can be done with
// TimeCircles
$(".fadeIn").click(function() {
	$("#PageOpenTimer").fadeIn();
});
$(".fadeOut").click(function() {
	$("#PageOpenTimer").fadeOut();
});
