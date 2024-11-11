package gui;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.FinanceiroModel;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDate;
import java.util.stream.Collectors;

public class FinanceiroController {

	@FXML
	private TableView<FinanceiroModel> financeiroTable;

	@FXML
	private TableColumn<FinanceiroModel, String> dataColumn;

	@FXML
	private TableColumn<FinanceiroModel, String> clienteColumn;

	@FXML
	private TableColumn<FinanceiroModel, Double> valorTotalColumn;

	@FXML
	private TableColumn<FinanceiroModel, Double> lucroColumn;

	@FXML
	private TableColumn<FinanceiroModel, Double> custoColumn;

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

	private ObservableList<FinanceiroModel> dadosFinanceiros;

	@SuppressWarnings("deprecation")
	@FXML
	private void initialize() {
		// Configura as colunas
		dataColumn.setCellValueFactory(new PropertyValueFactory<>("data"));
		clienteColumn.setCellValueFactory(new PropertyValueFactory<>("clienteNome"));
		valorTotalColumn.setCellValueFactory(new PropertyValueFactory<>("valorTotal"));
		custoColumn.setCellValueFactory(new PropertyValueFactory<>("custo"));
		lucroColumn.setCellValueFactory(new PropertyValueFactory<>("lucro"));

		// Configuração das larguras das colunas do cartTable
		dataColumn.setMinWidth(90);
		dataColumn.setMaxWidth(120);

		valorTotalColumn.setMinWidth(100);
		valorTotalColumn.setMaxWidth(140);

		custoColumn.setMinWidth(110);
		custoColumn.setMaxWidth(110);

		lucroColumn.setMinWidth(110);
		lucroColumn.setMaxWidth(110);

		dataColumn.setStyle("-fx-alignment: CENTER;");
		clienteColumn.setStyle("-fx-alignment: CENTER;");
		valorTotalColumn.setStyle("-fx-alignment: CENTER;");
		custoColumn.setStyle("-fx-alignment: CENTER;");
		lucroColumn.setStyle("-fx-alignment: CENTER;");

		// Configurar a política de redimensionamento para preencher o restante da
		// coluna "Produto"
		financeiroTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		// Carrega os dados financeiros
		carregarDadosFinanceiros();

		// Atualiza os totais
		 atualizarTotais(dadosFinanceiros);
	}

	private void carregarDadosFinanceiros() {
		// Exemplo de dados, substitua pela lógica de consulta ao banco
		dadosFinanceiros = FXCollections.observableArrayList(
				new FinanceiroModel("2024-11-01", "Cliente A", 100.00, 75.00, 25.00),
				new FinanceiroModel("2024-11-02", "Cliente B", 200.00, 120.00, 80.00),
				new FinanceiroModel("2024-11-03", "Cliente C", 150.00, 110.00, 40.00));

		financeiroTable.setItems(dadosFinanceiros);
	}

	private void atualizarTotais(ObservableList<FinanceiroModel> dados) {
		double totalVendas = dadosFinanceiros.stream().mapToDouble(FinanceiroModel::getValorTotal).sum();
		double totalCusto = dadosFinanceiros.stream().mapToDouble(FinanceiroModel::getCusto).sum();
		double lucroTotal = dadosFinanceiros.stream().mapToDouble(FinanceiroModel::getLucro).sum();

		totalVendasLabel.setText(String.format("Total de Vendas: R$ %.2f", totalVendas));
		custoTotalLabel.setText(String.format("Custo Total: R$ %.2f", totalCusto));
		lucroTotalLabel.setText(String.format("Lucro Total: R$ %.2f", lucroTotal));
	}

	@FXML
	private void filtrarPorData() {
		LocalDate dataInicial = dataInicialPicker.getValue();
		LocalDate dataFinal = dataFinalPicker.getValue();

		ObservableList<FinanceiroModel> dadosFiltrados = dadosFinanceiros.stream().filter(item -> {
			LocalDate dataItem = LocalDate.parse(item.getData());
			return (dataInicial == null || !dataItem.isBefore(dataInicial))
					&& (dataFinal == null || !dataItem.isAfter(dataFinal));
		}).collect(Collectors.toCollection(FXCollections::observableArrayList));

		financeiroTable.setItems(dadosFiltrados);
		atualizarTotais(dadosFiltrados);
	}

	@FXML
	private void handleAtualizarDados() {

	}

	@FXML
	private void handleExportarRelatorio() {

	}
}
