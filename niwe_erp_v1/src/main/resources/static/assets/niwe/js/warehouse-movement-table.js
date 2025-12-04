$(document).ready(function() {
	let itemId = $("#itemId").val();
	let warehouseId = $("#warehouseId").val();
	$('#stockMovementTable').DataTable({
		processing: true,
		serverSide: true,
		ajax: {
			url: `${BASE_URL}inflows/ajax/view/${itemId}/${warehouseId}`,
			type: 'POST',
			contentType: 'application/json',
			data: function(d) {
				console.log(JSON.stringify(d));
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
			{
				data: 'movementDate',
				render: function(data) {
					if (!data) return "";
					let d = new Date(data);
					return d.toLocaleString(); // "2025-11-29 10:20 AM"
				}
			},
			{ data: 'prevWarehouseQuantity' },
			{ data: 'movedQuantity' },
			{ data: 'currentWarehouseQuantity' },
			{
				data: 'movementType',
				render: function(data, type, row) {
					if (!data) return "";
					// optional: map to human-friendly labels or badges
					const labels = {
						IN: '<span class="badge bg-success">IN</span>',
						OUT: '<span class="badge bg-danger">OUT</span>',
						TRANSFER: '<span class="badge bg-primary">TRANSFER</span>'
					};
					return labels[data] || data;
				}
			},
			{ data: 'prevLocationQuantity' },
			{ data: 'currentLocationQuantity' }

		],
		language: {
			processing: "Loading...",
			emptyTable: "No products found"
		}
	});
});