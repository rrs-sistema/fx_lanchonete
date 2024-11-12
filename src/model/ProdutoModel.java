package model;

public class ProdutoModel {

	private Long id;
	private String nome;
	private String descricao;
	private CategoriaModel categoria;
	private double estoque;
	private double preco;
	private double custo;

	public ProdutoModel() {
	}

	public ProdutoModel(Long id, String nome, double preco) {
		this.id = id;
		this.nome = nome;
		this.preco = preco;
	}
	
	public ProdutoModel(Long id, String nome, String descricao, double preco) {
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.preco = preco;
	}
	
	public ProdutoModel(Long id, String nome, String descricao, CategoriaModel categoria, double preco) {
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.categoria = categoria;
		this.preco = preco;
	}
	
	public ProdutoModel(Long id, String nome, String descricao, CategoriaModel categoria, double preco, double custo) {
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.categoria = categoria;
		this.preco = preco;
		this.custo = custo;
	}

	public ProdutoModel(Long id, String nome, String descricao, CategoriaModel categoria, double preco, double custo, double estoque) {
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.categoria = categoria;
		this.preco = preco;
		this.custo = custo;
		this.estoque = estoque;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public CategoriaModel getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaModel categoria) {
		this.categoria = categoria;
	}

	public double getPreco() {
		return preco;
	}

	public void setPreco(double preco) {
		this.preco = preco;
	}

	// Getters e Setters
	public double getCusto() {
		return custo;
	}

	public void setCusto(double custo) {
		this.custo = custo;
	}

	@Override
	public String toString() {
		return nome;
	}

	public double getEstoque() {
		return estoque;
	}

	public void setEstoque(double estoque) {
		this.estoque = estoque;
	}
	
}
