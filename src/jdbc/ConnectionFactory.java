package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactory {

	private final static String urlComplemento = "?useSSL=false&useTimezone=true&serverTimezone=America/Sao_Paulo&characterEncoding=UTF-8";
    private static final String URL = "jdbc:mysql://localhost:3306/lanchonete" + urlComplemento;
    private static final String USER = "rivaldo";
    private static final String PASSWORD = "153111";
	private final static String driver = "com.mysql.jdbc.Driver";

	
    public static  Connection getConnection() {
        try {
        	Class.forName(driver);
        	return DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (Exception erro) {
			throw new RuntimeException(erro);
		}
    }
    
}
