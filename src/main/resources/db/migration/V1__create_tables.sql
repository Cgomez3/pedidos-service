-- =====================================
-- TABLA: clientes
-- =====================================
CREATE TABLE clientes (
    id VARCHAR(50) PRIMARY KEY,
    activo BOOLEAN NOT NULL
);

-- =====================================
-- TABLA: zonas
-- =====================================
CREATE TABLE zonas (
    id VARCHAR(50) PRIMARY KEY,
    soporte_refrigeracion BOOLEAN NOT NULL
);

-- =====================================
-- TABLA: cargas_idempotencia
-- =====================================
CREATE TABLE cargas_idempotencia (
    id UUID PRIMARY KEY,
    idempotency_key VARCHAR(100) NOT NULL,
    archivo_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_cargas_idempotencia
        UNIQUE (idempotency_key, archivo_hash)
);

-- =====================================
-- TABLA: pedidos
-- =====================================
CREATE TABLE pedidos (
    id UUID PRIMARY KEY,

    numero_pedido VARCHAR(50) NOT NULL,
    cliente_id VARCHAR(50) NOT NULL,
    zona_id VARCHAR(50) NOT NULL,

    fecha_entrega DATE NOT NULL,

    estado VARCHAR(20) NOT NULL,
    requiere_refrigeracion BOOLEAN NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,

    CONSTRAINT uk_pedidos_numero
        UNIQUE (numero_pedido),

    CONSTRAINT chk_estado_pedido
        CHECK (estado IN ('PENDIENTE','CONFIRMADO','ENTREGADO'))
);

