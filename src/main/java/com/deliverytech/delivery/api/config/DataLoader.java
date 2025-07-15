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

// Método inserirProdutos
private void inserirProdutos() {
    System.out.println("--- Inserindo Produtos ---");

    Restaurante restaurante = restauranteRepository.findAll().get(0); // ou buscar por nome/ID específico

    Produto produto1 = new Produto();
    produto1.setNome("Pizza Margherita");
    produto1.setDescricao("Pizza com molho de tomate e queijo");
    produto1.setPreco(new BigDecimal("25.00"));
    produto1.setRestaurante(restaurante);
    produto1.setDisponivel(true);

    Produto produto2 = new Produto();
    produto2.setNome("Hambúrguer Duplo");
    produto2.setDescricao("Dois hambúrgueres de carne com queijo");
    produto2.setPreco(new BigDecimal("18.00"));
    produto2.setRestaurante(restaurante);
    produto2.setDisponivel(true);

    produtoRepository.saveAll(Arrays.asList(produto1, produto2));
    System.out.println("✓ 2 produtos inseridos");
}

// Método inserirPedidos
private void inserirPedidos() {
    System.out.println("--- Inserindo Pedidos ---");

    Cliente cliente = clienteRepository.findAll().get(0);
    Restaurante restaurante = restauranteRepository.findAll().get(0);
    Produto produto = produtoRepository.findAll().get(0);

    Pedido pedido = new Pedido();
    pedido.setCliente(cliente);
    pedido.setRestaurante(restaurante);
    pedido.setDataPedido(LocalDateTime.now());
    pedido.setEnderecoEntrega("Rua de Entrega, 123");
    pedido.setStatus(StatusPedido.CONFIRMADO);

    BigDecimal taxaEntrega = restaurante.getTaxaEntrega();
    BigDecimal subtotal = produto.getPreco(); // simplificando: só 1 produto

    pedido.setTaxaEntrega(taxaEntrega);
    pedido.setSubtotal(subtotal);
    pedido.setValorTotal(subtotal.add(taxaEntrega));

    // Criar item do pedido
    ItemPedido item = new ItemPedido();
    item.setPedido(pedido);            // importante: associar o pedido
    item.setProduto(produto);
    item.setQuantidade(1);
    item.setPrecoUnitario(produto.getPreco());
    item.setPrecoTotal(produto.getPreco()); // ou quantidade * preço

    pedido.setItens(List.of(item)); // associa os itens ao pedido

    pedidoRepository.save(pedido);
    System.out.println("✓ 1 pedido inserido com 1 item");
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



