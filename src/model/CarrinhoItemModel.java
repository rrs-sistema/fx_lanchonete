package model;

public class CarrinhoItemModel {
	
	private ClienteModel cliente;
    private ProdutoModel produto;
    private int quantidade;

    public CarrinhoItemModel(ProdutoModel produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public ProdutoModel getProduto() {
        return produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getPrecoTotal() {
        return produto.getPreco() * quantidade;
    }
    
 // Novo getter para obter o nome do produto diretamente
    public String getProdutoNome() {
        return produto.getNome();
    }

	public ClienteModel getCliente() {
		return cliente;
	}

	public void setCliente(ClienteModel cliente) {
		this.cliente = cliente;
	}    
}
