INSERT INTO CORE_ROLE(version,name, description) VALUES (0,'ROLE_ADMIN','Administrator'),(0,'ROLE_USER','Default user');
INSERT INTO CORE_PERMISSION(version,name, description) VALUES
(0,'DASHBOARD_VIEW','View dashboard'),
(0,'PRODUCT_VIEW','View products'),
(0,'INVOICE_MANAGE','Manage invoices'),
(0,'USER_MANAGE','Manage users');

INSERT INTO CORE_ROLE_PERMISSION(role_id, permission_id)
SELECT r.id, p.id FROM CORE_ROLE r, CORE_PERMISSION p WHERE r.name='ROLE_ADMIN';