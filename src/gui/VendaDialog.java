package gui;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import model.ClienteModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class VendaDialog extends Stage {
    
    private ComboBox<ClienteModel> clienteComboBox;
    private ComboBox<String> pagamentoComboBox;
    private Button confirmarButton;
    private Button cancelarButton;

    public VendaDialog() {
        initStyle(StageStyle.UTILITY);
        initModality(Modality.APPLICATION_MODAL);
        setTitle("Informações da Venda");

        // Título
        Label titleLabel = new Label("Confirmação da Venda");
        titleLabel.setFont(new Font("Arial Bold", 20));
        titleLabel.setStyle("-fx-text-fill: #34495e;");
        
        // Cliente
        Label clienteLabel = new Label("Cliente:");
        clienteLabel.setFont(new Font("Arial", 16));
        clienteComboBox = new ComboBox<>();
        clienteComboBox.setPromptText("Selecione o cliente");

        // Tipo de Pagamento
        Label pagamentoLabel = new Label("Tipo de Pagamento:");
        pagamentoLabel.setFont(new Font("Arial", 16));
        pagamentoComboBox = new ComboBox<>();
        pagamentoComboBox.getItems().addAll("Dinheiro", "Cartão", "Pix");
        pagamentoComboBox.setPromptText("Selecione o tipo de pagamento");

        // Botões
        confirmarButton = new Button("Confirmar");
        confirmarButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-size: 14px; -fx-pref-width: 100px;");
        cancelarButton = new Button("Cancelar");
        cancelarButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 14px; -fx-pref-width: 100px;");
        
        cancelarButton.setOnAction(event -> close());

        // Layout
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(15);
        gridPane.add(clienteLabel, 0, 0);
        gridPane.add(clienteComboBox, 1, 0);
        gridPane.add(pagamentoLabel, 0, 1);
        gridPane.add(pagamentoComboBox, 1, 1);

        HBox buttonBox = new HBox(15, cancelarButton, confirmarButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox vbox = new VBox(20, titleLabel, gridPane, buttonBox);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));
        vbox.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: #34495e; -fx-border-width: 2px; -fx-border-radius: 10px; -fx-background-radius: 10px;");

        // Cena
        Scene scene = new Scene(vbox, 400, 250);
        setScene(scene);
    }

    public ComboBox<ClienteModel> getClienteComboBox() {
        return clienteComboBox;
    }

    public ComboBox<String> getPagamentoComboBox() {
        return pagamentoComboBox;
    }

    @SuppressWarnings("exports")
	public Button getConfirmarButton() {
        return confirmarButton;
    }

    public void showAndWait() {
        super.showAndWait();
    }
}
