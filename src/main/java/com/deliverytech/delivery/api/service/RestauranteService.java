package com.deliverytech.delivery.api.service;

import com.deliverytech.delivery.en ty.Restaurante; 
import com.deliverytech.delivery.repository.RestauranteRepository; 
import org.springframework.beans.factory.annota on.Autowired; 
import org.springframework.stereotype.Service; 
import org.springframework.transac on.annota on.Transac onal; 
 
import java.math.BigDecimal; 
import java.u l.List; 
import java.u l.Op onal; 
 
@Service 
@Transac onal 
public class RestauranteService { 
 
    @Autowired 
    private RestauranteRepository restauranteRepository; 
 
    /** 
     * Cadastrar novo restaurante 
     */ 
    public Restaurante cadastrar(Restaurante restaurante) { 
        // Validar nome único 
        if (restauranteRepository.findByNome(restaurante.getNome()).isPresent()) { 
            throw new IllegalArgumentExcep on("Restaurante já cadastrado: " + 
restaurante.getNome()); 
        } 
 
        validarDadosRestaurante(restaurante); 
        restaurante.setA vo(true); 
 
        return restauranteRepository.save(restaurante); 
    } 
 
    /** 
     * Buscar por ID 
     */ 
    @Transac onal(readOnly = true) 
    public Op onal<Restaurante> buscarPorId(Long id) { 
        return restauranteRepository.findById(id); 
    } 
 
    /** 
     * Listar restaurantes a vos 
     */ 
    @Transac onal(readOnly = true) 
    public List<Restaurante> listarA vos() { 
        return restauranteRepository.findByA voTrue(); 
    } 
 
    /** 
     * Buscar por categoria 
     */ 
    @Transac onal(readOnly = true) 
    public List<Restaurante> buscarPorCategoria(String categoria) { 
        return restauranteRepository.findByCategoriaAndA voTrue(categoria); 
    } 
 
    /** 
     * Atualizar restaurante 
     */ 
    public Restaurante atualizar(Long id, Restaurante restauranteAtualizado) { 
        Restaurante restaurante = buscarPorId(id) 
            .orElseThrow(() -> new IllegalArgumentExcep on("Restaurante não encontrado: " + id)); 
 
        // Verificar nome único (se mudou) 
        if (!restaurante.getNome().equals(restauranteAtualizado.getNome()) && 
            restauranteRepository.findByNome(restauranteAtualizado.getNome()).isPresent()) { 
            throw new IllegalArgumentExcep on("Nome já cadastrado: " + 
restauranteAtualizado.getNome()); 
        } 
 
        restaurante.setNome(restauranteAtualizado.getNome()); 
        restaurante.setCategoria(restauranteAtualizado.getCategoria()); 
        restaurante.setEndereco(restauranteAtualizado.getEndereco()); 
        restaurante.setTelefone(restauranteAtualizado.getTelefone()); 
        restaurante.setTaxaEntrega(restauranteAtualizado.getTaxaEntrega()); 
 
        return restauranteRepository.save(restaurante); 
    } 
 
    /** 
     * Ina var restaurante 
     */ 
    public void ina var(Long id) { 
        Restaurante restaurante = buscarPorId(id) 
            .orElseThrow(() -> new IllegalArgumentExcep on("Restaurante não encontrado: " + id)); 
 
        restaurante.setA vo(false); 
        restauranteRepository.save(restaurante); 
    } 
 
    private void validarDadosRestaurante(Restaurante restaurante) { 
        if (restaurante.getNome() == null || restaurante.getNome().trim().isEmpty()) { 
            throw new IllegalArgumentExcep on("Nome é obrigatório"); 
        } 
 
        if (restaurante.getTaxaEntrega() != null && 
            restaurante.getTaxaEntrega().compareTo(BigDecimal.ZERO) < 0) { 
            throw new IllegalArgumentExcep on("Taxa de entrega não pode ser nega va"); 
        } 
    } 
}