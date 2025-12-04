$(document).ready(function() {
	let labelOutflow = document.getElementById("lblOutflow").textContent;
	let labelView = document.getElementById("lblView").textContent;
	$('#inflowsTable').DataTable({
		processing: true,
		serverSide: true,
		ajax: {
			url: INFLOWS_DATA_URL,
			type: 'POST',
			contentType: 'application/json',
			data: function(d) {
				return JSON.stringify(d);  // DataTables.net expects JSON
			}
		},
		pageLength: 10,
		lengthMenu: [10, 25, 50, 100, 500],
		columns: [
			{
				data: null,               // no data needed
				orderable: false,
				render: function(data, type, row, meta) {
					return meta.row + 1 + meta.settings._iDisplayStart;  // auto row number
				}
			},
			{ data: 'itemName' },
			{ data: 'itemCode' },
			{ data: 'barcode' },
			{ data: 'unitPrice' },
			{ data: 'unitCost' },
			{ data: 'quantity' },
			{
				data: 'itemId',
				orderable: false,
				render: function(data, type, row) {
					return `<div class="input-group-append">
							<a href="#" data-toggle="modal" data-target="#outflowModal" data-id="${row.itemId}" data-name="${row.itemName}"
														data-quantity="${row.quantity}" data-warehouse="${row.warehouseId}" class="btn btn-warning open-update-modal" title="${labelOutflow}">
														<i class="fas fa-calendar-minus"></i></a>
							
							<a href="${BASE_URL}inflows/view/${row.itemId}/${row.warehouseId}" class="btn btn-success" title="${labelView}"><i class="fa fa-eye"></i></a>
							</div>`;
				}
			}
		],
		language: {
			processing: "Loading...",
			emptyTable: "No products found"
		}
	});
	// Modal opener for dynamically rendered links
	$(document).on('click', '.open-update-modal', function() {
		const modal = $('#updatePriceModal');
		modal.find('#itemId').val($(this).data('id'));
		modal.find('#updateType').val($(this).data('type'));
		modal.find('#newValue').val($(this).data('value'));
	});

	// Modal opener for dynamically rendered links
	$(document).on('click', '.open-update-modal', function() {
		const modal = $('#inflowModal');
		modal.find('#itemId').val($(this).data('id'));
		modal.find('#warehouseId').val($(this).data('warehouse'));
		modal.find('#itemName').val($(this).data('name'));
		modal.find('#oldValue').val($(this).data('quantity'));
	});
	// Modal opener for dynamically rendered links
	$(document).on('click', '.open-update-modal', function() {
		const modal = $('#outflowModal');
		modal.find('#itemId').val($(this).data('id'));
		modal.find('#warehouseId').val($(this).data('warehouse'));
		modal.find('#itemName').val($(this).data('name'));
		modal.find('#oldValue').val($(this).data('quantity'));
	});
});