package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {
	

	    @Override
	    public void start(Stage primaryStage) {
	        try {
	            // Carrega o FXML principal
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Principal.fxml"));
	        	//FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Produto.fxml"));
	            Parent root = loader.load();
	            
	            // Configura a cena e o palco principal
	            Scene scene = new Scene(root);
	            scene.getStylesheets().add(getClass().getResource("/gui/styles.css").toExternalForm());
	            primaryStage.setScene(scene);
	            primaryStage.setTitle("Sistema de PDV");
	            primaryStage.setMaximized(true);  // Abre a janela maximizada
	            primaryStage.show();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    public static void main(String[] args) {
	        launch(args);
	    }

}
