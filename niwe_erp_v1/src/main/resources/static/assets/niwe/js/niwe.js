jQuery(document).ready(function($) {
	'use strict';
	setTimeout(function() {
		$(".alert").alert('close');
	}, 5000); // 5 seconds
	$('#keep-order').multiSelect({
		keepOrder: true
	});
	var table = $('.datatable').DataTable({
		destroy: true,
	        dom: 'Bfrtip',
	        buttons: ['copy']
	    });
		
		table.buttons().container()
		    .appendTo($('.dataTables_wrapper .col-md-6:eq(0)'));
});

