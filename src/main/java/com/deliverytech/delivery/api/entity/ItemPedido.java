package com.deliverytech.delivery.api.entity;

import jakarta.persistence.*; 
import lombok.Data; 
 
import java.math.BigDecimal; 
 
@Entity 
@Data 
public class ItemPedido { 
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id; 
    private Integer quantidade; 
    private BigDecimal precoUnitario; 
    private BigDecimal precoTotal; 
 
    @ManyToOne 
    @JoinColumn(name = "pedido_id") 
    private Pedido pedido; 
 
    @ManyToOne 
    @JoinColumn(name = "produto_id") 
    private Produto produto; 

    public void calcularSubtotal() {
        if (quantidade != null && precoUnitario != null) {
            this.precoTotal = precoUnitario.multiply(BigDecimal.valueOf(quantidade));
        } else {
            this.precoTotal = BigDecimal.ZERO;
        }
    }
} 
