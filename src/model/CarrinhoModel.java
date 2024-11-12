package model;

import java.time.LocalDate;
import java.util.List;

public class CarrinhoModel {

	private Long id;
	private ClienteModel cliente;
	private String tipoPagamento;
	private LocalDate data;
	private List<CarrinhoItemModel> itens;
	private double custoTotal;

	public CarrinhoModel() {
	}

	public CarrinhoModel(Long id, ClienteModel cliente, LocalDate data, double custoTotal, String tipoPagamento) {
		this.id = id;
		this.cliente = cliente;
		this.data = data;
		this.custoTotal = custoTotal;
		this.tipoPagamento = tipoPagamento;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ClienteModel getCliente() {
		return cliente;
	}

	public void setCliente(ClienteModel cliente) {
		this.cliente = cliente;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public List<CarrinhoItemModel> getItens() {
		return itens;
	}

	public void setItens(List<CarrinhoItemModel> itens) {
		this.itens = itens;
	}

	public double getValorTotal() {
		return itens.stream().mapToDouble(CarrinhoItemModel::getTotalItem).sum();
	}

	public double getLucro() {
		return getValorTotal() - custoTotal;
	}

	public double getCustoTotal() {
		return custoTotal;
	}

	public void setCustoTotal(double custoTotal) {
		this.custoTotal = custoTotal;
	}

	public String getTipoPagamento() {
		return tipoPagamento;
	}

	public void setTipoPagamento(String tipoPagamento) {
		this.tipoPagamento = tipoPagamento;
	}

	public String getClienteNome() {
		return cliente != null ? cliente.getNome() : "";
	}
}
