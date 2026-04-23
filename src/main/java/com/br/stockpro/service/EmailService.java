package com.br.stockpro.service;

import com.br.stockpro.enums.AlertType;
import com.br.stockpro.model.Company;
import com.br.stockpro.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;


    public void sendStockAlert(Company company, Product product,
                               AlertType type, Integer quantity, Integer minQuantity) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(company.getEmail());
            message.setSubject(buildSubject(type, product.getName()));
            message.setText(buildBody(type, product, quantity, minQuantity));
            mailSender.send(message);

            log.info("Email de alerta enviado para {} — produto: {}",
                    company.getEmail(), product.getName());

        } catch (Exception e) {
            // nunca deixa o email derrubar o fluxo principal
            log.error("Falha ao enviar email de alerta para {}: {}",
                    company.getEmail(), e.getMessage());
        }
    }

    private String buildSubject(AlertType type, String productName) {
        return switch (type) {
            case LOW_STOCK -> "[StockPro] Estoque baixo: " + productName;
            case OUT_OF_STOCK -> "[StockPro] Produto zerado: " + productName;
            case LONG_OUT_OF_STOCK -> "[StockPro] Produto zerado há vários dias: " + productName;
            case ABOVE_MAXIMUM -> "[StockPro] Estoque acima do máximo: " + productName;
        };
    }

    private String buildBody(AlertType type, Product product,
                             Integer quantity, Integer minQuantity) {
        return switch (type) {
            case LOW_STOCK -> """
                    Alerta de estoque baixo
                    
                    Produto: %s
                    Código de barras: %s
                    Quantidade atual: %d
                    Quantidade mínima: %d
                    
                    Recomendamos realizar uma compra em breve.
                    """.formatted(product.getName(), product.getBarcode(),
                    quantity, minQuantity);

            case OUT_OF_STOCK -> """
                    Produto zerado
                    
                    Produto: %s
                    Código de barras: %s
                    
                    O estoque deste produto chegou a zero.
                    Realize uma compra o quanto antes.
                    """.formatted(product.getName(), product.getBarcode());

            case LONG_OUT_OF_STOCK -> """
                    Produto zerado há vários dias
                    
                    Produto: %s
                    Código de barras: %s
                    
                    Este produto está sem estoque há 7 dias ou mais.
                    Verifique se ainda faz parte do seu catálogo.
                    """.formatted(product.getName(), product.getBarcode());
            case ABOVE_MAXIMUM -> """
                    Estoque acima do máximo definido
                    
                    Produto: %s
                    Código de barras: %s
                    Quantidade atual: %d
                    Quantidade máxima: %d
                    
                    Verifique se há excesso de compras para este produto.
                    """.formatted(product.getName(), product.getBarcode(), quantity, minQuantity);
        };
    }
}
