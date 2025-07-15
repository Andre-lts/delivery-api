package com.deliverytech.delivery.api.entity;

//import com.deliverytech.delivery.api.entity.StatusPedido; 
import jakarta.persistence.*; 
import lombok.Data; 
 
import java.math.BigDecimal; 
import java.time.LocalDateTime; 
import java.util.List; 
 
@Entity 
@Data 
public class Pedido { 
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id; 
    private LocalDateTime dataPedido; 
    private String enderecoEntrega; 
    private BigDecimal subtotal; 
    private BigDecimal taxaEntrega; 
    private BigDecimal valorTotal; 
    private String observacoes;
 
    @Enumerated(EnumType.STRING) 
    private StatusPedido status; 
 
    @ManyToOne 
    @JoinColumn(name = "cliente_id") 
    private Cliente cliente; 
 
    @ManyToOne 
    @JoinColumn(name = "restaurante_id") 
    private Restaurante restaurante; 
 
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL) 
    private List<ItemPedido> itens; 

    public void confirmar() {
        if (this.status != StatusPedido.PENDENTE) {
            throw new IllegalStateException("Pedido não pode ser confirmado, pois não está pendente.");
        }
    
        this.status = StatusPedido.CONFIRMADO;
        this.dataPedido = LocalDateTime.now();
    }
    
    public void adicionarItem(ItemPedido item) {
        if (itens == null) {
            itens = new java.util.ArrayList<>();
        }
        itens.add(item);
        item.setPedido(this); // garante integridade relacional
    }
    
}


