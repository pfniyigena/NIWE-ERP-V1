$(document).ready(function() {
    let labelInflow = document.getElementById("lblInflow").textContent;
    let labelView = document.getElementById("lblView").textContent;
    let labelAdjustStock = document.getElementById("lblAdjustStock").textContent;
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
            { data: 'unitCost' },
            { data: 'unitPrice' },
            { data: 'quantity' },
            {
                data: 'itemId',
                orderable: false,
                render: function(data, type, row) {
                    return `<div class="input-group-append">
					        <a href="#" data-toggle="modal" data-target="#inflowModal" data-id="${row.itemId}" data-name="${row.itemName}"
							data-quantity="${row.quantity}" data-price="${row.unitPrice}" data-barcode="${row.barcode}" data-purchase="${row.unitCost}" data-warehouse="${row.warehouseId}" class="btn btn-danger open-update-modal" title="${labelInflow}">
							<i class="fas fa-calendar-plus"></i></a>	
							<a href="${BASE_URL}inflows/view/${row.itemId}/${row.warehouseId}" class="btn btn-success" title="${labelView}"><i class="fa fa-eye"></i></a>
							<a href="#" data-toggle="modal" data-target="#adjustStockModal" data-id="${row.itemId}" data-name="${row.itemName}"
													data-quantity="${row.quantity}" data-price="${row.unitPrice}" data-barcode="${row.barcode}" data-purchase="${row.unitCost}" data-warehouse="${row.warehouseId}" class="btn btn-warning open-update-modal" title="${labelAdjustStock}">
													<i class="fas fa-adjust"></i></a>
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
        const modal = $('#inflowModal');
        if (modal.length > 0) {
            modal.find('#itemId').val($(this).data('id'));
            modal.find('#warehouseId').val($(this).data('warehouse'));
            modal.find('#itemName').val($(this).data('name'));
            modal.find('#oldValue').val($(this).data('quantity'));
            modal.find('#newUnitPrice').val($(this).data('price'));
            modal.find('#newUnitCost').val($(this).data('purchase'));
            modal.find('#newBarcode').val($(this).data('barcode'));
        }
        const adjustModal = $('#adjustStockModal');
        if (adjustModal.length > 0) {
            adjustModal.find('#itemId').val($(this).data('id'));
            adjustModal.find('#warehouseId').val($(this).data('warehouse'));
            adjustModal.find('#itemName').val($(this).data('name'));
            adjustModal.find('#oldValue').val($(this).data('quantity'));
        }

    });
});