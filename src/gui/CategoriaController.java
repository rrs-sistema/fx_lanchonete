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
import model.CategoriaModel;
import service.CategoriaService;

public class CategoriaController {

    @FXML
    private Button createButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;
    
    @FXML
    private Button camcelarEdicaoButton;    
    
    @FXML
    private TextField categoriaNameField;

    @FXML
    private TableView<CategoriaModel> categoriaTable;

    @FXML
    private TableColumn<CategoriaModel, Integer> idColumn;

    @FXML
    private TableColumn<CategoriaModel, String> nameColumn;

    // Variável para verificar o estado de edição
    private boolean isEditing = false;
    
    private ObservableList<CategoriaModel> categorias = FXCollections.observableArrayList();
    private CategoriaModel categoriaselecionado;

	@FXML
    private void initialize() {
        // Configuração das colunas
	    // Configurar as colunas
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
	    
	    categoriaTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
	    
        categoriaTable.setItems(categorias);

        // Desabilitar inicialmente os botões Atualizar e Deletar
        camcelarEdicaoButton.setDisable(true);
        updateButton.setDisable(true);
        deleteButton.setDisable(true);

        // Listener para desabilitar e habilitar botões com base na seleção
        categoriaTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            categoriaselecionado = newSelection;
            if (categoriaselecionado != null) {
                preencherCamposComcategoriaselecionado(categoriaselecionado);
                createButton.setDisable(true);
                updateButton.setDisable(false); // Habilitar botão Atualizar
                camcelarEdicaoButton.setDisable(false); 
                deleteButton.setDisable(false); // Habilitar botão Deletar
                isEditing = true;
                categoriaTable.setDisable(true); // Desabilita a tabela ao entrar no modo de edição
            } else {
                updateButton.setDisable(true);  // Desabilitar botão Atualizar
                camcelarEdicaoButton.setDisable(true); 
                deleteButton.setDisable(true);  // Desabilitar botão Deletar
                createButton.setDisable(false);
                isEditing = false;
                categoriaTable.setDisable(false); // Reabilita a tabela ao finalizar edição                   
            }
        });
        
        configureTable();
        carregarCategorias();
    }

    private void carregarCategorias() {
        ObservableList<CategoriaModel> categorias = FXCollections.observableArrayList();
		try {
			categorias = CategoriaService.listarCategorias();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!categorias.isEmpty())
			categoriaTable.setItems(categorias);
    }
    
    private void configureTable() {
        // Configurações das colunas e tabela, se necessário
    	categoriaTable.setDisable(isEditing); // Inicializa com o estado de edição
    }
    
    @FXML
    private void handleCreate() {
    	if (categoriaNameField == null) {
			showAlert("Nenhum nome informado", "Por favor, informe o nome do categoria.");
			return;
		}
        String name = categoriaNameField.getText();

        CategoriaModel novoCategoria = new CategoriaModel(name);
        CategoriaService.insert(novoCategoria);
        carregarCategorias();
        
        limparCampos();
    }

    @FXML
    private void handleUpdate() {
        if (categoriaselecionado != null) {
            // Atualizar os dados do categoria selecionado com os valores dos campos de texto
        	Long id = categoriaselecionado.getId(); // Obtenha o ID do item selecionado
        	categoriaselecionado.setId(id);
            categoriaselecionado.setNome(categoriaNameField.getText());
            CategoriaService.update(categoriaselecionado);

            // Atualizar a tabela para refletir as mudanças
            categoriaTable.refresh();

            // Limpar campos de entrada e desmarcar a seleção na tabela
            limparCampos();
            //categoriaTable.getSelectionModel().clearSelection();  // Remove a seleção na tabela
            categoriaselecionado = null;  // Resetar o categoria selecionado

            // Reativar botões Criar e Buscar, e desativar Atualizar e Deletar
            createButton.setDisable(false);
            updateButton.setDisable(true); // Desabilitar o botão Atualizar
            camcelarEdicaoButton.setDisable(true); // Desabilitar o botão Atualizar
            deleteButton.setDisable(true); // Desabilitar o botão Deletar
        } else {
            showAlert("Nenhum categoria selecionado", "Por favor, selecione um categoria para atualizar.");
        }
    }

    @FXML
    private void handleCamcelarEdicao() {
        isEditing = false;
        categoriaTable.setDisable(false); // Reabilita a tabela se a edição for cancelada    	
        // Atualizar a tabela para refletir as mudanças
        categoriaTable.refresh();

        // Limpar campos de entrada e desmarcar a seleção na tabela
        limparCampos();
        categoriaTable.getSelectionModel().clearSelection();  // Remove a seleção na tabela
        categoriaselecionado = null;  // Resetar o categoria selecionado

        // Reativar botões Criar e Buscar, e desativar Atualizar e Deletar
        createButton.setDisable(false);
        updateButton.setDisable(true); // Desabilitar o botão Atualizar
        deleteButton.setDisable(true); // Desabilitar o botão Deletar
        camcelarEdicaoButton.setDisable(true); // Desabilitar o botão Deletar
    }
    
    @FXML
    private void handleDelete() {
        if (categoriaselecionado != null) {
            categorias.remove(categoriaselecionado);
            limparCampos();
            categoriaselecionado = null;
            deleteButton.setDisable(true);
            updateButton.setDisable(true);

            // Reativar botões Criar e Buscar após a exclusão
            createButton.setDisable(false);
        } else {
            showAlert("Nenhum categoria selecionado", "Por favor, selecione um categoria para deletar.");
        }
    }

    private void preencherCamposComcategoriaselecionado(CategoriaModel categoria) {
        categoriaNameField.setText(categoria.getNome());
    }

    private void limparCampos() {
        categoriaNameField.clear();
        isEditing = false;
        categoriaTable.setDisable(false);         
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
