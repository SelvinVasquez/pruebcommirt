package clases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBaseDeDatos {

    private static final String URL = "jdbc:mysql://localhost:3306/finalprogra";
    private static final String USUARIO = "root";
    private static final String CONTRASEÑA = "selvinloot";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CONTRASEÑA);
}
}