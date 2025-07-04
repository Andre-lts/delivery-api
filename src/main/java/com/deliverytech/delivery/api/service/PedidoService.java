package com.deliverytech.delivery.api.service;

import com.deliverytech.delivery.en ty.*; 
import com.deliverytech.delivery.repository.*; 
import org.springframework.beans.factory.annota on.Autowired; 
import org.springframework.stereotype.Service; 
import org.springframework.transac on.annota on.Transac onal; 
 
import java.math.BigDecimal; 
import java.u l.List; 
import java.u l.Op onal; 
 
@Service 
@Transac onal 
public class PedidoService { 
 
    @Autowired 
    private PedidoRepository pedidoRepository; 
 
    @Autowired 
    private ClienteRepository clienteRepository; 
 
    @Autowired 
    private RestauranteRepository restauranteRepository; 
 
    @Autowired 
    private ProdutoRepository produtoRepository; 
 
    /** 
     * Criar novo pedido 
     */ 
    public Pedido criarPedido(Long clienteId, Long restauranteId) { 
        Cliente cliente = clienteRepository.findById(clienteId) 
            .orElseThrow(() -> new IllegalArgumentExcep on("Cliente não encontrado: " + 
clienteId)); 
 
        Restaurante restaurante = restauranteRepository.findById(restauranteId) 
            .orElseThrow(() -> new IllegalArgumentExcep on("Restaurante não encontrado: " + 
restauranteId)); 
 
        if (!cliente.isA vo()) { 
            throw new IllegalArgumentExcep on("Cliente ina vo não pode fazer pedidos"); 
        } 
 
        if (!restaurante.getA vo()) { 
            throw new IllegalArgumentExcep on("Restaurante não está disponível"); 
        } 
 
        Pedido pedido = new Pedido(); 
        pedido.setCliente(cliente); 
        pedido.setRestaurante(restaurante); 
        pedido.setStatus(StatusPedido.PENDENTE); 
 
        return pedidoRepository.save(pedido); 
    } 
 
    /** 
     * Adicionar item ao pedido 
     */ 
    public Pedido adicionarItem(Long pedidoId, Long produtoId, Integer quan dade) { 
        Pedido pedido = buscarPorId(pedidoId) 
            .orElseThrow(() -> new IllegalArgumentExcep on("Pedido não encontrado: " + 
pedidoId)); 
 
        Produto produto = produtoRepository.findById(produtoId) 
            .orElseThrow(() -> new IllegalArgumentExcep on("Produto não encontrado: " + 
produtoId)); 
 
        if (!produto.getDisponivel()) { 
            throw new IllegalArgumentExcep on("Produto não disponível: " + produto.getNome()); 
        } 
 
        if (quan dade <= 0) { 
            throw new IllegalArgumentExcep on("Quan dade deve ser maior que zero"); 
        } 
 
        // Verificar se produto pertence ao mesmo restaurante do pedido 
        if (!produto.getRestaurante().getId().equals(pedido.getRestaurante().getId())) { 
            throw new IllegalArgumentExcep on("Produto não pertence ao restaurante do 
pedido"); 
        } 
 
        ItemPedido item = new ItemPedido(); 
        item.setPedido(pedido); 
        item.setProduto(produto); 
        item.setQuan dade(quan dade); 
        item.setPrecoUnitario(produto.getPreco()); 
        item.calcularSubtotal(); 
 
        pedido.adicionarItem(item); 
 
        return pedidoRepository.save(pedido); 
    } 
 
    /** 
     * Confirmar pedido 
     */ 
    public Pedido confirmarPedido(Long pedidoId) { 
        Pedido pedido = buscarPorId(pedidoId) 
            .orElseThrow(() -> new IllegalArgumentExcep on("Pedido não encontrado: " + 
pedidoId)); 
 
        if (pedido.getStatus() != StatusPedido.PENDENTE) { 
            throw new IllegalArgumentExcep on("Apenas pedidos pendentes podem ser 
confirmados"); 
        } 
 
        if (pedido.getItens().isEmpty()) { 
            throw new IllegalArgumentExcep on("Pedido deve ter pelo menos um item"); 
        } 
 
        pedido.confirmar(); 
        return pedidoRepository.save(pedido); 
    } 
 
    /** 
     * Buscar por ID 
     */ 
    @Transac onal(readOnly = true) 
    public Op onal<Pedido> buscarPorId(Long id) { 
        return pedidoRepository.findById(id); 
    } 
 
    /** 
     * Listar pedidos por cliente 
     */ 
    @Transac onal(readOnly = true) 
    public List<Pedido> listarPorCliente(Long clienteId) { 
        return pedidoRepository.findByClienteIdOrderByDataPedidoDesc(clienteId); 
    } 
 
    /** 
     * Buscar por número do pedido 
     */ 
    @Transac onal(readOnly = true) 
    public Op onal<Pedido> buscarPorNumero(String numeroPedido) { 
        return Op onal.ofNullable(pedidoRepository.findByNumeroPedido(numeroPedido)); 
    } 
 
    /** 
     * Cancelar pedido 
     */ 
    public Pedido cancelarPedido(Long pedidoId, String mo vo) { 
        Pedido pedido = buscarPorId(pedidoId) 
            .orElseThrow(() -> new IllegalArgumentExcep on("Pedido não encontrado: " + 
pedidoId)); 
 
        if (pedido.getStatus() == StatusPedido.ENTREGUE) { 
            throw new IllegalArgumentExcep on("Pedido já entregue não pode ser cancelado"); 
        } 
 
        if (pedido.getStatus() == StatusPedido.CANCELADO) { 
            throw new IllegalArgumentExcep on("Pedido já está cancelado"); 
        } 
 
        pedido.setStatus(StatusPedido.CANCELADO); 
        if (mo vo != null && !mo vo.trim().isEmpty()) { 
            pedido.setObservacoes(pedido.getObservacoes() + " | Cancelado: " + mo vo); 
        } 
 
        return pedidoRepository.save(pedido); 
    } 
} 
