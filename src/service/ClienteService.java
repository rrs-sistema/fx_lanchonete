package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jdbc.ConnectionFactory;
import model.ClienteModel;

public class ClienteService {

	public static void insert(ClienteModel cliente) {
	    String sql = "INSERT INTO cliente (nome, telefone, email, cidade, bairro, endereco) VALUES (?, ?, ?, ?, ?, ?)";
	    try (Connection conn = ConnectionFactory.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setString(1, cliente.getNome());
	        stmt.setString(2, cliente.getTelefone());
	        stmt.setString(3, cliente.getEmail());
	        stmt.setString(4, cliente.getCidade());
	        stmt.setString(5, cliente.getBairro());
	        stmt.setString(6, cliente.getEndereco());
	        stmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public static void update(ClienteModel cliente) {
	    String sql = "UPDATE cliente SET nome = ?, telefone = ?, email = ?, cidade = ?, bairro = ?, endereco = ? WHERE id = ?";
	    try (Connection conn = ConnectionFactory.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setString(1, cliente.getNome());
	        stmt.setString(2, cliente.getTelefone());
	        stmt.setString(3, cliente.getEmail());
	        stmt.setString(4, cliente.getCidade());
	        stmt.setString(5, cliente.getBairro());
	        stmt.setString(6, cliente.getEndereco());
	        stmt.setLong(7, cliente.getId());
	        stmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public static void delete(long clienteId) {
	    String sql = "DELETE FROM cliente WHERE id = ?";
	    try (Connection conn = ConnectionFactory.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setLong(1, clienteId);
	        stmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}	
	
	public static ObservableList<ClienteModel> listarClientes() {
	    ObservableList<ClienteModel> clientes = FXCollections.observableArrayList();
	    String sql = "SELECT * FROM cliente";
	    try (Connection conn = ConnectionFactory.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {
	        while (rs.next()) {
	            ClienteModel cliente = new ClienteModel(
	                rs.getLong("id"),
	                rs.getString("nome"),
	                rs.getString("email"),
	                rs.getString("telefone"),
	                rs.getString("endereco"),
	                rs.getString("bairro"),
	                rs.getString("cidade")
	            );
	            clientes.add(cliente);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return clientes;
	}	
}
