module fx_lanchonete {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.desktop;
	requires java.sql;
	
	opens gui to javafx.fxml;
	
	opens model to javafx.base;

    exports gui; 
    exports model;
	opens application to javafx.graphics, javafx.fxml;
}
