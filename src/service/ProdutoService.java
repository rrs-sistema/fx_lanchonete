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
	    String sql = "INSERT INTO produto (nome, descricao, categoria_id, preco) VALUES (?, ?, ?, ?)";
	    try (Connection conn = ConnectionFactory.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setString(1, produto.getNome());
	        stmt.setString(2, produto.getDescricao());
	        stmt.setLong(3, produto.getCategoria().getId());
	        stmt.setDouble(4, produto.getPreco());
	        stmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public static void update(ProdutoModel produto) {
	    String sql = "UPDATE produto SET nome = ?, descricao = ?, categoria_id = ?, preco = ? WHERE id = ?";
	    try (Connection conn = ConnectionFactory.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setString(1, produto.getNome());
	        stmt.setString(2, produto.getDescricao());
	        stmt.setLong(3, produto.getCategoria().getId());
	        stmt.setDouble(4, produto.getPreco());
	        stmt.setLong(5, produto.getId());
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
        String sql = "SELECT p.id, p.nome, p.descricao, p.preco, c.id as categoria_id, c.nome as categoria_nome " +
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
                        rs.getDouble("preco")
                );

                produtos.add(produto);
            }
        }
        return produtos;
    }
}