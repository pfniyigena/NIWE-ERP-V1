$(document).ready(function() {
	let labelUpdateSale = document.getElementById("lblUpdateSale").textContent;
	let labelUpdateCost = document.getElementById("lblUpdateCost").textContent;
	let labelInflow = document.getElementById("lblInflow").textContent;
	let labelView = document.getElementById("lblView").textContent;
	$('#outflowsTable').DataTable({
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
			{ data: 'locationCode' },
			{
				data: "unitPrice",
				render: function(data, type, row) {
					return `<a href="#" data-toggle="modal" 
						                               data-target="#updatePriceModal" 
						                               data-id="${row.id}" 
						                               data-type="sale" 
						                               data-value="${row.unitPrice}"
						                               class="btn btn-dark open-update-modal"
													  title="${labelUpdateSale}">
						                                <span>${row.unitPrice}</span>
						                            </a>`;
				}
			},
			{
				data: "unitCost",
				render: function(data, type, row) {
					return `<a href="#" data-toggle="modal" 
						                               data-target="#updatePriceModal" 
						                               data-id="${row.itemId}" 
						                               data-type="cost" 
						                               data-value="${row.unitCost}"
						                               class="btn btn-primary open-update-modal"
													   title="${labelUpdateCost}">
						                                <span>${row.unitCost}</span>
						                            </a>`;
				}
			},
			{
				data: "quantity",
				render: function(data, type, row) {
					return `<a href="#" data-toggle="modal" 
						                               data-target="#inflowModal" 
						                               data-id="${row.itemId}" 
						                               data-name="${row.itemName}"
													   data-quantity="${row.quantity}"
													   data-warehouse="${row.warehouseId}"
						                               class="btn btn-success open-update-modal"
													   title="${labelInflow}">
						                                <span>${row.quantity}</span>
						                            </a>`;
				}
			},
			{
				data: 'itemId',
				orderable: false,
				render: function(data, type, row) {
					return `<div class="input-group-append">
							<a href="${BASE_URL}outflows/view/${row.itemId}/${row.warehouseId}/${row.locationId}" class="btn btn-warning" title="${labelView}"><i class="fa fa-eye"></i></a>
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
});