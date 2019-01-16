import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
public class Cone {
	Connection conexion = null;
    Statement comando = null;
    ResultSet registro;
    @SuppressWarnings("finally")
	public Connection MySQLConnect() {
    	try {
    		Class.forName("com.mysql.jdbc.Driver");
    		String servidor="jdbc:mysql://localhost:3306/prueba";
    		String usuario="root";
    		String pass="";
    		conexion = DriverManager.getConnection(servidor, usuario, pass);
    		
    	}catch(ClassNotFoundException ex) {
    		 JOptionPane.showMessageDialog(null, ex, "Error en la conexión a la base de datos: " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
             conexion = null;
    	}catch(SQLException ex) {
    		JOptionPane.showMessageDialog(null, ex, "Error en la conexión a la base de datos: " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            conexion = null;
    	}catch(Exception ex) {
    		JOptionPane.showMessageDialog(null, ex, "Error en la conexión a la base de datos: " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            conexion = null;
    	}finally {
    		 JOptionPane.showMessageDialog(null, "Conexión Exitosa");
             return conexion;
    	}
    }
}
