-- INDICES
-- =====================================

CREATE INDEX IF NOT EXISTS idx_pedidos_estado_fecha
ON pedidos (estado, fecha_entrega);