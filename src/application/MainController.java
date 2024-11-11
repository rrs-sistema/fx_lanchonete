package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class MainController {

    @FXML
    private StackPane contentArea;
    
    @FXML
    private Button btnCategoria;
    @FXML
    private Button btnCliente;
    @FXML
    private Button btnProduto;
    @FXML
    private Button btnPDV;
    

    @FXML
    private void initialize() {
    	handlePdv();
    	atualizarEstiloMenu(btnPDV);
    }
    
    private void atualizarEstiloMenu(Button botaoSelecionado) {
        // Limpa o estilo de todos os botões
        btnCategoria.getStyleClass().remove("menu-selecionado");
        btnCliente.getStyleClass().remove("menu-selecionado");
        btnProduto.getStyleClass().remove("menu-selecionado");
        btnPDV.getStyleClass().remove("menu-selecionado");

        // Adiciona a classe "menu-selecionado" ao botão selecionado
        botaoSelecionado.getStyleClass().add("menu-selecionado");
    }
    
    // Carrega a tela de Cadastro de categoria
    @FXML
    private void handleCadastroCategoria() {
        carregarConteudo("/gui/Categoria.fxml");
        atualizarEstiloMenu(btnCategoria);
    }

    // Carrega a tela de Cadastro de Cliente
    @FXML    
    private void handleCadastroCliente() {
        carregarConteudo("/gui/Cliente.fxml");
        atualizarEstiloMenu(btnCliente);
    }
    
    // Carrega a tela de Cadastro de Produto
    @FXML
    private void handleCadastroProduto() {
        carregarConteudo("/gui/Produto.fxml");
        atualizarEstiloMenu(btnProduto);
    }

    // Carrega a tela de PDV
    @FXML
    private void handlePdv() {
        carregarConteudo("/gui/PDV.fxml");
        atualizarEstiloMenu(btnPDV);
    }

    private void carregarConteudo(String fxmlFile) {
        try {
            Node node = FXMLLoader.load(getClass().getResource(fxmlFile));
            setContent(node);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setContent(Node node) {
        if (contentArea != null) {
            contentArea.getChildren().setAll(node);
        } else {
            System.out.println("contentArea is null");
        }
    }
}
