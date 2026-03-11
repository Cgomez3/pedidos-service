ALTER TABLE pedidos
ADD CONSTRAINT fk_pedidos_cliente
FOREIGN KEY (cliente_id) REFERENCES clientes(id);

ALTER TABLE pedidos
ADD CONSTRAINT fk_pedidos_zona
FOREIGN KEY (zona_id) REFERENCES zonas(id);