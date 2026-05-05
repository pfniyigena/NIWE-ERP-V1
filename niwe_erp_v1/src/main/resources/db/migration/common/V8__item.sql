CREATE UNIQUE INDEX idx_unique_item_name_clean ON core_item (LOWER(TRIM(item_name)));
ALTER TABLE core_item ADD CONSTRAINT unique_item_name UNIQUE (item_name);
CREATE UNIQUE INDEX unique_item_name_active ON core_item(item_name) WHERE deleted = false;