$(document).ready(function() {
    var table = $('#warehouseStockReport').DataTable({
        processing: true,
        serverSide: true,
        ajax: {
            url: `${BASE_URL}warehouse-stocks/ajax/view/low`,
            type: 'POST',
            contentType: 'application/json',
            data: function(d) {
                d.categoryId = $('#categoryId').val();
                d.brandId = $('#brandId').val();
                return JSON.stringify(d);
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
            { data: 'stockLevel' },
            { data: 'categoryName' },
            { data: 'brandName' },
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
                        `${BASE_URL}warehouse-stocks/report/low/export/excel?categoryId=${$('#categoryId').val()}&brandId=${$('#brandId').val()}`;
                }
            },
            {
                text: 'PDF',
                className: 'btn btn-danger',
                action: function() {
                    window.location.href =
                        `${BASE_URL}warehouse-stocks/report/low/export/pdf?categoryId=${$('#categoryId').val()}&brandId=${$('#brandId').val()}`;
                }
            }
        ]

    });
    table.buttons().container()
        .appendTo('#warehouseStockReport .col-md-6:eq(0)');
    $('#filterBtn').on('click', function() {
        table.ajax.reload();
    });
    $('#categoryId, #brandId').on('change', function() {
        table.ajax.reload();
    });
});