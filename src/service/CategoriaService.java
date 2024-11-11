package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jdbc.ConnectionFactory;
import model.CategoriaModel;

public class CategoriaService {

	public static void insert(CategoriaModel categoria) {
	    String sql = "INSERT INTO categoria (nome) VALUES (?)";
	    try (Connection conn = ConnectionFactory.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setString(1, categoria.getNome());
	        stmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public static void update(CategoriaModel categoria) {
	    String sql = "UPDATE categoria SET nome = ? WHERE id = ?";
	    try (Connection conn = ConnectionFactory.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setString(1, categoria.getNome());
	        stmt.setLong(2, categoria.getId());
	        stmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public static void delete(long categoriaId) {
	    String sql = "DELETE FROM categoria WHERE id = ?";
	    try (Connection conn = ConnectionFactory.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setLong(1, categoriaId);
	        stmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}	
	
	public static ObservableList<CategoriaModel> listarCategorias() {
	    ObservableList<CategoriaModel> categorias = FXCollections.observableArrayList();
	    String sql = "SELECT * FROM categoria";
	    try (Connection conn = ConnectionFactory.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {
	        while (rs.next()) {
	        	CategoriaModel categoria = new CategoriaModel(
	                rs.getLong("id"),
	                rs.getString("nome")
	            );
	        	categorias.add(categoria);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return categorias;
	}	
}
