jQuery(document).ready(function($) {
	'use strict';
	setTimeout(function() {
		$(".alert").alert('close');
	}, 1000); // 1 seconds
	$('#keep-order').multiSelect({
		keepOrder: true
	});
	$('.datatable').each(function() {
		const pageLength = $(this).data('page-length') || 10;
		const order = $(this).data('order') || [];

		$(this).DataTable({
			pageLength: pageLength,
			order: order,
			lengthMenu: [10, 25, 50, 100]
		});
	});

});

