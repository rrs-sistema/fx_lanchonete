package model;

public class CarrinhoItemModel {
    private ProdutoModel produto;
    private int quantidade;
    private double precoUnitario; // Valor unitário do produto

    public CarrinhoItemModel(ProdutoModel produto, int quantidade, double precoUnitario) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    // Getters e Setters
    public ProdutoModel getProduto() { return produto; }
    public void setProduto(ProdutoModel produto) { this.produto = produto; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public double getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(double precoUnitario) { this.precoUnitario = precoUnitario; }

    public double getTotalItem() {
        return quantidade * precoUnitario;
    }
    
    // Novo getter para obter o nome do produto diretamente
    public String getProdutoNome() {
        return produto.getNome();
    }    
}
