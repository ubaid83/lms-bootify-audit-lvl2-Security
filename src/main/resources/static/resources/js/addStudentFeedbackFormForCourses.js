define(
		[ "jquery", "dataTables.bootstrap", "jquery.cascadingdropdown" ],
		function($) {
			$(function() {
				function createDataTable() {
					$('.table')
							.DataTable(
									{
										"lengthMenu" : [ [ 10, 25, 50, -1 ],
															[ 10, 25, 50, "All" ] ],
										initComplete : function() {
											this
													.api()
													.columns()
													.every(
															function() {
																var column = this;
																var headerText = $(
																		column
																				.header())
																		.text();
																if (headerText == "Sr. No."
																		|| headerText == "Select To Allocate")
																	return;
																var select = $(
																		'<select class="form-control"><option value="">All</option></select>')
																		.appendTo(
																				$(
																						column
																								.footer())
																						.empty())
																		.on(
																				'change',
																				function() {
																					var val = $.fn.dataTable.util
																							.escapeRegex($(
																									this)
																									.val());

																					column
																							.search(
																									val ? '^'
																											+ val
																											+ '$'
																											: '',
																									true,
																									false)
																							.draw();
																				});

																column
																		.data()
																		.unique()
																		.sort()
																		.each(
																				function(
																						d,
																						j) {
																					select
																							.append('<option value="'
																									+ d
																									+ '">'
																									+ d
																									+ '</option>')
																				});
															});
										}
									});
				}

				window.checkAll = function checkAll() {
					$('input:checkbox[name=courseIds]').prop('checked', true);
					return false;
				}

				window.uncheckAll = function uncheckAll() {
					$('input:checkbox[name=courseIds]').prop('checked', false);
					return false;
				}
				function destroyDataTable() {
					$('.table').DataTable().destroy();
				}
				function clearDataTable() {
					$('.table').DataTable().clear().draw();
				}
				createDataTable();
				$('#studentFeedbackForm')
						.cascadingDropdown(
								{
									selectBoxes : [ {
										onChange : function(event, value,
												requiredValues) {
											var feedback = $('#feedback').val();
											var acadYear = $('#acadYearForAcadSession').val();
											var acadSession = $('#acadSessionId').val();
											console.log('entered boxes');
											console.log('feedbackDetails--->'+feedback+","+acadYear+","+acadSession);
											if (feedback && feedback.length
													&& value && value.length)
												$
														.getJSON(
																'getFeedbackByCourse',
																$
																		.extend(
																				{
																					id : feedback,
																					acadYear : acadYear,
																					acadSession:acadSession
																				}),
																function(data) {
																	destroyDataTable();
																	$(
																			'#studentFeedbackTable tbody')
																			.html(
																					$
																							.map(
																									data,
																									function(
																											item,
																											index) {
																										var allocated = '';
																										if (item.feedbackId) {
																											allocated = 'Feedback Allocated';
																										} else {
																											allocated = '<input type="checkbox" name="students" value="'
																													+ item.username
																													+ '" />';
																										}
																										var $tr = $(
																												'<tr>')
																												.append(
																														$(
																																'<td>')
																																.text(
																																		index + 1),
																														$(
																																'<td>')
																																.html(
																																		allocated),
																														$(
																																'<td>')
																																.text(
																																		item.programName),
																														$(
																																'<td>')
																																.text(
																																		item.firstname
																																				+ " "
																																				+ item.lastname));
																										return $tr;
																									}));
																	createDataTable();
																});
										}
									} ]
								});

			});
		});