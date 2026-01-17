$(document).ready(function() {
    var table = $('#stockMovementReport').DataTable({
        processing: true,
        serverSide: true,
        ajax: {
            url: `${BASE_URL}stock-movements/ajax/view`,
            type: 'POST',
            contentType: 'application/json',
            data: function(d) {
                d.fromDate = $('#startDate').val(); // yyyy-MM-dd
                d.toDate = $('#endDate').val();
				d.movementType = $('#movementType').val();
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
            {
                data: 'movementDate',
                render: function(data) {
                    if (!data) return "";
                    let d = new Date(data);
                    return d.toLocaleString(); // "2025-11-29 10:20 AM"
                }
            },
            {
                data: 'movementType',
                render: function(data, type, row) {
                    if (!data) return "";
                    // optional: map to human-friendly labels or badges
                    const labels = {
                        STOCK_INITIAL: '<span class="badge bg-success text-white">STOCK INITIAL</span>',
                        GOOD_RECEIVED_NOTE: '<span class="badge bg-success text-white">GOOD RECEIVED NOTE</span>',
                        SALE: '<span class="badge bg-danger text-white">SALE</span>',
                        TRANSFER: '<span class="badge bg-primary text-white">TRANSFER</span>'
                    };
                    return labels[data] || data;
                }
            },
            { data: 'itemName' },
            { data: 'movedQuantity' },
            { data: 'managerName' },


        ],
        language: {
            processing: "Loading...",
            emptyTable: "No products found"
        },

        dom: 'Bfrtip',
        buttons: [
            {
                text: 'Excel',
                className: 'btn btn-success',
                action: function() {
                    window.location.href =
                        `${BASE_URL}stock-movements/export/excel`;
                }
            },
            {
                text: 'PDF',
                className: 'btn btn-danger',
                action: function() {
                    window.location.href =
                        `${BASE_URL}stock-movements/export/pdf`;
                }
            }
        ]


    });
    table.buttons().container()
        .appendTo('#stockMovementReport_wrapper .col-md-6:eq(0)');
    $('#filterBtn').on('click', function() {
        table.ajax.reload();
    });
	$('#fromDate, #toDate, #movementType').on('change', function () {
	    table.ajax.reload();
	});

});