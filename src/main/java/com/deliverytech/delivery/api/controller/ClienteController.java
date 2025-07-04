package com.deliverytech.delivery.api.controller;

import com.deliverytech.delivery.en ty.Cliente; 
import com.deliverytech.delivery.service.ClienteService; 
import org.springframework.beans.factory.annota on.Autowired; 
import org.springframework.h p.H pStatus; 
import org.springframework.h p.ResponseEn ty; 
import org.springframework.web.bind.annota on.*; 
 
import jakarta.valida on.Valid; 
import java.u l.List; 
import java.u l.Op onal; 
 
@RestController 
@RequestMapping("/clientes") 
@CrossOrigin(origins = "*") 
public class ClienteController { 
 
    @Autowired 
    private ClienteService clienteService; 
 
    /** 
     * Cadastrar novo cliente 
     */ 
    @PostMapping 
    public ResponseEn ty<?> cadastrar(@Valid @RequestBody Cliente cliente) { 
        try { 
            Cliente clienteSalvo = clienteService.cadastrar(cliente); 
            return ResponseEn ty.status(H pStatus.CREATED).body(clienteSalvo); 
        } catch (IllegalArgumentExcep on e) { 
            return ResponseEn ty.badRequest().body("Erro: " + e.getMessage()); 
        } catch (Excep on e) { 
            return ResponseEn ty.status(H pStatus.INTERNAL_SERVER_ERROR) 
                .body("Erro interno do servidor"); 
        } 
    } 
 
    /** 
     * Listar todos os clientes a vos 
     */ 
    @GetMapping 
    public ResponseEn ty<List<Cliente>> listar() { 
        List<Cliente> clientes = clienteService.listarA vos(); 
        return ResponseEn ty.ok(clientes); 
    } 
 
    /** 
     * Buscar cliente por ID 
     */ 
    @GetMapping("/{id}") 
    public ResponseEn ty<?> buscarPorId(@PathVariable Long id) { 
        Op onal<Cliente> cliente = clienteService.buscarPorId(id); 
 
        if (cliente.isPresent()) { 
            return ResponseEn ty.ok(cliente.get()); 
        } else { 
            return ResponseEn ty.notFound().build(); 
        } 
    } 
 
    /** 
     * Atualizar cliente 
     */ 
    @PutMapping("/{id}") 
    public ResponseEn ty<?> atualizar(@PathVariable Long id, 
                                      @Valid @RequestBody Cliente cliente) { 
        try { 
            Cliente clienteAtualizado = clienteService.atualizar(id, cliente); 
            return ResponseEn ty.ok(clienteAtualizado); 
        } catch (IllegalArgumentExcep on e) { 
            return ResponseEn ty.badRequest().body("Erro: " + e.getMessage()); 
        } catch (Excep on e) { 
            return ResponseEn ty.status(H pStatus.INTERNAL_SERVER_ERROR) 
                .body("Erro interno do servidor"); 
        } 
    } 
 
    /** 
     * Ina var cliente (so delete) 
     */ 
    @DeleteMapping("/{id}") 
    public ResponseEn ty<?> ina var(@PathVariable Long id) { 
        try { 
            clienteService.ina var(id); 
            return ResponseEn ty.ok().body("Cliente ina vado com sucesso"); 
        } catch (IllegalArgumentExcep on e) { 
            return ResponseEn ty.badRequest().body("Erro: " + e.getMessage()); 
        } catch (Excep on e) { 
            return ResponseEn ty.status(H pStatus.INTERNAL_SERVER_ERROR) 
                .body("Erro interno do servidor"); 
        } 
    } 
 
    /** 
     * Buscar clientes por nome 
     */ 
    @GetMapping("/buscar") 
    public ResponseEn ty<List<Cliente>> buscarPorNome(@RequestParam String nome) { 
        List<Cliente> clientes = clienteService.buscarPorNome(nome); 
        return ResponseEn ty.ok(clientes); 
    } 
 
    /** 
     * Buscar cliente por email 
     */ 
    @GetMapping("/email/{email}") 
    public ResponseEn ty<?> buscarPorEmail(@PathVariable String email) { 
        Op onal<Cliente> cliente = clienteService.buscarPorEmail(email); 
 
        if (cliente.isPresent()) { 
            return ResponseEn ty.ok(cliente.get()); 
        } else { 
            return ResponseEn ty.notFound().build(); 
        } 
    } 
} 
