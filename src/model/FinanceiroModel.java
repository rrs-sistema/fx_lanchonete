package model;

public class FinanceiroModel {
    private String data;
    private String clienteNome;
    private double valorTotal;
    private double custo;
    private double lucro;

    public FinanceiroModel(String data, String clienteNome, double valorTotal, double custo, double lucro) {
        this.data = data;
        this.clienteNome = clienteNome;
        this.valorTotal = valorTotal;
        this.custo = custo;
        this.lucro = lucro;
    }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    public String getClienteNome() { return clienteNome; }
    public void setClienteNome(String clienteNome) { this.clienteNome = clienteNome; }

    public double getValorTotal() { return valorTotal; }
    public void setValorTotal(double valorTotal) { this.valorTotal = valorTotal; }

    public double getCusto() { return custo; }
    public void setCusto(double custo) { this.custo = custo; }

    public double getLucro() { return lucro; }
    public void setLucro(double lucro) { this.lucro = lucro; }
}
