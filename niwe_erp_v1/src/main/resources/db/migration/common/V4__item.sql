CREATE INDEX idx_ws_item_id ON inventory_warehouse_stock (item_id);
CREATE INDEX idx_item_deleted ON core_item (deleted);
INSERT INTO inventory_warehouse_stock (
    id,
	version,
    item_id,
    warehouse_id,
    quantity,
    received_date
)
SELECT
    gen_random_uuid(),
	0,
    i.id,
    w.id,
    0,
    NOW()
FROM core_item i
CROSS JOIN INVENTORY_WAREHOUSE w
LEFT JOIN inventory_warehouse_stock ws
       ON ws.item_id = i.id
      AND ws.warehouse_id = w.id
WHERE ws.id IS NULL
  AND i.deleted = false;
