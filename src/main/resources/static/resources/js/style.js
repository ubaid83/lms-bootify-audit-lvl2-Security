var dashboardPie;
var dashboardBar;
var AssignmentBar;
var testBar;
var assignmentStudentBar;
var testStudentLine;
$(document)
		.ready(
				function() {
					/* Tool Tip */
					$('[data-toggle="tooltip"]').tooltip({
						trigger : 'hover'
					})
					//EMPTY SEARCH BOX
					$('input[type="search"]').attr("autocomplete", "off");
					$('input[type="search"]').val('');
					$('input[type=search]').attr("readonly", true);
					$('input[type=search]').click(function(){
						console.log("clicked")
						$(this).attr("readonly", false)
					})
					

					/* CUSTOM ALERT */
					$('.alert-ok, .alert-close').click(function() {

						$('.alertWrapper').toggleClass('d-none');
					});

					/*
					 * ========================================== LOGIN
					 * BACKGROUND IMAGE SLIDER
					 * ==========================================
					 */
					var imageArray = [ "../img/loginBg2.jpg",
							"../img/loginBg1.jpg", "../img/loginBg3.jpg",
							"../img/loginBg4.jpg" ]

					var i = 0;
					setInterval(function() {

						$('.loginBody').css({
							'background-image' : 'url(' + imageArray[i] + ')',
							'transition' : '1s'
						});

						i += 1;
						if (i == imageArray.length) {
							i = 0;
						}
					}, 5000);

					/*
					 * ========================================== MODAL SIDEBAR
					 * SLIDE ==========================================
					 */

					$('#sidebarClose').click(function() {

						$('#leftNav .modal-content').addClass('sidebarHide');

						window.setTimeout(function() {
							$('#leftnav').modal('hide');
						}, 500);
					});

					$('.leftnav-btn').click(
							function() {

								$('#leftNav .modal-content').removeClass(
										'sidebarHide');
							});

					// right navigation

					$('#rightNav #sidebarClose').click(function() {
						$('#rightNav .modal-content').addClass('sidebarHide');

						window.setTimeout(function() {
							$('#rightnav').modal('hide');
						}, 500);
					});
					$('.navbar-toggler').click(
							function() {
								$('#rightNav .modal-content').removeClass(
										'sidebarHide');
							});

					// Sidebar caret animation

					$('#leftnav .collapse, #rightnav .collapse').on(
							'show.bs.collapse',
							function() {

								$(this).parent().find('.fa-caret-right')
										.toggleClass('rotCar');
							});
					$('#leftnav .collapse, #rightnav .collapse').on(
							'hide.bs.collapse',
							function() {

								$(this).parent().find('.fa-caret-right')
										.toggleClass('rotCar');
							});

					$('.hoverDiv').hover(function() {
						$(this).find('.asset1 p').css('background', '#d53439');
						$(this).find('.asset1 img').toggleClass('trans');
					}, function() {
						$(this).find('.asset1 p').css('background', '#565656');
						$(this).find('.asset1 img').toggleClass('trans');
					});

					/*
					 * ========================================== RIGHT SIDEBAR
					 * JS ==========================================
					 */
					$('.sidebar .card-header').click(function() {
						$(this).find('.fas').toggleClass('rot-180');
					});

					$('.dbCard .card-header').click(function() {
						$(this).find('.fas').toggleClass('rot-180');
					});

					// ASSIGNMENT PAGE JS

					$('#subAssign')
							.click(
									function() {
										$('.alertWrapper')
												.toggleClass('d-none');
										$('.alert-text')
												.html(
														'Alert! Your assignment will be submitted for evaluation. Click \'Ok\' to confirm.');

										$('.alert-ok')
												.click(
														function() {
															$('#subDisplay')
																	.html(
																			"<div class='alert alert-success alert-dismissible fade show' role='alert'><strong>Well done!</strong> Your assignmnet has been submitted.<button type='button' class='close' data-dismiss='alert' aria-label='Close'><span aria-hidden='true'>&times;</span></button></div>");
														});

									});

					$('#resubAssign')
							.click(
									function() {

										$('.alertWrapper')
												.toggleClass('d-none');
										$('.alert-text')
												.html(
														'Warning! Your assignment will be submitted for re-evaluation. Click \'Ok\' to confirm.');

										$('.alert-ok')
												.click(
														function() {
															$('#editDisplay')
																	.html(
																			"<div class='alert alert-success alert-dismissible fade show' role='alert'><strong>Well done!</strong> Your assignmnet is sent for re-evaluation.<button type='button' class='close' data-dismiss='alert' aria-label='Close'><span aria-hidden='true'>&times;</span></button></div>");
														});
									});

					// My Course PAGE JS

				/*	$('#testTable input[value="Give Test"]')
							.click(
									function() {

										$('.alertWrapper')
												.toggleClass('d-none');
										$('.alert-text')
												.html(
														'Do you really want to start the test?');
										$('.alert-ok').html('Yes');
										$('.alert-close').html('No');

										// If OK is clicked load test module

										$('.alert-ok')
												.click(
														function() {
															// SHOW TEST MODULE
															$('#testModule')
																	.modal(
																			{
																				backdrop : 'static',
																				keyboard : false
																			});

															// PREVENT BACK
															history
																	.pushState(
																			null,
																			null,
																			document.URL);
															window
																	.addEventListener(
																			'popstate',
																			function() {
																				history
																						.pushState(
																								null,
																								null,
																								document.URL);
																			});
															// PREVENT REFRESH

															window.onbeforeunload = function() {
																return "Your progress will be lost!";
															}
															$(function() {
																$(document)
																		.keydown(
																				function(
																						e) {
																					return (e.which || e.keyCode) != 116;
																				});
															});

															// STOPWATCH
															$("#DateCountdown")
																	.TimeCircles();
															// For Mobile
															$(
																	"#CountDownTimerMobile, #CountDownTimer")
																	.TimeCircles(
																			{
																				time : {
																					Hours : {
																						show : true,
																						color : "#d53439"
																					},
																					Minutes : {
																						show : true,
																						color : "#2e8a00"
																					},
																					Seconds : {
																						show : true,
																						color : "#071e38"
																					}

																				}
																			});
															$(
																	"#CountDownTimerMobile, #CountDownTimer")
																	.TimeCircles(
																			{
																				circle_bg_color : "#d2d2d2"
																			});

															// For larger
															// devices

															$("#PageOpenTimer")
																	.TimeCircles();

															var updateTime = function() {
																var date = $(
																		"#date")
																		.val();
																var time = $(
																		"#time")
																		.val();
																var datetime = date
																		+ ' '
																		+ time
																		+ ':00';
																$(
																		"#DateCountdown")
																		.data(
																				'date',
																				datetime)
																		.TimeCircles()
																		.start();
															}
															$("#date")
																	.change(
																			updateTime)
																	.keyup(
																			updateTime);
															$("#time")
																	.change(
																			updateTime)
																	.keyup(
																			updateTime);
															// Stopwatch Timer
															// end

															window
																	.setTimeout(
																			function() {
																				var divHeight = $(
																						'.testQA')
																						.height();

																				$(
																						'.testQToggle')
																						.css(
																								{
																									'min-height' : divHeight
																											+ 'px',
																									'overflow-y' : 'scroll'
																								});
																			},
																			200);

															// Disable close and
															// nav buttons
															$(
																	'#modalTest .close')
																	.attr(
																			"disabled",
																			true);
															// Enable Complete
															// test button after
															// 30mins
															$("#CountDownTimer")
																	.TimeCircles()
																	.addListener(
																			function(
																					unit,
																					value,
																					total) {
																				if (total <= 5400) {
																					$(
																							'.completeWrap')
																							.removeClass(
																									'd-none');
																					$(
																							'.btn-complete')
																							.attr(
																									"disabled",
																									false);
																				}
																			});

															$('.btn-next')
																	.click(
																			function() {
																				var radioVal = $('#modalTest input[name="answer"]:checked').length;

																				if (radioVal == 0) {
																					$(
																							'.alert-text')
																							.html(
																									"Please select an answer before you go to next question.");
																					$(
																							'.alert-ok')
																							.html(
																									"Ok.");
																					$(
																							'.alert-close')
																							.html(
																									"Cancel");
																					$(
																							'.alertWrapper')
																							.removeClass(
																									'd-none')
																				}

																			});

															$('.skipQ')
																	.html(
																			'<i class="fas fa-flag-checkered">');

														});
										// End Clicked OK for test module
									});
*/
					// View Test Result Modal
					$('#testTable input[value="View"]').click(function() {
						$('#testResult').modal('show');
					});

					// Login Page

					if ($(window).width() <= 767) {

						var detach = $('.loginInfo').detach();
						$(detach).insertAfter('.loginField');
					}

					// DISPLAY NO DATA WHEN DATASETS ARE EMPTY
					Chart.plugins.register({
						afterDraw : function(chart) {
							if (chart.data.datasets[0].data.length === 0) {
								// No data is present
								var ctx = chart.chart.ctx;
								var width = chart.chart.width;
								var height = chart.chart.height
								chart.clear();

								ctx.save();
								ctx.textAlign = 'center';
								ctx.textBaseline = 'middle';
								ctx.font = "16px normal 'Helvetica Nueue'";
								ctx.fillText('No data to display', width / 2,
										height / 2);
								ctx.restore();
							}
						}
					});
					// END DISPLAY NO DATA WHEN DATASETS ARE EMPTY

					// Test Chart current Sem

					// import data for pie chart

					if ($('#testPieChart').length) {
						var dataArr = [];
						$.ajax({
							type : 'POST',
							url : myContextPath + '/getTestStats',
							success : function(data) {

								var parsedObj = JSON.parse(data);

								if (parsedObj.hasOwnProperty("passed")) {
									dataArr.push(Number(parsedObj.passed));
									dataArr.push(Number(parsedObj.pending));
									dataArr.push(Number(parsedObj.failed));
									console.log(dataArr);

									dashboardPie = new Chart(document
											.getElementById("testPieChart"), {
										type : 'pie',
										data : {
											labels : [ "Completed", "Pending",
													"Failed" ],
											datasets : [ {
												label : "Test",
												backgroundColor : [ "#39b54a",
														"#feb42f", "#d53439" ],
												data : dataArr
											} ]
										},
										options : {
											legend : {
												display : false
											},
											title : {
												display : true,
												text : 'Test Pie Chart'
											}
										}
									});
								} else {
									dashboardPie = new Chart(document
											.getElementById("testPieChart"), {
										type : 'pie',
										data : {
											labels : [ "Completed", "Pending",
													"Failed", "No Data" ],
											datasets : [ {
												label : "Test",
												backgroundColor : [ "#39b54a",
														"#feb42f", "#d53439",
														"#ddd" ],
												data : [ 0, 0, 0, 1 ]
											} ]
										},
										options : {
											legend : {
												display : false
											},
											title : {
												display : true,
												text : 'Test Pie Chart'
											}
										}
									});
								}
							},
							error : function(result) {
								var parsedObj = JSON.parse(result);
								console.log('error' + parsedObj);
							}
						});
						// End import data for pie chart
					}
					/*
					 * if($('#testPieChart').length) { new
					 * Chart(document.getElementById("testPieChart"), { type:
					 * 'pie', data: { labels: ["Completed", "Pending",
					 * "Failed"], datasets: [ { label: "Test", backgroundColor:
					 * ["#39b54a", "#feb42f","#d53439"], data: dataArr } ] },
					 * options: { legend: { display: false }, title: { display:
					 * true, text: 'Test Pie Chart' } } }); }
					 */
					// 1st Semester
					if ($('#testPieChartSem1').length) {
						new Chart(document.getElementById("testPieChartSem1"),
								{
									type : 'pie',
									data : {
										labels : [ "Completed", "Pending",
												"Failed" ],
										datasets : [ {
											label : "Test",
											backgroundColor : [ "#39b54a",
													"#feb42f", "#d53439" ],
											data : [ 5, 3, 0 ]
										} ]
									},
									options : {
										legend : {
											display : false
										},
										title : {
											display : true,
											text : 'Test Pie Chart'
										}
									}
								});
					}

					// ASSIGNMENT CHART
					if ($('#assignBarChart').length) {

						var dataArr1 = [];
						$
								.ajax({
									type : 'POST',
									url : myContextPath + '/getAssignmentStats',
									success : function(data) {

										var parsedObj = JSON.parse(data);

										if (parsedObj
												.hasOwnProperty("completed")) {
											dataArr1
													.push(Number(parsedObj.completed));
											dataArr1
													.push(Number(parsedObj.pending));
											dataArr1
													.push(Number(parsedObj.lateSubmitted));
											dataArr1
													.push(Number(parsedObj.rejected));
											console.log(dataArr1);

											dashboardBar = new Chart(
													document
															.getElementById("assignBarChart"),
													{
														type : 'bar',
														data : {
															labels : [
																	"Completed",
																	"Pending",
																	"Late Submitted",
																	"Rejected" ],
															datasets : [ {
																label : "Assignments",
																backgroundColor : [
																		"#39b54a",
																		"#feb42f",
																		"#8E5EA2",
																		"#d53439" ],
																data : dataArr1
															} ]
														},
														options : {
															legend : {
																display : false
															},
															title : {
																display : true,
																text : 'Assignments Bar Chart'
															},
															scales : {
																yAxes : [ {
																	ticks : {
																		min : 0,
																		stepSize : 20
																	}
																} ]
															}
														}
													});
										} else {
											dashboardBar = new Chart(
													document
															.getElementById("assignBarChart"),
													{
														type : 'bar',
														data : {
															labels : [
																	"Completed",
																	"Pending",
																	"Late Submitted",
																	"Rejected" ],
															datasets : [ {
																label : "Assignments",
																backgroundColor : [
																		"#39b54a",
																		"#feb42f",
																		"#8E5EA2",
																		"#d53439" ],
																data : [ 0, 0,
																		0, 0 ]
															} ]
														},
														options : {
															legend : {
																display : false
															},
															title : {
																display : true,
																text : 'Assignments Bar Chart (No Data)'
															},
															scales : {
																yAxes : [ {
																	ticks : {
																		min : 0,
																		stepSize : 1
																	}
																} ]
															}
														}
													});
										}
									},
									error : function(result) {
										var parsedObj = JSON.parse(result);
										console.log('error' + parsedObj);
									}
								});

						/*
						 * new Chart(document.getElementById("assignBarChart"), {
						 * type: 'bar', data: { labels: ["Completed", "Pending",
						 * "Rejected"], datasets: [ { label: "Assignments",
						 * backgroundColor: ["#39b54a", "#feb42f","#d53439"],
						 * data: [5,2,1] } ] }, options: { legend: { display:
						 * false }, title: { display: true, text: 'Assignments
						 * Bar Chart' }, scales: { yAxes: [{ ticks: { min: 0,
						 * stepSize: 1 } }] } } });
						 */
					}

					// Assignment chart sem I
					if ($('#assignBarChartSem1').length) {
						new Chart(
								document.getElementById("assignBarChartSem1"),
								{
									type : 'bar',
									data : {
										labels : [ "Completed", "Pending",
												"Rejected" ],
										datasets : [ {
											label : "Assignments",
											backgroundColor : [ "#39b54a",
													"#feb42f", "#d53439" ],
											data : [ 5, 3, 0 ]
										} ]
									},
									options : {
										legend : {
											display : false
										},
										title : {
											display : true,
											text : 'Assignments Bar Chart'
										},
										scales : {
											yAxes : [ {
												ticks : {
													min : 0,
													stepSize : 1
												}
											} ]
										}
									}
								});
					}

					// Assignment Chart

					if ($('#assignmentBarChart').length) {

						var acadSession = $('#assignSem').val();
						var courseId = $('#assignCourse').val();

						var dataArr1 = [];
						$
								.ajax({
									type : 'POST',
									url : myContextPath
											+ '/getAssignmentStatsBySem?acadSess='
											+ acadSession + '&courseId='
											+ courseId,
									success : function(data) {

										var parsedObj = JSON.parse(data);

										if (parsedObj
												.hasOwnProperty("completed")) {
											dataArr1
													.push(Number(parsedObj.completed));
											dataArr1
													.push(Number(parsedObj.pending));
											dataArr1
													.push(Number(parsedObj.lateSubmitted));
											dataArr1
													.push(Number(parsedObj.rejected));
											console.log(dataArr1);

											if (AssignmentBar) {
												AssignmentBar.destroy();
											}
											AssignmentBar = new Chart(
													document
															.getElementById("assignmentBarChart"),
													{
														type : 'bar',
														data : {
															labels : [
																	"Completed",
																	"Pending",
																	"Late Submitted",
																	"Rejected" ],
															datasets : [ {
																label : "Total",
																backgroundColor : [
																		"#2ea745",
																		"#d69400",
																		"#8e5ea2",
																		"#d53439" ],
																data : dataArr1
															} ]
														},
														options : {
															responsive : true,
															maintainAspectRatio : false,
															legend : {
																display : false
															},
															scales : {
																yAxes : [ {
																	ticks : {
																		min : 0,
																		stepSize : 1
																	}
																} ],
																xAxes : [ {
																	ticks : {
																		fontSize : 14
																	}
																} ]
															},

															title : {
																display : true,
																text : 'Overall Assignment Data'
															}
														}
													});
										} else {
											AssignmentBar = new Chart(
													document
															.getElementById("assignmentBarChart"),
													{
														type : 'bar',
														data : {
															labels : [
																	"Completed",
																	"Pending",
																	"Late Submitted",
																	"Rejected" ],
															datasets : [ {
																label : "Total",
																backgroundColor : [
																		"#2ea745",
																		"#d69400",
																		"#8e5ea2",
																		"#d53439" ],
																data : [ 0, 0,
																		0, 0 ]
															} ]
														},
														options : {
															responsive : true,
															maintainAspectRatio : false,
															legend : {
																display : false
															},
															scales : {
																yAxes : [ {
																	ticks : {
																		min : 0,
																		stepSize : 1
																	}
																} ],
																xAxes : [ {
																	ticks : {
																		fontSize : 14
																	}
																} ]
															},

															title : {
																display : true,
																text : 'Overall Assignment Data (No Data)'
															}
														}
													});
										}
									},
									error : function(result) {
										var parsedObj = JSON.parse(result);
										console.log('error' + parsedObj);
									}
								});

					}

					if ($('#courseAssignChart').length) {
						new Chart(
								document.getElementById("courseAssignChart"),
								{
									type : 'line',
									data : {
										labels : [ "Jan", "Feb", "Mar", "Apr",
												"May", "Jun", "Jul", "Aug",
												"Sep", "Oct", "Nov", "Dec" ],
										datasets : [ {
											label : "Marks",
											data : [ 50, 28, 87, 20, 50, 78,
													45, 93, 27, 55, 78, 33 ],
											fill : true,
											pointBorderColor : "#071e38",
											borderColor : "rgb(90, 213, 52, 0.8)",
											pointBackgroundColor : "#071e38",
											pointHoverBackgroundColor : "#071e38",
											backgroundColor : "rgb(90, 213, 52, 0.5)",
											pointHitRadius : 7,
											pointRadius : 3
										} ]
									},
									options : {
										responsive : true,
										maintainAspectRatio : false,
										legend : {
											display : false
										},
										scales : {
											yAxes : [ {
												ticks : {
													min : 0,
													fixedStepSize : 25
												}
											} ],
										},

										title : {
											display : true,
											text : 'Completed Assignments Chart'
										}
									}

								});
					}

					if ($('#courseTestChart').length) {
						new Chart(document.getElementById("courseTestChart"), {
							type : 'line',
							data : {
								labels : [ "Jan", "Feb", "Mar", "Apr", "May",
										"Jun", "Jul", "Aug", "Sep", "Oct",
										"Nov", "Dec" ],
								datasets : [ {
									label : "Marks",
									data : [ 50, 28, 87, 20, 50, 78, 45, 93,
											27, 55, 78, 33 ],
									fill : true,
									pointBorderColor : "#071e38",
									borderColor : "rgb(92, 166, 189, 0.8)",
									pointBackgroundColor : "#071e38",
									pointHoverBackgroundColor : "#071e38",
									backgroundColor : "rgb(92, 166, 189, 0.5)",
									pointHitRadius : 7,
									pointRadius : 3
								} ]
							},
							options : {
								responsive : true,
								maintainAspectRatio : false,
								legend : {
									display : false
								},
								scales : {
									yAxes : [ {
										ticks : {
											min : 0,
											fixedStepSize : 25
										}
									} ],
								},
								elements : {
									line : {
										tension : 0
									}
								},

								title : {
									display : true,
									text : 'Completed Assignments Chart'
								}
							}

						});
					}

					// Assignment Chart

					if ($('#testBarChart').length) {

						var acadSession = $('#testSem').val();
						var courseId = $('#testCourse').val();

						var dataArr1 = [];

						$
								.ajax({
									type : 'POST',
									url : myContextPath
											+ '/getTestStatsBySem?acadSess='
											+ acadSession + '&courseId='
											+ courseId,
									success : function(data) {

										var parsedObj = JSON.parse(data);

										if (parsedObj.hasOwnProperty("passed")) {
											dataArr1
													.push(Number(parsedObj.passed));
											dataArr1
													.push(Number(parsedObj.pending));
											dataArr1
													.push(Number(parsedObj.failed));
											console.log(dataArr1);

											testBar = new Chart(
													document
															.getElementById("testBarChart"),
													{
														type : 'bar',
														data : {
															labels : [
																	"Passed",
																	"Pending",
																	"Failed" ],
															datasets : [ {
																label : "Total",
																backgroundColor : [
																		"#2ea745",
																		"#d69400",
																		"#d53439" ],
																data : dataArr1,

															} ]
														},
														options : {
															responsive : true,
															maintainAspectRatio : false,
															legend : {
																display : false
															},
															scales : {
																yAxes : [ {
																	ticks : {
																		min : 0,
																		stepSize : 1
																	}
																} ],
																xAxes : [ {
																	ticks : {
																		fontSize : 14
																	}
																} ]
															},

															title : {
																display : true,
																text : 'Overall Test Data'
															}
														}
													});

										} else {

											testBar = new Chart(
													document
															.getElementById("testBarChart"),
													{
														type : 'bar',
														data : {
															labels : [
																	"Passed",
																	"Pending",
																	"Failed" ],
															datasets : [ {
																label : "Total",
																backgroundColor : [
																		"#2ea745",
																		"#d69400",
																		"#d53439" ],
																data : [ 0, 0,
																		0 ],

															} ]
														},
														options : {
															responsive : true,
															maintainAspectRatio : false,
															legend : {
																display : false
															},
															scales : {
																yAxes : [ {
																	ticks : {
																		min : 0,
																		stepSize : 1
																	}
																} ],
																xAxes : [ {
																	ticks : {
																		fontSize : 14
																	}
																} ]
															},

															title : {
																display : true,
																text : 'Overall Test Data (no data)'
															}
														}
													});

										}
									},
									error : function(result) {
										var parsedObj = JSON.parse(result);
										console.log('error' + parsedObj);
									}
								});
					}

					/*
					 * if ($(window).width() >= 480) { if
					 * ($('#testBarChart').length) { new Chart(
					 * document.getElementById("testBarChart"), { type : 'bar',
					 * data : { labels : [ "Completed", "Pending", "Failed" ],
					 * datasets : [ { label : "Total", backgroundColor : [
					 * "#2ea745", "#d69400", "#d53439" ], data : [ 0, 0, 0 ],
					 *  } ] }, options : { responsive : true,
					 * maintainAspectRatio : false, legend : { display : false },
					 * scales : { yAxes : [ { ticks : { min : 0, stepSize : 1 } } ],
					 * xAxes : [ { ticks : { fontSize : 12 } } ] },
					 * 
					 * title : { display : true, text : 'Overall Assignment
					 * Data(No Data)' } } }); } } else { if
					 * ($('#testBarChart').length) { new Chart(
					 * document.getElementById("testBarChart"), { type : 'bar',
					 * data : { labels : [ "Completed", "Pending", "Failed" ],
					 * datasets : [ { label : "Total", backgroundColor : [
					 * "#2ea745", "#d69400", "#d53439" ], data : [ 0, 0, 0 ],
					 *  } ] }, options : { responsive : true,
					 * maintainAspectRatio : false, legend : { display : false },
					 * scales : { yAxes : [ { ticks : { min : 0, stepSize : 1 } } ],
					 * xAxes : [ { ticks : { fontSize : 8 } } ] },
					 * 
					 * title : { display : true, text : 'Overall Assignment
					 * Data(No Data)' } } }); } }
					 */

					/*
					 * ========================================== STUDENT
					 * PROFILE PAGE JS
					 * ==========================================
					 */

					$('.stdntProfileRight .editStdntDetail').click(function() {
						$(this).parent().find('input').prop('disabled', false);
						$(this).parent().find('input').focus();
					});

					/*
					 * ========================================== GUARDIAN
					 * DASHBOARD PAGE JS
					 * ==========================================
					 */
					// DASHBOARD TOTAL ASSIGNMENT CHART
					if ($('#totalAssignPieChart').length) {
						new Chart(
								document.getElementById("totalAssignPieChart"),
								{
									type : 'pie',
									data : {
										labels : [ "Completed", "Pending" ],
										datasets : [ {
											backgroundColor : [ "#07B315",
													"#D60202" ],
											data : [ 4, 1 ]
										} ]
									},
									options : {
										responsive : true,
										maintainAspectRatio : false,
										legend : {
											display : true
										},
										title : {
											display : true,
											text : 'ASSIGNMENT CHART (Completed vs Pending)'
										}
									}
								});
					}
					// DASHBOARD TOTAL TEST CHART
					if ($('#totalTestPieChart').length) {
						new Chart(
								document.getElementById("totalTestPieChart"),
								{
									type : 'pie',
									data : {
										labels : [ "Completed", "Pending" ],
										datasets : [ {
											backgroundColor : [ "#07B315",
													"#D60202" ],
											data : [ 3, 2 ]
										} ]
									},
									options : {
										responsive : true,
										maintainAspectRatio : false,
										legend : {
											display : true
										},
										title : {
											display : true,
											text : 'TEST CHART (Completed vs Pending)'
										}
									}
								});
					}

					/*
					 * ========================================== MY REPORT PAGE
					 * ==========================================
					 */
					// ASSIGNMENT REPORT CHART
					if ($('#assignReportChart').length) {
						const
						cnv = document.getElementById('assignReportChart');
						const
						chart = new Chart(cnv.getContext('2d'), {
							'type' : 'bar',
							'data' : {
								'datasets' : []
							},
							'options' : {
								'animation' : {
									'duration' : 0
								},
								'maintainAspectRatio' : false,
								'responsive' : true,
								'scales' : {
									'xAxes' : [ {
										'barPercentage' : 0.5,
										'categoryPercentage' : 1,
										'stacked' : true,
										'barThickness' : 40
									} ],
									'yAxes' : [ {
										'gridLines' : {
											'zeroLineColor' : '#000',
											'zeroLineWidth' : 1,
											'offsetGridLines' : false
										},
										ticks : {
											stepSize : 10,
											max : 100
										}
									} ]
								}
							}
						});

						chart.data.labels = [ 'Jan', 'Feb', 'Mar', 'Apr',
								'May', ];
						// Mark secured in test or assignment
						chart.data.datasets.push({
							'data' : [ {
								"x" : "Jan",
								"y" : 50
							}, {
								"x" : "Feb",
								"y" : 90
							}, {
								"x" : "Mar",
								"y" : 75
							}, {
								"x" : "Apr",
								"y" : 60
							}, {
								"x" : "May",
								"y" : 50
							}, ],
							'type' : 'bar',
							'label' : 'Marks scored',
							'backgroundColor' : '#177726',
							'barPercentage' : 0.5,
							'categoryPercentage' : 1,
						});
						// MARK LEFT UNSCORED
						chart.data.datasets.push({
							'data' : [ {
								"x" : "Jan",
								"y" : -10
							}, {
								"x" : "Feb",
								"y" : -10
							}, {
								"x" : "Mar",
								"y" : -5
							}, {
								"x" : "Apr",
								"y" : -40
							}, {
								"x" : "May",
								"y" : 0
							} ],
							'type' : 'bar',
							'label' : 'Marks Unscored',
							'backgroundColor' : '#ff0000',
							'barPercentage' : 0.5,
							'categoryPercentage' : 1,
						});

						chart.update();
					}

					// TEST REPORT CHART

					if ($('#testReportChart').length) {
						const
						cnv = document.getElementById('testReportChart');
						const
						chart = new Chart(cnv.getContext('2d'), {
							'type' : 'line',
							'data' : {
								'datasets' : []
							},
							'options' : {
								'animation' : {
									'duration' : 0
								},
								'maintainAspectRatio' : false,
								'responsive' : true,
								'scales' : {
									'xAxes' : [ {
										'barPercentage' : 0.5,
										'categoryPercentage' : 1,
										'stacked' : true
									} ],
									'yAxes' : [ {
										'gridLines' : {
											'zeroLineColor' : '#000',
											'zeroLineWidth' : 1,
											'offsetGridLines' : false
										},
										ticks : {
											stepSize : 10,
											max : 100
										}
									} ]
								}
							}
						});

						chart.data.labels = [ 'Jan', 'Feb', 'Mar', 'Apr',
								'May', ];
						// Mark secured in test or assignment
						chart.data.datasets.push({
							'data' : [ {
								"x" : "Jan",
								"y" : 70
							}, {
								"x" : "Feb",
								"y" : 50
							}, {
								"x" : "Mar",
								"y" : 80
							}, {
								"x" : "Apr",
								"y" : 45
							}, {
								"x" : "May",
								"y" : 50
							}, ],
							'type' : 'line',
							'label' : 'Marks scored',
							'backgroundColor' : '#177726',
							'barPercentage' : 0.5,
							'categoryPercentage' : 1,
							lineTension : 0,
							fill : true,
							backgroundColor : "rgb(23, 119, 38, 0.5)",
							pointHitRadius : 10,
							pointRadius : 5,
							pointHoverRadius : 7,
							borderColor : "#177726"
						});
						// MARK LEFT UNSCORED
						chart.data.datasets.push({
							'data' : [ {
								"x" : "Jan",
								"y" : -30
							}, {
								"x" : "Feb",
								"y" : 0
							}, {
								"x" : "Mar",
								"y" : -20
							}, {
								"x" : "Apr",
								"y" : -5
							}, {
								"x" : "May",
								"y" : 0
							} ],
							'type' : 'line',
							'label' : 'Marks Unscored',
							'backgroundColor' : '#ff0000',
							'barPercentage' : 0.5,
							'categoryPercentage' : 1,

							lineTension : 0,
							fill : true,
							pointHitRadius : 10,
							pointRadius : 5,
							pointHoverRadius : 7,
							backgroundColor : "rgb(214, 0, 0, 0.5)",
							borderColor : "#ff0000"

						});

						chart.update();
					}

					/*
					 * ========================================== ATTENDANCE
					 * PAGE JS ==========================================
					 */
					$('#summaryFilter').click(function() {
						$('#summaryFilterContent').toggleClass('d-none');
					});

					$('#tabularFilter').click(function() {
						$('#tabularFilterContent').toggleClass('d-none');
					});

					/*
					 * ========================================== FOOTER FIX
					 * WHEN WINDOW HEIGHT
					 * ==========================================
					 */

					setTimeout(function() {
						$('.loginBody #userName').focus();

					}, 2001);

					$('#fgtPwd').click(function() {
						setTimeout(function() {
							$('#sapId').focus();
						}, 500);

					});

					/*
					 * Create Assignment Page Toggle
					 * 
					 * 
					 * 
					 * $('#plagscanRequired').click(function() {
					 * $('#plagContent').toggleClass('d-none');
					 * console.log('clicked!!!!'); if
					 * ($('#plagscanRequired').is(":checked")) {
					 * console.log('clicked Yes');
					 * $('#threshold').prop('required', true);
					 * $('#plagscanRequired').val('Yes');
					 *  } else { $('#plagscanRequired').val('No');
					 * $('#threshold').prop('required', false); } });
					 * 
					 * $('#runPlagiarism').click(function() {
					 * 
					 * if ($('#plagscanRequired').is(":checked")) {
					 * $('#runPlagiarism').val('Submission'); } else {
					 * $('#runPlagiarism').val('Manual'); }
					 * 
					 * }); Create Assignment Page Toggle End
					 */

					// ==================================================================
					// Document Ready end
					
					
					if($('#assignReportChartStudent').length > 0){
					
						assignmentStudentBar = new Chart(
								document
										.getElementById("assignReportChartStudent"),
								{
	
									'type' : 'bar',
									'data' : {
	
										'datasets' : [
												{
													label : "Marks Scored",
													backgroundColor : "#177726",
													data : [ 0 ]
												},
												{
													label : "Marks Unscored",
													backgroundColor : "#ff0000",
													data : [ 0 ]
												} ]
									},
									'options' : {
										'animation' : {
											'duration' : 0
										},
										'maintainAspectRatio' : false,
										'responsive' : true,
										'scales' : {
											'xAxes' : [ {
												'barPercentage' : 0.5,
												'categoryPercentage' : 1,
												'stacked' : true,
												'barThickness' : 40
											} ],
											'yAxes' : [ {
												'gridLines' : {
													'zeroLineColor' : '#000',
													'zeroLineWidth' : 1,
													'offsetGridLines' : false
												},
												ticks : {
													stepSize : 10,
													min : 0,
													max : 100
	
												}
											} ]
										}
									}
								});
						}
					
					if($('#testReportChartStudent').length > 0){
						
						testStudentLine = new Chart(
								document
										.getElementById("testReportChartStudent"),
								{

									'type' : 'line',
									'data' : {
										'datasets' : [
												{
													label : "Marks Scored",
													backgroundColor : "#177726",
													data : [ 0 ]
												},
												{
													label : "Marks Unscored",
													backgroundColor : "#ff0000",
													data : [ 0 ]
												} ]
									},
									'options' : {
										'animation' : {
											'duration' : 0
										},
										'maintainAspectRatio' : false,
										'responsive' : true,
										'scales' : {
											'xAxes' : [ {
												'barPercentage' : 0.5,
												'categoryPercentage' : 1,
												'stacked' : true
											} ],
											'yAxes' : [ {
												'gridLines' : {
													'zeroLineColor' : '#000',
													'zeroLineWidth' : 1,
													'offsetGridLines' : false
												},
												ticks : {
													stepSize : 10,
													min : 0,
													max : 100
												}
											} ]
										}
									}
								});
						}
					
					
				});

// Accordion Scroll on Top
$('#dashboardPage .collapse').on('shown.bs.collapse', function(e) {
	var $card = $(this).closest('.card');
	$('html,body').animate({
		scrollTop : $card.offset().top - 80
	}, 500);
});

$('.custToggle').click(function() {
	var thisVal = $(this).val();
	if (thisVal == 'Y') {
		$(this).val('N');
	} else {
		$(this).val('Y');
	}

});

new WOW().init();
