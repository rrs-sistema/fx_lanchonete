package gui;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
    private TextField estoqueField;
    
    @FXML
    private TextField custoField;
    
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
    private TableColumn<ProdutoModel, Double> estoqueColumn;      
    
    @FXML
    private TableColumn<ProdutoModel, Double> priceColumn;  
    
    @FXML
    private TableColumn<ProdutoModel, Double> custoColumn;      
    
    private ObservableList<ProdutoModel> produtos = FXCollections.observableArrayList();
    private ObservableList<CategoriaModel> categorias = FXCollections.observableArrayList();
    
    private ProdutoModel produtoSelecionado;

    @SuppressWarnings("deprecation")
	@FXML
    private void initialize() {
    	idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    	idColumn.setVisible(false);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        estoqueColumn.setCellValueFactory(new PropertyValueFactory<>("estoque"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("preco"));
        custoColumn.setCellValueFactory(new PropertyValueFactory<>("custo"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("descricao"));
	    
	    // Definir larguras e centralizar o conteúdo das colunas
        nameColumn.setMinWidth(220);
        descriptionColumn.setMinWidth(250);
        descriptionColumn.setMinWidth(450);
        estoqueColumn.setMinWidth(140);
        estoqueColumn.setMaxWidth(180);
        
        priceColumn.setMinWidth(100);
        priceColumn.setMaxWidth(120);
        custoColumn.setMinWidth(100);
        custoColumn.setMaxWidth(120);        
        categoryColumn.setMinWidth(100);
        categoryColumn.setMaxWidth(200);
	    
        nameColumn.setStyle("-fx-alignment: CENTER;");
        descriptionColumn.setStyle("-fx-alignment: CENTER;");
        estoqueColumn.setStyle("-fx-alignment: CENTER;");
        priceColumn.setStyle("-fx-alignment: CENTER;");
        custoColumn.setStyle("-fx-alignment: CENTER;");
        categoryColumn.setStyle("-fx-alignment: CENTER;");
        
        configurarColunaMoeda(custoColumn);
        configurarColunaMoeda(priceColumn);
	    
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
        
        configurarCampoMoeda(priceField);
        configurarCampoMoeda(custoField);
        
        carregarCategorias();
        carregarProdutos();
    }

    private void configurarColunaMoeda(TableColumn<ProdutoModel, Double> coluna) {
        coluna.setCellFactory(new Callback<TableColumn<ProdutoModel, Double>, TableCell<ProdutoModel, Double>>() {
            @Override
            public TableCell<ProdutoModel, Double> call(TableColumn<ProdutoModel, Double> param) {
                return new TableCell<ProdutoModel, Double>() {
                    @Override
                    protected void updateItem(Double valor, boolean empty) {
                        super.updateItem(valor, empty);
                        if (empty || valor == null) {
                            setText(null);
                        } else {
                            NumberFormat formatoBrasileiro = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
                            setText(formatoBrasileiro.format(valor));
                        }
                    }
                };
            }
        });
    }
    
    
    private void configurarCampoMoeda(TextField campo) {
        campo.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                campo.setText("R$ 0,00");
            } else {
                campo.setText(formatarParaReal(newValue));
                campo.positionCaret(campo.getText().length());  // Move o cursor para o final
            }
        });
    }

    @SuppressWarnings("exports")
	public double obterValorDouble(TextField campo) {
        String textoFormatado = campo.getText().replaceAll("[^\\d,]", "").replace(",", ".");
        return Double.parseDouble(textoFormatado);
    }
    
    private String formatarParaReal(String valor) {
        String textoLimpo = valor.replaceAll("[^\\d]", "");
        double valorNumerico = Double.parseDouble(textoLimpo) / 100;
        NumberFormat formatoBrasileiro = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return formatoBrasileiro.format(valorNumerico);
    }
    
    private void carregarCategorias() {
       
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
        
        double preco = obterValorDouble(priceField);
        double custo = obterValorDouble(custoField);
        double estoque = obterValorDouble(estoqueField);
        
        
        CategoriaModel categoria = categoryComboBox.getSelectionModel().getSelectedItem();
        ProdutoModel novoProduto = new ProdutoModel();
        novoProduto.setNome(nome);
        novoProduto.setDescricao(descricao);
        novoProduto.setCategoria(categoria);
        novoProduto.setEstoque(estoque);
        novoProduto.setPreco(preco);
        novoProduto.setCusto(custo);
        ProdutoService.insert(novoProduto);
        
        showAlert("Sucesso", "Produto cadastrado com sucesso!", Alert.AlertType.INFORMATION);
        carregarProdutos();
        limparCampos();
    }

    @FXML
    private void handleUpdate() {
        if (produtoSelecionado != null) {
        	
	    	if (productNameField.getText().isEmpty() || descriptionField.getText().isEmpty() || categoryComboBox == null 
	    			|| categoryComboBox.getSelectionModel() == null || priceField.getText() == null || priceField.getText().isEmpty() ||
	    			custoField.getText() == null || custoField.getText().isEmpty()) {
	            showAlert("Erro", "Todos os campos devem ser preenchidos", Alert.AlertType.ERROR);
	            return;
	        }
	        	
	        double preco = obterValorDouble(priceField);
	        double custo = obterValorDouble(custoField);
	        double estoque = obterValorDouble(estoqueField);
	        
            // Atualizar os dados do produto selecionado com os valores dos campos de texto
	    	Long id = produtoSelecionado.getId(); // Obtenha o ID do item selecionado
	    	produtoSelecionado.setId(id);
            produtoSelecionado.setNome(productNameField.getText());
            produtoSelecionado.setDescricao(descriptionField.getText());
            produtoSelecionado.setPreco(preco);
            produtoSelecionado.setCusto(custo);
            produtoSelecionado.setEstoque(estoque);
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
    	
    	NumberFormat formatoBrasileiro = DecimalFormat.getCurrencyInstance(new Locale("pt", "BR"));
    	NumberFormat formatoNumBrasileiro = DecimalFormat.getNumberInstance(new Locale("pt", "BR"));
    	   
    	
    	productNameField.setText(produto.getNome());
    	descriptionField.setText(produto.getDescricao());
        custoField.setText(formatoBrasileiro.format(produto.getCusto()));
        priceField.setText(formatoBrasileiro.format(produto.getPreco()));
        estoqueField.setText(formatoNumBrasileiro.format(produto.getEstoque()));
    	categoryComboBox.setValue(produto.getCategoria());
    }

    private void limparCampos() {
    	productNameField.clear();
    	descriptionField.clear();
    	categoryComboBox.setValue(null);
    	priceField.clear();
    	custoField.clear();
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
