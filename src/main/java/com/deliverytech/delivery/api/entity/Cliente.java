package com.deliverytech.delivery.api.entity;

import jakarta.persistence.*; 
import lombok.Data; 
import java.util.List; 
 
@Entity 
@Data 
public class Cliente { 
    @Id 
    @GeneratedValue(strategy = Genera onType.IDENTITY) 
    private Long id; 
    private String nome; 
    private String email; 
    private String telefone; 
    private String endereco; 
    private boolean ativo; 
 
    @OneToMany(mappedBy = "cliente") 
    private List<Pedido> pedidos; 
} 
