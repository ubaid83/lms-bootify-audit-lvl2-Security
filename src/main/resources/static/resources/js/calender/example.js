angular.module('mwl.calendar.docs', [ 'mwl.calendar', 'ngAnimate',
		'ui.bootstrap', 'colorpicker.module' ]);
angular.module('mwl.calendar.docs') // you will need to declare your module with
// the dependencies ['mwl.calendar',
// 'ui.bootstrap', 'ngAnimate']
.controller(
		'KitchenSinkCtrl',
		function(moment, alert, calendarConfig) {

			var vm = this;
			vmout = vm;

			// These variables MUST be set as a minimum for the calendar to work
			vm.calendarView = 'month';
			vm.viewDate = new Date();
			var actions = [ {
				label : '<i class=\'glyphicon glyphicon-pencil\'></i>',
				onClick : function(args) {
					console.log("Calender events click " + args.calendarEvent)
					alert.show('Edited', args.calendarEvent);
				}
			}, {
				label : '<i class=\'glyphicon glyphicon-remove\'></i>',
				onClick : function(args) {
					alert.show('Deleted', args.calendarEvent);
				}
			} ];
			var jsArray = [];
			// console.log("------------->"+ json );
			// jsArray = ${json};
			// console.log("------------->"+ json );
			/*
			 * for(int i = 0;i<jsArray.length;i++){ var obj = jsArray[i];
			 * obj["actions"] = actions; } console.log("jsArray"+jsArray);
			 */
			vm.actions = actions;
			
			console.log("events" + events);
			console.log("time date default is 0------> "
					+ moment().startOf('week').subtract(2, 'days').add(8,
							'hours').toDate());
			console.log("time date is   "
					+ moment(Date()).format("YYYY-MM-DD HH:mm:ss"));
			console.log("events" + events);
			if (events)
				vm.events = events;
			else
				vm.events = [];

			vm.cellIsOpen = true;

			vm.addEvent = function() {
				console.log("hii example called ");
				vm.events.push({
					title : 'New event',
					startsAt : moment().startOf('day').toDate(),
					endsAt : moment().endOf('day').toDate(),
					color : calendarConfig.colorTypes.important,
					draggable : true,
					resizable : true
				});
				
				var url1 = '/addCalenderEvent1';
                var location='/viewEvents';
                console.log(getDate());
                data1 = '{"username": "","event": "event1testsucess","startDate":"'+getDate()+'","endDate":"'+getDate()+'"}' ;
				AddEventDB(url1,location,data1);
				
				
				
			};

			vm.eventClicked = function(event) {
				console.log("calleddd clicked");
				alert.show('Clicked', event);
			};

			vm.eventEdited = function(event) {
				console.log("calleddd update called ");
				alert.show('Edited', event);
			};

			vm.eventDeleted = function(event) {
				console.log("calleddd delete functionality");
				alert.show('Deleted', event);
			};

			vm.eventTimesChanged = function(event) {
				console.log("calleddd delete .>>>>>");
				alert.show('Dropped or resized', event);
			};

			vm.toggle = function($event, field, event) {
				$event.preventDefault();
				$event.stopPropagation();
				event[field] = !event[field];
			};

			vm.timespanClicked = function(date, cell) {

				if (vm.calendarView === 'month') {
					if ((vm.cellIsOpen && moment(date).startOf('day').isSame(
							moment(vm.viewDate).startOf('day')))
							|| cell.events.length === 0 || !cell.inMonth) {
						vm.cellIsOpen = false;
					} else {
						vm.cellIsOpen = true;
						vm.viewDate = date;
					}
				} else if (vm.calendarView === 'year') {
					if ((vm.cellIsOpen && moment(date).startOf('month').isSame(
							moment(vm.viewDate).startOf('month')))
							|| cell.events.length === 0) {
						vm.cellIsOpen = false;
					} else {
						vm.cellIsOpen = true;
						vm.viewDate = date;
					}
				}

			};

			vm.deleterow = function(event) {
					console.log('EVENT ID'+event.currentTarget.id);
                    var url1 = 'deleteCalenderEvent?programId='+event.currentTarget.id;
                    var location='viewEvents';
					deleteRowsDB(url1,location);
					/*var res = $http.post('/deleteCalenderEvent',event.currentTarget.id);
					res.success(function(data, status, headers, config) {
						console.log('SUCCESS');
					});
					res.error(function(data, status, headers, config) {
						alert( "failure message: " + JSON.stringify({data: data}));
					});	*/	
					
			};
			
			vm.updaterow = function(event) {
				console.log('EVENT ID'+event.currentTarget.id);
                var url1 = '/addCalenderEventForm?id='+event.currentTarget.id;
                var location='addCalenderEventForm?id='+event.currentTarget.id;
                window.location.href = location;
               // editRowsDB(url1,location);
				/*var res = $http.post('/deleteCalenderEvent',event.currentTarget.id);
				res.success(function(data, status, headers, config) {
					console.log('SUCCESS');
				});
				res.error(function(data, status, headers, config) {
					alert( "failure message: " + JSON.stringify({data: data}));
				});	*/	
				
		};
		
		
		vm.calenderinviterow = function(event) {
			console.log('EVENT ID'+event.currentTarget.id);
            var url1 = '/viewCalenderEventDetails?eventId='+event.currentTarget.id;
            var location='viewCalenderEventDetails?eventId='+event.currentTarget.id;
            window.location.href = location;
          
	      };
			

		});


function deleteRowsDB(url1,location){
	//console.log("urllllllllllllllllll"+url1);
	

	console.log(contextPath);
	//----------------
	
	
                      
                       if (url1) {
                       	console.log("called")
                             $
                                         .ajax({
                                               type : 'POST',
                                               url : url1,
                                               success : function(
                                                           data) {
                                               	console.log("added  ");
                                               	window.location.href = location;
                                       
                                               }
                                         });
                       } else {
                            // alert('Error while deleting');
                       }
                

	//------------------
	
}

function editRowsDB(url1,location){
	//console.log("called------>>>"+id);
	
	//----------------
	
	
                      
                       if (url1) {
                       	console.log("called")
                             $
                                         .ajax({
                                               type : 'GET',
                                               url : url1,
                                               success : function(
                                                           data) {
                                               	console.log("added  ");
                                               	window.location.href = location;
                                       
                                               }
                                         });
                       } else {
                            // alert('Error while deleting');
                       }
                

	//------------------
	
}

function AddEventDB(url1,location,data1){
	//console.log("called------>>>"+id);
	
	//----------------
	
	
                      
                       if (url1) {
                       	console.log("called")
                             $
                                         .ajax({
                                               type : 'POST',
                                               contentType : 'application/json; charset=utf-8',
                                               dataType : 'json',
                                               url : url1,
                                               data : data1,
                                               success : function(
                                                           data) {
                                               	console.log("sucessfully deleted");
                                               	window.location.href = location;
                                       
                                               }
                                         });
                       } else {
                            // alert('Error while deleting');
                       }
                

	//------------------
	
}

function getDate() {
	  var date = new Date(),
	    year = date.getFullYear(),
	    month = (date.getMonth() + 1).toString(),
	    formatedMonth = (month.length === 1) ? ("0" + month) : month,
	    day = date.getDate().toString(),
	    formatedDay = (day.length === 1) ? ("0" + day) : day,
	    hour = date.getHours().toString(),
	    formatedHour = (hour.length === 1) ? ("0" + hour) : hour,
	    minute = date.getMinutes().toString(),
	    formatedMinute = (minute.length === 1) ? ("0" + minute) : minute,
	    second = date.getSeconds().toString(),
	    formatedSecond = (second.length === 1) ? ("0" + second) : second;
	  return  year + "-" + formatedMonth + "-" +formatedDay + " " + formatedHour + ':' + formatedMinute + ':' + formatedSecond;
	};


