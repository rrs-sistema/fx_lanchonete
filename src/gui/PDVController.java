package gui;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import model.CarrinhoItemModel;
import model.ProdutoModel;

public class PDVController {

    @FXML
    private TableView<ProdutoModel> menuTable;
    
    @FXML
    private TextField quantityField;
    
    @FXML
    private TableView<CarrinhoItemModel> cartTable;
    
    @FXML
    private TableColumn<ProdutoModel, Integer> menuProductColumn;

    @FXML
    private TableColumn<ProdutoModel, Double> menuPriceColumn;    
    
    @FXML
    private TableColumn<ProdutoModel, String> cartProductColumn;

    @FXML
    private TableColumn<ProdutoModel, Double> cartQuantityColumn;   
    
    @FXML
    private TableColumn<ProdutoModel, Integer> cartTotalColumn;
    
    @FXML
    private TableColumn<CarrinhoItemModel, Void> cartRemoveColumn;    
    
    @FXML
    private Button removeButton;
    
    @FXML
    private Button addButton;
    
    @FXML
    private Button checkoutButton;
    
    @FXML
    private Label totalLabel;

    private double total = 0.0;
    
    private ObservableList<CarrinhoItemModel> carrinho = FXCollections.observableArrayList();

    @SuppressWarnings("deprecation")
	@FXML
    private void initialize() {
    	// Configurar as colunas
        menuProductColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        menuPriceColumn.setCellValueFactory(new PropertyValueFactory<>("preco"));
        // Carregar produtos na tabela
        menuTable.setItems(getProdutos());
        
        // Configuração da tabela de carrinho
        cartProductColumn.setCellValueFactory(new PropertyValueFactory<>("produtoNome"));
        cartQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        cartTotalColumn.setCellValueFactory(new PropertyValueFactory<>("precoTotal"));
        
        // Configuração das larguras das colunas do cartTable
        cartQuantityColumn.setMinWidth(180);
        cartQuantityColumn.setMaxWidth(180);
        cartTotalColumn.setMinWidth(160);
        cartTotalColumn.setMaxWidth(160);
        cartRemoveColumn.setMinWidth(110);
        cartRemoveColumn.setMaxWidth(110);

        // Configurar a política de redimensionamento para preencher o restante da coluna "Produto"
        cartTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        cartTable.setItems(carrinho);     
        
        // Configurar a coluna de remoção
        configurarColunaRemover(); 
        
        // Inicializar o total
        atualizarTotalLabel();        
    }
    
    @FXML
    private void handleAddToCart() {
        ProdutoModel selectedProduct = menuTable.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            showAlert("Nenhum produto selecionado", "Por favor, selecione um produto do cardápio.");
            return;
        }

        int quantidade;
        try {
            quantidade = Integer.parseInt(quantityField.getText());
            if (quantidade <= 0) {
                showAlert("Quantidade inválida", "Por favor, insira uma quantidade maior que zero.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Quantidade inválida", "Por favor, insira um número de quantidade válido.");
            return;
        }

        CarrinhoItemModel itemCarrinho = new CarrinhoItemModel(selectedProduct, quantidade);
        carrinho.add(itemCarrinho);
        
     // Atualizar o total sempre que um item é adicionado
        atualizarTotalLabel();

        quantityField.clear(); // Limpa o campo de quantidade após adicionar ao carrinho
    }
    
    
    // Configuração da coluna "Remover" com um botão para cada item
    private void configurarColunaRemover() {
        cartRemoveColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<CarrinhoItemModel, Void> call(final TableColumn<CarrinhoItemModel, Void> param) {
                final TableCell<CarrinhoItemModel, Void> cell = new TableCell<>() {
                    private final Button btn = new Button("Remover");

                    {
                        btn.setOnAction(event -> {
                        	CarrinhoItemModel item = getTableView().getItems().get(getIndex());
                            carrinho.remove(item);
                            atualizarTotalLabel();
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        });
    }
    
 // Novo método para remover o item selecionado do carrinho
    @FXML
    private void handleRemoveFromCart() {
        CarrinhoItemModel selectedItem = cartTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            carrinho.remove(selectedItem); // Remove o item da lista
            atualizarTotalLabel(); // Atualiza o total após a remoção
        } else {
            showAlert("Nenhum item selecionado", "Por favor, selecione um item do carrinho para remover.");
        }
    }
    
 // Método para calcular e atualizar o total no totalLabel
    private void atualizarTotalLabel() {
        double total = carrinho.stream().mapToDouble(CarrinhoItemModel::getPrecoTotal).sum();
        totalLabel.setText(String.format("Total: R$ %.2f", total));
    }

    
    @FXML
    private void handleCheckout() {
        // Finalize a venda e limpe o carrinho
        System.out.println("Venda finalizada. Total: R$ " + String.format("%.2f", total));
        total = 0.0;
        totalLabel.setText("Total: R$ 0,00");
        cartTable.getItems().clear();
    }
    
    // Lista de produtos para o cardápio
    private ObservableList<ProdutoModel> getProdutos() {
        ObservableList<ProdutoModel> produtos = FXCollections.observableArrayList();
      
        produtos.add(new ProdutoModel("Hamburguer", 15.00));
        produtos.add(new ProdutoModel("Pizza", 20.00));
        produtos.add(new ProdutoModel("Refrigerante", 5.00));
        produtos.add(new ProdutoModel("Batata Frita", 10.00));
        produtos.add(new ProdutoModel("Suco", 7.00));
        return produtos;
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }    
    
}