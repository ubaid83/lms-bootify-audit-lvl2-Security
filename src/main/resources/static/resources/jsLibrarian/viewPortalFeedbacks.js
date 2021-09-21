define(
		[ "jquery", "dataTables.bootstrap", "froala_tables", "froala_lists",
				"froala_colors", "froala_font_family", "froala_font_size",
				"froala_block_styles", "froala_media_manager", "froala_video",
				"froala_inline_styles", "froala_fullscreen",
				"froala_char_counter", "froala_entities", "froala_file_upload",
				"froala_urls" ],
		function($) {
			$(function() {
				$('#editor')
						.editable(
								{
									inlineMode : false,
									buttons : [ 'bold', 'italic', 'underline',
											'sep', 'strikeThrough',
											'subscript', 'superscript', 'sep',
											'fontFamily', 'fontSize', 'color',
											'formatBlock', 'blockStyle',
											'inlineStyle', 'sep', 'align',
											'insertOrderedList',
											'insertUnorderedList', 'outdent',
											'indent', 'selectAll', 'sep',
											'createLink', 'table', 'sep',
											'undo', 'redo', 'sep',
											'insertHorizontalRule',
											'removeFormat', 'fullscreen' ],
									minHeight : 200,
									paragraphy : false,
									placeholder : 'Enter Assignment details OR instructions here',
									theme : 'blue',
									key : 'vA-16ddvvzalxvB-13C2uF-10A-8mG-7eC5lnmhuD3mmD-16==',
									toolbarFixed : false
								});
				/* Formatting function for row details - modify as you need */
				function format(d) {
					// `d` is the original data object for the row
					return d;
				}
				var table = $('.table')
						.DataTable(
								{
									"dom" : '<"top"i>rt<"bottom"flp><"clear">',
									"lengthMenu" : [ [ 10, 25, 50, -1 ],
											[ 10, 25, 50, "All" ] ],
									'responsive' : true,

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
																	|| headerText == "Expand/Collapse")
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

				$('#example tbody')
						.on(
								'click',
								'td.details-control',
								function() {
									var id = $(this).attr('id');
									//alert(id);
									var ctx = window.location.pathname
											.substring(0,
													window.location.pathname
															.indexOf("/", 2));
									//alert(ctx)
									var tr = $(this).closest('tr');
									var row = table.row(tr);

									if (row.child.isShown()) {
										// This row is already open - close it
										row.child.hide();
										tr.removeClass('shown');
									} else {
										// Open this row
										if (id) {
											$
													.ajax({
														type : 'GET',
														url : ctx
																+ '/getDiscussionByPortalFeedbackId?'
																+ 'id=' + id,
														success : function(data) {
															var json = JSON
																	.parse(data);
															console.log(json)
															var optionsAsString = "";

															row
																	.child(
																			format(json))
																	.show();
															tr
																	.addClass('shown');

														}
													});
										} else {
											alert('Error!');
										}

									}
								});

				/*
				 * $('#example tbody').on('click', 'td.details-control',
				 * function() { var list = '#' + $(this).attr('id');
				 * alert(list); $(list).show();
				 * 
				 * });
				 */

				// Handle click on "Expand All" button
				$('#btn-show-all-children').on('click', function() {
					// Enumerate all rows
					table.rows().every(function() {
						// If row has details collapsed
						if (!this.child.isShown()) {
							// Open this row
							this.child(format(this.data())).show();
							$(this.node()).addClass('shown');
						}
					});
				});

				// Handle click on "Collapse All" button
				$('#btn-hide-all-children').on('click', function() {
					// Enumerate all rows
					table.rows().every(function() {
						// If row has details expanded
						if (this.child.isShown()) {
							// Collapse row details
							this.child.hide();
							$(this.node()).removeClass('shown');
						}
					});
				});
			});
		});