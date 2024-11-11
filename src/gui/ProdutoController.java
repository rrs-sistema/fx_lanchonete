package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import model.CategoriaModel;
import model.ProdutoModel;
import service.CategoriaService;
import service.ProdutoService;

public class ProdutoController {

    @FXML
    private Button createButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;
    
    @FXML
    private Button camcelarEdicaoButton;    
    
    @FXML
    private TextField productNameField;
    
    @FXML
    private TextField descriptionField;
    
    @FXML
    private TextField priceField;

    @FXML
    private ComboBox<CategoriaModel> categoryComboBox;
    
    @FXML
    private TableColumn<?, ?> categoryColumn;
    
    @FXML
    private TableView<ProdutoModel> productTable;

    @FXML
    private TableColumn<ProdutoModel, Integer> idColumn;

    @FXML
    private TableColumn<ProdutoModel, String> nameColumn;
    
    @FXML
    private TableColumn<ProdutoModel, String> descriptionColumn;
    
    @FXML
    private TableColumn<ProdutoModel, Double> priceColumn;  
    
    private ObservableList<ProdutoModel> produtos = FXCollections.observableArrayList();
    private ProdutoModel produtoSelecionado;

    @SuppressWarnings("deprecation")
	@FXML
    private void initialize() {
    	idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    	idColumn.setVisible(false);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("preco"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("descricao"));
	    
	    // Definir larguras e centralizar o conteúdo das colunas
        nameColumn.setMinWidth(220);
        descriptionColumn.setMinWidth(250);
        descriptionColumn.setMinWidth(450);
        priceColumn.setMinWidth(100);
        priceColumn.setMaxWidth(120);
        categoryColumn.setMinWidth(100);
        categoryColumn.setMaxWidth(200);
	    
        nameColumn.setStyle("-fx-alignment: CENTER;");
        descriptionColumn.setStyle("-fx-alignment: CENTER;");
        priceColumn.setStyle("-fx-alignment: CENTER;");
        categoryColumn.setStyle("-fx-alignment: CENTER;");
	    
	    productTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	    
        productTable.setItems(produtos);

        // Desabilitar inicialmente os botões Atualizar e Deletar
        camcelarEdicaoButton.setDisable(true);
        updateButton.setDisable(true);
        deleteButton.setDisable(true);

        // Listener para desabilitar e habilitar botões com base na seleção
        productTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            produtoSelecionado = newSelection;
            if (produtoSelecionado != null) {
                preencherCamposComProdutoSelecionado(produtoSelecionado);
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
        carregarCategorias();
        carregarProdutos();
    }

    private void carregarCategorias() {
        ObservableList<CategoriaModel> categorias = FXCollections.observableArrayList();
        try {
            categorias = CategoriaService.listarCategorias(); // Obtenha a lista de categorias do banco de dados
        } catch (Exception e) {
            e.printStackTrace();
        }
        categoryComboBox.setItems(categorias);
    }
    
    private void carregarProdutos() {
        ObservableList<ProdutoModel> produtos = FXCollections.observableArrayList();
		try {
			produtos = ProdutoService.listarProdutos();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!produtos.isEmpty())
			productTable.setItems(produtos);
    }
    
    @FXML
    private void handleCreate() {
        
    	if (productNameField.getText().isEmpty() || descriptionField.getText().isEmpty() || categoryComboBox == null || categoryComboBox.getSelectionModel() == null || priceField.getText().isEmpty()) {
            showAlert("Erro", "Todos os campos devem ser preenchidos", Alert.AlertType.ERROR);
            return;
        }
		
        String nome = productNameField.getText();
        String descricao = descriptionField.getText();
        double preco = priceField.getText().isEmpty() ? 0 : Double.parseDouble(priceField.getText());
        CategoriaModel categoria = categoryComboBox.getSelectionModel().getSelectedItem();
        ProdutoModel novoProduto = new ProdutoModel();
        novoProduto.setNome(nome);
        novoProduto.setDescricao(descricao);
        novoProduto.setCategoria(categoria);
        novoProduto.setPreco(preco);
        ProdutoService.insert(novoProduto);
        
        showAlert("Sucesso", "Produto cadastrado com sucesso!", Alert.AlertType.INFORMATION);
        carregarProdutos();
        limparCampos();
    }

    @FXML
    private void handleUpdate() {
        if (produtoSelecionado != null) {
        	
	    	if (productNameField.getText().isEmpty() || descriptionField.getText().isEmpty() || categoryComboBox == null || categoryComboBox.getSelectionModel() == null || priceField.getText().isEmpty()) {
	            showAlert("Erro", "Todos os campos devem ser preenchidos", Alert.AlertType.ERROR);
	            return;
	        }
	        	
            // Atualizar os dados do produto selecionado com os valores dos campos de texto
	    	Long id = produtoSelecionado.getId(); // Obtenha o ID do item selecionado
	    	produtoSelecionado.setId(id);
            produtoSelecionado.setNome(productNameField.getText());
            produtoSelecionado.setDescricao(descriptionField.getText());
            produtoSelecionado.setPreco(Double.parseDouble(priceField.getText()));
            ProdutoService.update(produtoSelecionado);
            
            // Atualizar a tabela para refletir as mudanças
            productTable.refresh();

            // Limpar campos de entrada e desmarcar a seleção na tabela
            limparCampos();
            productTable.getSelectionModel().clearSelection();  // Remove a seleção na tabela
            produtoSelecionado = null;  // Resetar o produto selecionado

            // Reativar botões Criar e Buscar, e desativar Atualizar e Deletar
            createButton.setDisable(false);
            updateButton.setDisable(true); // Desabilitar o botão Atualizar
            camcelarEdicaoButton.setDisable(true); // Desabilitar o botão Atualizar
            deleteButton.setDisable(true); // Desabilitar o botão Deletar
        } else {
            showAlert("Nenhum produto selecionado", "Por favor, selecione um produto para atualizar.");
        }
    }

    @FXML
    private void handleCamcelarEdicao() {
        // Atualizar a tabela para refletir as mudanças
        productTable.refresh();

        // Limpar campos de entrada e desmarcar a seleção na tabela
        limparCampos();
        productTable.getSelectionModel().clearSelection();  // Remove a seleção na tabela
        produtoSelecionado = null;  // Resetar o produto selecionado

        // Reativar botões Criar e Buscar, e desativar Atualizar e Deletar
        createButton.setDisable(false);
        updateButton.setDisable(true); // Desabilitar o botão Atualizar
        deleteButton.setDisable(true); // Desabilitar o botão Deletar
        camcelarEdicaoButton.setDisable(true); // Desabilitar o botão Deletar
    }
    
    @FXML
    private void handleDelete() {
        if (produtoSelecionado != null) {
            produtos.remove(produtoSelecionado);
            limparCampos();
            produtoSelecionado = null;
            deleteButton.setDisable(true);
            updateButton.setDisable(true);

            // Reativar botões Criar e Buscar após a exclusão
            createButton.setDisable(false);
        } else {
            showAlert("Nenhum produto selecionado", "Por favor, selecione um produto para deletar.");
        }
    }

    private void preencherCamposComProdutoSelecionado(ProdutoModel produto) {
    	productNameField.setText(produto.getNome());
    	descriptionField.setText(produto.getDescricao());
    	priceField.setText(String.valueOf(produto.getPreco()));
    }

    private void limparCampos() {
    	productNameField.clear();
    	descriptionField.clear();
    	priceField.clear();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
