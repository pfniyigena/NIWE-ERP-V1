CREATE INDEX idx_stock_movement_filter
ON stock_movement (movement_date, movement_type);

CREATE INDEX idx_CORE_ERROR_LOG_created_at ON CORE_ERROR_LOG(created_at);
