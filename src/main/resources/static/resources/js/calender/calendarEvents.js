/* CALENDAR */
		  
		    function  init_calendar() {
		    	$("#addEvent").css("display","none");
				$("#updateEvent").css("display","none");
				$("#inviteEvent").css("display","none");
				 $("#deleteEvent").css("display","none");
				if( typeof ($.fn.fullCalendar) === 'undefined'){ return; }
				console.log('init_calendar');
					
				var date = new Date(),
					d = date.getDate(),
					m = date.getMonth(),
					y = date.getFullYear(),
					started,
					categoryClass;

				var calendar = $('#calendar').fullCalendar({

				  header: {
					left: 'prev,next today',
					center: 'title',
					right: 'month,agendaWeek,agendaDay,listMonth'
				  },
				  eventStartEditable:false,
				  selectable: true,
				  selectHelper: true,
				  select: function(start, end, allDay) {
					//$('#fc_create').click();
					  callFc_CreateClick();
					document.getElementById("eventForm").reset();
					$("#addEvent").css("display","block");
					$("#updateEvent").css("display","none");
					$("#inviteEvent").css("display","none");
					$("#deleteEvent").css("display","none");
					started = start;
					ended = end;
					console.log('start parameter '+start+' end parameter '+end)
					$(".antosubmit").on("click", function() {
					  var title = $("#title").val();
					  if (end) {
						ended = end;
					  }

					  categoryClass = $("#event_type").val();

					  if (title) {
						calendar.fullCalendar('renderEvent', {
							title: title,
							start: started,
							end: end,
							allDay: allDay
						  },
						  true // make the event "stick"
						);
					  }

					  $('#title').val('');

					  calendar.fullCalendar('unselect');

					  $('.antoclose').click();

					  return false;
					});
				  },
				  eventClick: function(calEvent, jsEvent, view) {
					//$('#fc_create').click();
					 
					  callFc_CreateClick();
					document.getElementById("eventForm").reset();
					
					var StartDateToFormat= calEvent.startDate;
					var EndDateToFormat= calEvent.endDate;
					//formattedEndDate.replace(' ','T')
					console.log("------format date "+calEvent.endDateTFormat);
					$('#event').val(calEvent.title);
					$('#description').val(calEvent.description);
					$('#calenderId').val(calEvent.id);
					$('#courseDropDown').val(calEvent.course);
					$('#startDate').val(StartDateToFormat.replace(' ','T'));
					$('#endDate').val(EndDateToFormat.replace(' ','T'));
					$('#showStatusDropDown').val(calEvent.showStatus);
					$("#inviteEvent").val(calEvent.id);
					if(calEvent.showStatus ==='N'){

                        $('#inviteEvent').attr('disabled',true);

                        }
					$("#addEvent").css("display","none");
					$("#updateEvent").css("display","block");
					$("#inviteEvent").css("display","block");
					$("#deleteEvent").css("display","block");
					categoryClass = $("#event_type").val();

					$(".antosubmit2").on("click", function() {
					  calEvent.title = $("#title2").val();

					  calendar.fullCalendar('updateEvent', calEvent);
					  $('.antoclose2').click();
					});

					calendar.fullCalendar('unselect');
				  },
				  editable: true,
				  events: events/*[{
					title: 'All Day Event777',
					start: new Date(y, m, 1)
				  }, {
					title: 'Long Event',
					start: new Date(y, m, d - 5),
					end: new Date(y, m, d - 2)
				  }, {
					title: 'Meeting',
					start: new Date(y, m, d, 10, 30),
					allDay: false
				  }, {
					title: 'Lunch',
					start: new Date(y, m, d + 14, 12, 0),
					end: new Date(y, m, d, 14, 0),
					allDay: false
				  }, {
					title: 'Birthday Party',
					start: new Date(y, m, d + 1, 19, 0),
					end: new Date(y, m, d + 1, 22, 30),
					allDay: false
				  }, {
					title: 'Click for Google',
					start: new Date(y, m, 28),
					end: new Date(y, m, 29),
					url: 'http://google.com/'
				  }]*/
				});
				
				function callFc_CreateClick() {
					 
					$('#fc_create').click();
					$('body').addClass('hidePadding');
					}
				
			};