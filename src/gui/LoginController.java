package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private Button loginButton;
    
    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private Label errorMessage;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        if (authenticate(username, password)) {
            System.out.println("Login bem-sucedido");
        } else {
            errorMessage.setText("Nome de usuário ou senha inválidos");
        }
    }

    private boolean authenticate(String username, String password) {
        return "admin".equals(username) && "1234".equals(password);
    }
}
