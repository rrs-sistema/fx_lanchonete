package gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import model.CarrinhoModel;
import service.CarrinhoService;

public class FinanceiroController {

    @FXML
    private TableView<CarrinhoModel> financeiroTable;
    @FXML
    private TableColumn<CarrinhoModel, LocalDate> dataColumn;
    @FXML
    private TableColumn<CarrinhoModel, String> clienteColumn;
    @FXML
    private TableColumn<CarrinhoModel, String> tipoPagamentoColumn;    
    @FXML
    private TableColumn<CarrinhoModel, Double> valorTotalColumn;
    @FXML
    private TableColumn<CarrinhoModel, Double> lucroColumn;
    @FXML
    private TableColumn<CarrinhoModel, Double> custoColumn;
    @FXML
    private Label totalVendasLabel;
    @FXML
    private Label lucroTotalLabel;
    @FXML
    private Label custoTotalLabel;
    @FXML
    private DatePicker dataInicialPicker;
    @FXML
    private DatePicker dataFinalPicker;
    @FXML
    private ProgressIndicator loadingIndicator;

    @SuppressWarnings("deprecation")
	@FXML
    private void initialize() {
        dataColumn.setCellValueFactory(new PropertyValueFactory<>("data"));
        tipoPagamentoColumn.setCellValueFactory(new PropertyValueFactory<>("tipoPagamento"));
        clienteColumn.setCellValueFactory(new PropertyValueFactory<>("clienteNome"));
        clienteColumn.setVisible(false);
        valorTotalColumn.setCellValueFactory(new PropertyValueFactory<>("valorTotal"));
        custoColumn.setCellValueFactory(new PropertyValueFactory<>("custoTotal"));
        lucroColumn.setCellValueFactory(new PropertyValueFactory<>("lucro"));
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy").withLocale(new Locale("pt", "BR"));
     // Configura a coluna de data com formatação personalizada
        dataColumn.setCellFactory(column -> {
            return new TableCell<CarrinhoModel, LocalDate>() {
                @Override
                protected void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(dateFormatter.format(item));
                    }
                }
            };
        });


		// Configuração das larguras das colunas do cartTable
		dataColumn.setMinWidth(90);
		dataColumn.setMaxWidth(120);

		valorTotalColumn.setMinWidth(100);
		valorTotalColumn.setMaxWidth(140);

		custoColumn.setMinWidth(110);
		custoColumn.setMaxWidth(110);

		lucroColumn.setMinWidth(110);
		lucroColumn.setMaxWidth(110);

        configurarColunaMoeda(valorTotalColumn);
        configurarColunaMoeda(custoColumn);
        configurarColunaMoeda(lucroColumn);
        
		tipoPagamentoColumn.setMinWidth(180);
		
		dataColumn.setStyle("-fx-alignment: CENTER;");
		tipoPagamentoColumn.setStyle("-fx-alignment: CENTER;");
		clienteColumn.setStyle("-fx-alignment: CENTER;");
		valorTotalColumn.setStyle("-fx-alignment: CENTER;");
		custoColumn.setStyle("-fx-alignment: CENTER;");
		lucroColumn.setStyle("-fx-alignment: CENTER;");
		
        financeiroTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        dataInicialPicker.setValue(LocalDate.now().minusDays(7));
        dataFinalPicker.setValue(LocalDate.now());
        
        carregarDadosComEspera();
    }

    private void configurarColunaMoeda(TableColumn<CarrinhoModel, Double> coluna) {
        coluna.setCellFactory(new Callback<TableColumn<CarrinhoModel, Double>, TableCell<CarrinhoModel, Double>>() {
            @Override
            public TableCell<CarrinhoModel, Double> call(TableColumn<CarrinhoModel, Double> param) {
                return new TableCell<CarrinhoModel, Double>() {
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
    
    
    private void carregarDadosComEspera() {
        loadingIndicator.setVisible(true);

        Task<ObservableList<CarrinhoModel>> loadDataTask = new Task<>() {
            @Override
            protected ObservableList<CarrinhoModel> call() throws Exception {
                LocalDate dataInicial = dataInicialPicker.getValue();
                LocalDate dataFinal = dataFinalPicker.getValue();
                List<CarrinhoModel> resultados = CarrinhoService.listaVenda(dataInicial, dataFinal);
                return FXCollections.observableArrayList(resultados);
            }
        };

        loadDataTask.setOnSucceeded(event -> {
            financeiroTable.setItems(loadDataTask.getValue());
            atualizarTotais(loadDataTask.getValue());
            loadingIndicator.setVisible(false); // Esconde o indicador de carregamento
        });

        loadDataTask.setOnFailed(event -> {
            loadDataTask.getException().printStackTrace();
            loadingIndicator.setVisible(false);
        });

        new Thread(loadDataTask).start();
    }

    private void atualizarTotais(ObservableList<CarrinhoModel> dados) {
        double totalVendas = dados.stream().mapToDouble(CarrinhoModel::getValorTotal).sum();
        double totalCusto = dados.stream().mapToDouble(CarrinhoModel::getCustoTotal).sum();
        double lucroTotal = dados.stream().mapToDouble(CarrinhoModel::getLucro).sum();

        totalVendasLabel.setText(String.format("Total de Vendas: R$ %.2f", totalVendas));
        custoTotalLabel.setText(String.format("Custo Total: R$ %.2f", totalCusto));
        lucroTotalLabel.setText(String.format("Lucro Total: R$ %.2f", lucroTotal));
    }

    @FXML
    private void filtrarPorData() {
        carregarDadosComEspera();
    }
}
