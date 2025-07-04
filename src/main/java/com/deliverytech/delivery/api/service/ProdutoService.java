package com.deliverytech.delivery.api.service;

import com.deliverytech.delivery.en ty.Produto; 
import com.deliverytech.delivery.en ty.Restaurante; 
import com.deliverytech.delivery.repository.ProdutoRepository; 
import com.deliverytech.delivery.repository.RestauranteRepository; 
import org.springframework.beans.factory.annota on.Autowired; 
import org.springframework.stereotype.Service; 
import org.springframework.transac on.annota on.Transac onal; 
 
import java.math.BigDecimal; 
import java.u l.List; 
import java.u l.Op onal; 
 
@Service 
@Transac onal 
public class ProdutoService { 
 
    @Autowired 
    private ProdutoRepository produtoRepository; 
 
    @Autowired 
    private RestauranteRepository restauranteRepository; 
 
    /** 
     * Cadastrar novo produto 
     */ 
    public Produto cadastrar(Produto produto, Long restauranteId) { 
        Restaurante restaurante = restauranteRepository.findById(restauranteId) 
            .orElseThrow(() -> new IllegalArgumentExcep on("Restaurante não encontrado: " + 
restauranteId)); 
 
        validarDadosProduto(produto); 
 
        produto.setRestaurante(restaurante); 
        produto.setDisponivel(true); 
 
        return produtoRepository.save(produto); 
    } 
 
    /** 
     * Buscar por ID 
     */ 
    @Transac onal(readOnly = true) 
    public Op onal<Produto> buscarPorId(Long id) { 
        return produtoRepository.findById(id); 
    } 
 
    /** 
     * Listar produtos por restaurante 
     */ 
    @Transac onal(readOnly = true) 
    public List<Produto> listarPorRestaurante(Long restauranteId) { 
        return produtoRepository.findByRestauranteIdAndDisponivelTrue(restauranteId); 
    } 
 
    /** 
     * Buscar por categoria 
     */ 
    @Transac onal(readOnly = true) 
    public List<Produto> buscarPorCategoria(String categoria) { 
        return produtoRepository.findByCategoriaAndDisponivelTrue(categoria); 
    } 
 
    /** 
     * Atualizar produto 
     */ 
    public Produto atualizar(Long id, Produto produtoAtualizado) { 
        Produto produto = buscarPorId(id) 
            .orElseThrow(() -> new IllegalArgumentExcep on("Produto não encontrado: " + id)); 
 
        validarDadosProduto(produtoAtualizado); 
 
        produto.setNome(produtoAtualizado.getNome()); 
        produto.setDescricao(produtoAtualizado.getDescricao()); 
        produto.setPreco(produtoAtualizado.getPreco()); 
        produto.setCategoria(produtoAtualizado.getCategoria()); 
 
        return produtoRepository.save(produto); 
    } 
 
    /** 
     * Alterar disponibilidade 
     */ 
    public void alterarDisponibilidade(Long id, boolean disponivel) { 
        Produto produto = buscarPorId(id) 
            .orElseThrow(() -> new IllegalArgumentExcep on("Produto não encontrado: " + id)); 
 
        produto.setDisponivel(disponivel); 
        produtoRepository.save(produto); 
    } 
 
    /** 
     * Buscar por faixa de preço 
     */ 
    @Transac onal(readOnly = true) 
    public List<Produto> buscarPorFaixaPreco(BigDecimal precoMin, BigDecimal precoMax) { 
        return produtoRepository.findByPrecoBetweenAndDisponivelTrue(precoMin, precoMax); 
    } 
 
    private void validarDadosProduto(Produto produto) { 
        if (produto.getNome() == null || produto.getNome().trim().isEmpty()) { 
            throw new IllegalArgumentExcep on("Nome é obrigatório"); 
        } 
 
        if (produto.getPreco() == null || produto.getPreco().compareTo(BigDecimal.ZERO) <= 0) { 
            throw new IllegalArgumentExcep on("Preço deve ser maior que zero"); 
        } 
    } 
} 