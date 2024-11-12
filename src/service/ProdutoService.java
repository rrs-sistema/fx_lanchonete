package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jdbc.ConnectionFactory;
import model.CategoriaModel;
import model.ProdutoModel;

public class ProdutoService {

	public static void insert(ProdutoModel produto) {
	    String sql = "INSERT INTO produto (nome, descricao, categoria_id, preco, custo, estoque) VALUES (?, ?, ?, ?, ?, ?)";
	    try (Connection conn = ConnectionFactory.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setString(1, produto.getNome());
	        stmt.setString(2, produto.getDescricao());
	        stmt.setLong(3, produto.getCategoria().getId());
	        stmt.setDouble(4, produto.getPreco());
	        stmt.setDouble(5, produto.getCusto());
	        stmt.setDouble(6, produto.getEstoque());
	        stmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public static void update(ProdutoModel produto) {
	    String sql = "UPDATE produto SET nome = ?, descricao = ?, categoria_id = ?, preco = ? , custo = ? , estoque = ? WHERE id = ?";
	    try (Connection conn = ConnectionFactory.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setString(1, produto.getNome());
	        stmt.setString(2, produto.getDescricao());
	        stmt.setLong(3, produto.getCategoria().getId());
	        stmt.setDouble(4, produto.getPreco());
	        stmt.setDouble(5, produto.getCusto());
	        stmt.setDouble(6, produto.getEstoque());
	        stmt.setLong(7, produto.getId());
	        stmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public static void updateEstoque(Long produtoId, double totalSaida) {
	    String sql = "UPDATE produto SET estoque = estoque - ? WHERE id = ?";
	    try (Connection conn = ConnectionFactory.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setDouble(1, totalSaida);
	        stmt.setLong(2, produtoId);
	        stmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public static void delete(long produtoId) {
	    String sql = "DELETE FROM produto WHERE id = ?";
	    try (Connection conn = ConnectionFactory.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setLong(1, produtoId);
	        stmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}	
	
	public static ObservableList<ProdutoModel> listarProdutos() throws Exception {
        String sql = "SELECT p.id, p.nome, p.descricao, p.estoque, p.preco, p.custo, c.id as categoria_id, c.nome as categoria_nome " +
                     "FROM produto p " +
                     "JOIN categoria c ON p.categoria_id = c.id";

        ObservableList<ProdutoModel> produtos = FXCollections.observableArrayList();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                CategoriaModel categoria = new CategoriaModel(
                        rs.getLong("categoria_id"),
                        rs.getString("categoria_nome")
                );

                ProdutoModel produto = new ProdutoModel(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getString("descricao"),
                        categoria,
                        rs.getDouble("preco"),
                        rs.getDouble("custo"),
                        rs.getDouble("estoque")
                );

                produtos.add(produto);
            }
        }
        return produtos;
    }
}