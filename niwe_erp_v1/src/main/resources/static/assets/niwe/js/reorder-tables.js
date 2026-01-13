$(document).ready(function() {
    $('#reordersTable').DataTable({
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
            { data: 'quantity' },
            {
              data: "stockLevel",
                render: function(data, type, row) {
                    return `<span>${row.stockLevel}</span>`;
                }
            },
        ],
        language: {
            processing: "Loading...",
            emptyTable: "No products found"
        }
    });
});