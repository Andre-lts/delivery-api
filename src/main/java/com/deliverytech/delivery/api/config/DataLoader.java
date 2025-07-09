package com.deliverytech.delivery.api.config;

import com.deliverytech.delivery.api.entity.*;
import com.deliverytech.delivery.api.entity.StatusPedido;
import com.deliverytech.delivery.api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== INICIANDO CARGA DE DADOS DE TESTE ===");

        // Limpar dados existentes
        pedidoRepository.deleteAll();
        produtoRepository.deleteAll();
        restauranteRepository.deleteAll();
        clienteRepository.deleteAll();

        // Inserir dados de teste
        inserirClientes();
        inserirRestaurantes();
        inserirProdutos();
        inserirPedidos();

        // Executar testes das consultas
        testarConsultas();

        System.out.println("=== CARGA DE DADOS CONCLUÍDA ===");
    }
    // Método inserirClientes
private void inserirClientes() {
    System.out.println("--- Inserindo Clientes ---");

    Cliente cliente1 = new Cliente();
    cliente1.setNome("João Silva");
    cliente1.setEmail("joao@email.com");
    cliente1.setTelefone("11999999999");
    cliente1.setEndereco("Rua A, 123");
    cliente1.setAtivo(true);

    Cliente cliente2 = new Cliente();
    cliente2.setNome("Maria Santos");
    cliente2.setEmail("maria@email.com");
    cliente2.setTelefone("11888888888");
    cliente2.setEndereco("Rua B, 456");
    cliente2.setAtivo(true);

    Cliente cliente3 = new Cliente();
    cliente3.setNome("Pedro Oliveira");
    cliente3.setEmail("pedro@email.com");
    cliente3.setTelefone("11777777777");
    cliente3.setEndereco("Rua C, 789");
    cliente3.setAtivo(false);

    clienteRepository.saveAll(Arrays.asList(cliente1, cliente2, cliente3));
    System.out.println("✓ 3 clientes inseridos");
}
// Método inserirRestaurantes
private void inserirRestaurantes() {
    System.out.println("--- Inserindo Restaurantes ---");

    Restaurante restaurante1 = new Restaurante();
    restaurante1.setNome("Pizza Express");
    restaurante1.setCategoria("Italiana");
    restaurante1.setEndereco("Av. Principal, 100");
    restaurante1.setTelefone("1133333333");
    restaurante1.setTaxaEntrega(new BigDecimal("3.50"));
    restaurante1.setAtivo(true);

    Restaurante restaurante2 = new Restaurante();
    restaurante2.setNome("Burger King");
    restaurante2.setCategoria("Fast Food");
    restaurante2.setEndereco("Rua Central, 200");
    restaurante2.setTelefone("1144444444");
    restaurante2.setTaxaEntrega(new BigDecimal("5.00"));
    restaurante2.setAtivo(true);

    restauranteRepository.saveAll(Arrays.asList(restaurante1, restaurante2));
    System.out.println("✓ 2 restaurantes inseridos");
}
// Método testarConsultas
private void testarConsultas() {
    System.out.println("\n=== TESTANDO CONSULTAS DOS REPOSITORIES ===");

    // Teste ClienteRepository
    System.out.println("\n--- Testes ClienteRepository ---");

    var clientePorEmail = clienteRepository.findByEmail("joao@email.com");
    System.out.println("Cliente por email: " +
        (clientePorEmail.isPresent() ? clientePorEmail.get().getNome() : "Não encontrado"));

    var clientesAtivos = clienteRepository.findByAtivoTrue();
    System.out.println("Clientes ativos: " + clientesAtivos.size());

    var clientesPorNome = clienteRepository.findByNomeContainingIgnoreCase("silva");
    System.out.println("Clientes com 'silva' no nome: " + clientesPorNome.size());

    boolean emailExiste = clienteRepository.existsByEmail("maria@email.com");
    System.out.println("Email maria@email.com existe: " + emailExiste);

    // ... continuar com outros testes
    }
}



