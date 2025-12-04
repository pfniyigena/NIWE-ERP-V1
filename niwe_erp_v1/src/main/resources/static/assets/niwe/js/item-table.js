$(document).ready(function() {
	$('#itemsTable').DataTable({
		processing: true,
		serverSide: true,
		ajax: {
			url: ITEMS_DATA_URL,
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
				data: 'taxId',
				orderable: false,
				render: function(data, type, row) {
					return `<span >${row.taxValue}</span>`;
				}
			},
			{
				data: 'itemId',
				orderable: false,
				render: function(data) {
					return `
					<a href="${BASE_URL}items/update/${data}" class="btn btn-primary"><i class="fas fa-pencil-alt"></i></a>
					<a href="${BASE_URL}items/view/${data}" class="btn btn-warning"><i class="fas fa-eye"></i></a>
					`;
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
});