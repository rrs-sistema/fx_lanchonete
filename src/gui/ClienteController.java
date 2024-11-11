package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import model.ClienteModel;
import service.ClienteService;

public class ClienteController {

    @FXML
    private Button createButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;
    
    @FXML
    private Button camcelarEdicaoButton;    
    
    @FXML
    private TextField clientNameField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField cidadeField;

    @FXML
    private TextField bairroField;

    @FXML
    private TextField enderecoField;

    @FXML
    private TableView<ClienteModel> clientTable;

    @FXML
    private TableColumn<ClienteModel, Integer> idColumn;

    @FXML
    private TableColumn<ClienteModel, String> nameColumn;

    @FXML
    private TableColumn<ClienteModel, Integer> phoneColumn;

    @FXML
    private TableColumn<ClienteModel, String> emailColumn;

    @FXML
    private TableColumn<ClienteModel, String> addressColumn;

    @FXML
    private TableColumn<ClienteModel, String> bairroColumn;

    @FXML
    private TableColumn<ClienteModel, String> cidadeColumn;
    
    private ObservableList<ClienteModel> clientes = FXCollections.observableArrayList();
    private ClienteModel clienteSelecionado;

    @SuppressWarnings("deprecation")
	@FXML
    private void initialize() {
        // Configuração das colunas
    	idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    	idColumn.setVisible(false);
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
	    phoneColumn.setCellValueFactory(new PropertyValueFactory<>("telefone"));
	    emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
	    addressColumn.setCellValueFactory(new PropertyValueFactory<>("endereco"));
	    bairroColumn.setCellValueFactory(new PropertyValueFactory<>("bairro"));
	    cidadeColumn.setCellValueFactory(new PropertyValueFactory<>("cidade"));
	    
	    // Definir larguras e centralizar o conteúdo das colunas
	    nameColumn.setMinWidth(200);
	    nameColumn.setMaxWidth(250);
	    phoneColumn.setMinWidth(100);
	    phoneColumn.setMaxWidth(125);
	    emailColumn.setMinWidth(220);
	    emailColumn.setMaxWidth(260);
	    
	    nameColumn.setStyle("-fx-alignment: CENTER;");
	    phoneColumn.setStyle("-fx-alignment: CENTER;");
	    emailColumn.setStyle("-fx-alignment: CENTER;");
	    addressColumn.setStyle("-fx-alignment: CENTER;");
	    bairroColumn.setStyle("-fx-alignment: CENTER;");
	    cidadeColumn.setStyle("-fx-alignment: CENTER;");
	    
	    clientTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	    
        clientTable.setItems(clientes);

        // Desabilitar inicialmente os botões Atualizar e Deletar
        camcelarEdicaoButton.setDisable(true);
        updateButton.setDisable(true);
        deleteButton.setDisable(true);

        // Listener para desabilitar e habilitar botões com base na seleção
        clientTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            clienteSelecionado = newSelection;
            if (clienteSelecionado != null) {
                preencherCamposComClienteSelecionado(clienteSelecionado);
                createButton.setDisable(true);
                updateButton.setDisable(false); // Habilitar botão Atualizar
                camcelarEdicaoButton.setDisable(false); 
                deleteButton.setDisable(false); // Habilitar botão Deletar
            } else {
                updateButton.setDisable(true);  // Desabilitar botão Atualizar
                camcelarEdicaoButton.setDisable(true); 
                deleteButton.setDisable(true);  // Desabilitar botão Deletar
                createButton.setDisable(false);
            }
        });
        
        carregarClientes();
    }

    private void carregarClientes() {
        ObservableList<ClienteModel> clientes = FXCollections.observableArrayList();
		try {
			clientes = ClienteService.listarClientes();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!clientes.isEmpty())
			clientTable.setItems(clientes);
    }
    
    @FXML
    private void handleCreate() {
    	if (clientNameField == null) {
			showAlert("Nenhum nome informado", "Por favor, informe o nome do cliente.");
			return;
		}
		if (phoneNumberField == null) {
			showAlert("Nenhum telefone informado", "Por favor, informe o telefone do cliente.");
			return;
		}
	   	
        String name = clientNameField.getText();
        String phone = phoneNumberField.getText();
        String email = emailField.getText().isEmpty() ? "" : emailField.getText();
        String address = enderecoField.getText().isEmpty() ? "" : enderecoField.getText();
        String cidade = cidadeField.getText().isEmpty() ? "" : cidadeField.getText();
        String bairro = bairroField.getText().isEmpty() ? "" : bairroField.getText();

        ClienteModel novoCliente = new ClienteModel(name, email, phone, address, bairro, cidade);
        ClienteService.insert(novoCliente);
        
        carregarClientes();

        limparCampos();
    }

    @FXML
    private void handleUpdate() {
    	if (clientNameField == null) {
			showAlert("Nenhum nome informado", "Por favor, informe o nome do cliente.");
			return;
		}
		if (phoneNumberField == null) {
			showAlert("Nenhum telefone informado", "Por favor, informe o telefone do cliente.");
			return;
		}
	  	
        if (clienteSelecionado != null) {
            // Atualizar os dados do cliente selecionado com os valores dos campos de texto
        	Long id = clienteSelecionado.getId(); // Obtenha o ID do item selecionado
        	clienteSelecionado.setId(id);
            clienteSelecionado.setNome(clientNameField.getText());
            clienteSelecionado.setTelefone(phoneNumberField.getText());
            clienteSelecionado.setEmail(emailField.getText());
            clienteSelecionado.setEndereco(enderecoField.getText());
            clienteSelecionado.setCidade(cidadeField.getText());
            clienteSelecionado.setBairro(bairroField.getText());
            ClienteService.update(clienteSelecionado);
            
            // Atualizar a tabela para refletir as mudanças
            clientTable.refresh();

            // Limpar campos de entrada e desmarcar a seleção na tabela
            limparCampos();
            clientTable.getSelectionModel().clearSelection();  // Remove a seleção na tabela

            // Reativar botões Criar e Buscar, e desativar Atualizar e Deletar
            createButton.setDisable(false);
            updateButton.setDisable(true); // Desabilitar o botão Atualizar
            camcelarEdicaoButton.setDisable(true); // Desabilitar o botão Atualizar
            deleteButton.setDisable(true); // Desabilitar o botão Deletar
        } else {
            showAlert("Nenhum cliente selecionado", "Por favor, selecione um cliente para atualizar.");
        }
    }

    @FXML
    private void handleCamcelarEdicao() {
        // Atualizar a tabela para refletir as mudanças
        clientTable.refresh();

        // Limpar campos de entrada e desmarcar a seleção na tabela
        limparCampos();
        clientTable.getSelectionModel().clearSelection();  // Remove a seleção na tabela
        clienteSelecionado = null;  // Resetar o cliente selecionado

        // Reativar botões Criar e Buscar, e desativar Atualizar e Deletar
        createButton.setDisable(false);
        updateButton.setDisable(true); // Desabilitar o botão Atualizar
        deleteButton.setDisable(true); // Desabilitar o botão Deletar
        camcelarEdicaoButton.setDisable(true); // Desabilitar o botão Deletar
    }
    
    @FXML
    private void handleDelete() {
        if (clienteSelecionado != null) {
            clientes.remove(clienteSelecionado);
            limparCampos();
            clienteSelecionado = null;
            deleteButton.setDisable(true);
            updateButton.setDisable(true);

            // Reativar botões Criar e Buscar após a exclusão
            createButton.setDisable(false);
        } else {
            showAlert("Nenhum cliente selecionado", "Por favor, selecione um cliente para deletar.");
        }
    }

    private void preencherCamposComClienteSelecionado(ClienteModel cliente) {
        clientNameField.setText(cliente.getNome());
        phoneNumberField.setText(cliente.getTelefone());
        emailField.setText(cliente.getEmail());
        enderecoField.setText(cliente.getEndereco());
        cidadeField.setText(cliente.getCidade());
        bairroField.setText(cliente.getBairro());
    }

    private void limparCampos() {
        clientNameField.clear();
        phoneNumberField.clear();
        emailField.clear();
        enderecoField.clear();
        cidadeField.clear();
        bairroField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
