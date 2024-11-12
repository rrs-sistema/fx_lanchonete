package gui;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
import model.CarrinhoModel;
import model.ClienteModel;
import model.ProdutoModel;
import service.CarrinhoService;
import service.ClienteService;
import service.ProdutoService;

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
    private TableColumn<ProdutoModel, Double> cartPrecoUnitarioColumn;
    
    @FXML
    private TableColumn<ProdutoModel, Double> cartTotalColumn;
    
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
    
    private ObservableList<CarrinhoItemModel> carrinho = FXCollections.observableArrayList();

    @SuppressWarnings("deprecation")
	@FXML
    private void initialize() {
    	// Configurar as colunas
        menuProductColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        menuPriceColumn.setCellValueFactory(new PropertyValueFactory<>("preco"));
        
        configurarColunaMoeda(menuPriceColumn);        
        // Carregar produtos na tabela
        menuTable.setItems(getProdutos());
        
        // Configuração da tabela de carrinho
        cartProductColumn.setCellValueFactory(new PropertyValueFactory<>("produtoNome"));
        cartQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        cartPrecoUnitarioColumn.setCellValueFactory(new PropertyValueFactory<>("precoUnitario"));
        cartTotalColumn.setCellValueFactory(new PropertyValueFactory<>("totalItem"));
        
        // Configuração das larguras das colunas do cartTable
        cartQuantityColumn.setMinWidth(180);
        cartQuantityColumn.setMaxWidth(180);
        
        cartPrecoUnitarioColumn.setMinWidth(160);
        cartPrecoUnitarioColumn.setMaxWidth(180);
        
        cartTotalColumn.setMinWidth(100);
        cartTotalColumn.setMaxWidth(140);
        
        configurarColunaMoeda(cartPrecoUnitarioColumn);
        configurarColunaMoeda(cartTotalColumn);
        
        cartRemoveColumn.setMinWidth(110);
        cartRemoveColumn.setMaxWidth(110);

        cartQuantityColumn.setStyle("-fx-alignment: CENTER;");
        cartPrecoUnitarioColumn.setStyle("-fx-alignment: CENTER;");
        cartTotalColumn.setStyle("-fx-alignment: CENTER;");
        cartRemoveColumn.setStyle("-fx-alignment: CENTER;");
        cartProductColumn.setStyle("-fx-alignment: CENTER;");
        
        // Configurar a política de redimensionamento para preencher o restante da coluna "Produto"
        cartTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        cartTable.setItems(carrinho);     
        
        // Configurar a coluna de remoção
        configurarColunaRemover(); 
        
        // Inicializar o total
        atualizarTotalLabel();        
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

        CarrinhoItemModel itemCarrinho = new CarrinhoItemModel(selectedProduct, quantidade, selectedProduct.getPreco());
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
        double total = carrinho.stream().mapToDouble(CarrinhoItemModel::getTotalItem).sum();
        totalLabel.setText(String.format("Total: R$ %.2f", total));
    }

    
    @FXML
    private void handleCheckout() {
        VendaDialog dialog = new VendaDialog();

        // Exibir a lista de clientes
        dialog.getClienteComboBox().getItems().addAll(getClientes());

        dialog.getConfirmarButton().setOnAction(event -> {
        	ClienteModel clienteSelecionado = dialog.getClienteComboBox().getValue();
            String pagamentoSelecionado = dialog.getPagamentoComboBox().getValue();

            if (clienteSelecionado == null || pagamentoSelecionado == null) {
                showAlert("Campos obrigatórios", "Por favor, selecione o cliente e o tipo de pagamento.");
            } else {
                salvarVenda(clienteSelecionado, pagamentoSelecionado); // Função para salvar a venda
                dialog.close();
            }
        });

        dialog.showAndWait();
    }
    
    private void salvarVenda(ClienteModel cliente, String tipoPagamento) {
        List<CarrinhoItemModel> itens = new ArrayList<>(carrinho);  // Considerando que "carrinho" é a lista de itens

        CarrinhoModel carrinhoModel = new CarrinhoModel();
        carrinhoModel.setCliente(cliente);
        carrinhoModel.setTipoPagamento(tipoPagamento);
        carrinhoModel.setData(LocalDate.now());
        carrinhoModel.setItens(itens);

        // Calcula o custo total e o valor total dos itens
        double custoTotal = itens.stream().mapToDouble(CarrinhoItemModel::getPrecoUnitario).sum();
        carrinhoModel.setCustoTotal(custoTotal);

        // Salva no banco
        boolean sucesso = CarrinhoService.salvarVenda(carrinhoModel);

        if (sucesso) {
            showAlert("Venda finalizada", "A venda foi salva com sucesso!", Alert.AlertType.INFORMATION);
            for (CarrinhoItemModel item : itens) {
            	ProdutoModel produto = item.getProduto();
            	ProdutoService.updateEstoque(produto.getId(), item.getQuantidade());
    		}            
            limparCarrinho();
        } else {
            showAlert("Erro ao finalizar venda", "Houve um problema ao salvar a venda.", Alert.AlertType.ERROR);
        }
    }
    
    @SuppressWarnings("exports")
	public double obterValorDouble(TextField campo) {
        String textoFormatado = campo.getText().replaceAll("[^\\d,]", "").replace(",", ".");
        return Double.parseDouble(textoFormatado);
    }
    
    private ObservableList<ClienteModel> getClientes() {
        ObservableList<ClienteModel> produtos = FXCollections.observableArrayList();
        
        try {
			produtos = ClienteService.listarClientes();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return produtos;
    }
    
    private ObservableList<ProdutoModel> getProdutos() {
        ObservableList<ProdutoModel> produtos = FXCollections.observableArrayList();
        
        try {
			produtos = ProdutoService.listarProdutos();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return produtos;
    }
    
    private void limparCarrinho() {
        carrinho.clear();
        atualizarTotalLabel();
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