package model;

import java.util.Date;
import java.util.List;

public class CarrinhoModel {
	
    private ClienteModel cliente;
    private Date data;
    private List<CarrinhoItemModel> itens;
    private double valorTotal;

    public CarrinhoModel(ClienteModel cliente, Date data, List<CarrinhoItemModel> itens) {
        this.cliente = cliente;
        this.data = data;
        this.itens = itens;
        this.valorTotal = calcularValorTotal();
    }

    // Getters e Setters
    public ClienteModel getCliente() { return cliente; }
    public void setCliente(ClienteModel cliente) { this.cliente = cliente; }

    public Date getData() { return data; }
    public void setData(Date data) { this.data = data; }

    public List<CarrinhoItemModel> getItens() { return itens; }
    public void setItens(List<CarrinhoItemModel> itens) { 
        this.itens = itens;
        this.valorTotal = calcularValorTotal(); // Atualiza o valor total
    }

    public double getValorTotal() { return valorTotal; }

    // Método para calcular o valor total dos itens do carrinho
    private double calcularValorTotal() {
        return itens.stream().mapToDouble(CarrinhoItemModel::getTotalItem).sum();
    }
}
