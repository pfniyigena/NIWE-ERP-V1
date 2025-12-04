$(document).ready(function() {
	$('#salesTable').DataTable({
		processing: true,
		serverSide: true,
		ajax: {
			url: SALES_DATA_URL,
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
			{ data: 'externalCode' },
			{ data: 'internalCode' },
			{ data: 'customerName' },
			{ data: 'customerTin' },
			{
				data: 'saleDate',
				render: function(data) {
					if (!data) return "";
					let d = new Date(data);
					return d.toLocaleString(); // "2025-11-29 10:20 AM"
				}
			},
			{ data: 'transactionType' },
			{ data: 'status' },
			{ data: 'totalAmountToPay' },
			{
				data: 'saleId',
				orderable: false,
				render: function(data) {
					return `
					<a href="${BASE_URL}sales/view/${data}" class="btn btn-warning"><i class="fas fa-eye"></i></a>
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