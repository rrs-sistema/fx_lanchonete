package service;

import model.CarrinhoItemModel;
import model.CarrinhoModel;
import model.ClienteModel;
import model.ProdutoModel;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jdbc.ConnectionFactory;

public class CarrinhoService {

    public static boolean salvarVenda(CarrinhoModel carrinho) {
        String carrinhoSql = "INSERT INTO carrinho (cliente_id, data, valor_total, custo_total, tipo_pagamento) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = ConnectionFactory.getConnection()) {
            conn.setAutoCommit(false); // Inicia a transação

            // Insere o carrinho
            try (PreparedStatement carrinhoStmt = conn.prepareStatement(carrinhoSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                carrinhoStmt.setLong(1, carrinho.getCliente().getId());
                carrinhoStmt.setDate(2, java.sql.Date.valueOf(carrinho.getData()));
                carrinhoStmt.setDouble(3, carrinho.getValorTotal());
                carrinhoStmt.setDouble(4, carrinho.getCustoTotal());
                carrinhoStmt.setString(5, carrinho.getTipoPagamento());
                carrinhoStmt.executeUpdate();

                try (var generatedKeys = carrinhoStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        carrinho.setId(generatedKeys.getLong(1));
                    }
                }
            }

            String itemSql = "INSERT INTO carrinho_item (carrinho_id, produto_id, quantidade, preco_unitario) VALUES (?, ?, ?, ?)";
            // Insere os itens do carrinho
            try (PreparedStatement itemStmt = conn.prepareStatement(itemSql)) {
                for (CarrinhoItemModel item : carrinho.getItens()) {
                    itemStmt.setLong(1, carrinho.getId());
                    itemStmt.setLong(2, item.getProduto().getId());
                    itemStmt.setInt(3, item.getQuantidade());
                    itemStmt.setDouble(4, item.getPrecoUnitario());
                    itemStmt.addBatch();
                }
                itemStmt.executeBatch();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
	public static ObservableList<CarrinhoModel> listaVenda(LocalDate dataInicial, LocalDate dataFinal) {
	    ObservableList<CarrinhoModel> carrinhos = FXCollections.observableArrayList();

	    String sql = "SELECT c.id AS carrinho_id, c.tipo_pagamento AS tipo_pagamento, cl.id AS cliente_id, cl.nome AS cliente_nome, "
	            + "c.data AS data, c.custo_total AS custo_total, "
	            + "ci.produto_id AS produto_id, p.nome AS produto_nome, ci.quantidade AS quantidade, "
	            + "ci.preco_unitario AS preco_unitario " 
	            + "FROM carrinho c "
	            + "JOIN cliente cl ON c.cliente_id = cl.id " 
	            + "JOIN carrinho_item ci ON ci.carrinho_id = c.id "
	            + "JOIN produto p ON ci.produto_id = p.id " 
	            + "WHERE c.data BETWEEN ? AND ? "
	            + "ORDER BY c.data DESC, c.id, ci.id";

	    try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setDate(1, Date.valueOf(dataInicial));
	        stmt.setDate(2, Date.valueOf(dataFinal));

	        CarrinhoModel carrinhoAtual = null;
	        List<CarrinhoItemModel> itens = new ArrayList<>();

	        try (ResultSet rs = stmt.executeQuery()) {
	            while (rs.next()) {
	                Long carrinhoId = rs.getLong("carrinho_id");

	                // Se mudamos de carrinho, salva o anterior e começa um novo
	                if (carrinhoAtual == null || !carrinhoId.equals(carrinhoAtual.getId())) {
	                    if (carrinhoAtual != null) {
	                        carrinhoAtual.setItens(itens); // Adiciona itens ao carrinho
	                        carrinhos.add(carrinhoAtual); // Adiciona o carrinho completo
	                    }

	                    // Novo carrinho
	                    ClienteModel cliente = new ClienteModel(rs.getLong("cliente_id"), rs.getString("cliente_nome"));
	                    carrinhoAtual = new CarrinhoModel(carrinhoId, cliente, rs.getDate("data").toLocalDate(),
	                            rs.getDouble("custo_total"), rs.getString("tipo_pagamento"));

	                    itens = new ArrayList<>(); // Nova lista de itens para o novo carrinho
	                }

	                // Cria item do carrinho atual
	                ProdutoModel produto = new ProdutoModel(rs.getLong("produto_id"), rs.getString("produto_nome"),
	                        rs.getDouble("preco_unitario"));

	                CarrinhoItemModel item = new CarrinhoItemModel(produto, rs.getInt("quantidade"),
	                        rs.getDouble("preco_unitario"));
	                itens.add(item); // Adiciona item à lista de itens do carrinho
	            }

	            // Adiciona o último carrinho lido
	            if (carrinhoAtual != null) {
	                carrinhoAtual.setItens(itens); // Define os itens no carrinho
	                carrinhos.add(carrinhoAtual); // Adiciona o último carrinho à lista
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return carrinhos;
	}
	
}
