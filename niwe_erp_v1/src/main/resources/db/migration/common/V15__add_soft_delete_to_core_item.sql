ALTER TABLE core_item ADD COLUMN deleted BOOLEAN NOT NULL DEFAULT FALSE;
SELECT barcode, COUNT(*) FROM core_item WHERE deleted = FALSE GROUP BY barcode HAVING COUNT(*) > 1;
CREATE UNIQUE INDEX IF NOT EXISTS uk_core_item_barcode_active ON core_item (barcode) WHERE deleted = false;