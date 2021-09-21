
var dashboardPie;
var dashboardBar;
var AssignmentBar;
var testBar;



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
																text : 'Overall Assignment Data'
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
																text : 'Overall Assignment Data (no data)'
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
					 * ========================================== STUDENT Report
					 * PAGE JS ==========================================
					 */
					if ($('#assignReportChartStudent').length) {
						
						assignmentStudentBar = new Chart(
								document
										.getElementById("assignReportChartStudent"),
								{

									'type' : 'bar',
									'data' : {

										'datasets' : [{
											label : "Marks Scored",
											backgroundColor : "#177726",
											data : [0]
										},
										{
											label : "Marks Unscored",
											backgroundColor : "#ff0000",
											data : [0]
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

						assignmentStudentBar.update();
						
					}

					// TEST REPORT CHART

					if ($('#testReportChartStudent').length) {
						
						testStudentLine = new Chart(
								document
										.getElementById("testReportChartStudent"),
								{

									'type' : 'line',
									'data' : {
										'datasets' : [{
											label : "Marks Scored",
											backgroundColor : "#177726",
											data : [0]
										},
										{
											label : "Marks Unscored",
											backgroundColor : "#ff0000",
											data : [0]
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
													min: 0,
													max : 100
												}
											} ]
										}
									}
								});

						testStudentLine.update();
					}

					/*
					 * ========================================== End STUDENT
					 * Report PAGE JS ==========================================
					 */
					 
					 
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