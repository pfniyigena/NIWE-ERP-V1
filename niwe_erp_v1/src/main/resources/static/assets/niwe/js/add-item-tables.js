$(document).ready(function() {
	let labelInflow = document.getElementById("lblInflow").textContent;
	let labelView = document.getElementById("lblView").textContent;
	let lblDelete = document.getElementById("lblDelete").textContent;
	$('#addItemTable').DataTable({
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
			{
				data: "unitCost",
				render: function(data, type, row) {
					return `<a href="#" data-toggle="modal" 
						                               data-target="#updatePriceModal" 
						                               data-id="${row.itemId}" 
						                               data-type="cost" 
						                               data-value="${row.unitCost}"
						                               class="text-success open-update-modal">
						                                <span>${row.unitCost}</span>
						                            </a>`;
				}
			},
			{
				data: "unitPrice",
				render: function(data, type, row) {
					return `<a href="#" data-toggle="modal" 
						                               data-target="#updatePriceModal" 
						                               data-id="${row.itemId}" 
						                               data-type="sale" 
						                               data-value="${row.unitPrice}"
						                               class="text-success open-update-modal">
						                                <span>${row.unitPrice}</span>
						                            </a>`;
				}
			},
			{ data: 'quantity' },
			{
				data: 'itemId',
				orderable: false,
				render: function(data, type, row) {
					return `<div class="input-group-append">	
							<a href="${BASE_URL}items/update/${data}" class="btn btn-primary"><i class="fas fa-pencil-alt"></i></a>
							<a href="${BASE_URL}items/view/${row.itemId}" class="btn btn-success" title="${labelView}"><i class="fa fa-eye"></i></a>
							<a href="#" data-toggle="modal" data-target="#deleteItemModal" data-id="${row.itemId}" data-name="${row.itemName}" class="btn btn-danger open-update-modal" title="${lblDelete}"><i class="fa fa-trash"></i></a>
							</div>`;
				}
			}
		],
		language: {
			processing: "Loading...",
			emptyTable: "No items found"
		}
	});

	$(document).on('click', '.open-update-modal', function() {
		const modal = $('#deleteItemModal');
		if (modal != null) {
			modal.find('#itemId').val($(this).data('id'));
			modal.find('#itemName').val($(this).data('name'));
		}
		const modal2 = $('#updatePriceModal');
		if (modal2 != null) {
			modal2.find('#itemId').val($(this).data('id'));
			modal2.find('#updateType').val($(this).data('type'));
			modal2.find('#newValue').val($(this).data('value'));
		}

	});
});