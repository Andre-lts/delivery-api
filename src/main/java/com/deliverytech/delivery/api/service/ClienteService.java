package com.deliverytech.delivery.api.service;

import com.deliverytech.delivery.en ty.Cliente; 
import com.deliverytech.delivery.repository.ClienteRepository; 
import org.springframework.beans.factory.annota on.Autowired; 
import org.springframework.stereotype.Service; 
import org.springframework.transac on.annota on.Transac onal; 
 
import java.u l.List; 
import java.u l.Op onal; 
 
@Service 
@Transac onal 
public class ClienteService { 
 
    @Autowired 
    private ClienteRepository clienteRepository; 
 
    /** 
     * Cadastrar novo cliente 
     */ 
    public Cliente cadastrar(Cliente cliente) { 
        // Validar email único 
        if (clienteRepository.existsByEmail(cliente.getEmail())) { 
            throw new IllegalArgumentExcep on("Email já cadastrado: " + cliente.getEmail()); 
        } 
 
        // Validações de negócio 
        validarDadosCliente(cliente); 
 
        // Definir como a vo por padrão 
        cliente.setA vo(true); 
 
        return clienteRepository.save(cliente); 
    } 
 
    /** 
     * Buscar cliente por ID 
     */ 
    @Transac onal(readOnly = true) 
    public Op onal<Cliente> buscarPorId(Long id) { 
        return clienteRepository.findById(id); 
    } 
 
    /** 
     * Buscar cliente por email 
     */ 
    @Transac onal(readOnly = true) 
    public Op onal<Cliente> buscarPorEmail(String email) { 
        return clienteRepository.findByEmail(email); 
    } 
 
    /** 
     * Listar todos os clientes a vos 
     */ 
    @Transac onal(readOnly = true) 
    public List<Cliente> listarA vos() { 
        return clienteRepository.findByA voTrue(); 
    } 
 
    /** 
     * Atualizar dados do cliente 
     */ 
    public Cliente atualizar(Long id, Cliente clienteAtualizado) { 
        Cliente cliente = buscarPorId(id) 
            .orElseThrow(() -> new IllegalArgumentExcep on("Cliente não encontrado: " + id)); 
 
        // Verificar se email não está sendo usado por outro cliente 
        if (!cliente.getEmail().equals(clienteAtualizado.getEmail()) && 
            clienteRepository.existsByEmail(clienteAtualizado.getEmail())) { 
            throw new IllegalArgumentExcep on("Email já cadastrado: " + 
clienteAtualizado.getEmail()); 
        } 
 
        // Atualizar campos 
        cliente.setNome(clienteAtualizado.getNome()); 
        cliente.setEmail(clienteAtualizado.getEmail()); 
        cliente.setTelefone(clienteAtualizado.getTelefone()); 
        cliente.setEndereco(clienteAtualizado.getEndereco()); 
 
        return clienteRepository.save(cliente); 
    } 
 
    /** 
     * Ina var cliente (so delete) 
     */ 
    public void ina var(Long id) { 
        Cliente cliente = buscarPorId(id) 
            .orElseThrow(() -> new IllegalArgumentExcep on("Cliente não encontrado: " + id)); 
 
        cliente.ina var(); 
        clienteRepository.save(cliente); 
    } 
 
    /** 
     * Buscar clientes por nome 
     */ 
    @Transac onal(readOnly = true) 
    public List<Cliente> buscarPorNome(String nome) { 
        return clienteRepository.findByNomeContainingIgnoreCase(nome); 
    } 
 
    /** 
     * Validações de negócio 
     */ 
    private void validarDadosCliente(Cliente cliente) { 
        if (cliente.getNome() == null || cliente.getNome().trim().isEmpty()) { 
            throw new IllegalArgumentExcep on("Nome é obrigatório"); 
        } 
 
        if (cliente.getEmail() == null || cliente.getEmail().trim().isEmpty()) { 
            throw new IllegalArgumentExcep on("Email é obrigatório"); 
        } 
 
        if (cliente.getNome().length() < 2) { 
            throw new IllegalArgumentExcep on("Nome deve ter pelo menos 2 caracteres"); 
        } 
    } 
} 
