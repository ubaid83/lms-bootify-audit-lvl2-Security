define(
		[ "jquery", "bootstrap-editable", "jBox" ],
		function($) {
			$(function() {
				// toggle `popup` / `inline` mode
				$.fn.editable.defaults.mode = 'inline';
				console.log('editable js ');
				$('.editable').each(function() {
					$(this).editable({
						success : function(response, newValue) {
							obj = JSON.parse(response);
							if (obj.status == 'error') {
								return obj.msg; // msg will be shown in editable
								// form
							}
						}
					});
				});

			});
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
																column.header())
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

			$(function() {
				window.showModal = function showModal(id, subject) {
					var myModal = new jBox('Modal', {
						ajax : {
							url : 'getAssignmentDetails',
							data : 'id=' + id,
							reload : true
						},
						animation : 'flip',
						draggable : true,
						title : subject

					});

					myModal.open();
				}
			});

		});