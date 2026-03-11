package com.prueba.pedidos.pedidos_service.aplicacion.service.parser;
import com.prueba.pedidos.pedidos_service.aplication.model.EstadoPedido;
import com.prueba.pedidos.pedidos_service.aplication.model.Pedido;
import com.prueba.pedidos.pedidos_service.shared.error.ErrorCode;
import com.prueba.pedidos.pedidos_service.shared.exception.BusinessException;
import org.apache.commons.csv.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CsvPedidoParser {
    public List<Pedido> parse(MultipartFile file) {
        List<Pedido> pedidos = new ArrayList<>();

        try {

            Reader in = new InputStreamReader(file.getInputStream());

            CSVParser parser = CSVFormat.DEFAULT
                    .withDelimiter(',')
                    .withFirstRecordAsHeader()
                    .withTrim()
                    .parse(in);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            for (CSVRecord record : parser) {

                Pedido pedido = new Pedido();

                pedido.setNumeroPedido(record.get("numeroPedido"));
                pedido.setClienteId(record.get("clienteId"));
                pedido.setFechaEntrega(LocalDate.parse(record.get("fechaEntrega"), formatter));
                pedido.setEstado(EstadoPedido.valueOf(record.get("estado")));
                pedido.setZonaId(record.get("zonaEntrega"));
                pedido.setRequiereRefrigeracion(Boolean.parseBoolean(record.get("requiereRefrigeracion")));

                pedidos.add(pedido);
            }

        } catch (Exception e) {
            throw new BusinessException(ErrorCode.CSV_INVALIDO);
        }

        return pedidos;
    }
}
